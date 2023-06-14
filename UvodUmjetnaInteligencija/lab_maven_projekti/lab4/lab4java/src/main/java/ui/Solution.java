package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Solution {	
	public static void main(String[] args) {
		Random random = new Random();
		String train = null;
	    String test = null;
	    String nn = null;
	    int popsize = 0;
	    int elitism = 0;
	    double p = 0;
	    double K = 0;
	    int iter = 0;

	    for (int i = 0; i < args.length; i++) {
	        if (args[i].equals("--train")) {
	            train = args[i + 1];
	        } else if (args[i].equals("--test")) {
	            test = args[i + 1];
	        } else if (args[i].equals("--nn")) {
	            nn = args[i + 1];
	        } else if (args[i].equals("--popsize")) {
	            popsize = Integer.parseInt(args[i + 1]);
	        } else if (args[i].equals("--elitism")) {
	            elitism = Integer.parseInt(args[i + 1]);
	        } else if (args[i].equals("--p")) {
	            p = Double.parseDouble(args[i + 1]);
	        } else if (args[i].equals("--K")) {
	            K = Double.parseDouble(args[i + 1]);
	        } else if (args[i].equals("--iter")) {
	            iter = Integer.parseInt(args[i + 1]);
	        }
	    }
	    
	    //Generiraj slucajnu populaciju mozgova od popsize jedinki; evaluiraj svaki.
	    List<NeuralNetwork> population = new ArrayList<>();
	    for(int i = 0; i < popsize; i++) {
	    	population.add(new NeuralNetwork(train, nn));
	    }
	    
	    //population.stream().mapToDouble(e -> e.err).sorted().forEach(System.out::println);
	    
	    //Ponavljaj dok nije kraj:
	    for(int i = 1; i <= iter; i++) {
	    	//Inicijaliziraj pomocnu populaciju na praznu.
	    	List<NeuralNetwork> tmpPopulation = new ArrayList<>();
	    	
	    	//provedi elitizam
	    	List<NeuralNetwork> elitePopulation  = maxElitism(population, elitism);
	    	
	    	if(i % 2000 == 0) {
	    		System.out.printf("[Train error @%d]: %.6f\n", i, elitePopulation.get(0).getErr());
	    	}
	    	
	    	if(elitePopulation.size() + tmpPopulation.size() <= popsize) {
	    		tmpPopulation.addAll(elitePopulation);
	    	}else {
	    		for(int j = 0; j < (popsize - tmpPopulation.size()); j++) {
	    			elitePopulation.add(elitePopulation.get(j));
	    		}
	    	}
	    	
	    	//Ponavljaj dok velicina pomocne populacije ne postane jednaka velicini populacije roditelja
	    	while(tmpPopulation.size() != population.size()) {
	    		Pair<NeuralNetwork, NeuralNetwork> parents = getParents(population);
	    		
	    		NeuralNetwork parent1 = parents.getFirst();
	    		NeuralNetwork parent2 = parents.getSecond();
	    		
	    		List<W> weights1 = parent1.getAllWeights();
	    		List<W> weights2 = parent2.getAllWeights();	
	    		
	    		List<W> bias1 = parent1.getBiases();
	    		List<W> bias2 = parent2.getBiases();
	    		
	    		List<W> childBias = cross(bias1, bias2);
	    		
	    		//Dijete = Križaj roditelje + Mutacija
	    		List<W> childWeights = cross(weights1, weights2);
	    		childWeights = mutate(childWeights, p, K, random);
	    		childBias = mutate(childBias, p, K, random);
	    		NeuralNetwork child = new NeuralNetwork(parent1.getAttributes(), parent1.getTrainingData(), nn, childWeights, childBias);
	    		
	    		tmpPopulation.add(child);
	    	}
	    	
	    	population = tmpPopulation;
	    }
	   
	    
	    //Generiraj slucajnu populaciju mozgova od popsize jedinki; evaluiraj svaki.
	    population = new ArrayList<>();
	    for(int i = 0; i < popsize; i++) {
	    	population.add(new NeuralNetwork(test, nn));
	    }
	    
	    //population.stream().mapToDouble(e -> e.err).sorted().forEach(System.out::println);
	    
	    //Ponavljaj dok nije kraj:
	    for(int i = 1; i <= iter + 1; i++) {
	    	//Inicijaliziraj pomocnu populaciju na praznu.
	    	List<NeuralNetwork> tmpPopulation = new ArrayList<>();
	    	
	    	//provedi elitizam
	    	List<NeuralNetwork> elitePopulation  = maxElitism(population, elitism);
	    	
	    	if(i == iter) {
	    		System.out.printf("[Test error]: %.6f\n", elitePopulation.get(0).getErr());
	    		break;
	    	}
	    	
	    	if(elitePopulation.size() + tmpPopulation.size() <= popsize) {
	    		tmpPopulation.addAll(elitePopulation);
	    	}else {
	    		for(int j = 0; j < (popsize - tmpPopulation.size()); j++) {
	    			elitePopulation.add(elitePopulation.get(j));
	    		}
	    	}
	    	
	    	//Ponavljaj dok velicina pomocne populacije ne postane jednaka velicini populacije roditelja
	    	while(tmpPopulation.size() != population.size()) {
	    		Pair<NeuralNetwork, NeuralNetwork> parents = getParents(population);
	    		
	    		NeuralNetwork parent1 = parents.getFirst();
	    		NeuralNetwork parent2 = parents.getSecond();
	    		
	    		List<W> weights1 = parent1.getAllWeights();
	    		List<W> weights2 = parent2.getAllWeights();	
	    		
	    		List<W> bias1 = parent1.getBiases();
	    		List<W> bias2 = parent2.getBiases();
	    		
	    		List<W> childBias = cross(bias1, bias2);
	    		
	    		//Dijete = Križaj roditelje + Mutacija
	    		List<W> childWeights = cross(weights1, weights2);
	    		childWeights = mutate(childWeights, p, K, random);
	    		childBias = mutate(childBias, p, K, random);
	    		NeuralNetwork child = new NeuralNetwork(parent1.getAttributes(), parent1.getTrainingData(), nn, childWeights, childBias);
	    		
	    		tmpPopulation.add(child);
	    	}
	    	
	    	population = tmpPopulation;
	    }
	    
	    
	}

	private static List<W> mutate(List<W> childWeights, double mutationProb, double scale, Random random) {
		for(W weight : childWeights) {
			if(random.nextDouble() < mutationProb) {
				double gauss = random.nextGaussian();
				weight.setValue(weight.getValue() + (gauss * scale));
			}
		}
		
		return childWeights;
	}

	private static List<W> cross(List<W> weights1, List<W> weights2) {
		List<W> rez = new ArrayList<>();
		
		for(int i = 0; i < weights1.size(); i++) {
			double firstValue = weights1.get(i).getValue();
			double secondValue = weights2.get(i).getValue();
			double average = (firstValue + secondValue) / 2;
			
			W tmp = weights1.get(i);
			W weight = new W(tmp.getStartNeuron(), tmp.getTargetNeuron(), tmp.getLayer(), average);
			rez.add(weight);
		}
		
		return rez;
	}

	private static Pair<NeuralNetwork, NeuralNetwork> getParents(List<NeuralNetwork> population) {
		//pretvori greške u dobrote 1 / err
		//što je manja greška to je veća dobrota
		Pair<NeuralNetwork, NeuralNetwork> rez = new Pair<NeuralNetwork, NeuralNetwork>(null, null);
		int firstParent = -10;
		
		double sum = population.stream().mapToDouble(e -> 1 / e.err).sum();
		
		//vrti dokle god se ne dobije jedan i drugi par
		while(rez.getFirst() == null || rez.getSecond() == null) {
			double arrow = Math.random() * sum;
			
			double area = 0;
			for(int i = 0; i < population.size(); i++) {
				area += (1 / population.get(i).err);
				if(Double.compare(arrow, area) <= 0) {
					if(rez.getFirst() == null) {
						rez.setFirst(population.get(i));
						firstParent = i;
					}else if(rez.getSecond() == null  && i != firstParent) {
						rez.setSecond(population.get(i));
					}
				}
			}
		}
		
		return rez;
	}

	private static List<NeuralNetwork> maxElitism(List<NeuralNetwork> tmpPopulation, int elitism) {
		
		return tmpPopulation.stream().sorted((nn1, nn2) -> Double.compare(nn1.err, nn2.err)).limit(elitism).collect(Collectors.toList());
	}
}
