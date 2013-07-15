package com.inge.edecisiones;

import java.lang.String;
import java.util.Date;

public class RespuestaForo {
	
	private String Respuesta;	
	private int NumeroRespuesta;	
	private int ID_Plebiscito;	
	private int ID;	
	private int ID_Usuario;
	private String nombre_usuario;	
	private Date Fecha;
	private java.sql.Time hora;
	
	
	public RespuestaForo( ) {
		
	}
	
	public String getRespuesta() {
		return Respuesta;
	}

	public void setRespuesta(String respuesta) {
		Respuesta = respuesta;
	}

	public int getNumeroRespuesta() {
		return NumeroRespuesta;
	}

	public void setNumeroRespuesta(int numeroRespuesta) {
		NumeroRespuesta = numeroRespuesta;
	}

	public int getID_Plebiscito() {
		return ID_Plebiscito;
	}

	public void setID_Plebiscito(int iD_Plebiscito) {
		ID_Plebiscito = iD_Plebiscito;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getID_Usuario() {
		return ID_Usuario;
	}

	public void setID_Usuario(int iD_Usuario) {
		ID_Usuario = iD_Usuario;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public Date getFecha() {
		return Fecha;
	}

	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	
	public java.sql.Time getHora() {
		return hora;
	}

	public void setHora(java.sql.Time hora) {
		this.hora = hora;
	}
}
