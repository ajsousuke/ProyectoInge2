package com.inge.edecisiones;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class Menu extends HorizontalLayout implements LoginStatusChangeListener {
	
	/**	 */
	private static final long serialVersionUID = 1L;

	/** */
	private Edecisiones controlador;
	
	/** Botones que proveen acceso a cada caso de uso */
	private Button log_in_out;
	private Button panelUsuario;	
	private Button consultarPlebiscito;
	private Button sign_up;
	private TextField  usr_email;
	
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
		controlador.addListener(this);
	}
	
	private void buildMenu() {
		this.setSizeFull();
		// crear botones
		log_in_out = new Button();
		panelUsuario = new Button("Panel de Usuario");		
		consultarPlebiscito = new Button("Consultar Plebiscitos");
		sign_up = new Button("Registrarse");
		usr_email = new TextField("Acceso como: ");
				
		// habilitar botones de panel de usuario y de log in / out
		loginStatusChange( controlador.usuarioLogin() );		
		
		// meterles el listener
		initButtonListeners();
		
		// add al menu
		this.addComponent(consultarPlebiscito);
		this.addComponent(log_in_out);
		this.addComponent(panelUsuario);
		this.addComponent(sign_up);
		this.addComponent(usr_email);
		
		this.setMargin(true);
	}
	

	public void initButtonListeners() {
		// ir a ventana de login o salir
		log_in_out.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if( controlador.usuarioLogin() ) { // si el usuario inicio sesion
					controlador.setUsuarioLogin(false); // quiere cerrar sesion
					controlador.getControlUsuario().ir_a_login();
				}
				else { // no ha iniciado sesion aun
					controlador.getControlUsuario().ir_a_login();
				}
			}
		});
		
		// ir a panel de usuario
		panelUsuario.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getControlUsuario().ir_a_panel();
			}
		});	
		
						
		// ir a CUS consultar plebiscito
		consultarPlebiscito.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getConsultarPlebiscito().ir_a_inicio();
			}
		});
		
		// ir a ventana de registrar usuario
		sign_up.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.getControlUsuario().registrar_usuario();
			}
		});	
	}

	/** 
	 * se dio un cambio en el estado de la sesion
	 * hay que cambiar los botones si el usuario
	 * inicio sesion o la cerro
	 * @param login: indica el nuevo estado
	 */
	@Override
	public void loginStatusChange(boolean login) {
		this.panelUsuario.setVisible(login);
		this.usr_email.setVisible(login);
		this.sign_up.setVisible(!login);		
		if( login ) { // inicio sesion
			log_in_out.setCaption("Cerrar sesión");
			usr_email.setReadOnly(false);
			usr_email.setValue( controlador.getUsuario().GetEmail() );
			usr_email.setReadOnly(true);
		}
		else { // sesion esta cerrada
			log_in_out.setCaption("Iniciar sesión");
		}				
	}	

} // END CLASS MENU
