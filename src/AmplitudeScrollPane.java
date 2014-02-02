import java.awt.Color;
import java.awt.Graphics;
import java.util.ListIterator;

import javax.swing.JScrollPane;


public class AmplitudeScrollPane extends JScrollPane {
	
	private Envelope e;
	
	AmplitudeScrollPane(Envelope env){
		
	}
	
	@Override
	public void paint(Graphics g){
		ListIterator<EnvPoint> iter;
		int i = 0;
		int h = this.getHeight();
		int w = this.getWidth();
		EnvPoint point;
		g.setColor(Color.BLUE);
		for(iter = e.currentGraph.listIterator(0); iter.hasNext();){
			point = iter.next();
			g.fillRect(i, (int) (h*(1-point.getY())), 1, (int) (h*point.getY()));
		}
	}
}
