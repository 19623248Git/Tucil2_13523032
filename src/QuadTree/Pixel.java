package QuadTree;

public class Pixel {

	private int r;
	private int g;
	private int b;

	// Default constructor
	public Pixel(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	// User-defined constructor with absPath
	public Pixel(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	// Returns 'r' channel value of pixel
	public int getR(){
		return this.r;
	}

	// Returns 'g' channel value of pixel
	public int getG(){
		return this.g;
	}

	// Returns 'b' channel value of pixel
	public int getB(){
		return this.b;
	}

	// Sets the r, g, and b attributes of Pixel
	public void setRGB(int r, int g, int b){
		this.r = r;
		this.b = b;
		this.g = g;
	}
}
