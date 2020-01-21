/*
 * Container for plotting the histogram
 */
package statisticscalculator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
import javafx.scene.text.Font;

/**
 * @author Aniebiet Akpan
 */
//this class plots the histogram and pdfCurve
public class HistogramPlotter implements Plottable {
    private double[][] binRanges;
    private double[] normalisedValues, frequencies;
    private int binNumber;
    private double writeHeight, writeWidth, yHeight, xWidth, maxFreq;
    GraphicsContext gc;
    // constructor
    public HistogramPlotter(double[][] bR, double[] nV, double[] frq, int bN, GraphicsContext gCont){
        //initialise data needed
         binRanges = bR;
         normalisedValues = nV;
         frequencies = frq;
         binNumber = bN;
         gc = gCont;
    }
    
    public void plotHistogram(){
        xWidth = 680.0;
        yHeight = 805.0;
        
        initialise(gc); //draw horizontal and vertical lines
        
        //get maxFreq
        maxFreq = 0.0;
        for(int i=0; i<binNumber; i++){
            if(normalisedValues[i] > maxFreq){
                maxFreq = normalisedValues[i];
            }
        }
        //validate bin number
        if(!(binNumber > 1)){
            JOptionPane.showMessageDialog(null, "Bin number is too small please select a valid file.", "Bin Number Error: ", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //fill horizontal points
        double startH = 70.0, stopH = 70+xWidth;
        writeWidth = xWidth;
        double hInterval = writeWidth/10, maximum = binRanges[binNumber-1][1], minimum =  binRanges[0][0];
        double rangeInterval = ( maximum - minimum )/10;
        
        String lowRange = "", highRange = "";
        double cnt,currentVal; //currentVal starts at minimum data value
        for(cnt=startH, currentVal = minimum; currentVal < maximum; cnt+=hInterval, currentVal+=rangeInterval){
            gc.fillText("|", cnt-1, yHeight + 24);
            lowRange = String.format("%.2e", currentVal*1000000);
            gc.setFont(new Font("Monospaced", 11));
            gc.fillText(lowRange, cnt-32, 840);
        }
        gc.fillText("|", cnt-1, yHeight + 24);
        lowRange = String.format("%.2e", maximum*1000000);
        gc.setFont(new Font("Monospaced", 11));
        gc.fillText(lowRange, cnt-32, 840);
        
        //fill vertical points
        //get max frequency
        double startV = 820.0, stopV = 20.0;
        writeHeight = startV - stopV;
        double vInterval = writeHeight/10, frequencyInterval = maxFreq/10;
        String freq = "";
        for(double i = startV-vInterval, j=frequencyInterval; i >= stopV; i -= vInterval, j += frequencyInterval){
            gc.fillText("-", 65, i);
            freq = String.format("%.2e", j);
            gc.setFont(new Font("Monospaced", 11));
            gc.fillText(freq, 20, i);
        }
        
        //plot the histogram
        double binWidth = (writeWidth / binNumber) * (4.0 / 5.0);
        double binHeight = 0, binStartX = startH, maxBinHeight = writeHeight;
        double xCord = binStartX, yCord;
        for (int i = 0; i < binNumber; i++) {
            binHeight = normalisedValues[i] / maxFreq * maxBinHeight;
            yCord = yHeight - binHeight + 15;
            this.drawShape("RectangleShape", String.valueOf(xCord) + ";" + String.valueOf(yCord) + ";" + String.valueOf(binWidth) + ";" + String.valueOf(binHeight) + ";", gc);
            xCord += (writeWidth / binNumber);
        }
        
    }
    
    public void drawPDFCurve(){
        //get Gaussian fittings results
        GaussianFittings gf = new GaussianFittings(binRanges, frequencies, binNumber);
        double[] pdfResults = gf.getGaussianPDFResult();
        //get max of the results
        double maxPDF = pdfResults[0];
        for(int i=0; i<binNumber; i++){
            maxPDF = (pdfResults[i] > maxPDF) ? pdfResults[i]: maxPDF;
        }
        
        //plot the curve
        double startH = 70;
        double binWidth = (writeWidth / binNumber) * (4.0 / 5.0);
        double pointHeight  = 0;
        double xCord = startH+binWidth/2, yCord = 0;
        double ovalHeight = 5, ovalWidth = 5;
        double nextXCord = 0, nextYCord = 0, nextPointHeight = 0;
        writeHeight -= 10;
        int i;
        for (i = 0; i < binNumber-1; i++) {
            pointHeight = pdfResults[i]/maxPDF * writeHeight;
            nextPointHeight = pdfResults[i+1]/maxPDF * writeHeight;
            yCord = yHeight - pointHeight;
            nextXCord = xCord + (writeWidth / binNumber);
            nextYCord = yHeight - nextPointHeight; 
            this.drawShape("OvalShape", String.valueOf(xCord) + ";" + String.valueOf(yCord) + ";" + String.valueOf(ovalWidth) + ";" + String.valueOf(ovalHeight) + ";", gc);
            this.drawShape("LineShape", String.valueOf(xCord) + ";" + String.valueOf(yCord) + ";" + String.valueOf(nextXCord) + ";" + String.valueOf(nextYCord) + ";", gc);
            xCord += (writeWidth / binNumber);
        }
        //drawLast ovalShape
        pointHeight = pdfResults[binNumber-1]/maxPDF * writeHeight;
        yCord = yHeight - pointHeight;
        this.drawShape("OvalShape", String.valueOf(xCord) + ";" + String.valueOf(yCord) + ";" + String.valueOf(ovalWidth) + ";" + String.valueOf(ovalHeight) + ";", gc);
    }
    //since there is no specific java class for writing vertically on the canvas
    //we can use this function anytime we need to write vertically
    public void writeVertically(String text, GraphicsContext gc, double xTranslate, double yTranslate, double xPos, double yPos){
        gc.save();
        gc.translate(xTranslate, yTranslate);
        gc.rotate(-90);
        gc.fillText(text, xPos, yPos);
        gc.restore();
    }
    //Draws shapes. Shapes not included can be added here
    public void drawShape(String shape, String props, GraphicsContext gc){
        if(shape.compareTo("LineShape") == 0){
             double[] userInputDataDouble = DrawUtils.readStringInfo(props);
             double x1 = userInputDataDouble[0], y1 = userInputDataDouble[1];
             double x2 = userInputDataDouble[2], y2 = userInputDataDouble[3];
             gc.strokeLine(x1,y1,x2,y2);
        }
        if(shape.compareTo("RectangleShape") == 0){
            double[] userInputDataDouble = DrawUtils.readStringInfo(props);
            double xCord = userInputDataDouble[0], yCord = userInputDataDouble[1];
            double width = userInputDataDouble[2], height = userInputDataDouble[3];
            gc.setFill(Color.RED);
            gc.fillRect(xCord,yCord,width,height);
        }
        if(shape.compareTo("OvalShape") == 0){
            double[] userInputDataDouble = DrawUtils.readStringInfo(props);
            double xCord = userInputDataDouble[0], yCord = userInputDataDouble[1];
            double width = userInputDataDouble[2], height = userInputDataDouble[3];
            gc.strokeOval(xCord,yCord,width,height);
        }
        //add any other shape here
    }
    //initialise the canvas
    public void initialise(GraphicsContext gc){
        //draw vertical and horizontal lines.         
        gc.fillText("Measurement Data (um)", 300, 860);
        writeVertically("Frequency", gc, -388.0, 400.0, 30.0, 400.0);
        //draw the vertical and horizontal axis
        this.drawShape("LineShape", "70.0;15.0;70.0;820.0;", gc); //vertical line
        this.drawShape("LineShape", "70.0;820.0;750.0;820.0;", gc); //horizontal
        this.drawShape("LineShape", "750.0;15.0;750.0;820.0;", gc); //right vertical line
        this.drawShape("LineShape", "70.0;15.0;750.0;15.0;", gc); //top horizontal line
        //origin 0
        gc.setFont(new Font("Monospaced", 11));
        gc.fillText("0", 60, 830);

    }
}
