import java.io.File;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.NumberCell;
import jxl.read.biff.BiffException;

public class ExcelReader {

    // Attributes
    public File inputFile;

    // Constructors
    public ExcelReader() {
    }

    public ExcelReader(String inputFile) {
        setInputFile(inputFile);
    }

    public ExcelReader(File inputFile) {
        setInputFile(inputFile);
    }

    // Methods
    public void setInputFile(String inputFile) {
        System.out.println(inputFile);
        this.inputFile = new File(inputFile);
    }

    public void setInputFile(File inputFile) {
        System.out.println(inputFile);
        this.inputFile = inputFile;
    }

    public WrittenStats read() throws IOException {
        try {
            WrittenStats stats = new WrittenStats();
            Workbook w = Workbook.getWorkbook(this.inputFile);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);

            stats.kingdom = sheet.getCell(1, 1).getContents();
            stats.group = sheet.getCell(2, 1).getContents();
            stats.subgroup = sheet.getCell(3, 1).getContents();
            stats.organism = sheet.getCell(4, 1).getContents();

//            System.out.println(stats.kingdom);
//            System.out.println(stats.group);
//            System.out.println(stats.subgroup);
//            System.out.println(stats.organism);

            // If this is a grouped stats excel file, we return null
            if (stats.group.isEmpty() || stats.subgroup.isEmpty() || stats.organism.isEmpty())
                return null;

            stats.nb_cds = Integer.parseInt(sheet.getCell(1, 2).getContents());
            stats.nb_tri = Integer.parseInt(sheet.getCell(1, 3).getContents());
            stats.nb_wrong_cds = Integer.parseInt(sheet.getCell(1, 4).getContents());

            for (int i=7; i<=70; i++) {
                String key = sheet.getCell(0, i).getContents();
                // Read phases key counts
                stats.addCount(0, key, Integer.parseInt(sheet.getCell(1, i).getContents()));
                stats.addCount(1, key, Integer.parseInt(sheet.getCell(3, i).getContents()));
                stats.addCount(2, key, Integer.parseInt(sheet.getCell(5, i).getContents()));
            }

            return stats;

        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return null;
    }

    public WrittenStats readGrouped() throws IOException {
        try {
            WrittenStats stats = new WrittenStats();
            Workbook w = Workbook.getWorkbook(this.inputFile);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);

            stats.kingdom = sheet.getCell(1, 1).getContents();
            stats.group = sheet.getCell(2, 1).getContents();
            stats.subgroup = sheet.getCell(3, 1).getContents();
            stats.organism = sheet.getCell(4, 1).getContents();

            // If this is a grouped stats excel file, its okay
            if (stats.group.isEmpty() || stats.subgroup.isEmpty() || stats.organism.isEmpty()) {

                for (int i=7; i<=70; i++) {
                    String key = sheet.getCell(0, i).getContents();
                    // Read phases key frequencies
                    stats.addFreq(0, key, (float) ((NumberCell) sheet.getCell(2, i)).getValue());
                    stats.addFreq(1, key, (float) ((NumberCell) sheet.getCell(4, i)).getValue());
                    stats.addFreq(2, key, (float) ((NumberCell) sheet.getCell(6, i)).getValue());
                }

                return stats;
            }
            else
                return null;

        } catch (BiffException e) {
            System.out.println(this.inputFile);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return null;
    }
}
