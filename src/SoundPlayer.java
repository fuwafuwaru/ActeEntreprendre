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
	private ToolFrame toolFrame;
	private ToolPanel toolPanel;
	private boolean running;
	private boolean paused = false;
	private Timer timer;
	private int timerPeriod; //C'est le temps qui correspond à une frame du spectre
	private Chromagram currentChromagram;
	private JButton play = new JButton();
	private JButton pause = new JButton();
	private JButton stop = new JButton();
	private double t = 0; //Nombre de fois où le timer est appelé correspond à la frame dans le spectre
	private ButtonContainer buttonContainer;
	private SharedResources sharedResources;
	private double musicLength;
	private Slide slide;
	private JLabel time = new JLabel("0:00");
	private int totalToRead;
	private int total;
	private int numBytesToRead = 50000;
	private byte[] myData = new byte[numBytesToRead];
	private MyThread thread;

	
	SoundPlayer(Chromagram chr){
		//stream = chr.getAudioInputStream();
		file = chr.getFile();
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
		toolFrame = new ToolFrame();
		
	}
	
	
	SoundPlayer(AudioInputStream au, SoundPanel sp){
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
	
	public void setButtonContainer(ButtonContainer bc){
		buttonContainer = bc;
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
			t = 0;
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clip.close();
			timer.stop();
			slide.setValue(0);
			sharedResources.drawingPanel.setCursor(0);
			sharedResources.buttonContainer.initIndex();
			soundPanel.setGraph(currentChromagram.spectrum[0]);	
			total = 0;
		}
	}
	
	
	
	
	/*-----------------------*
	 * Début classe ToolFrame*
	 *-----------------------*/
	
	class ToolFrame extends JFrame{
		
		ToolFrame(){
			setSize(500, 120);
			toolPanel = new ToolPanel();
			setContentPane(toolPanel);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	
	}
	
	
	
	
	/*-----------------------*
	 * Début classe ToolPanel*
	 *-----------------------*/
	
	class ToolPanel extends JPanel implements ActionListener{
		
		ToolPanel(){
			setSize(500, 120);
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.setAlignmentX(CENTER_ALIGNMENT);
			JPanel firstContainer = new JPanel();
			JPanel secondContainer = new JPanel();
			slide = new Slide();
			firstContainer.setLayout(new GridLayout(1, 3, 10, 10));
			//Image img = ImageIO.read(getClass().getResource("/home/anis/workspace/acteEntreprendre/player_play.png"));
			ImageIcon icPlay = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_play.png");
			play.setIcon(icPlay);
			ImageIcon icPause = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_pause.png");
			pause.setIcon(icPause);
			ImageIcon icStop = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_stop.png");
			stop.setIcon(icStop);
			
			play.setBorder(BorderFactory.createEmptyBorder());
			play.setContentAreaFilled(false);
			pause.setBorder(BorderFactory.createEmptyBorder());
			pause.setContentAreaFilled(false);
			stop.setBorder(BorderFactory.createEmptyBorder());
			stop.setContentAreaFilled(false);
			

			play.addActionListener(this);
			pause.addActionListener(this);
			stop.addActionListener(this);
			
			firstContainer.setLayout(new BoxLayout(firstContainer, BoxLayout.LINE_AXIS));
			firstContainer.setAlignmentX(CENTER_ALIGNMENT);
			firstContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			firstContainer.add(play);
			firstContainer.add(pause);
			firstContainer.add(stop);
			
			secondContainer.add(slide);
			secondContainer.add(time);
			secondContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

			add(secondContainer, BorderLayout.NORTH);
			add(firstContainer, BorderLayout.CENTER);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getSource() == play){
				play();
			}
			else if(arg0.getSource() == pause){
				pause();
			}
			
			else if(arg0.getSource() == stop){
				stop();
			}

			else if(arg0.getSource() == timer){
				if(t < currentChromagram.spectrum.length){
					soundPanel.setGraph(currentChromagram.spectrum[(int) t]);
					if(sharedResources.drawingPanel == null){
						System.out.println("oidnso");
					}
					sharedResources.drawingPanel.setCursor(((double)t)/currentChromagram.spectrum.length);
					slide.setValue((int) (t*timerPeriod/1000) + 1);
					t += 1;
					buttonContainer.more();
				}
				
			}
			
		}
	}
	
	
	/*-----------------------*
	 * Fin classe ToolPanel  *
	 *-----------------------*/
	
	
	public class Slide extends JSlider{
		public Slide(){
			this.setPreferredSize(new Dimension(400, 20));
			setMaximum((int) musicLength);
			setMinimum(0);
			setValue(0);
			addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent event){
					int min = ((JSlider)event.getSource()).getValue()/60;
					int sec = ((JSlider)event.getSource()).getValue()%60;
					if(sec < 10){
						time.setText(min + ":0" + sec + "/" + ((int) (musicLength/60)) + ":" + ((int) (musicLength))%60);
					}
					else{
						time.setText(min + ":" + sec + "/" + ((int) (musicLength/60)) + ":" + ((int) (musicLength))%60);
					}
				}
			});
		}

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
