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
	private boolean loaded = false;
	private double coefficientY = 0.05;
	private int coefficientX = 2;
	private int cursor = 0;
	private boolean launched = false;
	private SharedResources sharedResources;
	private double dedicatedLength;
	private double reductionFactor = 100;

	
	DrawingPanel(Envelope env){
		e = env;
		dedicatedLength = env.getCurrentGraph().length/reductionFactor;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension((int) dedicatedLength, 200));
		repaint();
		this.addMouseListener(this);
		this.setVisible(true);
	}
	
	public void setEnvelope(Envelope env){
		e = env;
	}
	
	public void setLoaded(){
		loaded = true;
		System.out.println("Le dessin d'amplitude a été chargé");
		repaint();
	}
	
	public void setLaunched(){
		launched = true;
		System.out.println("Launched is set");
	}
	
	public void raiseCoefficient(){
		coefficientY += 0.001;
		coefficientX++;
		repaint();
	}
	
	public void lowerCoefficient(){
		coefficientY -= 0.001;
		coefficientX--;
		repaint();
	}
	
	public Envelope getEnvelope(){
		return e;
	}
		
	
	public void setCursor(double x){
		cursor = (int) (x*dedicatedLength);
		this.paintImmediately(cursor, 0, 4, this.getHeight());
		//System.out.println(cursor);
		repaint();
	}
	
	

	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
		@Override
	public void paint(Graphics g){
		//System.out.println("Appel à la fonction repaint");
		super.paintComponent(g);
		int h = this.getHeight();
		int w = this.getWidth();
		EnvPoint point;
		g.setColor(Color.BLUE);
		if(loaded){			
			if(launched == false){
				/*for(int k = 0; k < e.currentGraph.length; k++){
					point = e.currentGraph[k];
					if(point != null){						
						g.fillRect(k*coefficientX, (int) (0.5*h- Math.abs(point.getY())*coefficientY), 1, (int) (Math.abs(point.getY())*coefficientY) + 1);
						g.fillRect(k*coefficientX, (int) (0.5*h)-1, 1, (int) (Math.abs(point.getY())*coefficientY));
					}
					
				}*/
				
				for(int k = 0; k < e.currentGraph.length; k += reductionFactor){
					point = e.currentGraph[k];
					if(point != null){						
						g.fillRect((int) (k/reductionFactor), (int) (0.5*h- Math.abs(point.getY())*coefficientY), 1, (int) (Math.abs(point.getY())*coefficientY) + 1);
						g.fillRect((int) (k/reductionFactor), (int) (0.5*h)-1, 1, (int) (Math.abs(point.getY())*coefficientY));
					}
					
				}	
			}
			
			else{
				System.out.println("On est dans la deuxième condition du loaded");
				g.fillRect(cursor, 0, 3, h);
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