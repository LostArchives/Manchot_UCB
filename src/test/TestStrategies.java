package test;

import model.Manchot;
import model.Strategie;

public class TestStrategies {

	public static void main(String[] args) {
		
		//long seed = System.currentTimeMillis();
		
		Manchot[] mes_manchots = Strategie.createManchots(15, -10, 10, 0, 10);
		
		Strategie strat = new Strategie(mes_manchots);
		
		//strat.rechercheAleatoire(15000);
		
		//strat.rechercheGloutonne(15000);
		
		strat.rechercheUCB(10000, 15000);
		//strat.rechercheUCB(1.412, 15000);
		//strat.rechercheUCB(20, 15000);
		//strat.rechercheUCB(100, 15000);
		
	}
	
}
