import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.awt.event.*;
import java.io.IOException;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import jxl.write.WriteException;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String argv[]) {

        final DatabaseModule db = DatabaseModule.getInstance();

        final Settings settings = Settings.getInstance();
        settings.setNumThreads(5);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                logger.debug("Inside Database Shutdown Hook");
                DatabaseModule db = DatabaseModule.getInstance();
                db.gracefulStop();
            }
        });

        try {

            JFrame frame = new MainForm(settings.getNumThreads());
            final MediatorGUI mediatorGUI = MediatorGUI.getInstance();
            mediatorGUI.setGUI((MainForm) frame);

            System.setProperty("java.net.useSystemProxies", "true");

            final DebugOption debugOption = new DebugOption();
            debugOption.parseInputCommand(argv);

            final SAXParserFactory factory = SAXParserFactory.newInstance();

            // Trigger launching of acquisition and treatment when hitting start button
            ((MainForm) frame).getStartButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    GenomeOverview genomeOverview = new GenomeOverview();

                    settings.turnON();
                    mediatorGUI.clearAllAreas();

                    List<Genome> genomeList = null;
                    try {
                        genomeList = genomeOverview.getGenomeList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("Nb of genome: " + genomeList.size());

                    int noOfThreads = settings.getNumThreads();
                    List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();

                    for (int i = 0; i < noOfThreads; i++) {
                        SimpleParser test = new SimpleParser();
                        GenomeThread genomeThread = null;
                        try {
                            genomeThread = new GenomeThread(String.valueOf(i), factory.newSAXParser(),
                                    genomeList.subList(i * (genomeList.size() / noOfThreads),
                                            (i + 1) * (genomeList.size() / noOfThreads)), db, test);
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        genomeThread.setDebuggingOption(debugOption);
                        genomeThreads.add(genomeThread);
                    }

                    List<Thread> threadList = new ArrayList<Thread>();

                    for (int i = 0; i < noOfThreads; i++) {
                        Thread thread = new Thread(genomeThreads.get(i));
                        thread.start();
                        threadList.add(thread);
                    }
                }
            });

            // Acquisition finished. Write summed-stats
            ((MainForm) frame).getUpdateGroupedStatsButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    GroupedStats gs = new GroupedStats();
                    gs.updateGroupedStats();
                }
            });

            // Stop threads when hitting Stop button
            ((MainForm) frame).getStopButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    settings.turnOFF();
                }
            });

            // Settings control with saveData checkbox
            ((MainForm) frame).getDataCheckbox().addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        settings.activateSaveData();
                    else
                        settings.deactivateSaveData();
                }
            });

            // Directory selection
            ((MainForm) frame).getChooserButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new java.io.File("."));
                    chooser.setDialogTitle("Select the output directory");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);

                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String newPath = chooser.getSelectedFile().getAbsolutePath();
                        settings.setOutputDir(newPath);
                        ((MainForm) frame).setOutputText(newPath);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
