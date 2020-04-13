import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class qrCode {
	
	private boolean[][] qr;
	private Pixel[][] result;
	private String qrPath;
	private int scaling;
	private int bufferX;
	private int bufferY;
	private int qrDim;
	private double nanoseconds;
	private int width;
	private int height;
	
	
	public qrCode() {
		qrPath = "";
		result = null;
		qr = null;
		scaling = 5;
		bufferX = 1;
		bufferY = 1;
		qrDim = 0;
		nanoseconds = 0;
		width = 0;
		height = 0;
	}
	public qrCode(String path) {
		qrPath = path;
		result = null;
		qr = null;
		scaling = 6;
		bufferX = 0;
		bufferY = 0;
		width = 0;
		height = 0;
	}
	
	public qrCode(String path, int scale) {
		qrPath = path;
		result = null;
		qr = null;
		scaling = scale;
		bufferX = 1;
		bufferY = 1;
	}
	
	public qrCode(String path, int scale, int buffer) {
		qrPath = path;
		result = null;
		qr = null;
		scaling = scale;
		bufferX = buffer;
		bufferY = buffer;
	}
	
	public qrCode(String path, int scale, int bufferX, int bufferY) {
		qrPath = path;
		result = null;
		qr = null;
		scaling = scale;
		this.bufferX = bufferX;
		this.bufferY = bufferY;
	}
	
	public void setQrPath(String path) {
		qrPath = path;
	}
	
	public void setScaling(int scale) {
		scaling = scale;
	}
	
	public void setBuffer(int buffer) {
		bufferX = buffer;
		bufferY = buffer;
	}
	
	public void setBuffer(int bufferX, int bufferY) {
		this.bufferX = bufferX;
		this.bufferY = bufferY;
	}
	
	public double getPerformNano() {
		if(nanoseconds == 0) {
			System.out.println("Have not called performAction method to time");
			return -1;
		}
		return nanoseconds;
	}
	
	public int getQRDimmensions() {
		if(qrDim == 0) {
			System.out.println("Have not revieved qr code to get dimension");
			return -1;
		}
		return qrDim;
	}
	
	public int getImageWidth() {
		return width;
	}
	
	public int getImageHeight() {
		return height;
	}
	
	public void decodeQr() {
		if( qr!= null) {
			qrDecoder.decode(qr);
		}
		else {
			System.out.println("Have not recived qr code to decode");
		}
		
	}
	
	private boolean[][] decode(Pixel[][] result) {

		double startTime = System.nanoTime();
		
		for(Pixel[] pRow: result) {
			for(Pixel p: pRow) {
				if(p.av() > 95) p.clearWhite();
				else p.clear();
			} 	
			
		}
		
		double ratios[] = {0,0,0,0,0};
	    int currentPos = 0;
		
		ArrayList<point> points = new ArrayList<>();
		ArrayList<point> points2 = new ArrayList<>();
		ArrayList<point> points3 = new ArrayList<>();
		
		for(int i = 1; i < result[0].length-1; i++) {
			currentPos = -1;
			for(int k = 0; k < 5; k++) ratios[k] = 0;
			for(int j = 1; j < result.length-1; j++) {
				
				if(currentPos == -1) {
					if(result[j][i].r() == 0 && result[j-1][i].r() == 255) {
						currentPos++;
						ratios[currentPos]++;
					}
				}
				else if(currentPos == 0) {
					if(result[j][i].r() == 255) currentPos++;
					ratios[currentPos]++;
				}
				else if(currentPos == 1) {
					if(result[j][i].r() == 0) {
						if(ratios[0]/ratios[1] >= .7 && ratios[0]/ratios[1] <= 1.4) currentPos++;
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							currentPos = 0;
						}
					}
					ratios[currentPos]++;
				}
				else if(currentPos == 2) {
					if(result[j][i].r() == 255) {
						if(ratios[2]/(ratios[0]+ratios[1])*2/3 >= .7 && ratios[2]/(ratios[0]+ratios[1])*2/3 <= 1.4) currentPos++;
						else {
							ratios[0] = ratios[2];
							ratios[2] = 0;
							ratios[1] = 0;
							currentPos = 1;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 3) {
					if(result[j][i].r() == 0) {
						if(ratios[3]/(ratios[0]+ratios[1])*2 >=.7 && ratios[3]/(ratios[0]+ratios[1])*2 <= 1.4) currentPos++;
						else if(ratios[3]/ratios[2] >=.75 && ratios[3]/ratios[2] <= 1.3) {
							ratios[0] = ratios[2];
							ratios[1] = ratios[3];
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 2;
						}
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 0;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 4) {
					if(result[j][i].r() == 255) {
						if((ratios[4]/ratios[3])/(ratios[0]/ratios[1]) >= .7 && (ratios[4]/ratios[3])/(ratios[0]/ratios[1]) <= 1.4) {
							points.add(new point(i, (int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2),(int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2)));
							//result[(int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2)][i].setColor(0,255,0);
						}
						ratios[0] = 0;
						ratios[1] = 0;
						ratios[2] = 0;
						ratios[3] = 0;
						ratios[4] = 0;
						currentPos = -1;
					}
					else {
						ratios[currentPos]++;
					}
				}

			}
		}
		
		for(point p: points) {
			currentPos = -1;
			for(int k = 0; k < 5; k++) ratios[k] = 0;
			for(int j = 1; j < result[0].length-1; j++) {

				if(currentPos == -1) {
					if(result[p.y()][j].r() == 0 && result[p.y()][j-1].r() == 255) {
						currentPos++;
						ratios[currentPos]++;
					}
				}
				else if(currentPos == 0) {
					if(result[p.y()][j].r() == 255) currentPos++;
					ratios[currentPos]++;
				}
				else if(currentPos == 1) {
					if(result[p.y()][j].r() == 0) {
						if(ratios[0]/ratios[1] >= .6 && ratios[0]/ratios[1] <= 1.6) currentPos++;
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							currentPos = 0;
						}
					}
					ratios[currentPos]++;
				}
				else if(currentPos == 2) {
					if(result[p.y()][j].r() == 255) {
						if(ratios[2]/(ratios[0]+ratios[1])*2/3 >= .6 && ratios[2]/(ratios[0]+ratios[1])*2/3 <= 1.6) currentPos++;
						else {
							ratios[0] = ratios[2];
							ratios[2] = 0;
							ratios[1] = 0;
							currentPos = 1;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 3) {
					if(result[p.y()][j].r() == 0) {
						if(ratios[3]/(ratios[0]+ratios[1])*2 >=.6 && ratios[3]/(ratios[0]+ratios[1])*2 <= 1.6) currentPos++;
						else if(ratios[3]/ratios[2] >=.6 && ratios[3]/ratios[2] <= 1.6) {
							ratios[0] = ratios[2];
							ratios[1] = ratios[3];
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 2;
						}
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 0;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 4) {
					if(result[p.y()][j].r() == 255) {
						if((ratios[4]/ratios[3])/(ratios[0]/ratios[1]) >= .6 && (ratios[4]/ratios[3])/(ratios[0]/ratios[1]) <= 1.6) {
							if(p.dist((int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2), p.y()) < 10) {
								points2.add(new point((int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2),p.y()));
								//result[(int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2)][i].setColor(0,255,0);
							}
						}
						ratios[0] = 0;
						ratios[1] = 0;
						ratios[2] = 0;
						ratios[3] = 0;
						ratios[4] = 0;
						currentPos = -1;
					}
					else {
						ratios[currentPos]++;
					}
				}

			}
		}
		
		for(point p: points2) {
			currentPos = -1;
			for(int k = 0; k < 5; k++) ratios[k] = 0;
			for(int j = 1; j < result.length-1; j++) {
//				result[j][p.x()].setColor(155,0,255);
				if(currentPos == -1) {
					if(result[j][p.x()].r() == 0 && result[j-1][p.x()].r() == 255) {
						currentPos++;
						ratios[currentPos]++;
					}
				}
				else if(currentPos == 0) {
					if(result[j][p.x()].r() == 255) currentPos++;
					ratios[currentPos]++;
				}
				else if(currentPos == 1) {
					if(result[j][p.x()].r() == 0) {
						if(ratios[0]/ratios[1] >= .6 && ratios[0]/ratios[1] <= 1.6) currentPos++;
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							currentPos = 0;
						}
					}
					ratios[currentPos]++;
				}
				else if(currentPos == 2) {
					if(result[j][p.x()].r() == 255) {
						if(ratios[2]/(ratios[0]+ratios[1])*2/3 >= .6 && ratios[2]/(ratios[0]+ratios[1])*2/3 <= 1.6) currentPos++;
						else {
							ratios[0] = ratios[2];
							ratios[2] = 0;
							ratios[1] = 0;
							currentPos = 1;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 3) {
					if(result[j][p.x()].r() == 0) {
						if(ratios[3]/(ratios[0]+ratios[1])*2 >=.6 && ratios[3]/(ratios[0]+ratios[1])*2 <= 1.6) currentPos++;
						else if(ratios[3]/ratios[2] >=.6 && ratios[3]/ratios[2] <= 1.6) {
							ratios[0] = ratios[2];
							ratios[1] = ratios[3];
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 2;
						}
						else {
							ratios[0] = 0;
							ratios[1] = 0;
							ratios[2] = 0;
							ratios[3] = 0;
							currentPos = 0;
						}
					}
					
					ratios[currentPos]++;
				}
				else if(currentPos == 4) {
					if(result[j][p.x()].r() == 255) {
						if((ratios[4]/ratios[3])/(ratios[0]/ratios[1]) >= .6 && (ratios[4]/ratios[3])/(ratios[0]/ratios[1]) <= 1.6) {
							if(p.dist(p.x(),(int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2)) < 20) {
								points3.add(new point(p.x(), (int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2),(int)(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])));
								//result[(int) (j-(ratios[0]+ratios[1]+ratios[2]+ratios[3]+ratios[4])/2)][i].setColor(0,255,0);
							}
						}
						ratios[0] = 0;
						ratios[1] = 0;
						ratios[2] = 0;
						ratios[3] = 0;
						ratios[4] = 0;
						currentPos = -1;
					}
					else {
						ratios[currentPos]++;
					}
				}
			}
		}
		
		for(int i = 0; i < points3.size(); i++) {
			for(int j = 0; j < points3.size(); j++) {
				if(i != j && points3.get(i).dist(points3.get(j)) < 7.5) {
					points.set(i, new point((points3.get(i).x() + points3.get(j).x())/2,(points3.get(i).y() + points3.get(j).y())/2));
					points3.remove(j);
					i=0;
				}
			}
		}
		
		int avgX = 0;
		int avgY = 0;
		
		for(point p: points3) {
			avgX+=p.x();
			avgY+=p.y();
		}
		
		try {
			avgX/= points3.size();
			avgY/= points3.size();
		}
		catch(Exception e) {
			System.out.println("Could not properly detect qr code");
			return null;
		}
		
		if(points3.size() < 3) {
			System.out.println("Could not properly detect qr code");
			return null;
		}

		else if(points3.size() > 3){
			ArrayList<point> finalPoints = new ArrayList<>();
				
			for(int j = 0; j < 3; j++) {
				int max1 = 0;
				for(int i = 0; i < points3.size(); i++) 
					if(points3.get(i).dist(avgX,avgY) > points3.get(max1).dist(avgX,avgY)) max1 = i;
				
				finalPoints.add(points3.get(max1));
				points3.remove(max1);
			}
			points3 = finalPoints;
		}
		
		int topRight = -1;
		for(int i = 0; i < 3; i++) {
			if(points3.get(i).x() < avgX && points3.get(i).y() > avgY) {
				points3.get(i).setFinder(1);
				topRight = i;	
			}
		}
		if(topRight != 0) {
			point temp = points3.get(0);
			points3.set(0, points3.get(topRight));
			points3.set(topRight, temp);
		}
		
		if(points3.get(0).y()-points3.get(1).y() < points3.get(0).y()-points3.get(2).y()) {
			points3.get(2).setFinder(2);
			points3.get(1).setFinder(3);
		}
		else {
			points3.get(1).setFinder(2);
			points3.get(2).setFinder(3);
		}
		
		qrDim = (int) Math.ceil(((double)points3.get(0).dist(points3.get(1)) / (double)points3.get(0).getBoxSize() * 7) + 7);
		
		if(qrDim < 21) qrDim = 21;
		else if(qrDim % 4 == 0) qrDim++;
		else if(qrDim % 4 == 2) qrDim--;
		else if(qrDim % 4 == 3) qrDim+=2;
				
		point[] outerP = new point[4];
		
		double slopeTop = (double)(points3.get(0).x() - points3.get(1).x())/(double)(points3.get(0).y() - points3.get(1).y());
		double topLeftShift = Math.cos(.75 * Math.PI + Math.atan(slopeTop));
		double topIncShift =  Math.sin(.75 * Math.PI + Math.atan(slopeTop));
		
		outerP[0] = new point((int) (points3.get(0).x()  - points3.get(0).getBoxSize()/1.7 * topIncShift),
				              (int) (points3.get(0).y()  - points3.get(0).getBoxSize()/1.7 * topLeftShift));
		
		double rightShift = Math.cos(.25 * Math.PI + Math.atan(slopeTop));
		double rightIncShift =  Math.sin(.25 * Math.PI + Math.atan(slopeTop));
		
		outerP[1] = new point((int) (points3.get(1).x()  - points3.get(1).getBoxSize()/1.7 * rightIncShift),
							  (int) (points3.get(1).y()  - points3.get(1).getBoxSize()/1.7 * rightShift));
		
		double slopeLeft = (double)(points3.get(0).x() - points3.get(2).x())/(double)(points3.get(0).y() - points3.get(2).y());
		double leftShift = Math.cos(1.25 * Math.PI + Math.atan(slopeLeft));
		double leftIncShift =  Math.sin(1.25 * Math.PI + Math.atan(slopeLeft));

		outerP[2] = new point((int) (points3.get(2).x()  + points3.get(2).getBoxSize()/1.7 * Math.abs(leftShift)),
	              			  (int) (points3.get(2).y()  + points3.get(2).getBoxSize()/1.7 * Math.abs(leftIncShift)));
		
		
		
		double cosShift = (Math.cos(Math.atan(slopeTop) + Math.PI) + Math.cos(Math.atan(slopeLeft) + Math.PI))/2;
		double sinShift = (Math.sin(Math.atan(slopeTop) + Math.PI) + Math.sin(Math.atan(slopeLeft) + Math.PI))/2;
		double slopeShift = 0;
		
		slopeShift = cosShift/sinShift;
		if(slopeShift < 0) slopeShift = -1/slopeShift;
		
		outerP[3] = new point(outerP[1].x() - outerP[0].x() + outerP[2].x(),outerP[1].y() - outerP[0].y() + outerP[2].y());
		
		int brX = 0;
		int brY = 0;
		
		for(double i = 0; i < (outerP[0].y() - outerP[3].y())+5; i+=.1) {
			if(outerP[0].dist((int) (outerP[0].x() + i*slopeShift), (int) (outerP[0].y() - i)) < outerP[0].dist(outerP[2]) * Math.sqrt(2) + 15) {
				if(result[(int) (outerP[0].y() - i)][(int) (outerP[0].x() + i*slopeShift)].av() == 0) {
					brX = (int) (outerP[0].x() + i*slopeShift);
					brY = (int) (outerP[0].y() - i);
				}
			}
		}
		outerP[3] = new point(brX, brY);
		
		boolean[][] qr = new boolean[qrDim][qrDim];
		int posX = 0;
		int posY = 0;
		
		double intX = (double)(outerP[2].x()-outerP[0].x())/(qrDim-1);
		double intY = (double)(outerP[2].y()-outerP[0].y())/(qrDim-1);
		
		double intX2 = (double)(outerP[3].x()-outerP[1].x())/(qrDim-1);
		double intY2 = (double)(outerP[3].y()-outerP[1].y())/(qrDim-1);
		
		double inbetweenX = 0;
		double inbetweenY = 0;
		
		for(double i = 0; i < qrDim; i++) {

			inbetweenY = ((((outerP[2].y() + intY2*i) - (outerP[3].y() + intY*i))*(i/(qrDim-1))) + (((outerP[0].y() + intY2*i) - (outerP[1].y() + intY*i))*(1-(i/(qrDim-1)))))/(qrDim-1);
			inbetweenX = ((((outerP[2].x() + intX2*i) - (outerP[3].x() + intX*i))*(i/(qrDim-1))) + (((outerP[0].x() + intX2*i) - (outerP[1].x() + intX*i))*(1-(i/(qrDim-1)))))/(qrDim-1);	
			
			for(int j = 0; j < qrDim; j++) {
				try {
					qr[posY][posX] = result[(int)((double)(outerP[0].y()) + intY*i - inbetweenY*j)][(int)((double)(outerP[0].x()) + intX*i - inbetweenX*j)].av() == 255;
//					result[(int)((double)(outerP[0].y()) + intY*i - inbetweenY*j)][(int)((double)(outerP[0].x()) + intX*i - inbetweenX*j)].setColor(255,0,0);
					
				}
				catch(Exception e){
					System.out.println("Could not properly detect qr code");
					return null;
				}
				posX++;
			}
			posY++;
			posX = 0;
		}
		
//		for(point p: outerP) result[p.y()][p.x()].setColor(0,255,0);
		
		for(int l = 0; l < 3; l++) {
			for(int k = -points3.get(l).getBoxSize()/2; k <= points3.get(l).getBoxSize()/2; k++) {
				for(int j = 0; j < 4; j++) {
					result[points3.get(l).y() -k][points3.get(l).x() + points3.get(l).getBoxSize()/2 - j].setColor(0,150,0);
					result[points3.get(l).y() +k][points3.get(l).x() - points3.get(l).getBoxSize()/2 + j].setColor(0,150,0);
					result[points3.get(l).y() + points3.get(l).getBoxSize()/2 - j][points3.get(l).x() -k].setColor(0,150,0);
					result[points3.get(l).y() - points3.get(l).getBoxSize()/2 + j][points3.get(l).x() +k].setColor(0,150,0);
				}
			}
		}
		
		nanoseconds = System.nanoTime()-startTime;
		
		return qr;
	}
	
	public void loadImage() throws IOException {
		
		if( qrPath!= null) {
			BufferedImage inputImage = ImageIO.read(new File("src/images/" + qrPath +".jpg"));
			
			width = inputImage.getWidth();
			height = inputImage.getHeight();
			
			//System.out.println("Starting process");
					
			result = imToArray(inputImage, scaling, bufferX, bufferY);
					
			//Pixel[][] result2 = imToArray(inputImage, 1, 0);
		}
		else {
			System.out.println("Please set path name via constructor or setQrPath methor");
		}
	}
	
	public void decodeImage() {
		if(result == null) {
			System.out.println("Cannot decode image (Image may not have loaded)");
		}
		else {
			qr = decode(result);
		}
	}
	
	public String getQRString() {
		if(qr!=null) {
			String qrDisp = "";
			for(int k = 0; k < qr.length; k++) {
				for(int j = 0; j < qr[0].length; j++) {
					qrDisp+= qr[k][j]? "  ":"||";
				}
				qrDisp+='\n';
			}
			return qrDisp;
		}
		return "Cannot Display QR (May need to load image first)";
	}
	
	public void displayQRString() {
		if(qr!=null) {
			for(int k = 0; k < qr.length; k++) {
				for(int j = 0; j < qr[0].length; j++) {
					System.out.print(qr[k][j]? "  ":"||");
				}
				System.out.println();
			}
		}
		else System.out.println("Cannot Display QR (May need to load image first)");
	}
	
	private Pixel[][] imToArray(BufferedImage inputImage, int scaling, int scaleBufferX, int scaleBufferY) {
		
		final byte[] pixels = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData();
		
		final int width =  (int) (inputImage.getWidth());
		final int height = (int) (inputImage.getHeight());

		int row = 0;
		int col = 0;
		
		Pixel[][] result = new Pixel[height/scaling + scaleBufferY][width/scaling + scaleBufferX];
		
		for (int pixel = 0; pixel < pixels.length; pixel+=3) {
			
			
			if(col%scaling == 0 & row%scaling == 0) {
				result[row/scaling][col/scaling] = new Pixel((pixels[pixel+2]),(pixels[pixel+1]),(pixels[pixel]));
			}
			
			col++;

			if (col == width) {
				col = 0;
				row++;
			}
		}
		return result;
	} 
	public void drawImage() {
		if(qrPath != null && result!= null) {
			BufferedImage highlight = new BufferedImage(result.length, result[0].length, BufferedImage.TYPE_INT_RGB);
		    for (int x = 0; x < result.length; x++) {
		        for (int y = 0; y < result[0].length; y++) {
		        	Color c = new Color(result[x][y].r(),result[x][y].g(),result[x][y].b());
		        	highlight.setRGB(result.length-x-1, y, c.getRGB());
		        }
		    }
		    File ImageFile = new File("src/images output/highlights/" + qrPath + "_analyzed" +".jpg");
		    try {
		        ImageIO.write(highlight, "png", ImageFile);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else {
			System.out.println("Cannot Display Image (May need to lead image first)");
		}
	}
	public void drawQRImage(int boxLength) {
		if(qrPath != null && result!= null && qr!= null) {
			BufferedImage qrRep = new BufferedImage((qr.length+2)*boxLength, (qr.length+2)*boxLength, BufferedImage.TYPE_INT_RGB);
		    for (int x = 0; x < qr.length+2; x++) {
		        for (int y = 0; y < qr.length+2; y++) {
		        	for(int i = 0; i < boxLength; i++) {
		        		for(int j = 0; j < boxLength; j++) {
		        			if(x > 0 && y > 0 && x <= qr.length & y <= qr.length) {
		        				qrRep.setRGB(x*boxLength+j, y*boxLength+i, !qr[y-1][x-1]?0:-1);
		        			}
		        			else {
		        				qrRep.setRGB(x*boxLength+j, y*boxLength+i, -1);
		        			}
		        		}
		        	}
		        }
		    }
		    File ImageFile = new File("src/images output/representations/" + qrPath + "_qr_representation" +".jpg");
		    try {
		        ImageIO.write(qrRep, "png", ImageFile);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else {
			System.out.println("Cannot Display Image (May need to lead image first)");
		}
	}
}