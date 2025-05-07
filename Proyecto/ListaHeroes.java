package Proyecto;


import java.util.ArrayList;
import java.util.Iterator;

public class ListaHeroes {

	private ArrayList<Heroe> lista;
	private static ListaHeroes miLista;
	
	private ListaHeroes()
	{
		this.lista=new ArrayList<Heroe>();
	}
	
	public static ListaHeroes getListaHeroes()
	{
		if (miLista==null) 
		{
			miLista=new ListaHeroes();
		}
		return miLista;
	}
	
	private Iterator<Heroe> getIterador()
	{
		return this.lista.iterator();
	}
	
	public void curacion()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe;
		
		while (itr.hasNext())
		{
			unHeroe=itr.next();
			if (!unHeroe.muerto())
			{
			unHeroe.curacion();
			}
		}
	}
	
	public void anadir(Heroe pHeroe)
	{
		this.lista.add(pHeroe);
	}
	
	public boolean todosMuertos()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe;
		boolean rdo=true;
		
		while (itr.hasNext())
		{
			unHeroe=itr.next();
			if(!unHeroe.muerto())
			{
				rdo=false;
			}
		}
		return rdo;
	}
	
	public void restauracion()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe;
		
		while (itr.hasNext())
		{
			unHeroe=itr.next();
			unHeroe.aplicarEstado();
			unHeroe.restaurarEstado();
			unHeroe.restaurarStats();
		}
	
	}
	public Heroe devolverPrimeroVivo()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe=null;
		boolean chivato=true;
		
		while (itr.hasNext() && chivato)
		{
			unHeroe=itr.next();
			if(!unHeroe.muerto())
			{
				chivato=false;
			}
		}
		if (unHeroe instanceof Espadachin)
		{
			System.out.print("\nActualmente estás controlando al espadachín.");
		}
		else if (unHeroe instanceof Mago)
		{
			System.out.print("\nActualmente estás controlando al mago.");
		}
		else if (unHeroe instanceof Curandera)
		{
			System.out.print("\nActualmente estás controlando a la curandera.");
		}
		
		Teclado.getTeclado().TeclearParaContinuar();
		
		return unHeroe;
	}
	public Heroe devolverPrimeroVivo2()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe=null;
		boolean chivato=true;
		
		while (itr.hasNext() && chivato)
		{
			unHeroe=itr.next();
			if(!unHeroe.muerto())
			{
				chivato=false;
			}
		}
		return unHeroe;
	}
	
	public boolean cubierto()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe=null;
		boolean chivato=false;
		while (itr.hasNext() )
		{
			unHeroe=itr.next();
			if (unHeroe.getCubierto())
			{
				chivato=true;
			}
		}
		return chivato;
	}
	
	public void ponerNoCubierto()
	{
		Iterator<Heroe> itr=this.getIterador();
		Heroe unHeroe=null;
		while (itr.hasNext() )
		{
			unHeroe=itr.next();
			unHeroe.setCubierto(false);
		}
	}
}
