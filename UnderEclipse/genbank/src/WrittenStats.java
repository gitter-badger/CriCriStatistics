import java.util.ArrayList;
import java.util.HashMap;

public class WrittenStats {

    // Attributes
    public String kingdom;
    public String group;
    public String subgroup;
    public String organism;

    public int nb_cds;
    public int nb_tri;
    public int nb_wrong_cds;

    public ArrayList<HashMap<String, Integer>> phases;

    public WrittenStats() {
        this.phases = new ArrayList<HashMap<String, Integer>>(3);;
        for (int i=0; i<3; i++) {
            phases.add(new HashMap<String, Integer>());
        }
    }

    public void addCount(int phase, String key, int count) {
        this.phases.get(phase).put(key, count);
    }

}