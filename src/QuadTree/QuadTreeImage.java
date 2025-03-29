package QuadTree;

import java.io.IOException;

class QuadTreeImage extends ImageProcessing{

        // Misc attributes
        private Pixel meanPixel;

        // Error measurement attributes
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

        

        // Get mean of pixels between x1 - x2 and y1 - y2
        public double meanPixelRange(int x_start, int x_end, int y_start, int y_end){
                this.meanPixel.setRGB(0, 0, 0);
                double n = (x_end - x_start + 1) * (y_end - y_start + 1);
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++){ // (i , j) -> (row, col)
                                this.meanPixel.add(getPixelValue(i, j));
                        }
                }
                this.meanPixel.divMean(n);
                return 0;
        }

}