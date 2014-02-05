import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Fenetre extends JFrame{
	 private SoundPanel snd;
	 private AmplitudeScrollPane asp;
	 public Fenetre(){
		 
		 this.setTitle("Fenetre principale");
		 this.setSize(1300, 900);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLocationRelativeTo(null);
		 this.setResizable(true);
		 JSplitPane split;
		 JSplitPane split2;
		 
		 JPanel inter = new JPanel();
		 
		 
		 snd = new SoundPanel();
		 snd.setParentContainer(this);
		 snd.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		 
		 Envelope env = new Envelope();
		 DrawingPanel dp = new DrawingPanel(new Envelope());
		 dp.setPreferredSize(new Dimension(2000, 400));
		 System.out.println(dp.getWidth());
		 asp = new AmplitudeScrollPane();
		 //asp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 
		 
		 ButtonContainer buttonContainer = new ButtonContainer();
		 //BeatFinderManual bfm = new BeatFinderManual(buttonContainer);
		 buttonContainer.setParentContainer(this);
		 buttonContainer.setAssociatedPanel(snd);
		 
		 
		 inter.setLayout(new BorderLayout());
		 inter.add(snd, BorderLayout.CENTER);
		 inter.add(buttonContainer, BorderLayout.WEST);
		 
		 split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonContainer, snd);
		 split.setOneTouchExpandable(true);
		 split.setDividerLocation(200);
		 
		 split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split, asp);
		 split2.setOneTouchExpandable(true);
		 split2.setDividerLocation(this.getHeight() - 400);

		 //this.setContentPane(inter);
		 this.setContentPane(split2);
		 this.pack();
		 this.setVisible(true);
		 //bfm.getFrame().setVisible(true);
	 }
	 
	 
	 public SoundPanel getSoundPanel(){
		 return snd;
	 }
	 
	 public AmplitudeScrollPane getAmplitudeScrollPane(){
		 return asp;
	 }
	 
}