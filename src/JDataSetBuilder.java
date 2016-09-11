import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class JDataSetBuilder {
	private static File inputFile;
	private static BufferedImage image;
	private static int[][] imageArray;
	
	public static void compileCSVImages(){
		inputFile = new File("src/template.bmp");

		List<String> pixelArrays = new ArrayList<String>();
		try {
			image = ImageIO.read(inputFile);
		}
		catch(IOException e){			
		}
		
		File columnNumbers = new File("src/noise.csv");
        Scanner csvReadStream;
		try {
			
			csvReadStream = new Scanner(columnNumbers);
			csvReadStream.useDelimiter("\r?\n");
	        while(csvReadStream.hasNext()){
	            String lineData = csvReadStream.next();
	            pixelArrays.add(lineData);	            
	        }
	        csvReadStream.close();
	        
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		//createCSV(pixelArrays);
		File trainingSetfolder = new File("src/trainingSet");
		File testSetFolder = new File("src/testSet");
		//createImage(pixelArrays, image);
		readImages(trainingSetfolder, "src/trainingSet/trainingData.csv");
		readImages(testSetFolder, "src/testSet/testData.csv");
		//createNoiseCSV(pixelArrays,"src/testSet/testData.csv", "src/trainingSet/trainingData.csv");
	}
	
	private static void readImages(File dataSetFolder, String fileName){

		File[] dataSetFiles = dataSetFolder.listFiles();
		
		BufferedImage readImage = null;
	    try {
			PrintWriter csvFileWriter = new PrintWriter(
			new FileOutputStream(new File(fileName), true)); 
			for(int i = 0; i < dataSetFiles.length; i++){
			if(!dataSetFiles[i].getName().toLowerCase().contains(".csv")){
			imageArray = new int[10][10];			
			try {

				readImage = ImageIO.read(dataSetFiles[i]);
				for(int j = 0; j < 10; j++){
					for(int k = 0; k < 10; k++){
						if(readImage.getRGB(k, j) != -1){
							imageArray[j][k] = 1;
						}
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int l = 0; l < 10; l++){
				for(int m = 0; m < 10; m++){
					String pixelChars =  Integer.toString(imageArray[l][m]) + ',';
			        csvFileWriter.write(pixelChars);
				}
			}
			csvFileWriter.write(',');
			if(dataSetFiles[i].getName().toLowerCase().contains("a.bmp")){
				csvFileWriter.write("0,");
			}
			else{
				csvFileWriter.write("1,");
			}			
			csvFileWriter.write('\n');
			}
		}
		csvFileWriter.close();
		} catch (FileNotFoundException e1) {
			PrintWriter csvFileWriter;
			try {
				csvFileWriter = new PrintWriter(new File(fileName));
			for(int i = 0; i <dataSetFiles.length; i++){
			imageArray = new int[10][10];
			try {
				readImage = ImageIO.read(dataSetFiles[i]);
				for(int j = 0; j < 10; j++){
					for(int k = 0; k < 10; k++){
						if(readImage.getRGB(k, j) != -1){
							imageArray[j][k] = 1;
						}
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int l = 0; l < 10; l++){
				
				for(int m = 0; m < 10; m++){
					String pixelChars =  Integer.toString(imageArray[l][m]) + ',';
			        csvFileWriter.write(pixelChars);
				}
			}
			csvFileWriter.write('\n');
		}
			} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		}   
	}
	
	private static void createImage(List<String> pixelArrays, BufferedImage image){
		for(int g = 0; g < pixelArrays.size(); g++){

			String[] tempVals = pixelArrays.get(g).split(",");
			BufferedImage image2 = cloneImage(image);	
			
			for(int h = 0; h < tempVals.length; h++){
				int pixel = Integer.parseInt(tempVals[h]);
				int pixelX = pixel % 10;
				int pixelY = (pixel - (pixel % 10)) / 10;
				
				if(pixelX > -1 && pixelX < 10 && pixelY > -1 && pixelY < 10){
					image2.setRGB(pixelX, pixelY, 167772161);
				}	            				
				try {
					String Path = "src/";
					if((g %10) == 0){
						ImageIO.write(image2, "BMP", new File(Path + "testSet/"+ g +"a.bmp"));
					}
					else{
						ImageIO.write(image2, "BMP", new File(Path + "trainingSet/"+ g +"a.bmp"));
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void createNoiseCSV(List<String> pixelArrays, String fileNameTestSet, String fileNameTrainingSet){
		File testSetFile = new File(fileNameTestSet);
		File trainSetFile = new File(fileNameTrainingSet);
		boolean isTestSet = false;
		if(!testSetFile.exists()) { 
			PrintWriter createNewFile;
			try {
				createNewFile = new PrintWriter(new File(fileNameTestSet));
				createNewFile.write("");
				createNewFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		if(!trainSetFile.exists()) { 
			PrintWriter createNewFile;
			try {
				createNewFile = new PrintWriter(new File(fileNameTrainingSet));
				createNewFile.write("");
				createNewFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}	
		
	    try {
			PrintWriter csvTestSetFileWriter = new PrintWriter(
			new FileOutputStream(new File(fileNameTestSet),true)); 
			PrintWriter csvTrainingSetFileWriter = new PrintWriter(
			new FileOutputStream(new File(fileNameTrainingSet),true)); 
			for(int g = 0; g < pixelArrays.size(); g++){
				imageArray = new int[10][10];
				String[] tempVals = pixelArrays.get(g).split(",");				
				for(int h = 0; h < tempVals.length; h++){
					int pixel = Integer.parseInt(tempVals[h]);
					int pixelX = pixel % 10;
					int pixelY = (pixel - (pixel % 10)) / 10;
					if(pixelX > -1 && pixelX < 10 && pixelY > -1 && pixelY < 10){
						imageArray[pixelY][pixelX] = 1;
					}	
				}
				
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						String pixelChars =  Integer.toString(imageArray[i][j]) + ',';
						if((g %10) == 0){
							csvTestSetFileWriter.write(pixelChars);
							isTestSet = true;
						}
						else{
							csvTrainingSetFileWriter.write(pixelChars);
							isTestSet = false;
						}
					}
				}
				if(isTestSet){
					csvTestSetFileWriter.write('\n');
				}
				else{
					csvTrainingSetFileWriter.write('\n');
				}
				isTestSet = false;
			}

			csvTestSetFileWriter.close();
			csvTrainingSetFileWriter.close();				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private static BufferedImage cloneImage(BufferedImage startImage) {
	    ColorModel cm = startImage.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = startImage.copyData(startImage.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
}
