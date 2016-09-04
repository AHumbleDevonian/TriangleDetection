
public class JImgRecogLayer {
	private int nodes;
	private double momentum;
	private double learningRate;
	private double[] outputs;
	private JImgRecogLayer outputLayer;
	private JImgRecogNode[] layerNodes;

	public JImgRecogLayer(int nodeCount, int[] inputsPerNode,  double momentumValue, double learningRateValue, JImgRecogLayer nextLayer){
		nodes = nodeCount;
		momentum = momentumValue;
		learningRate = learningRateValue;
		if(nextLayer != null){
			outputLayer = nextLayer;
		}
		layerNodes = new JImgRecogNode[nodes];
	    for (int i = 0; i < nodes; i++) {
	        layerNodes[i] = new JImgRecogNode(inputsPerNode[i], momentum, learningRate);
	        layerNodes[i].SetRandomWeights();
	    }
	}
	
	public int GetNodes(){
		return nodes;
	}
	
	public double[] GetOutputs(){
		return outputs;
	}
	
	public JImgRecogNode GetNode(int nodePosition){
		return layerNodes[nodePosition];
	}
	
	public void TrainLayer(double[] inputData, double expectedValue){
        outputs = new double[nodes];
        for (int i = 0; i < nodes; i++) {
            layerNodes[i].GenerateOutputs(inputData);
            outputs[i] = layerNodes[i].SigmoidActivation();
        }
        if (outputLayer != null) {
            outputLayer.TrainLayer(outputs, expectedValue);
        }
        for (int i = 0; i < nodes; i++) {
            if (outputLayer != null) {
                double hiddenErrors = 0;
                for (int j = 0; j < outputLayer.GetNodes(); j++) {
                    hiddenErrors = hiddenErrors + outputLayer.GetNode(j).GetWeightedDelta(i);
                }
                layerNodes[i].GenerateDelta(hiddenErrors, layerNodes[i].SigmoidActivation());
            }
            else {
                layerNodes[i].GenerateError(layerNodes[i].SigmoidActivation(), expectedValue);
                layerNodes[i].GenerateDelta(layerNodes[i].GetError(), layerNodes[i].SigmoidActivation());
            }
        }
	}
	
	public void ReassignLayerWeights(double[] inputData){
        if (outputLayer != null) {
            outputLayer.ReassignLayerWeights(outputs);
        }
        for (int i = 0; i < nodes; i++) {
            layerNodes[i].GenerateWeightOffset(inputData);
            layerNodes[i].ReassignWeights();
        }
	}

	public double[] GetOutputs(double[] inputData){
        for (int i = 0; i < nodes; i++) {
            layerNodes[i].GenerateOutputs(inputData);
            outputs[i] = layerNodes[i].SigmoidActivation();
        }
        if (outputLayer != null) {
            return outputLayer.GetOutputs(outputs);            
        }
        return outputs;
	}

}
	
