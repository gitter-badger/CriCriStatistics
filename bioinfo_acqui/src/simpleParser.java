import java.util.List;
import java.util.Scanner;

public class SimpleParser implements IGenomeParser {
    
    public SimpleParser(){
    }

    public boolean parseGenome(Genome genome, List<Scanner> genbanksScanner){
    }

    private boolean checkCDSBounds(String bounds){
      String[] number = bounds.split("..");

      if(number.length() < 2){
        //throws exception
        System.out.println("Incorrect cds bound:" + bounds);
      }
      else{ 
        if(number[i] < 0 || number[i] > total)
        System.out.println("Correct cds bound:" + number[0] + "," + number[1]);
      
      }


    }
    
    private boolean checkJoinOperator();
    private boolean checkComplementOperator();
    private boolean checkOperator();
    
    private boolean correctStartCodon(String codon){
      //String[] start = new String[8]("");
      String[] start = {"ATG","CTG","TTG","GTG","ATA","ATC","ATT","TTA"};
     
      for (i = 0 ; i < start.length ; i++){
        if(codon == start[i])
          return true;
      } 
      
      return false;
    }

    private boolean correctStopCodon(){
      String[] stop = {"TAA","TAG","TGA"};
      
      for (i = 0 ; i < stop.length ; i++){
        if(codon == stop[i])
          return true;
      }

      return false;
    }

    private boolean check();

    private String Sequence;
    private Genome genome;
    private int totalNucleotide;
    
    private interestingLinePrefixe(String token){
      boolean result = false;

      if( token == "ID" ){
        result = true;
      }
      else if( token == "FT"){
        result = true;
      }
      else if( token == "SQ"){
        result = true;
      }

      return result;
    }

    public Parser(Genome genome, List<Scanner> genbanksScanner){
      String currentLine = genbanksScanner.nextLine();
      String[] splittedLine = currentLine.split("' '*");
      
      if(interestingLinePrefixe(splittedLine[1])){
        if(splittedLine[2] == "CDS" ){
          if(checkCDSBounds(splittedLine[3])){
            getCDS()
          } 
        }    
      }


}
