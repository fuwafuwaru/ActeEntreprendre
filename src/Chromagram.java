import javax.sound.sampled.*;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.JFrame;

import java.io.*;
import java.util.LinkedList;


/*
 * TODO : Travailler l'actualisation de numberOfData en fontion de la taille de la musique chargée.
 */

public class Chromagram implements Runnable, Serializable {
	private int numberOfData = 1000;
	public ChromaVector[] chromagram;
	public Chord[] chordSerie;
	private Pitch[][] noteSerie;
	private static final int FFT_SIZE = 2048;
	public double[][] spectrum; 
	transient private File sound;
	transient private AudioInputStream stream;
	private String name;
	private String path;
	transient private SoundPanel snd;
	private float sampling = 44100;
	private int resamplingRate = 4;
	private float newSampling = sampling/resamplingRate;
	private final int MIDIMIN = 43;
	private final int MIDIMAX = 105;
	transient private int limitation = 1;
	transient private byte[] buffer;
	private int byteToBeRead;
	transient private Progress progBar;
	transient private JProgressBar bar;
	private float overlay = 1.0f;
	transient private AmplitudeScrollPane asp;
	//private AmplitudeScrollPane aspTmp;

	Chromagram(){
		System.out.printf("Nouveau chromagram créé. \n");
		System.out.printf("MidiMin : %d   MidiMax : %d \n", MIDIMIN, MIDIMAX);
		System.out.printf("ResolutionSpectral : %f Hz \n", getSpectralResolution());
	}
	
	Chromagram(String s){
		setPath(s);
		this.setFile();
		this.setStream();
		sampling = stream.getFormat().getSampleRate();
		newSampling = sampling/resamplingRate;
		this.init();
		System.out.printf("Nouveau chromagram créé. \n");
		System.out.printf("MidiMin : %d   MidiMax : %d \n", MIDIMIN, MIDIMAX);
		System.out.printf("ResolutionSpectral : %f Hz \n", getSpectralResolution());
	}
	
	Chromagram(String s, SoundPanel sp){
		setPath(s);
		this.setFile();
		this.setStream();
		sampling = stream.getFormat().getSampleRate();
		newSampling = sampling/resamplingRate;
		this.init();
		this.setSoundPanel(sp);
		System.out.printf("Nouveau chromagram créé. \n");
		System.out.printf("MidiMin : %d   MidiMax : %d \n", MIDIMIN, MIDIMAX);
		System.out.printf("ResolutionSpectral : %f Hz \n", getSpectralResolution());
	}
	
	public void setSoundPanel(SoundPanel s){
		snd = s;
		System.out.println("SoundPanel set");
	}
	
	public void setPath(String s){
		path = s;
	}
	
	public String getName(){
		return name;
	}
	
	public int getMidiMin(){
		return MIDIMIN;
	}
	
	public int getMidiMax(){
		return MIDIMAX;
	}
	
	
	public void setName(String s){
		name = s.substring(0, s.length() - 4);
	}
	
	private void init(){
		chromagram = new ChromaVector[numberOfData];
		chordSerie = new Chord[numberOfData];
		spectrum = new double[numberOfData][FFT_SIZE];
	}
	
	public void setFile(){
		sound = new File(path);
		setName(sound.getName());
		System.out.println("File set");
	}
	
	public void setStream(){
		try{
			stream = AudioSystem.getAudioInputStream(sound);
			numberOfData = stream.available()/(2*2*FFT_SIZE*resamplingRate); //On divise par 2 car sons 16 bits la plupart du temps et encore par deux pour la stéréo
		}catch(IOException e){
			System.out.println("IOException");
		}catch(UnsupportedAudioFileException e){
			System.out.println("UnsupportedAudioFileException");
		}
		System.out.println("Stream set");
	}
	
	public void setProgressBar(Progress prog){
		progBar = prog;
		bar = prog.getBar();
	}
	
	public int getFFTSize(){
		return FFT_SIZE;
	}
	
	public AudioInputStream getAudioInputStream(){
		return stream;
	}
	
	public File getFile(){
		return sound;
	}
	
	public float getNewSampling(){
		return newSampling;
	}
	
	public void setPanel(SoundPanel sp){
		snd = sp;
	}
	
	public void setAmplitudeScrollPane(AmplitudeScrollPane amp){
		asp = amp;
	}
	
	public double getSpectralResolution(){
		return (newSampling/FFT_SIZE);
	}
	
	public int getLimitation(){
		return limitation;
	}
	
	public String getpath(){
		return path;
	}
	
	public String getRootPath(String s){
		int lastSlash = 0;
		for(int k = 0; k<s.length(); k++){
			if(s.charAt(k) == '/'){
				lastSlash = k;
			}
		}
		return s.substring(0, lastSlash+1);
	}
	
	public void process() throws IOException{
		System.out.println("Signal processing now taking place ...");
		//setFile();
		//setStream();
		progBar.getBar().setMaximum(numberOfData-1);
		int frameSize = stream.getFormat().getFrameSize();
		//We multiply by the resampling rate the buffer size in order to get an array of the desired size for the
		//fft after resampling
		byteToBeRead = frameSize*FFT_SIZE*resamplingRate;
		buffer = new byte[byteToBeRead];
		getChromagram();
		System.out.println("Signal processing completed.");
	}
	
	
	
	/*Attention ne marche qu'avec des sons 16 bits en little endian
	*/
	
	public static int[] convertToInt(byte[] b){
		int[] intArray = new int[b.length/2];
		int j = 0;
		for(int k = 0; k<b.length; k+=2){
			intArray[j] = (int) ((b[k + 0] & 0xFF) | (b[k + 1] << 8));
			j++;
		}
		return intArray;
	}
	
	
	
	/*
	 * This function returns the newly sampled double array with the new sampling previousSampling/reSamplingRate
	 * without changes, converts from 44100Hz to 11025Hz.
	 */
	public double[] preProcess(byte [] b){
		int[] chunk = convertToInt(b);
		if(stream.getFormat().getChannels() == 1){
			double[] temp = resample(chunk);
			asp.getDrawingPanel().getEnvelope().clip(temp, 0, temp.length);
			return temp;
		}
		else if(stream.getFormat().getChannels() == 2){
			double[] temp = resample(stereoToMono(chunk));
			asp.getEnvelope().clip(temp, 0, temp.length);
			return temp;
		}
		else{
			try {
				throw (new Chromagram.ChannelNumberException("Nombre de canaux invalide"));
			} catch (ChannelNumberException e) {
				System.out.println("Le nombre de canaux n'est pas géré");
			}
			return null;
		}
	}
	
	class ChannelNumberException extends Exception{
		 public ChannelNumberException() { super(); }
		 public ChannelNumberException(String message) { super(message); }
	}
	
	public double[] resample(int[] array){
		int r  = resamplingRate;
		double[] array2= new double[array.length/r];
		double avrg = array[0];
		for(int k=1; k<array.length; k++){
			if(k%r==r-1){
				array2[k/r] = avrg/r;
				avrg = array[k];
			}
			else{
				avrg += array[k];
			}
		}
		return array2;
	}
	
    public static Complex[] convertToComplex(double[] a){
    	Complex[] complexArray = new Complex[a.length];
    	for(int k =0; k<a.length; k++){
    		complexArray[k] = new Complex(a[k]);
    	}
    	return complexArray;
    }
    
    
	
	public void getChromagram() throws IOException{//Il faut filtrer les valeurs trop basses au début qui pourrissent
		int k = 0;
		boolean notStarted = true;
		if(bar == null){
			System.out.println("bar null");
		}
		
		while(stream.read(buffer) != -1 && k < numberOfData){
			bar.setValue(k); //Mise à jour de la barre de chargement
			if(notStarted){
				int j = 0;
				double tmp = 0.;
				for(int i = 0; i<buffer.length; i+=2){
					j = Math.abs((int) ((buffer[i + 0] & 0xFF) | (buffer[i + 1] << 8)));
					tmp += j;
					}
				tmp = tmp / buffer.length;
				if(tmp >= 50){
					notStarted = false;
				}
			}
			spectrum[k] = ProcessingTools.onlyHalf(ProcessingTools.fft(convertToComplex(ProcessingTools.blackmanWindow(preProcess(buffer), newSampling, FFT_SIZE))));
			double[] array2 = setInMidiScale(spectrum[k]);			
			ChromaVector chrv = new ChromaVector(array2);
			chrv.normalise();		
			chromagram[k]=chrv;
			if(notStarted){
				chordSerie[k] = null;
			}
			else{
				if(k > 0 && chordSerie[k-1] != null){
					chordSerie[k] = chrv.findMaxCorrelation();
				}
				else{
					chordSerie[k] = chrv.findMaxCorrelation();
				}
			}
			k++;
		}
	}
	
	/*public void getOneMoreForSpectrum() throws IOException{
		stream.read(buffer);
		spectrum[limitation] = ProcessingTools.fft(convertToComplex(ProcessingTools.blackmanWindow(preProcess(buffer), newSampling, FFT_SIZE)));
		if(snd != null){
			snd.setGraph(spectrum[limitation]);
		}
		double[] array2 = setInMidiScale(spectrum[limitation]);			
		ChromaVector chrv = new ChromaVector(array2);
		chrv.normalise();		
		chromagram[limitation]=chrv;
		limitation++;
	}*/
	
	
	public float getMusicTime(int k){
		return (float) (k*FFT_SIZE*resamplingRate*overlay/sampling);
	}
	
	
	public int[] stereoToMono(int[] array){
		int[] array2 = new int[array.length/2];
		for(int k = 0; k < array.length; k+=2){
			array2[k/2] = (array[k]+array[k+1])/2;
		}
		return array2;
	}
	
	/*public void recognize(){
		int i = 0;
		while (i < chromagram.length && chromagram[i] != null) {
			System.out.println(chromagram[i].findMaxCorrelation());
			i++;
		}
	}*/
	
	
	
	public double[] setInMidiScale(double[] comp){
		double inverseRatio = (FFT_SIZE/newSampling);
		double ratio = 1/inverseRatio;
		//int kstart =  (int) (Math.round((inverseRatio*440*Math.pow(2, (MIDIMIN-69)/12.))));
		//int kstop = (int) (Math.round((inverseRatio*440*Math.pow(2, (MIDIMAX-69)/12.))));
		double[] array = new double[MIDIMAX-MIDIMIN+1];
		for(int j=MIDIMIN; j<MIDIMAX;j++){
			int i = (int) (Pitch.convertFreq(j)/ratio);
			int l =  (int) (Pitch.convertFreq(j+1)/ratio);
			if(i==l){
				i = (int) Math.round((Pitch.convertFreq(j)/ratio));
				l =  (int) Math.round((Pitch.convertFreq(j+1)/ratio));
				if(i != l){
					array[j-MIDIMIN]=0.5*(comp[i]+comp[i-1]);
					array[j+1-MIDIMIN]=0.5*(comp[i]+comp[i+1]);
					j++;
				}
				else{
					System.out.println("Les notes "+ j + " et " + (j+1) +" sont indissociables");
					array[j-MIDIMIN]=comp[i];
				}
			}
			else{
				array[j-MIDIMIN]=comp[i];
			}
		}		
		return array;
	}
	
	
	public void generateNoteSerie() throws IOException{
		int[] array;
		File test = new File("/home/anis/Lilypond/"+getName()+".txt");
		FileWriter tstw = new FileWriter(test);
		noteSerie = new Pitch[spectrum.length][10];
		for(int k = 0; k<spectrum.length; k++){
			array = ProcessingTools.findLocalMax(spectrum[k], (int) (Pitch.convertFreq(MIDIMIN)/(newSampling/FFT_SIZE)), (int) (Pitch.convertFreq(MIDIMAX)/(newSampling/FFT_SIZE)));
			for(int i = 0; i< array.length; i++){
				tstw.write(array[i] + " ");
				if(array[i] != 0){
					noteSerie[k][i] = new Pitch(array[i]*newSampling/FFT_SIZE);
				}
			}
			tstw.write("\n");
			tstw.flush();
		}
		tstw.close();
	}
	

	public void generateSheetMusic(){
		try {
			generateNoteSerie();
			File ff = new File("/home/anis/Lilypond/"+getName()+".ly");
			System.out.println("Fichier créé dans /home/anis/Lilypond/"+getName()+".ly");
			Pitch p;
			boolean b = ff.createNewFile();
			System.out.println(b);
			FileWriter ffw = new FileWriter(ff);
			ffw.write("{");
			System.out.println(noteSerie.length);
			for(int t = 0; t<noteSerie.length; t++){
				ffw.write("<");
				for(int i = 0; i < noteSerie[t].length/2; i++){
					if(noteSerie[t][i] != null){
						p = noteSerie[t][i];
						ffw.write(p.toString());
					}
				}
				ffw.write(">4 ");
				ffw.flush();
			}
			ffw.write("}");
			ffw.close();
			System.out.println("Le fichier a été correctement généré");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void generateSheetMusic(String s){
		try {
			
			//Mise en place des éléments de sauvegarde
			generateNoteSerie();
			File ff = new File("/home/anis/Lilypond/"+s+".ly");
			System.out.println("Fichier créé dans /home/anis/Lilypond/"+s+".ly");
			Pitch p;
			boolean b = ff.createNewFile();
			System.out.println(b);
			FileWriter ffw = new FileWriter(ff);
			
			//Écriture dans le fichier
			
			ffw.write("{");
			System.out.println(noteSerie.length);
			for(int t = 0; t<noteSerie.length; t++){
				ffw.write("<");
				for(int i = 0; i < noteSerie[t].length/2; i++){
					if(noteSerie[t][i] != null){
						p = noteSerie[t][i];
						ffw.write(p.toString());
					}
				}
				ffw.write(">4 ");
				ffw.flush();
			}
			ffw.write("}");
			ffw.close();
			System.out.println("Le fichier a été correctement généré");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void sauvegarde(){
		try
	      {
			System.out.println(getRootPath(this.getpath()));
	        FileOutputStream fileOut =
	        new FileOutputStream(getRootPath(this.getpath()) + name + ".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in " + getRootPath(this.getpath()) + name + ".ser");
	      }catch(IOException i)
	      {
	          System.out.println(i);
	          System.out.println("Problème lors de la sauvegarde du Chromagram");
	      }
	}
	
	@Override
	public void run() {
		try {
			process();
			System.out.println("Calcul du chromagram effectué avec succès");
			this.sauvegarde();
			progBar.end();
			asp.setLoaded();
			asp.getParent().repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		} catch (Exception err) {
		    err.printStackTrace();
		}
		SharedResources sharedResources = new SharedResources();
		Fenetre fen = new Fenetre(sharedResources);
	}
	
}
