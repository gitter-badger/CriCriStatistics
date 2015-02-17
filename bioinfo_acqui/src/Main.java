import javax.swing.*;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    public static void main(String argv[]) {

        // String sequence = "ACGTAAACAGATCACCCGCTGAT";
        // Alphabet a4 = new Alphabet();
        // Statistics stats = new Statistics(a4, sequence);
        // stats.phases.get(0).get("GTA");       
        
        try {
            JFrame frame = new MainForm();
            System.setProperty("java.net.useSystemProxies", "true");

            GenomeOverview genomeOverview = new GenomeOverview();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            List<Genome> genomeList = genomeOverview.getGenomeList();

            int noOfThreads = 8;
            List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();

            for (int i = 0; i < noOfThreads; i++)
            {
                GenomeThread genomeThread = new GenomeThread(String.valueOf(i), factory.newSAXParser(),
                        genomeList.subList(i*(genomeList.size()/noOfThreads),
                        (i+1)*(genomeList.size()/noOfThreads)), null);
                genomeThreads.add(genomeThread);
            }

            for (int i = 0; i < noOfThreads; i++) {
                Thread thread = new Thread(genomeThreads.get(i));
                thread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



