import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.ScrollPaneLayout;


class DrawingPanel extends JPanel {
	private Envelope e;
	private boolean launched = false;
	
	DrawingPanel(Envelope env){
		e = env;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(20000, 400));
		repaint();
		this.setVisible(true);
	}
	
	public void setEnvelope(Envelope env){
		e = env;
	}
	
	public void setLaunched(){
		launched = true;
		repaint();
	}
	
	public Envelope getEnvelope(){
		return e;
	}
		
		@Override
	public void paint(Graphics g){
		super.paintComponent(g);
		int h = this.getHeight();
		int w = this.getWidth();
		EnvPoint point;
		g.setColor(Color.BLUE);
		if(launched){				
			for(int k = 0; k < e.currentGraph.length; k++){
				point = e.currentGraph[k];
				if(point != null){
					if(point.getY() >= 0){							
						g.fillRect(k, (int) (0.5*h- point.getY()), 1, (int) (point.getY()));
					}
					else{
						g.fillRect(k, (int) (0.5*h), 1, (int) (-point.getY()));
					}
				}
					
			}
		}
		else{
			for(int k = 0; k < this.getWidth(); k++){
				g.fillRect(k, 100, 3, 3);
			}
		}
	}
}