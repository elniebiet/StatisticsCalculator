
package statisticscalculator;


import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author pmzsp1
 */
//this class handles Input and output operations 
public class IOOperation {
    
    /**
     * To perform file saving action
     * @param content
     * @param fileDestination
     * @throws java.io.IOException
     */
    private static List<Double> inputData = new ArrayList<>(); 
    
    //save to a png file
    public static void SaveToFile(Object content, File fileDestination) throws IOException{
        
        // get the extension 
        String fileName = fileDestination.getName();
        
        int indexOfExt = fileName.lastIndexOf('.')+1;

        String fileExtension = fileName.substring(indexOfExt);
        
        
        // if the content is a text area means 
        // save the text content
        if( content instanceof TextArea ) {
            String StringContent = ((TextArea) content).getText();
            
            FileWriter fileWriter = new FileWriter(fileDestination);
             
            fileWriter.write(StringContent);
            fileWriter.close();

        }      
        
        // if the content is a canvas and extension is png  
        // save the image
        if( (content instanceof Canvas) && fileExtension.equalsIgnoreCase("png") ){
            
            Canvas tempCanvas = (Canvas) content; 
            
            WritableImage writableImage = new WritableImage((int) tempCanvas.getWidth(), (int) tempCanvas.getHeight());
            tempCanvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
                ImageIO.write(renderedImage, "png", fileDestination);
                JOptionPane.showMessageDialog(null, "File saved successfully at "+fileDestination.getPath(), "Report: ", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e){
                
            }
            
        }
        
    }
    
    /**
     * To perform Open file action for Statistics Calculator
     * @param fileDestination
     */
    public static void OpenFileSC( File fileDestination, GraphicsContext gc, HistogramPlotter whichContainer)  {
        
        String fileName = fileDestination.getName();
        
        int indexOfExt = fileName.lastIndexOf('.')+1;

        String fileExtension = fileName.substring(indexOfExt);
        
        // if the content is a text area means 
        // save the text content
        if( fileExtension.equalsIgnoreCase("txt") ) {
            
            executeOpenTxtFileSC(fileName, gc, whichContainer);

        }
        
        else if( fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") ) {
            
            try {
                executeOpenPngFile(fileDestination, gc, whichContainer);
            } catch (IOException ex) {
                System.out.println("ERROR: failed to load; "+ex);
                return;
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please select a '.txt' file or an image.", "Invalid File : ", JOptionPane.ERROR_MESSAGE);

        }
   
    }
    
    private static void executeOpenTxtFileSC(String fileName, GraphicsContext gc, HistogramPlotter whichContainer){
        
        
        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            // go through inputFileLine by inputFileLine
            String inputFileLine = null;
            
            inputData.clear(); //clear any original input data
            
            while((inputFileLine = bufferedReader.readLine()) != null) {
               try {
                    inputData.add(Double.parseDouble(inputFileLine));
               } catch(Exception ex){ //check that file is valid
                    JOptionPane.showMessageDialog(null, "Invalid input file, must contain only numbers", "Error Loading File: ", JOptionPane.INFORMATION_MESSAGE);
                    bufferedReader.close();
                    return;
               }
                
            } // END READING FILE BUFFER                       
            
            // if the inputFileLine is null it means its the last entry which need to be make
            if( inputFileLine  == null ) {
                //if the list is empty
                if(inputData.isEmpty()){ //check that file is valid
                    JOptionPane.showMessageDialog(null, "Input data file is empty", "Error Loading File: ", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
               
            }

            // Always close files.
            bufferedReader.close();         
        }
        
        catch(IOException ex) {
            System.out.println("Developer message: Error reading file"+ex.getLocalizedMessage()+"\n");                  
        }
        
        
    }
    
    //execute image file
    private static void executeOpenPngFile(File fileDestination, GraphicsContext gc, HistogramPlotter whichContainer) throws IOException{
        
        BufferedImage bufferedImage = ImageIO.read(fileDestination);
        
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        
        gc.drawImage(image, 0, 0);
         
        
    }
    
    //get data
    public static List getData(){
        return inputData;
    }    
    
    
}
