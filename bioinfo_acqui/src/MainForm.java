import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.PrintStream;

class MainForm extends JFrame {
    private JTextArea textAreaAcqui = new JTextArea();
    private JTextArea textAreaParse = new JTextArea();
    private JTextArea textAreaStat = new JTextArea();
    private JProgressBar bar = new JProgressBar();
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public MainForm() {
        super("Trinucleotide statistical analysis");
        
        tabbedPane.addTab("Acquisition",null,new JScrollPane(textAreaAcqui),"");
        tabbedPane.addTab("Parsing",null,new JScrollPane(textAreaParse),"");
        tabbedPane.addTab("Statistic",null,new JScrollPane(textAreaStat),"");

        bar.setMaximum(500);
        bar.setMinimum(0);
        bar.setStringPainted(true);

        System.setOut(new PrintStream(new TextAreaOutputStream(textAreaAcqui)));

        DefaultCaret caret = (DefaultCaret)textAreaAcqui.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.setVisible(true);

        setSize(640,480);



        setVisible(true);
    }
}
