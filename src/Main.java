import QuadTree.QuadTreeImage;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter image path: ");
        String path = scanner.nextLine();

        try {
            // Initialize logger
            FileHandler fh = new FileHandler("node_errors.log");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);

            QuadTreeImage compressor = new QuadTreeImage(path);
            
            // Set compression parameters
            compressor.setMode(0);
            compressor.setVarThres(50);
            
            // Perform compression
            compressor.applyCompression();
            
            // Log node errors
            List<QuadTreeImage.Node> nodes = compressor.getAllNodes();
            logger.info("Total nodes: " + nodes.size());
            
            for (QuadTreeImage.Node node : nodes) {
                logger.info(String.format(
                    "Node [%d-%d][%d-%d] Error: %.2f | Size: %d | Leaf: %s",
                    node.getStartX(), node.getEndX(),
                    node.getStartY(), node.getEndY(),
                    node.getError(),
                    node.getSize(),
                    node.isLeaf()
                ));
            }

            // Display images
            System.out.println("\nOriginal Image:");
            compressor.viewImage();
            System.out.println("\nCompressed Image:");
            compressor.viewCompressedImage();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}