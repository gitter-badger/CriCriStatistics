import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

//TODO: Get genomes overview list (timestamp for updating once a day?week?
public class Main {

    public static void main(String argv[]) {

        try {
            JFrame frame = new MainForm();
            System.setProperty("java.net.useSystemProxies", "true");

            GenomeOverview genomeOverview = new GenomeOverview();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            List<Genome> genomeList = genomeOverview.getGenomeList();

            int noOfThreads = 8;
            List<GenomeThread> genomeThreads = new ArrayList<GenomeThread>();

            for (int i = 0; i < noOfThreads; i++)
            {
                GenomeThread genomeThread = new GenomeThread(String.valueOf(i), factory.newSAXParser(),
                        genomeList.subList(i*(genomeList.size()/noOfThreads),
                        (i+1)*(genomeList.size()/noOfThreads)));
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

class MainForm extends JFrame {
    private JTextArea textArea = new JTextArea();
    private JProgressBar bar = new JProgressBar();

    public MainForm() {
        super("Trinucleotide statistical analysis");

        bar.setMaximum(500);
        bar.setMinimum(0);
        bar.setStringPainted(true);

        System.setOut(new PrintStream(new TextAreaOutputStream(textArea)));

        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.getContentPane().add(bar, BorderLayout.NORTH);
        this.setVisible(true);

        setSize(640,480);



        setVisible(true);
    }
}


