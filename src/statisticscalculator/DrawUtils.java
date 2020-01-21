
package statisticscalculator;

/**
 *
 * @author pmzsp1
 */
public class DrawUtils {
    
    // to read string of data separated with ";" and return it as double array
    // e.g. 1;2;3 -> [1,2,3]
    public static double[] readStringInfo(String _listInfo){
        
        int lengthofString = _listInfo.length();
        
        // find how many data
        int noData = 0; 
        for(int i=0; i<lengthofString; i++){
            if(_listInfo.charAt(i)==';'){
                noData++; 
            }
        }
        
        // Collect the data and allocate to a double array 
        double[] data = new double[noData]; // reserve memory for data
        int indexPrevChar = 0; // for find data range
        int dataIterator = 0; // just iterator for data
        
        for(int i=0; i<lengthofString; i++){
            if(_listInfo.charAt(i)==';'){
                
                data[dataIterator] = Double.valueOf(_listInfo.substring(indexPrevChar, i));
                        
                dataIterator ++;         
                indexPrevChar = i+1; 
            }
        }
        
        return data; 
    }
    

}
