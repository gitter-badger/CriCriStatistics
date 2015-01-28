import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
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

    public GenomeThread(String threadName, SAXParser parser, List<Genome> genomeList) {

        this.threadName = threadName;
        this.genomeList = genomeList;

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
            System.out.print(this.threadName + '\t');
            getGenomeCDS(genome.getOrganism());
        }
    }

    private synchronized List<Scanner> getGenomeCDS(String genomeName)
    {
        try {
            List<Scanner> cdsList = new ArrayList<Scanner>();

            saxParser.parse("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=genome&term=" + genomeName,
                    handlerGenomeId);
            saxParser.parse("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=genome&db=nuccore&id=" + handlerGenomeId.getGenomeId(),
                    handlerSequenceId);

            for (final String sequenceId : handlerSequenceId.getSequenceIdList()) {
                try {
                    URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&rettype=gb&retmode=text&id=" + sequenceId);
                    Scanner s = new Scanner(url.openStream());
                    cdsList.add(s);
                    System.out.println(s.nextLine());
                    // read from your scanner
                } catch (IOException ex) {
                    // there was some connection problem, or the file did not exist on the server,
                    // or your URL was not in the right format.
                    // think about what to do now, and put it here.
                    ex.printStackTrace(); // for now, simply output it.
                }
            }

            return cdsList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
