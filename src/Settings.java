import org.apache.log4j.Logger;

import java.io.File;

public class Settings {

    private int numThreads;
    private boolean active;
    private boolean saveData;
    private String outputDir;

    // Singleton Stuff
    private static class SingletonHolder {
        public static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance() {
        return SingletonHolder.INSTANCE;
    }
    // End Singleton Stuff

    private Settings() {
        this.numThreads = 1;
        this.active = true;
        this.outputDir = System.getProperty("user.home") + File.separator + "Statistics";
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumThreads() {
        return this.numThreads;
    }

    public int getCleanThreadId() {
        return (int) ((Thread.currentThread().getId() % numThreads));
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

    public void activateSaveData() {
        this.saveData = true;
    }

    public void deactivateSaveData() {
        this.saveData = false;
    }

    public boolean savingData() {
        return this.saveData;
    }

    public void setOutputDir(String dir) {
        this.outputDir = dir;
    }

    public String getOutputDir() {
        return this.outputDir;
    }
}
