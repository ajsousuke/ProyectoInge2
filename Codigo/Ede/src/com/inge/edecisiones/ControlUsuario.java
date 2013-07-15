package com.inge.edecisiones;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;

public class ControlUsuario extends SubControlador {
	
	/** Nombres de las ventas */
	protected static final String PANEL_USUARIO = "Panel de Usuario";
	protected static final String LOGIN = "Login";
	protected static final String EDITAR_USUARIO = "EditarUsuario";
	
	/** Ventanas */
	VPanelUsuario vPanelUsuario;
	VEditarUsuario vEditarUsuario;
	VLogIn vLogin;
	boolean editar;
	
	/** Constructor*/
	public ControlUsuario(Edecisiones e) {
		super(e);
		// TODO Auto-generated constructor stub
	}
	
	public void initViews() {
		vPanelUsuario = new VPanelUsuario(this);
		vEditarUsuario = new VEditarUsuario(this);
		this.vLogin = new VLogIn(this);
		
		controlador.getNavigator().addView(PANEL_USUARIO, vPanelUsuario);
		controlador.getNavigator().addView(LOGIN, vLogin);
		controlador.getNavigator().addView(EDITAR_USUARIO, vEditarUsuario);
	}
	
	/** por ahora no se ocupa*/
	public void ir_a_inicio() {
		// operaciones previas
		
		// cargar ventana
		// EJ: 
		// controlador.getNavigator().navigateTo(CONSULTAR_PLEBISCITOS);
		controlador.getNavigator().navigateTo(EDITAR_USUARIO);
	}
	
	/** Solo para cargar el panel de usuario */
	public void ir_a_panel() {
		controlador.getNavigator().navigateTo(PANEL_USUARIO);		
	}
	
	/** Carga la ventana de editar datos del usuario logeado */
	public void editar_usuario() {
		editar = true;
		Usuario usr = controlador.getUsuario();
		usr.setDatosCiudadano(controlador.getControladorBD().recuperarCiudadano(usr));
		
		vEditarUsuario.limpiarDatos();
		vEditarUsuario.setUsuario(usr);
		vEditarUsuario.cargarDatos();
		
		controlador.getNavigator().navigateTo(EDITAR_USUARIO);
	}
	
	/** Carga la ventana de login */
	public void ir_a_login() {
		controlador.getNavigator().navigateTo(LOGIN);
	}
	
	/** Carga la ventada de registrar nuevo usuario*/
	public void registrar_usuario() {
		editar = false;
		Usuario usr = new Usuario(null);
		
		vEditarUsuario.limpiarDatos();
		vEditarUsuario.setUsuario(usr);
		
		controlador.getNavigator().navigateTo(EDITAR_USUARIO);
	}
	
	/** Inicia sesion
	 * Verifica si el correo y password suministrados
	 * se encuentran en la base de datos.
	 * */
	public boolean iniciar_sesion(String correo, String password) {
		boolean aceptado = false;
		Usuario usr = controlador.getControladorBD().usuarioValido(correo, password);
		
		if( usr != null ) {
			aceptado = true;
			controlador.setUsuario(usr);
			controlador.setUsuarioLogin(aceptado);
		}
		
		return aceptado;
	}
	
	//Valida los datos del usuario
	public void validar(Usuario usuario){
		String formato_email = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		
		if(!usuario.GetEmail().matches(formato_email))
			Notification.show("Correo invalido");
		else{
			guardar(usuario);
		}
	}
	
	//Guarda los datos en la base de datos
	public void guardar(Usuario usuario){
		if(editar){
			controlador.getControladorBD().EditarUsuario(usuario);
			//pasar a la otra ventana
		}
		else{
			controlador.getControladorBD().InsertarUsuario(usuario);
			//pasar a la otra ventana
		}
		Notification.show("Usuario Registrado");
		if ( controlador.usuarioLogin() ) {
			this.ir_a_panel();
		} else {
			this.ir_a_login();
		}
	}
	
	/**- 
	 * Getters 
	 * */
	public Navigator getNavigator() {
		return controlador.getNavigator();
	}
	
	public ControladorBD getControladorBD() {
		return controlador.getControladorBD();
	}	
	
}
