package com.inge.edecisiones;

import java.util.List;

public class ConsultarTendencia extends SubControlador{
	//añadir nombre de ventanas y eso
	protected static final String CONSULTAR_TENDENCIAS = "ConsultarTendencia";
	
	//ventanas
	VConsultarTendencia vConsultarTendencia;
	
	public ConsultarTendencia(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	//sobreescribir métodos y añadir métodos del controlador
	public void initViews(){
		vConsultarTendencia = new VConsultarTendencia(this);
		
		controlador.getNavigator().addView(CONSULTAR_TENDENCIAS, vConsultarTendencia);
	}
	
	public void ir_a_inicio(){
		//preparacion previa
		List<String> lista_tendencias = getEdecisiones().getControladorBD().RecuperarListaTendencias(
				getEdecisiones().getPlebiscito().GetID());
		vConsultarTendencia.llenarListaTendencias(lista_tendencias);
		
		controlador.getNavigator().navigateTo(CONSULTAR_TENDENCIAS);
	}
	
	//metodos propios
	public void consultarTendencia(String nombre){
		int idt = getEdecisiones().getControladorBD().RecuperarIdTendencia(
				getEdecisiones().getPlebiscito().GetID(), nombre);
		Tendencia tendencia = getEdecisiones().getControladorBD().RecuperarTendencia(idt);
		vConsultarTendencia.limpiar();
		vConsultarTendencia.cargarDatos(tendencia);
	}
}
