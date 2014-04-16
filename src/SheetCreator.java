import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SheetCreator {
	private SharedResources sharedResources;
	private SheetCreatorFrame shFrame;
	private SheetCreatorPanel shPanel;
	private JFileChooser fileBrowser;
	private JLabel direction = new JLabel("Rentrez le nom de la partition\n ou laissez le nom actuel");
	private JTextField outputName;
	private JButton generate = new JButton("Generate sheet !");
	private Slide slide;
	private JLabel direction2 = new JLabel("Choisissez la densité de note");
	private JTextField tempo = new JTextField("");
	
	
	SheetCreator(SharedResources sh){
		sharedResources = sh; //Cette ligne doit obligatoirement précéder la création de la fenêtre

		shFrame = new SheetCreatorFrame();

	}
	
	
	public void setVisible(boolean b){
		shFrame.setVisible(b);
	}
	
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	
	public SheetCreatorFrame getFrame() {
		return shFrame;
	}
	
	public SheetCreatorPanel getPanel() {
		return shPanel;
	}
	
	
	
	class SheetCreatorFrame extends JFrame {
		
		SheetCreatorFrame(){
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(300, 600);
			shPanel = new SheetCreatorPanel();
			setContentPane(shPanel);
			setVisible(true);
		}
		
	}
	
	
	class SheetCreatorPanel extends JPanel implements ActionListener {
		
		
		SheetCreatorPanel(){
			setLayout(new GridLayout(5, 1, 10, 10));
			this.setBorder(new TitledBorder("Création de la partition"));
			outputName = new JTextField(sharedResources.currentChromagram.getName());
			generate.addActionListener(this);
			slide = new Slide();
			tempo.setText("" + sharedResources.infoContainer.tempo);
			add(direction);
			add(outputName);
			add(direction2);
			add(slide);
			add(tempo);
			add(generate);
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource() == generate){
				
				sharedResources.currentChromagram.generateSheetMusic(outputName.getText(), Integer.parseInt(tempo.getText()));
				sharedResources.sheetCreator.setVisible(false);
			}
			
		}
	}
	
	
	

	public class Slide extends JSlider{
		public Slide(){
			setMaximum(100);
			setMinimum(0);
			setValue(50);
			setPaintTicks(true);
			setPaintLabels(true);
			setMinorTickSpacing(1);
			setMajorTickSpacing(10);
		}

	}

	
}
