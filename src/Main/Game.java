package Main;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

//import com.apple.eawt.Application;

public class Game {

	public static void main(String[] args){
		
		JFrame window = new JFrame("GLITCH");
		window.setSize(640, 480);
		window.setLocationRelativeTo(null);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
		String OS = System.getProperty("os.name").toLowerCase();
		try {
			window.setIconImage(ImageIO.read(Game.class.getResourceAsStream("/Misc/glitch.png")));
			if(OS.indexOf("mac") >= 0){
				//set dock icon
//				Application.getApplication().setDockIconImage(ImageIO.read(Game.class.getResourceAsStream("/Misc/glitch.png")));
				//disable long press accent menu while in game
				Process p = Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
