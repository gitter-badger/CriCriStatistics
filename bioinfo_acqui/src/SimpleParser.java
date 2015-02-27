import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.Stack;
import java.lang.StringBuilder;


/*TODO: Must change everything to stringBuffer/builder ...*/
public class SimpleParser implements IGenomeParser {
    
    private String sequence;
    private Genome genome;
    private int totalNucleotide;
    private Vector<String> cdsInfo;
    private Vector<String> cds;
    
    public SimpleParser(){
      this.sequence = new String();
      this.cds = new Vector<String>();
      this.cdsInfo = new Vector<String>();
    } 

    public void test(){
      File file = new File("sample.gb");
      
      try {
        Scanner scan = new Scanner(file);
        extractSequence(scan);
        scan = new Scanner(file);
        extractCDSInfo(scan);

        for(String cdsItem : this.cdsInfo){
          checkCDSBounds(cdsItem);
        }
      }
      catch (Exception e){
        e.printStackTrace();
      }
    }

    public boolean parseGenome(Genome genome, List<Scanner> genbanksScanner){
      return false;
    }

    private boolean checkCDSBounds(String bounds){
      if (bounds.charAt(0) > '9'){
        System.out.println("Is an operator " + bounds);
        
        if(checkParentheses(bounds))
          System.out.println("Parenthesis OK");
        else
          System.out.println("Parenthesis NOT OK");

        if (bounds.startsWith("complement",0))
          System.out.println("Is an complement operator");
        else if(bounds.startsWith("join",0))
          System.out.println("Is join");
        
        return false;
      }

      String[] number = bounds.split("\\.\\.");

      if(number.length < 2){
        //throws exception
        System.out.println("Incorrect cds bound:" + bounds);
      }
      else{
        
        if(Integer.parseInt(number[0]) > 0 || Integer.parseInt(number[1]) < totalNucleotide)
        System.out.println("Correct cds bound: " + bounds +", "+ number[0] + ", " + number[1]);
      
      }

      return true;
    }
    
    public boolean checkParentheses(String expr){
      Stack<Character> stack = new Stack<Character>();

      for (int i = 0 ; i < expr.length() ; i++ ){
         if (expr.charAt(i) == '(')
           stack.push('(');
         else if (expr.charAt(i) == ')'){
           
           if (stack.empty())
             //except
             return false;
           
           if (stack.peek() == '(')
             stack.pop();
           else
             //except
             return false;
        }

      }

      return stack.empty();
    }
    
    //private boolean checkJoinOperator();
    //private boolean checkComplementOperator();
    //private boolean checkOperator();
    
    private boolean correctStartCodon(String codon){
      //String[] start = new String[8]("");
      int i;

      String[] start = {"atg","ctg","ttg","gtg","ata","atc","att","tta"};
     
      
      //check codon size
      for (i = 0 ; i < start.length ; i++){
        if(codon.substring(0, 3).equals(start[i]));
          return true;
      } 
      
      return false;
    }

    private boolean correctStopCodon(String codon){
      String[] stop = {"taa","tag","tga"};
      int i;

      //check codon size
      for (i = 0 ; i < stop.length ; i++){
        if(codon.substring(codon.length() - 4, codon.length() - 1).equals(stop[i]));
          return true;
      }

      return false;
    }

    private boolean check(){ return true;};

    
    
    public void extractCDSInfo(Scanner genBank){
      String currentLine; 
      String[] splittedLine; 
      
      while (genBank.hasNextLine()){ 
        currentLine = genBank.nextLine();
        splittedLine = currentLine.trim().split("\\s+");
        
        if(splittedLine[0].equals("CDS") ){
          this.cdsInfo.add(splittedLine[1]);
        }

      }

    }
    
    public void extractCDS(Vector<Integer> bounds, boolean isComplement){
      String newCdsSequence = new String();
      
      for (int i = 0 ; i < bounds.size() - 1 ; i++ ){
        newCdsSequence += sequence.substring(bounds.elementAt(i), bounds.elementAt( i + 1));
      }
      
      if (isComplement){ 
        newCdsSequence = new StringBuilder(newCdsSequence).reverse().toString();
        newCdsSequence = complement(newCdsSequence);
      }

      cds.add(newCdsSequence);

    }
    
    public String complement(String sequence){
     StringBuilder mutableSequence  = new StringBuilder(sequence);

      for (int i = 0; i < sequence.length() ; i++ ){
        
        switch (sequence.charAt(i)){
          case 'a': mutableSequence.setCharAt(i,'t');
                    break;
          case 'c': mutableSequence.setCharAt(i,'g');
                    break;
          case 'g': mutableSequence.setCharAt(i,'c');
                    break;
          case 't': mutableSequence.setCharAt(i,'a');
                    break;
        }
      
      } 
      return mutableSequence.toString();
    }

    public Vector<Integer> join(String cdsInfo){
      String[] splittedGroupBounds = cdsInfo.split(",");
      String[] splittedBounds;
      Vector<Integer> individualBound = new Vector<Integer>();

      for (int i = 0 ; i < splittedGroupBounds.length ; i++) {
        splittedBounds = splittedGroupBounds[i].split("\\.\\.");
        individualBound.add(Integer.parseInt(splittedBounds[0]));
        individualBound.add(Integer.parseInt(splittedBounds[1]));
      }
      
      return individualBound;
    }

    //public processCDSInfo()
    public void extractSequence(Scanner genBank){
      String currentLine;
      String[] splittedLine;
      int i;

      //throw parsing exception
      //when a problem occured
      while (genBank.hasNextLine()){ 
        currentLine = genBank.nextLine();
        
        //if(splittedLine[0].trim().equals("ORIGIN") ){
        if(currentLine.trim().equals("ORIGIN") ){
          System.out.println("Yay !");

          while (genBank.hasNextLine()){
            currentLine = genBank.nextLine();
            
            if(currentLine.trim().equals("//")){
              this.totalNucleotide = this sequence.length();
              return;
            }
            else{
              splittedLine = currentLine.trim().split(" ");
              
              for (i = 1; i < splittedLine.length ; i++ ){ 
                this.sequence += splittedLine[i]; 
              }

            }
            
          }
        }
        
      }
    }

}
