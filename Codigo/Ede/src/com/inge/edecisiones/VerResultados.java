package com.inge.edecisiones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.Notification;

public class VerResultados extends SubControlador{
	//añadir nombre de ventana(s) y variables
	protected static final String VER_RESULTADOS = "VerResultados";
	
	//Ventanas
	VVerResultados vVerResultados;
	
	//Atributos propios
	boolean volver;
	
	public VerResultados(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	//sobreescribir métodos y añadir métodos propios
	public void initViews(){
		vVerResultados = new VVerResultados(this);
		
		getEdecisiones().getNavigator().addView(VER_RESULTADOS, vVerResultados);
	}
	
	public void ir_a_inicio(){
		volver = true;
		//preparación previa -> pedir los datos
		InfoPlebiscito info = getEdecisiones().getPlebiscito().GetInfoPlebiscito();
		Date[] periodoVotacion = info.GetPeriodoVotacion();
		Date resultados = info.GetPublicacionResultados();
		Date fechaActual = new Date();
		
		if((fechaActual.after(periodoVotacion[0]) && fechaActual.before(periodoVotacion[1]))
				|| (fechaActual.after(resultados))){
			setEstadisticas();
			setGenero();
			getEdecisiones().getNavigator().navigateTo(VER_RESULTADOS);
		}
		else{
			Notification.show("El plebiscito no se en encuentra en periodo de publicación de resultados.");
		}
		
	}
	
	public void setVolver(boolean volver){
		this.volver = volver;
	}
	
	public void volver(){
		if(volver)
			getEdecisiones().getConsultarPlebiscito().verDescripcion_Actual();
		else
			getEdecisiones().getVotar().ir_a_inicio();
	}
	
	public List<String> getDatos(){
		return getEdecisiones().getControladorBD().recuperarEstadisticasVotantes(
				getEdecisiones().getPlebiscito()); 
	}
	
	public void setEstadisticas(){
		ArrayList<String> general = (ArrayList<String>) getEdecisiones().getControladorBD().recuperarEstadisticas(
													getEdecisiones().getPlebiscito());
		//metodo set para la ventana
		vVerResultados.graficarGeneral(general);
	}
	
	public void setGenero(){
		ArrayList<String> genero = (ArrayList<String>) getEdecisiones().getControladorBD().recuperarEstadisticasGenero(
				getEdecisiones().getPlebiscito());
		//metodo set para la ventana
		vVerResultados.graficarGenero(genero);
	}
	
	public List<String> getGenerales(){
		List <String> generales = getEdecisiones().getControladorBD().recuperarEstadisticas(
				getEdecisiones().getPlebiscito());
		
		return generales;
	}
	
	public List<String> getFechas(){
		List <String> generales = new ArrayList<String>();
		InfoPlebiscito info = getEdecisiones().getPlebiscito().GetInfoPlebiscito();
		Date[] periodoVotacion = info.GetPeriodoVotacion();

		generales.add(periodoVotacion[0].getDate()+"/"+periodoVotacion[0].getMonth()+"/"+periodoVotacion[0].getYear());
		generales.add(periodoVotacion[1].getDate()+"/"+periodoVotacion[1].getMonth()+"/"+periodoVotacion[1].getYear());
		
		return generales;
	}
}
