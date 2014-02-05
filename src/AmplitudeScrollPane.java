import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;


public class AmplitudeScrollPane extends JScrollPane {
	
	private Envelope e;
	private boolean launched = false;
	private DrawingPanel drawingPanel;
	
	AmplitudeScrollPane(DrawingPanel dp){
		this.setLayout(new ScrollPaneLayout());
		this.setPreferredSize(new Dimension(1500, 400));
		drawingPanel = dp;
		this.add(drawingPanel);
		this.setVisible(true);
	}
	
	AmplitudeScrollPane(Envelope env){
		e = env;
		this.setVisible(true);
	}
	
	public Envelope getEnvelope(){
		return drawingPanel.getEnvelope();
	}
	
	public void setLaunched(){
		launched = true;
		drawingPanel.setLaunched();
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		drawingPanel.paint(g);
	}
}
