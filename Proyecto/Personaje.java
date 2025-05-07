package Proyecto;


abstract class Personaje {

	
	private int vida;
	private int ataque;
	private int defensa;
	private int estado;
	
	
	protected Personaje(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		this.vida=pVida;
		this.ataque=pAtaque;
		this.defensa=pDefensa;
		this.estado=pEstado;
	}
	
	abstract void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera);
	
	public void atacar(Personaje pPersonaje)
	{
		Dado unDado=new Dado();
		
		if (this.consultarEstado()==3)
		{
			System.out.print("\nEst� aturdido, no puede atacar!!");
		}
		else if (this.consultarEstado()==4)
		{
			System.out.print("\nEsta paralizado!!");
			int valor=unDado.tirarDado();
			if (valor>=4)
			{
				System.out.print("\nPero puede moverse!!");
				this.ataque(pPersonaje);
				this.cambiarEstado(5);
			}
			else
			{
				System.out.print("\nY no puede moverse!!");
			}
		}
		else
		{
			this.ataque(pPersonaje);
		}
	}
	
	public void ataque(Personaje pPersonaje)
	{
		int da�o;
		Dado unDado=new Dado();
		
		
		
		int vDado= unDado.tirarDado();
		if (vDado<6)
		{
			da�o=this.ataque*vDado-pPersonaje.obtenerDefensa();
			System.out.print("\nEl da�o provocado ha sido de "+da�o);
			pPersonaje.bajarVida(da�o);
			Teclado.getTeclado().TeclearParaContinuar();
		}
		else
		{
			System.out.print("\nHa sido un golpe cr�tico!!!");
			da�o=(this.ataque*vDado-pPersonaje.obtenerDefensa());
			int da�oCritico=da�o*2;
			System.out.print("\nEl da�o provocado ha sido de "+da�oCritico);
			pPersonaje.bajarVida(da�oCritico);
		}
	}
	
	public void bajarVida(int pCtd)
	{
		this.vida=this.vida-pCtd;
		if (this.muerto())
		{
			System.out.print("\nEl personaje ha perdido todos sus puntos de vida!!");
			this.vida=0;
		}
		else
		{
		this.mostrarVida();
		}
	}
	
	public int mostrarVida()
	{
		if (this instanceof Heroe)
		{
			System.out.print("\nLa nueva vida del h�roe es de: ");
			System.out.print(this.vida);
			Teclado.getTeclado().TeclearParaContinuar();
		}
		else
		{
			System.out.print("\nLa nueva vida del enemigo es de: ");
			System.out.print(this.vida);
			Teclado.getTeclado().TeclearParaContinuar();
		}
		return this.vida;
	}
	
	public int consultarEstado()
	{
		return this.estado;
	}
	
	public void restaurarEstado() 
	{
		this.estado=0;
	}
	
	public int obtenerDefensa()
	{
		return this.defensa;
	}
	
	public void aplicarEstado()
	{
		if (this.consultarEstado()==1)
		{
			if (this instanceof Espadachin)
			{
				System.out.print("\nOh no! El espadach�n recibe da�o de la quemadura!!");
				Teclado.getTeclado().TeclearParaContinuar();
			}
			else if (this instanceof Mago)
			{
				System.out.print("\nOh no! El mago recibe da�o de la quemadura!!");
				Teclado.getTeclado().TeclearParaContinuar();
			}
			else if (this instanceof Curandera)
			{
				System.out.print("\nOh no! La curandera recibe da�o de la quemadura!!");
				Teclado.getTeclado().TeclearParaContinuar();
			}
			
			
			
			this.bajarVida(50);
		}
		
	}
	
	public boolean muerto()
	{
		if (this.vida<=0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int obtenerAtaque()
	{
		return this.ataque;
	}
	
	
	public void curacion()
	{
		this.vida=this.vida+100;
		
		if (this.vida>500)
		{
			this.vida=500;
		}
	}
	public void cambiarEstado(int pEstado)
	{
		this.estado=pEstado;
	}
	
	public void cambiarAtaque(int pAtaque)
	{
		this.ataque=pAtaque;
	}
	
	public void cambiarDefensa(int pDefensa)
	{
		this.defensa=pDefensa;
	}
	
	public int devolverVida()
	{
		return this.vida;
	}
	
	public void subirAtaque(int pCtd)
	{
		this.ataque=this.ataque+pCtd;
	}
	

}
