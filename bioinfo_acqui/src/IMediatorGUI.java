public interface IMediatorGUI{
  public void updateAquisitionPanel(String info, int threadId);
  public void updateParsingPanel(String info, int threadId);
  public void updateStatisticsPanel(String info, int threadId);
  public void incrementProgressBar();
  public void setProgressBar(int nbTotalGenome);
}
