import org.apache.log4j.Logger;

public class MediatorGUI implements IMediatorGUI{

  private MainForm gui;
  final static Logger logger = Logger.getLogger(MediatorGUI.class); 
  private static Settings settings = Settings.getInstance();
  

  // Singleton Stuff
  private static class SingletonHolder {
	     public static final MediatorGUI INSTANCE = new MediatorGUI();
	}

  public static MediatorGUI getInstance() {
	     return SingletonHolder.INSTANCE;
	}
  // End Singleton Stuff

  private MediatorGUI(){
    this.gui = null;
  }

  public void setGUI(MainForm gui){
    this.gui = gui;
  }
  
  public void updateAquisitionPanel(String info){
    int id = settings.getCleanThreadId();
    
    if (this.gui!= null){ 
      this.gui.appendTextAreaAcquisition("\n", id);
      this.gui.appendTextAreaAcquisition(info, id);
    }
  }
  
  public void updateParsingPanel(String info){
    int id = settings.getCleanThreadId();
    
    if (this.gui!= null){
      this.gui.appendTextAreaParsing("\n", id);
      this.gui.appendTextAreaParsing(info, id);
    }
  }
  
  public void updateStatisticsPanel(String info){
    int id = settings.getCleanThreadId();
    if (this.gui!= null){
      this.gui.appendTextAreaStatistics(info, id);
    }
  }

  public void updateWritingPanel(String info){
    int id = settings.getCleanThreadId();
    if (this.gui!= null){
      this.gui.appendTextAreaWriting("\n", id);
      this.gui.appendTextAreaWriting(info, id);
    }
  }

  public synchronized void incrementProgressBar(){
    if (this.gui!= null){
      this.gui.incrementProgressBar();
    }
  }

  public void setProgressBar(int nbTotalGenome){
    if (this.gui!= null)
      this.gui.setProgressBar(nbTotalGenome);
  }

  // TODO: write this method
  public void clearAllAreas() {
    return;
  }
}
