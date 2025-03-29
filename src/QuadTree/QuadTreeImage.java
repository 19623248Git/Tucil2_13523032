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

        public double meanRange(int x_start, int x_end, int y_start, int y_end){
                for (int i = y_start; i < y_end; i++) {
                        for (int j = x_start; j < x_end; j++){ // (i , j) -> (row, col)
                                
                        }
                }
                return 0;
        }

}