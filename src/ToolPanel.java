import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;




public class ToolPanel extends JPanel implements ActionListener, commonButtons{
	
		public Slide slide;
		private SoundPlayer soundPlayer;
		private SharedResources sharedResources;
		private JButton play = new JButton();
		private JButton pause = new JButton();
		private JButton stop = new JButton();
		private double t = 0; //Nombre de fois où le timer est appelé correspond à la frame dans le spectre
		private ButtonContainer buttonContainer;
		private JLabel time = new JLabel("0:00");
		private Chromagram currentChromagram;
		private SoundPanel soundPanel;



		
		ToolPanel(SoundPlayer sp){
			setSize(500, 120);
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			this.setAlignmentY(CENTER_ALIGNMENT);
			JPanel firstContainer = new JPanel();
			JPanel secondContainer = new JPanel();
			slide = new Slide();
			firstContainer.setLayout(new GridLayout(1, 3, 10, 10));
			//Image img = ImageIO.read(getClass().getResource("/home/anis/workspace/acteEntreprendre/player_play.png"));
			ImageIcon icPlay = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_play.png");
			play.setIcon(icPlay);
			ImageIcon icPause = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_pause.png");
			pause.setIcon(icPause);
			ImageIcon icStop = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_stop.png");
			stop.setIcon(icStop);
			ImageIcon sheetIcon = new ImageIcon("/home/anis/workspace/acteEntreprendre/sheetIcon.png");
			sheet.setIcon(sheetIcon);
			
			
			play.setBorder(BorderFactory.createEmptyBorder());
			play.setContentAreaFilled(false);
			pause.setBorder(BorderFactory.createEmptyBorder());
			pause.setContentAreaFilled(false);
			stop.setBorder(BorderFactory.createEmptyBorder());
			stop.setContentAreaFilled(false);
			

			play.addActionListener(this);
			pause.addActionListener(this);
			stop.addActionListener(this);
			
			firstContainer.setLayout(new BoxLayout(firstContainer, BoxLayout.LINE_AXIS));
			firstContainer.setAlignmentX(CENTER_ALIGNMENT);
			firstContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			firstContainer.add(play);
			firstContainer.add(pause);
			firstContainer.add(stop);
			firstContainer.add(piano);
			firstContainer.add(guitare);
			firstContainer.add(sheet);

			secondContainer.add(slide);
			secondContainer.add(time);
			secondContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

			add(secondContainer, BorderLayout.EAST);
			add(firstContainer, BorderLayout.WEST);
			
			
		}
		
		
		
		ToolPanel(){
			setSize(500, 120);
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			this.setAlignmentY(CENTER_ALIGNMENT);
			JPanel firstContainer = new JPanel();
			JPanel secondContainer = new JPanel();
			JPanel thirdContainer = new JPanel();
			slide = new Slide();
			firstContainer.setLayout(new GridLayout(1, 3, 10, 10));
			//Image img = ImageIO.read(getClass().getResource("/home/anis/workspace/acteEntreprendre/player_play.png"));
			ImageIcon icPlay = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_play.png");
			play.setIcon(icPlay);
			ImageIcon icPause = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_pause.png");
			pause.setIcon(icPause);
			ImageIcon icStop = new ImageIcon("/home/anis/workspace/acteEntreprendre/player_stop.png");
			stop.setIcon(icStop);

			
			play.setBorder(BorderFactory.createEmptyBorder());
			play.setContentAreaFilled(false);
			pause.setBorder(BorderFactory.createEmptyBorder());
			pause.setContentAreaFilled(false);
			stop.setBorder(BorderFactory.createEmptyBorder());
			stop.setContentAreaFilled(false);
			

			play.addActionListener(this);
			pause.addActionListener(this);
			stop.addActionListener(this);
			
			firstContainer.setLayout(new BoxLayout(firstContainer, BoxLayout.LINE_AXIS));
			firstContainer.setAlignmentX(CENTER_ALIGNMENT);
			firstContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			firstContainer.add(play);
			firstContainer.add(pause);
			firstContainer.add(stop);
			
			thirdContainer.add(piano);
			thirdContainer.add(guitare);
			thirdContainer.add(sheet);
			thirdContainer.add(beat);
			
			sheet.addActionListener(buttonContainer);
		    play.addActionListener(buttonContainer);
		    beat.addActionListener(buttonContainer);
		    piano.addActionListener(buttonContainer);

			
			secondContainer.add(slide);
			secondContainer.add(time);
			secondContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			add(firstContainer, BorderLayout.EAST);
			add(secondContainer, BorderLayout.CENTER);
			add(thirdContainer, BorderLayout.WEST);

			
		}
		
		
		
		public void setSoundPlayer(SoundPlayer sp){
			
			soundPlayer = sp;
			slide.setMaximum((int) soundPlayer.musicLength);
			System.out.println("Le soundPlayer du toolPanel a bien été actualisé");
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getSource() == play){
				soundPlayer.play();
			}
			else if(arg0.getSource() == pause){
				soundPlayer.pause();
			}
			
			else if(arg0.getSource() == stop){
				soundPlayer.stop();
				slide.setValue(0);
				t=0;
			}

			else if(arg0.getSource() == soundPlayer.timer){
				if(currentChromagram == null){
					System.out.println("currentChromagram null");
				}
				if(t < currentChromagram.spectrum.length){
					soundPanel.setGraph(currentChromagram.spectrum[(int) t]);
					if(sharedResources.drawingPanel == null){
						System.out.println("Erreur dans toolPanel drawingPanel est null");
					}
					sharedResources.drawingPanel.setCursor(((double)t)/currentChromagram.spectrum.length);
					slide.setValue((int) (t*soundPlayer.timerPeriod/1000) + 1);
					t += 1;
					buttonContainer.more();
				}
				
			}
			
		}
		
		
		
		
		public void setSharedResources(SharedResources sh){
			System.out.println("SharedResources mis a jour dans toolPanel");
			sharedResources = sh;
			currentChromagram = sharedResources.currentChromagram;
			soundPanel = sharedResources.soundPanel;
			buttonContainer = sharedResources.buttonContainer;
			
		}
		
		
		
		
		public class Slide extends JSlider{
			public Slide(){
				this.setPreferredSize(new Dimension(400, 20));
				if(soundPlayer != null){
					setMaximum((int) soundPlayer.musicLength);
				}
				else{
					setMaximum(100);
				}
				setMinimum(0);
				setValue(0);
				addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent event){
						int min = ((JSlider)event.getSource()).getValue()/60;
						int sec = ((JSlider)event.getSource()).getValue()%60;
						if(sec < 10){
							time.setText(min + ":0" + sec + "/" + ((int) (soundPlayer.musicLength/60)) + ":" + ((int) (soundPlayer.musicLength))%60);
						}
						else{
							time.setText(min + ":" + sec + "/" + ((int) (soundPlayer.musicLength/60)) + ":" + ((int) (soundPlayer.musicLength))%60);
						}
					}
				});
			}

		}

		
	}

	