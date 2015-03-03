import java.beans.Statement;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics
{
    public ArrayList<HashMap<String, Integer>> phases;
    public int total_n_nucleotides = 0;
    private int shift_window_size;
    public String name;

    public Statistics(Alphabet alphabet) {

        shift_window_size = alphabet.item_length;
        phases = new ArrayList<HashMap<String, Integer>>(shift_window_size);

        // initialize list of hash to 0
        for (int i=0; i<shift_window_size; i++) {

            HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
            for (String key : alphabet.items) {

                frequencies.put(key, 0);
            }
            phases.add(frequencies);
        }
    }
    
    public Statistics(Alphabet alphabet, String seq) {

        this(alphabet);
        ComputeFrequencies(seq);
    }
    
    public Statistics(Alphabet alphabet, String[] seq) {

        this(alphabet);
        for (String cds: seq) {
            
            ComputeFrequencies(cds);
        }
    }

    public Statistics(Alphabet alphabet, Vector<String> seq) {

        this(alphabet);
        for (String cds: seq) {
            
            ComputeFrequencies(cds);
        }
    }


    private void ComputeFrequencies(String seq) {
        // TODO: use multi-threading here!
        for (int i=0; i<shift_window_size; i++) {

            PhaseFrequencies(seq, i);
        }
    }

    private void PhaseFrequencies(String sequence, int phase) {

        // ensure phase do not go out of bounds
        int true_phase = phase % shift_window_size;
        String work_sequence = sequence.substring(true_phase);

        // ensure the passed String has a size multiple of shift_window_size
        int shift = work_sequence.length() % shift_window_size;
        if (shift > 0) {

            work_sequence = work_sequence.substring(
                0, work_sequence.length() - shift);
        }

        // tried to optimize, got exceptions, got bored, got back to previous working version

//        char[] char_array = work_sequence.toCharArray();
//        char[] tmp = new char[shift_window_size+1];
//
//        for (int i=0; i<work_sequence.length(); i++) {
//
//            // walk on string to get chars
//            for (int c=0; c<shift_window_size; c++) {
//
//                tmp[c] = char_array[i];
//                i++;
//            }
//
//            // create String from these chars
//            String n_nucleotide = new String(tmp);
//            System.out.println("Nucl: "+ n_nucleotide);

        for (String n_nucleotide: Split(work_sequence)) {

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
        for (int i=0; i<len; i+=shift_window_size) {

            parts.add(s.substring(i, i+shift_window_size));
        }

        return parts;
    }
}
