
import java.util.ArrayList;

abstract class qrDecoder {
	
	static boolean[][] qr;
	static int mskNum;
	static int version;
	static int encMode;
	static int ecLevel;
	
	public static void decode(boolean[][] qrCode) {
		
		qr = qrCode;
		ecLevel = (!qr[8][0]?2:0) + (qr[8][1]?1:0);
		mskNum = (qr[8][4]?1:0) + (!qr[8][3]?2:0) + (qr[8][2]?4:0);
		version = (qr.length-17)/4;
		System.out.println("Version Number: " + version);
		System.out.println("Mask Code: " + (qr[8][2]?1:0)+(!qr[8][3]?1:0)+(qr[8][4]?1:0) + " (" + mskNum + ")");
		System.out.println("Error Correction Level: " + Integer.toBinaryString(ecLevel) + " (" + errorLevel(ecLevel) + ")");
//		int maskNum = 9;
		
		for(int i = 0; i < qr.length; i++) {
			for(int j = 0; j < qr.length; j++) {	
				System.out.print(msk(i, j)?"||":"  ");
			}
			System.out.println();
		}
		
		encMode = upScan(qr.length-1, qr.length-1, 2);
		
		System.out.println(Integer.toBinaryString(encMode) + " (" + encoding(encMode) + ")");
		System.out.println("Character Count Indicator Length: " + characterCountIndicatorLength());
		
		if(version == 1) {
			getV1QR();
		}
		else if(version == 2) {
			getV2QR();
		}
		else if(version == 3) {
			getV3QR();
		}

		
	}
	
	
	
	
	public static void getV3QR() {
		
		int qrLen = upScan(qr.length-3, qr.length-1, characterCountIndicatorLength()/2);
		System.out.println("Qr Length: " + qrLen);
		
		int numUp = (qr.length-11)/4;
		System.out.println(leftDScan(qr.length-19,qr.length-1));
		
	}
	
	

	
	public static void getV2QR() {
		
		ArrayList<Integer> values = new ArrayList<>();
		int qrLen = upScan(qr.length-3, qr.length-1, characterCountIndicatorLength()/2);
		System.out.println("Qr Length: " + qrLen);
		
		int numUp = (qr.length-11)/4;
		
		for(int i = 0; i < numUp; i++) values.add(upScan(qr.length-3-i*4, qr.length-1, 4));
		values.add(leftDScan(qr.length-3-numUp*4, qr.length-1));
		for(int i = 0; i < numUp; i++) values.add(downScan(11+i*4, qr.length-3, 4));
		values.add(leftUScan(11+numUp*4, qr.length-3));
		values.add(upAlignScan(qr.length-3, qr.length-5));
		values.add(upScan(qr.length-12, qr.length-5,4));
		values.add(leftDScan1(qr.length-16, qr.length-5));
		for(int i = 0; i < 2; i++) values.add(downScan(qr.length-13+i*9, qr.length-7,4));
		values.add(upScan(qr.length-1, qr.length-9,4));
		values.add(upAlignStagerScan(qr.length-5, qr.length-10));
		values.add(upStagerScan(qr.length-11, qr.length-10,4));
		values.add(upTimeStagerScan(qr.length-15, qr.length-10));
		values.add(upStagerScan(qr.length-20, qr.length-10,4));
		values.add(leftDScanEdge(1,qr.length-10));
		values.add(downTimeStagerScan(2,qr.length-12));
		for(int i = 0; i < 4; i++) values.add(downStagerScan(7+i*4,qr.length-12,4));
		values.add(leftStagerUScan(qr.length-2,qr.length-12));
		for(int i = 0; i < 3; i++) values.add(upStagerScan(qr.length-3-i*4, qr.length-14,4));
		values.add(upTimeStagerScan(qr.length-15, qr.length-14));
		values.add(upStagerScan(qr.length-20, qr.length-14,4));
		values.add(leftDScanEdge(1,qr.length-14));
		values.add(downTimeStagerScan(2,qr.length-16));
		values.add(downStagerScan(7,qr.length-16,4));

		System.out.print("Message: ");
		for(int i = 0; i < Math.min(qrLen, values.size()-1); i++) System.out.print((char)(int)values.get(i+1));
	}
	
	
	
	
	
	public static void getV1QR(){
		int qrLen = upScan(qr.length-3, qr.length-1, characterCountIndicatorLength()/2);
		System.out.println("Qr Length: " + qrLen);
		
		int numUp = (qr.length-11)/4;
		int numUp2 = (qr.length-5)/4;

		ArrayList<Integer> values = new ArrayList<>();
		
		for(int j = 0; j < 2; j++) {
			for(int i = 0; i < numUp; i++) values.add(upScan(qr.length-3-i*4, qr.length-1-j*4, 4));
			values.add(leftDScan(qr.length-3-numUp*4, qr.length-1-j*4));
			for(int i = 0; i < numUp; i++) values.add(downScan(11+i*4, qr.length-3-j*4, 4));
			values.add(leftUScan(11+numUp*4, qr.length-3-j*4));
		}
		for(int i = 0; i < numUp2; i++) values.add(upScan(qr.length-3-i*4 - ((qr.length-3-i*4 < 7)?1:0), qr.length-9,4));
		values.add(leftDScan(1, qr.length-9));
		values.add(downScan(2, qr.length-11,4));
		
		System.out.print("Message: ");
//		for(int i = 0; i < Math.min(qrLen, values.size()-1); i++) System.out.print((char)(int)values.get(i+1));
		for(int i = 0; i < values.size(); i++) System.out.print((int)values.get(i) + " ");
	}
	
	
	
	//point represents bottom right corner
	private static int upScan(int i, int j, int len) {
		int total = 0;
		for(int k = 0; k < len; k++) {
			for(int l = 0; l < 2; l++) {
				total <<= 1;
				total+=msk(i-k,j-l)?1:0;
			}
		}
		return total;
	}
	//point represents top right corner
	private static int downScan(int i, int j, int len) {
		int total = 0;
		for(int k = 0; k > -len; k--) {
			for(int l = 0; l < 2; l++) {
				total <<= 1;
				total+=msk(i-k,j-l)?1:0;
			}
		}
		return total;
	}
	//point represents bottom right corner
	private static int leftDScan(int i, int j) {
		return  (msk(i,j)?128:0) + (msk(i,j-1)?64:0) + (msk(i-1,j)?32:0) + (msk(i-1,j-1)?16:0) + (msk(i-1,j-2)?8:0) + (msk(i-1,j-3)?4:0) + (msk(i,j-2)?2:0) + (msk(i,j-3)?1:0);
	}
	//point represents top right corner
	private static int leftUScan(int i, int j) {
		return  (msk(i,j)?128:0) + (msk(i,j-1)?64:0) + (msk(i+1,j)?32:0) + (msk(i+1,j-1)?16:0) + (msk(i+1,j-2)?8:0) + (msk(i+1,j-3)?4:0) + (msk(i,j-2)?2:0) + (msk(i,j-3)?1:0);
	}
	//point represents bottom right of two by two grid that skips up past allignment marker: 2 wide, 4 high
	private static int upAlignScan(int i, int j) {
		return upScan(i,j,2)*16 + upScan(i-7,j,2);
	}
	//point represents bottom right of grid just like upScan, but upper two points placed to the left of row below
	private static int leftDScan3(int i, int j) {
		return upScan(i,j,3)*4 + (msk(i-2,j-2)?2:0) + (msk(i-2,j-3)?1:0);
	}
	//point represents top right which countr four left then down 2x2 like donwScan (flip of leftDScan3)
	private static int leftDScan1(int i, int j) {
		return (msk(i,j)?128:0) + (msk(i,j-1)?64:0) + downScan(i,j-2,3);
	}
	
	//point represents bottom left with the right column one higher
	private static int upStagerScan(int i, int j, int len) {
		int total = 0;
		for(int k = 0; k < len; k++) {
			for(int l = 0; l < 2; l++) {
				total <<= 1;
				total+=msk(i-l-k,j+l)?1:0;
			}
		}
		return total;
	}
	//point represents top left with left column one higher
	private static int downStagerScan(int i, int j, int len) {
		int total = 0;
		for(int k = 0; k < len; k++) {
			for(int l = 0; l < 2; l++) {
				total <<= 1;
				total+=msk(i+l+k,j+l)?1:0;
			}
		}
		return total;
	}
	//point represents bottom left which is four below an upRStaggerScan of len=2 (up four then stagger till 8 total)
	private static int upAlignStagerScan(int i, int j) {
		return (msk(i,j)?128:0) + (msk(i-1,j)?64:0) + (msk(i-2,j)?32:0) + (msk(i-3,j)?16:0) + upStagerScan(i-4,j,2);
	}
	//point represents bottom left just like upRStagerScan except last point of of loop is one above the usual to avoid timing pattern
	private static int upTimeStagerScan(int i, int j) {
		return upStagerScan(i,j,3)*4 + (msk(i-4,j)?2:0) + (msk(i-5,j+1)?1:0);
	}
	
	private static int leftDScanEdge(int i, int j) {
		return (msk(i,j)?128:0) + (msk(i-1,j+1)?64:0) + (msk(i-1,j)?32:0) + (msk(i-1,j-1)?16:0) + (msk(i-1,j-2)?8:0) + (msk(i,j-1)?4:0) + (msk(i,j-2)?2:0) + (msk(i+1,j-1)?1:0);
	}
	//point is top left and is identical to downStagerScan except last point is one lower in order to avoid timing pattern
	private static int downTimeStagerScan(int i, int j) {
		return downStagerScan(i,j,3)*4 + (msk(i+3,j)?2:0) + (msk(i+5,j+1)?1:0);
	}
	//point represents 2 point relative to leftUScan, first point is shifted to one above final point in original scan
	private static int leftStagerUScan(int i, int j) {
		return (msk(i,j)?128:0) + (msk(i+1,j+1)?64:0) + (msk(i+1,j)?32:0) + (msk(i+1,j-1)?16:0) + (msk(i+1,j-2)?8:0) + (msk(i,j-1)?4:0) + (msk(i,j-2)?2:0) + (msk(i-1,j-1)?1:0);
	}
	
	interface maskFunc {
        boolean m(int i, int j);
    }
	
	private static maskFunc[] mask = {
			//Mask 0
	        new maskFunc() {public boolean m(int i, int j){ return (i+j)%2 == 0;}},
	        //Mask 1
	        new maskFunc() {public boolean m(int i, int j){ return i%2 == 0;    }},
	        //Mask 2
	        new maskFunc() {public boolean m(int i, int j){ return j%3 == 0;    }},
	        //Mask 3
	        new maskFunc() {public boolean m(int i, int j){ return (i+j)%3 == 0;}},
	        //Mask 4
	        new maskFunc() {public boolean m(int i, int j){ return (i/2 + j/3)%2  == 0;}},
	        //Mask 5
	        new maskFunc() {public boolean m(int i, int j){ return (i*j)%2 + (i*j)%3 == 0;}},
	        //Mask 6
	        new maskFunc() {public boolean m(int i, int j){ return ((i*j)%3 + i*j)%2 == 0;}},
	        //Mask 7
	        new maskFunc() {public boolean m(int i, int j){ return ((i*j)%3+ i+j)%2 == 0;}},
	        //Test Mask (full grid)
	        new maskFunc() {public boolean m(int i, int j){ return true;}},
	        //Test Mask (empty grid)
	        new maskFunc() {public boolean m(int i, int j){ return false;}}
	};
	
	private static String encoding(int val) {
		if(val == 1) return "Numeric";
		if(val == 2) return "Alphanumeric";
		if(val == 4) return "Byte";
		if(val == 8) return "Kanji";
		if(val == 7) return "ECI";
		return "Unknown, assuming Byte";
	}
	
	private static String errorLevel(int val) {
		if(val ==0) return "M";
		if(val ==1) return "L";
		if(val ==2) return "H";
		if(val ==3) return "Q";
		return "Unknown, assuming L";
	}
	
	private static int characterCountIndicatorLength() {
		if(encMode == 1) {
			if(version > 26) return 14;
			if(version > 9) return 12;
			else return 10;
		}
		if(encMode == 2) {
			if(version > 26) return 13;
			if(version > 9) return 11;
			else return 9;
		}
		if(encMode == 4) {
			if(version > 26) return 16;
			if(version > 9) return 16;
			else return 8;
		}
		if(encMode == 8) {
			if(version > 26) return 12;
			if(version > 9) return 10;
			else return 8;
		}
		return 8;
	}
	
	private static boolean hasMarker() {
		if(qr[qr.length-7][qr.length-7]) return false;
		for(int i = qr.length-8; i < qr.length-5; i++) {
			if(!qr[i][qr.length-8]) return false;
			if(!qr[i][qr.length-6]) return false;
		}
		if(!qr[qr.length-8][qr.length-7] || !qr[qr.length-6][qr.length-7]) return false;
		for(int i = qr.length-9; i < qr.length-4; i++) {
			if(qr[i][qr.length-9]) return false;
			if(qr[i][qr.length-5]) return false;
		}
		for(int j = qr.length-8; j < qr.length-6; j++) {
			if(qr[qr.length-9][j]) return false;
			if(qr[qr.length-5][j]) return false;
		}
		return true;
	}
	
	public static boolean msk(int i, int j) {
		if(((i>8 && i<qr.length-8 && j!=6) ||
		   (j>8 && j<qr.length-8 && i!=6) ||
		   (j>qr.length-9 && i>qr.length-9)) &&
		   (!hasMarker() ||
		   (hasMarker() && (i< qr.length-9 || i>qr.length-5 || j< qr.length-9 || j>qr.length-5)))) {
				
			return qr[i][j] == mask[mskNum].m(i, j);
//			System.out.print(qr[i][j] == mask[index].m(i, j)?"||":"  ");
		}
		else {
			return !qr[i][j];
		}
	}
}