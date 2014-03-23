
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
	
	public String toString(){
		int height = midi/12;
		String out = "";
		
		if(chroma == Chroma.C){
			out =  "c";
		}
		else if(chroma == Chroma.Cd){
			out =  "cis";
		}
		else if(chroma == Chroma.D){
			out = "d";
		}
		else if(chroma == Chroma.Dd){
			out = "dis";
		}
		else if(chroma == Chroma.E){
			out = "e";
		}
		else if(chroma == Chroma.F){
			out = "f";
		}
		else if(chroma == Chroma.Fd){
			out = "fis";
		}
		else if(chroma == Chroma.G){
			out =  "g";
		}
		else if(chroma == Chroma.Gd){
			out = "gis";
		}
		else if(chroma == Chroma.A){
			out = "a";
		}
		else if(chroma == Chroma.Ad){
			out = "ais";
		}
		else if(chroma == Chroma.B){
			out = "b";
		}
		else return "";
		
		if(height < 5){
			for(int i = 0; i < 5 - height; i++){
				out = out + ",";
			}
		}
		
		if(height > 5){
			for(int i = 0; i < height - 5; i++){
				out = out + "'";
			}
		}
		
		return (out+" ");
	}
	
}
