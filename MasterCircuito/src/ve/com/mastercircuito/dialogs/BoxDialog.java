package ve.com.mastercircuito.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.StringTools;

public class BoxDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3391822224648515138L;
	
	// Common Tables
		private static final String INSTALLATIONS_TABLE = "installations";
		private static final String NEMAS_TABLE = "nemas";
		private static final String LOCK_TYPES_TABLE = "lock_types";
	// Common Fields
		private static final String INSTALLATION_FIELD = "installations.installation";
		private static final String NEMA_FIELD = "nemas.nema";
	// Box Tables
		private static final String BOXES_TABLE = "boxes";
		private static final String BOX_TYPES_TABLE = "box_types";
		private static final String BOX_SHEETS_TABLE = "box_sheets";
		private static final String BOX_FINISHES_TABLE = "box_finishes";
		private static final String BOX_COLORS_TABLE = "box_colors";
		private static final String BOX_UNITS_TABLE = "box_measure_units";
		private static final String BOX_CALIBERS_TABLE = "box_calibers";
	// Box Fields
		private static final String BOX_ID_FIELD = "boxes.id";
		private static final String BOX_TYPE_FIELD = "box_types.type";
		private static final String BOX_PAIRS_FIELD = "IF(boxes.pairs=0,'N/A',boxes.pairs) as pairs";
		private static final String BOX_SHEET_FIELD = "IF(boxes.sheet_id=0,'N/A',box_sheets.sheet) as sheet";
		private static final String BOX_FINISH_FIELD = "IF(boxes.finish_id=0,'N/A',box_finishes.finish) as finish";
		private static final String BOX_COLOR_FIELD = "IF(boxes.color_id=0,'N/A',box_colors.color) as color";
		private static final String BOX_HEIGHT_FIELD = "IF(boxes.height=0,'N/A',boxes.height) as height";
		private static final String BOX_WIDTH_FIELD = "IF(boxes.width=0,'N/A',boxes.width) as width";
		private static final String BOX_DEPTH_FIELD = "IF(boxes.depth=0,'N/A',boxes.depth) as depth";
		private static final String BOX_UNITS_FIELD = "IF(boxes.units_id=0,'N/A',box_measure_units.units) as units";
		private static final String BOX_CALIBER_FIELD = "IF(boxes.caliber_id=0,'N/A',box_calibers.caliber) as caliber";
		private static final String BOX_LOCK_TYPE_FIELD = "IF(boxes.lock_type_id=0,'N/A',lock_types.lock_type) as lock_type";
		private static final String BOX_PRICE_FIELD = "boxes.price";
	
	private Integer height = 300;
	private Integer width = 1200;
	
	private Db db;
	private Object[][] boxesSearchData;
	private JTable tableBoxesSearchResult;
	private ListSelectionModel listBoxSearchSelectionModel;
	private String searchSelectedBoxType = "", searchSelectedBoxInstallation = "", searchSelectedBoxNema = "", searchSelectedBoxPairs = "", searchSelectedBoxSheet = "", searchSelectedBoxFinish = "", searchSelectedBoxColor = "", searchSelectedBoxCaliber = "", searchSelectedBoxLockType = "";
	private JComboBox<String> comboBoxTypes, comboBoxInstallations, comboBoxNemas, comboBoxPairs, comboBoxSheets, comboBoxFinishes, comboBoxColors, comboBoxCalibers, comboBoxLockTypes;
	private String searchSelectedBoxHeight, searchSelectedBoxWidth, searchSelectedBoxDepth;
	private JTextField textBoxHeight, textBoxWidth, textBoxDepth;
	
	String[] boxesColumnNames = { "Id", "Tipo", "Instalacion", "Nema", "Pares Tel.", "Lamina", "Acabado", "Color", "Alto", "Ancho", "Profundidad", "Und.", "Calibre", "Cerradura", "Precio"};
	
	private int boxSearchId = 0;
	private int boxAddQuantity = 1;
	private double boxPrice;
	
	public BoxDialog(Window owner) {
		this(owner, "");
	}
	
	public BoxDialog(Window owner, String title) {
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
		panelCenter.add(createBoxAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		JScrollPane scrollPane = new JScrollPane(createBoxAddTablePanel());
		tableBoxesSearchResult.setFillsViewportHeight(true);
		panelCenter.add(scrollPane);
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createBoxAddCountPanel());
		panelLower.add(createBoxAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public int getBoxSearchId() {
		return this.boxSearchId;
	}

	public int getBoxAddQuantity() {
		return this.boxAddQuantity;
	}
	
	public double getBoxPrice() {
		return this.boxPrice;
	}
	
	private JPanel createBoxAddSearchPanel() {
		JPanel panelBoxAddSearch = new JPanel();
		panelBoxAddSearch.setLayout(new BoxLayout(panelBoxAddSearch, BoxLayout.PAGE_AXIS));
		JPanel searchBarPanel1 = new JPanel();
		searchBarPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel searchBarPanel2 = new JPanel();
		searchBarPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryTypes = "SELECT " + BoxDialog.BOX_TYPE_FIELD
				+ " FROM " + BoxDialog.BOX_TYPES_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.BOX_TYPES_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".type_id "
				+ " GROUP BY box_types.type";
		
		String queryInstallations = "SELECT " + BoxDialog.INSTALLATION_FIELD
				+ " FROM " + BoxDialog.INSTALLATIONS_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.INSTALLATIONS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".installation_id "
				+ " GROUP BY installations.installation";
		
		String queryNemas = "SELECT " + BoxDialog.NEMA_FIELD
				+ " FROM " + BoxDialog.NEMAS_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.NEMAS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".nema_id "
				+ " GROUP BY nemas.nema";
		
		String queryPairs = "SELECT boxes.pairs "
				+ " FROM " + BoxDialog.BOXES_TABLE
				+ " WHERE boxes.pairs > 0 "
				+ " GROUP BY " + BoxDialog.BOXES_TABLE + ".pairs";
		
		String querySheets = "SELECT " + BoxDialog.BOX_SHEET_FIELD
				+ " FROM " + BoxDialog.BOX_SHEETS_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.BOX_SHEETS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".sheet_id "
				+ " GROUP BY box_sheets.sheet";
		
		String queryFinishes = "SELECT " + BoxDialog.BOX_FINISH_FIELD
				+ " FROM " + BoxDialog.BOX_FINISHES_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.BOX_FINISHES_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".finish_id "
				+ " GROUP BY box_finishes.finish";
		
		String queryColors = "SELECT " + BoxDialog.BOX_COLOR_FIELD
				+ " FROM " + BoxDialog.BOX_COLORS_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.BOX_COLORS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".color_id "
				+ " GROUP BY box_colors.color";
		
		String queryCalibers = "SELECT " + BoxDialog.BOX_CALIBER_FIELD
				+ " FROM " + BoxDialog.BOX_CALIBERS_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.BOX_CALIBERS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".caliber_id "
				+ " GROUP BY box_calibers.caliber";
		
		String queryLockTypes = "SELECT " + BoxDialog.BOX_LOCK_TYPE_FIELD
				+ " FROM " + BoxDialog.LOCK_TYPES_TABLE + "," + BoxDialog.BOXES_TABLE
				+ " WHERE " + BoxDialog.LOCK_TYPES_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".lock_type_id "
				+ " GROUP BY lock_types.lock_type";
		
		JLabel labelType = new JLabel("Tipo:");
		JLabel labelInstallation = new JLabel("Instalacion:");
		JLabel labelNema = new JLabel("Nema:");
		JLabel labelPairs = new JLabel("Pares telefonicos:");
		JLabel labelFinish = new JLabel("Acabado:");
		JLabel labelColor = new JLabel("Color:");
		JLabel labelSheet = new JLabel("Lamina:");
		JLabel labelHeight = new JLabel("Alto");
		JLabel labelWidth = new JLabel("Ancho");
		JLabel labelDepth = new JLabel("Profundidad");
		JLabel labelCaliber = new JLabel("Calibre:");
		JLabel labelLockType = new JLabel("Cerradura:");
		
		ComboBoxListener lForCombo = new ComboBoxListener();
		
		comboBoxTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoxTypes.setActionCommand("box.search.type");
		comboBoxTypes.addActionListener(lForCombo);
		searchBarPanel1.add(labelType);
		searchBarPanel1.add(comboBoxTypes);
		
		comboBoxInstallations = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoxInstallations.setActionCommand("box.search.installation");
		comboBoxInstallations.addActionListener(lForCombo);
		searchBarPanel1.add(labelInstallation);
		searchBarPanel1.add(comboBoxInstallations);
		
		comboBoxNemas = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoxNemas.setActionCommand("box.search.nema");
		comboBoxNemas.addActionListener(lForCombo);
		searchBarPanel1.add(labelNema);
		searchBarPanel1.add(comboBoxNemas);
		
		comboBoxPairs = new JComboBox<String>(new Vector<String>(loadComboList(queryPairs, "pairs")));
		comboBoxPairs.setActionCommand("box.search.pairs");
		comboBoxPairs.addActionListener(lForCombo);
		String selectedBoxType = comboBoxTypes.getSelectedItem().toString();
		if(!selectedBoxType.equalsIgnoreCase("para Pares Telefonicos") && !selectedBoxType.equalsIgnoreCase("Todas")) {
			comboBoxPairs.setEnabled(false);
		} else {
			comboBoxPairs.setEnabled(true);
		}
		searchBarPanel1.add(labelPairs);
		searchBarPanel1.add(comboBoxPairs);
		
		comboBoxSheets = new JComboBox<String>(new Vector<String>(loadComboList(querySheets, "sheet")));
		comboBoxSheets.setActionCommand("box.search.sheet");
		comboBoxSheets.addActionListener(lForCombo);
		searchBarPanel1.add(labelSheet);
		searchBarPanel1.add(comboBoxSheets);
		
		comboBoxFinishes = new JComboBox<String>(new Vector<String>(loadComboList(queryFinishes, "finish")));
		comboBoxFinishes.setActionCommand("box.search.finish");
		comboBoxFinishes.addActionListener(lForCombo);
		searchBarPanel1.add(labelFinish);
		searchBarPanel1.add(comboBoxFinishes);
		
		comboBoxColors = new JComboBox<String>(new Vector<String>(loadComboList(queryColors, "color")));
		comboBoxColors.setActionCommand("box.search.color");
		comboBoxColors.addActionListener(lForCombo);
		searchBarPanel2.add(labelColor);
		searchBarPanel2.add(comboBoxColors);
		
		textBoxHeight = new JTextField(4);
		searchBarPanel2.add(labelHeight);
		searchBarPanel2.add(textBoxHeight);
		
		textBoxWidth = new JTextField(4);
		searchBarPanel2.add(labelWidth);
		searchBarPanel2.add(textBoxWidth);
		
		textBoxDepth = new JTextField(4);
		searchBarPanel2.add(labelDepth);
		searchBarPanel2.add(textBoxDepth);
		
		comboBoxCalibers = new JComboBox<String>(new Vector<String>(loadComboList(queryCalibers, "caliber")));
		comboBoxCalibers.setActionCommand("box.search.caliber");
		comboBoxCalibers.addActionListener(lForCombo);
		searchBarPanel2.add(labelCaliber);
		searchBarPanel2.add(comboBoxCalibers);
		
		comboBoxLockTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoxLockTypes.setActionCommand("box.search.lock_type");
		comboBoxLockTypes.addActionListener(lForCombo);
		searchBarPanel2.add(labelLockType);
		searchBarPanel2.add(comboBoxLockTypes);
		
		panelBoxAddSearch.add(searchBarPanel1);
		panelBoxAddSearch.add(searchBarPanel2);
		
		return panelBoxAddSearch;
	}
	
	private JPanel createBoxAddTablePanel() {
		loadBoxSearchTable("");
		
		MyTableModel mForTable = new MyTableModel(boxesSearchData, boxesColumnNames);
		
		tableBoxesSearchResult = new JTable();
		tableBoxesSearchResult.setModel(mForTable);
		tableBoxesSearchResult.setAutoCreateRowSorter(true);
		tableBoxesSearchResult.getTableHeader().setReorderingAllowed(false);
		
		MyListSelectionListener lForList = new MyListSelectionListener();
		
		listBoxSearchSelectionModel = tableBoxesSearchResult.getSelectionModel();
		listBoxSearchSelectionModel.addListSelectionListener(lForList);
		tableBoxesSearchResult.setSelectionModel(listBoxSearchSelectionModel);
		tableBoxesSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelBoxAddTable = new JPanel(new BorderLayout());
		panelBoxAddTable.add(tableBoxesSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelBoxAddTable.add(tableBoxesSearchResult, BorderLayout.CENTER);
		
		return panelBoxAddTable;
	}
	
	private JPanel createBoxAddCountPanel() {
		JPanel panelCountOuter = new JPanel(new BorderLayout());
		JPanel panelCountInner = new JPanel();
		
		panelCountInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonDecrease = new JButton("-");
		JButton buttonIncrease = new JButton("+");
		JLabel labelQuantity = new JLabel("1");
		boxAddQuantity = 1;
		Integer min = 1;
		Integer max = 100;
		
		panelCountInner.add(buttonDecrease);
		panelCountInner.add(labelQuantity);
		panelCountInner.add(buttonIncrease);
		
		buttonDecrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boxAddQuantity > min) {
					boxAddQuantity--;
					labelQuantity.setText(String.valueOf(boxAddQuantity));
				}
			}
		});
		
		buttonIncrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boxAddQuantity < max) {
					boxAddQuantity++;
					labelQuantity.setText(String.valueOf(boxAddQuantity));
				}
			}
		});
		
		panelCountOuter.add(panelCountInner, BorderLayout.CENTER);
		
		return panelCountOuter;
	}
	
	private JPanel createBoxAddButtonPanel() {
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
				
				if(tableBoxesSearchResult.getSelectedRow() > -1) {
					
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boxSearchId = 0;
				boxAddQuantity = 0;
				boxPrice = 0.00;
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	private void loadBoxSearchTable(String whereQuery) {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(BoxDialog.BOX_ID_FIELD);
		fields.add(BoxDialog.BOX_TYPE_FIELD);
		fields.add(BoxDialog.INSTALLATION_FIELD);
		fields.add(BoxDialog.NEMA_FIELD);
		fields.add(BoxDialog.BOX_PAIRS_FIELD);
		fields.add(BoxDialog.BOX_SHEET_FIELD);
		fields.add(BoxDialog.BOX_FINISH_FIELD);
		fields.add(BoxDialog.BOX_COLOR_FIELD);
		fields.add(BoxDialog.BOX_HEIGHT_FIELD);
		fields.add(BoxDialog.BOX_WIDTH_FIELD);
		fields.add(BoxDialog.BOX_DEPTH_FIELD);
		fields.add(BoxDialog.BOX_UNITS_FIELD);
		fields.add(BoxDialog.BOX_CALIBER_FIELD);
		fields.add(BoxDialog.BOX_LOCK_TYPE_FIELD);
		fields.add(BoxDialog.BOX_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(BoxDialog.BOXES_TABLE);
		tables.add(BoxDialog.BOX_TYPES_TABLE);
		tables.add(BoxDialog.INSTALLATIONS_TABLE);
		tables.add(BoxDialog.NEMAS_TABLE);
		tables.add(BoxDialog.BOX_SHEETS_TABLE);
		tables.add(BoxDialog.BOX_FINISHES_TABLE);
		tables.add(BoxDialog.BOX_COLORS_TABLE);
		tables.add(BoxDialog.BOX_UNITS_TABLE);
		tables.add(BoxDialog.BOX_CALIBERS_TABLE);
		tables.add(BoxDialog.LOCK_TYPES_TABLE);
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boxesQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE boxes.type_id = box_types.id "
						+ "AND ( (boxes.sheet_id > 0 "
							+ "AND boxes.sheet_id = box_sheets.id) "
							+ "OR boxes.sheet_id = 0) "
						+ "AND ( (boxes.finish_id > 0 "
							+ "AND boxes.finish_id = box_finishes.id) "
							+ "OR boxes.finish_id = 0) "
						+ "AND ( (boxes.color_id > 0 "
							+ "AND boxes.color_id = box_colors.id) "
							+ "OR boxes.color_id = 0) "
						+ "AND ( (boxes.units_id > 0 "
							+ "AND boxes.units_id = box_measure_units.id) "
							+ "OR boxes.units_id = 0) "
						+ "AND ( (boxes.caliber_id > 0 "
							+ "AND boxes.caliber_id = box_calibers.id) "
							+ "OR boxes.caliber_id = 0) "
						+ "AND ( (boxes.lock_type_id > 0 "
							+ "AND boxes.lock_type_id = lock_types.id) "
							+ "OR boxes.lock_type_id = 0) "
						+ "AND boxes.installation_id = installations.id "
						+ "AND boxes.nema_id = nemas.id "
						+ "AND boxes.active = '1' "
						+ whereQuery
						+ " GROUP BY boxes.id";

		boxesSearchData = db.fetchAll(db.select(boxesQuery));
		
		
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
			
			if(null != tableBoxesSearchResult 
					&& tableBoxesSearchResult.isFocusOwner() 
					&& lsm.getMinSelectionIndex() > -1) {
				boxSearchId = Integer.valueOf((String)tableBoxesSearchResult.getValueAt(tableBoxesSearchResult.getSelectedRow(), 0));
				boxPrice = Double.valueOf((String) tableBoxesSearchResult.getValueAt(tableBoxesSearchResult.getSelectedRow(), 14));
			} else if(tableBoxesSearchResult.getSelectedRow() == -1){
				boxSearchId = 0;
				boxAddQuantity = 0;
				boxPrice = 0.00;
			}
		}
		
	}
	
	private class ComboBoxListener implements ActionListener {
		
		private String fromQuery = "";
		private String whereQuery = "";
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			String actionCommand = ev.getActionCommand();
			whereQuery = "";
			if (actionCommand.equalsIgnoreCase("box.search.type")) {
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.BOX_TYPES_TABLE;
				whereQuery = " WHERE " + BoxDialog.BOXES_TABLE + ".type_id = " + BoxDialog.BOX_TYPES_TABLE + ".id ";
				searchSelectedBoxType = comboBoxTypes.getSelectedItem().toString();
				this.clearSelectedBoxOptions("type");
				
				if(searchSelectedBoxType.equalsIgnoreCase("de paso")) {
					comboBoxPairs.setEnabled(false);
					comboBoxLockTypes.setEnabled(false);
					comboBoxSheets.setEnabled(true);
					comboBoxFinishes.setEnabled(true);
					comboBoxColors.setEnabled(true);
					textBoxHeight.setEnabled(true);
					textBoxHeight.setEditable(true);
					textBoxWidth.setEnabled(true);
					textBoxWidth.setEditable(true);
					textBoxDepth.setEnabled(true);
					textBoxDepth.setEditable(true);
					comboBoxCalibers.setEnabled(true);
				} else if (searchSelectedBoxType.equalsIgnoreCase("industrial") 
						|| searchSelectedBoxType.equalsIgnoreCase("de Rack") 
						|| searchSelectedBoxType.equalsIgnoreCase("para Transformadores")) {
					comboBoxPairs.setEnabled(false);
					comboBoxSheets.setEnabled(true);
					comboBoxFinishes.setEnabled(true);
					comboBoxColors.setEnabled(true);
					textBoxHeight.setEnabled(true);
					textBoxHeight.setEditable(true);
					textBoxWidth.setEnabled(true);
					textBoxWidth.setEditable(true);
					textBoxDepth.setEnabled(true);
					textBoxDepth.setEditable(true);
					comboBoxCalibers.setEnabled(true);
					comboBoxLockTypes.setEnabled(true);
				} else if (searchSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
					comboBoxPairs.setEnabled(true);
					comboBoxSheets.setEnabled(true);
					comboBoxFinishes.setEnabled(true);
					comboBoxColors.setEnabled(true);
					textBoxHeight.setEnabled(true);
					textBoxHeight.setEditable(true);
					textBoxWidth.setEnabled(true);
					textBoxWidth.setEditable(true);
					textBoxDepth.setEnabled(true);
					textBoxDepth.setEditable(true);
					comboBoxCalibers.setEnabled(true);
					comboBoxLockTypes.setEnabled(true);
				} else {
					comboBoxPairs.setEnabled(true);
					comboBoxSheets.setEnabled(true);
					comboBoxFinishes.setEnabled(true);
					comboBoxColors.setEnabled(true);
					textBoxHeight.setEnabled(true);
					textBoxHeight.setEditable(true);
					textBoxWidth.setEnabled(true);
					textBoxWidth.setEditable(true);
					textBoxDepth.setEnabled(true);
					textBoxDepth.setEditable(true);
					comboBoxCalibers.setEnabled(true);
					comboBoxLockTypes.setEnabled(true);
				}
				
				if(!searchSelectedBoxType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOX_TYPES_TABLE + ".type = '"+searchSelectedBoxType+"' ";
				}
				this.loadComboBox("types");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.installation")) {
				searchSelectedBoxInstallation = comboBoxInstallations.getSelectedItem().toString();
				this.clearSelectedBoxOptions("installation");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.INSTALLATIONS_TABLE;
				if(!searchSelectedBoxInstallation.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.INSTALLATIONS_TABLE + ".installation = '"+searchSelectedBoxInstallation+"' ";
				}
				this.loadComboBox("installations");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.nema")) {
				searchSelectedBoxNema = comboBoxNemas.getSelectedItem().toString();
				this.clearSelectedBoxOptions("nema");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.NEMAS_TABLE;
				if(!searchSelectedBoxNema.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.NEMAS_TABLE + ".nema = '"+searchSelectedBoxNema+"' ";
				}
				this.loadComboBox("nemas");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.pairs")) {
				searchSelectedBoxPairs = comboBoxPairs.getSelectedItem().toString();
				this.clearSelectedBoxOptions("pairs");
				fromQuery = BoxDialog.BOXES_TABLE;
				if(!searchSelectedBoxPairs.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".pairs = '"+searchSelectedBoxPairs+"' ";
				}
				this.loadComboBox("pairs");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.sheet")) {
				searchSelectedBoxSheet = comboBoxSheets.getSelectedItem().toString();
				this.clearSelectedBoxOptions("sheet");
				if (searchSelectedBoxSheet.equalsIgnoreCase("HNF")) {
					comboBoxFinishes.setEnabled(false);
					comboBoxColors.setEnabled(false);
				} else {
					comboBoxFinishes.setEnabled(true);
					comboBoxColors.setEnabled(true);
				}
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.BOX_SHEETS_TABLE;
				if(!searchSelectedBoxSheet.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOX_SHEETS_TABLE + ".sheet = '"+searchSelectedBoxSheet+"' ";
				}
				this.loadComboBox("sheets");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.finish")) {
				searchSelectedBoxFinish = comboBoxFinishes.getSelectedItem().toString();
				this.clearSelectedBoxOptions("finish");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.BOX_FINISHES_TABLE;
				if(!searchSelectedBoxFinish.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOX_FINISHES_TABLE + ".finish = '"+searchSelectedBoxFinish+"' ";
				}
				this.loadComboBox("finishes");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.color")) {
				searchSelectedBoxColor = comboBoxColors.getSelectedItem().toString();
				this.clearSelectedBoxOptions("color");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.BOX_COLORS_TABLE;
				if(!searchSelectedBoxColor.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOX_COLORS_TABLE + ".color = '"+searchSelectedBoxColor+"' ";
				}
				this.loadComboBox("colors");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.caliber")) {
				searchSelectedBoxCaliber = comboBoxCalibers.getSelectedItem().toString();
				this.clearSelectedBoxOptions("caliber");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.BOX_CALIBERS_TABLE;
				if(!searchSelectedBoxCaliber.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOX_CALIBERS_TABLE + ".caliber = '"+searchSelectedBoxCaliber+"' ";
				}
				this.loadComboBox("calibers");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("box.search.lock_type")) {
				searchSelectedBoxLockType = comboBoxLockTypes.getSelectedItem().toString();
				this.clearSelectedBoxOptions("lock_type");
				fromQuery = BoxDialog.BOXES_TABLE + "," + BoxDialog.LOCK_TYPES_TABLE;
				if(!searchSelectedBoxLockType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += BoxDialog.LOCK_TYPES_TABLE + ".lock_type = '"+searchSelectedBoxLockType+"' ";
				}
				this.loadComboBox("lock_types");
				this.dynamicSearch();
			}
		}
		
		private void dynamicSearch() {
			whereQuery = "";
			searchSelectedBoxHeight = textBoxHeight.getText();
			searchSelectedBoxWidth = textBoxWidth.getText();
			searchSelectedBoxDepth = textBoxDepth.getText();
			if(searchSelectedBoxType != null && !searchSelectedBoxType.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxType.isEmpty()) {
				whereQuery += " AND " + BoxDialog.BOX_TYPES_TABLE + ".type = '" + searchSelectedBoxType + "'";
			}
			if(searchSelectedBoxInstallation != null && !searchSelectedBoxInstallation.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxInstallation.isEmpty()) {
				whereQuery += " AND boxes.installation_id = installations.id ";
				whereQuery += " AND " + BoxDialog.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoxInstallation + "'";
			}
			if(searchSelectedBoxNema != null && !searchSelectedBoxNema.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxNema.isEmpty()) {
				whereQuery += " AND " + BoxDialog.NEMAS_TABLE + ".nema = '" + searchSelectedBoxNema + "'";
			}
			if(searchSelectedBoxPairs != null && !searchSelectedBoxPairs.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxPairs.isEmpty() && (searchSelectedBoxType.equalsIgnoreCase("Todas") ||
							searchSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos") ||
							searchSelectedBoxType.isEmpty())) {
				whereQuery += " AND " + BoxDialog.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "'";
			}
			if(searchSelectedBoxSheet != null && !searchSelectedBoxSheet.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxSheet.isEmpty()) {
				whereQuery += " AND " + BoxDialog.BOX_SHEETS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".sheet_id";
				whereQuery += " AND " + BoxDialog.BOX_SHEETS_TABLE + ".sheet = '" + searchSelectedBoxSheet + "'";
			} else {
				whereQuery += "AND ( (boxes.sheet_id > 0 "
						+ "AND boxes.sheet_id = " + BoxDialog.BOX_SHEETS_TABLE + ".id) "
						+ "OR boxes.sheet_id = 0)";
			}
			if(searchSelectedBoxFinish != null && !searchSelectedBoxFinish.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxFinish.isEmpty()) {
				whereQuery += " AND " + BoxDialog.BOX_FINISHES_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".finish_id";
				whereQuery += " AND " + BoxDialog.BOX_FINISHES_TABLE + ".finish = '" + searchSelectedBoxFinish + "'";
			} else {
				whereQuery += "AND ( (boxes.finish_id > 0 "
						+ "AND boxes.finish_id = " + BoxDialog.BOX_FINISHES_TABLE + ".id) "
						+ "OR boxes.finish_id = 0)";
			}
			if(searchSelectedBoxColor != null && !searchSelectedBoxColor.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxColor.isEmpty()) {
				whereQuery += " AND " + BoxDialog.BOX_COLORS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".color_id";
				whereQuery += " AND " + BoxDialog.BOX_COLORS_TABLE + ".color = '" + searchSelectedBoxColor + "'";
			} else {
				whereQuery += "AND ( (boxes.color_id > 0 "
						+ "AND boxes.color_id = " + BoxDialog.BOX_COLORS_TABLE + ".id) "
						+ "OR boxes.color_id = 0)";
			}
			if(searchSelectedBoxHeight != null && !searchSelectedBoxHeight.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxHeight.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
				whereQuery += " AND " + BoxDialog.BOXES_TABLE + ".height = '" + searchSelectedBoxHeight + "'";
			}
			if(searchSelectedBoxWidth != null && !searchSelectedBoxWidth.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxWidth.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
				whereQuery += " AND " + BoxDialog.BOXES_TABLE + ".width = '" + searchSelectedBoxWidth + "'";
			}
			if(searchSelectedBoxDepth != null && !searchSelectedBoxDepth.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxDepth.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
				whereQuery += " AND " + BoxDialog.BOXES_TABLE + ".depth = '" + searchSelectedBoxDepth + "'";
			}
			whereQuery += "AND ( (boxes.units_id > 0 "
					+ "AND boxes.units_id = " + BoxDialog.BOX_UNITS_TABLE + ".id) "
					+ "OR boxes.units_id = 0)";
			if(searchSelectedBoxCaliber != null && !searchSelectedBoxCaliber.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxCaliber.isEmpty()) {
				whereQuery += " AND " + BoxDialog.BOX_CALIBERS_TABLE + ".id = " + BoxDialog.BOXES_TABLE + ".caliber_id";
				whereQuery += " AND " + BoxDialog.BOX_CALIBERS_TABLE + ".caliber = '" + searchSelectedBoxCaliber + "'";
			} else {
				whereQuery += "AND ( (boxes.caliber_id > 0 "
						+ "AND boxes.caliber_id = " + BoxDialog.BOX_CALIBERS_TABLE + ".id) "
						+ "OR boxes.caliber_id = 0)";
			}
			if(searchSelectedBoxLockType != null && !searchSelectedBoxLockType.equalsIgnoreCase("Todas") &&
					!searchSelectedBoxLockType.isEmpty()) {
				whereQuery += " AND boxes.lock_type_id = lock_types.id ";
				whereQuery += "AND " + BoxDialog.LOCK_TYPES_TABLE + ".lock_type = '" + searchSelectedBoxLockType + "'";
			} else {
				whereQuery += "AND ( (boxes.lock_type_id > 0 "
						+ "AND boxes.lock_type_id = " + BoxDialog.LOCK_TYPES_TABLE + ".id) "
						+ "OR boxes.lock_type_id = 0)";
			}
			
			loadBoxSearchTable(whereQuery);
			
			if(boxesSearchData.length > 0) {
				tableBoxesSearchResult.setModel(new MyTableModel(boxesSearchData, boxesColumnNames));
			} else {
				tableBoxesSearchResult.setModel(new DefaultTableModel());
			}
		}
		
		private void clearSelectedBoxOptions(String start) {
			switch(start) {
				case "type":
					searchSelectedBoxInstallation = "";
				case "installation":
					searchSelectedBoxNema = "";
				case "nema":
					searchSelectedBoxPairs = "";
				case "pairs":
					searchSelectedBoxSheet = "";
				case "sheet":
					searchSelectedBoxFinish =  "";
				case "finish":
					searchSelectedBoxColor = "";
				case "color":
					searchSelectedBoxCaliber = "";
				case "caliber":
					searchSelectedBoxLockType = "";
					searchSelectedBoxHeight = "";
					searchSelectedBoxWidth = "";
					searchSelectedBoxDepth = "";
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
		
		private String getSelectWhereQuery(String whereQuery) {
			if(whereQuery.isEmpty()) {
				whereQuery = " WHERE ";
			} else {
				whereQuery += " AND ";
			}
			return whereQuery;
		}
		
		private void loadComboBox(String start) {
			switch(start) {
				case "types":
					this.selectWhereQuery();
					fromQuery += "," + BoxDialog.INSTALLATIONS_TABLE;
					whereQuery += BoxDialog.BOXES_TABLE + ".type_id = " + BoxDialog.BOX_TYPES_TABLE + ".id";
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".installation_id = " + BoxDialog.INSTALLATIONS_TABLE + ".id ";
					comboBoxInstallations.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.INSTALLATIONS_TABLE, "installation"))));
				case "installations":
					fromQuery += "," + BoxDialog.NEMAS_TABLE;
					if(!searchSelectedBoxInstallation.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".installation_id = " + BoxDialog.INSTALLATIONS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("installation");
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".nema_id = " + BoxDialog.NEMAS_TABLE + ".id ";
					comboBoxNemas.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.NEMAS_TABLE, "nema"))));
				case "nemas":
					if (!searchSelectedBoxNema.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".nema_id = " + BoxDialog.NEMAS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("nema");
					if(searchSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
						this.selectWhereQuery();
						whereQuery += "boxes.pairs > 0";
						comboBoxPairs.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOXES_TABLE, "pairs"))));
					} else if(((!searchSelectedBoxInstallation.equalsIgnoreCase("Todas") ||
							searchSelectedBoxInstallation.isEmpty()) ||
							!searchSelectedBoxNema.equalsIgnoreCase("Todas")) && 
							(searchSelectedBoxType.equalsIgnoreCase("Todas") ||
									searchSelectedBoxType.isEmpty())){
						String tempWhereQuery = this.getSelectWhereQuery(whereQuery) + "boxes.pairs > 0";
						comboBoxPairs.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOXES_TABLE, "pairs", tempWhereQuery))));
					}
				case "pairs":
					fromQuery += "," + BoxDialog.BOX_SHEETS_TABLE;
					if(!searchSelectedBoxPairs.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "' ";
					}
					this.addBoxAdditionalWhere("pairs");
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".sheet_id = " + BoxDialog.BOX_SHEETS_TABLE + ".id ";
					comboBoxSheets.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOX_SHEETS_TABLE, "sheet"))));
				case "sheets":
					fromQuery += "," + BoxDialog.BOX_FINISHES_TABLE;
					if(!searchSelectedBoxSheet.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".sheet_id = " + BoxDialog.BOX_SHEETS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("sheet");
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".finish_id = " + BoxDialog.BOX_FINISHES_TABLE + ".id ";
					comboBoxFinishes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOX_FINISHES_TABLE, "finish"))));
				case "finishes":
					fromQuery += "," + BoxDialog.BOX_COLORS_TABLE;
					if(!searchSelectedBoxFinish.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".finish_id = " + BoxDialog.BOX_FINISHES_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("finish");
					this.selectWhereQuery();
					whereQuery += "((boxes.color_id > 0 "
							+ "AND boxes.color_id = box_colors.id) "
							+ "OR boxes.color_id = 0)";
					String tempWhereQuery = this.getSelectWhereQuery(whereQuery) + BoxDialog.BOXES_TABLE + ".color_id = " + BoxDialog.BOX_COLORS_TABLE + ".id";
					comboBoxColors.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOX_COLORS_TABLE, "color", tempWhereQuery))));
				case "colors":
					fromQuery += "," + BoxDialog.BOX_CALIBERS_TABLE;
					if(!searchSelectedBoxColor.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += "((boxes.color_id > 0 "
								+ "AND boxes.color_id = box_colors.id) "
								+ "OR boxes.color_id = 0)";
					}
					this.addBoxAdditionalWhere("color");
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".caliber_id = " + BoxDialog.BOX_CALIBERS_TABLE + ".id ";
					comboBoxCalibers.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.BOX_CALIBERS_TABLE, "caliber"))));
				case "calibers":
					fromQuery += "," + BoxDialog.LOCK_TYPES_TABLE;
					if(!searchSelectedBoxCaliber.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".caliber_id = " + BoxDialog.BOX_CALIBERS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("caliber");
					this.selectWhereQuery();
					whereQuery += BoxDialog.BOXES_TABLE + ".lock_type_id = " + BoxDialog.LOCK_TYPES_TABLE + ".id ";
					comboBoxLockTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(BoxDialog.LOCK_TYPES_TABLE, "lock_type"))));
					break;
			}
		}
		
		private void addBoxAdditionalWhere(String start) {
			//Add this to the filters to check if any option has been selected before and add it to the whereQuery
			//Need to fix this to add the filters backward, NOT ADDING YET WELL
			switch(start) {
				case "caliber":
					if(!fromQuery.contains(BoxDialog.BOX_COLORS_TABLE)) {
						fromQuery += "," + BoxDialog.BOX_COLORS_TABLE;
					}
					if(!searchSelectedBoxColor.isEmpty() && !searchSelectedBoxColor.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOX_COLORS_TABLE + ".color = '" + searchSelectedBoxColor + "'";
						this.selectWhereQuery();
						whereQuery += "((boxes.color_id > 0 "
								+ "AND boxes.color_id = box_colors.id) "
								+ "OR boxes.color_id = 0)";
					}
				case "color":
					if(!fromQuery.contains(BoxDialog.BOX_FINISHES_TABLE)) {
						fromQuery += "," + BoxDialog.BOX_FINISHES_TABLE;
					}
					if(!searchSelectedBoxFinish.isEmpty() && !searchSelectedBoxFinish.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOX_FINISHES_TABLE + ".finish = '" + searchSelectedBoxFinish + "'";
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".finish_id = " + BoxDialog.BOX_FINISHES_TABLE + ".id";
					}
				case "finish":
					if(!fromQuery.contains(BoxDialog.BOX_SHEETS_TABLE)) {
						fromQuery += "," + BoxDialog.BOX_SHEETS_TABLE;
					}
					if(!searchSelectedBoxSheet.isEmpty() && !searchSelectedBoxSheet.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOX_SHEETS_TABLE + ".sheet = '" + searchSelectedBoxSheet + "'";
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".sheet_id = " + BoxDialog.BOX_SHEETS_TABLE + ".id";
					}
				case "sheet":
					if(!fromQuery.contains(BoxDialog.BOXES_TABLE)) {
						fromQuery += "," + BoxDialog.BOXES_TABLE;
					}
					if(!searchSelectedBoxPairs.isEmpty() && !searchSelectedBoxPairs.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "' ";
					}
				case "pairs":
					if(!fromQuery.contains(BoxDialog.NEMAS_TABLE)) {
						fromQuery += "," + BoxDialog.NEMAS_TABLE;
					}
					if (!searchSelectedBoxNema.isEmpty() && !searchSelectedBoxNema.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.NEMAS_TABLE + ".nema = '" + searchSelectedBoxNema + "'";
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".nema_id = " + BoxDialog.NEMAS_TABLE + ".id";
					}
				case "nema":
					if(!fromQuery.contains(BoxDialog.INSTALLATIONS_TABLE)) {
						fromQuery += "," + BoxDialog.INSTALLATIONS_TABLE;
					}
					if(!searchSelectedBoxInstallation.isEmpty() && !searchSelectedBoxInstallation.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoxInstallation + "'";
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".installation_id = " + BoxDialog.INSTALLATIONS_TABLE + ".id";
					}
				case "installation":
					if(!fromQuery.contains(BoxDialog.BOX_TYPES_TABLE)) {
						fromQuery += "," + BoxDialog.BOX_TYPES_TABLE;
					}
					if(!searchSelectedBoxType.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOX_TYPES_TABLE + ".type = '" + searchSelectedBoxType + "'";
						this.selectWhereQuery();
						whereQuery += BoxDialog.BOXES_TABLE + ".type_id = " + BoxDialog.BOX_TYPES_TABLE + ".id";
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
		
		private List<String> loadList(String table, String column, String whereQuery) {
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
		
		
	}
	
}
