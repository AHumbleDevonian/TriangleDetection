import java.util.Random;

public class JImgRecogNode {
	private int inputs;
	private double momentum;
	private double learningRate;
	private double delta;
	private double error;
	private double[] weights;
	private double[] outputs;
	private double[] weightOffsets;
	
	public JImgRecogNode(int inputCount, double momentumValue, double learningRateValue){
		inputs = inputCount;
		momentum = momentumValue;
		learningRate = learningRateValue;
		
		weights = new double[(inputs + 1)];
		outputs = new double[(inputs + 1)];
		weightOffsets = new double[(inputs + 1)];
		
	}	
	
	public void SetInputs(int inputCount){
		inputs = inputCount;
	}

	public void SetMomentum(int momentumValue){
		momentum = momentumValue;
	}
	
	public void SetLearnRate(int learningRateValue){
		learningRate = learningRateValue;
	}
	
	public int GetInputs(){
		return inputs;
	}
	
	public double GetMomentum(){
		return momentum;
	}
	
	public double GetLearnRate(){
		return learningRate;
	}
	
	public double[] GetWeights(){
		return weights;
	}
	
	public void SetWeights(double[] inputWeights){
		weights = inputWeights;
	}
	
	public void SetRandomWeights(){
		Random random = new Random();
		for(int i = 0; i < (inputs + 1); i++){
			weights[i] = random.nextDouble() ;
		}
	}
	
	public double[] GenerateOutputs(double[] inputData){
        outputs = new double[(inputs + 1)];
        for (int i = 0; i < (inputs); i++) {
            outputs[i] = (inputData[i] * weights[i]);
        }
        outputs[inputs] = (1 * weights[inputs]);
		return outputs;
	}
	
	public double[] GetOutputs(){
		return outputs;
	}
	
	public double GetDelta(){
		return delta;
	}
	
    public double GetWeightedDelta(int inputWeight) {
        return (delta * outputs[inputWeight]);
    }
	
	public double GetError(){
		return error;
	}

	public void SetWeightOffsets(double[] offsetValues){
		weightOffsets = offsetValues;
	}
	
	public double[] GetWeightOffsets(){
		return weightOffsets;
	}
	
	public void GenerateError(double outputValue, double expectedValue){
		error = (expectedValue - outputValue);
	}
	
	public void GenerateDelta(double outputError, double outputValue){
		delta = (outputError * outputValue * (1 - outputValue));
	}
	
	public double SigmoidActivation(){
		double sumOutputs = 0;
	    for (int i = 0; i < (inputs +1); i++) {
	        sumOutputs = sumOutputs + outputs[i];
	    }
	    double sigmoidOutput = (1 / (1 + Math.exp(-sumOutputs)));
	    return sigmoidOutput;
	}

	public void ReassignWeights(){
	    for (int i = 0; i < (inputs +1) ; i++) {
	        weights[i] = (weights[i] + weightOffsets[i]);
	    }
	}

	public void GenerateWeightOffset(double[] inputData){
	    for (int i = 0; i < (inputs) ; i++) {
	        weightOffsets[i] = ((weightOffsets[i] * momentum) + (inputData[i] * learningRate * delta));
	    }
	    weightOffsets[inputs] = ((weightOffsets[(inputs - 1)] * momentum) + (1 * learningRate * delta));
	}	
	
}
