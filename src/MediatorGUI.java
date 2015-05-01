import org.apache.log4j.Logger;

public class MediatorGUI implements IMediatorGUI {

    private MainForm gui;
    final static Logger logger = Logger.getLogger(MediatorGUI.class);
    private static Settings settings = Settings.getInstance();
    final static int max_lines = 1000;


    // Singleton Stuff
    private static class SingletonHolder {
        public static final MediatorGUI INSTANCE = new MediatorGUI();
    }

    public static MediatorGUI getInstance() {
        return SingletonHolder.INSTANCE;
    }
    // End Singleton Stuff

    private MediatorGUI() {
        this.gui = null;
    }

    public void setGUI(MainForm gui) {
        this.gui = gui;
    }

    public void updateAquisitionPanel(String info) {
        int id = settings.getCleanThreadId();

        if (this.gui != null) {

            if (this.gui.countAcqui[id] >= max_lines) {
                this.clearAcquiArea(id);
                this.gui.countAcqui[id] = 0;
            }
                
            this.gui.appendTextAreaAcquisition("\n", id);
            this.gui.appendTextAreaAcquisition(info, id);
            this.gui.countAcqui[id]++;
        }
    }

    public void updateParsingPanel(String info) {
        int id = settings.getCleanThreadId();

        if (this.gui != null) {

            if (this.gui.countParse[id] >= max_lines) {
                this.clearParseArea(id);
                this.gui.countParse[id] = 0;
            }

            this.gui.appendTextAreaParsing("\n", id);
            this.gui.appendTextAreaParsing(info, id);
            this.gui.countParse[id]++;
        }
    }

    public void updateStatisticsPanel(String info) {
        int id = settings.getCleanThreadId();
        
        if (this.gui != null) {

            if (this.gui.countStats[id] >= max_lines) {
                this.clearStatsArea(id);
                this.gui.countStats[id] = 0;
            }

            this.gui.appendTextAreaStatistics(info, id);
        }
    }
    
    public void incrementStatsLines() {
        this.gui.countStats[settings.getCleanThreadId()]++;
    }

    public void incrementStatsLines(int num) {
        this.gui.countStats[settings.getCleanThreadId()] += num;
    }

    public void updateWritingPanel(String info) {
        int id = settings.getCleanThreadId();
        
        if (this.gui != null) {

            if (this.gui.countWrite[id] >= max_lines) {
                this.clearWriteArea(id);
                this.gui.countWrite[id] = 0;
            }

            this.gui.appendTextAreaWriting("\n", id);
            this.gui.appendTextAreaWriting(info, id);
            this.gui.countWrite[id]++;
        }
    }

    public synchronized void incrementProgressBar() {
        if (this.gui != null) {
            this.gui.incrementProgressBar();
        }
    }

    public void setProgressBar(int nbTotalGenome) {
        if (this.gui != null)
            this.gui.setProgressBar(nbTotalGenome);
    }
    
    public void clearAcquiArea(int id) {
        this.gui.clearAcquiArea(id);
    }

    public void clearParseArea(int id) {
        this.gui.clearParseArea(id);
    }

    public void clearStatsArea(int id) {
        this.gui.clearStatsArea(id);
    }

    public void clearWriteArea(int id) {
        this.gui.clearWriteArea(id);
    }

    public void clearAllAreas() {
        this.gui.clearAllAcquiArea();
        this.gui.clearAllParseArea();
        this.gui.clearAllStatsArea();
        this.gui.clearAllWriteArea();
    }
}
