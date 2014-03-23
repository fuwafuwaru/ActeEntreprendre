import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class InfoContainer extends JPanel {
	public JLabel musicTime;
	public JLabel nthSpectrum;
	public JLabel alledgedChord;
	public JLabel tempoLab;
	public JLabel measureLab;
	public JLabel timeInMeasureLab;
	public int index = 0;
	public int tempo;
	public int measure = 0;
	public int timeInMeasure = 0;
	private SharedResources sharedResources;
	
	public InfoContainer(){
		
		this.setLayout(new GridLayout(6, 1, 5, 5));
		
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
		
	    tempoLab = new JLabel("Pas de tempo calculé");
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
		timeInMeasureLab.setBorder(nthTimeBorder);
		
		
	    this.add(nthSpectrum);
	    this.add(musicTime);
	    this.add(alledgedChord);
	    this.add(tempoLab);
	    this.add(measureLab);
	    this.add(timeInMeasureLab);
	    
	    this.setSize(getMinimumSize());
	    this.setVisible(true);

	}
	
	
	public void setTime(){
		
	}
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	public void raiseIndex(){
		index++;
		float time = sharedResources.currentChromagram.getMusicTime(index);
		int timeCount = (int) (time*60/tempo);
		measure = (int) timeCount/4;
		timeInMeasure = timeCount%4 + 1;
		measureLab.setText(String.valueOf(measure));
		timeInMeasureLab.setText(String.valueOf(timeInMeasure));
		nthSpectrum.setText(String.valueOf(index));
		sharedResources.soundPanel.setGraph(sharedResources.currentChromagram.spectrum[index]);
		musicTime.setText(String.valueOf(time) + " s");
		alledgedChord.setText(String.valueOf(sharedResources.currentChromagram.chordSerie[index]));
	}
	
	
	public void lowerIndex(){
		if(index != 0){
			index--;
		}
		float time = sharedResources.currentChromagram.getMusicTime(index);
		int timeCount = 0;
		if(tempo != 0){
			timeCount = (int) (time*60/tempo);
		}
		measure = (int) timeCount/4;
		timeInMeasure = timeCount%4;
		measureLab.setText(String.valueOf(measure));
		timeInMeasureLab.setText(String.valueOf(timeInMeasure));
		nthSpectrum.setText(String.valueOf(index));
		sharedResources.soundPanel.setGraph(sharedResources.currentChromagram.spectrum[index]);
		musicTime.setText(String.valueOf(sharedResources.currentChromagram.getMusicTime(index))+" s");
		alledgedChord.setText(String.valueOf(sharedResources.currentChromagram.chordSerie[index]));
	}
	
	/*
	 * Cette méthode est appelée après que l'utilisateur a stoppé la musique
	 */
	public void reInit(){
		index = 0;
		measure = 0;
		timeInMeasure = 0;
		measureLab.setText(String.valueOf(measure));
		timeInMeasureLab.setText(String.valueOf(timeInMeasure));
		nthSpectrum.setText(String.valueOf(index));

	}
	
	
}
