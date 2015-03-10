import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Map.*;
import jxl.write.WriteException;

public class Statistics {

    private static IMediatorGUI mediatorGUI = MediatorGUI.getInstance();
    public ArrayList<HashMap<String, Integer>> phases;
    public int total_n_nucleotides = 0;
    private int word_length;
    public Genome genome;
    private static ExcelWriter excel;
    // TODO: add static attribute to classify each genome in kingdom, groups, subgroups...

    public Statistics(Alphabet alphabet, Genome genome) {

        this.genome = genome;
        word_length = alphabet.word_length;
        phases = new ArrayList<HashMap<String, Integer>>(word_length);

        // initialize list of hash to 0
        for (int i = 0; i < word_length; i++) {

            HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
            for (String key : alphabet.words) {

                frequencies.put(key, 0);
            }
            phases.add(frequencies);
        }
    }

    public Statistics(Alphabet alphabet, String seq, Genome genome) {

        this(alphabet, genome);
        ComputeFrequencies(seq);
    }

    public Statistics(Alphabet alphabet, String[] seq, Genome genome) {

        this(alphabet, genome);

        for (String cds : seq) {
            total_n_nucleotides += cds.length() / word_length;
            ComputeFrequencies(cds);
        }
    }
    
    public Statistics(Alphabet alphabet, Vector<String> seq, Genome genome) {

        this(alphabet, genome);
        for (String cds : seq) {
            total_n_nucleotides += cds.length() / word_length;
            ComputeFrequencies(cds);
        }

    }

    public void print() {

        mediatorGUI.updateStatisticsPanel("Organism: " + this.genome.getOrganism() + "\n", 0);
        mediatorGUI.updateStatisticsPanel("      ", 0);
        for (int i=0; i<phases.size(); i++) {
            
            mediatorGUI.updateStatisticsPanel(String.format("%-16s", "Phase " + i), 0);
        }
        mediatorGUI.updateStatisticsPanel("\n------------------------------------------------------\n", 0);

        for (Entry<String, Integer> entry : phases.get(0).entrySet() ) {

            String label = entry.getKey();
            mediatorGUI.updateStatisticsPanel(label.toUpperCase() + "   ", 0);
            for (int i=0; i<phases.size(); i++) {

                float percent = (float)(((float) (phases.get(i).get(label))) / ((float) (total_n_nucleotides))) * 100;
                mediatorGUI.updateStatisticsPanel(String.format("%-16s", percent + "%"), 0);
            }
            mediatorGUI.updateStatisticsPanel("\n", 0);
        }

        mediatorGUI.updateStatisticsPanel("\n", 0);
    }

    private void ComputeFrequencies(String seq) {
        // TODO: use multi-threading here!
        for (int i = 0; i < word_length; i++) {

            PhaseFrequencies(seq, i);
        }
    }

    private void PhaseFrequencies(String sequence, int phase) {

        // ensure phase do not go out of bounds
        int true_phase = phase % word_length;
        String work_sequence = sequence.substring(true_phase);

        // ensure the passed String has a size multiple of word_length
        int shift = work_sequence.length() % word_length;
        if (shift > 0) {

            work_sequence = work_sequence.substring(
                    0, work_sequence.length() - shift);
        } else {
            work_sequence = work_sequence.substring(
                    0, work_sequence.length() - 3);
        }

        // tried to optimize, got exceptions, got bored, got back to previous working version

//        char[] char_array = work_sequence.toCharArray();
//        char[] tmp = new char[word_length+1];
//
//        for (int i=0; i<work_sequence.length(); i++) {
//
//            // walk on string to get chars
//            for (int c=0; c<word_length; c++) {
//
//                tmp[c] = char_array[i];
//                i++;
//            }
//
//            // create String from these chars
//            String n_nucleotide = new String(tmp);
//            System.out.println("Nucl: "+ n_nucleotide);

        for (String n_nucleotide : Split(work_sequence)) {

            // increment each met key
            phases.get(true_phase)
                  .put(n_nucleotide,
                       phases.get(true_phase)
                             .get(n_nucleotide) + 1);
        }
    }

    private List<String> Split(String s) {

        List<String> parts = new ArrayList<String>();
        int len = s.length();
        for (int i = 0; i < len; i += word_length) {

            parts.add(s.substring(i, i + word_length));
        }

        return parts;
    }

    private static String verifyString(String text) {
        return text.replaceAll("[?:!/*<>]+", "_");
    }

    public static void Write(Statistics stats)
            throws IOException, WriteException {
        String outputFile;

        if (stats.genome.getKingdom() != null && stats.genome.getGroup() != null
                && stats.genome.getSubGroup() != null && stats.genome.getOrganism() != null) {
            outputFile = verifyString(stats.genome.getKingdom()) + File.separator + verifyString(stats.genome.getGroup())
                    + File.separator
                    + verifyString(stats.genome.getSubGroup()) + File.separator;

            String homeDirectory = System.getProperty("user.home");

            String absoluteFilePath = "";

            absoluteFilePath = homeDirectory + File.separator + "Statistics" + File.separator + outputFile;

            File file = new File(absoluteFilePath);
            file.mkdirs();
            File stat = new File(absoluteFilePath + verifyString(stats.genome.getOrganism()) + ".xls");          
            stat.createNewFile();

        }

        // TODO: write data in file with Add methods

    }
}
