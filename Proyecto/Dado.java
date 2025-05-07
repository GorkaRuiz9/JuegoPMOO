package Proyecto;

import java.util.Random;

public class Dado {

	private int nCaras=6;
	
	
	public Dado() {}
	
	public int tirarDado()
	{
		Random r=new Random();
		int tirada=r.nextInt(nCaras)+1;
		return tirada;
	}
	
}
