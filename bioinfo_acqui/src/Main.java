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
//        String sequence1 = "acgtaaacagatcacccgctgat";
//        String sequence2 = "tcgtagtagtagtcaccctccgta";
//        String[] seq_array = new String[2];
//        seq_array[0] = sequence1;
//        seq_array[1] = sequence2;
//        Alphabet a4 = new Alphabet();
//        Statistics stats1 = new Statistics(a4, sequence1);
//        Statistics stats2 = new Statistics(a4, sequence2);
//        Statistics stats12 = new Statistics(a4, seq_array);
//
//        System.out.println("Alphabet items: " + a4.items);
//
//        System.out.println("Seq 1: " + sequence1);
//        System.out.println("Seq 2: " + sequence2);
//
//        System.out.println();
//        System.out.println("gta in seq1 phase 1: " + stats1.phases.get(0).get("gta"));
//        System.out.println("gta in seq1 phase 2: " + stats1.phases.get(1).get("gta"));
//        System.out.println("gta in seq1 phase 3: " + stats1.phases.get(2).get("gta"));
//
//        System.out.println();
//        System.out.println("gta in seq2 phase 1: " + stats2.phases.get(0).get("gta"));
//        System.out.println("gta in seq2 phase 2: " + stats2.phases.get(1).get("gta"));
//        System.out.println("gta in seq2 phase 3: " + stats2.phases.get(2).get("gta"));
//
//        System.out.println();
//        System.out.println("gta in seq1+seq2 phase 1: " + stats12.phases.get(0).get("gta"));
//        System.out.println("gta in seq1+seq2 phase 2: " + stats12.phases.get(1).get("gta"));
//        System.out.println("gta in seq1+seq2 phase 3: " + stats12.phases.get(2).get("gta"));

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



