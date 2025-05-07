package Proyecto;


abstract class Villano extends Personaje{
	
	private int contHab1;
	private int contHab2;
	
	protected Villano(int pVida, int pAtaque, int pDefensa, int pEstado)
	{
		super(pVida, pAtaque, pDefensa, pEstado);
		this.contHab1=0;
		this.contHab2=0;
	}
	
	public int consultarHab1()
	{
		return this.contHab1;
	}
	
	public int consultarHab2()
	{
		return this.contHab2;
	}
	
	public void sumarContadores()
	{
		this.contHab1=this.contHab1+1;
		this.contHab2=this.contHab2+1;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
