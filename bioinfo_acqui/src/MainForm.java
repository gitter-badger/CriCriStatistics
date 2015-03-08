import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.PrintStream;
import org.apache.log4j.Logger;

class MainForm extends JFrame {
    private JTextArea textAreaAcqui = new JTextArea();
    private JTextArea textAreaParse = new JTextArea();
    private JTextArea textAreaStat = new JTextArea();
    private JProgressBar bar = new JProgressBar();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private int progressBarValue;
    final static Logger logger = Logger.getLogger(MainForm.class); 

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    public void appendTextAreaAcquisition(String info){
      this.textAreaAcqui.append(info);
    }
    
    public void appendTextAreaParsing(String info){
      this.textAreaParse.append(info);
    }
    
    public void appendTextAreaStatistics(String info){
      this.textAreaStat.append(info);
    }
    
    public void incrementProgressBar(){
      this.progressBarValue++;
      this.bar.setValue( progressBarValue);
    }
    
    public void setProgressBar(int max){
      this.bar.setMaximum(max);
      this.bar.setStringPainted(true);
    }

    public MainForm() {
        super("Trinucleotide statistical analysis");
        
        this.tabbedPane.addTab("Acquisition",null,new JScrollPane(textAreaAcqui),"");
        this.tabbedPane.addTab("Parsing",null,new JScrollPane(textAreaParse),"");
        this.tabbedPane.addTab("Statistic",null,new JScrollPane(textAreaStat),"");

        this.bar.setMaximum(500);
        this.bar.setMinimum(0);
        this.bar.setStringPainted(true);
        this.progressBarValue = 0;

        //System.setOut(new PrintStream(new TextAreaOutputStream(textAreaAcqui)));

        DefaultCaret caret = (DefaultCaret)textAreaAcqui.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        // monospace font
        Font monoFont = new Font("Monospaced", Font.PLAIN ,12);
        this.textAreaAcqui.setFont(monoFont);
        this.textAreaParse.setFont(monoFont);
        this.textAreaStat.setFont(monoFont);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.setVisible(true);

        setSize(640,480);



        setVisible(true);
    }
}
