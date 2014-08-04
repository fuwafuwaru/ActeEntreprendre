import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;


public class Envelope {
	private int index = 0;
	//protected ArrayList<EnvPoint> currentGraph;
	protected EnvPoint[] currentGraph = new EnvPoint[500000];
	private double mMinValue = 1.0e-7;
	private double mMaxValue = 2.0;
	private double mDefaultValue = 1.0;
	
	
	Envelope(){
		//currentGraph = new ArrayList<EnvPoint>();
		for(int k = 0; k < currentGraph.length; k++){
			currentGraph[k] = new EnvPoint(100);
		}
	}
	
	public void rescale(double minValue, double maxValue)
	{
	   double oldMinValue = mMinValue;
	   double oldMaxValue = mMaxValue;
	   mMinValue = minValue;
	   mMaxValue = maxValue;

	   // rescale the default value
	   double factor = (mDefaultValue - oldMinValue) / (oldMaxValue - oldMinValue);
	   mDefaultValue = ClampValue(mMinValue + (mMaxValue - mMinValue) * factor);

	   // rescale all points
	   EnvPoint point;
	   /*for(ListIterator<EnvPoint> iter = currentGraph.listIterator(0); iter.hasNext();) {
		  point = iter.next();
	      factor = (point.getY() - oldMinValue) / (oldMaxValue - oldMinValue);
	      point.setY(mMinValue + (mMaxValue - mMinValue) * factor);
	   }*/
	   

	}
	
	
	public EnvPoint[] getCurrentGraph(){
		return currentGraph;
	}
	
	public double ClampValue(double value){ 
		return Math.max(mMinValue, Math.min(mMaxValue, value)); 
	}
	
	public void setCurrentGraph(int n){
		currentGraph = new EnvPoint[n];
	}
	
	
	public int getIndex(){
		return index;
	}
	
	//Add the values of an array at the end of the list
	//Pour le moment offsetInArray ne sert à rien
	public void clip (double[] array, int offsetInArray, int length){
		/*for(int k = offsetInArray; k < Math.max(array.length, offsetInArray+length - 1); k++){
			currentGraph.add(new EnvPoint(array[k]));
		}*/
		double tmp = 0;
		int k = 0;
		for(k = 0; k < array.length; k++){
			currentGraph[index+k] = new EnvPoint(array[k]);
			tmp = 0;
		}
		index += array.length;
	}
	
	/*public void clipWithOffset(int[] array, int offsetInArray, int length, int offset){
		for(int k = 0; k<offset; k++){
			currentGraph.add(new EnvPoint(0));
		}
		clip(array, offsetInArray, length);
	}*/
	
	
}
