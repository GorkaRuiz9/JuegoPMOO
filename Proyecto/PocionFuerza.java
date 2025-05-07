package Proyecto;


public class PocionFuerza extends Objeto{

	
	void utilizarObjeto(Heroe pHeroe) 
	{
		pHeroe.subirAtaque(50);
		super.cambiarTengo();
	}

}
