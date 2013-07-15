package com.inge.edecisiones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class Edecisiones extends UI implements LoginStatusChangeNotifier {
	
	/*- Variables que usa toda la aplicacion */
	private ControladorBD controladorBD;
	private Navigator navigator;
	private Plebiscito plebiscito;
	private boolean usuario_login; // para saber si el usuario inicio sesion
	private List<LoginStatusChangeListener> listeners;
	private Usuario usuario;
	
	/*- Controladores de las ventanas */
	//	Allan
	private EditarTendencia editarTendencia;
	private EditarPlebiscito editarPlebiscito;
	private ConsultarPlebiscito consultarPlebiscito;
	private ParticiparForo participarForo;
	private ControlUsuario controlUsuario;
	// 	Ariel
	private RegistrarPadron registarPadron;
	private Votar votar;
	private ConsultarTendencia consultarTendencia;
	private VerResultados verResultados;
	
	
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		setContent(layout);	
		
		// Inicializa variables de la aplicacion
		this.navigator = new Navigator(this,this);
		controladorBD = new ControladorBD();
		usuario_login = false;
		listeners = new ArrayList<LoginStatusChangeListener>();
		
		// Provisional
		this.plebiscito = new Plebiscito();
		plebiscito.SetID(1);
		
		// inicializa los controladores
		initControllers();		
		
		//Entrada por defecto:
		//editarTendencia.ir_a_inicio();
		//consultarPlebiscito.ir_a_inicio();
		controlUsuario.ir_a_login();
	}
	
	/** Inicializa los subcontroladores de las ventanas */
	public void initControllers() {		
		editarTendencia = new EditarTendencia(this);
		editarPlebiscito = new EditarPlebiscito(this);
		registarPadron = new RegistrarPadron(this);
		votar = new Votar(this);
		
		// 2nda entrega:
		consultarPlebiscito = new ConsultarPlebiscito(this);
		participarForo      = new ParticiparForo(this);
		controlUsuario      = new ControlUsuario(this);
		
		consultarTendencia	= new ConsultarTendencia(this);
		verResultados 		= new VerResultados(this);	// no hace nada por el momento
	}
	
	/** 
	 * Setters
	 * */
	
	public void setPlebiscito(Plebiscito p) {
		this.plebiscito = p;
	} 
	
	/** si hay un cambio en el estado de la sesion de usuario*/
	public void setUsuarioLogin(boolean login) {
		this.usuario_login = login; // cambie el estado
		this.notificar(); // notifique a los listeners
	}
	
	public void setUsuario(Usuario usr) {
		this.usuario = usr;
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
	
	public ConsultarTendencia getConsultarTendencia() {
		return consultarTendencia;
	}

	public VerResultados getVerResultados() {
		return verResultados;
	}

	public Votar getVotar(){
		return votar;
	}
	
	public ConsultarPlebiscito getConsultarPlebiscito() {
		return consultarPlebiscito;
	}

	public ParticiparForo getParticiparForo() {
		return participarForo;
	}
	
	public ControlUsuario getControlUsuario(){
		return this.controlUsuario;
	}
	
	public boolean usuarioLogin() {
		return usuario_login;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	
	/**
	 *	Metodos para notificar que el usuario inicio sesion. 
	 */
	
	@Override
	public void notificar() {
		for(LoginStatusChangeListener listener : this.listeners) {
			listener.loginStatusChange(usuario_login);
		}
	}

	@Override
	public void addListener(LoginStatusChangeListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(LoginStatusChangeListener listener) {
		this.listeners.remove(listener);		
	}
}