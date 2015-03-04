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

        //SimpleParser test = new SimpleParser();
        //test.test();
        
        DatabaseModule db = new DatabaseModule();
        boolean work = true;
        work = db.updateGenomeEntry(16,"XXXX");
        logger.debug("1 " + work);
        work = db.updateGenomeEntry(29,"XXXX");
        logger.debug("2 " + work);
        work = db.updateGenomeEntry(52,"XXXX");
        logger.debug("3 " + work);
        work = db.updateGenomeEntry(19,"XXXX");
        logger.debug("3 " + work);

        work = db.updateGenomeEntry(2,"XXXX");
        logger.debug("same hash " + work);
        work = db.updateGenomeEntry(3,"YYYY");
        logger.debug("update hash " + work);
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



