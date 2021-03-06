package com.inge.edecisiones;

import java.io.InputStream;
import java.util.List;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class VConsultarTendencia extends CustomComponent implements View{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout vrl_tendencia;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;
	@AutoGenerated
	private Button btn_Volver;
	@AutoGenerated
	private Button btn_Consultar;
	@AutoGenerated
	private ComboBox cbx_Tendencias;
	@AutoGenerated
	private Panel panel_Menu;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	private ConsultarTendencia controlador;
	private Menu menu;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public VConsultarTendencia(final ConsultarTendencia controlador) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		mainLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
		
		// TODO add user code here
		this.controlador = controlador;
		
		panel_Menu.setContent(new Menu(controlador.getEdecisiones()));
		panel_Menu.setSizeUndefined();
		
		limpiar();
		
		//listeners
		btn_Consultar.addClickListener(new Button.ClickListener(
				) {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String nombreTendencia = cbx_Tendencias.getValue().toString();
				controlador.consultarTendencia(nombreTendencia);
			}
		});
		
		cbx_Tendencias.setImmediate(true);
		//agregar el listener
	}
	
	public void llenarListaTendencias(List<String> lista_nombres){
		if (! cbx_Tendencias.removeAllItems() ) {
			Notification.show("Error en VConsultarPadron");
		}
		
		if( lista_nombres.isEmpty() ) {
			cbx_Tendencias.setCaption("No hay tendencias para consultar");	
		}
		else {
			cbx_Tendencias.setCaption("Seleccione un plebiscito y presione \"Consultar\"");
			
			for(String nombre_plebiscito : lista_nombres) {
				cbx_Tendencias.addItem(nombre_plebiscito);				
			}
		}
	}
	
	public void limpiar(){
		//pnl_Tendencia.setVisible(false);
		vrl_tendencia.removeAllComponents();
	}

	//se encarga de cargar los datos de la tendencia escogida
	public void cargarDatos(Tendencia tendencia){
		
		//cargar cosas
		HorizontalLayout hrl_nombre = new HorizontalLayout();
		
		hrl_nombre.addComponent(new Label(tendencia.GetNombre()));
		if(tendencia.GetImagen()!=null){
			//cargar la imagen en el panel
			StreamResource.StreamSource ss = new Imagen(tendencia.GetImagen());
			StreamResource sr = new StreamResource(ss, tendencia.getNombreImagen());
			Image img = new Image("", sr);
			img.setVisible(true);
			Panel pnl_imagen = new Panel(img);
			pnl_imagen.setSizeUndefined();
			hrl_nombre.addComponent(pnl_imagen);
		}
		vrl_tendencia.addComponent(hrl_nombre);
		
		vrl_tendencia.addComponent(new Label("Descripcion"));
		vrl_tendencia.addComponent(new TextArea("",tendencia.GetDescripcion()));
		
		
		vrl_tendencia.addComponent(new Label("Links"));
		VerticalLayout vrl_links = new VerticalLayout();
		List<String> links = tendencia.GetLinksExternos();
		//System.out.println(links.size());
		for(int i=0; i<links.size(); i++){
			String link = links.get(i);
			System.out.println(link);
			vrl_links.addComponent(new Link("Link "+(i+1), new ExternalResource(link)));
		}
		vrl_tendencia.addComponent(vrl_links);
		
		vrl_tendencia.addComponent(new Label("Información de Contacto"));
		if(tendencia.GetInfoContacto()!=null)
			vrl_tendencia.addComponent(new TextArea("",tendencia.GetInfoContacto()));
		
		//mainLayout.setSizeUndefined();
		mainLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
	}
	
	class Imagen implements StreamResource.StreamSource{
		InputStream is = null;
		
		public Imagen(InputStream is){
			this.is = is;
		}
		
		@Override
		public InputStream getStream() {
			return this.is;
		}
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// panel_Menu
		panel_Menu = buildPanel_Menu();
		mainLayout.addComponent(panel_Menu);
		mainLayout.setComponentAlignment(panel_Menu, new Alignment(33));
		
		// cbx_Tendencias
		cbx_Tendencias = new ComboBox();
		cbx_Tendencias
				.setCaption("Seleccione una tendencia y presione \"Consultar\"");
		cbx_Tendencias.setImmediate(false);
		cbx_Tendencias.setWidth("-1px");
		cbx_Tendencias.setHeight("-1px");
		mainLayout.addComponent(cbx_Tendencias);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		mainLayout.addComponent(horizontalLayout_2);
		
		// vrl_tendencia
		vrl_tendencia = new VerticalLayout();
		vrl_tendencia.setImmediate(false);
		vrl_tendencia.setWidth("-1px");
		vrl_tendencia.setHeight("-1px");
		vrl_tendencia.setMargin(false);
		mainLayout.addComponent(vrl_tendencia);
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildPanel_Menu() {
		// common part: create layout
		panel_Menu = new Panel();
		panel_Menu.setImmediate(false);
		panel_Menu.setWidth("100px");
		panel_Menu.setHeight("30px");
		
		// verticalLayout_2
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("100.0%");
		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(false);
		panel_Menu.setContent(verticalLayout_2);
		
		return panel_Menu;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		
		// btn_Consultar
		btn_Consultar = new Button();
		btn_Consultar.setCaption("Consultar");
		btn_Consultar.setImmediate(true);
		btn_Consultar.setWidth("-1px");
		btn_Consultar.setHeight("-1px");
		horizontalLayout_2.addComponent(btn_Consultar);
		
		// btn_Volver
		btn_Volver = new Button();
		btn_Volver.setCaption("Volver");
		btn_Volver.setImmediate(true);
		btn_Volver.setWidth("-1px");
		btn_Volver.setHeight("-1px");
		horizontalLayout_2.addComponent(btn_Volver);
		
		return horizontalLayout_2;
	}

}
