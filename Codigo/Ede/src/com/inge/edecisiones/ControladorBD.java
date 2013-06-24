/**
 * @(#) ControladorBD.java
 */

package com.inge.edecisiones;

import java.lang.String;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vaadin.addon.sqlcontainer.SQLContainer;
import com.vaadin.addon.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.addon.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.addon.sqlcontainer.query.FreeformQuery;
import com.vaadin.addon.sqlcontainer.query.TableQuery;

public class ControladorBD
{
	private String NombreBD = "jdbc:mysql://127.0.0.1:3306/Edecisiones_BD";	
	private String Usuario = "root";	
	private String Password = "";
	
	private JDBCConnectionPool conexion = null;
	private Connection conn = null;
	
	public ControladorBD() {
			Conectar();	
	}
	
	public void Conectar( )	{
		try {
            /*conexion = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    NombreBD, Usuario, "", 2, 5);*/
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(NombreBD,Usuario,Password);
            System.out.println("se pudo conectar a la bd");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("no se pudo conectar a la bd");
        }
	}
	
	public void Cerrar() {
		try{
			conn.close();
		}
		catch(Exception e){
			
		}
	}
	
	public void recuperarCiudadano(){
		try{
			PreparedStatement st = conn.prepareStatement("select * from Persona");
			ResultSet rs = st.executeQuery();
			
			//con el metodo next se va desplazando por las tuplas
			//de la respuesta
			while(rs.next()){
				//el ResultSet tiene metodos para convertir datos de sql
				//a datos de java, tienen el formato:
				//                rs.getTipoJava("NombreAtributo");
				System.out.println(rs.getInt("Cedula")+" "+rs.getString("Nombre")
						+" "+rs.getString("Apellido1")+" "+rs.getString("Apellido2"));
			}
		}
		catch(Exception e){
		}
	}
	
	public void InsertarCiudadano(Ciudadano c, int plebiscito) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Persona (Cedula,Nombre,Apellido1,Apellido2) values (? ,? ,? ,?)");
			String[] nombre = c.GetName().split(" ");
			
			st.setInt(1, c.GetCedula());
			st.setString(2, nombre[0]);
			st.setString(3, nombre[1]);
			st.setString(4, nombre[2]);
			
			int rs = st.executeUpdate();
			
			st = conn.prepareStatement("insert into Padron (Persona_Cedula,Plebiscito_Id_Plebiscito)"+
					"values (?,?)");
			
			st.setInt(1, c.GetCedula());
			st.setInt(2, plebiscito);
			
			rs = st.executeUpdate();
			
			if(rs > 0)
				System.out.println("Se inserto correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EliminarCiudadano(Ciudadano c) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"delete from Persona where Cedula = ?");
			st.setInt(1, c.GetCedula());
			
			int rs = st.executeUpdate();
			
			if(rs>0)
				System.out.println("Se elimino correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EditarCiudadano(Ciudadano c, int Cedula) {
		try{
			PreparedStatement st = conn.prepareStatement(
					"update Persona set Nombre = ?, Apellido1 = ?, Apellido2 = ? " +
					"where Cedula = ? ");
			String[] nombre = c.GetName().split(" ");
			
			st.setString(1, nombre[0]);
			st.setString(2, nombre[1]);
			st.setString(3, nombre[2]);
			st.setInt(4, Cedula);
			
			int rs = st.executeUpdate();
			
			if(rs > 0)
				System.out.println("Se actualizo correctamente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param pe
	 * @return
	 */
	public Padron recuperarPadron(Plebiscito pe){
		Padron p = null;
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select pe.Cedula, pe.Nombre, pe.Apellido1, pe.Apellido2 "+
					"from Persona pe join Padron pa on pa.Persona_Cedula = pe.Cedula "+
					"where pa.Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, pe.GetID());
			
			ResultSet rs = st.executeQuery();
			
			p = new Padron(pe.GetID());
			while(rs.next()){
				Ciudadano c = new Ciudadano(rs.getString("Nombre"), rs.getString("Apellido1"),
						rs.getString("Apellido2"), rs.getInt("Cedula"));
				p.AddCiudadano(c);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return p;
	}
	
	/**
	 * 
	 */
	public void insertarPadron(Padron p){
		try{
			List<Ciudadano> padron = p.GetCiudadanos();
			for(Ciudadano c : padron){
				PreparedStatement st = conn.prepareStatement(
						"insert into Persona (Cedula,Nombre,Apellido1,Apellido2) values (? ,? ,? ,?)");
				String[] nombre = c.GetName().split(" ");
				st.setInt(1, c.GetCedula());
				st.setString(2, nombre[0]);
				st.setString(3, nombre[1]);
				st.setString(4, nombre[2]);
				
				int rs = st.executeUpdate();
				
				st = conn.prepareStatement("insert into Padron (Persona_Cedula,Plebiscito_Id_Plebiscito)"+
						"values (?,?)");
				st.setInt(1, c.GetCedula());
				st.setInt(2, p.GetID_Plebiscito());
				
				rs = st.executeUpdate();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupera una lista de las tendencias del plebiscito p
	 */
	public List<Tendencia> RecuperarTendencias(Plebiscito p){
		ArrayList<Tendencia> retorno = new ArrayList<Tendencia>();
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select Id_Tendencia,Nombre_Tendencia,Fotografia "+
					"from Tendencia where Plebiscito_Id_Plebiscito = ?");
			st.setInt(1, p.GetID());
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				Tendencia t = new Tendencia(p.GetID());
				t.SetNombre(rs.getString("Nombre_Tendencia"));
				t.SetID(rs.getInt("Id_Tendencia"));
				t.SetImagen(rs.getBinaryStream("Fotografia"));
				retorno.add(t);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//System.out.println("Cantidad tuplas: "+retorno.size());
		return retorno;
	}
	
	/**
	 * Recupera una lista con los nombres de las tendencias
	 * del plebiscito de id:
	 * @param idp
	 * @return la lista que contiene los nombres
	 */
	public List<String> RecuperarListaTendencias( int idp ) {
		List<String> resultado = new ArrayList<String>();
		
		PreparedStatement rec;
		ResultSet result;
		
		try {
			String query = "select Nombre_Tendencia from Tendencia " +
					"where Plebiscito_Id_Plebiscito = ?";
			
			 rec = conn.prepareStatement(query);
			 rec.setInt(1, idp);
			 
			 result = rec.executeQuery();
			 
			 while( result.next() ) {
				 resultado.add(result.getString("Nombre_Tendencia"));
			 } 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	/**
	 * Recupera la tendencia de nombre cuyo id es 
	 * @param idt
	 * @return null si no existe la tendencia. La tendencia si 
	 * esta existe.
	 * NOTA: 
	 * Los siguentes campos del objeto tendencia devuelto pueden ser null:
	 * - Imagen
	 * - NombreImagen
	 */
	public Tendencia RecuperarTendencia(int idt) {
		Tendencia tendencia = null;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Tendencia, Nombre_Tendencia, Fotografia, Nombre_Fotografia, Descripcion, Info_contacto, Plebiscito_Id_Plebiscito " +
				"FROM Tendencia " +
				"WHERE Id_Tendencia = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idt);
			
			result = rec.executeQuery();
			
			if( result.first() ) {
				tendencia = new Tendencia();
				
				//recuperar los not null
				tendencia.SetID(idt); // esto ya lo sabia
				tendencia.SetIDPlebiscito(result.getInt("Plebiscito_Id_Plebiscito"));
				tendencia.SetNombre(result.getString("Nombre_Tendencia"));
				tendencia.SetDescripcion(result.getString("Descripcion"));
				tendencia.SetInfoContacto(result.getString("Info_contacto"));
				
				// los links externos siempre son una lista (no un campo null)
				// incluso si esta vacia. 
				tendencia.SetLinksExternos( RecuperarLinksExternos(idt) );
				
				// estos campos si pueden quedar null
				tendencia.SetImagen(result.getBinaryStream("Fotografia"));	
				// si hay imagen, pedir el nombre
				if( tendencia.GetImagen() != null ) {
					tendencia.setNombreImagen(result.getString("Nombre_Fotografia"));
				}
				else {
					tendencia.setNombreImagen(null);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tendencia;
	}
	
	/**
	 * Recupera los links externos de un tendencia de id:
	 * @param idt
	 * @return una lista con los links externos
	 */
	public List<String> RecuperarLinksExternos(int idt) {
		List<String> resultado = new ArrayList<String>();
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT LinkExterno FROM LinksExternos " +
				"WHERE Tendencia_Id_Tendencia = ?";
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idt);
			
			result = rec.executeQuery();
			
			 while( result.next() ) {
				 resultado.add(result.getString("LinkExterno"));
			 } 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	/**
	 * Recupera el id de la tendencia del plebiscito con id
	 * @param idp
	 * y cuyo nombre de tendencia es
	 * @param nombret
	 * @return el id de la tendencia si existe, si no -1
	 */
	public int RecuperarIdTendencia(int idp, String nombret) {
		int id = -1;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Tendencia FROM Tendencia " +
				"WHERE Plebiscito_Id_Plebiscito = ? AND Nombre_Tendencia = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setInt(1, idp);
			rec.setString(2, nombret);
			
			result = rec.executeQuery();
			
			if ( result.first() ) {
				id = result.getInt("Id_Tendencia");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return id;
	}
	
	/**
	 * Recupera el ide del plebiscito de nombre:
	 * @param nombrep
	 * @return el id del plebiscito si existe, de lo contrario -1
	 */
	public int RecuperarIdPlebiscito(String nombrep) {
		int id = -1;
		
		PreparedStatement rec;
		ResultSet result;
		
		String sql = "SELECT Id_Plebiscito FROM Plebiscito " +
				"WHERE Nombre_Plebiscito = ?";
		
		try {
			rec = conn.prepareStatement(sql);
			rec.setString(1, nombrep);
			
			result = rec.executeQuery();
			
			if ( result.first() ) {
				id = result.getInt("Id_Plebiscito");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return id;
	}
	
	
	/**
	 * 
	 * @param nombre
	 * @return
	 */
	public Plebiscito RecuperarPlebiscito( String nombre ) {
		Plebiscito resultado = null;
		PreparedStatement recuperaP;
		ResultSet pRecuperado;
		try {
			String queryP = "SELECT Id_Plebiscito, Nombre_Plebiscito, Tipo, Acceso, Descripcion, " +
					"Votacion_Inicio, Votacion_Final, Insc_Tend_Inicio, Insc_Tend_Final, " +
					"Discusion_Inicio, Discusion_Final, Resultados " +
					"FROM Plebiscito "+
					"where Nombre_Plebiscito = ?";
			
			recuperaP = conn.prepareStatement(queryP);
			recuperaP.setString(1,nombre);
			
			pRecuperado = recuperaP.executeQuery();
			
			// si se recupero al menos una fila:
			if ( pRecuperado.first() ) {
				resultado = new Plebiscito();
				resultado.SetID(pRecuperado.getInt("Id_Plebiscito"));
				
				resultado.GetInfoPlebiscito().SetNombre(pRecuperado.getString("Nombre_Plebiscito"));
				resultado.GetInfoPlebiscito().SetTipo(pRecuperado.getBoolean("Tipo"));
				resultado.GetInfoPlebiscito().SetAcceso(pRecuperado.getBoolean("Acceso"));
				resultado.GetInfoPlebiscito().SetDescripcion(pRecuperado.getString("Descripcion"));
				
				resultado.GetInfoPlebiscito().SetPeriodoVotacion(pRecuperado.getDate("Votacion_Inicio"),pRecuperado.getDate("Votacion_Final"));
				resultado.GetInfoPlebiscito().SetPeriodoInscripcion(pRecuperado.getDate("Insc_Tend_Inicio"),pRecuperado.getDate("Insc_Tend_Final"));
				resultado.GetInfoPlebiscito().SetPeriodoDiscusion(pRecuperado.getDate("Discusion_Inicio"),pRecuperado.getDate("Discusion_Final") );				
				resultado.GetInfoPlebiscito().SetPublicacionResultados(pRecuperado.getDate("Resultados"));
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public void recuperarPadron() {
		
	}
	
	/**
	 * recupera los nombres de los plebiscitos
	 * @return lista que contiene los nombres
	 */
	public List<String> RecuperarListaPlebiscitos() {
		// TODO Auto-generated method stub
		List<String> resultado = new ArrayList<String>();
		PreparedStatement rec;
		ResultSet result;
		
		try {
			 rec = conn.prepareStatement("select Nombre_Plebiscito from Plebiscito");
			 result = rec.executeQuery();
			 
			 while( result.next() ) {
				 resultado.add(result.getString("Nombre_Plebiscito"));
			 } 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	/**
	 * 	recupera el voto de un ciudadano en un plebiscito
	 * @param p
	 */
	public boolean RecuperarVoto(int Cedula, Plebiscito p){
		boolean retorno = false;
		
		try{
			PreparedStatement st = conn.prepareStatement(
					"select * from Voto where Persona_Cedula = ? and " +
					"Tendencia_Id_Tendencia in " +
					"(select Id_Tendencia from Tendencia " +
					"where Plebiscito_Id_Plebiscito = ? )");
			st.setInt(1, Cedula);
			st.setInt(2, p.GetID());
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
				retorno = true;
		}
		catch(Exception e){
			
		}
		
		return retorno;
	}
	
	
//	public Resultados RecuperarResultadosFinales( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Foro RecuperarForoDiscusion( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Padron RecuperarPadron( Plebiscito p )
//	{
//		return null;
//	}
//	
//	public Usuario RecuperarUsuario( String usr, String cont )
//	{
//		return null;
//	}
//	
//	public GeneradorEstadisticas RecuperarEstadisticas( Plebiscito p )
//	{
//		return null;
//	}
	
	public void InsertarPlebiscito( Plebiscito p ) {
		InfoPlebiscito datos = p.GetInfoPlebiscito();
		
		String insquery = "INSERT INTO Plebiscito(Nombre_Plebiscito, Tipo, Acceso, Descripcion, " +
					"Votacion_Inicio, Votacion_Final, Insc_Tend_Inicio, Insc_Tend_Final, " +
					"Discusion_Inicio, Discusion_Final, Resultados) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(insquery);
			insertar.setString(1, datos.GetNombre());
			insertar.setBoolean(2, datos.GetTipo());
			insertar.setBoolean(3, datos.GetAcceso());
			insertar.setString(4, datos.GetDescripcion());
			
			java.sql.Date inivota = new java.sql.Date(datos.GetPeriodoVotacion()[0].getTime()); 
			java.sql.Date finvota= new java.sql.Date(datos.GetPeriodoVotacion()[1].getTime());
			insertar.setDate(5, inivota);
			insertar.setDate(6, finvota);
			
			java.sql.Date iniins = new java.sql.Date(datos.GetPeriodoInscripcion()[0].getTime());
			java.sql.Date finins= new java.sql.Date(datos.GetPeriodoInscripcion()[1].getTime());
			insertar.setDate(7, iniins);
			insertar.setDate(8, finins);
			
			java.sql.Date inidis= new java.sql.Date(datos.GetPeriodoDiscusion()[0].getTime());
			java.sql.Date findis= new java.sql.Date(datos.GetPeriodoDiscusion()[1].getTime());
			insertar.setDate(9, inidis);
			insertar.setDate(10, findis);
			
			java.sql.Date resuls= new java.sql.Date(datos.GetPublicacionResultados().getTime());
			insertar.setDate(11, resuls);
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("InsertarPlebiscito: Se inserto correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * inserta la tendencia
	 * @param t
	 */
	public void InsertarTendencia( Tendencia t ) {
		String sql = "INSERT INTO Tendencia(Nombre_Tendencia, Fotografia, " +
				"Nombre_Fotografia, Descripcion, Info_contacto, " +
				"Plebiscito_Id_Plebiscito) " +
				"VALUES (?, ?, ?, ?, ?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(sql);
			
			insertar.setString(1, t.GetNombre());
			
			if(t.GetImagen() == null) {
				System.out.println(": insertarten: no hay imagen");
				insertar.setNull(2, java.sql.Types.BLOB);
				insertar.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				insertar.setBlob(2, t.GetImagen());
				insertar.setString(3, t.getNombreImagen());
			}
			
			insertar.setString(4, t.GetDescripcion());
			insertar.setString(5, t.GetInfoContacto());
			insertar.setInt(6, t.GetID_Plebiscito());
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("InsertarTendencia: Se inserto correctamente, insertar links");
				
				// se supone que ya se inserto
				int idt = RecuperarIdTendencia(t.GetID_Plebiscito(), t.GetNombre());
				t.SetID(idt);
				
				if( t.GetLinksExternos() != null ) {
					InsertarLinksExternos( idt, t.GetLinksExternos() );
				}
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	/**
	 * inserta los links de la tendencia de id:
	 * @param idt
	 */
	public void InsertarLinksExternos(int idt, List<String> lista) {
		
		String sql = "INSERT INTO LinksExternos(Tendencia_Id_Tendencia, LinkExterno) " +
				"VALUES (?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			for(String link : lista) {
				insertar = conn.prepareStatement(sql);
				insertar.setInt(1, idt);
				insertar.setString(2, link);
				
				insertar.executeUpdate();				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * edita los links de la tendencia de id:
	 * @param idt
	 */
	public void EditarLinksExternos(int idt, List<String> lista) {
		List<String> originales = RecuperarLinksExternos(idt);
		
		
		String sql = "INSERT INTO LinksExternos(Tendencia_Id_Tendencia, LinkExterno) " +
				"VALUES (?, ?)";	
		
		PreparedStatement insertar;
		
		try {
			// primero insertar los nuevos
			for(String link : lista) {
				// si uno esta en la nueva lista y no en el original
				// es un nuevo link que debe ser agregado
				if( !originales.contains(link) ) {
					insertar = conn.prepareStatement(sql);
					insertar.setInt(1, idt);
					insertar.setString(2, link);
					if ( insertar.executeUpdate() > 1 ) {
						System.out.println("EditarLinksExternos: Se inserto correctamente");
					}
						
				}				
			}
			
			// ahora borrar los elminados
			PreparedStatement borrar;
			sql = "DELETE FROM LinksExternos " +
					"WHERE Tendencia_Id_Tendencia = ? AND LinkExterno = ?";
			
			for(String linkviejo : originales) {
				// si la lista mas reciente
				// no contiene un link en la lista original
				// es porque fue eliminado y se debe quitar
				// de la bd
				//System.out.println("tendencia " + idt + ": " +linkviejo);
				if( !lista.contains(linkviejo) ) {
					//System.out.println(": no esta en la nueva lista");
					
					borrar = conn.prepareStatement(sql);
					borrar.setInt(1, idt);
					borrar.setString(2, linkviejo);
					borrar.executeUpdate();					
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserta el voto en bd
	 * @param voto
	 */
	public void InsertarVoto(Voto voto){
		try{
			PreparedStatement st = conn.prepareStatement(
					"insert into Voto (Persona_Cedula,Tendencia_Id_Tendencia) " +
					"values (?,?)");
			
			st.setInt(1, voto.GetId_Usuario());
			st.setInt(2, voto.GetID_Tendencia());
			
			int rs = st.executeUpdate();
			
			if(rs>0)
				System.out.println("Se realizo el voto");
			else
				System.out.println("No se pudo realizar el voto");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
//	
//	public void InsertarRespuestaForo( RespuestaForo rf )
//	{
//		
//	}
//	
//	public void InsertarPadron( Padron pad )
//	{
//		
//	}
//	
//	public void InsertarUsuario( Usuario usr )
//	{
//		
//	}
	
	/**
	 * Cambia los valores del plebiscito p
	 * @param p
	 */
	public void EditarPlebiscito( Plebiscito p ) {
		InfoPlebiscito datos = p.GetInfoPlebiscito();
		String upquery = "UPDATE plebiscito " +
				"SET Nombre_Plebiscito = ?, Tipo = ?, Acceso = ?, Descripcion = ?, " +
				"Votacion_Inicio = ?, Votacion_Final = ?, Insc_Tend_Inicio = ?, Insc_Tend_Final = ?, " +
				"Discusion_Inicio = ?, Discusion_Final = ?, Resultados = ? " +
				"WHERE Id_Plebiscito = ?";
				

		PreparedStatement insertar;
		
		try {
			insertar = conn.prepareStatement(upquery);
			insertar.setString(1, datos.GetNombre());
			insertar.setBoolean(2, datos.GetTipo());
			insertar.setBoolean(3, datos.GetAcceso());
			insertar.setString(4, datos.GetDescripcion());
			
			java.sql.Date inivota = new java.sql.Date(datos.GetPeriodoVotacion()[0].getTime()); 
			java.sql.Date finvota= new java.sql.Date(datos.GetPeriodoVotacion()[1].getTime());
			insertar.setDate(5, inivota);
			insertar.setDate(6, finvota);
			
			java.sql.Date iniins = new java.sql.Date(datos.GetPeriodoInscripcion()[0].getTime());
			java.sql.Date finins= new java.sql.Date(datos.GetPeriodoInscripcion()[1].getTime());
			insertar.setDate(7, iniins);
			insertar.setDate(8, finins);
			
			java.sql.Date inidis= new java.sql.Date(datos.GetPeriodoDiscusion()[0].getTime());
			java.sql.Date findis= new java.sql.Date(datos.GetPeriodoDiscusion()[1].getTime());
			insertar.setDate(9, inidis);
			insertar.setDate(10, findis);
			
			java.sql.Date resuls= new java.sql.Date(datos.GetPublicacionResultados().getTime());
			insertar.setDate(11, resuls);
			
			insertar.setInt(12, p.GetID());
			
			int i = insertar.executeUpdate();
			if( i > 0) {
				System.out.println("EditarPlebiscito: Se edito correctamente el plebiscito");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void EditarTendencia( Tendencia t ) {
		String sql = "UPDATE Tendencia " +
				"SET Nombre_Tendencia = ?, Fotografia = ?, " +
				"Nombre_Fotografia = ?, Descripcion = ?, Info_contacto = ?, " +
				"Plebiscito_Id_Plebiscito = ? " +
				"WHERE Id_Tendencia = ?";	
		
		PreparedStatement editar;
		
		try {
			editar = conn.prepareStatement(sql);
			
			editar.setString(1, t.GetNombre());
			
			if(t.GetImagen() == null) {
				//System.out.println("editarten: no hay imagen");
				editar.setNull(2, java.sql.Types.BLOB);
				editar.setNull(3, java.sql.Types.VARCHAR);
			}
			else {
				
				ByteArrayInputStream ba = (ByteArrayInputStream)(t.GetImagen());
				System.out.println(ba.toString());
				editar.setBlob(2,  t.GetImagen());
				editar.setString(3, t.getNombreImagen());
			}
			
			editar.setString(4, t.GetDescripcion());
			editar.setString(5, t.GetInfoContacto());
			editar.setInt(6, t.GetID_Plebiscito());
			
			editar.setInt(7, t.GetID());
			
			int i = editar.executeUpdate();
			if( i > 0) {
				System.out.println("EditarTendencia: Se edito correctamente, editar links");
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// editar los links externos
		if( t.GetLinksExternos() != null ) {
			EditarLinksExternos( t.GetID(), t.GetLinksExternos() );
		}
	}
//	
//	public void EditarPadron( Padron p )
//	{
//		
//	}
//	
//	public void EditarUsuario( Usuario usr )
//	{
//		
//	}
	
	public void EliminarPlebiscito( Plebiscito p ) {
		
	}

	public void EliminarPlebiscito(String nombrep) {
		// TODO Auto-generated method stub		
		PreparedStatement elimina;
		
		try {
			String queryP = "DELETE FROM plebiscito where Nombre_Plebiscito = ?";
			
			elimina = conn.prepareStatement(queryP);
			elimina.setString(1,nombrep);
			
			// hacer algo con i
			int i = elimina.executeUpdate();
			if( i > 0 ) {
				System.out.println("EliminarPlebiscito: Se elimino correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Elimina la tendencia cuyo id es:
	 * @param idt
	 */
	public void EliminarTendencia( int idt ) {
		PreparedStatement elimina;
		
		try {
			String sql = "DELETE FROM Tendencia where Id_Tendencia = ?";
			
			elimina = conn.prepareStatement(sql);
			elimina.setInt(1,idt);
			
			// hacer algo con i
			int i = elimina.executeUpdate();
			if( i > 0 ) {
				System.out.println("EliminarTendencia: Se elimino correctamente");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
//	public void EliminarPadron( Padron p )
//	{
//		
//	}
//	
//	public void EliminarUsuario( Usuario usr )
//	{
//		
//	}
	
	
}
