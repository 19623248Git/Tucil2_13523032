# QuadTree Image Compression

This is a java program that receives image input of type .png and .jpg and performs **QuadTree Compression** in order to shrink the file size of the input image, by calculating the error of each region corresponding to its user-determined threshold.

**The error measurement methods consists of:**
- Variance
- Mean Absolute Deviation
- Maximum Pixel Difference
- Entropy
- Structural Similarity Index (SSIM)

This program also supports **finding user-determined percentage compression** of the input image with it's own tolerance and maximum iteration.

```
Author: Nathan Jovial Hartono - 13523032
```

# How to run the CLI program

## Compiling the program

### Ensure java versions (23.0.2 or above)

#### check java version: 
```cmd
java --version
```

#### expected output: 
```cmd
java 23.0.2 2025-01-21
Java(TM) SE Runtime Environment (build 23.0.2+7-58)
Java HotSpot(TM) 64-Bit Server VM (build 23.0.2+7-58, mixed mode, sharing)
```

#### check javac version: 
```cmd
javac --version
```

#### expected output: 
```cmd
javac 23.0.2
```

### Compile at root directory of project
```cmd
javac -d bin -cp src src/QuadTree/*.java src/Main.java
```

### Run the Program at root directory of project
```cmd
java -cp bin Main
```

# Class Definitions

## Pixel class definition:

### Attributes:
``` Java
private int r;
private int g;
private int b;
```

### Methods:
```Java
public int getR(); // Returns 'r' channel value of pixel
public int getG(); // Returns 'g' channel value of pixel
public int getB(); // Returns 'b' channel value of pixel
public void setRGB(int r, int g, int b); // Sets the r, g, and b attributes of Pixel
public void add(Pixel other); // Adds pixel with other pixels
public void minus(Pixel other) // Substracts pixel with other pixels
public void divMean(double n) // Mean division of pixel from n pixels
```

## ImageProcessing class definition:

### Attributes:
``` Java
private BufferedImage image;
private int width;
private int height;
private String absPath;
```

### Methods:
```Java
public void loadImage(String absPath); // Loads image not from constructor
public void inputAbsPath(); // Method for user input absolute path
public int getWidth(); // Returns width attribute
public int getHeight(); // Returns height attribute
public Boolean isImageEmpty(); // Returns true if image is null
public Pixel getPixelValue(int x, int y); // Returns a Pixel object at (x,y) coordinate
public void viewImage(); // Image viewer with JFrame
```


## Node class definition:

### Attributes:
```Java
// Data attributes
private int start_x;
private int end_x;
private int start_y;
private int end_y;
private int size;
private double error;
private Pixel meanPixel;

// Tree Data Structure
private Node ne, se, sw, nw;
```

### Methods:
```Java
public boolean isLeaf() // checks if the current node is a leaf
public void DnC() // divide and conquer method
```

## QuadTree class definition:

### Attributes:
```Java
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
public class Node
```

### Methods:
```Java
public Pixel meanPixelRange(int start_x, int end_x, int start_y, int end_y) // calculates the pixel's mean value of that region
public double computeError(Node node) // compute error of the node based off the mode
public boolean errorThresholdCheck(Node node) // checks if error is below Threshold
public double calcVariance(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel) // variance error calculation
public double calcMad(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel) // mad error calculation
public double calcMpd(int x_start, int x_end, int y_start, int y_end) // mpd error calculation
public double calcEntropy(int x_start, int x_end, int y_start, int y_end) // entropy error calculation
public double calcSsim(int x_start, int x_end, int y_start, int y_end, Pixel meanPixel) // ssim error calculation
private double calculateChannelEntropy(int[] histogram, int totalPixels) // helper function for calcEntropy
public void findPercentage(double expectedPercentage, double tolerance, int max_iter) // percentage finder method
public void reconstructImage(boolean logResults) // method wrapper to call reconstructFromNode
public void reconstructFromNode(Node node) // builds image from the tree
public List<Node> getAllNodes() // returns all of the nodes in the tree
private void traverseNodes(Node node, List<Node> nodes) // tree traversing method, DFS
public int getTreeDepth() // method wrapper for computeDepth
private int computeDepth(Node node) //computes depth of tree
public void compress() // method wrapper for root.DnC()
public void applyCompression(boolean logResults) // method wrapper for compress()
public void start() // method to start the entire process, use in Main.java
```