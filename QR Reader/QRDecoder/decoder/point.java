
public class point {
	int pointX;
	int pointY;
	int boxSize = 0;
	int numFinder = 0;
	
	public point(int x, int y) {
		pointX = x;
		pointY = y;
	}
	
	public point(int x, int y, int box) {
		pointX = x;
		pointY = y;
		boxSize = box;
	}
	
	public point(point p) {
		pointX = p.x();
		pointY = p.y();
	}
	
	public void setBoxSize(int size) {
		boxSize = size;
	}
	
	public int getBoxSize() {
		return boxSize;
	}
	
	public void setFinder(int val) {
		numFinder = val;
	}
	
	public int getFinder() {
		return numFinder;
	}
	
	public int x() {
		return pointX;
	}
	
	public int y() {
		return pointY;
	}
	
	public double dist(point p) {
		return Math.sqrt((pointX-p.x())*(pointX-p.x()) + (pointY-p.y())*(pointY-p.y()));
	}
	
	public double dist(int x, int y) {
		return Math.sqrt((pointX-x)*(pointX-x) + (pointY-y)*(pointY-y));
	}
	
	public String toString() {
		return "X: " + Integer.toString(pointX) + " Y: " + Integer.toString(pointY);
	}
}
