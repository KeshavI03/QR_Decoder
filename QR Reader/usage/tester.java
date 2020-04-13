import java.io.IOException;

public class tester {

	public static void main(String[] args) throws IOException { 
		qrCode code = new qrCode("qr19");
//		System.out.println("Loading Image");
			
		code.loadImage();
			
//		System.out.println("Loaded Image");
//		System.out.println("Width: " + code.getImageWidth() + " Height: " + code.getImageHeight());
//		System.out.println("Decoding Image");
			
		code.decodeImage();
			
//		System.out.println("Decoded Image");
//		System.out.println("Nanoseconds: " + code.getPerformNano());
//		System.out.println(code.getQRDimmensions() + " x " + code.getQRDimmensions());
//		System.out.println("Extracted QR Code:\n");
			
//		code.displayQRString();
			
//		System.out.println("Drawing Result Image");
			
		code.drawImage();
		code.drawQRImage(15);
			
//		System.out.println("Drew Result Image/n/n");
		code.decodeQr();
	}

}