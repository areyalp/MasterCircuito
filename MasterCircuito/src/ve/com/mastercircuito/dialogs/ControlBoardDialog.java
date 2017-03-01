package ve.com.mastercircuito.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.StringTools;

public class ControlBoardDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -380402895317829859L;
	
	// Common Tables
		private static final String INSTALLATIONS_TABLE = "installations";
		private static final String NEMAS_TABLE = "nemas";
		private static final String INTERRUPTIONS_TABLE = "interruptions";
		private static final String LOCK_TYPES_TABLE = "lock_types";
	// Common Fields
		private static final String INSTALLATION_FIELD = "installations.installation";
		private static final String NEMA_FIELD = "nemas.nema";
		private static final String INTERRUPTION_FIELD = "interruptions.interruption";
	// Board Tables
		private static final String CONTROL_BOARD_TABLE = "control_boards";
		private static final String CONTROL_BOARD_BAR_CAPACITIES_TABLE = "control_board_bar_capacities";
		private static final String CONTROL_BOARD_BAR_TYPES_TABLE = "control_board_bar_types";
		private static final String CONTROL_BOARD_CIRCUITS_TABLE = "control_board_circuits";
		private static final String CONTROL_BOARD_TYPES_TABLE = "control_board_types";
		private static final String CONTROL_BOARD_VOLTAGES_TABLE = "control_board_voltages";
	// Board Fields
		private static final String CONTROL_BOARD_ID_FIELD = "control_boards.id";
		private static final String CONTROL_BOARD_NAME_FIELD = "control_boards.`name`";
		private static final String CONTROL_BOARD_TYPE_FIELD = "control_board_types.type";
		private static final String CONTROL_BOARD_BAR_CAPACITY_FIELD = "control_board_bar_capacities.bar_capacity";
		private static final String CONTROL_BOARD_BAR_TYPE_FIELD = "control_board_bar_types.bar_type";
		private static final String CONTROL_BOARD_CIRCUITS_FIELD = "control_board_circuits.circuits";
		private static final String CONTROL_BOARD_VOLTAGE_FIELD = "control_board_voltages.voltage";
		private static final String CONTROL_BOARD_PHASES_FIELD = "control_boards.phases";
		private static final String CONTROL_BOARD_GROUND_FIELD = "IF(control_boards.ground=0,'NO','SI') as ground";
		private static final String CONTROL_BOARD_LOCK_TYPE_FIELD = "lock_types.lock_type";
		private static final String CONTROL_BOARD_PRICE_FIELD = "control_boards.price";
	
	private Integer height = 300;
	private Integer width = 1200;
	
	private Db db;
	private Object[][] boardsSearchData;
	private JTable tableBoardsSearchResult;
	private ListSelectionModel listBoardSearchSelectionModel;
	private JTextField textBoardSearchNames;
	private JComboBox<String> comboBoardTypes, comboBoardInstallations, comboBoardNemas, comboBoardBarCapacities, comboBoardBarTypes, comboBoardCircuits, comboBoardVoltages, comboBoardPhases, comboBoardGround, comboBoardInterruptions, comboBoardLockTypes;
	private String searchSelectedBoardType = "", searchSelectedBoardInstallation = "", searchSelectedBoardNema = "", searchSelectedBoardBarCapacity = "", searchSelectedBoardBarType = "", searchSelectedBoardCircuits = "", searchSelectedBoardVoltage = "", searchSelectedBoardPhases = "", searchSelectedBoardGround = "", searchSelectedBoardInterruption = "", searchSelectedBoardLockType = "";
	
	String[] boardsColumnNames = { "Id", "Nombre", "Tipo", "Instalacion", "Nema", "Cap. Barra", "Tipo Barra", "Circuitos", "Voltaje", "Fases", "Tierra", "Interrupcion", "Cerradura", "Precio"};
	
	private Integer boardSearchId = 0;
	private Integer boardAddQuantity = 1;
	private double boardPrice = 0.00;
	
	public ControlBoardDialog(Window owner) {
		this(owner, "");
	}
	
	public ControlBoardDialog(Window owner, String title) {
		super(owner, title, JDialog.DEFAULT_MODALITY_TYPE);
		this.setMinimumSize(new Dimension(this.width, this.height));
		this.setSize(new Dimension(this.width, this.height));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width / 2) - (this.getWidth() / 2);
		int y = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(x, y);
		
		db = new Db();
		
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));
		panelCenter.add(createBoardAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCenter.add(createBoardAddTablePanel());
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createBoardAddCountPanel());
		panelLower.add(createBoardAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public Integer getBoardSearchId() {
		return this.boardSearchId;
	}
	
	public Integer getBoardAddQuantity() {
		return this.boardAddQuantity;
	}
	
	public double getBoardPrice() {
		return this.boardPrice;
	}
	
	private JPanel createBoardAddSearchPanel() {
		JPanel panelBoxAddSearch = new JPanel();
		panelBoxAddSearch.setLayout(new BoxLayout(panelBoxAddSearch, BoxLayout.PAGE_AXIS));
		JPanel searchBarPanel1 = new JPanel();
		searchBarPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel searchBarPanel2 = new JPanel();
		searchBarPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryTypes = "SELECT " + ControlBoardDialog.CONTROL_BOARD_TYPE_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".type_id "
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".type";
		
		String queryInstallations = "SELECT " + ControlBoardDialog.INSTALLATION_FIELD
				+ " FROM " + ControlBoardDialog.INSTALLATIONS_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.INSTALLATIONS_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".installation_id "
				+ " GROUP BY " + ControlBoardDialog.INSTALLATIONS_TABLE + ".installation";
		
		String queryNemas = "SELECT " + ControlBoardDialog.NEMA_FIELD
				+ " FROM " + ControlBoardDialog.NEMAS_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.NEMAS_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".nema_id "
				+ " GROUP BY " + ControlBoardDialog.NEMAS_TABLE + ".nema";
		
		String queryBarCapacities = "SELECT " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITY_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_capacity_id "
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity";
		
		String queryBarTypes = "SELECT " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPE_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_type_id "
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".bar_type";
		
		String queryCircuits = "SELECT " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".circuits_id "
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".circuits";
		
		String queryVoltages = "SELECT " + ControlBoardDialog.CONTROL_BOARD_VOLTAGE_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".voltage_id "
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".voltage";
		
		String queryPhases = "SELECT " + ControlBoardDialog.CONTROL_BOARD_PHASES_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".phases";
		
		String queryGround = "SELECT " + ControlBoardDialog.CONTROL_BOARD_GROUND_FIELD
				+ " FROM " + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " GROUP BY " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".ground";
		
		String queryInterruptions = "SELECT " + ControlBoardDialog.INTERRUPTION_FIELD
				+ " FROM " + ControlBoardDialog.INTERRUPTIONS_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".interruption_id "
				+ " GROUP BY " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".interruption";
		
		String queryLockTypes = "SELECT " + ControlBoardDialog.CONTROL_BOARD_LOCK_TYPE_FIELD
				+ " FROM " + ControlBoardDialog.LOCK_TYPES_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TABLE
				+ " WHERE " + ControlBoardDialog.LOCK_TYPES_TABLE + ".id = " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".lock_type_id "
				+ " GROUP BY " + ControlBoardDialog.LOCK_TYPES_TABLE + ".lock_type";
		
		JLabel labelName = new JLabel("Nombre: ");
		JLabel labelType = new JLabel("Tipo:");
		JLabel labelInstallation = new JLabel("Instalacion:");
		JLabel labelNema = new JLabel("Nema:");
		JLabel labelBarCapacity = new JLabel("Cap. Barra:");
		JLabel labelBarType = new JLabel("Tipo Barra:");
		JLabel labelCircuits = new JLabel("Circuitos:");
		JLabel labelVoltage = new JLabel("Voltaje:");
		JLabel labelPhases = new JLabel("Fases:");
		JLabel labelGround = new JLabel("Tierra:");
		JLabel labelInterruption = new JLabel("Interrupcion:");
		JLabel labelLockType = new JLabel("Cerradura:");
		
		ComboBoxListener lForCombo = new ComboBoxListener();
		
		textBoardSearchNames = new JTextField(6);
		textBoardSearchNames.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadBoardSearchTable("");
				
				if(boardsSearchData.length > 0) {
					tableBoardsSearchResult.setModel(new MyTableModel(boardsSearchData, boardsColumnNames));
				} else {
					tableBoardsSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		searchBarPanel1.add(labelName);
		searchBarPanel1.add(textBoardSearchNames);
		
		comboBoardTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoardTypes.setActionCommand("board.search.type");
		comboBoardTypes.addActionListener(lForCombo);
		searchBarPanel1.add(labelType);
		searchBarPanel1.add(comboBoardTypes);
		
		comboBoardInstallations = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoardInstallations.setActionCommand("board.search.installation");
		comboBoardInstallations.addActionListener(lForCombo);
		searchBarPanel1.add(labelInstallation);
		searchBarPanel1.add(comboBoardInstallations);
		
		comboBoardNemas = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoardNemas.setActionCommand("board.search.nema");
		comboBoardNemas.addActionListener(lForCombo);
		searchBarPanel1.add(labelNema);
		searchBarPanel1.add(comboBoardNemas);
		
		comboBoardBarCapacities = new JComboBox<String>(new Vector<String>(loadComboList(queryBarCapacities, "bar_capacity")));
		comboBoardBarCapacities.setActionCommand("board.search.bar_capacity");
		comboBoardBarCapacities.addActionListener(lForCombo);
		searchBarPanel1.add(labelBarCapacity);
		searchBarPanel1.add(comboBoardBarCapacities);
		
		comboBoardBarTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryBarTypes, "bar_type")));
		comboBoardBarTypes.setActionCommand("board.search.bar_type");
		comboBoardBarTypes.addActionListener(lForCombo);
		searchBarPanel1.add(labelBarType);
		searchBarPanel1.add(comboBoardBarTypes);
		
		comboBoardCircuits = new JComboBox<String>(new Vector<String>(loadComboList(queryCircuits, "circuits")));
		comboBoardCircuits.setActionCommand("board.search.circuits");
		comboBoardCircuits.addActionListener(lForCombo);
		searchBarPanel1.add(labelCircuits);
		searchBarPanel1.add(comboBoardCircuits);
		
		comboBoardVoltages = new JComboBox<String>(new Vector<String>(loadComboList(queryVoltages, "voltage")));
		comboBoardVoltages.setActionCommand("board.search.voltage");
		comboBoardVoltages.addActionListener(lForCombo);
		searchBarPanel2.add(labelVoltage);
		searchBarPanel2.add(comboBoardVoltages);
		
		comboBoardPhases = new JComboBox<String>(new Vector<String>(loadComboList(queryPhases, "phases")));
		comboBoardPhases.setActionCommand("board.search.phases");
		comboBoardPhases.addActionListener(lForCombo);
		searchBarPanel2.add(labelPhases);
		searchBarPanel2.add(comboBoardPhases);
		
		comboBoardGround = new JComboBox<String>(new Vector<String>(loadComboList(queryGround, "ground")));
		comboBoardGround.setActionCommand("board.search.ground");
		comboBoardGround.addActionListener(lForCombo);
		searchBarPanel2.add(labelGround);
		searchBarPanel2.add(comboBoardGround);
		
		comboBoardInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboBoardInterruptions.setActionCommand("board.search.interruption");
		comboBoardInterruptions.addActionListener(lForCombo);
		searchBarPanel2.add(labelInterruption);
		searchBarPanel2.add(comboBoardInterruptions);
		
		comboBoardLockTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoardLockTypes.setActionCommand("board.search.lock_type");
		comboBoardLockTypes.addActionListener(lForCombo);
		searchBarPanel2.add(labelLockType);
		searchBarPanel2.add(comboBoardLockTypes);
		
		panelBoxAddSearch.add(searchBarPanel1);
		panelBoxAddSearch.add(searchBarPanel2);
		
		return panelBoxAddSearch;
	}
	
	private JPanel createBoardAddTablePanel() {
		loadBoardSearchTable("");
		
		MyTableModel mForTable = new MyTableModel(boardsSearchData, boardsColumnNames);
		
		tableBoardsSearchResult = new JTable();
		tableBoardsSearchResult.setModel(mForTable);
		tableBoardsSearchResult.setAutoCreateRowSorter(true);
		tableBoardsSearchResult.getTableHeader().setReorderingAllowed(false);
		
		listBoardSearchSelectionModel = tableBoardsSearchResult.getSelectionModel();
		listBoardSearchSelectionModel.addListSelectionListener(new MyListSelectionListener());
		tableBoardsSearchResult.setSelectionModel(listBoardSearchSelectionModel);
		tableBoardsSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableBoardsSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(tableBoardsSearchResult, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private JPanel createBoardAddCountPanel() {
		JPanel panelCountOuter = new JPanel(new BorderLayout());
		JPanel panelCountInner = new JPanel();
		
		panelCountInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonDecrease = new JButton("-");
		JButton buttonIncrease = new JButton("+");
		JLabel labelQuantity = new JLabel("1");
		boardAddQuantity = 1;
		boardPrice = 0.00;
		Integer min = 1;
		Integer max = 100;
		
		panelCountInner.add(buttonDecrease);
		panelCountInner.add(labelQuantity);
		panelCountInner.add(buttonIncrease);
		
		buttonDecrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boardAddQuantity > min) {
					boardAddQuantity--;
					labelQuantity.setText(String.valueOf(boardAddQuantity));
				}
			}
		});
		
		buttonIncrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boardAddQuantity < max) {
					boardAddQuantity++;
					labelQuantity.setText(String.valueOf(boardAddQuantity));
				}
			}
		});
		
		panelCountOuter.add(panelCountInner, BorderLayout.CENTER);
		
		return panelCountOuter;
	}
	
	private JPanel createBoardAddButtonPanel() {
		JPanel panelOuter = new JPanel(new BorderLayout());
		JPanel panelInner = new JPanel();
		panelInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonAccept = new JButton("Aceptar");
		JButton buttonCancel = new JButton("Cancelar");
		panelInner.add(buttonAccept);
		panelInner.add(buttonCancel);
		
		buttonAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(tableBoardsSearchResult.getSelectedRow() > -1) {
					boardPrice = Double.valueOf((String) tableBoardsSearchResult.getValueAt(tableBoardsSearchResult.getSelectedRow(), 13));
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boardSearchId = 0;
				boardAddQuantity = 0;
				boardPrice = 0.00;
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	private void loadBoardSearchTable(String whereQuery) {
		whereQuery = "";
		if (null != comboBoardTypes.getSelectedItem()) {
			searchSelectedBoardType = comboBoardTypes.getSelectedItem().toString();
		}
		if (null != comboBoardInstallations.getSelectedItem()) {
			searchSelectedBoardInstallation = comboBoardInstallations.getSelectedItem().toString();
		}
		if (null != comboBoardNemas.getSelectedItem()) {
			searchSelectedBoardNema = comboBoardNemas.getSelectedItem().toString();
		}
		if (null != comboBoardBarCapacities.getSelectedItem()) {
			searchSelectedBoardBarCapacity = comboBoardBarCapacities.getSelectedItem().toString();
		}
		if (null != comboBoardBarTypes.getSelectedItem()) {
			searchSelectedBoardBarType = comboBoardBarTypes.getSelectedItem().toString();
		}
		if (null != comboBoardCircuits.getSelectedItem()) {
			searchSelectedBoardCircuits = comboBoardCircuits.getSelectedItem().toString();
		}
		if (null != comboBoardVoltages.getSelectedItem()) {
			searchSelectedBoardVoltage = comboBoardVoltages.getSelectedItem().toString();
		}
		if (null != comboBoardPhases.getSelectedItem()) {
			searchSelectedBoardPhases = comboBoardPhases.getSelectedItem().toString();
		}
		if (null != comboBoardGround.getSelectedItem()) {
			searchSelectedBoardGround = comboBoardGround.getSelectedItem().toString();
		}
		if (null != comboBoardInterruptions.getSelectedItem()) {
			searchSelectedBoardInterruption = comboBoardInterruptions.getSelectedItem().toString();
		}
		if (null != comboBoardLockTypes.getSelectedItem()) {
			searchSelectedBoardLockType = comboBoardLockTypes.getSelectedItem().toString();
		}
		
		if(null != textBoardSearchNames && !textBoardSearchNames.getText().isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".`name` LIKE '%" + textBoardSearchNames.getText() + "%'";
		}
		if(null != searchSelectedBoardType  && !searchSelectedBoardType.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardType.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".type = '" + searchSelectedBoardType + "'";
		}
		if(searchSelectedBoardInstallation != null && !searchSelectedBoardInstallation.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardInstallation.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".installation_id = " + ControlBoardDialog.INSTALLATIONS_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoardInstallation + "'";
		}
		if(searchSelectedBoardNema != null && !searchSelectedBoardNema.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardNema.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".nema_id = " + ControlBoardDialog.NEMAS_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.NEMAS_TABLE + ".nema = '" + searchSelectedBoardNema + "'";
		}
		if(searchSelectedBoardBarCapacity != null && !searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardBarCapacity.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_capacity_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '" + searchSelectedBoardBarCapacity + "'";
		}
		if(searchSelectedBoardBarType != null && !searchSelectedBoardBarType.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardBarType.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_type_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".bar_type = '" + searchSelectedBoardBarType + "'";
		}
		if(searchSelectedBoardCircuits != null && !searchSelectedBoardCircuits.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardCircuits.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".circuits_id = " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".circuits = '" + searchSelectedBoardCircuits + "'";
		}
		if(searchSelectedBoardVoltage != null && !searchSelectedBoardVoltage.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardVoltage.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".voltage_id = " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".voltage = '" + searchSelectedBoardVoltage + "'";
		}
		if(searchSelectedBoardPhases != null && !searchSelectedBoardPhases.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardPhases.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "'";
		}
		if(searchSelectedBoardGround != null && !searchSelectedBoardGround.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardGround.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".ground = '" + (searchSelectedBoardGround.equalsIgnoreCase("SI")?"1":"0") + "'";
		}
		if(searchSelectedBoardInterruption != null && !searchSelectedBoardInterruption.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardInterruption.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".interruption_id = " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".interruption = '" + searchSelectedBoardInterruption + "'";
		}
		if(searchSelectedBoardLockType != null && !searchSelectedBoardLockType.equalsIgnoreCase("Todas") &&
				!searchSelectedBoardLockType.isEmpty()) {
			whereQuery += " AND " + ControlBoardDialog.CONTROL_BOARD_TABLE + ".lock_type_id = " + ControlBoardDialog.LOCK_TYPES_TABLE + ".id ";
			whereQuery += " AND " + ControlBoardDialog.LOCK_TYPES_TABLE + ".lock_type = '" + searchSelectedBoardLockType + "'";
		}
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(ControlBoardDialog.CONTROL_BOARD_ID_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_NAME_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_TYPE_FIELD);
		fields.add(ControlBoardDialog.INSTALLATION_FIELD);
		fields.add(ControlBoardDialog.NEMA_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITY_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_BAR_TYPE_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_CIRCUITS_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_VOLTAGE_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_PHASES_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_GROUND_FIELD);
		fields.add(ControlBoardDialog.INTERRUPTION_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_LOCK_TYPE_FIELD);
		fields.add(ControlBoardDialog.CONTROL_BOARD_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(ControlBoardDialog.CONTROL_BOARD_TABLE);
		tables.add(ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE);
		tables.add(ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE);
		tables.add(ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE);
		tables.add(ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE);
		tables.add(ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE);
		tables.add(ControlBoardDialog.INSTALLATIONS_TABLE);
		tables.add(ControlBoardDialog.INTERRUPTIONS_TABLE);
		tables.add(ControlBoardDialog.LOCK_TYPES_TABLE);
		tables.add(ControlBoardDialog.NEMAS_TABLE);
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boxesQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE control_boards.type_id = control_board_types.id "
						+ "AND control_boards.installation_id = installations.id "
						+ "AND control_boards.nema_id = nemas.id "
						+ "AND ( (control_boards.bar_capacity_id > 0 "
							+ "AND control_boards.bar_capacity_id = control_board_bar_capacities.id) "
							+ "OR control_boards.bar_capacity_id = 0)"
						+ "AND ( (control_boards.bar_type_id > 0 "
							+ "AND control_boards.bar_type_id = control_board_bar_types.id) "
							+ "OR control_boards.bar_type_id = 0)"
						+ "AND ( (control_boards.circuits_id > 0 "
							+ "AND control_boards.circuits_id = control_board_circuits.id) "
							+ "OR control_boards.circuits_id = 0)"
						+ "AND control_boards.voltage_id = control_board_voltages.id "
						+ "AND ( (control_boards.interruption_id > 0 "
							+ "AND control_boards.interruption_id = interruptions.id) "
							+ "OR control_boards.interruption_id = 0)"
						+ "AND ( (control_boards.lock_type_id > 0 "
							+ "AND control_boards.lock_type_id = lock_types.id) "
							+ "OR control_boards.lock_type_id = 0)"
						+ "AND control_boards.active = '1' "
						+ whereQuery
						+ " GROUP BY control_boards.id";

		boardsSearchData = db.fetchAll(db.select(boxesQuery));
	}
	
	private List<String> loadComboList(String queryString, String columnName) {
		List<String> comboList = new ArrayList<String>();
		comboList.add("Todas");
		comboList.addAll(db.fetchColumnAsList(db.select(queryString), columnName));
		return comboList;
	}
	
	private class MyListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent ev) {
			ListSelectionModel lsm = (ListSelectionModel) ev.getSource();
			
			if(null != tableBoardsSearchResult 
					&& tableBoardsSearchResult.isFocusOwner() 
					&& lsm.getMinSelectionIndex() > -1) {
				boardSearchId = Integer.valueOf((String)tableBoardsSearchResult.getValueAt(tableBoardsSearchResult.getSelectedRow(), 0));
			}
		}
		
	}
	
	private class ComboBoxListener implements ActionListener {
		
		String fromQuery = "";
		String whereQuery = "";
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			String actionCommand = ev.getActionCommand();
			whereQuery = "";
			if (actionCommand.equalsIgnoreCase("board.search.type")) {
				searchSelectedBoardType = comboBoardTypes.getSelectedItem().toString();
				this.clearSelectedBoardOptions("type");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE;
				if(!searchSelectedBoardType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".type = '"+searchSelectedBoardType+"' ";
				}
				this.loadComboBoard("types");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.installation")) {
				searchSelectedBoardInstallation = comboBoardInstallations.getSelectedItem().toString();
				this.clearSelectedBoardOptions("installation");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.INSTALLATIONS_TABLE;
				if(!searchSelectedBoardInstallation.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.INSTALLATIONS_TABLE + ".installation = '"+searchSelectedBoardInstallation+"' ";
				}
				this.loadComboBoard("installations");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.nema")) {
				searchSelectedBoardNema = comboBoardNemas.getSelectedItem().toString();
				this.clearSelectedBoardOptions("nema");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.NEMAS_TABLE;
				if(!searchSelectedBoardNema.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.NEMAS_TABLE + ".nema = '"+searchSelectedBoardNema+"' ";
				}
				this.loadComboBoard("nemas");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.bar_capacity")) {
				searchSelectedBoardBarCapacity = comboBoardBarCapacities.getSelectedItem().toString();
				this.clearSelectedBoardOptions("bar_capacity");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE;
				if(!searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '"+searchSelectedBoardBarCapacity+"' ";
				}
				this.loadComboBoard("bar_capacities");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.bar_type")) {
				searchSelectedBoardBarType = comboBoardBarTypes.getSelectedItem().toString();
				this.clearSelectedBoardOptions("bar_type");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE;
				if(!searchSelectedBoardBarType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".bar_type = '"+searchSelectedBoardBarType+"' ";
				}
				this.loadComboBoard("bar_types");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.circuits")) {
				searchSelectedBoardCircuits = comboBoardCircuits.getSelectedItem().toString();
				this.clearSelectedBoardOptions("circuits");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE;
				if(!searchSelectedBoardCircuits.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".circuits = '"+searchSelectedBoardCircuits+"' ";
				}
				this.loadComboBoard("circuits");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.voltage")) {
				searchSelectedBoardVoltage = comboBoardVoltages.getSelectedItem().toString();
				this.clearSelectedBoardOptions("voltage");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE;
				if(!searchSelectedBoardVoltage.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".voltage = '"+searchSelectedBoardVoltage+"' ";
				}
				this.loadComboBoard("voltages");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.phases")) {
					searchSelectedBoardPhases = comboBoardPhases.getSelectedItem().toString();
					this.clearSelectedBoardOptions("phases");
					fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE;
					if(!searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".phases = '"+searchSelectedBoardPhases+"' ";
					}
					this.loadComboBoard("phases");
					this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.ground")) {
				searchSelectedBoardGround = comboBoardGround.getSelectedItem().toString();
				this.clearSelectedBoardOptions("ground");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE;
				this.loadComboBoard("ground");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.interruption")) {
				searchSelectedBoardInterruption = comboBoardInterruptions.getSelectedItem().toString();
				this.clearSelectedBoardOptions("interruption");
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.INTERRUPTIONS_TABLE;
				if(!searchSelectedBoardInterruption.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.INTERRUPTIONS_TABLE + ".interruption = '"+searchSelectedBoardInterruption+"' ";
				}
				this.loadComboBoard("interruptions");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("board.search.lock_type")) {
				searchSelectedBoardLockType = comboBoardLockTypes.getSelectedItem().toString();
				fromQuery = ControlBoardDialog.CONTROL_BOARD_TABLE + "," + ControlBoardDialog.LOCK_TYPES_TABLE;
				if(!searchSelectedBoardLockType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.LOCK_TYPES_TABLE + ".lock_type = '"+searchSelectedBoardLockType+"' ";
				}
				this.loadComboBoard("interruptions");
				this.dynamicSearch();
			}
		}
		
		public void dynamicSearch() {
			loadBoardSearchTable(whereQuery);
			
			if(boardsSearchData.length > 0) {
				tableBoardsSearchResult.setModel(new MyTableModel(boardsSearchData, boardsColumnNames));
			} else {
				tableBoardsSearchResult.setModel(new DefaultTableModel());
			}
		}
		
		private void clearSelectedBoardOptions(String start) {
			switch(start) {
				case "type":
					searchSelectedBoardInstallation = "";
				case "installation":
					searchSelectedBoardNema = "";
				case "nema":
					searchSelectedBoardBarCapacity = "";
				case "bar_capacity":
					searchSelectedBoardBarType = "";
				case "bar_type":
					searchSelectedBoardCircuits =  "";
				case "circuits":
					searchSelectedBoardVoltage = "";
				case "voltage":
					searchSelectedBoardPhases = "";
				case "phases":
					searchSelectedBoardGround = "";
				case "ground":
					searchSelectedBoardInterruption = "";
				case "interruption":
					searchSelectedBoardLockType = "";
					break;
			}
		}
		
		private void selectWhereQuery() {
			if(whereQuery.isEmpty()) {
				whereQuery = " WHERE ";
			} else {
				whereQuery += " AND ";
			}
		}
		
		private void addBoardAdditionalWhere(String start) {
			switch(start) {
				case "lock_type":
					if(!fromQuery.contains(ControlBoardDialog.INTERRUPTIONS_TABLE)) {
						fromQuery += "," + ControlBoardDialog.INTERRUPTIONS_TABLE;
					}
					if(!searchSelectedBoardInterruption.isEmpty() && !searchSelectedBoardInterruption.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.INTERRUPTIONS_TABLE + ".interruption = '" + searchSelectedBoardInterruption + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".interruption_id = " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".id";
					}
				case "interruption":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_TABLE;
					}
					if(!searchSelectedBoardGround.isEmpty() && !searchSelectedBoardGround.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".ground = '" + ((searchSelectedBoardGround.equalsIgnoreCase("SI"))?"1":"0") + "'";
					}
				case "ground":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_TABLE;
					}
					if(!searchSelectedBoardPhases.isEmpty() && !searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "'";
					}
				case "phases":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE;
					}
					if(!searchSelectedBoardVoltage.isEmpty() && !searchSelectedBoardVoltage.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".voltage = '" + searchSelectedBoardVoltage + "' ";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".voltage_id = " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".id";
					}
				case "voltage":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE;
					}
					if (!searchSelectedBoardCircuits.isEmpty() && !searchSelectedBoardCircuits.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".circuits = '" + searchSelectedBoardCircuits + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".circuits_id = " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".id";
					}
				case "circuits":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE;
					}
					if (!searchSelectedBoardBarType.isEmpty() && !searchSelectedBoardBarType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".bar_type = '" + searchSelectedBoardBarType + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_type_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".id";
					}
				case "bar_type":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE;
					}
					if (!searchSelectedBoardBarCapacity.isEmpty() && !searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '" + searchSelectedBoardBarCapacity + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_capacity_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".id";
					}
				case "bar_capacity":
					if(!fromQuery.contains(ControlBoardDialog.NEMAS_TABLE)) {
						fromQuery += "," + ControlBoardDialog.NEMAS_TABLE;
					}
					if (!searchSelectedBoardNema.isEmpty() && !searchSelectedBoardNema.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.NEMAS_TABLE + ".nema = '" + searchSelectedBoardNema + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".nema_id = " + ControlBoardDialog.NEMAS_TABLE + ".id";
					}
				case "nema":
					if(!fromQuery.contains(ControlBoardDialog.INSTALLATIONS_TABLE)) {
						fromQuery += "," + ControlBoardDialog.INSTALLATIONS_TABLE;
					}
					if(!searchSelectedBoardInstallation.isEmpty() && !searchSelectedBoardInstallation.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoardInstallation + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".installation_id = " + ControlBoardDialog.INSTALLATIONS_TABLE + ".id";
					}
				case "installation":
					if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE)) {
						fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE;
					}
					if(!searchSelectedBoardType.isEmpty() && !searchSelectedBoardType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".type = '" + searchSelectedBoardType + "'";
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".type_id = " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".id";
					}
					if(null != textBoardSearchNames && !textBoardSearchNames.getText().isEmpty()) {
						this.selectWhereQuery();
						whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".name LIKE '%" + textBoardSearchNames.getText() + "%'";
					}
					break;
			}
		}
		
		private List<String> loadList(String table, String column) {
			List<String> list = new ArrayList<String>();
			list.add("Todas");
			list.addAll(db.fetchColumnAsList(db.select("SELECT " + table + "." + column +" "
														+ " FROM "
														+ fromQuery
														+ whereQuery
														+ " GROUP BY " + table + "." + column), column));
			if(list.size() == 1) {
				list.remove(0);
			}
			return list;
		}
		
		private List<String> loadList(String table, String column, String conditionalColumn, boolean activator) {
			List<String> list = new ArrayList<String>();
			list.add("Todas");
			list.addAll(db.fetchColumnAsList(db.select("SELECT " + conditionalColumn +" "
														+ " FROM "
														+ fromQuery
														+ whereQuery
														+ " GROUP BY " + table + "." + column), column));
			if(list.size() == 1) {
				list.remove(0);
			}
			return list;
		}
		
		private void loadComboBoard(String start) {
			switch(start) {
			case "types":
				this.selectWhereQuery();
				fromQuery += "," + ControlBoardDialog.INSTALLATIONS_TABLE;
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".type_id = " + ControlBoardDialog.CONTROL_BOARD_TYPES_TABLE + ".id";
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".installation_id = " + ControlBoardDialog.INSTALLATIONS_TABLE + ".id ";
				comboBoardInstallations.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.INSTALLATIONS_TABLE, "installation"))));
			case "installations":
				fromQuery += "," + ControlBoardDialog.NEMAS_TABLE;
				if(!searchSelectedBoardInstallation.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".installation_id = " + ControlBoardDialog.INSTALLATIONS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("installation");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".nema_id = " + ControlBoardDialog.NEMAS_TABLE + ".id ";
				comboBoardNemas.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.NEMAS_TABLE, "nema"))));
			case "nemas":
				fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE;
				if(!searchSelectedBoardNema.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".nema_id = " + ControlBoardDialog.NEMAS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("nema");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_capacity_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".id ";
				comboBoardBarCapacities.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE, "bar_capacity"))));
			case "bar_capacity":
				fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE;
				if(!searchSelectedBoardBarCapacity.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_capacity_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_CAPACITIES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("bar_capacity");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_type_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".id ";
				comboBoardBarTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE, "bar_type"))));
			case "bar_type":
				fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE;
				if(!searchSelectedBoardBarType.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".bar_type_id = " + ControlBoardDialog.CONTROL_BOARD_BAR_TYPES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("bar_type");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".circuits_id = " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".id ";
				comboBoardCircuits.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE, "circuits"))));
			case "circuits":
				fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE;
				if(!searchSelectedBoardCircuits.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".circuits_id = " + ControlBoardDialog.CONTROL_BOARD_CIRCUITS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("circuits");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".voltage_id = " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".id ";
				comboBoardVoltages.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE, "voltage"))));
			case "voltage":
				if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_TABLE)) {
					fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_TABLE;
				}
				if(!searchSelectedBoardVoltage.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".voltage_id = " + ControlBoardDialog.CONTROL_BOARD_VOLTAGES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("voltage");
				comboBoardPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_TABLE, "phases"))));
			case "phases":
				if(!fromQuery.contains(ControlBoardDialog.CONTROL_BOARD_TABLE)) {
					fromQuery += "," + ControlBoardDialog.CONTROL_BOARD_TABLE;
				}
				if(!searchSelectedBoardPhases.isEmpty() && !searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "' ";
				}
				this.addBoardAdditionalWhere("phases");
				comboBoardGround.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.CONTROL_BOARD_TABLE, "ground", ControlBoardDialog.CONTROL_BOARD_GROUND_FIELD, true))));
			case "ground":
				fromQuery += "," + ControlBoardDialog.INTERRUPTIONS_TABLE;
				if(!searchSelectedBoardGround.isEmpty() && !searchSelectedBoardGround.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".ground = '" + (searchSelectedBoardGround.equalsIgnoreCase("SI")?"1":"0") + "'";
				}
				this.addBoardAdditionalWhere("ground");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".interruption_id = " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".id ";
				comboBoardInterruptions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.INTERRUPTIONS_TABLE, "interruption"))));
			case "interruption":
				fromQuery += "," + ControlBoardDialog.LOCK_TYPES_TABLE;
				if(!searchSelectedBoardInterruption.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".interruption_id = " + ControlBoardDialog.INTERRUPTIONS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("interruption");
				this.selectWhereQuery();
				whereQuery += ControlBoardDialog.CONTROL_BOARD_TABLE + ".lock_type_id = " + ControlBoardDialog.LOCK_TYPES_TABLE + ".id ";
				comboBoardLockTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(ControlBoardDialog.LOCK_TYPES_TABLE, "lock_type"))));
				break;
			}
		}
		
	}
	
}
