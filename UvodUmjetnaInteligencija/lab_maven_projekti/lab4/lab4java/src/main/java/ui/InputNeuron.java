package ui;

public class InputNeuron extends Neuron{
	
	public InputNeuron(W[] outputWeights, int layer) {
		this.outputWeights = outputWeights;
		this.layer = layer;
	}	
	
	public InputNeuron(int layer, int indexInLayer) {
		this.layer = layer;
		this.indexInLayer = indexInLayer;
	}
}
