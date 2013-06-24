/**
 * @(#) InfoPlebiscito.java
 */

package com.inge.edecisiones;

import java.lang.String;
import java.util.Date;

public class InfoPlebiscito
{
	private String Nombre;
	
	private String Descripcion;
	
	private boolean Tipo;
	
	private boolean Acceso;
	
	private Date[] PeriodoDiscusion = new Date[2];
	
	private Date[] PeriodoInscripcionTendencias = new Date[2];
	
	private Date[] PeriodoVotacion = new Date[2];
	
	private Date PublicacionResultados;
	
	public void SetNombre( String nom )
	{
		Nombre = nom;
	}
	
	public void SetDescripcion( String des )
	{
		Descripcion = des;
	}
	
	
	/**
	 * Tipo: true = plebiscito. false = eleccion.
	 */
	public void SetTipo( boolean ty )
	{
		Tipo = ty;
	}
	
	/**
	 * true = publico
	 */
	public void SetAcceso( boolean b )
	{
		Acceso = b;
	}
	
	public void SetPeriodoDiscusion( Date pdi, Date pdf )
	{
		PeriodoDiscusion[0] = pdi;
		PeriodoDiscusion[1] = pdf;
	}
	
	public void SetPeriodoInscripcion( Date pii, Date pif )
	{
		PeriodoInscripcionTendencias[0] = pii;
		PeriodoInscripcionTendencias[1] = pif;
	}
	
	public void SetPeriodoVotacion( Date pvi, Date pvf )
	{
		PeriodoVotacion[0] = pvi;
		PeriodoVotacion[1] = pvf;
	}
	
	public void SetPublicacionResultados( Date pr )
	{
		PublicacionResultados = pr;
	}
	
	public String GetNombre( )
	{
		return Nombre;
	}
	
	public String GetDescripcion( )
	{
		return Descripcion;
	}
	
	public boolean GetTipo( )
	{
		return Tipo;
	}
	
	public boolean GetAcceso( )
	{
		return Acceso;
	}
	
	public Date[] GetPeriodoDiscusion( )
	{
		return PeriodoDiscusion;
	}
	
	public Date[] GetPeriodoInscripcion( )
	{
		return PeriodoInscripcionTendencias;
	}
	
	public Date[] GetPeriodoVotacion( )
	{
		return PeriodoVotacion;
	}
	
	public Date GetPublicacionResultados( )
	{
		return PublicacionResultados;
	}
	
	
}
