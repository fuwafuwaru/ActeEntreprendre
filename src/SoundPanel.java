import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SoundPanel extends JPanel implements MouseListener {
	public double[] graph;
	private Fenetre parentContainer;
	private int[] localMaximums = {0};
	private double coefficient = 0.05;
	private double dh = 0.01;
	private SharedResources sharedResources;
	//private PrintingPanel printingPanel;
	
	SoundPanel(){
		//printingPanel = new PrintingPanel(this);
		//this.setLayout(new BorderLayout(10, 10));
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
		}	
		repaint();
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
		coefficient = (c/100.)*(c/100.)*(c/100.);
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
		try{
			int h = this.getHeight();
			int w = this.getWidth();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			g.setColor(Color.BLACK);
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
		}catch(NullPointerException e){
			System.out.println("Attention NullPointerException");
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("mouse clicked");
		if(arg0.getButton() == MouseEvent.BUTTON1){//Zoom
			raiseCoefficient();
			System.out.println("zoom");
		}
		
		else if(arg0.getButton() == MouseEvent.BUTTON3){//DeZoom
			lowerCoefficient();
			System.out.println("dezoom");

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
}