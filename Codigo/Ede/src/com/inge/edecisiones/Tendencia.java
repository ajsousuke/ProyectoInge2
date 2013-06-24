/**
 * @(#) Tendencia.java
 */

package com.inge.edecisiones;

import java.awt.Image;
import java.io.InputStream;

public class Tendencia
{
	private int ID;
	
	private String Nombre;
	
	private String Descripcion;
	
	private InputStream Imagen;
	
	private String nombreImagen;
	
	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	private String InfoContacto;
	
	private java.util.List<String> LinksExternos;
	
	private int ID_Plebiscito;
	
	public Tendencia( int idp )
	{
		this.ID_Plebiscito = idp;
	}
	
	public Tendencia() {
		// TODO Auto-generated constructor stub
	}

	public void SetID( int id )
	{
		this.ID = id;
	}
	
	public void SetNombre( String nombre )
	{
		this.Nombre = nombre;
	}
	
	public void SetDescripcion( String descripcion )
	{
		this.Descripcion = descripcion;
	}
	
	public void SetImagen( InputStream imagen )
	{
		this.Imagen = imagen;
	}
	
	public void SetInfoContacto( String info )
	{
		this.InfoContacto = info;
	}
	
	public void SetLinksExternos( java.util.List<String> links )
	{
		this.LinksExternos = links;
	}
	
	public void SetIDPlebiscito( int idP )
	{
		this.ID_Plebiscito = idP;
	}
	
	public void AddLinkExterno( String link )
	{
		this.LinksExternos.add(link);
	}
	
	public String GetNombre( )
	{
		return this.Nombre;
	}
	
	public String GetDescripcion( )
	{
		return this.Descripcion;
	}
	
	public InputStream GetImagen( )
	{
		return this.Imagen;
	}
	
	public String GetInfoContacto( )
	{
		return this.InfoContacto;
	}
	
	public java.util.List<String> GetLinksExternos( )
	{
		return this.LinksExternos;
	}
	
	public int GetID_Plebiscito( )
	{
		return this.ID_Plebiscito;
	}

	public int GetID() {
		return this.ID;
	}
	
	
}
