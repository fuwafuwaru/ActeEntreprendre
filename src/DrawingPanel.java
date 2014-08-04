import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;
import javax.swing.ScrollPaneLayout;


class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {
	private Envelope e;
	private boolean loaded = false;
	private double coefficientY = 0.05;
	private int coefficientX = 2;
	private int cursor = 0;
	private boolean launched = false;
	private SharedResources sharedResources;
	private double dedicatedLength;
	private double reductionFactor = 10;
	private boolean pressed = false;
	private int pressedX = 0; 
	private int pressedY = 0;
	private int currentX = 0;
	private int currentY = 0;
	private int releasedX = 0;
	private int releasedY = 0;
	private int previousPressedX = 0;
	private int previousReleasedX = 0;
	
	DrawingPanel(Envelope env){
		e = env;
		dedicatedLength = env.getCurrentGraph().length/3;
		System.out.println("taille de l'enveloppe : " + env.getCurrentGraph().length);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension((int) dedicatedLength + 300, 100));
		this.addMouseMotionListener(this);
		repaint();
		this.addMouseListener(this);
		this.setVisible(true);
	}
	
	
	public void setDedicatedLength(){
		
		dedicatedLength = e.getCurrentGraph().length/reductionFactor;
		this.setPreferredSize(new Dimension((int) dedicatedLength + 300, 100));
		System.out.println(this.getWidth());
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
				
				g.setColor(new Color(100, 100, 100));
				if(currentX-pressedX >= 0){
					g.fillRect(pressedX, 0, currentX-pressedX, h);
					g.setColor(Color.BLUE);

					for(int k = 0; k < e.currentGraph.length-reductionFactor; k+=reductionFactor ){
						EnvPoint tmp = new EnvPoint(0.);
				
						for(int i = 0; i < reductionFactor; i++){
							tmp.setY(tmp.getY()+e.currentGraph[k+i].getY()/reductionFactor);
						}
						point = tmp;
						if(point != null){	
							if(k/reductionFactor >= pressedX && k/reductionFactor <= currentX){
								g.setColor(Color.red);
							}
						
							g.fillRect((int) (k/reductionFactor), (int) (0.5*h- Math.abs(point.getY())*coefficientY), 1, (int) (Math.abs(point.getY())*coefficientY) + 1);
							g.fillRect((int) (k/reductionFactor), (int) (0.5*h)-1, 1, (int) (Math.abs(point.getY())*coefficientY));
							g.setColor(Color.blue);
						}
						
					}	
				}
				else{
					int x = pressedX - currentX;
					g.fillRect(pressedX-x, 0, x, h);
					g.setColor(Color.BLUE);

					for(int k = 0; k < e.currentGraph.length-reductionFactor; k+=reductionFactor ){
						EnvPoint tmp = new EnvPoint(0.);
				
						for(int i = 0; i < reductionFactor; i++){
							tmp.setY(tmp.getY()+e.currentGraph[k+i].getY()/reductionFactor);
						}
						point = tmp;
						if(point != null){	
							if(k/reductionFactor >= currentX && k/reductionFactor <= pressedX){
								g.setColor(Color.red);
							}
						
							g.fillRect((int) (k/reductionFactor), (int) (0.5*h- Math.abs(point.getY())*coefficientY), 1, (int) (Math.abs(point.getY())*coefficientY) + 1);
							g.fillRect((int) (k/reductionFactor), (int) (0.5*h)-1, 1, (int) (Math.abs(point.getY())*coefficientY));
							g.setColor(Color.blue);
						}
						
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
				g.fillRect(k, 50, 3, 3);
			}
			
			
		
			
		}
	}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			if(previousReleasedX-previousPressedX>=0){
				paintImmediately(previousPressedX, 0, previousReleasedX-previousPressedX, getHeight());
			}
			else{
				int x = previousPressedX - previousReleasedX;
				paintImmediately(previousPressedX-x, 0, x, getHeight());
	
			}
			pressedX = 0;
			pressedY = 0;
			releasedX = 0;
			currentX = 0;
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public void mouseWheelRolled(MouseWheelEvent e){
			System.out.println(e.getWheelRotation());
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(previousReleasedX-previousPressedX>=0){
				paintImmediately(previousPressedX, 0, previousReleasedX-previousPressedX, getHeight());
			}
			else{
				int x = previousPressedX-previousReleasedX;
				paintImmediately(previousPressedX-x, 0, x, getHeight());
			}
			pressed = true;
			previousPressedX = pressedX;
			pressedX = arg0.getX();
			pressedY = arg0.getY();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			pressed = false;
			previousReleasedX = releasedX;		
			releasedX = arg0.getX();
			releasedY = arg0.getY();
			currentX = arg0.getX();
			//repaint();
			if(releasedX - pressedX >= 0){
				paintImmediately(pressedX, 0,releasedX - pressedX, getHeight());	
			}
			else{
				int x = pressedX- releasedX;
				paintImmediately(pressedX-x, 0,x, getHeight());	
			}
			previousPressedX = pressedX;
			previousReleasedX = releasedX;
			pressedX = 0;
			releasedX = 0;
			
			
		}


		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(pressed){
				
				currentX = arg0.getX();
				currentY = arg0.getY();
				repaint();
				//this.paintImmediately(pressedX, 0, currentX-pressedX, this.getHeight());
			}
		}


		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
}