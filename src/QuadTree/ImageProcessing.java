package QuadTree;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ImageProcessing {
	
	private BufferedImage image;
	private int width;
	private int height;
	private String absPath;

	// Default constructor
	public ImageProcessing() {
		this.image = null;
		this.absPath = " ";
		this.width = 0;
		this.height = 0;
	}

	// User-defined constructor with absPath
	public ImageProcessing(String absPath) throws IOException {
		this.absPath = absPath;
		this.image = ImageIO.read(new File(absPath));
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
	}

	// Loads image not from constructor
	public void loadImage(String absPath) throws IOException {
		this.absPath = absPath;
		this.image = ImageIO.read(new File(absPath));
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
	}

	// Method for user input absolute path
	public void inputAbsPath(){
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter the absolute path of the image: ");
			this.absPath = scanner.nextLine();
		}

		try{
			loadImage(this.absPath);
			System.out.println("Image loaded succesfully.");
		}
		catch(IOException e){
			System.out.println("Error loading image. Check the file path.");
		}
	}

	// Returns width attribute
	public int getWidth(){
		return this.width;
	}

	// Returns height attribute
	public int getHeight(){
		return this.height;
	}

	// Returns true if image is null
	public Boolean isImageEmpty(){
		return this.image == null;
	}

	// Returns a Pixel object at (x,y) coordinate
	public Pixel getPixelValue(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			throw new IllegalArgumentException("Coordinates out of bounds.");
		}
		int pixel = image.getRGB(x, y);
		Color color = new Color(pixel, true); // Extract color components
		return new Pixel(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	// Image viewer with JFrame
	public void viewImage() {
		if (isImageEmpty()) {
			System.out.println("No image loaded.");
			return;
		}

		JFrame frame = new JFrame("Image Viewer");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(this.width, this.height);

		JLabel label = new JLabel(new ImageIcon(this.image));
		frame.add(new JScrollPane(label));

		frame.pack(); // Adjusts the frame size based on image size
		frame.setLocationRelativeTo(null); // Centers the frame
		frame.setVisible(true);
	}
}

