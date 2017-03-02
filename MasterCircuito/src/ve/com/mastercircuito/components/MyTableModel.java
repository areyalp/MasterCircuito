package ve.com.mastercircuito.components;

import java.util.HashSet;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ve.com.mastercircuito.db.Db;

public class MyTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7291256884024381770L;
	private String[] columnNames;
	private String query;
	private Object[][] data;
	private String table;
	private HashSet<Integer> editableColumns;
	
	public MyTableModel(String query, String[] columnNames, String table, HashSet<Integer> editableColumns) {
		super();
		Db db = new Db();
		this.query = query;
		this.data = db.fetchAll(db.select(query));
		this.columnNames = columnNames;
		this.table = table;
		this.editableColumns = editableColumns;
	}
	
	public MyTableModel(Object[][] data, String[] columnNames) {
		super();
		this.data = data;
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
	
	@Override
	public int getRowCount() {
		return this.data.length;
	}
	
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return this.data[row][col];
	}
	
	public Object getValueAt(int row, String colName) {
		return this.data[row][this.findColumn(colName)];
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
	
	public void makeColumnEditable(Integer column) {
		this.editableColumns.add(column);
	}
	
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
			for(int i = 0; i < data.length; i++) {
				if(data[i][col].equals(Boolean.TRUE)) {
					hasMain = true;
				}
			}
			if (((!hasMain && value.equals(Boolean.TRUE)) || (hasMain && value.equals(Boolean.FALSE))) && Integer.valueOf((String) this.data[row][2]) == 1) {
				Integer switchId = Integer.valueOf(String.valueOf(this.data[row][0]));
				updateBoolean(switchId, (Boolean) value);
				this.data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		} else {
			boolean cellChanged = false;
			if(this.data[row][col] != value) {
				cellChanged = true;
			}
			this.data[row][col] = value;
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
						sql = "UPDATE " + table + " SET " + field + " = '" + value + "' WHERE id = " + this.data[row][0];
						Db.update(sql);
						break;
				}
				Db db = new Db();
				this.data = db.fetchAll(db.select(query));
				fireTableDataChanged();
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
	}
	
	public void add(Object[] value) {
		for(int i = 0; i < value.length; i++) {
			this.data[getRowCount()][i] = value[i];
		}
	}
	
}
