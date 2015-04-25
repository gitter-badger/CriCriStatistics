import java.lang.String;
import java.util.ArrayList;

public class Alphabet {

    public ArrayList<String> words;
    public int word_length;

    // default to A4
    public Alphabet() {
    
        words = new ArrayList<String>();
        word_length = 3;
        GenerateCombinations(word_length, new char[] {'a','c','g','t'}, "");
    }

    /* This constructor is able to construct any type of alphabet,
    * given letters and the length of the words that compose it. */
    public Alphabet(String alphabet, int length) {
    
        words = new ArrayList<String>();
        word_length = length;
        GenerateCombinations(length, RemoveDuplicates(alphabet).toCharArray(), "");
    }

    /* This method can be useful if you want to build an alphabet based
    * of an actual sequence of words. It returns a string composed of
    * copies of each character met. Example: "ACTCTGGATTATCGAATCGAT" -> "ACTG". */
    private String RemoveDuplicates(String s) {
        StringBuilder noDupes = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            
            String si = s.substring(i, i + 1);
            if (noDupes.indexOf(si) == -1) {
                
                noDupes.append(si);
            }
        }
        return noDupes.toString().toLowerCase();
    }

    /* Given a sequence of letters, computes and returns each combination of these letters. */
    private void GenerateCombinations(int length, char[] alphabet, String curr) {
    
        if (curr.length() == length) {
        
            words.add(curr);
        }
        else {
        
            for (char c : alphabet) {
            
                // TODO: could be optimized with a StringBuilder
                String oldCurr = curr;
                curr += c;
                GenerateCombinations(length, alphabet, curr);
                curr = oldCurr;
            }
        }
    }
}
