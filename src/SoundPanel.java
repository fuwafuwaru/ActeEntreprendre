import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;

public class SoundPanel extends JPanel {
	public double[] graph;
	private Fenetre parentContainer;
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
	
	
	public void setGraph(double[] b){
		graph = b;
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
			for(int k=0; k < graph.length; k++){;
				g.setColor(new Color(35, 142, 200));
				g.fillRect(5*(k-46),h-((int) graph[k])/200, 2,((int) graph[k]));
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
}