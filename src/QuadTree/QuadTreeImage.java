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

        // Node structure
        public class Node{

                // Data attributes
                private int start_x;
                private int end_x;
                private int start_y;
                private int end_y;
                private double error;
                private Pixel meanPixel;

                // Tree data structure
                // private Node parent; // maybe a quad tree doesn't need a parent node, let's remove it now
                private Node ne, se, sw, nw; // instead of list of childrens, determine 4 childrens

                // Get mean of pixels between x1 - x2 and y1 - y2
                public void meanPixelRange(){
                        this.meanPixel.setRGB(0, 0, 0);
                        double n = (this.end_x - this.start_x + 1) * (this.end_y - this.start_y + 1);
                        for (int i = this.start_y; i <= this.end_y; i++) {
                                for (int j = this.start_x; j <= this.end_x; j++){ // (i , j) -> (row, col)
                                        this.meanPixel.add(getPixelValue(j, i));
                                }
                        }
                        this.meanPixel.divMean(n);
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

        public double computeError(Node node){
                return switch (this.mode) {
                        case 0 -> {
                                node.meanPixelRange();
                                yield calcVariance(node.start_x, node.end_x, node.start_y, node.end_y, node.meanPixel);
                        }
                        case 1 -> {
                                node.meanPixelRange();
                                yield calcMad(node.start_x, node.end_x, node.start_y, node.end_y, node.meanPixel);
                        }
                        case 2 -> calcMpd(node.start_x, node.end_x, node.start_y, node.end_y);
                        case 3 -> calcEntropy(node.start_x, node.end_x, node.start_y, node.end_y);
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

}