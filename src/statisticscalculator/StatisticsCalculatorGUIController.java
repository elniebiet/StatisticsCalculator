/**
 * This is the 'StatisticsCalculatorGUI.fxml' Controller Class
 */

package statisticscalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.MenuButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.RadioButton;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;


//==============================================================================
/* This is the controller class for StatisticsCalculatorGUI fxml
*/
//==============================================================================
public class StatisticsCalculatorGUIController {
    
    // =========================================================================
    /*
    private variables needed for our the Histogram Plotter
    */
    // =========================================================================
    private HistogramPlotter SCHistogramDrawing;
    private String selectedBinMethod; 
    private List<Double> inputData = new ArrayList<>(); 
    private int binNumber;
    private double binRanges[][]; //for display
    private double normalised[]; //for display
    private double frequencies[]; //for display
    private int toggleState = 0; //toggle State for pdf curve
    
    @FXML // fx:id="SCCanvas"
    private Canvas SCCanvas;
    
    @FXML //fx:id="importData"
    private MenuItem importData;
    
    @FXML //fx:id="openImage"
    private MenuItem openImage;
    
    @FXML //fx:id="exportMenuButton"
    private MenuItem exportMenuItem;
    
    @FXML //fx:id="exitMenuButton"
    private MenuItem exitMenuItem;
    
     @FXML //fx:id="aboutMenuItem"
    private MenuItem aboutMenuItem;
    
    @FXML // fx:id="clearAllButtonSC"
    private Button clearAllButtonSC; // Value injected by FXMLLoader
    
    @FXML // fx:id="exitButtonSC"
    private Button exitButtonSC; // Value injected by FXMLLoader
    
    @FXML // fx:id="rawDataTextArea"
    private TextArea rawDataTextArea; // Value injected by FXMLLoader
    
    @FXML // fx:id="statisticalAnalysisTextArea"
    private TextArea statisticalAnalysisTextArea; // Value injected by FXMLLoader
    
    @FXML // fx:id="gaussianFittingTextArea"
    private TextArea gaussianFittingTextArea; // Value injected by FXMLLoader
    
    @FXML
    private RadioButton squareRootBin;
    @FXML
    private ToggleGroup binToggleGroup;
    @FXML
    private RadioButton sturgesBin;
    @FXML
    private RadioButton riceBin;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button normaliseButtonSC;
    @FXML
    private Button togglePDFCurve;
    
    
    public StatisticsCalculatorGUIController(){
        initialize();
    }
    void initialize() {
                
        
        assert clearAllButtonSC != null : "fx:id=\"clearAllButtonSC\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert chooseFileButton != null : "fx:id=\"chooseFileButton\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert rawDataTextArea != null : "fx:id=\"rawDataTextArea\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert statisticalAnalysisTextArea != null : "fx:id=\"statisticalAnalysisTextArea\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert gaussianFittingTextArea != null : "fx:id=\"gaussianFittingTextArea\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert SCCanvas != null : "fx:id=\"SCCanvas\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        
        assert sturgesBin != null : "fx:id=\"sturgesBin\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert squareRootBin != null : "fx:id=\"squareRootBin\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert riceBin != null : "fx:id=\"riceBin\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert binToggleGroup != null : "fx:id=\"binToggleGroup\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert clearAllButtonSC != null : "fx:id=\"clearAllButtonSC\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert togglePDFCurve != null : "fx:id=\"togglePDFCurve\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert exitButtonSC != null : "fx:id=\"exitButtonSC\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert exitMenuItem != null : "fx:id=\"exitMenuItem\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert exportMenuItem != null : "fx:id=\"exportMenuItem\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert importData != null : "fx:id=\"importData\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert openImage != null : "fx:id=\"openImage\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        assert aboutMenuItem != null : "fx:id=\"aboutMenuItem\" was not injected: check your FXML file 'StatisticsCalculatorGUI.fxml'.";
        
        //make file to hold PDF curve toggle state
        try {
            makeToggleFile(1);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StatisticsCalculatorGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //make border on canvas
    void makeBorderOnCanvas(GraphicsContext gc){
        
        //
        gc.strokeLine(0, 0, gc.getCanvas().getWidth(), 0);
        gc.strokeLine(0, 0, 0, gc.getCanvas().getHeight());
        gc.strokeLine(0, gc.getCanvas().getHeight(), gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.strokeLine(gc.getCanvas().getWidth(), gc.getCanvas().getHeight(), gc.getCanvas().getWidth(), 0);
        
    }
    
   
    //==========================================================================
    /**
     * if clear button is clicked
     */
    //==========================================================================
    @FXML
    void clearAllButtonSCClicked() {
        
        // get the canvas handler 
        GraphicsContext gc = SCCanvas.getGraphicsContext2D();
        
        // clear canvas
        clearAllAction (gc);
        
        //clear textAreas
        rawDataTextArea.clear();
        statisticalAnalysisTextArea.clear();
        gaussianFittingTextArea.clear();
        
        
    }
    
    //==========================================================================
    /**
     * clear button general action 
     * @param gc this particular canvas will be cleared
     */
    //==========================================================================
    void clearAllAction (GraphicsContext gc){
        
        // clear canvas
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
    }
    
    
    //==========================================================================
    /**
     * if save button is clicked
     * @throws Exception if file is not chosen correctly
     */
    //==========================================================================
     @FXML
    void saveButtonSCClicked() throws Exception {
        
        FileChooser myfileChooser = new FileChooser();
        myfileChooser.setTitle("Save to");
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, .jpg, .jpeg)", "*.png", "*.jpg", "*.jpeg");
        myfileChooser.getExtensionFilters().add(extFilter);
        
        //
        File saveFileDestination = null;
        
        try {
            int indexOfExt = 0;
            while (indexOfExt == 0 ){

                saveFileDestination = myfileChooser.showSaveDialog(new Stage());

                String fileName = saveFileDestination.getName();
                indexOfExt = fileName.lastIndexOf('.')+1;
                System.out.println(fileName);

                if (indexOfExt == 0 ){
                    Label addExtString = new Label("MUST HAVE AN EXTENSION...");

                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().add(addExtString);

                    Scene secondScene = new Scene(secondaryLayout, 230, 100);

                    // New window (Stage)
                    Stage newWindow = new Stage();
                    newWindow.setScene(secondScene);

                    newWindow.showAndWait();
                }

            }
            IOOperation.SaveToFile(SCCanvas, saveFileDestination);
        } catch(Exception e){
              System.out.println("Developer error message #SaveButtonClicked: "+e.getMessage());
        }
    }
   
    
    
    //==========================================================================
    /**
     * if choose file button is clicked
     */
    //==========================================================================
    @FXML
    void chooseFileButtonClicked() {
        
        // first clear everything
        clearAllButtonSCClicked(); 
        
        FileChooser myfileChooser = new FileChooser();
        myfileChooser.setTitle("Open...");
        
        File openFileDestination = null;

        try {

            int indexOfExt = 0;
            while (indexOfExt == 0 ){

                openFileDestination = myfileChooser.showOpenDialog(new Stage());

                String fileName = openFileDestination.getName();
                indexOfExt = fileName.lastIndexOf('.')+1;

                if (indexOfExt == 0 ){
                    Label addExtString = new Label("FILE HAS NO EXTENSION...");

                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().add(addExtString);

                    Scene secondScene = new Scene(secondaryLayout, 230, 100);

                    // New window (Stage)
                    Stage newWindow = new Stage();
                    newWindow.setScene(secondScene);

                    newWindow.showAndWait();
                }

            }
        } catch (Exception e){
            return; //incase of a cancel
        }
        
        //verify toggle is selected
        if(!(verifyToggleSelected() == 0)){
            JOptionPane.showMessageDialog(null, "Please select a bin method ", "No bin method selected: ", JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        
        try{
            // get the canvas handler
            GraphicsContext gc = SCCanvas.getGraphicsContext2D();
            IOOperation.OpenFileSC(openFileDestination,gc,SCHistogramDrawing);
            //get data from IOOperation
            setInputData(IOOperation.getData());

            //display data from analysis
            StatisticalAnalysis sa = new StatisticalAnalysis();
            sa.setInputData(inputData);
            DisplayAnalysisData disp = new DisplayAnalysisData(sa);
            disp.displayRawDataAnalysis(rawDataTextArea);
            disp.displayStatsDataAnalysis(statisticalAnalysisTextArea);
            setBinNumber(sa.getBinNumber(selectedBinMethod));
            binRanges = sa.getBinRanges();
            normalised = sa.getNormalised();
            frequencies = sa.getFrequencies();
            disp.displayGaussianFittings(gaussianFittingTextArea, binRanges, frequencies, binNumber);
        } catch(Exception e){
            System.out.println("Developer Error from #ChooseFileButtonClicked: "+e.getMessage());
        }
    }
    
   //normalise button clicked 
    @FXML
    void normaliseButtonSCClicked(){
        
        try {
            //confirm toggle selection 
            if(!(verifyToggleSelected() == 0)){
                JOptionPane.showMessageDialog(null, "Please select a bin method ", "No bin method selected: ", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        
            StatisticalAnalysis sa = new StatisticalAnalysis();
            sa.setInputData(inputData);
            DisplayAnalysisData disp = new DisplayAnalysisData(sa);
            disp.displayRawDataAnalysis(rawDataTextArea);
            disp.displayStatsDataAnalysis(statisticalAnalysisTextArea);
            setBinNumber(sa.getBinNumber(selectedBinMethod));
            binRanges = sa.getBinRanges();
            normalised = sa.getNormalised();
            frequencies = sa.getFrequencies();
            disp.displayGaussianFittings(gaussianFittingTextArea, binRanges, frequencies, binNumber);

            GraphicsContext gc = SCCanvas.getGraphicsContext2D();
            clearAllAction(gc); //clear everything from initial plot 

            //get bin ranges, normalised frequencies and plot
            HistogramPlotter hist = new HistogramPlotter(binRanges, normalised,frequencies, binNumber, gc);
            hist.plotHistogram();
            makeToggleFile(1); //initialise pdf toggle
            
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select a valid datafile ", "Invalid Data Input: ", JOptionPane.ERROR_MESSAGE);
            System.out.println("Developer Error from #normaliseButtonSClicked #Plot Error: "+ex.getMessage());
        }
        
        
    }
    @FXML
    void togglePDFCurveClicked(){
        try {
            //confirm toggle selection 
            if(!(verifyToggleSelected() == 0)){
                JOptionPane.showMessageDialog(null, "Please select a bin method ", "No bin method selected: ", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        
            StatisticalAnalysis sa = new StatisticalAnalysis();
            sa.setInputData(inputData);
            DisplayAnalysisData disp = new DisplayAnalysisData(sa);
            disp.displayRawDataAnalysis(rawDataTextArea);
            disp.displayStatsDataAnalysis(statisticalAnalysisTextArea);
            setBinNumber(sa.getBinNumber(selectedBinMethod));
            binRanges = sa.getBinRanges();
            normalised = sa.getNormalised();
            frequencies = sa.getFrequencies();
            disp.displayGaussianFittings(gaussianFittingTextArea, binRanges, frequencies, binNumber);

            GraphicsContext gc = SCCanvas.getGraphicsContext2D();
            clearAllAction(gc); //clear everything from initial plot 

            //get bin ranges, normalised frequencies and plot
            HistogramPlotter hist = new HistogramPlotter(binRanges, normalised,frequencies, binNumber, gc);
            hist.plotHistogram();
            toggleState = getToggleState();
            if(toggleState == 1) hist.drawPDFCurve();
            else {
                clearAllAction(gc);
                hist.plotHistogram();
                makeToggleFile(1);
            }
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Please select a valid datafile ", "Invalid Data Input: ", JOptionPane.ERROR_MESSAGE);
            System.out.println("Developer Error from #normaliseButtonSClicked #Plot Error: "+ex.getMessage());
        }
    }
    @FXML
    void exitButtonSCClicked(){
        System.exit(1);
    }
    @FXML
    void exitMenuItemClicked(){
        exitButtonSCClicked();
    }
    
    @FXML
    void importDataMenuItemClicked(){
        chooseFileButtonClicked();
    }
    
    @FXML
    void openImageMenuItemClicked(){
        chooseFileButtonClicked();
    }
    
    @FXML
    void exportMenuItemClicked() throws Exception{
        saveButtonSCClicked();
    }
    
     @FXML
    void aboutMenuItemClicked() throws Exception{
        JOptionPane.showMessageDialog(null, "AirCoach Statistics Calculator\nDeveloped By: 20212007\nFor: EEE3084 Coursework 3", "AirCoach", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void makeToggleFile(int toggle) throws FileNotFoundException{
        PrintWriter writer;
        try {
            writer = new PrintWriter("Resources/toggleState.txt", "UTF-8");
            writer.println(toggle);
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StatisticsCalculatorGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private int getToggleState() throws FileNotFoundException, IOException{
        File file = new File("Resources\\toggleState.txt"); 
  
        BufferedReader br = new BufferedReader(new FileReader(file)); 

        String st, stri=""; 
        while ((st = br.readLine()) != null) {
          stri += st; 
        }
        int tog = Integer.parseInt(stri);
        if(tog == 0) makeToggleFile(1);
        else makeToggleFile(0);
        
        return tog;
    }
    public String getSelectedBinMethod(){
        return selectedBinMethod;
    }
    
    public void setInputData(List<Double> inData){
        inputData = inData;
    }
    public void setBinNumber(int bn){
        binNumber = bn;
    }
    private int verifyToggleSelected(){
        //get the selected toggle
        RadioButton rb = (RadioButton)binToggleGroup.getSelectedToggle();
        if(binToggleGroup.getSelectedToggle() == null){
            return 1;
        } else {
            //set selected bin method
            selectedBinMethod = rb.getId();
            return 0;
        }
    }

}
