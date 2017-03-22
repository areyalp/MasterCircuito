package ve.com.mastercircuito.components;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ve.com.mastercircuito.db.Db;

public class MyTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7291256884024381770L;
	
	private String[] columnNames;
	private String query;
	private String table;
	private HashSet<Integer> editableColumns;
	
	public MyTableModel(String query, String[] columnNames, String table, HashSet<Integer> editableColumns) {
		this(queryToObject(query), columnNames);
		this.query = query;
		this.columnNames = columnNames;
		this.table = table;
		this.editableColumns = editableColumns;
	}
	
	public MyTableModel(Object[][] data, String[] columnNames) {
		super(data, columnNames);
		this.columnNames = columnNames;
	}
	
	public MyTableModel(Object[][] data, String[] columnNames, String table) {
		this(data, columnNames);
		this.table = table;
	}
	
	public MyTableModel(Object[][] data, String[] columnNames, String table, HashSet<Integer> editableColumns) {
		this(data, columnNames, table);
		this.editableColumns = editableColumns;
	}
	
	public TableModel getTableModel() {
		return this;
	}
	
	protected static Object[][] queryToObject(String query) {
		if (query == null) {
			return null;
		}
		Db db = new Db();
		Object[][] localObject = db.fetchAll(db.select(query));
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return localObject;
	}
	
	protected static Object[][] queryToObjectBoolean(String query, int booleanColumn) {
		if (query == null) {
			return null;
		}
		Db db = new Db();
		Object[][] localObject = db.fetchAllAddBoolean(db.select(query), booleanColumn);
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return localObject;
	}
	
	public void setQuery(String query) {
		Object[][] objectData = queryToObject(query);
		this.setData(objectData);
	}
	
	public void setQueryAddBoolean(String query, int booleanColumn) {
		Object[][] objectData = queryToObjectBoolean(query, booleanColumn);
		this.setData(objectData);
	}
	
	private void setData(Object[][] objectData) {
		if (objectData.length > 0) {
			this.setDataVector(convertToVector(objectData), this.columnIdentifiers);
			if (this.getDataVector().size() > 0) {
				fireTableDataChanged();
			}
		} else {
			this.setRowCount(0);
		}
	}
	
	@Override
	public int getRowCount() {
		return this.getDataVector().size();
	}
	
//	@Override
//	public int getColumnCount() {
//		return this.columnNames.length;
//	}

	@Override
	public Object getValueAt(int row, int col) {
		@SuppressWarnings("unchecked")
		Vector<Object> localVector = (Vector<Object>) this.getDataVector().elementAt(row);
		return localVector.elementAt(col);
	}
	
	public Object getValueAt(int row, String colName) {
		@SuppressWarnings("unchecked")
		Vector<Object> localVector = (Vector<Object>) this.getDataVector().elementAt(row);
		return localVector.elementAt(this.findColumn(colName));
	}
	
	@Override
	public String getColumnName(int col) {
		return this.columnNames[col];
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		if(this.getRowCount() > 0) {
			return this.getValueAt(0, c).getClass();
		} else {
			return null;
		}
	}
	
	public HashSet<Integer> getEditableColumns() {
		return this.editableColumns;
	}
	
	public void setEditableColumns(HashSet<Integer> editableColumns) {
		this.editableColumns = editableColumns;
	}
	
//	public void makeColumnEditable(Integer column) {
//		this.editableColumns.add(column);
//	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		HashSet<Integer> editableColumns = this.getEditableColumns();
		if(null != editableColumns && editableColumns.contains(new Integer(col))) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Class<?> columnClass = this.getColumnClass(col);
		if(columnClass.equals(Boolean.class)) {
			boolean hasMain = false;
			//Map<Integer,Boolean> mainSwitchesMap = new HashMap<Integer,Boolean>();
			for(int i = 0; i < this.dataVector.size(); i++) {
				@SuppressWarnings("unchecked")
				Vector<Object> localVector = (Vector<Object>) this.dataVector.elementAt(i);
				if(localVector.elementAt(col).equals(Boolean.TRUE)) {
					hasMain = true;
				}
			}
			@SuppressWarnings("unchecked")
			Vector<Object> localVector = (Vector<Object>) this.dataVector.elementAt(row);
			if (((!hasMain && value.equals(Boolean.TRUE)) || (hasMain && value.equals(Boolean.FALSE))) && Integer.valueOf((String) localVector.elementAt(2)) == 1) {
				Integer switchId = Integer.valueOf(String.valueOf(localVector.elementAt(0)));
				updateBoolean(switchId, (Boolean) value);
				localVector.setElementAt((Boolean) value, col);
				fireTableCellUpdated(row, col);
			}
		} else {
			boolean cellChanged = false;
			@SuppressWarnings("unchecked")
			Vector<Object> localVector = (Vector<Object>) this.dataVector.elementAt(row);
			if(localVector.elementAt(col) != value) {
				cellChanged = true;
			}
			localVector.setElementAt(value, col);
			fireTableCellUpdated(row, col);
			
			String columnName = this.getColumnName(col);
			String field = "";
			
			switch(columnName.toLowerCase()) {
				case "precio":
				case "precio costo":
					field = "price";
					break;
				case "cantidad":
					field = "quantity";
					break;
				case "factor":
					field = "factor";
					break;
				case "%mo":
					field = "mo";
					break;
				case "%gi":
					field = "gi";
					break;
				case "%ga":
					field = "ga";
					break;
				case "referencia":
					field = "reference";
					break;
				case "descripcion":
					field = "material";
					break;
				case "cliente":
					field = "client";
					break;
				case "codigo cliente":
					field = "client_code";
					break;
				case "representante":
					field = "representative";
					break;
				case "rif":
					field = "rif";
					break;
			}
			
			if(cellChanged && !field.isEmpty()) {
				String sql = "";
				switch(table) {
					case "board_switches":
					case "board_materials":
					case "control_board_switches":
					case "control_board_materials":
					case "budget_switches":
					case "budget_boxes":
					case "budget_boards":
					case "budget_control_boards":
					case "budget_materials":
					case "materials":
					case "clients":
						sql = "UPDATE " + table + " SET " + field + " = '" + value + "' WHERE id = " + localVector.elementAt(0);
						Db.update(sql);
						break;
				}
				Db db = new Db();
				this.setDataVector(convertToVector(db.fetchAll(db.select(query))), this.columnIdentifiers);
				fireTableDataChanged();
				try {
					db.getConnection().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	private void updateBoolean(int switchId, Boolean value) {
		Db db = new Db();
		
		if(value.equals(Boolean.FALSE)) {
			db.removeMainSwitch(table, switchId);
		} else {
			db.addMainSwitch(table, switchId);
		}
		
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public void add(Object[] value) {
//		for(int i = 0; i < value.length; i++) {
//			this.addRow(value);
//			this.data[getRowCount()][i] = value[i];
//		}
//	}
	
}
