import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class ImagePPM {

	private String magicNumber;
	private String comment;
	private int width;
	private int height;
	private int maxColor;
	private pixel[][] pixels;

	/* Read File 
	 * @param String inputFile
	 * This will read the file and set all the variables in this class
	 * @return void
	 * @throws fileNotFound exception
	 */
	public void readFile(String inputFile) {
		try {
			Scanner sc = new Scanner(new File(inputFile));
			this.magicNumber = sc.nextLine();
			String tmp = sc.nextLine();
			if (tmp.startsWith("#")) {
				this.comment = tmp;
				this.width = sc.nextInt();
				this.height = sc.nextInt();
			} else {
				String[] tmpString = tmp.split("\\s");
				this.width = Integer.parseInt(tmpString[0]);
				this.height = Integer.parseInt(tmpString[1]);
			}
			this.maxColor = sc.nextInt();
			this.pixels = new pixel[this.height][this.width];
			for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					/*if (sc.hasNext("#")) {
						//System.out.println("Hash found");
						sc.nextLine();
						sc.nextLine();
					}*/
					pixel dot = new pixel();
					dot.r = sc.nextInt();
					dot.g = sc.nextInt();
					dot.b = sc.nextInt();
					pixels[i][j] = dot;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}



	/* Write File
	 * @param String outputFile
	 * @return void
	 */
	public void writeFile(String outputFile) {
		try {
			FileWriter fs = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fs);
			out.write(this.magicNumber);
			out.newLine();
			out.write(this.width + " " + this.height);
			out.newLine();
			out.write(Integer.toString(this.maxColor));
			out.newLine();
			for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					out.write(pixels[i][j].r + " " + pixels[i][j].g + " " + pixels[i][j].b + "  ");
				}
				out.newLine();
			}
			out.close();
		} catch (Exception e) {
			System.out.println("Error Writing");
		}
	}

	/* Invert
	 *
	 */
	public void invert() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				pixels[i][j].r = maxColor - pixels[i][j].r;	
				pixels[i][j].g = maxColor - pixels[i][j].g;	
				pixels[i][j].b = maxColor - pixels[i][j].b;
			}
		}
	}

	/* GreyScale
	 *
	 */
	public void grayscale() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				int r = pixels[i][j].r;
				int g = pixels[i][j].g;
				int b = pixels[i][j].b;
				int avg = (r + g + b)/3;
				pixels[i][j].r = avg;
				pixels[i][j].g = avg;
				pixels[i][j].b = avg;
			}
		}
	}

	/* Emboss
	 *
	 */
	public void emboss() {
		for (int i = this.height-1; i >= 0; i--) {
			for (int j = this.width-1; j >= 0; j--) {
				//System.out.println("i="+i+" j="+j);
				if (j == 0 || i == 0) {
					pixels[i][j].r = maxColor / 2 + 1;
					pixels[i][j].g = maxColor / 2 + 1;
					pixels[i][j].b = maxColor / 2 + 1;
				} else {
					int redDiff = (pixels[i][j].r - pixels[i-1][j-1].r);
					int greenDiff = (pixels[i][j].g - pixels[i-1][j-1].g);
					int blueDiff = (pixels[i][j].b - pixels[i-1][j-1].b);
					int maxDiff = redDiff;
					if (Math.abs(greenDiff) > Math.abs(maxDiff)) {
						maxDiff = greenDiff;
					} 
					if (Math.abs(blueDiff) > Math.abs(maxDiff)) {
						maxDiff = blueDiff;
					}
					int v = 128 + maxDiff;
					if (v < 0 ) {
						v = 0;
					} else if (v > maxColor) {
						v = maxColor;
					}			
					pixels[i][j].r = v;
					pixels[i][j].g = v;
					pixels[i][j].b = v;
				}
			}
		}
    pixels[0][0].r = maxColor / 2 + 1;
    pixels[0][0].g = maxColor / 2 + 1;
    pixels[0][0].b = maxColor / 2 + 1;
	}

	/* Motionblur
	 *
	 */
	public void motionblur(int num) {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				int avgR, avgG, avgB;
				avgR = avgG = avgB = 0;
				int tmp = num;
        if (j+num > this.width) {
          tmp = num -(j+num-this.width);
        }
        for (int k = 0; k <= tmp-1; k++) {
          avgR += pixels[i][j+k].r;
          avgG += pixels[i][j+k].g;
          avgB += pixels[i][j+k].b;
        }
        if (tmp != 0) {
          pixels[i][j].r = avgR/tmp;
          pixels[i][j].g = avgG/tmp;
          pixels[i][j].b = avgB/tmp;
        } else {
          pixels[i][j].r = avgR;
          pixels[i][j].g = avgG;
          pixels[i][j].b = avgB;
        } 
//        System.out.println(avgR + " " + avgG + " " + avgB); 
        /*int tmp = 0;
				for (int k = 0; k < num-1; k++) {
					if (j+num >= this.width) {
						tmp++;
					}
					avgR += pixels[i][j+k-tmp].r;
					avgG += pixels[i][j+k-tmp].g;
					avgB += pixels[i][j+k-tmp].b;
					
				}
				pixels[i][j].r = avgR/(num-tmp);
				pixels[i][j].g = avgG/(num-tmp);
				pixels[i][j].b = avgB/(num-tmp);

        */
			}
		}
	}

	public void print() {
		System.out.println(this.magicNumber);
		System.out.println(this.comment);
		System.out.println(this.width + " " + this.height);
		System.out.println(this.maxColor);
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				System.out.print(pixels[i][j].r + " " + pixels[i][j].g + " " + pixels[i][j].b + "  ");
			}
			System.out.println();
		}
	}

}
