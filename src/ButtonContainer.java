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
	private JFileChooser choixMusique = new JFileChooser("/home/Anis/workspace/ActeEntreprendre/sons/");
	private JButton choisirMusique;
	private JLabel nomMusique;
	private File file;
	private String cheminMusique;
	
	
	public ButtonContainer(){		
		this.setSize(280, 600);
		this.setLayout(new BorderLayout(10, 10));
		this.setLayout(new GridLayout(11, 1, 5, 5));
		this.setBorder(BorderFactory.createTitledBorder("ToolBox"));
		
		choisirMusique = new JButton("Sélectionner la musique");
		TitledBorder choixBorder = BorderFactory.createTitledBorder("Musique");
		choixBorder.setTitleFont(choixBorder.getTitleFont().deriveFont(Font.BOLD ));
		choisirMusique.setBorder(choixBorder);
		
		launch = new JButton("Get Chromagram !");
		TitledBorder launchBorder = BorderFactory.createTitledBorder("Launch");
		launchBorder.setTitleFont(launchBorder.getTitleFont().deriveFont(Font.BOLD ));
		launch.setBorder(launchBorder);
		
		more = new JButton(">>");
		TitledBorder moreBorder = BorderFactory.createTitledBorder("More");
		moreBorder.setTitleFont(moreBorder.getTitleFont().deriveFont(Font.BOLD ));
		//more.setBorder(moreBorder);
		
		less = new JButton("<<");
		TitledBorder lessBorder = BorderFactory.createTitledBorder("Less");
		lessBorder.setTitleFont(lessBorder.getTitleFont().deriveFont(Font.BOLD ));
		//less.setBorder(lessBorder);

		musicTime = new JLabel("0");
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
	    TitledBorder tempoBorder = BorderFactory.createTitledBorder("Tempo");
	    tempoBorder.setTitleFont(tempoBorder.getTitleFont().deriveFont(Font.BOLD ));
		tempoLab.setBorder(tempoBorder);
		
	    measureLab = new JLabel("0");
	    TitledBorder measureBorder = BorderFactory.createTitledBorder("Measure");
	    measureBorder.setTitleFont(measureBorder.getTitleFont().deriveFont(Font.BOLD ));
		measureLab.setBorder(measureBorder);
		
	    timeInMeasureLab = new JLabel("0");
	    TitledBorder nthTimeBorder = BorderFactory.createTitledBorder("Time in measure");
	    nthTimeBorder.setTitleFont(nthTimeBorder.getTitleFont().deriveFont(Font.BOLD ));
		timeInMeasureLab.setBorder(nthTimeBorder);
	    
	    nomMusique = new JLabel("Pas de musique encore sélectionnée");
		
	    choisirMusique.addActionListener(this);
	    launch.addActionListener(this);
	    more.addActionListener(this);
	    less.addActionListener(this);
	    
	    this.add(choisirMusique);
	    this.add(nomMusique);
	    this.add(launch);
	    this.add(more);
	    this.add(less);
	    this.add(nthSpectrum);
	    this.add(musicTime);
	    this.add(alledgedChord);
	    this.add(tempoLab);
	    this.add(measureLab);
	    this.add(timeInMeasureLab);
	    this.setVisible(true);
	    this.setSize(90, 50);
	}
	
	public void setTempo(int t){
		tempo = t;
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
			/*isLaunched = true;
			Chromagram chr = new Chromagram("/home/anis/workspace/acteEntreprendre/sons/dresden.wav");
			setCurrentChromagram(chr);
			chr.setPanel(parentContainer.getSoundPanel());
			tempoLab.setText(String.valueOf(tempo));
			try{
				chr.process();
				musicTime.setText(String.valueOf(chr.getMusicTime(index)) + " s");
				ChromaVector cv = currentChromagram.chromagram[index];
				alledgedChord.setText(String.valueOf(cv.findMaxCorrelation()));
			 }catch(IOException err){
			 }*/
			
			//Chromagram chr = new Chromagram("/home/anis/workspace/acteEntreprendre/sons/dresden.wav");
			//Chromagram chr = new Chromagram("sons/dresden.wav");
			if(cheminMusique != null){
				System.out.println(cheminMusique);
				Chromagram chr = new Chromagram(cheminMusique);
				setCurrentChromagram(chr);
				chr.setPanel(parentContainer.getSoundPanel());
				tempoLab.setText(String.valueOf(tempo));
				Thread thread = new Thread(chr);
				Progress prog = new Progress(thread, chr, this);
				try {
					thread.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(isLaunched){
					musicTime.setText(String.valueOf(chr.getMusicTime(index)) + " s");
					ChromaVector cv = currentChromagram.chromagram[index];
					alledgedChord.setText(String.valueOf(cv.findMaxCorrelation()));	
				}
			}
				
			else{
				new MusiqueDialog(this.getWidth()/2, this.getHeight()/2);
			}
		 }
		
		//Attention la localisation en mesure ne marche que pour du 4/4
		else if(e.getSource() == more){
			index++;
			float time = currentChromagram.getMusicTime(index);
			int timeCount = (int) (time*60/tempo);
			measure = (int) timeCount/4;
			timeInMeasure = timeCount%4 + 1;
			measureLab.setText(String.valueOf(measure));
			timeInMeasureLab.setText(String.valueOf(timeInMeasure));
			nthSpectrum.setText(String.valueOf(index));
			associatedPanel.setGraph(currentChromagram.spectrum[index]);
			musicTime.setText(String.valueOf(time) + " s");
			alledgedChord.setText(String.valueOf(currentChromagram.chordSerie[index]));				
		}
		
		
		else if(e.getSource() == less){
			index--;
			float time = currentChromagram.getMusicTime(index);
			int timeCount = (int) (time*60/tempo);
			measure = (int) timeCount/4;
			timeInMeasure = timeCount%4;
			measureLab.setText(String.valueOf(measure));
			timeInMeasureLab.setText(String.valueOf(timeInMeasure));
			nthSpectrum.setText(String.valueOf(index));
			associatedPanel.setGraph(currentChromagram.spectrum[index]);
			musicTime.setText(String.valueOf(currentChromagram.getMusicTime(index))+" s");
			alledgedChord.setText(String.valueOf(currentChromagram.chordSerie[index]));				
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
	 }
}
