import org.apache.log4j.Logger;

public class Settings{
  private int numThreads;
  // Singleton Stuff
  private static class SingletonHolder { 
	     public static final Settings INSTANCE = new Settings();
	}
	
  public static Settings getInstance() {
	     return SingletonHolder.INSTANCE;
	}
  // End Singleton Stuff

  private Settings(){
    this.numThreads = 1;
  }
  
  public void setNumThreads(int numThreads){
    this.numThreads = numThreads;
  }
  
  public int getNumThreads(){
    return this.numThreads;
  }

  public int getCleanThreadId(){
    return (int)((Thread.currentThread().getId() % numThreads) + 1 );
  }
  
}
