package com.inge.edecisiones;

import java.util.List;

public class Foro {
	private List<RespuestaForo> Respuestas;
	
	private int ID_Plebiscito;
	
	public Foro( int p ) {
		ID_Plebiscito = p;
	}
	
	public void setRespuestas( List<RespuestaForo> r ) {
		this.Respuestas = r;
	}
	
	public  List<RespuestaForo> getRespuestas() {
		return Respuestas;
	}
	
	public void AddRespuestaForo( RespuestaForo r )	{
		this.Respuestas.add(r);
	}	
}
