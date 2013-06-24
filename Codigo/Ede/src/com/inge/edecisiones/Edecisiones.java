package com.inge.edecisiones;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.navigator.Navigator;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class Edecisiones extends UI {
	
	/*- Variables que usa toda la aplicacion */
	ControladorBD controladorBD;
	Navigator navigator;
	Plebiscito plebiscito;
	
	/*- Controladores de las ventanas */
	//	Allan
	EditarTendencia editarTendencia;
	EditarPlebiscito editarPlebiscito;
	// 	Ariel
	RegistrarPadron registarPadron;
	Votar votar;
	
	
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
//		Panel p = new Panel();
//		p.setSizeUndefined();
//		p.setContent(new Menu(navigator));
//			
//			
//		layout.addComponent(p);	
		
		setContent(layout);		
		
		// Inicializa variables de la aplicacion
		this.navigator = new Navigator(this,this);
		controladorBD = new ControladorBD();
		
		// Provisional
		//aqui se recupera un plebiscito q tenga un periodo de votacion
		//y de paso, tiene que haber en el padron un mae
		//con cedula 301750586 para que funque bien el de votar
		this.plebiscito = controladorBD.RecuperarPlebiscito("Plebiscito_Prueba");
		
		//
		initControllers();		
		editarTendencia.ir_a_inicio();		
	}
	
	public void initControllers() {		
		editarTendencia = new EditarTendencia(this);
		editarPlebiscito = new EditarPlebiscito(this);
		registarPadron = new RegistrarPadron(this);
		votar = new Votar(this);
	}
	
	/**
	 * Todos los getters
	 * 
	 */
	
	public ControladorBD getControladorBD() {
		return controladorBD;
	}
	
	public Navigator getNavigator() {
			return navigator;		
	}
	
	public Plebiscito getPlebiscito(){
		return plebiscito;
	}
	
	public EditarPlebiscito getEditarPlebiscito() {
		return editarPlebiscito;
	}
	
	public EditarTendencia getEditarTendencia() {
		return editarTendencia;
	}
	
	public RegistrarPadron getRegistrarPadron(){
		return registarPadron;
	}
	
	public Votar getVotar(){
		return votar;
	}
}