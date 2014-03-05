import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Fenetre extends JFrame{
	 private SoundPanel snd;
	 private AmplitudeScrollPane asp;
	 private BeatFinderManual bfm;
	 private InfoContainer infoContainer;
	 
	 public Fenetre(){
		 
		 this.setTitle("Fenetre principale");
		 this.setSize(1300, 900);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLocationRelativeTo(null);
		 this.setResizable(true);
		 JSplitPane split;
		 JSplitPane split2;
		 JScrollPane soundPanelContainer;
		 JPanel finalContainerSpectrum = new JPanel();
		 JPanel inter = new JPanel();
		 
		 
		 infoContainer = new InfoContainer();

		 
		 snd = new SoundPanel();
		 snd.setParentContainer(this);
		 snd.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		 soundPanelContainer = new JScrollPane(snd);
		 finalContainerSpectrum.add(soundPanelContainer, BorderLayout.CENTER);
		 finalContainerSpectrum.setVisible(true);
                

		 Envelope env = new Envelope();
		 DrawingPanel dp = new DrawingPanel(new Envelope());
		 dp.setPreferredSize(new Dimension(2000, 400));
		 System.out.println(dp.getWidth());
		 asp = new AmplitudeScrollPane();
		 //asp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 
		 
		 ButtonContainer buttonContainer = new ButtonContainer();
		 bfm = new BeatFinderManual(buttonContainer);
		 buttonContainer.setParentContainer(this);
		 buttonContainer.setAssociatedPanel(snd);
		 buttonContainer.setInfoContainer(infoContainer);

	        
		 
		 inter.setLayout(new BorderLayout());
		 inter.add(snd, BorderLayout.CENTER);
		 inter.add(buttonContainer, BorderLayout.WEST);
		 
		 split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonContainer, snd);
		 split.setOneTouchExpandable(true);
		 split.setDividerLocation(200);
		 
		 JSplitPane split3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split, infoContainer);
		 split3.setOneTouchExpandable(true);
		 split3.setResizeWeight(0.8);
		 
		 split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split3, asp);
		 split2.setOneTouchExpandable(true);
		 split2.setDividerLocation(this.getHeight() - 400);

		 //this.setContentPane(inter);
		 this.setContentPane(split2);
		 this.pack();
		 this.setVisible(true);
		 bfm.getFrame().setVisible(false);
	 }
	 
	 
	 public SoundPanel getSoundPanel(){
		 return snd;
	 }
	 
	 public AmplitudeScrollPane getAmplitudeScrollPane(){
		 return asp;
	 }
	 	 
	 public void printBeatFinder(){
		 bfm.getFrame().setVisible(true);
	 }
}