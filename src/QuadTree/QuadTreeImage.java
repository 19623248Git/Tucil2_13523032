package QuadTree;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class QuadTreeImage extends ImageProcessing{

        // Error measurement attributes
        // Storing the threshold value of RGB not each color channel
        private double var_thres;
        private double mad_thres;
        private double mpd_thres;
        private double entr_thres;
        private double ssim_thres;

        // Algorithm attributes
        private int mode;
        private int minBlockSize;
        private double compressPercent;
        private Node root;
        private double elapsedTime;

        // Output image
        private BufferedImage img_output;
        private String out_path;

        // Node structure
        public class Node{

                // Data attributes
                private int start_x;
                private int end_x;
                private int start_y;
                private int end_y;
                private int size;
                private double error;
                private Pixel meanPixel;

                // Tree data structure
                // private Node parent; // maybe a quad tree doesn't need a parent node, let's remove it now
                private Node ne, se, sw, nw; // instead of list of childrens, determine 4 childrens

                // default constructor
                public Node(){
                        this.start_x = 0;
                        this.end_x = getWidth()-1;
                        this.start_y = 0;
                        this.end_y = getHeight()-1;
                        this.error = 0;
                        this.size = (this.end_x - this.start_x + 1) * (this.end_y - start_y + 1);
                        this.meanPixel = new Pixel();
                }

                // user defined constructor
                public Node(int start_x, int end_x, int start_y, int end_y){
                        this.start_x = start_x;
                        this.end_x = end_x;
                        this.start_y = start_y;
                        this.end_y = end_y;
                        this.error = 0;
                        this.size = (this.end_x - this.start_x + 1) * (this.end_y - start_y + 1);
                        this.meanPixel = new Pixel();
                }

                // Getter
                public int getStartX() {
                        return start_x;
                }
        
                public int getEndX() {
                        return end_x;
                }
        
                public int getStartY() {
                        return start_y;
                }
        
                public int getEndY() {
                        return end_y;
                }

                public int getSize(){
                        return size;
                }
        
                public double getError() {
                        return error;
                }
        
                public Pixel getMeanPixel() {
                        return meanPixel;
                }

                // Setter
                public void setStartX(int start_x) {
                        this.start_x = start_x;
                }
        
                public void setEndX(int end_x) {
                        this.end_x = end_x;
                }
        
                public void setStartY(int start_y) {
                        this.start_y = start_y;
                }
        
                public void setEndY(int end_y) {
                        this.end_y = end_y;
                }

                public void setSize(int size){
                        this.size = size;
                }
        
                public void setError(double error) {
                        this.error = error;
                }
        
                public void setMeanPixel(Pixel meanPixel) {
                        this.meanPixel = meanPixel;
                }

                // Helper methods
                public boolean isLeaf() {
                        return(ne == null && se == null && sw == null && nw == null);
                }

                // Divide and Conquer
                public void DnC(){
                        
                        this.error = computeError(this);

                        // base case
                        if(this.size <= getMinBlockSize()) return;

                        if(!errorThresholdCheck(this)){
                                // Useful debugging printer
                                // System.out.println("The current block size: " + this.size + " With start_x: " + this.start_x + " end_x: " + this.end_x + " start_y: " + this.start_y + " end_y: " + this.end_y);
                                int midX = (this.end_x + this.start_x)/2;
                                int midY = (this.end_y + this.start_y)/2;
                                this.ne = new Node(midX, this.end_x, this.start_y ,midY);
                                this.se = new Node(midX, this.end_x, midY, this.end_y);
                                this.sw = new Node(this.start_x, midX, midY, this.end_y);
                                this.nw = new Node(this.start_x, midX, this.start_y ,midY);
                                ne.DnC();
                                se.DnC();
                                sw.DnC();
                                nw.DnC();
                        }
                }
        }

        
        
        public QuadTreeImage(){
                super();
                this.var_thres = 150; // 150 - 250
                this.mad_thres = 15; // 8 - 15
                this.mpd_thres = 60; // 40 - 60
                this.entr_thres = 3; // 2 - 3
                this.ssim_thres = 0.92; // 0.85 - 0.92
                this.minBlockSize = 16;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.elapsedTime = 0;
                this.root = new Node();
                this.out_path = "compressed";
                this.img_output = getImage();
        }

        public QuadTreeImage(String absPath) throws IOException{
                super(absPath);
                this.var_thres = 150; // 150 - 250
                this.mad_thres = 15; // 8 - 15
                this.mpd_thres = 60; // 40 - 60
                this.entr_thres = 3; // 2 - 3
                this.ssim_thres = 0.92; // 0.85 - 0.92
                this.minBlockSize = 16;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.elapsedTime = 0;
                this.root = new Node();
                this.out_path = "compressed";
                this.img_output = getImage();
        }

        // Getters
        public double getVarThres() { 
                return var_thres; 
        }
        public double getMadThres() { 
                return mad_thres; 
        }
        public double getMpdThres() { 
                return mpd_thres; 
        }
        public double getEntrThres() { 
                return entr_thres; 
        }
        public double getSsimThres() { 
                return ssim_thres; 
        }
        public int getMode() { 
                return mode; 
        }
        public int getMinBlockSize() { 
                return minBlockSize; 
        }
        public double getCompressPercent() { 
                return compressPercent; 
        }
        public double getElapsedTime(){
                return this.elapsedTime;
        }

        // Setters
        public void setVarThres(double var_thres) { 
                this.var_thres = var_thres; 
        }
        public void setMadThres(double mad_thres) { 
                this.mad_thres = mad_thres; 
        }
        public void setMpdThres(double mpd_thres) { 
                this.mpd_thres = mpd_thres; 
        }
        public void setEntrThres(double entr_thres) { 
                this.entr_thres = entr_thres; 
        }
        public void setSsimThres(double ssim_thres) { 
                this.ssim_thres = ssim_thres; 
        }
        public void setMode(int mode) { 
                this.mode = mode; 
        }
        public void setMinBlockSize(int minBlockSize) { 
                this.minBlockSize = minBlockSize; 
        }
        public void setCompressPercent(double compressPercent) { 
                this.compressPercent = compressPercent; 
        }
        public void setElapsedTime(double elapsedTime){
                this.elapsedTime = elapsedTime;
        }

        // Get mean of pixels between x1 - x2 and y1 - y2
        public Pixel meanPixelRange(int start_x, int end_x, int start_y, int end_y){
                Pixel meanPixel = new Pixel();
                double n = (end_x - start_x + 1) * (end_y - start_y + 1);
                for (int i = start_y; i <= end_y; i++) {
                        for (int j = start_x; j <= end_x; j++){ // (i , j) -> (row, col)
                                meanPixel.add(getPixelValue(j, i));
                        }
                }
                meanPixel.divMean(n);
                return meanPixel;
        }

        public double computeError(Node node){
                node.meanPixel = meanPixelRange(node.start_x, node.end_x, node.start_y, node.end_y);
                return switch (this.mode) {
                        case 0 -> calcVariance(node.start_x, node.end_x, node.start_y, node.end_y, node.meanPixel);
                        case 1 -> calcMad(node.start_x, node.end_x, node.start_y, node.end_y, node.meanPixel);
                        case 2 -> calcMpd(node.start_x, node.end_x, node.start_y, node.end_y);
                        case 3 -> calcEntropy(node.start_x, node.end_x, node.start_y, node.end_y);
                        default -> throw new IllegalArgumentException("Invalid mode: " + this.mode);
                }; 
        }

        // if error is less than threshold return true
        public boolean errorThresholdCheck(Node node){
                return switch (this.mode) {
                        case 0 -> node.error <= this.var_thres;
                        case 1 -> node.error <= this.mad_thres;
                        case 2 -> node.error <= this.mpd_thres;
                        case 3 -> node.error <= this.entr_thres;
                        default -> throw new IllegalArgumentException("Invalid mode: " + this.mode);
                }; 
        }

        public double calcVariance(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel){
                double var_r = 0;
                double var_g = 0;
                double var_b = 0;
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                for(int i = y_start; i <= y_end; i++){
                        for(int j = x_start; j <= x_end; j++){ // (i , j) -> (row, col)
                                var_r+=Math.pow(getPixelValue(j, i).getR()-meanPixel.getR(), 2);
                                var_g+=Math.pow(getPixelValue(j, i).getG()-meanPixel.getG(), 2);
                                var_b+=Math.pow(getPixelValue(j, i).getB()-meanPixel.getB(), 2);
                        }
                }
                var_r/=n;
                var_g/=n;
                var_b/=n;
                return ((var_r + var_g + var_b) / 3);
        }

        public double calcMad(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel) {
                double mad_r = 0;
                double mad_g = 0;
                double mad_b = 0;
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                for (int i = y_start; i <= y_end; i++) {
                        for (int j = x_start; j <= x_end; j++) { // (i , j) -> (row, col)
                                mad_r += Math.abs(getPixelValue(j, i).getR() - meanPixel.getR());
                                mad_g += Math.abs(getPixelValue(j, i).getG() - meanPixel.getG());
                                mad_b += Math.abs(getPixelValue(j, i).getB() - meanPixel.getB());
                        }
                }
                
                mad_r /= n;
                mad_g /= n;
                mad_b /= n;
                return ((mad_r + mad_g + mad_b) / 3);
        }
        
        public double calcMpd(int x_start, int x_end, int y_start, int y_end) {
                int minR = 255, maxR = 0;
                int minG = 255, maxG = 0;
                int minB = 255, maxB = 0;
        
                for (int y = y_start; y <= y_end; y++) {
                        for (int x = x_start; x <= x_end; x++) {
                                Pixel p = getPixelValue(x, y);
                                int r = (int) p.getR();
                                int g = (int) p.getG();
                                int b = (int) p.getB();
                
                                minR = Math.min(minR, r);
                                maxR = Math.max(maxR, r);
                                minG = Math.min(minG, g);
                                maxG = Math.max(maxG, g);
                                minB = Math.min(minB, b);
                                maxB = Math.max(maxB, b);
                        }
                }
        
                double mpdR = maxR - minR;
                double mpdG = maxG - minG;
                double mpdB = maxB - minB;
        
                return (mpdR + mpdG + mpdB) / 3.0;
        }

        public double calcEntropy(int x_start, int x_end, int y_start, int y_end) {
                // create histogram for each color channel
                int[] histR = new int[256];
                int[] histG = new int[256];
                int[] histB = new int[256];
                
                int blockWidth = x_end - x_start + 1;
                int blockHeight = y_end - y_start + 1;
                int totalPixels = blockWidth * blockHeight;
                
                // Build histograms
                for (int y = y_start; y <= y_end; y++) {
                        for (int x = x_start; x <= x_end; x++) {
                                Pixel p = getPixelValue(x, y);
                                histR[(int) p.getR()]++;
                                histG[(int) p.getG()]++;
                                histB[(int) p.getB()]++;
                        }
                }
                
                // Calculate entropy for each channel
                double entropyR = calculateChannelEntropy(histR, totalPixels);
                double entropyG = calculateChannelEntropy(histG, totalPixels);
                double entropyB = calculateChannelEntropy(histB, totalPixels);
                
                return (entropyR + entropyG + entropyB) / 3.0;
        }
            
        private double calculateChannelEntropy(int[] histogram, int totalPixels) {
                double entropy = 0.0;
                for (int i = 0; i < 256; i++) {
                        if (histogram[i] > 0) {
                                double probability = (double) histogram[i] / totalPixels;
                                entropy -= probability * (Math.log(probability) / Math.log(2));
                        }
                }
                return entropy;
        }

        public void reconstructImage(){
                reconstructFromNode(root);
                String extension = getExtension();
                this.out_path += "." + extension;
                try {
                        File outputFile = new File(this.out_path);
                        ImageIO.write(this.img_output, extension, outputFile);
                }
                catch(IOException e){
                        System.out.println(e);
                }
        }

        public void reconstructFromNode(Node node){
                if (node.isLeaf()) {
                        // Fill the region with the mean pixel
                        for (int y = node.start_y; y <= node.end_y; y++) {
                            for (int x = node.start_x; x <= node.end_x; x++) {
                                int r = (int) node.meanPixel.getR();
                                int g = (int) node.meanPixel.getG();
                                int b = (int) node.meanPixel.getB();
                                Color color = new Color(r, g, b);
                                // update output image;
                                this.img_output.setRGB(x, y, color.getRGB());
                            }
                        }
                } else {
                        reconstructFromNode(node.ne);
                        reconstructFromNode(node.se);
                        reconstructFromNode(node.sw);
                        reconstructFromNode(node.nw);
                }
        }

        // Debugging methods to traverse through nodes
        public List<Node> getAllNodes() {
                List<Node> nodes = new ArrayList<>();
                traverseNodes(root, nodes);
                return nodes;
        }

        private void traverseNodes(Node node, List<Node> nodes) {
                if (node == null) return;
                        nodes.add(node);
                if (!node.isLeaf()) {
                        traverseNodes(node.ne, nodes);
                        traverseNodes(node.se, nodes);
                        traverseNodes(node.sw, nodes);
                        traverseNodes(node.nw, nodes);
                }
        }

        public void compress(){
                root.DnC();
        }

        public void applyCompression(){
                double start = System.nanoTime();
                compress();
                double finish = System.nanoTime();
                this.elapsedTime = (finish - start)/1_000_000_000;
                System.out.println("Compression Elapsed Time: " + this.elapsedTime + " seconds");
                reconstructImage();
        }

        public void viewCompressedImage() {
                if (img_output == null) {
                        System.out.println("No compressed image available.");
                        return;
                }

                JFrame frame = new JFrame("Compressed Image");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel label = new JLabel(new ImageIcon(img_output));
                frame.add(new JScrollPane(label));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        }
}
