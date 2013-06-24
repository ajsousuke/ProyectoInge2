package com.inge.edecisiones;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

public class Menu extends HorizontalLayout{
	
	/**	 */
	private static final long serialVersionUID = 1L;

	/** */
	private Edecisiones controlador;
	
	/** Botones que proveen acceso a cada caso de uso */
	private Button editarPlebiscito;
	private Button editarTendencia;
	private Button registrarPadron;
	private Button votar;
	
/* ESTO SIRVE P NADA: Listener para los botones */
//	class ButtonListener implements Button.ClickListener {
//
//		SubControlador subcontrolador;
//        public ButtonListener(SubControlador subcon) {
//            this.subcontrolador = subcon;
//        }
//
//		@Override
//		public void buttonClick(ClickEvent event) {
//			// TODO Auto-generated method stub
//			//obvio q no sirve pues llama al metodo de la superclase que esta vacio
//			subcontrolador.ir_a_inicio();
//		}
//    }
	
	/**
	 * Necesita referencia al controlador principal
	 * */
	public Menu(Edecisiones con) {
		this.controlador = con;
		buildMenu();
	}
	
	private void buildMenu() {
		this.setSizeFull();
		// crear botones
		editarPlebiscito = new Button("Editar Plebiscito");
		editarTendencia = new Button("Editar Tendencia");
		registrarPadron = new Button("Editar Padron");
		votar = new Button("Votar");
		
		// meterles el listener
		initButtonListeners();
		
		// add al menu
		this.addComponent(editarPlebiscito);
		this.addComponent(editarTendencia);
		this.addComponent(registrarPadron);
		this.addComponent(votar);
		this.setCaption("Presione alguno de estos botones para cambiar de menu");
		this.setMargin(true);
	}
	

	public void initButtonListeners() {
		// ir a CUS editar plebiscito
		editarPlebiscito.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getEditarPlebiscito().ir_a_inicio();
			}
		});
		
		// ir a CUS editar tendencia
		editarTendencia.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getEditarTendencia().ir_a_inicio();
			}
		});	
		
		// ir a CUS registrar padron
		registrarPadron.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getRegistrarPadron().ir_a_inicio();
			}
		});
		
		// ir a CUS votar
		votar.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getVotar().ir_a_inicio();
			}
		});
	}	

} // END CLASS MENU
