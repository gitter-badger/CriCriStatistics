import com.almworks.sqlite4java.*;
import java.io.File;

public class DatabaseModule{
  private SQLiteConnection db; 
  
  public DatabaseModule(){
    this.db = new SQLiteConnection(new File("database"));
    
    try{
      this.db.open(true);
    }
    catch(Exception e){
      e.printStackTrace();   
    }
    
    try{
      SQLiteStatement statement = this.db.prepare("CREATE TABLE IF NOT EXISTS genome_version (id INT PRIMARY KEY NOT NULL, hash CHAR(50));");
      statement.stepThrough();
      statement.dispose();
    } 
    catch (SQLiteException ex){
      System.out.println("Query Execution SQLiteException: " + ex.getMessage());
    }
  }

  private void addGenomeEntry(int genomeId, String hash){
    try{
      SQLiteStatement statement = this.db.prepare("INSERT INTO genome_version VALUES("+genomeId+","+hash+");");
      statement.stepThrough();
      statement.dispose();
    } 
    catch (SQLiteException ex){
      System.out.println("Query Execution SQLiteException: " + ex.getMessage());
    }
  }
  
  private String getGenomeEntry(int genomeId){
      
      try{
        SQLiteStatement st = this.db.prepare("SELECT * FROM genome_version WHERE id="+genomeId+";");
        System.out.println("TOTO ?");
        if(!st.step()){
          st.dispose();
          return null;
        }
        else{
          if(st.hasRow()) {
            //int columns = st.columnCount();
            
            //for(int column = 0 ; column < columns ; column++)
            return (String)(st.columnValue(0));
            
            //return stack.toArray();
          } 
          else {
            st.dispose();
            return null;
          }
        }
      } 
      catch (SQLiteException ex){
        System.out.println("Fetch SQLiteException: " + ex.getMessage());
      }
      
      return null;
  }

  public boolean updateGenomeEntry(int genomeId, String hash){
    String oldHash = getGenomeEntry(genomeId);

    if(oldHash == null){
      addGenomeEntry(genomeId, hash);
      return false;
    }
    else{
      if (oldHash.equals(hash)){
        return true;
      }
      else{
        addGenomeEntry(genomeId, hash);
        return false;
      }
    }
  }

}
