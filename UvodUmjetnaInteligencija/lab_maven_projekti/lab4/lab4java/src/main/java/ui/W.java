package ui;

import java.util.Objects;

public class W {
	int startNeuron;
	int targetNeuron;
	int layer;
	double value;
	
	public W(int startNeuron, int targetNeuron, int layer, double value) {
		super();
		this.startNeuron = startNeuron;
		this.targetNeuron = targetNeuron;
		this.layer = layer;
		this.value = value;
	}

	public int getStartNeuron() {
		return startNeuron;
	}

	public void setStartNeuron(int startNeuron) {
		this.startNeuron = startNeuron;
	}

	public int getTargetNeuron() {
		return targetNeuron;
	}

	public void setTargetNeuron(int targetNeuron) {
		this.targetNeuron = targetNeuron;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "W [startNeuron=" + startNeuron + ", targetNeuron=" + targetNeuron + ", layer=" + layer + ", value="
				+ value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(layer, startNeuron, targetNeuron);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		W other = (W) obj;
		return layer == other.layer && startNeuron == other.startNeuron && targetNeuron == other.targetNeuron;
	}
}
