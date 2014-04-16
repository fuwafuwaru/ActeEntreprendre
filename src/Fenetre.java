import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Fenetre extends JFrame{
	 private SoundPanel snd;
	 private AmplitudeScrollPane asp;
	 private BeatFinderManual bfm;
	 private InfoContainer infoContainer;
	 private Chromagram currentChromagram;
	 public SharedResources sharedResources;
	 private ButtonContainer buttonContainer;
	 private JMenuBar menuBar;
	 private JMenu fichier;
	 public JMenuItem nouveau;
	 public JMenuItem ouvrir;
	 public JMenuItem enregistrer;
	 public JMenuItem quitter;
	 
	 public Fenetre(SharedResources share){
		 sharedResources = share;

		 //this.setExtendedState(JFrame.MAXIMIZED_VERT);		
		 this.setSize(1300, 1000);
		 this.setTitle("Eufonya");
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLocationRelativeTo(null);
		 this.setResizable(true);
		 JSplitPane split;
		 JSplitPane split2;
		 JScrollPane soundPanelContainer;
		 JPanel finalContainerSpectrum = new JPanel();
		 JPanel inter = new JPanel();
		 
		 buttonContainer = new ButtonContainer();

		 
		 setMenuBar();
		 setJMenuBar(menuBar);
		 
		 
		 infoContainer = new InfoContainer();
		 infoContainer.setSharedResources(sharedResources);

		 
		 snd = new SoundPanel();
		 snd.setSharedResources(sharedResources);
		 snd.setParentContainer(this);
		 snd.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		 soundPanelContainer = new JScrollPane(snd);
		 finalContainerSpectrum.add(soundPanelContainer, BorderLayout.CENTER);
		 finalContainerSpectrum.setVisible(true);
                

		 Envelope env = new Envelope();
		 DrawingPanel dp = new DrawingPanel(new Envelope());
		 dp.setSharedResources(sharedResources);
		 dp.setPreferredSize(new Dimension(2000, 400));
		 System.out.println(dp.getWidth());
		 asp = new AmplitudeScrollPane();
		 asp.setSharedResources(sharedResources);
		 //asp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 
		 
		 bfm = new BeatFinderManual(buttonContainer);
		 buttonContainer.setParentContainer(this);
		 buttonContainer.setAssociatedPanel(snd);
		 buttonContainer.setInfoContainer(infoContainer);
		 buttonContainer.setSharedResources(sharedResources);

	        
		 
		 inter.setLayout(new BorderLayout());
		 inter.add(snd, BorderLayout.CENTER);
		 inter.add(buttonContainer, BorderLayout.WEST);
		 
		 split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonContainer, snd);
		 split.setOneTouchExpandable(true);
		 split.setDividerLocation(200);
		 
		 JSplitPane split3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split, infoContainer);
		 split3.setOneTouchExpandable(true);
		 split3.setResizeWeight(0.95);
		 
		 split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split3, asp);
		 split2.setOneTouchExpandable(true);
		 split2.setResizeWeight(0.9);

		 //this.setContentPane(inter);
		 this.setContentPane(split2);
		 this.pack();
		 this.setVisible(true);
		 sharedResources.buttonContainer = buttonContainer;
		 sharedResources.soundPanel = snd;
		 sharedResources.infoContainer = infoContainer;
		 sharedResources.currentChromagram = currentChromagram;
		 sharedResources.amplitudeScrollPane = asp;
		 sharedResources.drawingPanel = dp;
		 
	 }
	 
	 
	 public void setCurrentChromagram(Chromagram chr){
		 currentChromagram = chr;
		 
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
	 
	 
	 //Fonction qui gère la mise en place du menu supérieur
	 
	 
	 public void setMenuBar(){
		 menuBar = new JMenuBar();
		 fichier = new JMenu("Fichier");
		 menuBar.add(fichier);
		 
		 nouveau = new JMenuItem("Nouveau");
		 fichier.add(nouveau);
		 nouveau.addActionListener(buttonContainer);
		 
		 ouvrir = new JMenuItem("Ouvrir");
		 fichier.add(ouvrir);
		 ouvrir.addActionListener(buttonContainer);
		 
		 enregistrer = new JMenuItem("Enregistrer");
		 fichier.add(enregistrer);
		 enregistrer.addActionListener(buttonContainer);
		 
		 quitter = new JMenuItem("Quitter");
		 fichier.add(quitter);
		 quitter.addActionListener(buttonContainer);
	 }
	 
	 
	 
	 class MenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		 
	 }
	 
	 
	 
	 
}