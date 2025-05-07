package Proyecto;


public class PocionCura extends Objeto{

	@Override
	void utilizarObjeto(Heroe pHeroe) 
	{
		pHeroe.curacion();
		super.cambiarTengo();
	}

}