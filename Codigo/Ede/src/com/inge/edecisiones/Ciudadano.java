/**
 * @(#) Ciudadano.java
 */

package com.inge.edecisiones;

public class Ciudadano
{
	private String Nombre;
	
	private int Cedula;
	
	private String Apellido1;
	
	private String Apellido2;
	
	private int ID;
	
	public Ciudadano( String n, String a1, String a2, int c )
	{
		this.Nombre = n;
		this.Apellido1 = a1;
		this.Apellido2 = a2;
		this.Cedula = c;
	}
	
	public void SetID( int id )
	{
		this.ID = id;
	}
	
	public String GetName( )
	{
		return this.Nombre+" "+this.Apellido1+" "+this.Apellido2;
	}
	
	public int GetCedula( )
	{
		return this.Cedula;
	}
	
	
}
