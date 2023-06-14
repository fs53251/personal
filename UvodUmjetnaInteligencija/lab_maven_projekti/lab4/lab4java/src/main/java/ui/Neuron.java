package ui;

import java.util.Arrays;

public abstract class Neuron {
	W bias;
	W[] inputWeights;
	W[] outputWeights;
	double output;
	int layer;
	int indexInLayer;
	
	public int getIndexInLayer() {
		return indexInLayer;
	}
	public void setIndexInLayer(int indexInLayer) {
		this.indexInLayer = indexInLayer;
	}
	public W getBias() {
		return bias;
	}
	public void setBias(W bias) {
		this.bias = bias;
	}
	public W[] getInputWeights() {
		return inputWeights;
	}
	public void setInputWeights(W[] inputWeights) {
		this.inputWeights = inputWeights;
	}
	public W[] getOutputWeights() {
		return outputWeights;
	}
	public void setOutputWeights(W[] outputWeights) {
		this.outputWeights = outputWeights;
	}
	public double getOutput() {
		return output;
	}
	public void setOutput(double output) {
		this.output = output;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	@Override
	public String toString() {
		return "Neuron [bias=" + bias + ", inputWeights=" + Arrays.toString(inputWeights) + ", outputWeights="
				+ Arrays.toString(outputWeights) + ", output=" + output + ", layer=" + layer + ", indexInLayer="
				+ indexInLayer + "]";
	}

	
}
