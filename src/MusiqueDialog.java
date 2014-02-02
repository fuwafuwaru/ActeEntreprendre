import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;


public class MusiqueDialog extends JDialog implements ActionListener{
	private JButton fermer;
	MusiqueDialog(int x, int y){
		this.setSize(300, 150);
		JLabel message = new JLabel("Veuillez sélectionner une musique");
		message.setFont(new Font("Verdana", Font.BOLD, 13));
		fermer = new JButton("Fermer");
		fermer.addActionListener(this);
		fermer.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.add(message, BorderLayout.NORTH);
		this.add(fermer, BorderLayout.SOUTH);
		this.setLocation(x, y);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == fermer){
			this.dispose();
		}
		
	}
}
