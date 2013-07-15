package com.inge.edecisiones;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class VImportarPadron extends CustomComponent implements Receiver, SucceededListener, 
																	FailedListener, View{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private GridLayout gridLayout_1;
	@AutoGenerated
	private Button btn_Volver;
	@AutoGenerated
	private Button btn_Importar;
	@AutoGenerated
	private Upload upl_Padron;
	@AutoGenerated
	private Label lbl_Texto3;
	@AutoGenerated
	private Label lbl_Texto2;
	@AutoGenerated
	private Label lbl_Texto1;
	@AutoGenerated
	private Panel Menu;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	private String rutaArchivo="";
	private Importador imp;
	private Padron p;
	private RegistrarPadron controlador;
	private ByteArrayOutputStream arc;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public VImportarPadron(final RegistrarPadron controlador) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.controlador = controlador;
		imp = new Importador();
		
		Menu.setContent(new Menu (controlador.getEdecisiones()));
		Menu.setSizeUndefined();	
		
		//cambiar de página
		btn_Volver.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				controlador.cargarPadron();
			}
		});
		
		btn_Importar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				boolean error = false;
				
				if(!rutaArchivo.equals("")){

					//String separador = txf_Caracter.getValue().equals("")? ",":txf_Caracter.getValue();
					//ArrayList<String[]> t = imp.importar(rutaArchivo, separador);
					//ArrayList<String[]> t = imp.importar(new ByteArrayInputStream(arc.toByteArray()), separador);
					ArrayList<String> t = imp.importar(new ByteArrayInputStream(arc.toByteArray()));
					controlador.guardar(t);
					File archivo = new File(rutaArchivo);
					archivo.delete();
				}
				else{
					Notification.show("No se suministro ningun archivo");
				}
			}
		});
		
		upl_Padron.setReceiver(this);
		upl_Padron.addFailedListener(this);
		upl_Padron.addSucceededListener(this);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		Notification.show("Se subió el archivo correctamente");
		rutaArchivo = event.getFilename();
		/*File archivo = new File(event.getFilename());
		rutaArchivo = event.getFilename();
		try{
			FileOutputStream fos = new FileOutputStream(archivo);
			fos.write(arc.toByteArray());
			fos.flush();
			fos.close();
		}
		catch(Exception e){
			
		}*/
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub
		Notification.show(/*"Hubo un error al subir el archivo"*/event.getReason().getMessage());
		event.getReason();
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		/*FileOutputStream fos = null;
		File archivo; //archivo que se va a escribir momentaneamente en disco
		
		try{
			rutaArchivo = filename;
			archivo = new File(rutaArchivo);
			fos = new FileOutputStream(archivo);
		}
		catch(java.io.FileNotFoundException e){
			e.printStackTrace();
		}
		return fos;*/
		arc = new ByteArrayOutputStream();
		return arc;
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
		
		// Menu
		Menu = buildMenu();
		mainLayout.addComponent(Menu);
		
		// lbl_Texto1
		lbl_Texto1 = new Label();
		lbl_Texto1.setImmediate(false);
		lbl_Texto1.setWidth("-1px");
		lbl_Texto1.setHeight("-1px");
		lbl_Texto1
				.setValue("Indique el nombre del archivo desde el cual desea importar un padron.En el archivo deben de venir los datos de los ciudadanos línea por línea.");
		mainLayout.addComponent(lbl_Texto1);
		
		// lbl_Texto2
		lbl_Texto2 = new Label();
		lbl_Texto2.setImmediate(false);
		lbl_Texto2.setWidth("-1px");
		lbl_Texto2.setHeight("-1px");
		lbl_Texto2.setValue("Cédula");
		mainLayout.addComponent(lbl_Texto2);
		mainLayout.setComponentAlignment(lbl_Texto2, new Alignment(48));
		
		// lbl_Texto3
		lbl_Texto3 = new Label();
		lbl_Texto3.setImmediate(false);
		lbl_Texto3.setWidth("-1px");
		lbl_Texto3.setHeight("-1px");
		lbl_Texto3.setValue("Estos datos deben números de cédulas válidos.");
		mainLayout.addComponent(lbl_Texto3);
		
		// upl_Padron
		upl_Padron = new Upload();
		upl_Padron.setImmediate(false);
		upl_Padron.setWidth("-1px");
		upl_Padron.setHeight("-1px");
		mainLayout.addComponent(upl_Padron);
		
		// gridLayout_1
		gridLayout_1 = buildGridLayout_1();
		mainLayout.addComponent(gridLayout_1);
		mainLayout.setComponentAlignment(gridLayout_1, new Alignment(48));
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildMenu() {
		// common part: create layout
		Menu = new Panel();
		Menu.setImmediate(false);
		Menu.setWidth("100px");
		Menu.setHeight("30px");
		
		// verticalLayout_2
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("100.0%");
		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(false);
		Menu.setContent(verticalLayout_2);
		
		return Menu;
	}

	@AutoGenerated
	private GridLayout buildGridLayout_1() {
		// common part: create layout
		gridLayout_1 = new GridLayout();
		gridLayout_1.setImmediate(false);
		gridLayout_1.setWidth("193px");
		gridLayout_1.setHeight("-1px");
		gridLayout_1.setMargin(false);
		gridLayout_1.setSpacing(true);
		gridLayout_1.setColumns(2);
		
		// btn_Importar
		btn_Importar = new Button();
		btn_Importar.setCaption("Importar");
		btn_Importar.setImmediate(true);
		btn_Importar.setWidth("-1px");
		btn_Importar.setHeight("-1px");
		gridLayout_1.addComponent(btn_Importar, 0, 0);
		gridLayout_1.setComponentAlignment(btn_Importar, new Alignment(33));
		
		// btn_Volver
		btn_Volver = new Button();
		btn_Volver.setCaption("Volver");
		btn_Volver.setImmediate(true);
		btn_Volver.setWidth("-1px");
		btn_Volver.setHeight("-1px");
		gridLayout_1.addComponent(btn_Volver, 1, 0);
		gridLayout_1.setComponentAlignment(btn_Volver, new Alignment(34));
		
		return gridLayout_1;
	}

}
