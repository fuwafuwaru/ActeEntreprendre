import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SoundPanel extends JPanel implements MouseListener {
	public double[] graph;
	private Fenetre parentContainer;
	public int[] localMaximums = {0};
	private double coefficient = 0.03125;
	private double coefficientX = 1;
	private double dh = 0.01;
	public SharedResources sharedResources;
	//private PrintingPanel printingPanel;
	private RightClickMenu menu;
	private JMenuItem zoom;
	private JMenuItem dezoom;
	private JMenuItem open;
	private JMenu displayMode;
	private JMenuItem classicSpectrum;
	private JMenuItem simpleBars;
	private JMenuItem chromaVector;
	private DisplayMode dispMode = DisplayMode.SPECTRUM;
	private double[] barGraph;
	private int[] localBarMaximum;

	
	
	SoundPanel(){
		//printingPanel = new PrintingPanel(this);
		//this.setLayout(new BorderLayout(10, 10));
		menu = new RightClickMenu();
		this.setBorder(BorderFactory.createTitledBorder("Spectrum"));
		this.setSize(getMaximumSize());
		double[] temp = new double[1000];
		for(int k = 0; k < temp.length; k++){
			temp[k] = k;
		}
		this.setGraph(temp);
		this.addMouseListener(this);
		this.setVisible(true);
	}
	

	SoundPanel(double[] b){
		this.setBorder(BorderFactory.createTitledBorder("Spectrum"));
		
		setGraph(b);
		this.setSize(getMaximumSize());

		this.setVisible(true);
	}
	
	
	SoundPanel(SoundPanel snd){
		this.graph = snd.graph;
		this.localMaximums = snd.localMaximums;
		
	}
	
	
	public void setParentContainer(Fenetre parent){
		parentContainer = parent;
	}
	
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	
	
	public void setGraph(double[] b){
		graph = b;
		if(sharedResources != null){
			if(sharedResources.currentChromagram != null){
				localMaximums = ProcessingTools.findLocalMax(graph, (int) (Pitch.convertFreq(sharedResources.currentChromagram.getMidiMin())/sharedResources.currentChromagram.getSpectralResolution()), (int) (Pitch.convertFreq(sharedResources.currentChromagram.getMidiMin())/sharedResources.currentChromagram.getSpectralResolution()));
			}
			repaint();
			if(sharedResources.ffs != null){
				sharedResources.ffs.repaint();
			}
		}	
		
	}
	
	public void raiseCoefficient(){
		coefficient += dh;
		repaint();
	}
	
	public void lowerCoefficient(){
		if(coefficient - dh <= 0){
			dh = dh/5;
		}
		coefficient -= dh;
		repaint();
	}
	
	public void setZoom(int c){
		coefficient = (c/100.)*(c/100.)*(c/100.)*(c/100.)*(c/100.);
		repaint();
	}
	
	
	public void setScaleX(int s){
		
		coefficientX = s/50.;
		repaint();
		
		
	}
	
	
	
	/*public class PrintingPanel extends JPanel{
		private SoundPanel sp;
		
		PrintingPanel(SoundPanel s){
			sp = s;
			this.setVisible(true);
		}
		
		
		public void paint(Graphics g){
			
		}
	};*/
	
	public void paint(Graphics g){
		super.paint(g);
		int h = this.getHeight();
		int w = this.getWidth();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.BLACK);
		
		/*
		 * Cas d'un style spectrum
		 */
		if(dispMode == DisplayMode.SPECTRUM){
			for(int k=0; k < graph.length; k++){
				g.setColor(new Color(35, 142, 200));
				g.fillRect(5*(k-46),h-((int) (graph[k]*coefficient)), 2,((int) (graph[k]*coefficient)));
			}
			for(int k : localMaximums){//On colorie en rouge les maximums
				g.setColor(Color.red);
				g.fillRect(5*(k-46),h-((int) (graph[k]*coefficient)), 2,((int) (graph[k]*coefficient)));
			}
			g.setColor(Color.black);
			for(int i = 53; i < 105; i++){
				double x = Pitch.convertFreq(i);				
				int u =  (int) (x/5.38); //2048/11025
				g.fillRect(5*(u-46), 100, 1, h-100);
				g.drawString((new Pitch(i)).getChroma().toString(), 5*(u-46), 70);
			}
		}
		
		
		
		/*
		 * Cas d'un style en barres
		 */
		
		else if(dispMode == DisplayMode.BARS){
			double sum = 0;
			Pitch currentNote = new Pitch(46);
			g.setColor(new Color(35, 142, 200));
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			g.setColor(Color.BLACK);
			g.setColor(new Color(35, 142, 200));
			int numberOfBarsDrawn = 0;
			
			
			for(int k=10; k < graph.length; k++){ //Ce qui est trop aigu ne nous intéresse pas
				Pitch n = new Pitch(k*5.38);
				if(currentNote.getMidi() > n.getMidi()){
					//La note est trop basse on s'en fout
				}
				else if(currentNote.getMidi() == n.getMidi()){
					sum += graph[k]; //On somme les amplitudes qui correspondent à la même note
				}
				else{
					/*for(int i = 0; i < sum*coefficient/7; i++){
						g.fillRect(5*(k-46),h-((int) (sum*coefficient) + 7*i + 2), 10, 7);
						sum = graph[k];
						currentNote = n;
					}*/
					numberOfBarsDrawn++;
					g.fillRect((int) (20*coefficientX*(numberOfBarsDrawn)),h-((int) (sum*coefficient)), (int) (10*coefficientX), (int) (sum*coefficient));
					g.setColor(Color.BLACK);
					g.drawString(currentNote.toSimpleString(), (int)(20*(numberOfBarsDrawn)*coefficientX), h-((int) (sum*coefficient))-20);
					g.setColor(new Color(35, 142, 200));
					sum = graph[k];
					currentNote = n;

				}
			}
			
			
			
		}
		
		
		
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getButton() == MouseEvent.BUTTON1){
			menu.setVisible(false);
		}
		
		else if(arg0.getButton() == MouseEvent.BUTTON3){
			
			menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());


		}
		
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	class RightClickMenu extends JPopupMenu implements ActionListener {	
		
		RightClickMenu(){
			
			displayMode = new JMenu("Apparence");
			classicSpectrum = new JMenuItem("Spectre classique");
			simpleBars = new JMenuItem("En barre");
			chromaVector = new JMenuItem("Vecteur de chroma");
			
			classicSpectrum.addActionListener(this);
			simpleBars.addActionListener(this);
			chromaVector.addActionListener(this);
			
			displayMode.add(classicSpectrum);
			displayMode.add(simpleBars);
			displayMode.add(chromaVector);
			
			
			
			
			zoom = new JMenuItem("Zoom");
			dezoom = new JMenuItem("Dezoom");
			open = new JMenuItem("Ouvrir dans une autre fenêtre");
			
			zoom.addActionListener(this);
			dezoom.addActionListener(this);
			open.addActionListener(this);
			
			add(zoom);
			add(dezoom);
			add(open);
			add(displayMode);
			
			setVisible(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(e.getSource() == zoom){
				raiseCoefficient();
				
			}
			else if(e.getSource() == dezoom){
				lowerCoefficient();
			}
			
			else if(e.getSource() == open){
				//TODO : ouvrir spectre dans une nouvelle fenêtre
				SoundPanel sp = sharedResources.soundPanel;
				sharedResources.ffs = new FullFrameSpectrum(new SoundPanel(sp));
			}
			
			
			else if(e.getSource() == classicSpectrum){
				
			}
			
			else if(e.getSource() == simpleBars){
				dispMode = DisplayMode.BARS;
				repaint();
			}
			
			else if(e.getSource() == chromaVector){
				
			}
		}
		
		
	}
	
	enum DisplayMode {SPECTRUM, BARS, VECTOR};
	
}