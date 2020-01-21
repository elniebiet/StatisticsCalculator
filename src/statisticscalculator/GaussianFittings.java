package statisticscalculator;

import javafx.scene.control.TextArea;
import org.apache.commons.math3.fitting.*;

/**
 *
 * @author Aniebiet Akpan
 */

//this class gets the gaussian fittings for the supplied data
public class GaussianFittings extends GaussianHandler  {
    private static double parameters[];    
    WeightedObservedPoints obs;
    private double[][] BinRanges;
    private double[] Frequencies, GaussianPDFResult;
    private double BinNumber;
    
    GaussianFittings(double[][] bR, double[] freqs, double binN){
        BinRanges = bR;
        Frequencies = freqs;
        BinNumber = binN;
        addWeightedData();
        parameters = GaussianCurveFitter.create().fit(obs.toList());
    }
    //gets normalisation factor 
    public double getNormFactor(){
        return parameters[0];
    }
    //gets the mean
    public double getMean(){
        return parameters[1];
    }
    //gets the stamdard deviation
    public double getSigma(){
        return parameters[2];
    }
    //adds the weighted data for gaussian analysis
    private void addWeightedData(){
        obs = new WeightedObservedPoints();
        for(int i=0; i<BinNumber-1; i++){
            obs.add(BinRanges[i][0],Frequencies[i]);
        }
        obs.add(BinRanges[(int)BinNumber-1][1],Frequencies[(int)BinNumber-1]);
    }
    //gets the gaussian fittings
    public double[] getGaussianPDFResult(){
        GaussianPDFResult = new double[(int)BinNumber];
        for(int i=0; i<BinNumber; i++){
            GaussianPDFResult[i] =  parameters[0] * Math.exp((-0.5*Math.pow((BinRanges[i][0]-parameters[1])/parameters[2],2.0)));
        }
        return GaussianPDFResult;
    }
}