package ve.com.mastercircuito.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;

public class MysqlDriver {
	
	protected Connection conn = null;
	private String host, database, dbuser, dbpassword;
	
	private int insertId = 0;
	private int numRows = 0;
	
	public MysqlDriver() {
		Properties prop = new Properties();
		InputStream propertiesInput;
		propertiesInput = getClass().getResourceAsStream("config.properties");
		// load a properties file
		try {
			prop.load(propertiesInput);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error loading config file - " + e.getMessage());
		}
		this.host = prop.getProperty("host");
		this.database = prop.getProperty("database");
		this.dbuser = prop.getProperty("dbuser");
		this.dbpassword = prop.getProperty("dbpassword");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "com.mysql.jdbc.Driver Not Found - " + e.getMessage());
		}
		this.connect();
		
	}
	
	public MysqlDriver(String host, String dbuser, String dbpassword, String database) {
		this.host = host;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		this.database = database;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "com.mysql.jdbc.Driver Not Found - " + e.getMessage());
		}
		this.connect();
	}
	
	public Connection connect() {
		String url = "jdbc:mysql://" + host + "/" + database;
		
		try {
			conn = DriverManager.getConnection(url, dbuser, dbpassword);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "error connecting to " + url + " - " + e.getMessage());
		}
		return conn;
	}
	
	public boolean query(String queryString) {
		Boolean queryResult = false;
		Statement sqlState;
		try {
			sqlState = conn.createStatement();
			queryResult = sqlState.execute(conn.nativeSQL(queryString));
		} catch (SQLException e) {
			this.setInsertId(0);
			queryResult = false;
			e.printStackTrace();
		}
		this.setInsertId(0);
		return queryResult;
	}
	
	public ResultSet select(String queryString) {
		ResultSet queryResult = null;
		Statement sqlState;
		try {
			sqlState = conn.createStatement();
			queryResult = sqlState.executeQuery(queryString);
			this.setNumRows(queryResult);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error executing select:" + e.getMessage());
		}
		this.setInsertId(0);
		return queryResult;
	}
	
	public void insert(String queryString) {
		Statement sqlState;
		int insertedId = 0;
		try {
			sqlState = this.conn.prepareStatement(queryString);
			sqlState.executeUpdate(queryString, Statement.RETURN_GENERATED_KEYS);
			try (ResultSet generatedKeys = sqlState.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	insertedId = generatedKeys.getInt(1);
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.setInsertId(insertedId);
	}
	
	public boolean update(String queryString) {
		PreparedStatement sqlState;
		try{
			sqlState = this.conn.prepareStatement(queryString);
			int count = sqlState.executeUpdate();
			if(count > 0){
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(String queryDelete) {
		PreparedStatement sqlState;
		try {
			sqlState = this.conn.prepareStatement(queryDelete);
			int count = sqlState.executeUpdate();
			if(count > 0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Object[][] fetchAll(ResultSet set) {
		ResultSetMetaData metaData;
		int rows = this.getNumRows();
		int columns = 0;
		int i = 0;
		if(set != null) {
			try {
				metaData = set.getMetaData();
				columns = metaData.getColumnCount();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Object[][] results = new Object[rows][columns+1];
		if(set != null) {
			try {
				set.beforeFirst();
				while(set.next()) {
					for(int j = 0; j < columns; j++) {
						results[i][j] = set.getString(j+1);
					}
					i++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public Object[][] fetchAllAddButton(ResultSet set, int tableColumn) {
		ResultSetMetaData metaData;
		int rows = getNumRows();
		int columns = 0;
		int i = 0;
		if(set != null) {
			try {
				metaData = set.getMetaData();
				columns = metaData.getColumnCount();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Object[][] results = new Object[rows][columns+1];
		if(set != null) {
			try {
				set.beforeFirst();
				while(set.next()) {
					for(int j = 0; j < columns+1; j++) {
						if (tableColumn == j + 1) {
							results[i][j] = new String("...");
						} else {
							results[i][j] = set.getString(j+1);
						}
					}
//					results[i][columns] = new Boolean(false);
					i++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public Object[][] fetchAllAddBoolean(ResultSet set, int tableColumn) {
		ResultSetMetaData metaData;
		int rows = getNumRows();
		int columns = 0;
		int i = 0;
		if(set != null) {
			try {
				metaData = set.getMetaData();
				columns = metaData.getColumnCount();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Object[][] results = new Object[rows][columns+1];
		if(set != null) {
			try {
				set.beforeFirst();
				while(set.next()) {
					for(int j = 0; j < columns; j++) {
						if (tableColumn == j + 1) {
							results[i][j] = new Boolean((set.getString(j+1).equalsIgnoreCase("1")?true:false));
						} else {
							results[i][j] = set.getString(j+1);
						}
					}
					i++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public List<String> fetchColumnAsList(ResultSet set, String columnName) {
		List<String> results = new ArrayList<String>();
		try {
			if(null != set) {
				set.beforeFirst();
				while(set.next()) {
					results.add(set.getString(columnName));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public Object[] fetchColumnAsArray(ResultSet set, String columnName) {
		List<String> results = this.fetchColumnAsList(set, columnName);
		return results.toArray();
	}
	
	private void setNumRows(ResultSet set) {
		try {
			set.last();
			this.numRows = set.getRow();
			set.beforeFirst();;
		} catch (SQLException e) {
			this.numRows = 0;
			e.printStackTrace();
		}
	}
	
	public int getNumRows() {
		return this.numRows;
	}
	
	private void setInsertId(int insertId) {
		this.insertId = insertId;
	}
	
	public int getInsertId() {
		return this.insertId;
	}
	
}