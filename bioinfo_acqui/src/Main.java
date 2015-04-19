import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

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
            MediatorGUI mediatorGUI = MediatorGUI.getInstance();
            mediatorGUI.setGUI((MainForm) frame);

            System.setProperty("java.net.useSystemProxies", "true");

            final DebugOption debugOption = new DebugOption();
            debugOption.parseInputCommand(argv);
            
            final SAXParserFactory factory = SAXParserFactory.newInstance();

            // Trigger launching of acquisition and treatment when hitting button
            ((MainForm) frame).getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    GenomeOverview genomeOverview = new GenomeOverview();

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
