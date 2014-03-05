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
	
	
	
}
