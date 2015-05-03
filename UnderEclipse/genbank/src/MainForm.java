import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.UIManager.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.text.DefaultCaret;
import java.awt.*;

import org.apache.log4j.Logger;

class MainForm extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea[] textAreaAcqui;
    private JTextArea[] textAreaParse;
    private JTextArea[] textAreaStat;
    private JTextArea[] textAreaWrite;
    
    public int[] countAcqui;
    public int[] countParse;
    public int[] countStats;
    public int[] countWrite;
    
    private JProgressBar bar = new JProgressBar();
    private int progressBarValue;
    private int progressBarMax;

    private JTabbedPane mainTabbedPane = new JTabbedPane();
    
    private JButton startButton = new JButton("(Re)Start acquisition");
    private JButton stopButton = new JButton("Stop acquisition");
    private JCheckBox saveData = new JCheckBox("Save data on disk");
    private JButton chooser = new JButton("Select output directory");
    private JButton updateGrouped = new JButton("Update grouped statistics");
    private JTextField outputText = new JTextField(50);

    private static int num_threads;
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

    public void appendTextAreaWriting(String info, int index) {
        this.textAreaWrite[index].append(info);
    }
    
    public void clearAcquiArea(int index) {
        this.textAreaAcqui[index].setText(null);
    }

    public void clearParseArea(int index) {
        this.textAreaParse[index].setText(null);
    }

    public void clearStatsArea(int index) {
        this.textAreaStat[index].setText(null);
    }

    public void clearWriteArea(int index) {
        this.textAreaWrite[index].setText(null);
    }

    public void clearAllAcquiArea() {
        for (int i=0; i<num_threads; i++)
            this.textAreaAcqui[i].setText(null);
    }

    public void clearAllParseArea() {
        for (int i=0; i<num_threads; i++)
            this.textAreaParse[i].setText(null);
    }

    public void clearAllStatsArea() {
        for (int i=0; i<num_threads; i++)
            this.textAreaStat[i].setText(null);
    }

    public void clearAllWriteArea() {
        for (int i=0; i<num_threads; i++)
            this.textAreaWrite[i].setText(null);
    }

    public void incrementProgressBar() {
        this.progressBarValue++;
        this.bar.setValue(progressBarValue);
    }

    public void setProgressBar(int max) {
        this.progressBarMax = max;
        this.bar.setMaximum(max);
        this.bar.setStringPainted(true);
    }
    
    public void setProgress(int v) {
        this.progressBarValue = v;
        this.bar.setValue(v);
    }
    
    public int getProgress() {
        return this.progressBarValue;
    }
    
    public int getBarSize() {
        return this.progressBarMax;
    }

    public MainForm(int numThreads) {
        
        super("Trinucleotide statistical analysis");
        num_threads = numThreads;

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        this.mainTabbedPane.addTab("Menu", CreateMenuTab());
        this.mainTabbedPane.addTab("Progress", CreateUpdateTab(numThreads));

        // Build progress bar
        BuildBar();

        // Text field is read only
        this.outputText.setEditable(false);
        setOutputText(Settings.getInstance().getOutputDir());
        
        // Enable / Disable buttons at startup
        this.startButton.setEnabled(true);
        this.updateGrouped.setEnabled(true);
        this.chooser.setEnabled(true);
        this.stopButton.setEnabled(false);

        // Set main display
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        this.setVisible(true);

        setSize(640, 480);
        setVisible(true);
    }

    private JPanel CreateMenuTab() {

        JPanel main = new JPanel();
        
        JPanel menuTabbedPane = new JPanel();
        menuTabbedPane.setLayout(new BoxLayout(menuTabbedPane, BoxLayout.Y_AXIS));
//        menuTabbedPane.setPreferredSize(new Dimension(320, 400));

        this.startButton.setMinimumSize(new Dimension(250, 20));
        this.startButton.setMaximumSize(new Dimension(250, 20));
        
        this.updateGrouped.setMinimumSize(new Dimension(250, 20));
        this.updateGrouped.setMaximumSize(new Dimension(250, 20));
        
        this.chooser.setMinimumSize(new Dimension(250, 20));
        this.chooser.setMaximumSize(new Dimension(250, 20));
        
        this.stopButton.setMinimumSize(new Dimension(250, 20));
        this.stopButton.setMaximumSize(new Dimension(250, 20));
        
        this.outputText.setMinimumSize(new Dimension(250, 20));
        this.outputText.setMaximumSize(new Dimension(250, 20));
        
        this.saveData.setMinimumSize(new Dimension(250, 20));
        this.saveData.setMaximumSize(new Dimension(250, 20));

        this.outputText.setMinimumSize(new Dimension(250, 20));
        this.outputText.setMaximumSize(new Dimension(250, 20));

        menuTabbedPane.add(this.startButton);
        menuTabbedPane.add(this.stopButton);
        menuTabbedPane.add(this.updateGrouped);
        menuTabbedPane.add(this.chooser);
        menuTabbedPane.add(this.saveData);
        menuTabbedPane.add(this.outputText);
        
        main.add(menuTabbedPane, BorderLayout.WEST);

        return main;
    }

    private JTabbedPane CreateUpdateTab(int numThreads) {

        JTabbedPane updateTabbedPane = new JTabbedPane();
        JTabbedPane acquisitionTabbedPane = new JTabbedPane();
        JTabbedPane parsingTabbedPane = new JTabbedPane();
        JTabbedPane statisticTabbedPane = new JTabbedPane();
        JTabbedPane writingTabbedPane = new JTabbedPane();

        // monospace font
        this.textAreaAcqui = new JTextArea[numThreads];
        this.textAreaParse = new JTextArea[numThreads];
        this.textAreaStat = new JTextArea[numThreads];
        this.textAreaWrite = new JTextArea[numThreads];

        this.countAcqui = new int[numThreads];
        this.countParse = new int[numThreads];
        this.countStats = new int[numThreads];
        this.countWrite = new int[numThreads];

        Font monoFont = new Font("Monospaced", Font.PLAIN, 12);
        DefaultCaret caret;

        for (int i = 0; i < numThreads; i++) {

            this.textAreaAcqui[i] = new JTextArea();
            this.textAreaParse[i] = new JTextArea();
            this.textAreaStat[i] = new JTextArea();
            this.textAreaWrite[i] = new JTextArea();

            this.textAreaAcqui[i].setFont(monoFont);
            this.textAreaParse[i].setFont(monoFont);
            this.textAreaStat[i].setFont(monoFont);
            this.textAreaWrite[i].setFont(monoFont);

            this.countAcqui[i] = 0;
            this.countParse[i] = 0;
            this.countStats[i] = 0;
            this.countWrite[i] = 0;

            acquisitionTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaAcqui[i]));
            parsingTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaParse[i]));
            statisticTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaStat[i]));
            writingTabbedPane.addTab("Thread " + i, new JScrollPane(this.textAreaWrite[i]));

            caret = (DefaultCaret) textAreaAcqui[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            caret = (DefaultCaret) textAreaParse[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            caret = (DefaultCaret) textAreaStat[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            caret = (DefaultCaret) textAreaWrite[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }

        updateTabbedPane.addTab("Acquisition", acquisitionTabbedPane);
        updateTabbedPane.addTab("Parsing", parsingTabbedPane);
        updateTabbedPane.addTab("Statistics", statisticTabbedPane);
        updateTabbedPane.addTab("Excel Writer", writingTabbedPane);

        return updateTabbedPane;
    }

    private void BuildBar() {

        this.bar.setMaximum(500);
        this.bar.setMinimum(0);
        this.bar.setStringPainted(true);
        this.progressBarValue = 0;
    }

    public JButton getStartButton() {

        return this.startButton;
    }

    public JButton getStopButton() {

        return this.stopButton;
    }

    public JCheckBox getDataCheckbox() {

        return this.saveData;
    }

    public JButton getChooserButton() {

        return this.chooser;
    }

    public void setOutputText(String s) {

        this.outputText.setText(s);
    }

    public JTextField getOutputTextField() {

        return this.outputText;
    }

    public JButton getUpdateGroupedStatsButton() {

        return this.updateGrouped;
    }
}
