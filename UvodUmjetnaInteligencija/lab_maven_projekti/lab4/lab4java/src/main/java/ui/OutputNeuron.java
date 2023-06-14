package ui;

public class OutputNeuron extends Neuron{
	
	public OutputNeuron(W bias, W[] inputWeights, double output, int layer) {
		super();
		this.bias = bias;
		this.inputWeights = inputWeights;
		this.output = output;
		this.layer = layer;
	}
	
	public OutputNeuron(int layer, int indexInLayer) {
		this.layer = layer;
		this.indexInLayer = indexInLayer;
	}
}
