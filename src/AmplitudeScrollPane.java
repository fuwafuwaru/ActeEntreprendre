import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;


public class AmplitudeScrollPane extends JPanel {
	
	private Envelope e;
	private boolean loaded = false;
	private boolean launched = false;
	private DrawingPanel drawingPanel;
	private SharedResources sharedResources;
	
	AmplitudeScrollPane(){
		this.setSize(2000, 100);
		this.setLayout(new BorderLayout());
		drawingPanel = new DrawingPanel(new Envelope());
		JScrollPane jsp = new JScrollPane(drawingPanel);
		jsp.setPreferredSize(new Dimension(2000, 120));
		this.add(jsp, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	AmplitudeScrollPane(Envelope env){
		e = env;
		this.setVisible(true);
	}
	
	public Envelope getEnvelope(){
		return drawingPanel.getEnvelope();
	}
	
	public void setLoaded(){
		loaded = true;
		drawingPanel.setLoaded();
	}
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	public void setLaunched(){
		launched = true;
		drawingPanel.setLaunched();
	}
	
	
	public DrawingPanel getDrawingPanel(){
		return drawingPanel;
	}
	
	public void setDrawingPanel(DrawingPanel d){
		drawingPanel = d;
	}
	
	public void initEnvelope(){
		e = new Envelope();
	}
	
	public void paint(Graphics g){
		drawingPanel.paint(g);
		super.paint(g);
	}
}
