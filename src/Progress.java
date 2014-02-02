import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Progress extends JFrame{
	private Thread t;
	private JProgressBar bar;
	private JButton launch;
	private ButtonContainer bc;
	public Progress(Thread thread, Chromagram chr, ButtonContainer butcon){
		this.setSize(300, 80);
<<<<<<< HEAD
		this.setTitle("*** Génération du chromagram ***");
=======
		this.setTitle("*** Progression ***");
>>>>>>> a86d86a99729cf547d7faf3628c0e52733ef4015
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		bc = butcon;
		t = thread;
		bar = new JProgressBar();
		bar.setMaximum(500);
		bar.setMinimum(0);
		bar.setStringPainted(true);
		chr.setBar(bar);
		this.getContentPane().add(bar, BorderLayout.CENTER);
		launch = new JButton("Lancer");
		launch.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
			t.start();
			bc.isLaunched = true;
			}
		});
		this.getContentPane().add(launch, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	

	public JProgressBar getBar(){
		return this.bar;
	}
<<<<<<< HEAD
	
	public void end(){
		System.out.println("fermeture de la fenêtre de chargement");
		this.dispose();
	}
=======
>>>>>>> a86d86a99729cf547d7faf3628c0e52733ef4015
}
