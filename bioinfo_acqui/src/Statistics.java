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

    public Statistics(Alphabet alphabet)
    {
        shift_window_size = alphabet.item_length;
        phases = new ArrayList<HashMap<String, Integer>>();
        for (HashMap<String, Integer> frequencies : phases) {
            for (String key : alphabet.items) {
                frequencies.put(key, 0);
            }
        }
    }

    public void ComputeFrequencies(String sequence, int phase)
    {
        int true_phase = phase % shift_window_size;
        // ensure the passed String has a size multiple of shift_window_size
        String work_sequence = sequence.substring(
                true_phase, sequence.length() - shift_window_size + true_phase);
    }

    private String[] Split(String s)
    {
        // we could use a loop on s.substring but here we are seeking performance
        int length = s.length();
        String[] result = new String[(length / shift_window_size)];
        char[] temp_array = s.toCharArray();
        // TODO: finish this
        //for (int i=0, int j=0; i<length; i++, j++)
        //{
        //    result[j] = new String(temp_array[i]+temp_array[])
        //}

        return result;
    }
}