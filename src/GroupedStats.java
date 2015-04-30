import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.FileUtils;

public class GroupedStats {

    // Attributes
    public static HashMap<String, ArrayList<WrittenStats>> kingdomHash;
    public static HashMap<String, ArrayList<WrittenStats>> groupHash;
    public static HashMap<String, ArrayList<WrittenStats>> subgroupHash;

    // Constructor
    public GroupedStats() {
        this.kingdomHash = new HashMap<String, ArrayList<WrittenStats>>();
        this.groupHash = new HashMap<String, ArrayList<WrittenStats>>();
        this.subgroupHash = new HashMap<String, ArrayList<WrittenStats>>();
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

        for (File file : list) {
//            System.out.println("Reading file " + file);
            try {
                // For each found file, read it and get the contents
                er.setInputFile(file);
                WrittenStats ws = er.read();

                if (ws == null) continue;

                // ... then add these contents into kingdom, group, and subgroup lists
                ArrayList<WrittenStats> kingdom = kingdomHash.get(ws.kingdom);
                if (kingdom == null) {
                    kingdom = new ArrayList<WrittenStats>();
                    kingdomHash.put(ws.kingdom, kingdom);
                } else {
                    ensureNoDuplicate(kingdom, ws.organism);
                }
                kingdom.add(ws);

                ArrayList<WrittenStats> group = groupHash.get(ws.group);
                if (group == null) {
                    group = new ArrayList<WrittenStats>();
                    groupHash.put(ws.group, group);
                } else {
                    ensureNoDuplicate(group, ws.organism);
                }
                group.add(ws);

                ArrayList<WrittenStats> subgroup = subgroupHash.get(ws.subgroup);
                if (subgroup == null) {
                    subgroup = new ArrayList<WrittenStats>();
                    subgroupHash.put(ws.subgroup, subgroup);
                } else {
                    ensureNoDuplicate(subgroup, ws.organism);
                }
                subgroup.add(ws);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExcelWriter.writeKingdoms(kingdomHash);
        ExcelWriter.writeGroups(groupHash);
        ExcelWriter.writeSubgroups(subgroupHash);
    }
}
