package Proyecto;


abstract class Heroe extends Personaje{
	
	private int contador;
	private boolean habilidadEspecial;
	private boolean cubierto;
	private ListaObjetos mochila;

	protected Heroe(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
		this.contador=0;
		this.habilidadEspecial=false;
		this.cubierto=false;
		this.mochila=ListaObjetos.getListaObjetos();
	}
	
	abstract void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera);
	
	public int consultarContador()
	{
		return this.contador;
	}
	
	public void sumarContador()
	{
		this.contador=this.contador+1;
	}
	
	public void reiniciarContador()
	{
		this.contador=-1;
	}
	
	public void cambiarPersonaje(Villano pPersonaje,Heroe pEspadachin, Heroe pMago, Heroe pCurandera)
	{
		Teclado ed=Teclado.getTeclado();
		int numero=ed.cambioPersonaje();
		
		if (numero==1 && !pEspadachin.muerto()) 
		{
			System.out.print("\nEstás actualmente controlando al espadachín.");
			pEspadachin.realizarAccion(pPersonaje, pEspadachin, pMago, pCurandera);
		}
		else if (numero==2 && !pMago.muerto())
		{
			System.out.print("\nEstás actualmente controlando al mago.");
			pMago.realizarAccion(pPersonaje, pEspadachin, pMago, pCurandera);
		}
		else if (numero==3 && !pCurandera.muerto())
		{
			System.out.print("\nEstás actualmente controlando a la curandera.");
			pCurandera.realizarAccion(pPersonaje, pEspadachin, pMago, pCurandera);
		}
		else
		{
			System.out.print("\nEse personaje está muerto, elige a otro");
			this.cambiarPersonaje(pPersonaje, pEspadachin, pMago, pCurandera);
		}
	}
	
	public boolean habEspecial()
	{
		return this.habilidadEspecial;
	}
	
	public void cambioConHab(boolean valor)
	{
		this.habilidadEspecial=valor;
	}
	
	public void cubrirse()
	{
		System.out.print("\nTurno del demonio de fuego, preparate!!!");
		System.out.print("El héroe está cubierto, por lo que recibe poca cantidad de daño!!");
		super.bajarVida(50);
		this.cubierto=true;
	}
	
	public boolean getCubierto()
	{
		return this.cubierto;
	}
	
	public void setCubierto(boolean pValor)
	{
		this.cubierto=pValor;
	}
	
	abstract void restaurarStats();
	
	public void utilizarObjeto() 
	{
		this.mochila.utilizarObjeto(this);
	}
	
	
	
	
	
	
	
	
	
	
	
}
