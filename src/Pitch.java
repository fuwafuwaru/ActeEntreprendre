
public class Pitch {
	private double frequency;
	private int midi;
	private Chroma chroma;
	
	Pitch(){
		frequency = 0;
		midi = 0;
		chroma = null;
	}
	
	Pitch(double freq){
		frequency = freq;
		midi = (int) (69 + Math.round(12*Math.log(freq/440)/Math.log(2)));
		chroma = Chroma.values()[midi%12];
	}
	
	Pitch(int mid){
		frequency = 440*Math.pow(2, (mid-69)/12.);
		midi = mid;
		chroma = Chroma.values()[mid%12];
	}
	
	public double getFrequency(){return frequency;}
	public int getMidi(){return midi;}
	public Chroma getChroma(){return chroma;}
	
	public static int convertMidi(double freq){
		return (int) Math.round(69 + Math.round(12*Math.log(freq/440)/Math.log(2)));
	}
	
	public static double convertFreq(int mid){
		return 440*Math.pow(2, (mid-69)/12.);
	}
	
	public static Pitch pitchFromChromaV(ChromaVector v){
		int i = v.getMaxIndex();
		return (new Pitch(i));
	}
}
