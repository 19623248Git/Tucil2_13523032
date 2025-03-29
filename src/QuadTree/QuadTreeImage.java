package QuadTree;

import java.io.IOException;

class QuadTreeImage extends ImageProcessing{

        // Misc attributes
        private Pixel meanPixel;

        // Error measurement attributes
        // Storing the value of RGB not each color channel
        private double variance;
        private double var_thres;
        private double mad;
        private double mad_thres;
        private double mpd;
        private double mpd_thres;
        private double entropy;
        private double entr_thres;
        private double ssim;
        private double ssim_thres;

        // Algorithm attributes
        private int mode;
        private int minBlockSize;
        private double compressPercent;

        public QuadTreeImage(){
                super();
                this.meanPixel = new Pixel();
                this.variance = 0;
                this.var_thres = 0;
                this.mad = 0;
                this.mad_thres = 0;
                this.mpd = 0;
                this.mpd_thres = 0;
                this.entropy = 0;
                this.entr_thres = 0;
                this.ssim = 0;
                this.ssim_thres = 0;
                this.minBlockSize = 4;
                this.compressPercent = 0;
                this.mode = 0; // default mode
        }

        public QuadTreeImage(String absPath) throws IOException{
                super(absPath);
                this.meanPixel = new Pixel();
                this.variance = 0;
                this.var_thres = 0;
                this.mad = 0;
                this.mad_thres = 0;
                this.mpd = 0;
                this.mpd_thres = 0;
                this.entropy = 0;
                this.entr_thres = 0;
                this.ssim = 0;
                this.ssim_thres = 0;
                this.minBlockSize = 4;
                this.compressPercent = 0;
                this.mode = 0; // default mode
        }

        // Getters
        public Pixel getMeanPixel() { 
                return meanPixel; 
        }
        public double getVariance() { 
                return variance; 
        }
        public double getVarThres() { 
                return var_thres; 
        }
        public double getMad() { 
                return mad; 
        }
        public double getMadThres() { 
                return mad_thres; 
        }
        public double getMpd() { 
                return mpd; 
        }
        public double getMpdThres() { 
                return mpd_thres; 
        }
        public double getEntropy() { 
                return entropy; 
        }
        public double getEntrThres() { 
                return entr_thres; 
        }
        public double getSsim() { 
                return ssim; 
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
        public void setMeanPixel(Pixel meanPixel) { 
                this.meanPixel = meanPixel; 
        }
        public void setVariance(double variance) { 
                this.variance = variance; 
        }
        public void setVarThres(double var_thres) { 
                this.var_thres = var_thres; 
        }
        public void setMad(double mad) { 
                this.mad = mad; 
        }
        public void setMadThres(double mad_thres) { 
                this.mad_thres = mad_thres; 
        }
        public void setMpd(double mpd) { 
                this.mpd = mpd; 
        }
        public void setMpdThres(double mpd_thres) { 
                this.mpd_thres = mpd_thres; 
        }
        public void setEntropy(double entropy) { 
                this.entropy = entropy; 
        }
        public void setEntrThres(double entr_thres) { 
                this.entr_thres = entr_thres; 
        }
        public void setSsim(double ssim) { 
                this.ssim = ssim; 
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
        public void meanPixelRange(int x_start, int x_end, int y_start, int y_end){
                this.meanPixel.setRGB(0, 0, 0);
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++){ // (i , j) -> (row, col)
                                this.meanPixel.add(getPixelValue(j, i));
                        }
                }
                this.meanPixel.divMean(n);
        }

        public void calcVariance(int x_start, int x_end, int y_start, int y_end){
                double var_r = 0;
                double var_g = 0;
                double var_b = 0;
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                meanPixelRange(x_start, x_end, y_start, y_end);
                for(int i = y_start; i < y_end; i++){
                        for(int j = x_start; j < x_end; j++){ // (i , j) -> (row, col)
                                var_r+=Math.pow(getPixelValue(j, i).getR()-this.meanPixel.getR(), 2);
                                var_g+=Math.pow(getPixelValue(j, i).getG()-this.meanPixel.getG(), 2);
                                var_b+=Math.pow(getPixelValue(j, i).getB()-this.meanPixel.getB(), 2);
                        }
                }
                var_r/=n;
                var_g/=n;
                var_b/=n;
                this.variance = (var_r + var_g + var_b) / 3;
        }

        public void calcMad(int x_start, int x_end, int y_start, int y_end) {
                double mad_r = 0;
                double mad_g = 0;
                double mad_b = 0;
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                meanPixelRange(x_start, x_end, y_start, y_end);
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++) { // (i , j) -> (row, col)
                                mad_r += Math.abs(getPixelValue(j, i).getR() - this.meanPixel.getR());
                                mad_g += Math.abs(getPixelValue(j, i).getG() - this.meanPixel.getG());
                                mad_b += Math.abs(getPixelValue(j, i).getB() - this.meanPixel.getB());
                        }
                }
                
                mad_r /= n;
                mad_g /= n;
                mad_b /= n;
                this.mad = (mad_r + mad_g + mad_b) / 3;
        }
        
        public void calcMpd(int x_start, int x_end, int y_start, int y_end){
                double max_r = 0;
                double max_g = 0;
                double max_b = 0;
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++) { // (i , j) -> (row, col)
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
                this.mpd = (max_r + max_g + max_b) / 3;
        }

        public void calcEntropy(int x_start, int x_end, int y_start, int y_end){
                double entr_r = 0;
                double entr_g = 0;
                double entr_b = 0;
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++) { // (i , j) -> (row, col)
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
                this.entropy = (entr_r + entr_g + entr_b) / 3;
        }

}