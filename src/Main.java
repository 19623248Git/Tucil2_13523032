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
        Scanner scanner2 = new Scanner(System.in);
        System.out.print("Enter mode: ");
        int mode = scanner2.nextInt();
        System.out.print("Enter mode error: ");
        int modeError = scanner2.nextInt();
        scanner2.close();
        try {

                // Initialize logger
                FileHandler fh = new FileHandler("node_errors.log");
                fh.setFormatter(new SimpleFormatter());
                logger.addHandler(fh);
                logger.setUseParentHandlers(false);

                QuadTreeImage compressor = new QuadTreeImage(path);
                
                // Display original image
                compressor.viewImage();

                if(mode == 1){
                        // Set compression parameters
                        compressor.setMode(modeError);
                        compressor.setOutPath("compress");
                        // Perform compression
                        compressor.applyCompression();

                        // Log node errors
                        List<QuadTreeImage.Node> nodes = compressor.getAllNodes();

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

                        compressor.viewCompressedImage();
                }

                if (mode == 2){
                        
                        compressor.setMode(modeError);
                        int limit;
                        double increment;
                        if(modeError == 3){
                                limit = 8;
                                increment = 0.1;
                        }
                        else{
                                limit = 255;
                                increment = 1;
                        }

                        for(double i = 0; i <= limit; i+=increment){
                                
                                if(modeError == 0){
                                        compressor.setVarThres(i);
                                }
                                else if(modeError == 1){
                                        compressor.setMadThres(i);
                                }
                                else if(modeError == 2){
                                        compressor.setMpdThres(i);
                                }
                                else{
                                        compressor.setEntrThres(i);
                                }

                                compressor.setOutPath("compress");
                                
                                // Perform compression
                                compressor.applyCompression();
                                
                                // Log node errors
                                List<QuadTreeImage.Node> nodes = compressor.getAllNodes();
                                logger.info("Variance threshold: " + i);
                                logger.info("Compressed percentage: " + compressor.getCompressPercent());
                                logger.info("Total nodes: " + nodes.size());
                                logger.info("-----------------------------------------------------------------------------------");
                        }
                }
                
        } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
        } finally {
                scanner.close();
        }
        return;
    }
}