/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statisticscalculator;

/**
 *
 * @author anieb
 */
public abstract class GaussianHandler extends StatisticalAnalysis {
    //at minimum any Gaussian class should be able to provide
    //the mean, normalisation factor and standard deviation (sigma).
    private double mean;
    private double sigma;
    private double normFactor;
    
    public double getMean(){
        return mean;
    }
    public double getSigma(){
        return mean;
    }
    public double getNormFactor(){
        return mean;
    }
}
