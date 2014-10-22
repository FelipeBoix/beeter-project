package edu.upc.eetac.dsa.smachado.beeter.api;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
 
public class DataSourceSPA {
    private DataSource dataSource;// referencia la datasource
	private static DataSourceSPA instance;
 
	private DataSourceSPA() {// constructor singleton
		super();
		Context envContext = null;
		try {
			envContext = new InitialContext();
			Context initContext = (Context) envContext.lookup("java:/comp/env");
			dataSource = (DataSource) initContext.lookup("jdbc/beeterdb");//se obtiene la referencia la datasource
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}
 
	public final static DataSourceSPA getInstance() {//obtener instancia singleton
		if (instance == null)
			instance = new DataSourceSPA();
		return instance;
	}
 
	public DataSource getDataSource() {//retorna la instancia
		return dataSource;
	}
}