
package statisticscalculator;

/**
 *
 * @author Aniebiet Akpan
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StatisticsCalculator extends Application{
    
    // start GUI function 
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("StatisticsCalculatorGUI.fxml")); // load the fxml file
        primaryStage.setTitle("AirCoach Statistics Calculator");
        
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("logo/aircoach.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


