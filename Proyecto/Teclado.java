package Proyecto;

import java.util.Scanner;


public class Teclado {

	//Atributos
	private Scanner sc;
	private static Teclado miTeclado;
	
	//Constructora
	private Teclado()
	{
		this.sc= new Scanner(System.in);
	}
	
	public static Teclado getTeclado()
	{
		if (miTeclado==null)
		{
			miTeclado= new Teclado();
		}
		return miTeclado;
	}
	
	//Métodos
	public int cambioPersonaje()
	{
		int s1=0;
		
		
		
		while (s1==0)
		{
			System.out.print("\nPara cambiar de personaje elija: ");
			System.out.print("\n---Introduzca 1 para el Espadachín");
			System.out.print("\n---Introduzca 2 para el Mago");
			System.out.print("\n---Introduzca 3 para la Curandera");
			
			s1=this.sc.nextInt();
			if (s1!=1 && s1!=2 && s1!=3)
			{
				s1=0;
			}
		}
		
		
		
		return s1;
	}
	
	public void TeclearParaContinuar()
	{
		this.sc.nextLine();
	}
		
	
	public int realizarAccion()
	{
		int s1=0;
		
		while (s1==0)
		{
			System.out.print("\nQue quieres hacer?");
			System.out.print("\n---Para atacar introduce 1");
			System.out.print("\n---Para cubrirte introduce 2");
			System.out.print("\n---Para utilizar un objeto introduce 3");
			System.out.print("\n---Para cambiar de personaje introduce 4");
			s1=this.sc.nextInt();
			if (s1!=1 && s1!=2 && s1!=3 && s1!=4)
			{
				s1=0;
			}
			
		}
		
		
		
		return s1;
	}
	
	public int realizarAccionHabEspecial()
	{
		int s1=0;
		
		while (s1==0)
		{
			System.out.print("Que quieres hacer?");
			System.out.print("\n---Para atacar introduce 1");
			System.out.print("\n---Para cubrirte introduce 2");
			System.out.print("\n---Para utilizar un objeto introduce 3");
			System.out.print("\n---Para cambiar de personaje introduce 4");
			
			System.out.print("\n---ATENCION!!!! Tienes disponible la habilidad especial del héroe, si quieres utilizarla introduce 5");
			s1=this.sc.nextInt();
			if (s1!=1 && s1!=2 && s1!=3 && s1!=4 && s1!=5)
			{
				s1=0;
			}
			
		}
		
		
		
		return s1;
	}
	
	public int seleccionObjeto()
	{
		int s1=0;
		
		
		
		while (s1==0)
		{
			System.out.print("\nQue objeto quieres utilizar?: ");
			System.out.print("\n---Introduzca 1 para la poción de fuerza");
			System.out.print("\n---Introduzca 2 para la poción de cura");
			System.out.print("\n---Introduzca 3 para la poción cura estados");
			
			s1=this.sc.nextInt();
			if (s1!=1 && s1!=2 && s1!=3)
			{
				s1=0;
			}
		}
		
		
		
		return s1;
	}
		

	}
	
	
	

