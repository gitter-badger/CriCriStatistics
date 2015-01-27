import java.util.ArrayList;
import java.util.List;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    public static void main(String argv[]) {

        try {
            GenomeOverview genomeOverview = new GenomeOverview();

            List<Genome> genomeList = genomeOverview.getGenomeList();

            int noOfThreads = 4;
            List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();

            for (int i = 0; i < noOfThreads; i++)
            {
                GenomeThread genomeThread = new GenomeThread(genomeList.subList(i*(genomeList.size()/noOfThreads),
                        i+1*(genomeList.size()/noOfThreads)));
                genomeThreads.add(genomeThread);
                genomeThread.run();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

