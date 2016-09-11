import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JTriangleDetection {
	
	public static void main(String[] args) {
		//XORTest();
		//Test();	
		File testDataFile = new File("src/testSet/testData.csv");
		File trainDataFile = new File("src/trainingSet/trainingData.csv");

		if(!testDataFile.exists() && !trainDataFile.exists()) { 
			JDataSetBuilder.compileCSVImages();
		}	
		
		List<String> pixelArrays = new ArrayList<String>();		
		
		File columnNumbers = new File("src/trainingSet/trainingData.csv");
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
		
		double[][] imageArray = new double[pixelArrays.size()][100];
		double [] expected = new double[pixelArrays.size()];
		for(int g = 0; g < pixelArrays.size(); g++){
			
			String[] tempVals = pixelArrays.get(g).split(",");
			
			for(int h = 0; h < 100; h++){
				double pixel = Double.parseDouble(tempVals[h]);
				imageArray[g][h] = pixel;
			}
			expected[g] = Double.parseDouble(tempVals[101]);
		}
				
		
	    double momentum = 0.8;
	    double learnRate = 0.5;
	    int epochs = 1000;
		int[] inputs = new int[]{60};
	    int[] secondLayerInputs = new int[60];
	    for(int k = 0; k < secondLayerInputs.length; k++){
	    	secondLayerInputs[k] = 100;
	    }	    
	    
	    JImgRecogLayer outerLayer = new JImgRecogLayer(1,inputs,momentum,learnRate,null);    
	    
	    JImgRecogLayer upperLayer = new JImgRecogLayer(60, secondLayerInputs,momentum,learnRate,outerLayer);	
	    		
	    for (int j = 0; j < epochs; j++) {
	        for (int i = 0; i < pixelArrays.size(); i++) {
	            upperLayer.TrainLayer(imageArray[i], expected[i]);
	            upperLayer.ReassignLayerWeights(imageArray[i]);
	        }
	    }
	
	    for(int i = 0; i < pixelArrays.size(); i++){
	    	System.out.print(System.lineSeparator());
	    	System.out.print(System.lineSeparator());
	    	System.out.print("  Output:  ");	
	    	DecimalFormat df = new DecimalFormat("#.000");
	    	double[] out = upperLayer.GetOutputs(imageArray[i]);   		    	
	    	System.out.print(df.format(out[0]));
	    	System.out.print(System.lineSeparator());
	    	System.out.print("  Expected:  ");	
	    	System.out.print(expected[i]);
	    }
    	System.out.print(System.lineSeparator());
    	
    	System.out.print("TEST DATA");
    	System.out.print(System.lineSeparator());
    	System.out.print(System.lineSeparator());
    	
		pixelArrays = new ArrayList<String>();	
		File columnNumbers2 = new File("src/testSet/testData.csv");
		try {
			
			csvReadStream = new Scanner(columnNumbers2);
			csvReadStream.useDelimiter("\r?\n");
	        while(csvReadStream.hasNext()){
	            String lineData = csvReadStream.next();
	            pixelArrays.add(lineData);	            
	        }
	        csvReadStream.close();
	        
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		imageArray = new double[pixelArrays.size()][100];
		expected = new double[pixelArrays.size()];
		for(int g = 0; g < pixelArrays.size(); g++){
			
			String[] tempVals = pixelArrays.get(g).split(",");
			
			for(int h = 0; h < 100; h++){
				double pixel = Double.parseDouble(tempVals[h]);
				imageArray[g][h] = pixel;
			}
			expected[g] = Double.parseDouble(tempVals[101]);
		}
	    
	    
	    for(int i = 0; i < pixelArrays.size(); i++){
	    	System.out.print(System.lineSeparator());
	    	System.out.print(System.lineSeparator());
	    	System.out.print("  Output:  ");	
	    	DecimalFormat df = new DecimalFormat("#.000");
	    	double[] out = upperLayer.GetOutputs(imageArray[i]);   		    	
	    	System.out.print(df.format(out[0]));
	    	System.out.print(System.lineSeparator());
	    	System.out.print("  Expected:  ");	
	    	System.out.print(expected[i]);
	    }
	}
	
	public static void XORTest(){
	    double[][] data = new double[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
	    double[] expected = new double[] {0, 1, 1, 0};
	    
	    double momentum = 0.8;
	    double learnRate = 0.5;
	    int epochs = 1000;
		int[] inputs = new int[]{2};
	    int[] secondLayerInputs = new int[]{2,2};
	    JImgRecogLayer outerLayer = new JImgRecogLayer(1,inputs,momentum,learnRate,null);
	    JImgRecogLayer upperLayer = new JImgRecogLayer(2, secondLayerInputs,momentum,learnRate,outerLayer);	
	    		

	    for (int j = 0; j < epochs; j++) {
	        for (int i = 0; i < 4; i++) {
	            upperLayer.TrainLayer(data[i], expected[i]);
	            upperLayer.ReassignLayerWeights(data[i]);
	        }
	    }
	
	    for(int i = 0; i < 4; i++){
	    	System.out.print(System.lineSeparator());
	    	System.out.print(" Inputs: ");
	    	System.out.print(data[i][0]);
	    	System.out.print(" ");
	    	System.out.print(data[i][1]);
	    	System.out.print(System.lineSeparator());
	    	System.out.print("  Outputs:  ");	
	    	double[] out = upperLayer.GetOutputs(data[i]);   		    	
	    	System.out.print(out[0]);
	    }
	}


}
