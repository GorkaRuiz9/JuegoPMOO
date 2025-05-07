package Proyecto;


import java.util.ArrayList;
import java.util.Iterator;

public class ListaObjetos {

	private ArrayList<Objeto> lista;
	private static ListaObjetos miLista;
	
	private ListaObjetos()
	{
		this.lista= new ArrayList<Objeto>();
	}
	
	public static ListaObjetos getListaObjetos()
	{
		if (miLista==null)
		{
			miLista= new ListaObjetos();
		}
		return miLista;
	}
	
	private Iterator<Objeto> getIterador()
	{
		return this.lista.iterator();
	}
	
	public void utilizarObjeto (Heroe pHeroe)
	{
		int valor=Teclado.getTeclado().seleccionObjeto();
		
		if (valor==1)
		{
			Objeto fuerza=this.devolverFuerza();
			if (fuerza.devolverTengo())
			{
				fuerza.utilizarObjeto(pHeroe);
				System.out.print("\nLa fuerza del héroe ha aumentado!!! Es momento de atacar!!!");
			}
			else
			{
				System.out.print("\nNo tienes objetos restantes de este tipo!!!");
			}
		}
		else if (valor==2)
		{
			Objeto defensa=this.devolverCura();
			if (defensa.devolverTengo())
			{
				defensa.utilizarObjeto(pHeroe);
				System.out.print("\nLa vida del héroe ha aumentado!!!");
			}
			else
			{
				System.out.print("\nNo tienes objetos restantes de este tipo!!!");
			}
		}
		else if (valor==3)
		{
			Objeto curaEstados=this.devolverCuraEstados();
			if (curaEstados.devolverTengo())
			{
				curaEstados.utilizarObjeto(pHeroe);
				System.out.print("\nEl estado del héroe vuelve a ser normal!!! Es momento de atacar!!!");
			}
			else
			{
				System.out.print("\nNo tienes objetos restantes de este tipo!!!");
			}
		}
	}
	
	public Objeto devolverFuerza ()
	{
		Objeto unObjeto=null;
		Iterator<Objeto> itr=this.getIterador();
		
		while (itr.hasNext())
		{
			unObjeto=itr.next();
			if (unObjeto instanceof PocionFuerza)
			{
				return unObjeto;
			}
		}
		return null;
	}
	
	public Objeto devolverCura ()
	{
		Objeto unObjeto=null;
		Iterator<Objeto> itr=this.getIterador();
		
		while (itr.hasNext())
		{
			unObjeto=itr.next();
			if (unObjeto instanceof PocionCura)
			{
				return unObjeto;
			}
		}
		return null;
	}
	
	public Objeto devolverCuraEstados ()
	{
		Objeto unObjeto=null;
		Iterator<Objeto> itr=this.getIterador();
		
		while (itr.hasNext())
		{
			unObjeto=itr.next();
			if (unObjeto instanceof PocionCuraEstados)
			{
				return unObjeto;
			}
		}
		return null;
	}
	
	public void anadir(Objeto pObjeto)
	{
		this.lista.add(pObjeto);
	}
}
