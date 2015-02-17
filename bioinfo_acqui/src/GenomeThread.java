import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenomeThread implements Runnable
{
    protected SAXParser saxParser;

    protected HandlerGenomeId handlerGenomeId = new HandlerGenomeId();
    protected HandlerSequenceId handlerSequenceId = new HandlerSequenceId();

    protected List<Genome> genomeList;

    protected String threadName;

    private IGenomeParser genomeParser;

    public GenomeThread(String threadName, SAXParser parser, List<Genome> genomeList, IGenomeParser genomeParser) {

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
        for (final Genome genome: genomeList) {
            if (genomeParser != null)
                genomeParser.parseGenome(genome, getGenomeGenbanks(genome.getOrganism()));
            else
                getGenomeGenbanks(genome.getOrganism());
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

    private List<Scanner> getGenomeGenbanks(String genomeName)
    {
        try {
            List<Scanner> cdsList = new ArrayList<Scanner>();

            synchronized(saxParser)
            {
                try {
                    InputStream genomeIdIS = getParseInputSource("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=genome&term=" + genomeName);

                    if (genomeIdIS != null)
                    {
                        saxParser.parse(genomeIdIS, handlerGenomeId);

                        InputStream sequenceIdIS = getParseInputSource("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=genome&db=nuccore&id=" + handlerGenomeId.getGenomeId());

                        if (sequenceIdIS != null)
                        {
                            saxParser.parse(sequenceIdIS, handlerSequenceId);
                        }
                    }
                    //TODO: error handling (else)

                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            for (final String sequenceId : handlerSequenceId.getSequenceIdList()) {
                try {
                    URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&rettype=gb&retmode=text&id=" + sequenceId);
                    Scanner s = new Scanner(url.openStream());
                    cdsList.add(s);
                    System.out.println("      *"+s.nextLine());
//
//                    FileWriter fw = new FileWriter(genomeName+sequenceId);
//                    while (s.hasNextLine())
//                    {
//                        fw.write(s.nextLine()+'\n');
//                    }
//                    fw.close();
                    // read from your scanner
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
