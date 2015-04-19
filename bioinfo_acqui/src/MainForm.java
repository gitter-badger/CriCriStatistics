import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import jxl.write.WriteException;
import org.apache.log4j.Logger;

class MainForm extends JFrame {

    private JTextArea[] textAreaAcqui;
    private JTextArea[] textAreaParse;
    private JTextArea[] textAreaStat;
    private JProgressBar bar = new JProgressBar();
    private JTabbedPane mainTabbedPane = new JTabbedPane();
    private JButton button = new JButton("(Re)Start acquisition");
    private int progressBarValue;
    final static Logger logger = Logger.getLogger(MainForm.class);

    public void appendTextAreaAcquisition(String info, int index) {
        this.textAreaAcqui[index].append(info);
    }

    public void appendTextAreaParsing(String info, int index) {
        this.textAreaParse[index].append(info);
    }

    public void appendTextAreaStatistics(String info, int index) {
        this.textAreaStat[index].append(info);
    }

    public void incrementProgressBar() {
        this.progressBarValue++;
        this.bar.setValue(progressBarValue);
    }

    public void setProgressBar(int max) {
        this.bar.setMaximum(max);
        this.bar.setStringPainted(true);
    }

    public MainForm(int numThreads) {
        
        super("Trinucleotide statistical analysis");

        // Trigger writing of Kindgom/Group/Subgroup XLS files on CloseWindow event
        this.addWindowListener(new WindowAdapter() {
            
            public void windowClosing(WindowEvent e) {
                
                ExcelWriter ew = new ExcelWriter();
                try {
                    try {
                        ew.writeStatistics();
                    } catch (WriteException ex) {
                        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Terminated.");
                System.exit(1);
            }
        });

        this.mainTabbedPane.addTab("Menu", CreateMenuTab());
        this.mainTabbedPane.addTab("Update", CreateUpdateTab(numThreads));

        // Build progress bar
        BuildBar();
        
        // Set main display
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        this.setVisible(true);

        setSize(640, 480);
        setVisible(true);
    }
    
    private JPanel CreateMenuTab() {

        JPanel menuTabbedPane = new JPanel();

//        JLabel label1 = new JLabel( "Username:" );
//        label1.setBounds( 10, 15, 150, 20 );
//        panel1.add( label1 );
//
//        JTextField field = new JTextField();
//        field.setBounds( 10, 35, 150, 20 );
//        panel1.add( field );
        
        // Start acquisition must clear all the textarea
        menuTabbedPane.add(this.button, BorderLayout.NORTH);

//        JPanel topPanel = new JPanel();
//        topPanel.setLayout( new BorderLayout() );
//        getContentPane().add( topPanel );
        
        return menuTabbedPane;
    }
    
    private JTabbedPane CreateUpdateTab(int numThreads) {

        JTabbedPane updateTabbedPane = new JTabbedPane();
        JTabbedPane acquisitionTabbedPane = new JTabbedPane();
        JTabbedPane parsingTabbedPane = new JTabbedPane();
        JTabbedPane statisticTabbedPane = new JTabbedPane();

        // monospace font
        this.textAreaAcqui = new JTextArea[numThreads];
        this.textAreaParse = new JTextArea[numThreads];
        this.textAreaStat = new JTextArea[numThreads];

        Font monoFont = new Font("Monospaced", Font.PLAIN, 12);
        DefaultCaret caret;

        for (int i = 0; i < numThreads; i++) {

            this.textAreaAcqui[i] = new JTextArea();
            this.textAreaParse[i] = new JTextArea();
            this.textAreaStat[i] = new JTextArea();

            this.textAreaAcqui[i].setFont(monoFont);
            this.textAreaParse[i].setFont(monoFont);
            this.textAreaStat[i].setFont(monoFont);

            acquisitionTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaAcqui[i]));
            parsingTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaParse[i]));
            statisticTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaStat[i]));

            caret = (DefaultCaret)textAreaAcqui[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            caret = (DefaultCaret)textAreaParse[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            caret = (DefaultCaret)textAreaStat[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }

        updateTabbedPane.addTab("Acquisition", acquisitionTabbedPane);
        updateTabbedPane.addTab("Parsing", parsingTabbedPane);
        updateTabbedPane.addTab("Statistics", statisticTabbedPane);

        return updateTabbedPane;
    }
    
    private void BuildBar() {

        this.bar.setMaximum(500);
        this.bar.setMinimum(0);
        this.bar.setStringPainted(true);
        this.progressBarValue = 0;
    }
    
    public JButton getButton() {
        
        return this.button;
    }
}
