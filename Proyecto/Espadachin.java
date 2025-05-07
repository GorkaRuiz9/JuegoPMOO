package Proyecto;


public class Espadachin extends Heroe{

	public Espadachin(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
	}
	
	public void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera)
	{
		Teclado tc=Teclado.getTeclado();
		Dado unDado=new Dado();
		
		if (super.consultarContador()<3)
		{
			int accion=tc.realizarAccion();
			
			if (accion==1)
			{
				super.atacar(pPersonaje);
				int dado=unDado.tirarDado();
				if (dado>=5 && super.consultarEstado()==0 && !pPersonaje.muerto())
				{
					System.out.print("\nEl golpe ha aturdido al enemigo!! No podr� atacar en su siguiente turno");
					pPersonaje.cambiarEstado(3);
				}
				this.sumarContador();
			}
			else if (accion==2)
			{
				super.cubrirse();
				this.sumarContador();
				pPersonaje.sumarContadores();
			}
			else if (accion==3)
			{
				super.utilizarObjeto();
				this.realizarAccion(pPersonaje, pEspadachin, pMago, pCurandera);
			}
			else if (accion==4)
			{
				super.cambiarPersonaje(pPersonaje,pEspadachin, pMago, pCurandera);
			}
			
		}
		else if (super.consultarContador()>=3)
		{
			int accion2=tc.realizarAccionHabEspecial();
			
			if (accion2==1)
			{
				super.atacar(pPersonaje);
				int dado=unDado.tirarDado();
				if (dado>=5 && super.consultarEstado()==0 && !pPersonaje.muerto())
				{
					System.out.print("\nEl golpe ha aturdido al enemigo!! No podr� atacar en su siguiente turno");
					pPersonaje.cambiarEstado(3);
				}
				this.sumarContador();
			}
			else if (accion2==2)
			{
				super.cubrirse();
				this.sumarContador();
			}
			else if (accion2==3)
			{
				super.utilizarObjeto();
				this.realizarAccion(pPersonaje, pEspadachin, pMago, pCurandera);
			}
			else if (accion2==4)
			{
				super.cambiarPersonaje(pPersonaje,pEspadachin, pMago, pCurandera);
			}
			else 
			{
				this.coontrataque(pPersonaje);
			}
			
		}
		
		
	}
	
	public void coontrataque(Personaje pPersonaje)
	{
		Dado unDado=new Dado();
		
		int da�o= pPersonaje.obtenerAtaque()*unDado.tirarDado()-this.obtenerDefensa();
		
		System.out.print("\nEl Espadach�n toma una postura de contraataque!");
		System.out.print("\nEl da�o provocado al Espadach�n es de" +da�o);
		System.out.print("\nPero este lo repele devolviendo el ataque del enemigo causando un fuerte da�o en este");
		
		Teclado.getTeclado().TeclearParaContinuar();
		
		int da�oC=da�o*2;
		
		pPersonaje.bajarVida(da�oC);
		
		super.reiniciarContador();
		super.cambioConHab(true);
	}

	@Override
	void restaurarStats() 
	{
		super.cambiarAtaque(35);
		super.cambiarDefensa(10);
	}
	
	
	
	
}
