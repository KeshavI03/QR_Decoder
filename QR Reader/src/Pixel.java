
public class Pixel {
	private int red;
	private int green;
	private int blue;
	
	public Pixel() {
		red = 0;
		green = 0;
		blue = 0;
	}
	
	public Pixel(int r, int g, int b) {
		if (r < 0) {
			r += 256;
		}
		if (g < 0) {
			g += 256;
		}
		if (b < 0) {
			b += 256;
		}
		red = r;
		green = g;
		blue = b;
	}
	
	public int r() {
		return red;
	}
	
	public int g() {
		return green;
	}
	
	public int b() {
		return blue;
	}
	
	public void setR(int r) {
		if (r < 0) {
			r += 256;
		}
		red = r;
	}
	
	public void setG(int g) {
		if (g < 0) {
			g += 256;
		}
		green = g;
	}
	
	public void setB(int b) {
		if (b < 0) {
			b += 256;
		}
		blue = b;
	}
	
	public void setColor(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public void setColor(Pixel p) {
		red = p.r();
		green = p.g();
		blue = p.b();
	}
	
	public void clear() {
		red = 0;
		green = 0;
		blue = 0;
	}
	
	public void clearWhite() {
		red = 255;
		green = 255;
		blue = 255;
	}
	
	public int av() {
		return (red + blue + green)/3;
	}
	
	public int colorDist(Pixel p) {
		return Math.abs(p.r()-red) + Math.abs(p.g()-green) + Math.abs(p.b()-blue);
	}
}
