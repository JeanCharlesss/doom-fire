import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


class ColorPalette{
	private static final Color[] intensityColors = {
			new Color(7,7,7),
			new Color(31,7,7),
			new Color(47,15,7),
			new Color(71,15,7),
			new Color(87,23,7),
			new Color(103,31,7),
			new Color(119,31,7),
			new Color(143,39,7),
			new Color(159,47,7),
			new Color(175,63,7),
			new Color(191,71,7),
			new Color(199,71,7),
			new Color(223,79,7),
			new Color(223,87,7),
			new Color(223,87,7),
			new Color(215,96,7),
			new Color(215,95,7),
			new Color(215,103,15),
			new Color(207,111,15),
			new Color(207,119,15),
			new Color(207,127,15),
			new Color(207,135,23),
			new Color(199,135,23),
			new Color(199,143,23),
			new Color(199,151,31),
			new Color(191,159,31),
			new Color(191,159,31),
			new Color(191,167,39),
			new Color(191,167,39),
			new Color(191,175,47),
			new Color(183,175,47),
			new Color(183,183,47),
			new Color(183,183,55),
			new Color(207,207,111),
			new Color(223,223,159),
			new Color(239,239,199),
	};
	
	public static Color getColorByIntensity(int intensity) {
		if(intensity >= 0 && intensity <=55) {
			return intensityColors[intensity];
		}
		return Color.black;
	}
}
public class DoomFire extends JPanel implements Runnable{
	final int size = 512;
	
	boolean isRunning;
	Thread thread; //fio de execução
	BufferedImage view;
	
	int pixelSize = 8;
	int[][] structFire;
	int fireWidth = 64, fireHeight = 64;
	
	
	
	public DoomFire() {
		setPreferredSize(new Dimension(size,size)); //dimensão da tela
	}
	//criando a tela extendida do JPanel
	public static void main (String[]args) {
		JFrame window = new JFrame("Doom Fire");
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(new DoomFire());
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	//iniciando o fio de execução
	@Override
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			isRunning = true;
			thread.start();
		}
	}
	
	public void start() {
		view = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
		structFire = new int[fireWidth][fireHeight];
		for(int row = 0; row < fireWidth; row++) {
			for(int column = 0; column < fireHeight; column++) {
				if(row + 1 < fireWidth) {
					structFire[row][column] = 0;
				}else {
					structFire[row][column] = 35;
				}
			}
		}
	}
	public void update() {
		for(int row = 0; row < fireWidth; row++) {
			for(int column = 0; column < fireHeight; column++) {
				if(column + 1<fireHeight) {
					int decay = new Random().nextInt(3);
					int intensityBelow = Math.max(structFire[column + 1][row] - decay,0);
					if(row - decay >= 0) {
						structFire[column][row - decay] = intensityBelow;
					}else if(column - 1 >= 0){
						structFire[column][fireHeight - decay] = intensityBelow;
					}else{
						structFire[column][row] = intensityBelow;
					}
				}
			}
		}
	}
	public void draw() {
		Graphics2D g2 = (Graphics2D) view.getGraphics();
		for(int row = 0; row < fireWidth; row++) {
			for (int column = 0; column < fireHeight; column++) {
				g2.setColor(ColorPalette.getColorByIntensity(structFire[row][column]));
				g2.fillRect(column * pixelSize, row * pixelSize, pixelSize, pixelSize);
			}
		}
		
		Graphics g = getGraphics();
		g.drawImage(view, 0, 0, size, size,null);
		g.dispose();
	}
		
	@Override
	public void run() {
		try {
			start();
			while(isRunning == true) {
				update();
				draw();
				Thread.sleep(1000/60);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
