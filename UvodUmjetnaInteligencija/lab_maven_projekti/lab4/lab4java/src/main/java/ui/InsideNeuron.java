package ui;

public class InsideNeuron extends Neuron{	
	
	public InsideNeuron(W bias, W[] inputWeights, W[] outputWeights, double output, int layer) {
		super();
		this.bias = bias;
		this.inputWeights = inputWeights;
		this.outputWeights = outputWeights;
		this.output = output;
		this.layer = layer;
	}	
	
	public InsideNeuron(int layer, int indexInLayer) {
		this.layer = layer;
		this.indexInLayer = indexInLayer;
	}
}
