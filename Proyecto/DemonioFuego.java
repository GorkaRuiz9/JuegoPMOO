package Proyecto;


public class DemonioFuego extends Villano{

	public DemonioFuego(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
	}
	
	public void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera)
	{
		Dado unDado=new Dado();
		int valor=unDado.tirarDado();
		
		if ((valor==1 || valor==2)&& !pEspadachin.muerto())
		{
			System.out.print("\nEL demonio de fuego ataca al espadachín!!!!");
			this.accion(pEspadachin);
		}
		else if ((valor==3|| valor==4)&& !pMago.muerto())
		{
			System.out.print("\nEL demonio de fuego ataca al mago!!!!");
			this.accion(pMago);
		}
		else if ((valor==5|| valor==6)&& !pCurandera.muerto())
		{
			System.out.print("\nEL demonio de fuego ataca a la curandera!!!!");
			this.accion(pCurandera);
		}
		else
		{
			Heroe unHeroe=ListaHeroes.getListaHeroes().devolverPrimeroVivo2();
			if (unHeroe instanceof Espadachin) 
			{
				System.out.print("\nEL demonio de fuego ataca al espadachín!!!!");
				this.accion(unHeroe);
			}
			else if (unHeroe instanceof Mago)
			{
				System.out.print("\nEL demonio de fuego ataca al mago!!!!");
				this.accion(unHeroe);
			}
			else
			{
				System.out.print("\nEL demonio de fuego ataca a la curandera!!!!");
				this.accion(unHeroe);
			}
		}
		super.sumarContadores();
	}
	
	public void accion(Personaje pPersonaje)
	{
		if (super.consultarHab2()%5==0 && super.consultarHab2()!=0)
		{
			this.habilidad2(pPersonaje);
			
		}
		else if (super.consultarHab1()%3==0 && super.consultarHab2()%5!=0&& super.consultarHab1()!=0)
		{
			this.habilidad1(pPersonaje);
		}
		else
		{
			super.atacar(pPersonaje);
			if (super.consultarEstado()==0 && !pPersonaje.muerto())
			{
				this.quema(pPersonaje);
			}
		}
	}
	
	public void quema(Personaje pPersonaje)
	{
		Dado unDado=new Dado();
		int valor=unDado.tirarDado();
		
		if ((valor==5 || valor==6)&& !pPersonaje.muerto())
		{
			pPersonaje.cambiarEstado(1);
			System.out.print("El héroe ha sido quemado!!!!");
		}
	}
	
	public void habilidad1(Personaje pPersonaje)
	{
		if (super.consultarEstado()==3)
		{
			System.out.print("El enemigo está aturdido!!");
		}
		else
		{
		System.out.print("\nEl demonio de fuego utiliza un ataque especial!!");
		super.atacar(pPersonaje);
		if (super.consultarEstado()!=3 && super.consultarEstado()!=4 && !pPersonaje.muerto())
		{
			pPersonaje.cambiarEstado(3);
			System.out.print("\nEl héroe atacado ha sido aturdido!!");
		}
		}
	}
	
	public void habilidad2(Personaje pPersonaje)
	{
		if (super.consultarEstado()==3)
		{
			System.out.print("El enemigo está aturdido!!");
		}
		else
		{
		System.out.print("\nEl demonio de fuego utiliza su habilidad especial que le permite atacar dos veces seguidas!!");
		
		super.atacar(pPersonaje);
		if (!pPersonaje.muerto())
		{
			super.atacar(pPersonaje);
		}
		}
	}
	
	
	
	
	
	
	
	
}
