# QuadTree Image Compression

This is a java program that receives image input of type .png and .jpg and performs **QuadTree Compression** in order to shrink the file size of the input image.

**Author:** Nathan Jovial Hartono - 13523032

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