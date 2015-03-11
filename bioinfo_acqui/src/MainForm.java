import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.PrintStream;
import org.apache.log4j.Logger;

class MainForm extends JFrame {
    private JTextArea[] textAreaAcqui;
    private JTextArea[] textAreaParse;
    private JTextArea[] textAreaStat;
    private JProgressBar bar = new JProgressBar();
    private JTabbedPane mainTabbedPane = new JTabbedPane();
    private JTabbedPane acquisitionTabbedPane = new JTabbedPane();
    private JTabbedPane parsingTabbedPane = new JTabbedPane();
    private JTabbedPane statisticTabbedPane = new JTabbedPane();
    private int progressBarValue;
    final static Logger logger = Logger.getLogger(MainForm.class); 


    public void appendTextAreaAcquisition(String info, int index){
      this.textAreaAcqui[index].append(info);
    }
    
    public void appendTextAreaParsing(String info, int index){
      this.textAreaParse[index].append(info);
    }
    
    public void appendTextAreaStatistics(String info, int index){
      this.textAreaStat[index].append(info);
    }
    
    public void incrementProgressBar(){
      this.progressBarValue++;
      this.bar.setValue( progressBarValue);
    }
    
    public void setProgressBar(int max){
      this.bar.setMaximum(max);
      this.bar.setStringPainted(true);
    }

    public MainForm(int numThreads) {
        super("Trinucleotide statistical analysis");
        
        this.textAreaAcqui = new JTextArea[numThreads];
        this.textAreaParse = new JTextArea[numThreads];
        this.textAreaStat = new JTextArea[numThreads];
        
        // monospace font
        Font monoFont = new Font("Monospaced", Font.PLAIN ,12);
        
        for (int i = 0; i < numThreads; i++ ){
          this.textAreaAcqui[i] = new JTextArea();
          this.textAreaParse[i] = new JTextArea();
          this.textAreaStat[i] = new JTextArea();
          
          this.textAreaAcqui[i].setFont(monoFont);
          this.textAreaParse[i].setFont(monoFont);
          this.textAreaStat[i].setFont(monoFont);
          
          this.acquisitionTabbedPane.addTab("", null, new JScrollPane(this.textAreaAcqui[i]), "");
          this.parsingTabbedPane.addTab("", null, new JScrollPane(this.textAreaParse[i]), "");
          this.statisticTabbedPane.addTab("", null, new JScrollPane(this.textAreaStat[i]), "");
        }

        this.mainTabbedPane.addTab("Acquisition", null, acquisitionTabbedPane, "");
        this.mainTabbedPane.addTab("Parsing", null, parsingTabbedPane, "");
        this.mainTabbedPane.addTab("Statistic", null, statisticTabbedPane, "");

        this.bar.setMaximum(500);
        this.bar.setMinimum(0);
        this.bar.setStringPainted(true);
        this.progressBarValue = 0;

        //DefaultCaret caret = (DefaultCaret)textAreaAcqui.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //System.setOut(new PrintStream(new TextAreaOutputStream(textAreaAcqui)));

        //DefaultCaret caret = (DefaultCaret)textAreaAcqui.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //caret = (DefaultCaret)textAreaParse.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //caret = (DefaultCaret)textAreaStat.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.setVisible(true);

        setSize(640,480);
        setVisible(true);
    }
}
