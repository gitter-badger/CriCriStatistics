import javax.swing.*;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {
        
    final static Logger logger = Logger.getLogger(Main.class); 
    
    public static void main(String argv[]) {
        
        DatabaseModule db = DatabaseModule.getInstance();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
          @Override
          public void run() {
            logger.debug("Inside Add Shutdown Hook");
            DatabaseModule db = DatabaseModule.getInstance();
            db.gracefulStop();
          }
        });

        
        try {

            JFrame frame = new MainForm();
            System.setProperty("java.net.useSystemProxies", "true");
            
            DebugOption debugOption = new DebugOption();
            debugOption.parseInputCommand(argv);

            GenomeOverview genomeOverview = new GenomeOverview();
            SAXParserFactory factory = SAXParserFactory.newInstance();
          
            List<Genome> genomeList = genomeOverview.getGenomeList();
            logger.info("Nb of genome: "+ genomeList.size() );

            int noOfThreads = 1;
            List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();
            
            for (int i = 0; i < noOfThreads; i++)
            {
                SimpleParser test = new SimpleParser();
                GenomeThread genomeThread = new GenomeThread(String.valueOf(i), factory.newSAXParser(),
                        genomeList.subList(i*(genomeList.size()/noOfThreads),
                        (i+1)*(genomeList.size()/noOfThreads)), db, test);
                genomeThread.setDebuggingOption(debugOption);
                genomeThreads.add(genomeThread);
            }

            for (int i = 0; i < noOfThreads; i++) {
                Thread thread = new Thread(genomeThreads.get(i));
                thread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



