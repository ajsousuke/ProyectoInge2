/**
 * @(#) Ciudadano.java
 */

package com.inge.edecisiones;

import java.util.Date;

public class Ciudadano
{
	private String Nombre;
	
	private int Cedula;
	
	private String Apellido1;
	
	private String Apellido2;
	
	private boolean Sexo;
	
	private int ID;
	
	private Date FechaNac;

	public Ciudadano( String n, String a1, String a2, int c )
	{
		this.Nombre = n;
		this.Apellido1 = a1;
		this.Apellido2 = a2;
		this.Cedula = c;
	}
	
	public void setCedula(int ced) {
		this.Cedula = ced;
	}
	
	public Ciudadano() {
		// TODO Auto-generated constructor stub
	}

	public void SetID( int id )
	{
		this.ID = id;
	}
	
	public void SetSexo(boolean sexo){
		this.Sexo = sexo;
	}
	
	public void SetFechaNac(Date fecha){
		this.FechaNac = fecha;
	}

	public String GetName( )
	{
		return this.Nombre+" "+this.Apellido1+" "+this.Apellido2;
	}
	
	public int GetCedula( )
	{
		return this.Cedula;
	}
	
	public boolean GetSexo(){
		return this.Sexo;
	}

	public Date GetFechaNac(){
		return this.FechaNac;
	}
}
