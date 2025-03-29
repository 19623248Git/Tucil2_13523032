import QuadTree.ImageProcessing;
import QuadTree.Pixel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
	try (Scanner scanner = new Scanner(System.in)) {
	    ImageProcessing imgProc = new ImageProcessing();

	    Map<Integer, Consumer<Scanner>> menuActions = new HashMap<>();

	    menuActions.put(1, (sc) -> {
		System.out.print("Enter the absolute path of the image: ");
		String path = sc.nextLine();
		try {
		    imgProc.loadImage(path);
		    System.out.println("Image loaded successfully.");
		} catch (IOException e) {
		    System.out.println("Error loading image. Check the file path.");
		}
	    });

	    menuActions.put(2, (sc) -> {
		if (imgProc.isImageEmpty()) {
		    System.out.println("No image loaded. Load an image first.");
		    return;
		}
		System.out.print("Enter x coordinate: ");
		int x = sc.nextInt();
		System.out.print("Enter y coordinate: ");
		int y = sc.nextInt();

		try {
		    Pixel pixel = imgProc.getPixelValue(x, y);
		    System.out.println("Pixel RGB at (" + x + ", " + y + "): R=" + pixel.getR() + ", G=" + pixel.getG() + ", B=" + pixel.getB());
		} catch (IllegalArgumentException e) {
		    System.out.println("Error: " + e.getMessage());
		}
	    });

	    menuActions.put(3, (sc) -> 
		System.out.println("Image Dimensions: " + imgProc.getWidth() + "x" + imgProc.getHeight())
	    );

	    menuActions.put(4, (sc) -> {
		if (imgProc.isImageEmpty()) {
		    System.out.println("No image loaded. Load an image first.");
		    return;
		}
		imgProc.viewImage();
	    });

	    menuActions.put(5, (sc) -> {
		System.out.println("Exiting...");
		System.exit(0);
	    });

	    while (true) {
		System.out.println("\n===== Image Processing Menu =====");
		System.out.println("1. Load Image");
		System.out.println("2. Get Pixel RGB Value");
		System.out.println("3. Show Image Dimensions");
		System.out.println("4. View Image");
		System.out.println("5. Exit");
		System.out.print("Enter your choice: ");

		if (!scanner.hasNextInt()) {
		    System.out.println("Invalid input. Please enter a number.");
		    scanner.nextLine();
		    continue;
		}

		int choice = scanner.nextInt();
		scanner.nextLine();

		menuActions.getOrDefault(choice, (sc) -> 
		    System.out.println("Invalid choice. Please try again.")
		).accept(scanner);
	    }
	}
    }
}
