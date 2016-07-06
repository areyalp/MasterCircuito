package ve.com.mastercircuito.components;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.StringTools;

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
			String boardMainSwitches = "";
			int boardMains = 0;
			int boardContainerId = 0;
			for(int i = 0; i < data.length; i++) {
				if(data[i][col].equals(true)) {
					boardMains++;
					boardMainSwitches = boardMainSwitches.concat(data[i][0] + ",");
				}
			}
			if(value.equals(false)) {
				boardMainSwitches = boardMainSwitches.replace(this.data[row][0] + ",", "");
				boardMains--;
			}
			if(boardMains < 2) {
				this.data[row][col] = value;
				fireTableCellUpdated(row, col);
				if(value.equals(true)) {
					boardMainSwitches = boardMainSwitches.concat(data[row][0] + ",");
				}
				
				if(boardMainSwitches.endsWith(",")) {
					boardMainSwitches = StringTools.removeLastChar(boardMainSwitches);
				}
				
				Object boardSwitchId = this.data[row][0];
				boardContainerId = db.getBoardContainerId(Integer.valueOf((String) boardSwitchId));
				ArrayList<Object> listFields = new ArrayList<Object>();
				ArrayList<Object> listValues = new ArrayList<Object>();
				listFields.add("main_switch_id");
				listValues.add("'" + boardMainSwitches + "'");
				db.editBoard(boardContainerId, listFields, listValues);
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
