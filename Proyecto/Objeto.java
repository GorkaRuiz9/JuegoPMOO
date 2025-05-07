package Proyecto;


abstract class Objeto {

	private boolean tengo;
	
	protected Objeto()
	{
		this.tengo=true;
	}
	
	public boolean devolverTengo()
	{
		return this.tengo;
	}
	
	public void cambiarTengo()
	{
		this.tengo=false;
	}
	
	abstract void utilizarObjeto(Heroe pHeroe);
	
	
	
}
