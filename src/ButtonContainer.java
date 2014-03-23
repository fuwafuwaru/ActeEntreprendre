import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ButtonContainer extends JPanel implements ActionListener {
	private JButton launch;
	private Fenetre parentContainer;
	private JButton more;
	private JButton less;
	public boolean isLaunched = false;
	private Chromagram currentChromagram;
	private SoundPanel associatedPanel;
	private int tempo;
	private JFileChooser choixMusique = new JFileChooser("/home/anis/workspace/acteEntreprendre/sons/");
	private JFileChooser choixChromagram = new JFileChooser("/home/anis/workspace/acteEntreprendre/");
	private JButton sheet = new JButton("sheet");
	private JButton play = new JButton("Jouer le son !");
	private JButton choisirMusique;
	private JButton getTempo;
	private JLabel nomMusique;
	private File file;
	private String cheminMusique;
	private InfoContainer ic;
	private JButton chargerChromagram;
	private SharedResources sharedResources;
	private JSlider zoom;
	
	public ButtonContainer(){	
		this.setSize(280, 600);
		//this.setLayout(new BoxLayout(this, 1));

		this.setLayout(new GridLayout(10, 1, 5, 5));
		this.setBorder(BorderFactory.createTitledBorder("ToolBox"));
		
		zoom = new JSlider();
		zoom.setMaximum(100);
		zoom.setMinimum(0);
		zoom.setValue(50);
		zoom.setPaintTicks(false);
		zoom.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent event){
					associatedPanel.setZoom(((JSlider)event.getSource()).getValue());
				}
			});
		
		choisirMusique = new JButton("Sélectionner la musique");
		TitledBorder choixBorder = BorderFactory.createTitledBorder("Musique");
		choixBorder.setTitleFont(choixBorder.getTitleFont().deriveFont(Font.BOLD ));
		choisirMusique.setBorder(choixBorder);
		
		launch = new JButton("Get Chromagram !");
		TitledBorder launchBorder = BorderFactory.createTitledBorder("Launch");
		launchBorder.setTitleFont(launchBorder.getTitleFont().deriveFont(Font.BOLD ));
		launch.setBorder(launchBorder);
		
		getTempo = new JButton("Get Tempo !");
		TitledBorder tempoBorder = BorderFactory.createTitledBorder("Tempo");
		tempoBorder.setTitleFont(tempoBorder.getTitleFont().deriveFont(Font.BOLD ));
		getTempo.setBorder(tempoBorder);
		
		more = new JButton(">>");
		TitledBorder moreBorder = BorderFactory.createTitledBorder("More");
		moreBorder.setTitleFont(moreBorder.getTitleFont().deriveFont(Font.BOLD ));
		//more.setBorder(moreBorder);
		
		less = new JButton("<<");
		TitledBorder lessBorder = BorderFactory.createTitledBorder("Less");
		lessBorder.setTitleFont(lessBorder.getTitleFont().deriveFont(Font.BOLD ));
		//less.setBorder(lessBorder);
		
		chargerChromagram = new JButton("Charger un chromagram");
		TitledBorder chargerBorder = BorderFactory.createTitledBorder("Charger");
		chargerBorder.setTitleFont(chargerBorder.getTitleFont().deriveFont(Font.BOLD ));
		

	
	    
	    nomMusique = new JLabel("Pas de musique encore sélectionnée");
		
	    choisirMusique.addActionListener(this);
	    chargerChromagram.addActionListener(this);
	    getTempo.addActionListener(this);
	    launch.addActionListener(this);
	    more.addActionListener(this);
	    less.addActionListener(this);
	    sheet.addActionListener(this);
	    play.addActionListener(this);
	    this.add(choisirMusique);
	    this.add(nomMusique);
	    this.add(launch);
	    this.add(chargerChromagram);
	    this.add(play);
	    this.add(sheet);
	    this.add(getTempo);
	    this.add(more);
	    this.add(less);
	    this.add(zoom);
	    this.setVisible(true);
	    this.setSize(90, 50);
	}
	
	public void setTempo(int t){
		tempo = t;
	}
	
	
	public void setInfoContainer(InfoContainer inf){
		ic = inf;
	}
	
	public void setAssociatedPanel(SoundPanel sp){
		associatedPanel = sp;
	}
	
	public void setParentContainer(Fenetre parent){
		parentContainer = parent;
	}
	
	public void setCurrentChromagram(Chromagram chr){
		currentChromagram = chr;
		nomMusique.setText(chr.getName());
		parentContainer.setCurrentChromagram(chr);
	}
	
	public void setSharedResources(SharedResources sh){
		sharedResources = sh;
	}
	
	
	public void more(){
		ic.raiseIndex();
	}
	
	
	public void less(){
		ic.lowerIndex();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == launch){
			if(cheminMusique != null){
				ic.index = 0;
				System.out.println(cheminMusique);
				Chromagram chr = new Chromagram(cheminMusique);
				setCurrentChromagram(chr);
				sharedResources.currentChromagram = chr;
				chr.setPanel(parentContainer.getSoundPanel());
				chr.setAmplitudeScrollPane(parentContainer.getAmplitudeScrollPane());
				parentContainer.getAmplitudeScrollPane().initEnvelope();
				ic.tempoLab.setText(String.valueOf(ic.tempo));
				Thread thread = new Thread(chr);
				Progress prog = new Progress(thread, chr, this);
				try {
					thread.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(isLaunched){
					ic.musicTime.setText(String.valueOf(chr.getMusicTime(ic.index)) + " s");
					ChromaVector cv = currentChromagram.chromagram[ic.index];
					//alledgedChord.setText(String.valueOf(cv.findMaxCorrelation()));	
				}
			}
				
			else{
				new MusiqueDialog(this.parentContainer.getWidth()/2, this.getHeight()/2);
			}
		 }
		
		//Attention la localisation en mesure ne marche que pour du 4/4
		else if(e.getSource() == more){
			more();				
		}
		
		
		else if(e.getSource() == less){
			less();				
		}
		
		else if(e.getSource() == choisirMusique){
			int returnVal = choixMusique.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            file = choixMusique.getSelectedFile();
	            nomMusique.setText(file.getName());
	            cheminMusique = file.getPath();
	        } else {
	        }
	        
		}
		
		else if(e.getSource() == getTempo){
			parentContainer.printBeatFinder();
		}
		
		
		else if(e.getSource() == chargerChromagram){
			int returnVal = choixChromagram.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try {
					FileInputStream f = new FileInputStream(choixChromagram.getSelectedFile());
					ObjectInputStream o = new ObjectInputStream(f);
					setCurrentChromagram((Chromagram) o.readObject());
					sharedResources.currentChromagram = currentChromagram;
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ClassNotFoundException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				
			}
		}
		
		else if(e.getSource() == sheet){
			SheetCreator sc = new SheetCreator(sharedResources);
			sharedResources.sheetCreator = sc;
			
		}
		
		else if(e.getSource() == play){
			SoundPlayer soundPlayer = new SoundPlayer(currentChromagram);
			soundPlayer.setSoundPanel(associatedPanel);
			soundPlayer.setButtonContainer(this);
			soundPlayer.setSharedResources(sharedResources);
			
		}
		
		else if(e.getSource() == parentContainer.ouvrir){
			int returnVal = choixChromagram.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try {
					FileInputStream f = new FileInputStream(choixChromagram.getSelectedFile());
					ObjectInputStream o = new ObjectInputStream(f);
					setCurrentChromagram((Chromagram) o.readObject());
					sharedResources.currentChromagram = currentChromagram;
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ClassNotFoundException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			}
		}
		
		else if(e.getSource() == parentContainer.nouveau){
			int returnVal = choixMusique.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            file = choixMusique.getSelectedFile();
	            nomMusique.setText(file.getName());
	            cheminMusique = file.getPath();
	        } else {
	        }
		}
		
		else if(e.getSource() == parentContainer.enregistrer){
			//TODO : faire l'enregistrement
		}
		
		
		else if(e.getSource() == parentContainer.quitter){
			System.exit(0);
		}
		
	 }
	
	
	
}
