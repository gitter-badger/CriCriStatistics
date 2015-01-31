import java.lang.String;
import java.util.ArrayList;

public class Alphabet
{
    public ArrayList<String> items;
    public int item_length;

    // default to A4
    public Alphabet()
    {
        items = new ArrayList<String>();
        item_length = 3;
        GenerateCombinations(item_length, new char[] {'A','C','G','T'}, "");
    }

    public Alphabet(String alphabet, int length)
    {
        items = new ArrayList<String>();
        item_length = length;
        // TODO: remove duplicates in alphabet string
        GenerateCombinations(item_length, alphabet.toCharArray(), "");
    }

    private void GenerateCombinations(int length, char[] alphabet, String curr)
    {
        if (curr.length() == length)
        {
            items.add(curr);
        }
        else
        {
            for (char c : alphabet)
            {
                String oldCurr = curr;
                curr += c;
                GenerateCombinations(length, alphabet, curr);
                curr = oldCurr;
            }
        }
    }
}
