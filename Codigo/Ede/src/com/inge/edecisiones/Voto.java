/**
 * @(#) Voto.java
 */

package com.inge.edecisiones;

public class Voto
{
	private int ID;
	
	private int ID_Tendencia;
	
	private int ID_Usuario;
	
	public Voto( int ten, int usr )
	{
		this.ID_Tendencia = ten;
		this.ID_Usuario = usr;
	}
	
	public void SetID( int id )
	{
		this.ID = id;
	}
	
	public int GetID_Tendencia(){
		return this.ID_Tendencia;
	}
	
	public int GetId_Usuario(){
		return this.ID_Usuario;
	}
}
