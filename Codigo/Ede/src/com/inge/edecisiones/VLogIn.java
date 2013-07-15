package com.inge.edecisiones;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.PasswordField;

public class VLogIn extends Panel implements View, Button.ClickListener {

	/**	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Componentes de la ventana */
	private VerticalLayout mainLayout;
	private Panel menu;
	
	private TextField entradaCorreo;
	private PasswordField entradaPassword;
	private Button login;
	
	/** Controlador */
	ControlUsuario controlador;
	
	
	/** Constructor */
	public VLogIn(ControlUsuario con) {
		// obligatorio:
		controlador = con;
		buildMainLayout();
		initBotones();
		// lo demas		
	}
	
	/** Definir las acciones de los botones */
	private void initBotones() {
		login.addClickListener(this);
	}
	
	/** Contruir el contenido de la ventana*/
	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		
		/* agregar componentes al layout */
		
		// menu
		menu = new Panel();
		menu.setContent(new Menu(controlador.getEdecisiones()));
		mainLayout.addComponent(menu);
		
		// entrada correo
		entradaCorreo = new TextField("Digite su correo");
		mainLayout.addComponent(entradaCorreo);
		
		// entrada password
		entradaPassword = new PasswordField("Digite su password");
		mainLayout.addComponent(entradaPassword);
		
		// boton login
		this.login = new Button("Iniciar Sesion");
		mainLayout.addComponent(login);
				
		// definir el mainlayout como contenido del panel
		this.setContent(mainLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		entradaCorreo.setValue("");
		entradaPassword.setValue("");
		entradaCorreo.focus();		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		boolean aceptado =
				controlador.iniciar_sesion(entradaCorreo.getValue(), entradaPassword.getValue());
		if( !aceptado ) {
			Notification.show("Alguno de los datos es erroneo");
			this.entradaCorreo.focus();
		}	
		else {
			Notification.show("Bienvenido");
			controlador.ir_a_panel();
		}
	}

}
