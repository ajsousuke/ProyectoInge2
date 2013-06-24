package com.inge.edecisiones;

public class SubControlador {
	
	// referencia al controlador principal
	protected Edecisiones controlador;
	
	public SubControlador(Edecisiones e) {
		controlador = e;
		initViews();
	}

	public void initViews() {

	}	

	public void ir_a_inicio() {
		
	}
	
	public Edecisiones getEdecisiones() {
		return controlador;
	}	
	
}
