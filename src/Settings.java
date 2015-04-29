import org.apache.log4j.Logger;

public class Settings {

    private int numThreads;
    private boolean active;

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
        this.active = true;
    }

    public void setNumThreads(int numThreads){
        this.numThreads = numThreads;
    }

    public int getNumThreads(){
        return this.numThreads;
    }

    public int getCleanThreadId(){
        return (int)((Thread.currentThread().getId() % numThreads));
    }

    public void turnON() {
        this.active = true;
    }

    public void turnOFF() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }
}
