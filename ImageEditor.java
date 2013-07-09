

public class ImageEditor {


	public static void main(String[] args){
		int num = 1;
		int argLength = args.length;
		String inputFile, outputFile, conversionType;
		if (argLength > 4 || argLength < 3) {
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
			System.exit(-9);
		}
		if (argLength == 4) {
			num = Integer.parseInt(args[3]);
		}
		inputFile = args[0];
		outputFile = args[1];
		conversionType = args[2].toLowerCase();

		ImagePPM image = new ImagePPM();
		
		image.readFile(inputFile);
		
		switch (conversionType) {
			case "invert":
				image.invert();		
				break;
			case "grayscale":
				image.grayscale();
				break;
			case "emboss":
				image.emboss();
				break;
			case "motionblur":
				if (argLength != 4) {
					System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
					System.exit(-9);
				}
				image.motionblur(num);
				break;
			default:
				System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
				System.exit(-9);
		}

		image.writeFile(outputFile);

	}


}
