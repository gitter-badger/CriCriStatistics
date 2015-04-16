import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import jxl.CellReferenceHelper;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String outputFile;
    private Statistics statistic;
    private final String NOM = "Nom";
    private final String CHEMIN = "Chemin";
    private final String NB_TRINU = "Nb trinucléotides";
    private final String NB_CDS = "Nb CDS";
    private final String NB_CDS_NON_TRAITES = "Nb CDS non traites";
    private final String TRINUCLEOTIDES = "Trinucléotides";
    private final String TOTAL = "Total";

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public ExcelWriter(Statistics stat) {
        statistic = stat;

    }

    public ExcelWriter() {
    }

    public void write()
            throws IOException, WriteException {
        File file = new File(outputFile);
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

    private static String verifyString(String text) {
        return text.replaceAll("[?:!/*<>]+", "_");
    }


        String homeDirectory = System.getProperty("user.home");

        String absoluteFilePath = "";

        absoluteFilePath = homeDirectory + File.separator + "Statistics" + File.separator;


        for (Map.Entry<String, ArrayList<Statistics>> entry : StatsFactory.getKindomMap().entrySet()) {

            File file = new File(absoluteFilePath + File.separator + verifyString(entry.getKey()) + File.separator + verifyString(entry.getKey()) + ".xls");
            file.createNewFile();

            WritableWorkbook workbook;
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Statistics", 0);
            WritableSheet excelSheet = workbook.getSheet(0);
            createLabel(excelSheet);
            int i = 0;
            if (entry.getValue().get(0).genome.getKingdom() != null) {
                addLabel(excelSheet, ++i, 1, entry.getValue().get(0).genome.getKingdom());
            }

            createContent2(excelSheet, entry.getValue(), entry.getKey());
            workbook.write();
            workbook.close();
        }


        for (Map.Entry<String, ArrayList<Statistics>> entry : StatsFactory.getGroupMap().entrySet()) {

            File file = new File(absoluteFilePath + File.separator
                    + File.separator + verifyString(entry.getValue().get(0).genome.getKingdom())
                    + File.separator + verifyString(entry.getKey()) + File.separator + verifyString(entry.getKey()) + ".xls");
            file.createNewFile();

            WritableWorkbook workbook;
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Statistics", 0);
            WritableSheet excelSheet = workbook.getSheet(0);
            createLabel(excelSheet);

            int i = 0;
            if (entry.getValue().get(0).genome.getKingdom() != null) {
                addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).genome.getKingdom());
            }
            if (entry.getValue().get(0).genome.getGroup() != null) {
                addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).genome.getGroup());
            }

            createContent2(excelSheet, entry.getValue(), entry.getKey());

            workbook.write();
            workbook.close();
        }

        for (Map.Entry<String, ArrayList<Statistics>> entry : StatsFactory.getSubGroupMap().entrySet()) {

            File file = new File(absoluteFilePath + File.separator
                    + File.separator + verifyString(entry.getValue().get(0).genome.getKingdom())
                    + File.separator + verifyString(entry.getValue().get(0).genome.getGroup()) + File.separator + verifyString(entry.getKey()) + File.separator
                    + verifyString(entry.getKey()) + ".xls");
            file.createNewFile();

            WritableWorkbook workbook;
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Statistics", 0);
            WritableSheet excelSheet = workbook.getSheet(0);
            createLabel(excelSheet);

            int i = 0;
            if (entry.getValue().get(0).genome.getKingdom() != null) {
                addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).genome.getKingdom());
            }
            if (entry.getValue().get(0).genome.getGroup() != null) {
                addLabel(excelSheet, ++i, 1,  entry.getValue().get(0).genome.getGroup());
            }
            if (entry.getValue().get(0).genome.getSubGroup() != null) {
                addLabel(excelSheet, ++i, 1, entry.getValue().get(0).genome.getSubGroup());
            }


            createContent2(excelSheet, entry.getValue(), entry.getKey());
            workbook.write();
            workbook.close();
        }
    }

    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setAlignment(Alignment.CENTRE);
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);
        for (int i = 0; i < 7; i++) {
            sheet.setColumnView(i, cv);
        }


    }

    private void createContent(WritableSheet sheet) throws WriteException,
            RowsExceededException {

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
                addReal(sheet, nb, k, (float) (statistic.phases.get(j).get(label)));
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

    private void createContent2(WritableSheet sheet, ArrayList<Statistics> stats, String name) throws WriteException,
            RowsExceededException {
        int totalTrinu = 0;
        int totalCDS = 0;
        int totalCDSn = 0;

        addLabel(sheet, 0, 0, NOM);
        addLabel(sheet, 1, 0, name);
        addLabel(sheet, 0, 1, CHEMIN);

        int k = 7;
        addLabel(sheet, 1, 6, "Nb Ph0");
        addLabel(sheet, 2, 6, "Pb Ph0");
        addLabel(sheet, 3, 6, "Nb Ph1");
        addLabel(sheet, 4, 6, "Pb Ph1");
        addLabel(sheet, 5, 6, "Nb Ph2");
        addLabel(sheet, 6, 6, "Pb Ph2");

        for (int m = 0; m < stats.size(); m++) {
            totalCDS = totalCDS + stats.get(m).genome.getNbCorrectCDS() + stats.get(m).genome.getNbFailedCDS();
            totalCDSn = totalCDSn + stats.get(m).genome.getNbFailedCDS();
            totalTrinu = totalTrinu + stats.get(m).total_n_nucleotides;
        }
        addLabel(sheet, 0, 2, NB_CDS);
        addNumber(sheet, 1, 2, totalCDS);
        addLabel(sheet, 0, 3, NB_TRINU);
        addNumber(sheet, 1, 3, totalTrinu);
        addLabel(sheet, 0, 4, NB_CDS_NON_TRAITES);
        addNumber(sheet, 1, 4, totalCDSn);
        addLabel(sheet, 0, 6, TRINUCLEOTIDES);



        Map<String, Integer> treeMap = new TreeMap<String, Integer>(stats.get(0).phases.get(0));

        for (Entry<String, Integer> entry : treeMap.entrySet()) {
            String label = entry.getKey();
            addLabel(sheet, 0, k, label.toUpperCase());

            int nb = 1;
            for (int j = 0; j < stats.get(0).phases.size(); j++) {
                int ntotatl = 0;
               
                for (int m = 0; m < stats.size(); m++) {
                    ntotatl = ntotatl + stats.get(m).phases.get(j).get(label);
                }
                addReal(sheet, nb, k, (float) (ntotatl));
                float percent = ((float) ntotatl) / ((float) totalTrinu) * 100;
                addReal(sheet, nb + 1, k, percent);
                nb += 2;

            }
            k++;
        }

        addLabel(sheet, 0, k, TOTAL);
        for (int j = 0; j < 2 * stats.get(0).phases.size(); j++) {
            StringBuffer buf = new StringBuffer();

            buf.append("ROUND(SUM(" + CellReferenceHelper.getColumnReference(j + 1) + "8:"
                    + CellReferenceHelper.getColumnReference(j + 1) + (k) + "),3)");
            Formula f = new Formula(j + 1, k, buf.toString());

            sheet.addCell(f);
        }


    }

    private void addNumber(WritableSheet sheet, int column, int row,
            Integer integer) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addReal(WritableSheet sheet, int column, int row,
            Float fl) throws WriteException, RowsExceededException {

        Number number;
        number = new Number(column, row, fl, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
}
