import com.almworks.sqlite4java.*;
import java.io.File;

public class DatabaseModule{
  private static SQLiteQueue queue;
  
  // Singleton Stuff
  private static class SingletonHolder { 
	     public static final DatabaseModule INSTANCE = new DatabaseModule();
	}
	
  public static DatabaseModule getInstance() {
	     return SingletonHolder.INSTANCE;
	}
  // End Singleton Stuff


  private DatabaseModule(){
    queue = new SQLiteQueue(new File(System.getProperty("user.home") + File.separator + "GenBanksDB.sqlite"));
    queue.start();
    
    queue.execute(new SQLiteJob<Object>() {
      protected Object job(SQLiteConnection connection) throws SQLiteException {
        try{
          SQLiteStatement statement = connection.prepare("CREATE TABLE IF NOT EXISTS genome_version (id INT PRIMARY KEY NOT NULL, hash CHAR(50));");
          statement.stepThrough();
          statement.dispose();
          System.out.println("Database is ready.");
          return null;
        }
        catch (SQLiteException ex){
          System.out.println("Query Execution SQLiteException: " + ex.getMessage());
          return null;
        }
      }
    });
  }


  private void addGenomeEntry(int genomeId, String hash){
    final int  id = genomeId;
    final String h = hash;

    queue.execute(new SQLiteJob<Object>() {
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
      final int id = genomeId;

      String hash = (String) queue.execute(new SQLiteJob<Object>() {
        protected Object job(SQLiteConnection connection) throws SQLiteException {
          try{
            SQLiteStatement st = connection.prepare("SELECT * FROM genome_version WHERE id=" + id + ";");
            if(!st.step()){
              st.dispose();
              return null;
            }
            else{
              if(st.hasRow()) {
                return (String) (st.columnValue(1));
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
    
    queue.execute(new SQLiteJob<Object>() {
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
  
  public boolean genomeEntryExist(int genomeId){
    
    if(getGenomeEntry(genomeId) == null)
      return false;
    else
      return true;
  }

  public void gracefulStop(){
    try{ 
      queue.stop(true).join();
    }
    catch(Exception e){
      System.out.println("Database queue execution failed: " + e.getMessage());
    }
  }
}
