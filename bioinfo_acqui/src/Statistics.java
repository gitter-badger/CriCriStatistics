import java.beans.Statement;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics
{
    public ArrayList<HashMap<String, Integer>> phases;
    private int shift_window_size;
    // TODO: add total_number_of_nucleotides attribute

    // TODO: we could also add a constructor with an array of CDS (String[] seq)
    // then the frequencies would be summed in the same HashMap (keeping phases separated of course)
    public Statistics(Alphabet alphabet, String seq) {

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

        ComputeFrequencies(seq);
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

        // increment each met key
        for (String n_nucleotide : Split(work_sequence)) {

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
