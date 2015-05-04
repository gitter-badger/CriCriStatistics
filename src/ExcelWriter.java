import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.HashMap;

import jxl.CellReferenceHelper;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter {

    // Attributes
    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
    private static Settings settings = Settings.getInstance();
//    private static int count_missing_data = 0;

    private static WritableCellFormat arial;
    private static WritableCellFormat twodigit;
    private String outputFile;
    private Statistics statistic;
    private static String NOM = "Nom";
    private static String CHEMIN = "Chemin";
    private static String NB_TRINU = "Nb trinucléotides";
    private static String NB_CDS = "Nb CDS";
    private static String NB_CDS_NON_TRAITES = "Nb CDS non traites";
    private static String TRINUCLEOTIDES = "Trinucléotides";
    private static String TOTAL = "Total";

    // Constructor
    public ExcelWriter() {
    }

    // Methods
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setStatistics(Statistics stats) {
        this.statistic = stats;
        this.outputFile = buildOutputFilePath(stats.genome);
    }

    public void write()
            throws IOException, WriteException {
//        System.out.println("File: " + this.outputFile);
        File file = new File(this.outputFile);
        file.createNewFile();

        WritableWorkbook workbook;
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Statistics", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet);

        workbook.write();
        workbook.close();
    }

    /* Ensure we do not write files with forbidden of annoying characters. */
    public static String verifyString(String text) {
        return text.replaceAll("[?:!/*<>]+", "_");
    }

    public static String buildOutputDirPath(Genome genome) {
        if (genome.getKingdom() != null
                && genome.getGroup() != null
                && genome.getSubGroup() != null) {

            String outputDir = verifyString(genome.getKingdom()) + File.separator
                    + verifyString(genome.getGroup()) + File.separator
                    + verifyString(genome.getSubGroup()) + File.separator;

            return settings.getOutputDir() + File.separator + outputDir;
        }
        else {
            return settings.getOutputDir() + File.separator + "_Missing_Data" + File.separator;
        }
    }

    public static String buildOutputFilePath(Genome genome) {

        return buildOutputFilePath(genome, buildOutputDirPath(genome));
    }

    public static String buildOutputFilePath(Genome genome, String dirpath) {

        return buildOutputFilePath(genome, dirpath, "xls");
    }

    public static String buildOutputFilePath(Genome genome, String dirpath, String ext) {

        if (genome.getOrganism() != null) {
            return dirpath + verifyString(genome.getOrganism()) + "." + ext;
        }
        else {
            return null;
            // We cannot use this as count_missing_data will be reinitialized to 0
            // if we relaunch the program
//            count_missing_data++;
//            return dirpath + "_no_organism-" + count_missing_data + ".xls";
        }
    }

    /* Initialize fonts and number formats. */
    private static void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create an arial font
        WritableFont arial10pt = new WritableFont(WritableFont.ARIAL, 10);
        // Define the cell format
        arial = new WritableCellFormat(arial10pt);
        // Lets automatically wrap the cells
        arial.setAlignment(Alignment.CENTRE);
        arial.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(arial);
        cv.setAutosize(true);
        for (int i = 0; i < 7; i++) {
            sheet.setColumnView(i, cv);
        }

        twodigit = new WritableCellFormat(
                new WritableFont(WritableFont.ARIAL), new NumberFormat("#0.00"));
        // Lets automatically wrap the cells
        twodigit.setAlignment(Alignment.CENTRE);
        twodigit.setWrap(true);
    }

    private void createContent(WritableSheet sheet)
            throws WriteException, RowsExceededException {


        mediatorGUI.updateWritingPanel("Writing Excel file for organism " + statistic.genome.getOrganism());
        addLabel(sheet, 0, 0, NOM);
        addLabel(sheet, 1, 0, statistic.genome.getOrganism());
        addLabel(sheet, 0, 1, CHEMIN);
        int i = 0;
        if (statistic.genome.getKingdom() != null) {
            addLabel(sheet, ++i, 1, statistic.genome.getKingdom());
        }
        if (statistic.genome.getGroup() != null) {
            addLabel(sheet, ++i, 1, statistic.genome.getGroup());
        }
        if (statistic.genome.getSubGroup() != null) {
            addLabel(sheet, ++i, 1, statistic.genome.getSubGroup());
        }
        if (statistic.genome.getOrganism() != null) {
            addLabel(sheet, ++i, 1, statistic.genome.getOrganism());
        }

        addLabel(sheet, 0, 2, NB_CDS);
        addNumber(sheet, 1, 2, statistic.genome.getNbCorrectCDS() + statistic.genome.getNbFailedCDS());
        addLabel(sheet, 0, 3, NB_TRINU);
        addNumber(sheet, 1, 3, statistic.total_n_nucleotides);
        addLabel(sheet, 0, 4, NB_CDS_NON_TRAITES);
        addNumber(sheet, 1, 4, statistic.genome.getNbFailedCDS());
        addLabel(sheet, 0, 6, TRINUCLEOTIDES);
        int k = 7;
        addLabel(sheet, 1, 6, "Nb Ph0");
        addLabel(sheet, 2, 6, "Pb Ph0");
        addLabel(sheet, 3, 6, "Nb Ph1");
        addLabel(sheet, 4, 6, "Pb Ph1");
        addLabel(sheet, 5, 6, "Nb Ph2");
        addLabel(sheet, 6, 6, "Pb Ph2");

        Map<String, Integer> treeMap = new TreeMap<String, Integer>(statistic.phases.get(0));

        for (Entry<String, Integer> entry : treeMap.entrySet()) {

            String label = entry.getKey();
            addLabel(sheet, 0, k, label.toUpperCase());
            int nb = 1;
            for (int j = 0; j < statistic.phases.size(); j++) {

                float percent = (float) (((float) (statistic.phases.get(j).get(label))) / ((float) (statistic.total_n_nucleotides))) * 100;
                addNumber(sheet, nb, k, statistic.phases.get(j).get(label));
                addReal(sheet, nb + 1, k, percent);
                nb += 2;
            }
            k++;
        }

        addLabel(sheet, 0, k, TOTAL);

        for (int j = 0; j < 2 * statistic.phases.size(); j++) {
            StringBuffer buf = new StringBuffer();

            buf.append("ROUND(SUM(" + CellReferenceHelper.getColumnReference(j + 1) + "8:"
                    + CellReferenceHelper.getColumnReference(j + 1) + (k) + "),3)");
            Formula f = new Formula(j + 1, k, buf.toString());

            sheet.addCell(f);
        }

    }

    private static void createGroupedContent(WritableSheet sheet, ArrayList<WrittenStats> stats, String name)
            throws WriteException, RowsExceededException {
        int totalTrinu = 0;
        int totalCDS = 0;
        int totalCDSn = 0;

        addLabel(sheet, 0, 0, NOM);
        addLabel(sheet, 1, 0, name);
        addLabel(sheet, 0, 1, CHEMIN);

        addLabel(sheet, 1, 6, "Nb Ph0");
        addLabel(sheet, 2, 6, "Pb Ph0");
        addLabel(sheet, 3, 6, "Nb Ph1");
        addLabel(sheet, 4, 6, "Pb Ph1");
        addLabel(sheet, 5, 6, "Nb Ph2");
        addLabel(sheet, 6, 6, "Pb Ph2");

        for (int m = 0; m < stats.size(); m++) {
            totalCDS = totalCDS + stats.get(m).nb_cds;
            totalCDSn = totalCDSn + stats.get(m).nb_wrong_cds;
            totalTrinu = totalTrinu + stats.get(m).nb_tri;
        }

        addLabel(sheet, 0, 2, NB_CDS);
        addNumber(sheet, 1, 2, totalCDS);
        addLabel(sheet, 0, 3, NB_TRINU);
        addNumber(sheet, 1, 3, totalTrinu);
        addLabel(sheet, 0, 4, NB_CDS_NON_TRAITES);
        addNumber(sheet, 1, 4, totalCDSn);
        addLabel(sheet, 0, 6, TRINUCLEOTIDES);

        Map<String, Integer> treeMap = new TreeMap<String, Integer>(stats.get(0).phases_count.get(0));

        int k = 7;
        for (Entry<String, Integer> entry : treeMap.entrySet()) {
            String label = entry.getKey();
            addLabel(sheet, 0, k, label.toUpperCase());

            int nb = 1;
            for (int j = 0; j < stats.get(0).phases_count.size(); j++) {
                int ntotatl = 0;
               
                for (int m = 0; m < stats.size(); m++) {
//                    System.out.println("Label: " + label);
                    ntotatl = ntotatl + stats.get(m).phases_count.get(j).get(label);
                }
                addNumber(sheet, nb, k, ntotatl);
                float percent = ((float) ntotatl) / ((float) totalTrinu) * 100;
                addReal(sheet, nb + 1, k, percent);
                nb += 2;

            }
            k++;
        }

        addLabel(sheet, 0, k, TOTAL);
        for (int j = 0; j < 2 * stats.get(0).phases_count.size(); j++) {
            StringBuffer buf = new StringBuffer();

            buf.append("ROUND(SUM(" + CellReferenceHelper.getColumnReference(j + 1) + "8:"
                    + CellReferenceHelper.getColumnReference(j + 1) + (k) + "),3)");
            Formula f = new Formula(j + 1, k, buf.toString());

            sheet.addCell(f);
        }


    }

    /* Write a number as an integer at the given position. */
    private static void addNumber(WritableSheet sheet, int column, int row,
            Integer integer) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, integer, arial);
        sheet.addCell(number);
    }

    /* Write a number as a float at the given position. 
    * The whole number is written but only 2 decimals are displayed. */
    private static void addReal(WritableSheet sheet, int column, int row,
            Float fl) throws WriteException, RowsExceededException {

        Number number;
        number = new Number(column, row, fl, twodigit);
        sheet.addCell(number);
    }

    /* Write a label (text) at the given position. */
    private static void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, arial);
        sheet.addCell(label);
    }

    public static void writeKingdoms(HashMap<String, ArrayList<WrittenStats>> kingdomHash) {
        String outputDir = Settings.getInstance().getOutputDir();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        for (Map.Entry<String, ArrayList<WrittenStats>> entry : kingdomHash.entrySet()) {

            try {
                File file = new File(outputDir + File.separator
                        + verifyString(entry.getKey()) + File.separator
                        + verifyString(entry.getKey()) + ".xls");
                file.createNewFile();

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Statistics", 0);
                WritableSheet excelSheet = workbook.getSheet(0);
                createLabel(excelSheet);
                int i = 0;
                if (entry.getValue().get(0).kingdom != null) {
                    mediatorGUI.updateWritingPanel("Writing Excel file for kingdom " + entry.getValue().get(0).kingdom);
                    addLabel(excelSheet, ++i, 1, entry.getValue().get(0).kingdom);
                }

                createGroupedContent(excelSheet, entry.getValue(), entry.getKey());
                workbook.write();
                workbook.close();
                
                mediatorGUI.incrementProgressBar();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeGroups(HashMap<String, ArrayList<WrittenStats>> groupHash) {
        String outputDir = Settings.getInstance().getOutputDir();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        for (Map.Entry<String, ArrayList<WrittenStats>> entry : groupHash.entrySet()) {

            try {
                File file = new File(outputDir + File.separator
                        + verifyString(entry.getValue().get(0).kingdom) + File.separator
                        + verifyString(entry.getKey()) + File.separator
                        + verifyString(entry.getKey()) + ".xls");
                file.createNewFile();

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Statistics", 0);
                WritableSheet excelSheet = workbook.getSheet(0);
                createLabel(excelSheet);

                int i = 0;
                if (entry.getValue().get(0).kingdom != null) {
                    addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).kingdom);
                }
                if (entry.getValue().get(0).group != null) {
                    mediatorGUI.updateWritingPanel("Writing Excel file for group " + entry.getValue().get(0).group);
                    addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).group);
                }

                createGroupedContent(excelSheet, entry.getValue(), entry.getKey());
                workbook.write();
                workbook.close();
                
                mediatorGUI.incrementProgressBar();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeSubgroups(HashMap<String, ArrayList<WrittenStats>> subgroupHash) {
        String outputDir = Settings.getInstance().getOutputDir();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        for (Map.Entry<String, ArrayList<WrittenStats>> entry : subgroupHash.entrySet()) {

            try {
                File file = new File(outputDir + File.separator
                        + verifyString(entry.getValue().get(0).kingdom) + File.separator
                        + verifyString(entry.getValue().get(0).group) + File.separator
                        + verifyString(entry.getKey()) + File.separator
                        + verifyString(entry.getKey()) + ".xls");
                file.createNewFile();

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Statistics", 0);
                WritableSheet excelSheet = workbook.getSheet(0);
                createLabel(excelSheet);

                int i = 0;
                if (entry.getValue().get(0).kingdom != null) {
                    addLabel(excelSheet, ++i, 1, entry.getValue().get(0).kingdom);
                }
                if (entry.getValue().get(0).group != null) {
                    addLabel(excelSheet, ++i, 1, entry.getValue().get(0).group);
                }
                if (entry.getValue().get(0).subgroup != null) {
                    mediatorGUI.updateWritingPanel("Writing Excel file for subgroup " + entry.getValue().get(0).subgroup);
                    addLabel(excelSheet, ++i, 1, entry.getValue().get(0).subgroup);
                }

                createGroupedContent(excelSheet, entry.getValue(), entry.getKey());
                workbook.write();
                workbook.close();

                mediatorGUI.incrementProgressBar();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }
}
