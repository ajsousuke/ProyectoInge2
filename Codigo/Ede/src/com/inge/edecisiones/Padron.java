/**
 * @(#) Padrón.java
 */

package com.inge.edecisiones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Padron
{
	private int ID_Plebiscito;
	
	private java.util.ArrayList<Ciudadano> Ciudadanos;
	
	public Padron ( int id )
	{
		this.ID_Plebiscito = id;
		this.Ciudadanos = new ArrayList<Ciudadano>();
	}
	
	public List<Ciudadano> GetCiudadanos( )
	{
		return this.Ciudadanos;
	}
	
	public void AddCiudadano( Ciudadano c )
	{
		Ciudadanos.add(c);
	}
	
	public int GetID_Plebiscito( )
	{
		return this.ID_Plebiscito;
	}
	
}
