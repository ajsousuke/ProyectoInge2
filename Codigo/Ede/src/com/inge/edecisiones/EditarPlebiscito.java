package com.inge.edecisiones;

import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;

public class EditarPlebiscito extends SubControlador {
	
	/*- Errores de validez de los datos */
	
	private enum ErrorDatos {OK, NOMBRE_REPETIDO};
	
	/*- Nombres de la ventas */
	protected static final String NUEVO_PLEBISCITO = "NuevoPlebiscito";
	protected static final String EDITAR_PLEBISCITO = "EditarPlebiscito";
	
	/*- Ventanas */
	VNuevoPlebiscito nuevoPlebiscito;
	VEditarPlebiscito editarPlebiscito;
	
	/*- utilitarios */
	boolean editar; // indica si se esta editando un plebiscito ya existente
	Plebiscito plebiscito; // Plebiscito que se guardará.
	
	
	public EditarPlebiscito(Edecisiones e) {
		super(e);
	}
	
	public void initViews(){
		nuevoPlebiscito = new VNuevoPlebiscito(this);
		editarPlebiscito = new VEditarPlebiscito(this);
		controlador.getNavigator().addView(NUEVO_PLEBISCITO, nuevoPlebiscito);
		controlador.getNavigator().addView(EDITAR_PLEBISCITO, editarPlebiscito);		
	}
	
	/* 
	 * Estos metodos implementan la funcionalidad de los botones de las ventanas
	 *  */
	
	/**
	 * ir a la ventana de inicio
	 */
	public void ir_a_inicio() {
		// preparacion previa
		List<String> lista_plebiscitos = controlador.getControladorBD().RecuperarListaPlebiscitos(
				controlador.getUsuario().GetID() );
		
		nuevoPlebiscito.llenarListaPlebiscitos(lista_plebiscitos);
		
		// por ahora volver al inicio del caso de uso
		controlador.getNavigator().navigateTo(NUEVO_PLEBISCITO);
	}
	
	/**
	 * para crear uno nuevo hay q limpiar los contenidos de la ventana
	 * antes de presentarla
	 */
	public void crearNuevo() {
		editar = false;
		
		plebiscito = new Plebiscito();
		plebiscito.setIdUsuarioCreador(  controlador.getUsuario().GetID()  );
		
		editarPlebiscito.setInfoPlebiscito(plebiscito.GetInfoPlebiscito());
		editarPlebiscito.limpiar();
		
		controlador.getNavigator().navigateTo(EDITAR_PLEBISCITO);
	}
	
	/**
	 * al editar un plebiscito, hay que llenar la 
	 * ventana de edicion con los datos del
	 * plebiscito seleccionado.
	 * @param nombre: el nombre del plebiscito 
	 * seleccionado
	 */
	public void editarExistente(String nombre){
		editar = true;
		
		plebiscito = controlador.getControladorBD().RecuperarPlebiscito(nombre);
		if (plebiscito != null) {
			editarPlebiscito.setInfoPlebiscito(plebiscito.GetInfoPlebiscito());
			editarPlebiscito.llenar();
			
			controlador.getNavigator().navigateTo(EDITAR_PLEBISCITO);
		}
		else {
			Notification.show("editarExistente: Se produjo un error");
		}
	}	
	
	
	/**
	 * Si se esta editando un plebiscito la BD
	 * se debe actualizar.
	 * Si no, es un nuevo plebiscito y se
	 * debe insertar en la BD.
	 */
	public void guardar() {
		if(editar == true) {
			controlador.getControladorBD().EditarPlebiscito(plebiscito);
		}
		else {
			controlador.getControladorBD().InsertarPlebiscito(plebiscito);
		}		
	}
	
	/**
	 * Borrar el plebiscito seleccionado
	 */
	public void eliminar(String nombrep) {
		// se elimina de la base de datos
		controlador.getControladorBD().EliminarPlebiscito(nombrep);
		
		//se actualiza la ventana:
		List<String> lista_plebiscitos = controlador.getControladorBD().RecuperarListaPlebiscitos(
				controlador.getUsuario().GetID() );
		nuevoPlebiscito.llenarListaPlebiscitos(lista_plebiscitos);
	}	
	
	/**
	 * Revisa si los datos del plebiscito a ingresar son
	 * validos.
	 * Ej: al crear uno nuevo este no
	 * puede tener el nombre de uno ya existente
	 * @return true si es valido
	 */
	public boolean datosValidos() {
		ErrorDatos error = checkDatos();
		boolean valido = false;
		
		switch (error) {
			case OK:
				valido = true;
				break;
				
			case NOMBRE_REPETIDO:
				valido = false;
				Notification.show("Error: Ya existe otro plebiscito con ese nombre");
	
			default:
				Notification.show("Hay un error");
				break;
		}
		
		return valido;
	}
	
	/**
	 * Revisa el plebiscito a ver si hay errores.
	 * @return el tipo de error
	 * en el plebiscito
	 */
	private ErrorDatos checkDatos() {
		ErrorDatos error = ErrorDatos.OK;
		
		// revisar que no existe plebiscito
		// con mismo nombre, esto solo si se esta creando un nuevo plebiscito
		if(editar == false) {
			List<String> lista_plebiscitos =
					controlador.getControladorBD().RecuperarListaPlebiscitos(
							controlador.getUsuario().GetID());
			error =
					lista_plebiscitos.contains(plebiscito.GetInfoPlebiscito().GetNombre())?ErrorDatos.NOMBRE_REPETIDO: ErrorDatos.OK;
		}
		
		// revisar si el nuevo nombre que se le da al plebiscito
		// es igual al de otro plebliscito existente.
		if(editar == true) {
			// buscar si existe plebliscito con el nuevo nombre
			Plebiscito otro;
			otro = 
				controlador.getControladorBD().RecuperarPlebiscito(plebiscito.GetInfoPlebiscito().GetNombre());
			// si existe
			if (otro != null) {
				// y es un plebiscito diferente al que se esta editando=> error
				// si es el mismo plebiscito, quiere decir que no se le cambio el nombre
				error = otro.GetID()!=plebiscito.GetID()? ErrorDatos.NOMBRE_REPETIDO:ErrorDatos.OK;
			}
		}
		
		return error;
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
	
	public Plebiscito getPlebiscito() {
		return plebiscito;
	}
}
