import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FullFrameSpectrum extends JFrame {
	private SharedResources sharedResources;
	private FullSoundPanel soundPanel;
	public double[] graph;
	public int[] localMaximums = {0};

	
	FullFrameSpectrum(SoundPanel sp){
		this.setSize(1300, 1000);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Spectrum");

		setContentPane(new FullSoundPanel());
		
		this.setVisible(true);
		
	}
	
	
	public void setSharedResources(SharedResources sr){
		sharedResources = sr;
	}
	
	
	
	class FullSoundPanel extends JPanel{
		
		
		
		public FullSoundPanel(){
			this.setSize(getMaximumSize());
			double[] temp = new double[1000];
			for(int k = 0; k < temp.length; k++){
				temp[k] = k;
			}
			this.setGraph(temp);
			this.setVisible(true);
		}
		
		
		
		public void paint(Graphics g){
			super.paint(g);
			this.setBackground(new Color(34, 142, 200));
			try{
				int h = this.getHeight();
				int w = this.getWidth();
				int marginx = 50;
				int marginy = 50;
				h = h - marginy;
				g.setColor(Color.WHITE);
				g.fillRect(marginx, 0, w, h);
				g.setColor(Color.BLACK);
				for(int k=0; k < graph.length; k++){
					g.setColor(new Color(35, 142, 200));
					g.fillRect(5*k + marginx,h-((int) (graph[k])), 2,((int) (graph[k])));
				}
				for(int k : localMaximums){//On colorie en rouge les maximums
					g.setColor(Color.red);
					g.fillRect(5*k + marginx,h-((int) (graph[k])), 2,((int) (graph[k])));
				}
				g.setColor(Color.black);
				int offset = 10;
				for(int i = 53; i < 105; i++){
					double x = Pitch.convertFreq(i);
					int u =  (int) (x/5.38); //2048/11025
					g.fillRect(5*u + marginx, 100, 1, h-100);
					g.drawString((new Pitch(i)).getChroma().toString(), 5*u + marginx, 60+offset);
					offset = -offset;
				}
			}catch(NullPointerException e){
				System.out.println("Attention NullPointerException");
			}
		}
		
		public void setGraph(double[] b){
			graph = b;
			if(sharedResources != null){
				if(sharedResources.currentChromagram != null){
					localMaximums = ProcessingTools.findLocalMax(graph, (int) (Pitch.convertFreq(sharedResources.currentChromagram.getMidiMin())/sharedResources.currentChromagram.getSpectralResolution()), (int) (Pitch.convertFreq(sharedResources.currentChromagram.getMidiMin())/sharedResources.currentChromagram.getSpectralResolution()));
				}
				repaint();
				if(sharedResources.ffs != null){
					sharedResources.ffs.repaint();
				}
			}	
			
		}
		
	}
	
	
	
}

















