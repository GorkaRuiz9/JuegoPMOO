package Proyecto;


public class Mago extends Heroe{

	public Mago(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
	}
	
	public void realizarAccion(Villano pPersonaje, Heroe pEspadachin, Heroe pMago, Heroe pCurandera)
	{
		Teclado tc=Teclado.getTeclado();
		Dado unDado=new Dado();
		
		if (super.consultarContador()<5)
		{
			
			int accion=tc.realizarAccion();
			
			if (accion==1)
			{
				super.atacar(pPersonaje);
				int dado=unDado.tirarDado();
				
				if (dado>=4 && super.consultarEstado()==0 && !pPersonaje.muerto())
				{
					System.out.print("\nEl enemigo ha quedado paralizado!! Puede que no se mueva en el siguiente turno!!");
					pPersonaje.cambiarEstado(4);
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
			else
			{
				super.cambiarPersonaje(pPersonaje,pEspadachin, pMago, pCurandera);
			}
			
		}
		else if (super.consultarContador()>=5)
		{
			int accion2=tc.realizarAccionHabEspecial();
			
			if (accion2==1)
			{
				super.atacar(pPersonaje);
				int dado=unDado.tirarDado();
				
				if (dado>=4 && super.consultarEstado()==0 && !pPersonaje.muerto())
				{
					System.out.print("\nEl enemigo ha quedado paralizado!! Puede que no se mueva en el siguiente turno!!");
					pPersonaje.cambiarEstado(4);
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
				this.hechizoHielo(pPersonaje);
			}
			

		}
			
	}
	
	public void hechizoHielo(Personaje pPersonaje)
	{
		
		System.out.print("\nEl mago utiliza  el Hechizo de Hielo");
		
		super.atacar(pPersonaje);
		super.reiniciarContador();
		
		System.out.print("\nParece que el hechizo ha dejado al enemigo congelado!!! No podr� atacar hasta el siguiente turno!!");
		super.reiniciarContador();
		super.cambioConHab(true);
	}

	@Override
	void restaurarStats() 
	{
		super.cambiarAtaque(30);
		super.cambiarDefensa(5);
	}
	
	
	
	
	
	
	
	
}
