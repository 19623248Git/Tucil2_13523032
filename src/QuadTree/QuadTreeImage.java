package QuadTree;

import java.io.IOException;

class QuadTreeImage extends ImageProcessing{

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
                        computeError(this);
                        if(!errorThresholdCheck(this) && !(this.size <= getMinBlockSize())){
                                int start_dividedX = (this.end_x - this.start_x + 1)/2;
                                int start_dividedY = (this.end_y - this.start_y + 1)/2;
                                this.ne = new Node(start_dividedX, this.end_x, this.start_y ,start_dividedY-1);
                                this.se = new Node(start_dividedX, this.end_x, start_dividedY, this.end_y);
                                this.sw = new Node(this.start_x, start_dividedX-1, start_dividedY, this.end_y);
                                this.nw = new Node(this.start_x, start_dividedX-1, this.start_y ,start_dividedY-1);
                                ne.DnC();
                                se.DnC();
                                sw.DnC();
                                nw.DnC();
                        }
                        else{
                                // do nothing
                        }
                }

        }
        

        public QuadTreeImage(){
                super();
                this.var_thres = 0;
                this.mad_thres = 0;
                this.mpd_thres = 0;
                this.entr_thres = 0;
                this.ssim_thres = 0;
                this.minBlockSize = 4;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.root = new Node();
        }

        public QuadTreeImage(String absPath) throws IOException{
                super(absPath);
                this.var_thres = 0;
                this.mad_thres = 0;
                this.mpd_thres = 0;
                this.entr_thres = 0;
                this.ssim_thres = 0;
                this.minBlockSize = 4;
                this.compressPercent = 0;
                this.mode = 0; // default mode
                this.root = new Node();
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
        
        public double calcMpd(int x_start, int x_end, int y_start, int y_end){
                double max_r = 0;
                double max_g = 0;
                double max_b = 0;
                for (int i = y_start; i <= y_end; i++) {
                        for (int j = x_start; j <= x_end; j++) { // (i , j) -> (row, col)
                                if(max_r < getPixelValue(j, i).getR()){
                                        max_r = getPixelValue(j, i).getR();
                                }
                                if(max_g < getPixelValue(j, i).getG()){
                                        max_g = getPixelValue(j, i).getG();
                                }
                                if(max_b < getPixelValue(j, i).getB()){
                                        max_b = getPixelValue(j, i).getB();
                                }
                        }
                }
                return ((max_r + max_g + max_b) / 3);
        }

        public double calcEntropy(int x_start, int x_end, int y_start, int y_end){
                double entr_r = 0;
                double entr_g = 0;
                double entr_b = 0;
                for (int i = y_start; i <= y_end; i++) {
                        for (int j = x_start; j <= x_end; j++) { // (i , j) -> (row, col)
                                if(entr_r == 0){entr_r = 0;}
                                if(entr_g == 0){entr_g = 0;}
                                if(entr_b == 0){entr_b = 0;}
                                entr_r += (getPixelValue(j, i).getR() * Math.log(getPixelValue(j, i).getR())/Math.log(2));
                                entr_b += (getPixelValue(j, i).getB() * Math.log(getPixelValue(j, i).getB())/Math.log(2));
                                entr_g += (getPixelValue(j, i).getG() * Math.log(getPixelValue(j, i).getG())/Math.log(2));
                        }
                }
                entr_r*=-1;
                entr_b*=-1;
                entr_g*=-1;
                return ((entr_r + entr_g + entr_b) / 3);
        }

        public void compress(){
                root.DnC();
        }

}