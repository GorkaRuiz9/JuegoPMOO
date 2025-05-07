package Proyecto;


public class Main {

	private static Main instancia= new Main();
	
	PocionFuerza fuerza=new PocionFuerza();
	PocionCura cura=new PocionCura();
	PocionCuraEstados curaEstados=new PocionCuraEstados();
	
	Espadachin es= new Espadachin(500, 35, 10, 0);
	Mago mg= new Mago(500, 30, 5, 0);
	Curandera cu= new Curandera(500, 25, 15, 0);
	
	DemonioFuego df= new DemonioFuego(1500, 45, 15,0);
	
	
	
	
	
	private Main()
	{
		
	}
	
	public void jugarUnaPartida()
	{
		Instrucciones.getInstrucciones().imprimirInstrucciones();
		
		ListaObjetos.getListaObjetos().anadir(fuerza);
		ListaObjetos.getListaObjetos().anadir(cura);
		ListaObjetos.getListaObjetos().anadir(curaEstados);
		
		ListaHeroes.getListaHeroes().anadir(es);
		ListaHeroes.getListaHeroes().anadir(mg);
		ListaHeroes.getListaHeroes().anadir(cu);
		
		
		while (!ListaHeroes.getListaHeroes().todosMuertos() && !df.muerto())
		{
			System.out.print("\nTurno de los héroes, elige que hacer");
			
			ListaHeroes.getListaHeroes().devolverPrimeroVivo().realizarAccion(df, es, mg, cu);
			ListaHeroes.getListaHeroes().restauracion();
			
			if (es.habEspecial() || mg.habEspecial() || ListaHeroes.getListaHeroes().cubierto())
			{es.cambioConHab(false);mg.cambioConHab(false);ListaHeroes.getListaHeroes().ponerNoCubierto();}
			else if (!df.muerto())
			{
				Teclado.getTeclado().TeclearParaContinuar();
				System.out.print("\nTurno del demonio de fuego, preparate!!!");
				df.realizarAccion(df, es, mg, cu);
				df.restaurarEstado();
				Teclado.getTeclado().TeclearParaContinuar();
			}
		}
		
		if (df.muerto())
		{
			System.out.print("\nEnhorabuena!!! Has derrotado al villano!!");
			System.out.print("\nTu puntuación ha sido de: ");
			System.out.print(es.devolverVida()+mg.devolverVida()+cu.devolverVida());
		}
		else
		{
			System.out.print("\nVayaaa, otra vez será. Inténtalo de nuevo!!!");
		}
		
	}
	
	public static void main(String[] args)
	{
		instancia.jugarUnaPartida();
	}
	
}
