
package statisticscalculator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Aniebiet Akpan
 */
//this class does all the necessary analysis on the data
public class StatisticalAnalysis {
    private double mean, variance, standardDeviation, median;
    private List<Double> inputData = new ArrayList<>();
    private int dataCount; 
    private double minimumValue, maximumValue;
    protected int k; //number of bins
    private double binSize;
    protected double[][] binRanges; //can be accessed to GaussianFittings class
    protected double[] frequencies, normalised; //can be accessed by GaussianFittings class
    
    public StatisticalAnalysis(){
        //initialise variables
        mean = 0;
        standardDeviation = 0;
        variance = 0;
        median = 0;
    }
    //sets the Input data from the controller which gets it from IOOperation
    public void setInputData(List<Double> input){
        inputData = input;
        Collections.sort(inputData); //sort the data
        dataCount = inputData.size();
    }
    //gets the mean
    public double getMean(){
        double sum = 0;
        for(int i=0; i<dataCount; i++)
            sum += inputData.get(i);
        return sum/dataCount;
    }
    //gets the median
    public double getMedian(){
        if (inputData.size() % 2 == 0)
            median = (inputData.get(dataCount/2) + inputData.get(dataCount/2 - 1))/2;
        else
            median = inputData.get(dataCount/2);
        return median;
    }
    //gets the variance
    public double getVariance(){
        //variance is the sum of the squares of deviation from the mean divided by the total data
        double sumOfDeviationSquare = 0;
        double mean = getMean();
        for(int i=0; i<dataCount; i++){
            sumOfDeviationSquare += (inputData.get(i) - mean) * (inputData.get(i) - mean);
        }
        variance = sumOfDeviationSquare/dataCount;
        return variance;
    }
    //gets the standard deviation
    public double getStandardDeviation(){
        standardDeviation = Math.sqrt(getVariance());
        return standardDeviation;
    }
    //gets the minimum value
    public double getMinimum(){
        minimumValue = inputData.get(0);
        for(int i=1; i<dataCount; i++){
            if(inputData.get(i) < minimumValue)
                minimumValue = inputData.get(i);
        }
        return minimumValue;
    }
    //gets the maximum value
    public double getMaximum(){
        maximumValue = inputData.get(0);
        for(int i=1; i<dataCount; i++){
            if(inputData.get(i) > maximumValue)
                maximumValue = inputData.get(i);
        }
        return maximumValue;
    }
    public int getDataCount(){
        return dataCount;
    }
    //get bin number
    public int getBinNumber(){
        return k;
    }
    //get the number of bins with the method as parameter (overloaded)
    public int getBinNumber(String method){
        
        k = 0;
        switch(method){
            case "squareRootBin":
                k = (int)Math.sqrt(dataCount);
                break;
            case "riceBin":
                k = (int)(3.3 * Math.log(dataCount)+1.0);
                break;
            default: //default is Sturges method
                k = (int)( Math.pow(dataCount, 1.0/3.0));
                break;
                //other bin methods can be added to this switch
        }
        return k;
    }
    //gets the bin size
    public double getBinSize(int bNumber){
        binSize = (getMaximum()-getMinimum())/bNumber;
        return binSize;
    }
    //arranges the bins in ranges depending on the binsize
    public double[][] getBinRanges(){
        binSize = getBinSize(k);
        binRanges = new double[k][2];
        binRanges[0][0] = minimumValue;
        binRanges[0][1] = minimumValue + binSize;
        for(int i=1; i<k; i++){
            binRanges[i][0] = binRanges[i-1][1];
            binRanges[i][1] = binRanges[i][0] + binSize;
        }
        //add some tolerance to the last range so all values are included
        binRanges[k-1][1] += binSize;
        return binRanges;
    }
    //returns the frequencies of each data
    public double[] getFrequencies(){
        frequencies = new double[k];
        getBinRanges();
        for(int i=0; i<k; i++){
            frequencies[i] = 0;
            for(int j=0; j<inputData.size(); j++){
                if(inputData.get(j) >= binRanges[i][0] && inputData.get(j) < binRanges[i][1]){
                    frequencies[i] = frequencies[i] + 1;
                }
                if(inputData.get(j) > binRanges[i][1]){
                    break;
                }
            }
        }
        return frequencies;
    }
    //returns the normalised values
    public double[] getNormalised(){
        getFrequencies();
        normalised = new double[k]; 
        for(int i=0; i<k; i++){
            normalised[i] = frequencies[i]/(binSize*dataCount);
        }
        return normalised;
    }
}
