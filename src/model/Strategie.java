package model;

import java.util.HashMap;
import java.util.Random;

public class Strategie {

	private Manchot[] _manchots;
	
	public Strategie(Manchot[] p_manchots) {
		_manchots = p_manchots;
	}
	
	public double rechercheAleatoire(int nb_iterations) {
		
		FileWriter f = new FileWriter("output/Aleatoire" + _manchots.length + "Machines_" + nb_iterations + "Iterations.csv");
		f.Write("Iterations;Gain_cumule" + "\n");
		
		double gain_cumul = 0;
		
		Random r = new Random();
		
		for (int i = 0 ; i< nb_iterations;i++) {
			
			int index = r.nextInt(_manchots.length);
			
			double gain = _manchots[index].tirerBras();
			
			gain_cumul += gain;
			
			f.Write((i+1) + ";" + gain_cumul + "\n");
			
			System.out.println("Stratégie aléatoire - Itération " + (i+1) + " : Machine " + index + " - Gain : " + gain + " - Gain Total : "+ gain_cumul );
			
		}
		
		f.flush();
		
		return gain_cumul;
		
	}
	
	public double rechercheGloutonne(int nb_iterations) {
		
		FileWriter f = new FileWriter("output/Gloutonne_" + _manchots.length + "Machines_" + nb_iterations + "Iterations.csv");
		f.Write("Iterations;Gain_cumule" + "\n");
		
		double gain_cumul = 0;
		
		int bestIndex = getBestManchotIndex();
		
		for (int i = 0 ; i< nb_iterations;i++) {
			
			double gain = _manchots[bestIndex].tirerBras();
			
			gain_cumul += gain;
			
			f.Write((i+1) + ";" + gain_cumul + "\n");
			
			System.out.println("Stratégie Gloutonne (Machine "+ bestIndex +") - Itération " + (i+1) + " - Gain : " + gain + " - Gain Total : " + gain_cumul);
		}
		
		f.flush();
		
		return  gain_cumul;
	}
	
	public double rechercheUCB(double K, int nb_iterations) {
		
		FileWriter f = new FileWriter("output/UCB_" + _manchots.length + "Machines_" + nb_iterations + "Iterations_K"+ K +".csv");
		f.Write("Iterations;Gain_cumule" + "\n");
		
		double gain_cumul = 0;
		HashMap<Integer,Integer> _nbTiragesParBras = new HashMap<Integer,Integer>();
		HashMap<Integer,Double> _scoresParBras = new HashMap<Integer,Double>();
		
		for (int i = 0 ; i < _manchots.length ; i++) {
			if (_nbTiragesParBras.get(i)==null) {
				_nbTiragesParBras.put(i,0);
			}
			if (_scoresParBras.get(i)==null) {
				_scoresParBras.put(i,0.0);
			}
			double gain = _manchots[i].tirerBras();
			gain_cumul += gain;
			
			_nbTiragesParBras.put(i,_nbTiragesParBras.get(i) + 1);
			_scoresParBras.put(i,_scoresParBras.get(i) + gain);
			
		}
		
		for (int i = 0 ; i < nb_iterations - _manchots.length ; i++) {
			
			int indexUCB = getBestIndexUCB(K, _nbTiragesParBras,_scoresParBras,(i+1));
			double gain = _manchots[indexUCB].tirerBras();
			
			_nbTiragesParBras.put(indexUCB,_nbTiragesParBras.get(indexUCB) + 1);
			_scoresParBras.put(indexUCB,_scoresParBras.get(indexUCB) + gain);
			
			gain_cumul += gain;
			
			f.Write((i+1) + ";" + gain_cumul + "\n");
			
			System.out.println("Stratégie UCB - Machine " + indexUCB + " - Itération " + (i+1) + " - Gain : " + gain + " - Gain Total : " + gain_cumul);
			
		}
		
		f.flush();
		
		return gain_cumul;
	}
	
	
	public static Manchot[] createManchots(int nb_manchots , int min_variance, int max_variance , int min_esperance, int max_esperance) {
		
		Manchot[] _manchots = new Manchot[nb_manchots];
		
		Random r = new Random();
		
		for (int i = 0 ; i < nb_manchots ; i++) {
			double esperance = min_esperance + max_esperance * r.nextDouble();
		
			double variance = min_variance + max_variance * r.nextDouble();
			
			_manchots[i] = new Manchot(esperance, variance);
			
		}
		
		return _manchots;
		
	}
	
	/** Pour la recherche Gloutonne **/
	private int getBestManchotIndex() {
		
		int _bestManchotIndex = -1;
		
		double bestGain = -999;
		
		for (int i = 0 ; i < _manchots.length; i++) {
			
			double gain_actuel = _manchots[i].tirerBras();
			
			if (gain_actuel > bestGain) {
				_bestManchotIndex = i;
				bestGain = gain_actuel;
			}
			
		}
		
		return _bestManchotIndex;
	}
	
	
	/** Pour la recherche par UCB **/
	private int getBestIndexUCB(double K,HashMap<Integer,Integer> p_tirages,HashMap<Integer,Double> p_sommes,int actual_iteration) {
		
		int bestManchotIndex = -1;
		double bestUCBScore = -999;
		
		for (int i = 0 ; i < _manchots.length ; i++) {
			
			double actualUCBScore = (p_sommes.get(i) / p_tirages.get(i)) + K * Math.sqrt(Math.log(actual_iteration / p_tirages.get(i)));
			
			if (actualUCBScore > bestUCBScore) {
				bestUCBScore = actualUCBScore;
				bestManchotIndex = i;
			}
			
		}
		
		return bestManchotIndex;
	}
	
	
}
