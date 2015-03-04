import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.Stack;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.logging.Level;
import jxl.write.WriteException;
import org.apache.log4j.Logger;

public class SimpleParser implements IGenomeParser {
    
    final static Logger logger = Logger.getLogger(SimpleParser.class); 
    private StringBuilder sequence;
    private Genome genome;
    private int totalNucleotide;
    private Vector<String> cdsInfo;
    private Vector<String> cds;
    private Alphabet a4;
    
    public SimpleParser(){
      this.sequence = new StringBuilder();
      this.cds = new Vector<String>();
      this.cdsInfo = new Vector<String>();
      this.a4 = new Alphabet();
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
    
    private List<Scanner> dupScanner(Scanner toDup){
      Scanner copy1 ;
      Scanner copy2 ;
      StringBuilder src = new StringBuilder();
      String srcFinal;
      List<Scanner> duplicates = new ArrayList<Scanner>();

      while (toDup.hasNextLine() ){
        src.append(toDup.nextLine()); 
        src.append("\n"); 
      }
      
      toDup.close();

      srcFinal = new String(src.toString()); 
      copy1 = new Scanner(srcFinal);
      copy2 = new Scanner(srcFinal);
      duplicates.add(copy1);
      duplicates.add(copy2);

      return duplicates;
    }

    public boolean parseGenome(Genome genome, List<Scanner> genbanksScanner){
      List<Scanner> duplicates;
      this.sequence.setLength(0);
      this.cdsInfo.clear();
      this.cds.clear();
      this.totalNucleotide = -1; 
      
      if( genbanksScanner == null)
        return false;

      for (Scanner scan : genbanksScanner){
        duplicates = dupScanner(scan);
        extractSequence(duplicates.get(0) );
        extractCDSInfo(duplicates.get(1) );
        
        duplicates.get(0).close();
        duplicates.get(1).close();

        for(String cdsItem : this.cdsInfo){
          checkCDSBounds(cdsItem);
        }
        
        
        genome.setNbFailedCDS(this.cds.size() - this.cdsInfo.size());
        genome.setNbCorrectCDS(this.cds.size() );

        if ( this.cds.size() > 0){
            try {
                Statistics stats = new Statistics(this.a4, this.cds, genome);
                System.out.println("gta in seq1+seq2 phase 1: " + stats.phases.get(0).get("gta"));
                stats.print();
                Statistics.Write(stats);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SimpleParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                java.util.logging.Logger.getLogger(SimpleParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

      }

      return true;    
    }
    
    private boolean checkCDSBounds(String bounds){
      boolean isComplement = false;
      
      //logger.debug("For cds:" + bounds );
      
      if (bounds.charAt(0) > '9'){
        //logger.debug("Is an operator " + bounds);
        
        if(checkParentheses(bounds)){
          //logger.debug("Parenthesis OK");
        }
        else{
          //logger.debug("Parenthesis NOT OK");
        }

        if (bounds.startsWith("complement", 0)){
          
          if(!bounds.contains("join")){
            bounds = bounds.substring(11,bounds.length()).replace(")","");
            isComplement = true;
          }
          else{
            isComplement = true;
            bounds = bounds.substring(11,bounds.length());
            
            if(bounds.startsWith("join", 0)){
              Vector<Integer> boundsVector = join(bounds.substring(5, bounds.length()).replace(")","")); 
              extractCDS(boundsVector, isComplement);
              return true;
            } 
          }
        }
        else if(bounds.startsWith("join",0)){
          Vector<Integer> boundsVector = join(bounds.substring(5, bounds.length()).replace(")","")); 
          extractCDS(boundsVector, isComplement);
          return true;
        }
        
        return false;
      }

      String[] number = bounds.split("\\.\\.");
      Vector<Integer> boundVector = new Vector<Integer>();

      if(number.length < 2){
        //throws exception
        System.out.println("Incorrect cds bound:" + bounds);
      }
      else{
        
        try {
          int minBound = Integer.parseInt(number[0]);
          int maxBound = Integer.parseInt(number[1]);
        
          if( minBound > 0 && maxBound < totalNucleotide){
            //System.out.println("Correct cds bound: " + bounds +", "+ number[0] + ", " + number[1]);
            //Convert element position to index
            boundVector.add(minBound - 1);
            boundVector.add(maxBound );
            extractCDS(boundVector, isComplement);
          }
        }
        catch (Exception e){
          logger.error("Bad bounds for CDS:" + bounds );
        }

      }

      return true;
    }
    

    private boolean checkAlphabet(StringBuilder sequence){
      for (int i = 0 ; i < sequence.length() ; i++) {
        
        switch (sequence.charAt(i)){
          case 'a': break;
          case 'c': break;
          case 'g': break;
          case 't': break;
          default: return false;
        }
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
    
    private boolean correctStartCodon(StringBuilder codon){
      //String[] start = new String[8]("");
      int i;

      String[] start = {"atg","ctg","ttg","gtg","ata","atc","att","tta"};
     
      
      //check codon size
      for (i = 0 ; i < start.length ; i++){
        if(codon.substring(0, 3).equals(start[i]))
          return true;
      } 
      
      return false;
    }

    private boolean correctStopCodon(StringBuilder codon){
      String[] stop = {"taa","tag","tga"};
      int i;

      //check codon size

      for (i = 0 ; i < stop.length ; i++){
        if(codon.substring(codon.length() - 3, codon.length() ).equals(stop[i]))
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
        if ( splittedLine.length > 1){
          if(splittedLine[0].equals("CDS") ){
            this.cdsInfo.add(splittedLine[1]);
          }
        }

      }

    }
    
    private boolean checkMax(Vector<Integer> vec, int max){
      
      for (int i = 0; i < vec.size() ; i++ ){
        if (vec.elementAt(i) > max){
          return false;
        }
      } 

      return true;
    }

    public void extractCDS(Vector<Integer> bounds, boolean isComplement){
      StringBuilder newCdsSequence = new StringBuilder();
      
      if (!checkMax(bounds, this.totalNucleotide))
        return;

      for (int i = 0 ; i < bounds.size() - 1 ; i++ ){

        if(bounds.elementAt(i) >= bounds.elementAt(i + 1))
          return;
        else
          newCdsSequence.append(this.sequence.substring(bounds.elementAt(i), bounds.elementAt( i + 1)));
      
      }
      
      if (!correctStartCodon(newCdsSequence)){
        //System.out.println("Incorrect start trinucleotide");
        return;
      }
      
      if (!correctStopCodon(newCdsSequence)){
        //System.out.println("Incorrect stop trinucleotide");
        return;
      }
      else{
        //System.out.println("CORRECT stop trinucleotide");
      }

      if (!((newCdsSequence.length() % 3) == 0) ){
        //System.out.println("Number of nucleotide not a multiple of 3");
      }

      if (!checkAlphabet(newCdsSequence)){
        return;
      }

      if (isComplement){ 
        newCdsSequence = newCdsSequence.reverse();
        newCdsSequence = complement(newCdsSequence);
      }
      
      cds.add(newCdsSequence.toString());

    }
    
    public StringBuilder complement(StringBuilder mutableSequence){

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
      return mutableSequence;
    }

    public Vector<Integer> join(String cdsInfo){
      String[] splittedGroupBounds = cdsInfo.split(",");
      String[] splittedBounds;
      Vector<Integer> individualBound = new Vector<Integer>();

      for (int i = 0 ; i < splittedGroupBounds.length ; i++) {
        splittedBounds = splittedGroupBounds[i].split("\\.\\.");
        
        try{
          individualBound.add(Integer.parseInt(splittedBounds[0]));
          individualBound.add(Integer.parseInt(splittedBounds[1]));
        }
        catch(Exception e){
          logger.error("Bad bounds in join operator CDS:" + cdsInfo );
          logger.error("",e);
        }

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

          while (genBank.hasNextLine()){
            currentLine = genBank.nextLine();
            
            if(currentLine.trim().equals("//")){
              this.totalNucleotide = this.sequence.length();
              return;
            }
            else{
              splittedLine = currentLine.trim().split(" ");
              
              for (i = 1; i < splittedLine.length ; i++ ){ 
                this.sequence.append(splittedLine[i]); 
              }

            }
            
          }
        }
        
      }
    }

}
