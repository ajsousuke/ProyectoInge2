package com.inge.edecisiones;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class VPanelUsuario extends Panel implements View{

	/**	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Componentes de la ventana */
	private VerticalLayout mainLayout;
	private Panel menu;
	
	Button editarPadron;
	Button editarPlebiscito;
	Button editarTendencia;
	Button editarPerfil;
	
	Label notaPadron;
	Label notaPlebiscito;
	Label notaTendencia;
	Label notaPerfil;
	
	/** Controlador */
	ControlUsuario controlador;
	
	
	/** Constructor */
	public VPanelUsuario(ControlUsuario con) {
		// obligatorio:
		controlador = con;
		buildMainLayout();
		initBotones();
		// lo demas
		
		
	}
	
	/** Definir las acciones de los botones */
	private void initBotones() {
		// editar padron
		editarPadron.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.getEdecisiones().getRegistrarPadron().ir_a_inicio();				
			}
		});
		
		// editar plebiscito
		editarPlebiscito.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.getEdecisiones().getEditarPlebiscito().ir_a_inicio();				
			}
		});
		
		// ir a editar tendencia
		editarTendencia.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.getEdecisiones().getEditarTendencia().ir_a_inicio();				
			}
		});
		
		//ir a editar perfil
		editarPerfil.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controlador.getEdecisiones().getControlUsuario().editar_usuario();		
			}
		});
	}
	
	/** Contruir el contenido de la ventana*/
	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		
		/* agregar componentes al layout */
		
		// menu
		menu = new Panel();
		menu.setContent(new Menu(controlador.getEdecisiones()));
		mainLayout.addComponent(menu);
		
		// Boton editar plebiscito
		notaPlebiscito = new Label("Si desea editar sus plebiscitos presione:");
		mainLayout.addComponent(notaPlebiscito);
		
		editarPlebiscito = new Button("Editar plebiscitos");
		mainLayout.addComponent(editarPlebiscito);
		
		// Boton editar tendencia
		notaTendencia = new Label("Si desea editar sus tendencias presione:");
		mainLayout.addComponent(notaTendencia);
		
		editarTendencia = new Button("Editar tendencias");
		mainLayout.addComponent(editarTendencia);
		
		// Boton editar padron
		notaPadron = new Label("Si desea editar algun padrón presione:");
		mainLayout.addComponent(notaPadron);
		
		editarPadron = new Button("Editar padrón");
		mainLayout.addComponent(editarPadron);
		
		// Boton editar perfil
		notaPerfil = new Label("Si desea editar sus datos personales presione:");
		mainLayout.addComponent(notaPerfil);
		
		editarPerfil = new Button("Editar perfil");
		mainLayout.addComponent(editarPerfil);
		
				
		// definir el mainlayout como contenido del panel
		this.setContent(mainLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
