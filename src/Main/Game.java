package Main;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Game {

	public static void main(String[] args){
		
		JFrame window = new JFrame("GLITCH");
		window.setSize(640, 480);
		window.setIconImage(Toolkit.getDefaultToolkit().getImage("glitch.png"));
		window.setLocationRelativeTo(null);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}
}
