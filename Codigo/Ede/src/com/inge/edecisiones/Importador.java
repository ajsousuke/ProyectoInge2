package com.inge.edecisiones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Importador {
	
	public Importador(){
		//no se inicializa nada
	}

	public ArrayList<String[]> importar(String nombre, String separador){
		ArrayList<String[]> retorno = new ArrayList<String[]>();
		String linea = null;
		String datos [];
		
		try{
			BufferedReader lector = new BufferedReader(new FileReader(nombre));
			
			
			while((linea = lector.readLine()) != null){
				datos = linea.split(separador);
				retorno.add(datos);
			}
			
			lector.close();
		}
		catch(IOException io){
			//realizar algo
		}
		
		return retorno;
	}
	
	public ArrayList<String[]> importar(InputStream is, String separador){
		ArrayList<String[]> retorno = new ArrayList<String[]>();
		String linea = null;
		String datos [];
		
		try{
			BufferedReader lector;
			lector = new BufferedReader(new InputStreamReader(is));
			
			while((linea = lector.readLine()) != null){
				datos = linea.split(separador);
				retorno.add(datos);
			}
			
			lector.close();
		}
		catch(IOException io){
			//realizar algo
		}
		
		return retorno;
	}
	
	public ArrayList<String> importar(InputStream is){
		ArrayList<String> retorno = new ArrayList<String>();
		String linea = null;
		String datos [];
		
		try{
			BufferedReader lector;
			lector = new BufferedReader(new InputStreamReader(is));
			
			while((linea = lector.readLine()) != null){
				retorno.add(linea);
			}
			
			lector.close();
		}
		catch(IOException io){
			//realizar algo
		}
		
		return retorno;
	}
}
