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

    // it works like an array of dictionnary: phases[0]("ACG") = 1.25
    public ArrayList<HashMap<String, Integer>> phases;
    public int total_n_nucleotides = 0;
    private int word_length;
    public Genome genome;
    private static ExcelWriter excel = new ExcelWriter();

    public Statistics(Alphabet alphabet, Genome genome) {

        /* Each Statistics object has a Genome object in order
        * to be able to get required information such as number of
        * correct CDS, etc. */
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

    /* Constructor that takes just one CDS.
    * Automatically compute frequencies of given CDS. */
    public Statistics(Alphabet alphabet, String seq, Genome genome) {

        this(alphabet, genome);
        ComputeFrequencies(seq);
    }

    /* Constructor that takes an array of CDS.
    * Automatically compute frequencies of given CDS. */
    public Statistics(Alphabet alphabet, String[] seq, Genome genome) {

        this(alphabet, genome);
        for (String cds : seq) {
            total_n_nucleotides += (cds.length() / word_length) - 1;
            ComputeFrequencies(cds);
        }
    }

    /* Constructor that takes a vector of CDS.
    * Automatically compute frequencies of given CDS. */
    public Statistics(Alphabet alphabet, Vector<String> seq, Genome genome) {

        this(alphabet, genome);
        for (String cds : seq) {
            total_n_nucleotides += (cds.length() / word_length) - 1;
            ComputeFrequencies(cds);
        }

    }

    /* Method to display computed frequencies in a pretty way. */
    public void print() {

        mediatorGUI.updateStatisticsPanel("Organism: " + this.genome.getOrganism() + "\n");
        mediatorGUI.updateStatisticsPanel("      ");
        for (int i = 0; i < phases.size(); i++) {
            mediatorGUI.updateStatisticsPanel(String.format("%-16s", "Phase " + i));
        }
        mediatorGUI.updateStatisticsPanel("\n------------------------------------------------------\n");

        for (Entry<String, Integer> entry : phases.get(0).entrySet()) {

            String label = entry.getKey();
            mediatorGUI.updateStatisticsPanel(label.toUpperCase() + "   ");
            for (int i = 0; i < phases.size(); i++) {

                float percent = (float) (((float) (phases.get(i).get(label))) / ((float) (total_n_nucleotides))) * 100;
                mediatorGUI.updateStatisticsPanel(String.format("%-16s", percent + "%"));
            }
            mediatorGUI.updateStatisticsPanel("\n");
            mediatorGUI.incrementStatsLines();
        }

        mediatorGUI.updateStatisticsPanel("\n");
        mediatorGUI.incrementStatsLines(4);
    }

    /* The length of a word in the current alphabet determines
    * the number of phases to compute. With A4, words' length is 3,
    * thus we have 3 phases. */
    private void ComputeFrequencies(String seq) {
        // TODO: multi-threading could be used here
        for (int i = 0; i < word_length; i++) {

            PhaseFrequencies(seq, i);
        }
    }

    /* Compute the frequency of each word in a CDS for a given phase. */
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

        // we split the CDS into words (or keys)
        for (String n_nucleotide : Split(work_sequence)) {

            // increment each met key
            phases.get(true_phase)
                    .put(n_nucleotide,
                            phases.get(true_phase)
                                    .get(n_nucleotide) + 1);
        }
    }

    /* Takes a sequence and return the list of words that composes it. */
    private List<String> Split(String s) {

        List<String> parts = new ArrayList<String>();
        int len = s.length();
        for (int i = 0; i < len; i += word_length) {

            parts.add(s.substring(i, i + word_length));
        }

        return parts;
    }

    /* This method allows us to write the computed statistics into an Excel file. */
    public void write()
            throws IOException, WriteException {

        File dir = new File(ExcelWriter.buildOutputDirPath(this.genome));
        dir.mkdirs();
        excel.setStatistics(this);
        excel.write();
    }

    /* Each time a genome's tri-nucleotides frequency has been computed,
    * we tag it as done in a local database. That let us know which genome
    * should or should not be treated when the program is restarted. */
    public void tagAsDone() {
        DatabaseModule db = DatabaseModule.getInstance();
        db.updateGenomeEntry(this.genome.getId(), "XXXXX");
    }
}
