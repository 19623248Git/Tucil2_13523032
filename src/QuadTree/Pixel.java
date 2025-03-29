package QuadTree;

public class Pixel {

	private int r;
	private int g;
	private int b;

	public Pixel(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	public Pixel(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getR(){
		return this.r;
	}

	public int getG(){
		return this.g;
	}

	public int getB(){
		return this.b;
	}

	public void setRGB(int r, int g, int b){
		this.r = r;
		this.b = b;
		this.g = g;
	}
}
