import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.ScrollPaneLayout;


class DrawingPanel extends JPanel implements MouseListener {
	private Envelope e;
	private boolean launched = false;
	private double coefficient = 0.05;

	
	DrawingPanel(Envelope env){
		e = env;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(env.getCurrentGraph().length, 400));
		repaint();
		this.addMouseListener(this);
		this.setVisible(true);
	}
	
	public void setEnvelope(Envelope env){
		e = env;
	}
	
	public void setLaunched(){
		launched = true;
		repaint();
	}
	
	public void raiseCoefficient(){
		coefficient += 0.001;
		repaint();
	}
	
	public void lowerCoefficient(){
		coefficient -= 0.001;
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
					g.fillRect(k, (int) (0.5*h- Math.abs(point.getY())*coefficient), 1, (int) (Math.abs(point.getY())*coefficient) + 1);
					g.fillRect(k, (int) (0.5*h)-1, 1, (int) (Math.abs(point.getY())*coefficient));
				}
					
			}
		}
		else{
			for(int k = 0; k < this.getWidth(); k++){
				g.fillRect(k, 100, 3, 3);
			}
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