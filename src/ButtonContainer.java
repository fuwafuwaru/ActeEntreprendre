import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ButtonContainer extends JPanel implements ActionListener {
	private JButton launch;
	private Fenetre parentContainer;
	private JButton more;
	private JButton less;
	public boolean isLaunched = false;
	private Chromagram currentChromagram;
	private JLabel musicTime;
	private JLabel nthSpectrum;
	private int index = 0;
	private JLabel alledgedChord;
	private SoundPanel associatedPanel;
	private int tempo;
	private int measure = 0;
	private int timeInMeasure = 0;
	private JLabel tempoLab;
	private JLabel measureLab;
	private JLabel timeInMeasureLab;
	private JFileChooser choixMusique = new JFileChooser("/home/anis/workspace/acteEntreprendre/sons/");
	private JButton choisirMusique;
	private JButton getTempo;
	private JLabel nomMusique;
	private File file;
	private String cheminMusique;
	private InfoContainer ic;
	
	public ButtonContainer(){	
		this.setSize(280, 600);
		this.setLayout(new BorderLayout(10, 10));
		this.setLayout(new GridLayout(6, 1, 5, 5));
		this.setBorder(BorderFactory.createTitledBorder("ToolBox"));
		
		choisirMusique = new JButton("Sélectionner la musique");
		TitledBorder choixBorder = BorderFactory.createTitledBorder("Musique");
		choixBorder.setTitleFont(choixBorder.getTitleFont().deriveFont(Font.BOLD ));
		choisirMusique.setBorder(choixBorder);
		
		launch = new JButton("Get Chromagram !");
		TitledBorder launchBorder = BorderFactory.createTitledBorder("Launch");
		launchBorder.setTitleFont(launchBorder.getTitleFont().deriveFont(Font.BOLD ));
		launch.setBorder(launchBorder);
		
		getTempo = new JButton("Get Tempo !");
		TitledBorder tempoBorder = BorderFactory.createTitledBorder("Tempo");
		tempoBorder.setTitleFont(tempoBorder.getTitleFont().deriveFont(Font.BOLD ));
		getTempo.setBorder(tempoBorder);
		
		more = new JButton(">>");
		TitledBorder moreBorder = BorderFactory.createTitledBorder("More");
		moreBorder.setTitleFont(moreBorder.getTitleFont().deriveFont(Font.BOLD ));
		//more.setBorder(moreBorder);
		
		less = new JButton("<<");
		TitledBorder lessBorder = BorderFactory.createTitledBorder("Less");
		lessBorder.setTitleFont(lessBorder.getTitleFont().deriveFont(Font.BOLD ));
		//less.setBorder(lessBorder);

		/*musicTime = new JLabel("0");
		TitledBorder timeBorder = BorderFactory.createTitledBorder("Time");
		timeBorder.setTitleFont(timeBorder.getTitleFont().deriveFont(Font.BOLD ));
		musicTime.setBorder(timeBorder);
		
		alledgedChord = new JLabel();
		TitledBorder chordBorder = BorderFactory.createTitledBorder("Chord");
		chordBorder.setTitleFont(chordBorder.getTitleFont().deriveFont(Font.BOLD ));
		alledgedChord.setBorder(chordBorder);
		
		nthSpectrum = new JLabel(String.valueOf(index));
		TitledBorder spectrumBorder = BorderFactory.createTitledBorder("nth spectrum frame");
		spectrumBorder.setTitleFont(spectrumBorder.getTitleFont().deriveFont(Font.BOLD ));
		nthSpectrum.setBorder(spectrumBorder);
		
	    tempoLab = new JLabel("0");
	    TitledBorder tempoFinderBorder = BorderFactory.createTitledBorder("Tempo");
	    tempoFinderBorder.setTitleFont(tempoFinderBorder.getTitleFont().deriveFont(Font.BOLD ));
		tempoLab.setBorder(tempoFinderBorder);
		
	    measureLab = new JLabel("0");
	    TitledBorder measureBorder = BorderFactory.createTitledBorder("Measure");
	    measureBorder.setTitleFont(measureBorder.getTitleFont().deriveFont(Font.BOLD ));
		measureLab.setBorder(measureBorder);
		
	    timeInMeasureLab = new JLabel("0");
	    TitledBorder nthTimeBorder = BorderFactory.createTitledBorder("Time in measure");
	    nthTimeBorder.setTitleFont(nthTimeBorder.getTitleFont().deriveFont(Font.BOLD ));
		timeInMeasureLab.setBorder(nthTimeBorder);*/
	    
	    nomMusique = new JLabel("Pas de musique encore sélectionnée");
		
	    choisirMusique.addActionListener(this);
	    getTempo.addActionListener(this);
	    launch.addActionListener(this);
	    more.addActionListener(this);
	    less.addActionListener(this);
	    
	    this.add(choisirMusique);
	    this.add(nomMusique);
	    this.add(launch);
	    this.add(getTempo);
	    this.add(more);
	    this.add(less);
	    /*this.add(nthSpectrum);
	    this.add(musicTime);
	    this.add(alledgedChord);
	    this.add(tempoLab);
	    this.add(measureLab);
	    this.add(timeInMeasureLab);*/
	    this.setVisible(true);
	    this.setSize(90, 50);
	}
	
	public void setTempo(int t){
		tempo = t;
	}
	
	
	public void setInfoContainer(InfoContainer inf){
		ic = inf;
	}
	
	public void setAssociatedPanel(SoundPanel sp){
		associatedPanel = sp;
	}
	
	public void setParentContainer(Fenetre parent){
		parentContainer = parent;
	}
	
	public void setCurrentChromagram(Chromagram chr){
		currentChromagram = chr;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == launch){
			if(cheminMusique != null){
				ic.index = 0;
				System.out.println(cheminMusique);
				Chromagram chr = new Chromagram(cheminMusique);
				setCurrentChromagram(chr);
				chr.setPanel(parentContainer.getSoundPanel());
				chr.setAmplitudeScrollPane(parentContainer.getAmplitudeScrollPane());
				parentContainer.getAmplitudeScrollPane().initEnvelope();
				ic.tempoLab.setText(String.valueOf(ic.tempo));
				Thread thread = new Thread(chr);
				Progress prog = new Progress(thread, chr, this);
				try {
					thread.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(isLaunched){
					ic.musicTime.setText(String.valueOf(chr.getMusicTime(ic.index)) + " s");
					ChromaVector cv = currentChromagram.chromagram[ic.index];
					//alledgedChord.setText(String.valueOf(cv.findMaxCorrelation()));	
				}
			}
				
			else{
				new MusiqueDialog(this.parentContainer.getWidth()/2, this.getHeight()/2);
			}
		 }
		
		//Attention la localisation en mesure ne marche que pour du 4/4
		else if(e.getSource() == more){
			ic.index++;
			float time = currentChromagram.getMusicTime(ic.index);
			int timeCount = (int) (time*60/tempo);
			ic.measure = (int) timeCount/4;
			ic.timeInMeasure = timeCount%4 + 1;
			ic.measureLab.setText(String.valueOf(ic.measure));
			ic.timeInMeasureLab.setText(String.valueOf(ic.timeInMeasure));
			ic.nthSpectrum.setText(String.valueOf(ic.index));
			associatedPanel.setGraph(currentChromagram.spectrum[ic.index]);
			ic.musicTime.setText(String.valueOf(time) + " s");
			ic.alledgedChord.setText(String.valueOf(currentChromagram.chordSerie[ic.index]));				
		}
		
		
		else if(e.getSource() == less){
			if(ic.index != 0){
				ic.index--;
			}
			float time = currentChromagram.getMusicTime(ic.index);
			int timeCount = (int) (time*60/tempo);
			ic.measure = (int) timeCount/4;
			ic.timeInMeasure = timeCount%4;
			ic.measureLab.setText(String.valueOf(ic.measure));
			ic.timeInMeasureLab.setText(String.valueOf(ic.timeInMeasure));
			ic.nthSpectrum.setText(String.valueOf(ic.index));
			associatedPanel.setGraph(currentChromagram.spectrum[ic.index]);
			ic.musicTime.setText(String.valueOf(currentChromagram.getMusicTime(ic.index))+" s");
			ic.alledgedChord.setText(String.valueOf(currentChromagram.chordSerie[ic.index]));				
		}
		
		else if(e.getSource() == choisirMusique){
			int returnVal = choixMusique.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            file = choixMusique.getSelectedFile();
	            nomMusique.setText(file.getName());
	            cheminMusique = file.getPath();
	        } else {
	        }
	        
		}
		
		else if(e.getSource() == getTempo){
			parentContainer.printBeatFinder();
		}
	 }
}
