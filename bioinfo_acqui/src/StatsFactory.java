
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import jxl.write.WriteException;

public class StatsFactory {

    HashMap<String, ArrayList<Statistics>> kingdomHash = new HashMap<String, ArrayList<Statistics>>();
    HashMap<String, ArrayList<Statistics>> groupHash = new HashMap<String, ArrayList<Statistics>>();
    HashMap<String, ArrayList<Statistics>> subgroupHash = new HashMap<String, ArrayList<Statistics>>();

    public void new_stats(Alphabet alphabet, Vector<String> seq, Genome genome)
            throws IOException, WriteException {
        Statistics stats = new Statistics(alphabet, seq, genome);
        stats.print();
        Statistics.Write(stats);
        stats.tagAsDone(); 

        if (!kingdomHash.containsKey(stats.genome.getKingdom())) {
            ArrayList<Statistics> stat = new ArrayList<Statistics>();
            stat.add(stats);
            kingdomHash.put(stats.genome.getKingdom(), stat);
        } else {
            ArrayList<Statistics> stat;
            stat = kingdomHash.get(stats.genome.getKingdom());
            stat.add(stats);
        }

        if (!groupHash.containsKey(stats.genome.getGroup())) {
            ArrayList<Statistics> stat = new ArrayList<Statistics>();
            stat.add(stats);
            groupHash.put(stats.genome.getGroup(), stat);
        } else {
            ArrayList<Statistics> stat;
            stat = groupHash.get(stats.genome.getGroup());
            stat.add(stats);
        }

        if (!subgroupHash.containsKey(stats.genome.getSubGroup())) {
            ArrayList<Statistics> stat = new ArrayList<Statistics>();
            stat.add(stats);
            subgroupHash.put(stats.genome.getSubGroup(), stat);
        } else {
            ArrayList<Statistics> stat;
            stat = subgroupHash.get(stats.genome.getSubGroup());
            stat.add(stats);
        }

    }
}
