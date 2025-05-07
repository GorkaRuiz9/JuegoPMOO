package Proyecto;


public class Curandera extends Heroe{
	
	public Curandera(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
	}
	
	public void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera)
	{
		Teclado tc=Teclado.getTeclado();
		
		if (super.consultarContador()<2)
		{
			int accion=tc.realizarAccion();
			
			if (accion==1)
			{
				super.atacar(pPersonaje);
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
			else
			{
				super.cambiarPersonaje(pPersonaje,pEspadachin, pMago, pCurandera);
			}
			
		}
		else if (super.consultarContador()>=2)
		{
			int accion2=tc.realizarAccionHabEspecial();
			
			if (accion2==1)
			{
				super.atacar(pPersonaje);
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
				this.Curacion();
			}
			
		}
			
	}
	
	public void Curacion()
	{
		System.out.print("\nLa curandera usa la curación, recuperando 100 de vida a todos los héroes!!");
		ListaHeroes.getListaHeroes().curacion();
		
		super.reiniciarContador();
	}

	@Override
	void restaurarStats() 
	{
		super.cambiarAtaque(25);
		super.cambiarDefensa(15);
	}

	
	
	
	
	
	
	
	
}
