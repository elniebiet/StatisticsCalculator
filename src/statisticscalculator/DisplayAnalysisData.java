
package statisticscalculator;

import javafx.scene.control.TextArea;

/**
 *
 * @author Aniebiet Akpan
 */
//this class is called by the controller to to display the Raw Data Analysis, Statistical Analysis
//and Gaussian fittings Analysis, it accepts Text areas as input and gets 
//Gaussian fittings data from GaussianFittings class
public class DisplayAnalysisData {
    private StatisticalAnalysis statAn;
    
    public DisplayAnalysisData(StatisticalAnalysis stats){
        setStatAnalysis(stats);
    }
    public void displayRawDataAnalysis(TextArea tx){
        tx.setStyle("-fx-font-size: 0.9em;"); //set font size
        String rawData = "";
        rawData += "Number of Data: " + Integer.toString(statAn.getDataCount())+"\n";
        rawData += "Minimum Value (um): " + String.format("%.2e", statAn.getMinimum() * 1000000)+"\n";
        rawData += "Maximum Value(um): " + String.format("%.2e", statAn.getMaximum() * 1000000)+"\n";
        tx.setText(rawData);
    }
    public void displayStatsDataAnalysis(TextArea tx){
        tx.setStyle("-fx-font-size: 0.9em;"); //set font size
        tx.setEditable(false);
        String statAnalysis = "";
        statAnalysis += "Mean Value (um): " + String.format("%.2e", statAn.getMean() * 1000000) + "\n";
        statAnalysis += "Median Value (um): " + String.format("%.2e", statAn.getMedian() * 1000000) + "\n";
        statAnalysis += "Variance (um): " + String.format("%.2e", statAn.getVariance() * 1000000) + "\n";
        statAnalysis += "S. Deviation (um): " + String.format("%.2e", statAn.getStandardDeviation() * 1000000) + "\n";
        tx.setText(statAnalysis);
    }
    public void displayGaussianFittings(TextArea tx, double[][] binRanges, double[] freq, double binN){
        GaussianFittings gf = new GaussianFittings(binRanges, freq, binN);
        tx.setStyle("-fx-font-size: 0.9em;"); //set font size
        tx.setEditable(false);
        String gaussianFits = "";
        gaussianFits += "Norm. Factor: Value (um): " + String.format("%.2e", gf.getNormFactor() * 1000000) + "\n";
        gaussianFits += "Mean (um): " + String.format("%.2e", gf.getMean() * 1000000) + "\n";
        gaussianFits += "Sigma (um): " + String.format("%.2e", gf.getSigma() * 1000000) + "\n";
        tx.setText(gaussianFits);
    }
    public void setStatAnalysis(StatisticalAnalysis st){
        statAn = st;
    }
}
