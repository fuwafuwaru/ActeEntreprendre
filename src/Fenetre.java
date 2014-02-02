import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Fenetre extends JFrame{
	 private SoundPanel snd;
	 
	 public Fenetre(){
		 
		 this.setTitle("Fenetre principale");
		 this.setSize(1000, 600);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLocationRelativeTo(null);
		 this.setResizable(true);
		 JSplitPane split;
		 //JSplitPane split2; pour plus tard
		 
		 JPanel inter = new JPanel();
		 
		 
		 snd = new SoundPanel();
		 snd.setParentContainer(this);
		 snd.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		 
		 
		 ButtonContainer buttonContainer = new ButtonContainer();
		 BeatFinderManual bfm = new BeatFinderManual(buttonContainer);
		 buttonContainer.setParentContainer(this);
		 buttonContainer.setAssociatedPanel(snd);
		 
		 /*JPanel containerButton = new JPanel();
		 containerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		 containerButton.add(buttonContainer, BorderLayout.CENTER);
		 containerButton.setSize(280, this.getHeight());*/
		 
		 inter.setLayout(new BorderLayout());
		 inter.add(snd, BorderLayout.CENTER);
		 inter.add(buttonContainer, BorderLayout.WEST);
		 
		 split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonContainer, snd);
		 split.setOneTouchExpandable(true);
		 split.setDividerLocation(200);
		 
		 //this.setContentPane(inter);
		 this.setContentPane(split);
		 this.setVisible(true);
	 }
	 
	 
	 public SoundPanel getSoundPanel(){
		 return snd;
	 }
	 
}