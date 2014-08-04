import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class SoundPlayer {
	private AudioInputStream stream;
	private File file;
	private SourceDataLine clip;
	private DataLine.Info dataLine;
	private SoundPanel soundPanel;
	private ToolPanel toolPanel;
	private boolean running;
	private boolean paused = false;
	public Timer timer;
	public int timerPeriod; //C'est le temps qui correspond à une frame du spectre
	private Chromagram currentChromagram;
	private SharedResources sharedResources;
	public double musicLength;
	private int totalToRead;
	private int total;
	private int numBytesToRead = 50000;
	private byte[] myData = new byte[numBytesToRead];
	private MyThread thread;
	private ButtonContainer buttonContainer;

	
	SoundPlayer(Chromagram chr, SharedResources sr){
		//stream = chr.getAudioInputStream();
		sharedResources = sr;
		file = chr.getFile();
		toolPanel = sharedResources.playerPanel;
		toolPanel.setSharedResources(sharedResources);
		try {
			stream = AudioSystem.getAudioInputStream(file);
			totalToRead = stream.available();
			musicLength = stream.available()/(2*2*stream.getFormat().getSampleRate());
			System.out.println("The music lasts : " + musicLength + " s");
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentChromagram = chr;
		
	}
	
	
	SoundPlayer(AudioInputStream au, SoundPanel sp,  SharedResources sr){
		sharedResources = sr;
		stream = au;
		soundPanel = sp;
		dataLine = new DataLine.Info(Clip.class, stream.getFormat());
		/*try {
			clip = (Clip) AudioSystem.getLine(dataLine);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	public void setSoundPanel(SoundPanel sp){
		soundPanel = sp;
	}
	
	
	
	private void initTimer() {    
		if(timer ==null){
			 timer = new Timer(timerPeriod, toolPanel);
		     timer.setRepeats(true);
		}
	     
	     timer.start();        
	  }
	
	
	
	
	public void play(){
		sharedResources.drawingPanel.setLaunched();
		if(paused == true && running == true){
			paused = false;
			running = true;
			thread = new MyThread();
			thread.start();
			initTimer();

			//clip.start();
		}
		else if(running == false){
			try {
				stream = AudioSystem.getAudioInputStream(file);
				totalToRead = stream.available();
				musicLength = stream.available()/(2*2*stream.getFormat().getSampleRate());
				System.out.println("The music lasts : " + musicLength + " s");
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			dataLine = new DataLine.Info(SourceDataLine.class, stream.getFormat()); //avant Clip.class
			timerPeriod = (int) (1000*currentChromagram.getFFTSize()/currentChromagram.getNewSampling());
			try {
				clip = (SourceDataLine) AudioSystem.getLine(dataLine);
				clip.open(stream.getFormat());
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//clip.open(stream);
			initTimer();
			paused = false;
			running = true;
			clip.start();
			System.out.println("Le son a été ouvert avec succès et la lecture commence");
			thread = new MyThread();
			thread.start();
		}
		
		
		
	}
	
	
	
	public void pause(){
		if(paused == false && running == true){
			paused = true;
			//clip.stop();
			timer.stop();
		}
		
	}
	
	public void stop(){
		if(running == true){
			paused = false;
			running = false;
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clip.close();
			timer.stop();
			toolPanel.slide.setValue(0);
			sharedResources.drawingPanel.setCursor(0);
			sharedResources.buttonContainer.initIndex();
			soundPanel.setGraph(currentChromagram.spectrum[0]);	
			total = 0;
		}
	}
	
	
	
	public void setButtonContainer(ButtonContainer bc){
		
		buttonContainer = bc;
	
	}
	
	
	
	
	
	class MyThread extends Thread {
		
		
	
		public void run() {
			while (total < totalToRead && !paused && running){
				int numBytesRead;
				try {
					numBytesRead = stream.read(myData, 0, numBytesToRead);
					if (numBytesRead == -1) break;
				    total += numBytesRead; 
				    clip.write(myData, 0, numBytesRead);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
		
		}

		
		public void pause(){
			
		}
		
	}

	
}
