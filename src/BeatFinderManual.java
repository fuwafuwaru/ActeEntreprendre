import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

//Il faudrait rajouter en plus la possibilité de lire la musique en parallèle.

public class BeatFinderManual {
	
	public int tempo; 
	private frame son;
	private ButtonContainer button;
	
	public int getTempo(){
		return tempo;
	}
	
	BeatFinderManual(ButtonContainer but){
		button = but;
		son = new frame();
	}
	
	public void end(){
		son.setVisible(false);
		son.dispose();
	}
	
	class frame extends JFrame{
		frame(){
			panel cont = new panel(this);
			addKeyListener(cont);
			this.setTitle("BeatFinder");
		    this.setSize(600, 300);
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    this.setLocationRelativeTo(null);
		    this.add(cont);
		    this.setVisible(true);
		}
		
	}
	
	class panel extends JPanel implements KeyListener{
		int nombreTemps = 7;
		double[] times = new double[nombreTemps];
		int[] positions = new int[nombreTemps];
		int count = 0;
		boolean isOver = false;
		int x = 20;
		int dh = 2;
		Timer timer;
		JFrame parentContainer;
		
		panel(JFrame parent){
			this.setVisible(true);
			parentContainer = parent;
		}
		
		
		public void launch() throws InterruptedException{
			timer = new Timer(40, taskperformer);
			timer.setInitialDelay(0);
			timer.start(); 
		}
		
		ActionListener taskperformer = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == timer){
					x += dh;
					repaint();
				}
			}
			
		};
		
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			g.fillOval(x, this.getHeight()/2, 15, 15);
			g.setColor(Color.RED);
			for(int a : positions){
				if(a != 0){
					g.fillOval(a, this.getHeight()/2, 15, 15);
				}
			}
			
		}
		
		
		
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			int i = arg0.getKeyCode();
			if(i == KeyEvent.VK_SPACE){
				if(count < nombreTemps){
					times[count] = System.nanoTime()/(Math.pow(10, 9));
					positions[count] = x;
					count++;
					repaint();
				}
				else{
					isOver = true;
					timer.stop();
					tempo = beatfinder();
					System.out.println(tempo);
					button.setTempo(tempo);
					end();
				}
			}
			else if(i == KeyEvent.VK_ENTER){
				try {
					launch();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
		
		public int beatfinder(){
			
			double sum = 0.;
			for(int i = 1; i< times.length; i++){
				double y = times[i] - times[i-1];
				sum += y;
			}
			sum = sum/(times.length - 1);
			return (int) (60/sum);
		}
	}
	
	/*public static void main(String[] args){
		System.out.println("je suis exec");
		BeatFinderManual f = new BeatFinderManual();
	}*/
	
}
