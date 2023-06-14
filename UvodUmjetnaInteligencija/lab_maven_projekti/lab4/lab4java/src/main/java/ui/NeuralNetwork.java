package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {
	List<Layer> layers;
	
	List<String> attributes;
	List<Zapis> trainingData;
	
	double err = 0;
	
	Random random = new Random();
	
	public NeuralNetwork(String train, String nn) {
		try {
			init(train, nn);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		layers = new LinkedList<>();
		
		setLayers(nn);
		
		setWeights(null, null);
	
		err = calculateErr(trainingData);
	}
	
	public NeuralNetwork(List<String> attributes, List<Zapis> trainingData, String nn, List<W> weights, List<W> bias) {
		this.attributes = attributes;
		this.trainingData = trainingData;
		layers = new LinkedList<>();
		
		setLayers(nn);
		
		setWeights(weights, bias);
		
		err = calculateErr(trainingData);
	}
	
	private double calculateErr(List<Zapis> trainingData) {
		int N = trainingData.size();
		
		double tmpRez = 0;
		for(int i = 0; i < N; i++) {
			double y = forwardPass(trainingData.get(i));
			double rez = y - trainingData.get(i).getCilj();
			rez = Math.pow(rez, 2);
			tmpRez += rez;
		}
		
		return tmpRez / N;
	}

	public double forwardPass(Zapis X) {
		//map Xi to input neurons
		Layer layer = layers.get(0);
		for(int i = 1; i <= X.getVrijednosti().size(); i++) {
			Double value = X.getVrijednosti().get(i - 1);
			
			for(Neuron neuron : layer.getNeurons()) {
				if(neuron.getIndexInLayer() == i){
					neuron.setOutput(value);
				}
			}
		}
		double tmp = 0;
		//forward pass through inner neurons
		for(int i = 1; i < layers.size(); i++) {
			layer = layers.get(i);
			
			for(int j = 0; j < layer.getNeurons().size(); j++) {
				Neuron neuron = layer.getNeurons().get(j);
				tmp = neuron.getBias().getValue();
				for(W weight : neuron.getInputWeights()) {
					Neuron left = getNeuron(i - 1, weight.getStartNeuron());
					if(left != null) {
						tmp += weight.getValue() * left.getOutput();
					}
				}
				if(i != layers.size() -1) {
					tmp = sigm(tmp);
				}
				neuron.setOutput(tmp);
			}
		}
		
		return tmp;
	}

	private double sigm(double tmp) {
		return 1 / (1 + Math.exp((-1) * tmp));
	}

	private Neuron getNeuron(int l, int indexNeuron) {
		Layer layer = layers.get(l);
		for(int i = 0; i < layer.getNeurons().size(); i++) {
			if(layer.getNeurons().get(i).getIndexInLayer() == indexNeuron) {
				return layer.getNeurons().get(i);
			}
		}
		return null;
	}

	private void setWeights(List<W> weights, List<W> bias) {
		
		for(int i = 0; i < layers.size()-1; i++) {
			int neighbourNeurons = layers.get(i + 1).getNeurons().size();
			Layer layer = layers.get(i);
			
			if(i == 0) {
				for(Neuron neuron : layer.getNeurons()) {
					neuron.setBias(new W(0, 0, 0, 0));
					neuron.setOutput(0);
					neuron.setInputWeights(new W[0]);
					
					neuron.setOutputWeights(new W[neighbourNeurons]);
					for(int j = 1; j <= neighbourNeurons; j++) {
						W weight = new W(neuron.indexInLayer, j, i + 1, random.nextGaussian() * 0.01);
						
						if(weights != null) {
							double valueChild = weights.stream().filter(e -> e.equals(weight)).findFirst().get().getValue();
							weight.setValue(valueChild);
						}
						
						neuron.outputWeights[j - 1] = weight;
					}
				}
			}else {
				int leftNeighbourNumber = layers.get(i - 1).getNeurons().size();
				int rightNeighbourNumber = layers.get(i + 1).getNeurons().size();
				
				
				for(Neuron neuron : layer.getNeurons()) {
					
					W bs = new W(0, neuron.indexInLayer, i, random.nextGaussian() * 0.01);
					
					if(bias != null) {
						double valueBias = bias.stream().filter(e -> e.equals(bs)).findFirst().get().getValue();
						bs.setValue(valueBias);
					}
					neuron.setBias(bs);
					
					neuron.setInputWeights(new W[leftNeighbourNumber]);
			
					int tmp = 0;
					for(Neuron left : layers.get(i - 1).getNeurons()) {
						for(W weight : left.getOutputWeights()) {
							if(weight.targetNeuron == neuron.indexInLayer) {
								neuron.inputWeights[tmp++] = weight; 
							}
						}
					}
					
					neuron.setOutputWeights(new W[rightNeighbourNumber]);
					for(int j = 1; j <= rightNeighbourNumber; j++) {
						
						W weight = new W(neuron.indexInLayer, j, i + 1, random.nextGaussian() * 0.01);
						
						if(weights != null) {
							double valueChild = weights.stream().filter(e -> e.equals(weight)).findFirst().get().getValue();
							weight.setValue(valueChild);
						}
						
						neuron.outputWeights[j - 1] = weight;
					}
				}
			}
		}
		
		//weights in output layer
		Layer outputLayer = layers.get(layers.size() - 1);
		for(Neuron neuron : outputLayer.getNeurons()) {
			
			W bs = new W(0, neuron.indexInLayer, layers.size() - 1, random.nextGaussian() * 0.01);
			
			if(bias != null) {
				double valueBias = bias.stream().filter(e -> e.equals(bs)).findFirst().get().getValue();
				bs.setValue(valueBias);
			}
			neuron.setBias(bs);
			
			neuron.setOutputWeights(new W[0]);
			
			
			Layer left = layers.get(layers.size() -2);
			int leftNeighbourNumber = left.getNeurons().size();
			
			neuron.setInputWeights(new W[leftNeighbourNumber]);
			int tmp = 0;
			for(Neuron leftNeighbour : left.getNeurons()) {
				for(W weight : leftNeighbour.getOutputWeights()) {
					if(weight.targetNeuron == neuron.indexInLayer) {
						neuron.inputWeights[tmp++] = weight; 
					}
				}
			}
		}
	}

	private void setLayers(String nn) {
		int layerCounter = 0;
		
		//set input layer
		int numberOfInputNeurons = attributes.size();
		List<Neuron> inputNeurons = new LinkedList<>();
		for(int i = 1; i <= numberOfInputNeurons; i++) {
			InputNeuron in = new InputNeuron(layerCounter, i);
			inputNeurons.add(in);
		}
		Layer inputLayer = new Layer(layerCounter++, inputNeurons);
		layers.add(inputLayer);
		
		//set inside layers
		for(String el : nn.split("s")) {
			int neuronsInLayer = Integer.parseInt(el);
			
			List<Neuron> insideNeurons = new LinkedList<>();
			for(int i = 1; i <= neuronsInLayer; i++) {
				InsideNeuron in = new InsideNeuron(layerCounter, i);
				insideNeurons.add(in);
			}
			Layer layer = new Layer(layerCounter++, insideNeurons);
			layers.add(layer);
		}
		
		//set output layer
		List<Neuron> outputNeuron = new LinkedList<>();
		outputNeuron.add(new OutputNeuron(layerCounter, 1));
		Layer layer = new Layer(layerCounter++, outputNeuron);
		layers.add(layer);
	}

	private void init(String train, String nn) throws NumberFormatException, IOException {
		InputStream is = new FileInputStream(new File(train));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		boolean prvi = false;
		String line;
		attributes = new LinkedList<>();
		trainingData = new LinkedList<>();
		
		while((line = br.readLine()) != null) {
			String[] red = line.split(",");
			
			if(!prvi) {
				for(int i = 0; i < red.length - 1; i++) {
					attributes.add(red[i]);
				}
				prvi = true;
			}else {
				List<Double> vrijednosti = new LinkedList<>();
				
				for(int i = 0; i < red.length - 1; i++) {
					vrijednosti.add(Double.parseDouble(red[i]));
				}
				
				trainingData.add(new Zapis(vrijednosti,Double.parseDouble(red[red.length - 1])));
			}
		}
	}
	
	public List<W> getAllWeights(){
		List<W> rez = new ArrayList<>();
		for(Layer layer : layers) {
			for(Neuron neuron : layer.getNeurons()) {
				rez.addAll(Arrays.asList(neuron.getOutputWeights()));
			}
		}
		
		return rez;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public List<Zapis> getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(List<Zapis> trainingData) {
		this.trainingData = trainingData;
	}

	public double getErr() {
		return err;
	}

	public void setErr(double err) {
		this.err = err;
	}

	public List<W> getBiases() {
		List<W> rez = new ArrayList<>();
		for(Layer layer : layers) {
			for(Neuron neuron : layer.getNeurons()) {
				rez.add(neuron.getBias());
			}
		}
		
		return rez;
	}
	
	
}
