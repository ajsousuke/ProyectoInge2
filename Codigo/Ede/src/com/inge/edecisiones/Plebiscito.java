/**
 * @(#) Plebiscito.java
 */

package com.inge.edecisiones;

import java.util.List;

public class Plebiscito
{
	private int ID;
	private InfoPlebiscito Informacion;
	
//	private java.util.List<Tendencia> Tendencias;
//	
//	private Resultados ResultadosFinales;	
//	
//	private Foro ForoDiscucion;
//	
//	private Padron Padron;
	
	
	
	public Plebiscito() {
		Informacion = new InfoPlebiscito();
	}
	
	public void setInfoPlebiscito(InfoPlebiscito info) {
		Informacion = info;		
	}
	
//	public void AddTendencia( Tendencia t )
//	{
//		
//	}
//	
//	public void SetTendencias( Tendencia t )
//	{
//		
//	}
//	
//	public void SetResultadosFinales( Resultados r )
//	{
//		
//	}
//	
//	public void SetPadron( Padron p )
//	{
//		
//	}
	
	public int GetID( )
	{
		return ID;
	}
	
	public InfoPlebiscito GetInfoPlebiscito( )
	{
		return Informacion;
	}

	public void SetID(int id) {
		// TODO Auto-generated method stub
		this.ID = id;
	}
	
//	public Foro GetForoDiscucion( )
//	{
//		return null;
//	}
//	
//	public Padron GetPadron( )
//	{
//		return null;
//	}
//	
//	public Resultados GetResultadosFinales( )
//	{
//		return null;
//	}
//	
//	public List<Tendencia> GetTendencias( )
//	{
//		return null;
//	}
	
	
}
