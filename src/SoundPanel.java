import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;

public class SoundPanel extends JPanel {
	public Complex[] graph;
	private Fenetre parentContainer;
	
	SoundPanel(){
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createTitledBorder("Spectrum"));
		
		Complex[] temp = new Complex[2000];
		for(int k = 0; k<temp.length; k++){
			temp[k]= new Complex(k);
		}
		setGraph(temp);
		this.setVisible(true);
	}
	

	SoundPanel(Complex[] b){
		this.setBorder(BorderFactory.createTitledBorder("Spectrum"));
		
		setGraph(b);
		this.setVisible(true);
	}
	
	public void setParentContainer(Fenetre parent){
		parentContainer = parent;
	}
	
	
	public void setGraph(Complex[] b){
		graph = b;
		repaint();
	}
	
	public void paint(Graphics g){
		try{
			int h = this.getHeight();
			int w = this.getWidth();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			g.setColor(Color.BLACK);
			for(int k=0; k<this.getWidth() && k < graph.length; k+=2){;
				g.fillRect(k,h-((int) graph[k].abs())/100,2,((int) graph[k].abs()));
			}
		}catch(NullPointerException e){
			System.out.println("Attention NullPointerException");
		}
	}
}