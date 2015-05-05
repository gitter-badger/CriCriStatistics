import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import org.apache.commons.io.FileUtils;

public class GroupedStats {

    // Attributes
    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();

    public static HashMap<String, ArrayList<WrittenStats>> kingdomHash = new HashMap<String, ArrayList<WrittenStats>>();
    public static HashMap<String, ArrayList<WrittenStats>> groupHash = new HashMap<String, ArrayList<WrittenStats>>();
    public static HashMap<String, ArrayList<WrittenStats>> subgroupHash = new HashMap<String, ArrayList<WrittenStats>>();

    // Constructor
    public GroupedStats() {
    }

    // Methods
    public static void ensureNoDuplicate(ArrayList<WrittenStats> array, String name) {

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).organism.equals(name)) {
                array.remove(i);
                break;
            }
        }
    }

    public static void updateGroupedStats() {

        // Init
        ExcelReader er = new ExcelReader();
        String outputDir = Settings.getInstance().getOutputDir();
        String[] extensions = {"xls", "xlsx"};
        Collection<File> list = FileUtils.listFiles(new File(outputDir), extensions, true);

        int old_size = mediatorGUI.getBarSize();
        int old_percentage = mediatorGUI.getProgress();
        mediatorGUI.setProgressBar(list.size());
        mediatorGUI.setProgress(0);

        ArrayList<String> kingdoms = new ArrayList<String>();

        for (File file : list) {
            try {
                // For each found file, read it and get the contents
                er.setInputFile(file);
                WrittenStats ws = er.read();
                mediatorGUI.incrementProgressBar();

                if (ws == null) continue;

                // ... then add these contents into kingdom, group, and subgroup lists
                ArrayList<WrittenStats> kingdom = kingdomHash.get(ws.kingdom);
                if (kingdom == null) {
                    kingdom = new ArrayList<WrittenStats>();
                    kingdomHash.put(ws.kingdom, kingdom);
                    kingdoms.add(ws.kingdom);
                //} else {
                //    ensureNoDuplicate(kingdom, ws.organism);
                }
                kingdom.add(ws);

                ArrayList<WrittenStats> group = groupHash.get(ws.group);
                if (group == null) {
                    group = new ArrayList<WrittenStats>();
                    groupHash.put(ws.group, group);
                //} else {
                //    ensureNoDuplicate(group, ws.organism);
                }
                group.add(ws);

                ArrayList<WrittenStats> subgroup = subgroupHash.get(ws.subgroup);
                if (subgroup == null) {
                    subgroup = new ArrayList<WrittenStats>();
                    subgroupHash.put(ws.subgroup, subgroup);
                //} else {
                //    ensureNoDuplicate(subgroup, ws.organism);
                }
                subgroup.add(ws);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mediatorGUI.setProgressBar(kingdomHash.size() + groupHash.size() + subgroupHash.size());
        mediatorGUI.setProgress(0);

        ExcelWriter.writeKingdoms(kingdomHash);
        ExcelWriter.writeGroups(groupHash);
        ExcelWriter.writeSubgroups(subgroupHash);

        ArrayList<WrittenStats> ks = new ArrayList<WrittenStats>();

        for (String kd : kingdoms) {

            try {
                String checked_kd = ExcelWriter.verifyString(kd);
                String outputFile = Settings.getInstance().getOutputDir() + File.separator
                    + checked_kd + File.separator + checked_kd + ".xls";

                er.setInputFile(outputFile);
                WrittenStats ws = er.readGrouped();

                if (ws != null) ks.add(ws);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExcelWriter.writeGlobal(ks);

        if (old_size > 0) {
            mediatorGUI.setProgressBar(old_size);
            mediatorGUI.setProgress(old_percentage);
        }
    }
}
