package database;

import java.sql.*;


/** Holds open a connection to one DB at a time */
public class DBManager 
{
//	private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	//private final String dbDirectory = System.getProperty("user.dir")+"/databases";
//	String createString = "CREATE TABLE WISH_LIST  "
//	  +  "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY " 
//	  +  " WISH_ITEM VARCHAR(32) NOT NULL) " ;
	
	private Connection connection = null;
	
	
	public DBManager()
	{
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/** Opens or creates a DB 
	 * 
	 * @param dbName The name for the database you'd like to open/create
	 */
	public void startDB(String dbName)
	{
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		if(connection != null)
		{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			connection = DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createTable(String tableName)
	{
		try {
			connection.createStatement().execute("create table  "+
					tableName+"(ENTITY_ID int primary key" +
					", ENTITY_NAME varchar(16))");
		} catch (SQLException e) {
			
			if(e.getSQLState().equals("X0Y32")) 
			{
	            // table already exists, no big deal
				return;
			}
			e.printStackTrace();
		}
	}
	
	public void insert(String whichTable, String insertMe)
	{
		Statement s = null;
		try 
		{
			
			connection.createStatement().execute("insert into " + whichTable+
					" values " + insertMe);
//			PreparedStatement psInsert = connection.prepareStatement
//					   ("insert into " + whichTable+"(ENTITY_NAME) values (?)");
//			
//			psInsert.setString(1,insertMe);
//			psInsert.executeUpdate();
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String seeTable(String whichTable)
	{
		StringBuilder sb = new StringBuilder();
		try{
			Statement s = connection.createStatement();
			ResultSet results = s.executeQuery("select * from " + whichTable);
			
			while( results.next() )
			{
				// just puts the ENTITY_ID, ENTITY into a stringbuilder
				sb.append(results.getInt(1)+ ",  " +
						results.getString(2)+"\n");
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		return sb.toString();
	}
	
	public void shutDown()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
