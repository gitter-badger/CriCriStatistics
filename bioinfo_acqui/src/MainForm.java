import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.PrintStream;

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