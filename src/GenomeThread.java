import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.security.MessageDigest;

public class GenomeThread implements Runnable
{
    protected SAXParser saxParser;

    protected HandlerGenomeId handlerGenomeId = new HandlerGenomeId();
    protected HandlerSequenceId handlerSequenceId = new HandlerSequenceId();

    protected List<Genome> genomeList;

    protected String threadName;
    
    protected DebugOption debugOption;
    protected DatabaseModule db;

    private IGenomeParser genomeParser;

    public GenomeThread(String threadName, SAXParser parser, List<Genome> genomeList,DatabaseModule db, IGenomeParser genomeParser) {
        
        this.debugOption = null;
        this.db = db;
        this.threadName = threadName;
        this.genomeList = genomeList;
        this.genomeParser = genomeParser;

        try {
            saxParser = parser;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    { 
        
        IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
        for (final Genome genome: genomeList) {
            if (genomeParser != null){
                genomeParser.parseGenome(genome, getGenomeGenbanks(genome.getOrganism(), genome));
                mediatorGUI.incrementProgressBar();
            }
            else
                getGenomeGenbanks(genome.getOrganism(), genome);
        }
    }

    private InputStream getParseInputSource(String urlString)
    {
        try {
            URLConnection urlConnection = new URL(urlString).openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            return urlConnection.getInputStream();
        } catch (Exception e) {
            IOException ioe = new IOException("Couldn't open " + urlString);
            ioe.initCause(e);
            System.out.println(ioe);
            return null;
        }
    }
    
    public void setDebuggingOption(DebugOption debugOption){
      this.debugOption = debugOption;
    }

    private List<Scanner> getGenomeGenbanks(String genomeName, Genome genome)
    {
        // bug fix :
        // exception raised when we got : in URLs (genome name) like Cellulophaga phage phi19:1
        genomeName = genomeName.replaceAll(":", " ");

        IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
        try {
            List<Scanner> cdsList = new ArrayList<Scanner>();

            synchronized(saxParser)
            {
                try {
                    InputStream genomeIdIS = getParseInputSource("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=genome&term="
                            + URLEncoder.encode(genomeName,java.nio.charset.StandardCharsets.UTF_8.toString()));
          
                    if (genomeIdIS != null)
                    {
                        saxParser.parse(genomeIdIS, handlerGenomeId);

                        genome.setId(Integer.parseInt(handlerGenomeId.getGenomeId()));                    
                        
                        if(this.db.genomeEntryExist(genome.getId()))
                          return null;

                        InputStream sequenceIdIS = getParseInputSource("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=genome&db=nuccore&id=" + handlerGenomeId.getGenomeId());
                          
                        MessageDigest md = MessageDigest.getInstance("SHA-256");

                        if (sequenceIdIS != null)
                        {
                            saxParser.parse(sequenceIdIS, handlerSequenceId);
                        }
                        else
                        {
                            mediatorGUI.updateAquisitionPanel("sequenceIdIS null   "+handlerGenomeId.getGenomeId());
                        }
                    }
                    else
                    {
                        mediatorGUI.updateAquisitionPanel("genomeIdIS null   " + genomeName);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            for (final String sequenceId : handlerSequenceId.getSequenceIdList()) {
                try {
                    URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&rettype=gb&retmode=text&id=" + sequenceId);
                    Scanner s = new Scanner(url.openStream());
                    cdsList.add(s);
                    mediatorGUI.updateAquisitionPanel("      *"+s.nextLine());
                    
                    if( debugOption != null && debugOption.isGBLoggingActivated() ){
                        File sequenceDir = new File("sequenceDir");
                        sequenceDir.mkdir();
                        FileWriter fw = new FileWriter("sequenceDir/"+genomeName+sequenceId);
                    
                        while (s.hasNextLine())
                        {
                            fw.write(s.nextLine()+'\n');
                        }
                        fw.close();
                        // read from your scanner
                    }

                } catch (IOException ex) {
                    // there was some connection problem, or the file did not exist on the server,
                    // or your URL was not in the right format.
                    // think about what to do now, and put it here.
                    System.out.println(ex);
                }
            }

            return cdsList;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
}
