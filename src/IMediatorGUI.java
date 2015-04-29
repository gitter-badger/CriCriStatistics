public interface IMediatorGUI {
    public void updateAquisitionPanel(String info);

    public void updateParsingPanel(String info);

    public void updateStatisticsPanel(String info);

    public void updateWritingPanel(String info);

    public void incrementProgressBar();

    public void setProgressBar(int nbTotalGenome);
}
