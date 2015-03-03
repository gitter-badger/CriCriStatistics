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

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {
    public static void main(String argv[]) {

        //SimpleParser test = new SimpleParser();
        //test.test();
        
        try {

            JFrame frame = new MainForm();
            System.setProperty("java.net.useSystemProxies", "true");
            
            DebugOption debugOption = new DebugOption();
            debugOption.parseInputCommand(argv);

            GenomeOverview genomeOverview = new GenomeOverview();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            List<Genome> genomeList = genomeOverview.getGenomeList();

            int noOfThreads = 8;
            List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();

            for (int i = 0; i < noOfThreads; i++)
            {
                SimpleParser test = new SimpleParser();
                GenomeThread genomeThread = new GenomeThread(String.valueOf(i), factory.newSAXParser(),
                        genomeList.subList(i*(genomeList.size()/noOfThreads),
                        (i+1)*(genomeList.size()/noOfThreads)), test);
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



