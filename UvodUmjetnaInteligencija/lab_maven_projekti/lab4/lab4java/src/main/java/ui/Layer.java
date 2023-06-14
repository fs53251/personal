package ui;

import java.util.List;

public class Layer {
	int index;
	List<Neuron> neurons; 
	
	public Layer(int index, List<Neuron> neurons) {
		super();
		this.index = index;
		this.neurons = neurons;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(List<Neuron> neurons) {
		this.neurons = neurons;
	}

	@Override
	public String toString() {
		return "Layer [index=" + index + ", neurons=" + neurons + "]";
	}
	
}
