import org.apache.log4j.Logger;

public class MediatorGUI implements IMediatorGUI{

  private MainForm gui;
  final static Logger logger = Logger.getLogger(MediatorGUI.class);

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

  public void updateAquisitionPanel(String info, int threadId){
    if (this.gui!= null){
      this.gui.appendTextAreaAcquisition("\n");
      this.gui.appendTextAreaAcquisition(info);
    }
  }

  public void updateParsingPanel(String info, int threadId){
    if (this.gui!= null){
      this.gui.appendTextAreaParsing("\n");
      this.gui.appendTextAreaParsing(info);
    }
  }

  public void updateStatisticsPanel(String info, int threadId){
    if (this.gui!= null){
//      this.gui.appendTextAreaStatistics("\n");
      this.gui.appendTextAreaStatistics(info);
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
}
