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
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(5000, 400));
		this.setVisible(true);
	}
	
	public void setEnvelope(Envelope env){
		e = env;
	}
	
	public void setLaunched(){
		launched = true;
	}
	
	public Envelope getEnvelope(){
		return e;
	}
		
		@Override
	public void paint(Graphics g){
		int h = this.getHeight();
		int w = this.getWidth();
		EnvPoint point;
		g.setColor(Color.BLUE);
		if(launched){				
			for(int k = 0; k < e.currentGraph.length; k++){
				point = e.currentGraph[k];
				if(point != null){
					if(point.getY() >= 0){							
						g.fillRect(k, (int) (0.5*h- point.getY()/500), 1, (int) (point.getY()/500));
					}
					else{
						g.fillRect(k, (int) (0.5*h), 1, (int) (-point.getY()/500));
					}
				}
					
			}
		}
		else{
			for(int k = 0; k < 5000; k++){
				g.fillRect(k, 100, 3, 3);
			}
		}
	}
}