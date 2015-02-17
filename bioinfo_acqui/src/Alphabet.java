import java.lang.String;
import java.util.ArrayList;

public class Alphabet {

    public ArrayList<String> items;
    public int item_length;

    // default to A4
    public Alphabet() {
    
        items = new ArrayList<String>();
        item_length = 3;
        GenerateCombinations(item_length, new char[] {'a','c','g','t'}, "");
    }

    public Alphabet(String alphabet, int length) {
    
        items = new ArrayList<String>();
        item_length = length;
        GenerateCombinations(length, RemoveDuplicates(alphabet).toCharArray(), "");
    }

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

    private void GenerateCombinations(int length, char[] alphabet, String curr) {
    
        if (curr.length() == length) {
        
            items.add(curr);
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
