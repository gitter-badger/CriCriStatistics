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

    public ArrayList<HashMap<String, Integer>> phases_count;
    public ArrayList<HashMap<String, Float>> phases_freq;
    public ArrayList<HashMap<String, Integer>> pref_frames;

    // Constructor
    public WrittenStats() {
        this.phases_count = new ArrayList<HashMap<String, Integer>>(3);
        this.phases_freq = new ArrayList<HashMap<String, Float>>(3);
        this.pref_frames = new ArrayList<HashMap<String, Integer>>(3);

        for (int i=0; i<3; i++) {
            phases_count.add(new HashMap<String, Integer>());
            phases_freq.add(new HashMap<String, Float>());
            pref_frames.add(new HashMap<String, Integer>());
        }
    }

    // Methods
    public void addCount(int phase, String key, int count) {
        this.phases_count.get(phase).put(key, count);
    }

    public void addFreq(int phase, String key, float freq) {
        this.phases_freq.get(phase).put(key, freq);
    }

    public void addPref(int phase, String key, int count) {
        this.pref_frames.get(phase).put(key, count);
    }
}
