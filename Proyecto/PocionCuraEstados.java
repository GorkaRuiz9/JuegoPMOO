package Proyecto;


public class PocionCuraEstados extends Objeto{

	@Override
	void utilizarObjeto(Heroe pHeroe) 
	{
		pHeroe.restaurarEstado();
		super.cambiarTengo();
	}

}
