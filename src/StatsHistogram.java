import java.awt.Color;
import java.awt.Font;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * This demo shows a simple bar chart created using the {@link XYSeriesCollection} dataset.
 *
 */
public class StatsHistogram extends ApplicationFrame {
    
    IntervalXYDataset[] datasetPhases;
     
    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public StatsHistogram(final String title, Statistics stat) {
        super(title);
        datasetPhases = new IntervalXYDataset[3];
        createDataset(stat);
        JFreeChart chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public StatsHistogram(final String title, WrittenStats stat) {
        super(title);
        datasetPhases = new IntervalXYDataset[3];
        createDataset(stat);
        JFreeChart chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private void createDataset(Statistics stats) {
        final XYSeries[] seriesPhases = new XYSeries[3];
        int i = 0, j = 0;
        seriesPhases[0] = new XYSeries("Phases 1");
        seriesPhases[1] = new XYSeries("Phases 2");
        seriesPhases[2] = new XYSeries("Phases 3");
        
        
        for (i = 0; i < 3 ; i++){ 
          j = 0;
          for (Map.Entry<String, Integer> entry : stats.phases.get(i).entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            seriesPhases[i].add(j, value);
            j++;
          }
        } 

        for (i = 0; i < 3; i++){
          datasetPhases[i] = new XYSeriesCollection(seriesPhases[i]);
        }

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private void createDataset(WrittenStats stats) {
        final XYSeries[] seriesPhases = new XYSeries[3];
        int i = 0, j = 0;
        seriesPhases[0] = new XYSeries("Phases 1");
        seriesPhases[1] = new XYSeries("Phases 2");
        seriesPhases[2] = new XYSeries("Phases 3");
        
        
        for (i = 0; i < 3 ; i++){ 
          j = 0;
          for (Map.Entry<String, Float> entry : stats.phases_freq.get(i).entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            seriesPhases[i].add(j, value);
            j++;
          }
        } 

        for (i = 0; i < 3; i++){
          datasetPhases[i] = new XYSeriesCollection(seriesPhases[i]);
        }

    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart() {
        final JFreeChart chart = ChartFactory.createXYBarChart(
            "XY Series Demo",
            "X", 
            false,
            "Y", 
            datasetPhases[0],
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        return chart;    
    }

    public void showStatsHistogram(){
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

}
