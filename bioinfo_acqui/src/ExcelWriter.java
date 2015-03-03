import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
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
    private WritableWorkbook workbook;

    public ExcelWriter(Statistics stat)
           throws IOException, WriteException {

        //this.outputFile = stat.name + ".xls";
        File file = new File(outputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        this.workbook = Workbook.createWorkbook(file, wbSettings);

        // create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // automatically wrap the cells
        times.setWrap(true);

        // create a bold font with underline
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        // ???
        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);
    }
    
    public void Write() 
           throws IOException, WriteException {
        
        workbook.write();
        workbook.close();
    }
    
    public WritableSheet NewSheet(String name, int num) {

        workbook.createSheet(name, num);
        return workbook.getSheet(num);
    }
    
    public WritableSheet GetSheet(int num) {
        
        return workbook.getSheet(num);
    }
    
    public void AddNumber(WritableSheet sheet, int column, int row, Integer integer)
           throws WriteException, RowsExceededException {

        sheet.addCell(new Number(column, row, integer, times));
    }

    public void AddCaption(WritableSheet sheet, int column, int row, String s)
           throws WriteException, RowsExceededException {
        
        sheet.addCell(new Label(column, row, s, timesBoldUnderline));
    }
    
    public void AddLabel(WritableSheet sheet, int column, int row, String s)
           throws WriteException, RowsExceededException {

        sheet.addCell(new Label(column, row, s, times));
    }
}
