
package statisticscalculator;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Aniebiet Akpan
 */
//this class handles all plots
public interface Plottable {
    //a plottable interface should be able to draw a shape, write vertically and initialise the plot
    public void initialise(GraphicsContext gc);
    public void drawShape(String shape, String props, GraphicsContext gc);
    public void writeVertically(String text, GraphicsContext gc, double xTranslate, double yTranslate, double xPos, double yPos);
}
