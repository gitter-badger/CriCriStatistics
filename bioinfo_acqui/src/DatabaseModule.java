import com.almworks.sqlite4java.*;
import java.io.File;

public class DatabaseModule{
  //private SQLiteConnection db; 
  private static SQLiteQueue queue;
  
  
  // Singleton Stuff
	/*
  private static class SingletonHolder { 
	     public static final DataAccess INSTANCE = new DataAccess();
	}
	public static DataAccess getInstance() {
	     return SingletonHolder.INSTANCE;
	}
	*/
  // End Singleton Stuff

  public DatabaseModule(){
    //this.db = new SQLiteConnection(new File("database"));
    this.queue = new SQLiteQueue(new File("database"));
    
    /*
    try{
      this.db.open(true);
    }
    catch(Exception e){
      e.printStackTrace();   
    }
    */

    this.queue.start();
    
    queue.execute(new SQLiteJob<Object>() {
      protected Object job(SQLiteConnection connection) throws SQLiteException {
        try{
          SQLiteStatement statement = connection.prepare("CREATE TABLE IF NOT EXISTS genome_version (id INT PRIMARY KEY NOT NULL, hash CHAR(50));");
          statement.stepThrough();
          statement.dispose();
          System.out.println("table is created");
          return null;
        }
        catch (SQLiteException ex){
          System.out.println("Query Execution SQLiteException: " + ex.getMessage());
          return null;
        }
      }
    });
    /* 
    try{
      queue.stop(true).join();
    }
    catch(Exception e){
      System.out.println("Database queue execution failed: " + e.getMessage());
    }
    */
  }

  private void addGenomeEntry(int genomeId, String hash){
    
    final int  id = genomeId;
    final String h = hash;

    this.queue.execute(new SQLiteJob<Object>() {
      protected Object job(SQLiteConnection connection) throws SQLiteException {
        try{
          SQLiteStatement statement = connection.prepare("INSERT INTO genome_version (id, hash) VALUES(" + id + ",\"" + h + "\");");
          statement.stepThrough();
          statement.dispose();
        } 
        catch (SQLiteException ex){
          System.out.println("Query Execution SQLiteException: " + ex.getMessage());
        }
        
        return null;
      }
    });
  }
  
  private String getGenomeEntry(int genomeId){
      /* 
      try{ 
        queue.stop(true).join();
      }
      catch(Exception e){
        System.out.println("Database queue execution failed: " + e.getMessage());
      }
      */
      final int id = genomeId;

      String hash = (String) this.queue.execute(new SQLiteJob<Object>() {
        protected Object job(SQLiteConnection connection) throws SQLiteException {
          try{
            SQLiteStatement st = connection.prepare("SELECT * FROM genome_version WHERE id=" + id + ";");
            System.out.println("TOTO ?");
            if(!st.step()){
              st.dispose();
              return null;
            }
            else{
              if(st.hasRow()) {
                //int columns = st.columnCount();
                
                //for(int column = 0 ; column < columns ; column++)
                return (String) (st.columnValue(1));
                
                //return stack.toArray();
              } 
              else {
                st.dispose();
                return (String) null;
              }
            }
          } 
          catch (SQLiteException ex){
            System.out.println("Fetch SQLiteException: " + ex.getMessage());
          }
          return null;
        }
      }).complete();
      
      return hash;
  }
  
  private void updateHash(int genomeId, String hash){
    final int  id = genomeId;
    final String h = hash;
    
    this.queue.execute(new SQLiteJob<Object>() {
      protected Object job(SQLiteConnection connection) throws SQLiteException {
        try{
          SQLiteStatement statement = connection.prepare("UPDATE genome_version SET hash =\"" + h + "\" WHERE id=" + id + ";");
          statement.stepThrough();
          statement.dispose();
        } 
        catch (SQLiteException ex){
          System.out.println("Query Execution SQLiteException: " + ex.getMessage());
        }
        
        return null;
      }
    });
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
        updateHash(genomeId, hash);
        return false;
      }
    }
  }

}
