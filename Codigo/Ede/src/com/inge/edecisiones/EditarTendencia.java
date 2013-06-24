package com.inge.edecisiones;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class EditarTendencia extends SubControlador  implements Receiver, SucceededListener {
	
	/*- Errores en datos de tendencia*/
	private enum error_datos_tendencia { OK, LINK_MALFORMADO, NOMBRE_TENDENCIA_REPETIDO};
	
	/*- Nombres de la ventas */
	protected static final String NUEVA_TENDENCIA = "NuevaTendencia";
	protected static final String EDITAR_TENDENCIA = "EditarTendencia";
	
	/*- Ventanas */
	VNuevaTendencia nuevaTendencia;
	VEditarTendencia editarTendencia;
	
	/*- utilitarios */
	boolean editar; 
	Tendencia tendencia;
	ByteArrayOutputStream recibeImagen;
	
	public EditarTendencia(Edecisiones e) {
		super(e);
	}
	
	@Override
	public void initViews() {
		nuevaTendencia = new VNuevaTendencia(this);
		editarTendencia= new VEditarTendencia(this);
		controlador.getNavigator().addView(NUEVA_TENDENCIA, nuevaTendencia);
		controlador.getNavigator().addView(EDITAR_TENDENCIA, editarTendencia);	
	}
	
	/* 
	 * Estos metodos implementan la funcionalidad de los botones de las ventanas
	 *  */
	
	@Override
	public void ir_a_inicio() {
		// preparacion previa
		List<String> lista_plebiscitos = controlador.getControladorBD().RecuperarListaPlebiscitos();
		nuevaTendencia.llenarListaPlebiscitos(lista_plebiscitos);
		nuevaTendencia.initListaTendencias();
		
		// cargar ventana
		this.controlador.getNavigator().navigateTo(NUEVA_TENDENCIA);
	}
	
	/**
	 * Crear nueva tendencia
	 * asigna a la tendencia creada el id del 
	 * plebiscito seleccionado
	 */
	public void crearNuevo(String nom_plebiscito) {
		editar = false;
		
		tendencia = new Tendencia();
		tendencia.SetID(-1);	
		int idp = controlador.getControladorBD().RecuperarIdPlebiscito(nom_plebiscito);
		tendencia.SetIDPlebiscito(idp);
		
		editarTendencia.setTendencia(tendencia);		
		editarTendencia.limpiar();
		
		this.controlador.getNavigator().navigateTo(EDITAR_TENDENCIA);
	}
	
	/**
	 * al editar una tendencia, hay que llenar la 
	 * ventana de edicion con los datos de la
	 * tendencia seleccionada
	 * 
	 * Esto incluye presentar la imagen de la tendencia
	 * si hay una
	 * 
	 * @param nom_tendencia: el nombre de la tendencia 
	 * @param nom_plebiscito: nombre del plebiscito al cual pertenece
	 * seleccionada
	 */
	public void editarExistente(String nom_plebiscito, String nom_tendencia){
		editar = true;
		
		int id_plebiscito = getControladorBD().RecuperarIdPlebiscito(nom_plebiscito);
		int id_tendencia = getControladorBD().RecuperarIdTendencia(id_plebiscito, nom_tendencia);		
		tendencia = getControladorBD().RecuperarTendencia(id_tendencia);
		
		editarTendencia.setTendencia(tendencia);
		editarTendencia.llenar();
		
		// presentar la imagen si la hay
		if( tendencia.GetImagen() != null) {
			StreamResource.StreamSource imagensource = new FuenteImagen(tendencia.GetImagen());
			
			StreamResource recurso_imagen = new StreamResource(imagensource, tendencia.getNombreImagen());
			editarTendencia.mostrarImagen(recurso_imagen);
		}		
		
		controlador.getNavigator().navigateTo(EDITAR_TENDENCIA);
	}
	
	/**
	 * Guardar a la base de datos la
	 * tendencia
	 */
	public void guardar() {
		// TODO Auto-generated method stub
		if( editar == true ) {
			controlador.getControladorBD().EditarTendencia(tendencia);
		}
		else {
			controlador.getControladorBD().InsertarTendencia(tendencia);
		}
	}
	
	/**
	 * Eliminar la tendencia de nombre
	 * @param nom_plebiscito
	 * del plebiscito de nombre.
	 * @param nom_tendencia
	 * de la base de datos
	 */
	public void eliminar(String nom_plebiscito, String nom_tendencia) {
		// eliminar la tendencia de la bd
		int id_plebiscito = getControladorBD().RecuperarIdPlebiscito(nom_plebiscito);
		int id_tendencia = getControladorBD().RecuperarIdTendencia(id_plebiscito, nom_tendencia);
		
		controlador.getControladorBD().EliminarTendencia(id_tendencia);
		
		// actualizar la venta
		nuevaTendencia.initListaTendencias();
	}
	
	/**
	 * revisa que los datos suministrados 
	 * son validos
	 * @return true si son validos, false si
	 * presental algun error
	 */
	public boolean datosValidos() {
		// TODO Auto-generated method stub
		error_datos_tendencia codigo_error = checkDatos();
		boolean esValido = false;
		
		switch (codigo_error ) {
			case OK:
				esValido = true;
				break;
			case NOMBRE_TENDENCIA_REPETIDO:
				esValido = false;
			default:
				esValido = true;
				break;
		}
		
		return true;
	}	
	
	/**
	 * Revisa el plebiscito a ver si hay errores.
	 * @return el tipo de error
	 * en el plebiscito
	 */
	private error_datos_tendencia checkDatos() {
		error_datos_tendencia codigo_error = error_datos_tendencia.OK;
		
		// revisar si el nombre de la tendencia ya existe
		if( nombre_tendencia_repetido() ) {
			return error_datos_tendencia.NOMBRE_TENDENCIA_REPETIDO;
		}
		
		return codigo_error;
	}
	
	/**
	 * revisa si el nombre de tendencia
	 * a registrar se 
	 * se puede usar
	 */
	private boolean nombre_tendencia_repetido() {
		boolean rep = false;
		
		// datos de la tendencia actual
		int id_p_actual = tendencia.GetID_Plebiscito();
		String nom_t_actual = tendencia.GetNombre();
		
		// se busca en la bd una tendencia de el plebiscito seleccionado
		// que tenga el nombre que se le dio a la tendencia que se va a guardar
		int id_tendencia_en_bd = controlador.getControladorBD().RecuperarIdTendencia(id_p_actual, nom_t_actual ); 
		
		// si id_tendencia_en_db != 1 => en la bd hay una tendencia
		// que ya tiene el nombre de la que se esta registrando
		if( id_tendencia_en_bd != -1) {
			rep = ( id_tendencia_en_bd == tendencia.GetID() ) ? false:true; 
		}
		
		return rep;
	}
	
	/**
	 * clase para obtener la fuente
	 * de una imagen
	 * 
	 */
	public class FuenteImagen implements StreamSource {		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		InputStream datos; 
		
		public FuenteImagen(ByteArrayOutputStream os) {
			datos = new ByteArrayInputStream(os.toByteArray());
		}
		
		public FuenteImagen(InputStream is) {
			datos = is;
		}

		@Override
		public InputStream getStream() {
			// TODO Auto-generated method stub
			return datos;
		}		
	}
	
	/** 
	 * todos los getters
	 */
	
	public Navigator getNavigator() {
		return controlador.getNavigator();
	}
	
	public ControladorBD getControladorBD() {
		return controlador.getControladorBD();
	}
	
	/**
	 * metodos para manejar
	 * la subida de archivos
	 */

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		// guardar la imgen en la tendencia
		
		StreamResource.StreamSource imagensource = new FuenteImagen(recibeImagen);
		tendencia.SetImagen(imagensource.getStream());
		System.out.println("uploadsucceded: se coloco imagen en tendencia");
		tendencia.setNombreImagen(event.getFilename());
		
		StreamResource recurso_imagen = new StreamResource(imagensource, event.getFilename());
		editarTendencia.mostrarImagen(recurso_imagen);
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		recibeImagen = new ByteArrayOutputStream();
		return recibeImagen;
	}
}
