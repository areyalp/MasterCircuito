package ve.com.mastercircuito.components;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ve.com.mastercircuito.db.Db;

public class MyTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7291256884024381770L;
	private String[] columnNames;
	private Object[][] data;
	
	public MyTableModel(Object[][] data, String[] columnNames) {
		super();
		this.data = data;
		this.columnNames = columnNames;
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
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == this.getColumnCount() - 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if(this.getColumnClass(col).equals(Boolean.class)) {
			Db db = new Db();
			Map<Integer,Boolean> mainSwitchesMap = new HashMap<Integer,Boolean>();
			for(int i = 0; i < data.length; i++) {
				if(data[i][col].equals(Boolean.TRUE)) {
					mainSwitchesMap.put(Integer.valueOf(String.valueOf(this.data[i][0])), Boolean.valueOf(String.valueOf(this.data[i][col])));
				}
			}
			
			Integer boardSwitchId = Integer.valueOf(String.valueOf(this.data[row][0]));
			
			if(value.equals(Boolean.FALSE)) {
				mainSwitchesMap.remove(Integer.valueOf(String.valueOf(this.data[row][0])));
				db.removeBoardMainSwitch(Integer.valueOf(String.valueOf(this.data[row][0])), boardSwitchId);
				this.data[row][col] = value;
				fireTableCellUpdated(row, col);
			} else if(value.equals(Boolean.TRUE) && mainSwitchesMap.size() < 2) {
				mainSwitchesMap.put(Integer.valueOf(String.valueOf(this.data[row][0])), (Boolean) this.data[row][col]);
				db.addBoardMainSwitch(Integer.valueOf(String.valueOf(this.data[row][0])), boardSwitchId);
				this.data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		} else {
			this.data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}
	
	public void add(Object[] value) {
		for(int i = 0; i < value.length; i++) {
			this.data[getRowCount()][i] = value[i];
		}
	}
	
}
