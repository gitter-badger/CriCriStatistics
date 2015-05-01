public interface IMediatorGUI {
    public void updateAquisitionPanel(String info);

    public void updateParsingPanel(String info);

    public void updateStatisticsPanel(String info);

    public void incrementStatsLines();

    public void incrementStatsLines(int num);

    public void updateWritingPanel(String info);

    public void incrementProgressBar();

    public void setProgressBar(int nbTotalGenome);

    public void clearAcquiArea(int id);

    public void clearParseArea(int id);

    public void clearStatsArea(int id);

    public void clearWriteArea(int id);

    public void clearAllAreas();
}
