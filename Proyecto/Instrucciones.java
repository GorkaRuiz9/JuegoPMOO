package Proyecto;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Instrucciones {

	private static Instrucciones misInstrucciones;
	
	private Instrucciones() {}
	
	public static Instrucciones getInstrucciones()
	{
		if (misInstrucciones==null)
		{
			misInstrucciones=new Instrucciones();
		}
		return misInstrucciones;
	}
	
	public void imprimirInstrucciones()
	{
		try {
			BufferedReader reader= new BufferedReader(new FileReader("C:\\Users\\Gorka\\Desktop\\ws\\PROYECTO\\src\\Ejemplo.txt"));
			String linea;
			while((linea=reader.readLine())!=null)
			{
				System.out.print("\n");
				System.out.print(linea);
			}
			Teclado.getTeclado().TeclearParaContinuar();

		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	
}
