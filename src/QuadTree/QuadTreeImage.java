package QuadTree;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

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
        private double compressedSize;

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
                private Node ne, se, sw, nw;

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
                this.var_thres = 44;
                this.mad_thres = 5;
                this.mpd_thres = 35;
                this.entr_thres = 2;
                this.ssim_thres = 0.92; // 0.85 - 0.92
                this.minBlockSize = 16;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.elapsedTime = 0;
                this.root = new Node();
                this.out_path = "compressed";
                this.img_output = getImage();
                this.compressedSize = 0;
        }

        public QuadTreeImage(String absPath) throws IOException{
                super(absPath);
                this.var_thres = 44;
                this.mad_thres = 5;
                this.mpd_thres = 35;
                this.entr_thres = 2;
                this.ssim_thres = 0.92; // 0.85 - 0.92
                this.minBlockSize = 16;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.elapsedTime = 0;
                this.root = new Node();
                this.out_path = "compressed";
                this.img_output = getImage();
                this.compressedSize = 0;
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

        public String getOutPath(){
                return this.out_path;
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

        public void setOutPath(String out_path){
                this.out_path = out_path;
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
                        case 4 -> calcSsim(node.start_x, node.end_x, node.start_y, node.end_y, node.meanPixel);
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
                        case 4 -> node.error <= this.ssim_thres;
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

        public double calcSsim(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel){

                double n = (x_end - x_start + 1) * (y_end - y_start + 1);

                double sumR = 0;
                double sumG = 0;
                double sumB = 0;
                double sumR2 = 0;
                double sumG2 = 0;
                double sumB2 = 0;
                double sumRMean = 0;
                double sumGMean = 0;
                double sumBMean = 0;

                for(int y = y_start; y < y_end; y++){
                        for(int x = x_start; x < x_end; x++){

                                double r_val = getPixelValue(x, y).getR();
                                double g_val = getPixelValue(x, y).getG();
                                double b_val = getPixelValue(x, y).getB();
                                 
                                sumR += r_val;
                                sumG += g_val;
                                sumB += b_val;

                                sumR2 += (r_val * r_val);
                                sumG2 += (g_val * g_val);
                                sumB2 += (b_val * b_val);

                                sumRMean += (r_val * meanPixel.getR());
                                sumGMean += (r_val * meanPixel.getG());
                                sumBMean += (r_val * meanPixel.getB());

                        }
                }

                double meanR = sumR / n;
                double meanG = sumG / n;
                double meanB = sumB / n;

                double varR  = (sumR2 / n) - (meanR * meanR);
                double varG  = (sumG2 / n) - (meanG * meanG);
                double varB  = (sumB2 / n) - (meanB * meanB);

                // omit covariance because we are comparing the same thing
                // double covR = (sumRMean / n)  - (meanR * meanPixel.getR());
                // double covG = (sumGMean / n)  - (meanG * meanPixel.getG());
                // double covB = (sumBMean / n)  - (meanB * meanPixel.getB());
                double covR = 0;
                double covG = 0;
                double covB = 0;

                double L_val =  255;
                double k1 = 0.01;
                double k2 = 0.03;

                double c1 = (k1*L_val) * (k1*L_val);
                double c2 = (k2*L_val) * (k2*L_val);

                double ssimR = ((2 * meanR * meanPixel.getR() + c1)*((2*covR) + c2)) / (((meanR * meanR) + (meanPixel.getR() * meanPixel.getR()) + c1) * (varR +c2));
                double ssimG = ((2 * meanG * meanPixel.getG() + c1)*((2*covG) + c2)) / (((meanG * meanG) + (meanPixel.getG() * meanPixel.getG()) + c1) * (varG +c2));
                double ssimB = ((2 * meanB * meanPixel.getB() + c1)*((2*covB) + c2)) / (((meanB * meanB) + (meanPixel.getB() * meanPixel.getB()) + c1) * (varB +c2));

                return 1 - ((0.299 * ssimR) + (0.587*ssimG) + (0.114*ssimB));

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

        public void findPercentage(double expectedPercentage, double tolerance, int max_iter){
                double max, min, start;
                Boolean solution_found = false;
                if(this.mode >= 0 && this.mode <= 2){
                        min = 0;
                        max = 225;
                        start = (max + min) / 2;
                        switch (this.mode) {
                                case 0: 
                                        this.var_thres = start;
                                        break;
                                case 1: 
                                        this.mad_thres = start;
                                        break;
                                case 2: 
                                        this.mpd_thres = start;
                                        break;
                            default:
                                this.var_thres = start;
                                break;
                        }
                }
                else if(this.mode == 3){
                        min = 2;
                        max = 8;
                        start = (max + min) / 2;
                        this.entr_thres = start;
                }
                else{
                        // ssim
                        min = 0;
                        max = 1;
                        start = (max + min) / 2;
                        this.ssim_thres = start;
                }

                this.compressPercent = 1000;
                
                boolean noLogResults = false;

                while(!solution_found && max_iter > 0){
                        applyCompression(noLogResults);
                        if(this.compressPercent > expectedPercentage + tolerance){
                                switch (this.mode) {
                                        case 0 -> this.var_thres /= 2;
                                        case 1 -> this.mad_thres /= 2;
                                        case 2 -> this.mpd_thres /= 2;
                                        case 3 -> this.entr_thres /= 2;
                                        case 4 -> this.ssim_thres /= 2;
                                        default -> this.var_thres /= 2;
                                }
                        }
                        else if(this.compressPercent < expectedPercentage - tolerance){
                                switch (this.mode) {
                                        case 0 -> this.var_thres += (this.var_thres/2);
                                        case 1 -> this.mad_thres += (this.mad_thres/2);
                                        case 2 -> this.mpd_thres += (this.mpd_thres/2);
                                        case 3 -> this.entr_thres += (this.entr_thres/2);
                                        case 4 -> this.ssim_thres += (this.ssim_thres/2);
                                        default -> this.var_thres += (this.var_thres/2);
                                }
                        }
                        else if(this.compressPercent > (expectedPercentage - tolerance) && this.compressPercent < (expectedPercentage + tolerance)){
                                solution_found = true;
                        }
                        max_iter-=1;
                }
                applyCompression(!noLogResults);
        }

        public void reconstructImage(boolean logResults){
                reconstructFromNode(root);
                List<Node> treeNodes = getAllNodes();
                int nodeCount = treeNodes.size();
                int treeDepth = getTreeDepth();
                String extension = getExtension();
                String outPath = this.out_path + "." + extension;
                try {
                        File outputFile = new File(outPath);
                        ImageIO.write(this.img_output, extension, outputFile);
                        this.compressedSize = outputFile.length();
                        this.compressPercent = 100 - ((this.compressedSize / getFileSize()) * 100); // this is in percentage
                        if(logResults){
                                System.out.println("The original size: " + getFileSize() + " bytes");
                                System.out.println("The compressed size: " + this.compressedSize + " bytes");
                                System.out.println("The compression percentage: " + this.compressPercent + "%");
                                System.out.println("Total node count of Quadtree: " + nodeCount);
                                System.out.println("Quadtree depth: " + treeDepth);
                        }
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

        public int getTreeDepth() {
                return computeDepth(root);
        }
            
        private int computeDepth(Node node) {
                if (node == null || node.isLeaf()) {
                        return 1; // a leaf or null counts as depth 1 from its own level
                }
                int ne = computeDepth(node.ne);
                int se = computeDepth(node.se);
                int sw = computeDepth(node.sw);
                int nw = computeDepth(node.nw);
                return 1 + Math.max(Math.max(ne, se), Math.max(sw, nw));
        }
            

        public void compress(){
                root.DnC();
        }

        public void applyCompression(boolean logResults){
                try{
                        this.img_output = ImageIO.read(new File(getAbsPath()));
                        double start = System.nanoTime();
                        compress();
                        double finish = System.nanoTime();
                        this.elapsedTime = (finish - start)/1_000_000_000;
                        System.out.println("Compression Elapsed Time: " + this.elapsedTime + " seconds");
                        reconstructImage(logResults);
                        this.img_output.flush();
                        this.img_output = null;
                }
                catch(IOException e){
                        System.err.println(e);
                }
        }

        // public void viewCompressedImage() {
        //         if (img_output == null) {
        //                 System.out.println("No compressed image available.");
        //                 return;
        //         }

        //         JFrame frame = new JFrame("Compressed Image");
        //         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //         JLabel label = new JLabel(new ImageIcon(img_output));
        //         frame.add(new JScrollPane(label));
        //         frame.pack();
        //         frame.setLocationRelativeTo(null);
        //         frame.setVisible(true);
        // }

        public void start() {
                Boolean end_process = false;
                Boolean input_path = true;
                Boolean output_path = true;
                Boolean pick_mode = true;
                Boolean input_block_size = true;
                Boolean start_compress = true;
                Boolean find_compress_percent = false;
                Scanner scanner = new Scanner(System.in);
                while(!end_process){
                        if(input_path){
                                this.inputAbsPath();
                                this.root = new Node();

                        }

                        if(output_path){
                                System.out.print("Input absolute output path (without extension): ");
                                this.out_path = scanner.nextLine();
                        }

                        if(pick_mode){
                                System.out.println("1. Variance ");
                                System.out.println("2. Mean Absolute Deviation ");
                                System.out.println("3. Max Pixel Difference ");
                                System.out.println("4. Entropy ");
                                System.out.println("5. SSIM ");
                                System.out.print("Pick error calculation method: ");
                                this.mode = scanner.nextInt();
                                scanner.nextLine();
                                this.mode-=1; // adjust to switch case statements from other methods
                                switch (this.mode) {
                                        case 0 -> {
                                                System.out.print("Input variance threshold: ");
                                                this.var_thres = scanner.nextDouble();
                                        }
                                        case 1 -> {
                                                System.out.print("Input MAD threshold: ");
                                                this.mad_thres = scanner.nextDouble();
                                        }
                                        case 2 -> {
                                                System.out.print("Input MPD threshold: ");
                                                this.mpd_thres = scanner.nextDouble();
                                        }
                                        case 3 -> {
                                                System.out.print("Input Entropy threshold: ");
                                                this.entr_thres = scanner.nextDouble();
                                        }
                                        case 4 -> {
                                                System.out.print("Input SSIM threshold: ");
                                                this.ssim_thres = scanner.nextDouble();
                                        }
                                        default -> {
                                                System.out.println("Input variance threshold: ");
                                                this.var_thres = scanner.nextDouble();
                                        }
                                }
                                scanner.nextLine();
                        }
                        if(input_block_size){
                                System.out.print("Input minimum block size for each node: ");
                                this.minBlockSize = scanner.nextInt();
                                if (this.minBlockSize < 4){
                                        System.out.println("the block input is too small, may cause a stackOverflow error, defaulting to 4...");
                                        this.minBlockSize = 4;
                                }
                                scanner.nextLine();
                        }
                        
                        if(start_compress || input_path || output_path || pick_mode || input_block_size){
                                System.out.print("Start compression [y/n] ?: ");
                                Boolean confirm = false;
                                while(!confirm){
                                        String response = scanner.nextLine();
                                        if("y".equals(response) || "Y".equals(response)){
                                                boolean logResults = true;
                                                applyCompression(logResults);
                                                confirm = true;
                                        }
                                        else if("n".equals(response) || "N".equals(response)){
                                                System.out.println("Redirecting to target compression percentage finder...");
                                                find_compress_percent = true;
                                                confirm = true;
                                        }
                                        else{
                                                System.out.println("Invalid input please try again!");
                                        }
                                }
                        }
                        
                        if(find_compress_percent){
                                double expectedPercentage, tolerance;
                                int max_iter;
                                System.out.print("Input target compression percentage (optional): ");
                                expectedPercentage = scanner.nextDouble();
                                scanner.nextLine();
                                if(expectedPercentage==0){
                                        System.out.println("Skipping the process...");
                                }
                                else {
                                        System.out.print("Input tolerance for target: ");
                                        tolerance = scanner.nextDouble();
                                        scanner.nextLine();
                                        System.out.print("Input maximum iteration (infinite loop prevention): ");
                                        max_iter = scanner.nextInt();
                                        scanner.nextLine();
                                        findPercentage(expectedPercentage, tolerance, max_iter);
                                }
                        }

                        input_path = false;
                        output_path = false;
                        pick_mode = false;
                        input_block_size = false;
                        start_compress = false;
                        find_compress_percent = false;

                        System.out.print("Do you want to exit the program [y/n] ?: ");
                        String exitResponse = scanner.nextLine();
                        if ("y".equalsIgnoreCase(exitResponse)) {
                                end_process = true;
                                scanner.close();
                        }
                        else{
                                // if not yes then we ask for which part of input
                                System.out.println("1. re-input absolute image path ");
                                System.out.println("2. re-input absolute output path ");
                                System.out.println("3. re-input error calculation mode ");
                                System.out.println("4. re-input block size minimum ");
                                System.out.println("5. repeat compression process ");
                                System.out.println("6. compression with target percentage ");
                                System.out.print("Select what to do next: ");
                                int response;
                                response = scanner.nextInt();
                                switch (response) {
                                        case 1 -> input_path = true;
                                        case 2 -> output_path = true;
                                        case 3 -> pick_mode = true;
                                        case 4 -> input_block_size = true;
                                        case 5 -> start_compress = true;
                                        case 6 -> find_compress_percent = true;
                                        default -> {
                                                input_path = true;
                                        }
                                }
                                scanner.nextLine();
                        }
                }
        }
}
