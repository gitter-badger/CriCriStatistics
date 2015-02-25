import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

public class SimpleParser implements IGenomeParser {
    
    private String sequence;
    private Genome genome;
    private int totalNucleotide;
    private Vector<String> cds;
    
    public SimpleParser(){
      this.sequence = new String();
      this.cds = new Vector<String>();
    } 

    public void test(){
      File file = new File("sample.gb");
      
      try {
        Scanner scan = new Scanner(file);
        extractSequence(scan);
        scan = new Scanner(file);
        extractCDS(scan);

        for(String cdsItem : this.cds){
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
    
    //private boolean checkJoinOperator();
    //private boolean checkComplementOperator();
    //private boolean checkOperator();
    
    private boolean correctStartCodon(String codon){
      //String[] start = new String[8]("");
      int i;

      String[] start = {"ATG","CTG","TTG","GTG","ATA","ATC","ATT","TTA"};
     
      
      //check codon size
      for (i = 0 ; i < start.length ; i++){
        if(codon.substring(0, 3).equals(start[i]));
          return true;
      } 
      
      return false;
    }

    private boolean correctStopCodon(String codon){
      String[] stop = {"TAA","TAG","TGA"};
      int i;

      //check codon size
      for (i = 0 ; i < stop.length ; i++){
        if(codon.substring(codon.length() - 4, codon.length() - 1).equals(stop[i]));
          return true;
      }

      return false;
    }

    private boolean check(){ return true;};

    
    
    public void extractCDS(Scanner genBank){
      String currentLine; 
      String[] splittedLine; 
      
      while (genBank.hasNextLine()){ 
        currentLine = genBank.nextLine();
        splittedLine = currentLine.trim().split("\\s+");
        
        if(splittedLine[0].equals("CDS") ){
          this.cds.add(splittedLine[1]);
        }

      }

    }
    
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
            
            if(currentLine.trim().equals("//"))
              return;
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
