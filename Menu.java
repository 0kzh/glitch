import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Menu extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	BufferedImage logo = ImageIO.read(new File("logo.png"));
	JLabel[] buttons = {new JLabel("Play"), new JLabel("Options"), new JLabel("Exit")};
	int currentSelection = 0;
	public Menu() throws IOException{
		super("Ninjump");
		File f = new File("pointer.gif");
        Icon icon = new ImageIcon(f.toURI().toURL());
		JPanel menu = new JPanel(new BorderLayout());
		JPanel selection = new JPanel();
		JPanel copyright = new JPanel();
		JPanel border = new JPanel();
		border.setBorder(new EmptyBorder(0,0,0,325));
		border.setBackground(Color.BLACK);
		menu.setBackground(Color.BLACK);
		selection.setBackground(Color.BLACK);
		selection.setLayout(new BoxLayout(selection, BoxLayout.Y_AXIS));
		copyright.setLayout(new BoxLayout(copyright, BoxLayout.Y_AXIS));
		selection.add(Box.createRigidArea(new Dimension(0,100)));
		selection.add(Box.createVerticalGlue());
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].setAlignmentX(JLabel.RIGHT_ALIGNMENT);
			buttons[i].setFont(new Font("Arial", Font.PLAIN, 24));
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setIconTextGap(20);
			selection.add(buttons[i]);
			selection.add(Box.createRigidArea(new Dimension(0,20)));
		}
		selection.add(Box.createVerticalGlue());
		
		copyright.setBackground(Color.BLACK);
		JLabel c1 = new JLabel("2015-2016 Kelvin Zhang");
		JLabel c2 = new JLabel("Version 1.0");
		c1.setAlignmentX(Component.CENTER_ALIGNMENT);
		c2.setAlignmentX(Component.CENTER_ALIGNMENT);
		c1.setFont(new Font("Arial", Font.PLAIN, 14));
		c2.setFont(new Font("Arial", Font.PLAIN, 14));
		c1.setForeground(Color.WHITE);
		c2.setForeground(Color.WHITE);
		copyright.add(c1);
		copyright.add(c2);
		copyright.add(Box.createRigidArea(new Dimension(0,20)));
		menu.add(border, BorderLayout.EAST);
		menu.add(selection, BorderLayout.CENTER);
		menu.add(copyright, BorderLayout.SOUTH);
		super.setContentPane(menu);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.getContentPane().setPreferredSize(new Dimension(740, 480));
		buttons[0].setForeground(Color.LIGHT_GRAY);
		buttons[0].setIcon(icon);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				
				if(e.getKeyCode() == KeyEvent.VK_UP){
					if(currentSelection > 0){
						currentSelection--;
					}else{
						currentSelection = 2;
					}
		             
		        }
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					if(currentSelection < 2){
						currentSelection++;
					}else{
						currentSelection = 0;
					}
		        }
				
				for(int i = 0; i < buttons.length; i++){
					if(i != currentSelection){
						buttons[i].setForeground(Color.WHITE);
						buttons[i].setIcon(null);
					}else{
						buttons[i].setForeground(Color.LIGHT_GRAY);
						buttons[i].setIcon(icon);
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(logo, (this.getBounds().width / 2) - (logo.getWidth() / 2), 80, null);
	}
	public void actionPerformed(ActionEvent e)
    {
		
    }
	
	public static void main(String[] args) throws IOException{
		Menu self = new Menu();
		self.pack();
		self.setVisible(true);
		self.setResizable(false);
	}

}
