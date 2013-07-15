/**
 * @(#) Usuario.java
 */

package com.inge.edecisiones;

import java.io.InputStream;

public class Usuario
{
	private int ID;
	
	private String Email;
	
	private String Password;
	
	private InputStream Foto;
	
	private Ciudadano DatosCiudadano;
	
	
	public Usuario( Ciudadano c )
	{
		this.DatosCiudadano = c;
	}
	
	public void SetID( int id )
	{
		this.ID = id;
	}
	
	public void SetEmail( String email )
	{
		this.Email = email;
	}
	
	public void SetContrasena( String pass )
	{
		this.Password = pass;
	}
	
	public void SetFoto(InputStream foto)
	{
		this.Foto = foto;
	}	

	public Ciudadano getDatosCiudadano( )
	{
		return this.DatosCiudadano;
	}
	
	public int GetID( )
	{
		return this.ID;
	}
	
	public String GetEmail( )
	{
		return this.Email;
	}
	
	public String GetPassword( )
	{
		return this.Password;
	}
	
	public InputStream GetFoto( )
	{
		return this.Foto;
	}
	
	public void setDatosCiudadano(Ciudadano c) {
		this.DatosCiudadano = c;
	}
	
}
