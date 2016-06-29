package ve.com.mastercircuito.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import ve.com.mastercircuito.components.MyInternalFrame;
import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.font.Fa;
import ve.com.mastercircuito.utils.Errors;
import ve.com.mastercircuito.utils.Numbers;
import ve.com.mastercircuito.utils.StringTools;

public class MainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717733090547682708L;
	// Form Modes
	private static final int VIEW_MODE = 1;
	private static final int ADD_MODE = 2;
	private static final int EDIT_MODE = 3;
	// Common Tables
	private static final String INSTALLATIONS_TABLE = "installations";
	private static final String NEMAS_TABLE = "nemas";
	private static final String INTERRUPTIONS_TABLE = "interruptions";
	private static final String LOCK_TYPES_TABLE = "lock_types";
	// Common Fields
	private static final String INSTALLATION_FIELD = "installations.installation";
	private static final String NEMA_FIELD = "nemas.nema";
	private static final String INTERRUPTION_FIELD = "interruptions.interruption";
	
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
	// Board Tables
	private static final String BOARD_TABLE = "boards";
	private static final String BOARD_BAR_CAPACITIES_TABLE = "board_bar_capacities";
	private static final String BOARD_BAR_TYPES_TABLE = "board_bar_types";
	private static final String BOARD_CIRCUITS_TABLE = "board_circuits";
	private static final String BOARD_TYPES_TABLE = "board_types";
	private static final String BOARD_VOLTAGES_TABLE = "board_voltages";
	// Board Fields
	private static final String BOARD_ID_FIELD = "boards.id";
	private static final String BOARD_NAME_FIELD = "boards.`name`";
	private static final String BOARD_TYPE_FIELD = "board_types.type";
	private static final String BOARD_BAR_CAPACITY_FIELD = "board_bar_capacities.bar_capacity";
	private static final String BOARD_BAR_TYPE_FIELD = "board_bar_types.bar_type";
	private static final String BOARD_CIRCUITS_FIELD = "board_circuits.circuits";
	private static final String BOARD_VOLTAGE_FIELD = "board_voltages.voltage";
	private static final String BOARD_PHASES_FIELD = "boards.phases";
	private static final String BOARD_GROUND_FIELD = "IF(boards.ground=0,'NO','SI') as ground";
	private static final String BOARD_LOCK_TYPE_FIELD = "lock_types.lock_type";
	private static final String BOARD_PRICE_FIELD = "boards.price";
	// Finish filling up the fields of the board table and the tables above too
	
	private Db db;
	
	private JDesktopPane desktop;
	private JPanel theToolBarPanel;
	private JToolBar toolBar;
	private JButton toolBarButtonSwitches, toolBarButtonBoxes, toolBarButtonPanels, toolBarButtonBudgets, toolBarButtonStarters, toolBarButtonTracing;
	//*** Switch Global Variables and objects ***//
	private MyInternalFrame switchesFrame = new MyInternalFrame();
	private Object[][] switchesData = {};
	// Switch View Objects
	private JComboBox<String> comboSwitchBrands, comboSwitchTypes, comboSwitchPhases, comboSwitchCurrents, comboSwitchInterruptions;
	private String searchSelectedSwitchBrand = "", searchSelectedSwitchType = "", searchSelectedSwitchPhases = "", searchSelectedSwitchCurrent = "", searchSelectedSwitchInterruption = "";
	private JPanel panelSwitchRight, panelSwitchDescription, panelWrapperSwitchDescription, panelSwitchAddNew, panelSwitchEdit;
	private JTextField textSwitchPrice, textSwitchPhases, textSwitchCurrent, textSwitchType, textSwitchBrand;
	private JTextArea textSwitchDescription;
	private JLabel labelSwitchCopy;
	// Switch Table Objects
	private JScrollPane tableSwitchScrollPane;
	private JTable tableSwitchesResult;
	private int switchTableSelectedIndex = -1;
	private ListSelectionModel listSwitchSelectionModel;
	// Switch Add Objects
	private JTextField textSwitchAddPrice, textSwitchAddModel;
	private JTextArea textSwitchAddDescription;
	private JButton buttonSwitchAdd, buttonSwitchEdit, buttonSwitchAddSave, buttonSwitchAddCancel;
	private JComboBox<String> comboSwitchAddBrands, comboSwitchAddTypes, comboSwitchAddPhases, comboSwitchAddCurrents, comboSwitchAddInterruptions, comboSwitchAddVoltages;
	private String addSelectedSwitchBrand;
	// Switch Edit Objects
	private int editSwitchId = 0, editSwitchCurrent = 0, editSwitchInterruption = 0;
	private double editSwitchPrice = 0;
	private String editSwitchPhases, editSwitchBrand, editSwitchType, editSwitchModel, editSwitchVoltage;
	private JTextField textSwitchEditPrice, textSwitchEditModel;
	private JTextArea textSwitchEditDescription;
	private JComboBox<String> comboSwitchEditPhases, comboSwitchEditCurrent, comboSwitchEditBrand, comboSwitchEditType, comboSwitchEditInterruption, comboSwitchEditVoltage;
	private String editSelectedSwitchBrand;
	// Switch Settings Objects
	private JComboBox<String> comboSwitchSettingsBrands, comboSwitchSettingsTypes, comboSwitchSettingsCurrents, comboSwitchSettingsVoltages, comboSwitchSettingsInterruptions;
	
	//*** Box Global Variables and objects ***//
	private MyInternalFrame boxesFrame = new MyInternalFrame();
	private Object[][] boxesData = {};
	// Box View Objects
	private JComboBox<String> comboBoxTypes, comboBoxInstallations, comboBoxNemas, comboBoxPairs, comboBoxSheets, comboBoxFinishes, comboBoxColors, comboBoxCalibers, comboBoxLockTypes;
	private JTextField textBoxHeight, textBoxWidth, textBoxDepth;
	private String searchSelectedBoxType = "", searchSelectedBoxInstallation = "", searchSelectedBoxNema = "", searchSelectedBoxPairs = "", searchSelectedBoxSheet = "", searchSelectedBoxFinish = "", searchSelectedBoxColor = "", searchSelectedBoxHeight = "", searchSelectedBoxWidth = "", searchSelectedBoxDepth = "", searchSelectedBoxCaliber = "", searchSelectedBoxLockType = "";
	private JPanel panelBoxDescription, panelWrapperBoxDescription, panelBoxAddNew, panelBoxEdit;
	private JTextField textBoxDescriptionType, textBoxDescriptionInstallation, textBoxDescriptionNema, textBoxDescriptionPairs, textBoxDescriptionSheet, textBoxDescriptionFinish, textBoxDescriptionColor, textBoxDescriptionHeight, textBoxDescriptionWidth, textBoxDescriptionDepth, textBoxDescriptionUnits, textBoxDescriptionCaliber, textBoxDescriptionLockType, textBoxDescriptionPrice;
	private JTextArea textBoxDescription;
	private JLabel labelBoxCopy;
	// Box Table Objects
	private JScrollPane tableBoxScrollPane;
	private JTable tableBoxesResult;
	private int boxTableSelectedIndex = -1;
	private ListSelectionModel listBoxSelectionModel;
	// Box Add Objects
	private JButton buttonBoxAdd, buttonBoxEdit, buttonBoxAddSave, buttonBoxAddCancel, buttonBoxEditSave, buttonBoxEditCancel;
	private JComboBox<String> comboBoxAddTypes, comboBoxAddInstallations, comboBoxAddNemas, comboBoxAddSheets, comboBoxAddFinishes, comboBoxAddColors, comboBoxAddUnits, comboBoxAddCalibers, comboBoxAddLockTypes;
	private JTextField textBoxAddPairs, textBoxAddHeight, textBoxAddWidth, textBoxAddDepth, textBoxAddCaliberComments, textBoxAddPrice;
	private String addSelectedBoxType = "", addSelectedBoxSheet = "";
	private JTextArea textBoxAddDescription;
	// Box Edit Objects
	private double editBoxPrice = 0;
	private int editBoxId = 0, editBoxPairs = 0, editBoxHeight = 0, editBoxWidth = 0, editBoxDepth = 0;
	private String editSelectedBoxType, editSelectedBoxSheet;
	private String editBoxType, editBoxInstallation, editBoxNema, editBoxSheet, editBoxFinish, editBoxColor, editBoxUnits, editBoxCaliber, editBoxCaliberComments, editBoxLockType;
	private JTextField textBoxEditPairs, textBoxEditHeight, textBoxEditWidth, textBoxEditDepth, textBoxEditCaliberComments, textBoxEditPrice;
	private JComboBox<String> comboBoxEditTypes, comboBoxEditInstallations, comboBoxEditNemas, comboBoxEditSheets, comboBoxEditFinishes, comboBoxEditColors, comboBoxEditUnits, comboBoxEditCalibers, comboBoxEditLockTypes;
	private JTextArea textBoxEditDescription;
	// Box Settings Objects
	private JComboBox<String> comboBoxSettingsColors, comboBoxSettingsCalibers;
	
	//*** Board Global Variables and objects ***//
	private MyInternalFrame boardsFrame = new MyInternalFrame();
	private Object[][] boardsData = {};
	private Object[][] boardSwitchesData = {};
	// Board View Objects
	private JPanel panelBoardLower, panelBoardDescription, panelWrapperBoardDescription;
	private JButton buttonBoardAdd, buttonBoardEdit;
	private JTextField textBoardSearchNames;
	private JComboBox<String> comboBoardTypes, comboBoardInstallations, comboBoardNemas, comboBoardBarCapacities, comboBoardBarTypes, comboBoardCircuits, comboBoardVoltages, comboBoardPhases, comboBoardGround, comboBoardInterruptions, comboBoardLockTypes;
	private String searchSelectedBoardType = "", searchSelectedBoardInstallation = "", searchSelectedBoardNema = "", searchSelectedBoardBarCapacity = "", searchSelectedBoardBarType = "", searchSelectedBoardCircuits = "", searchSelectedBoardVoltage = "", searchSelectedBoardPhases = "", searchSelectedBoardGround = "", searchSelectedBoardInterruption = "", searchSelectedBoardLockType = "";
	private JTextField textBoardDescriptionName, textBoardDescriptionType, textBoardDescriptionInstallation, textBoardDescriptionNema, textBoardDescriptionBarCapacity, textBoardDescriptionBarType, textBoardDescriptionCircuits, textBoardDescriptionVoltage, textBoardDescriptionPhases, textBoardDescriptionGround, textBoardDescriptionInterruption, textBoardDescriptionLockType, textBoardDescriptionPrice;
	private JTextArea textBoardDescription;
	private JLabel labelBoardCopy;
	// Board Table Objects
	private JScrollPane tableBoardScrollPane;
	private JTable tableBoardsResult;
	private ListSelectionModel listBoardSelectionModel;
	private int boardsTableSelectedIndex;
	private int selectedBoardId = 0;
	// Board Switches Objects
	private JPanel boardSwitchesPanel;
	private JTable tableBoardSwitchesResult;
	private ListSelectionModel listBoardSwitchesSelectionModel;
	private int boardSwitchesTableSelectedIndex;
	private JButton buttonAddBoardSwitch, buttonRemoveBoardSwitch;
	private int selectedBoardSwitchId;
	// Board Switches Add Objects
	private JDialog dialogBoardSwitchAdd;
	private Object[][] boardSwitchesSearchData;
	private JTable tableBoardSwitchesSearchResult;
	private ListSelectionModel listBoardSwitchSearchSelectionModel;
	private JComboBox<String> comboBoardSwitchBrands, comboBoardSwitchTypes, comboBoardSwitchPhases, comboBoardSwitchCurrents, comboBoardSwitchInterruptions;
	private int boardSwitchAddQuantity = 1;
	private int boardSwitchSearchQuantity = 1;
	private int boardSwitchSearchId = 0;
	// Board Add Objects
	private JPanel panelBoardAddNew;
	private JButton buttonBoardAddCancel;
	private JTextField textBoardAddName, textBoardAddPrice;
	private JComboBox<String> comboBoardAddType, comboBoardAddInstallation, comboBoardAddNema, comboBoardAddBarCapacity, comboBoardAddBarType, comboBoardAddCircuits, comboBoardAddVoltage, comboBoardAddPhases, comboBoardAddInterruption, comboBoardAddLockType;
	private JCheckBox checkBoardAddGround;
	private JTextArea textBoardAddDescription;
	// Board Edit Objects
	private JPanel panelBoardEdit;
	private JButton buttonBoardAddSave;
	private int editBoardId, editBoardPhases;
	private Double editBoardPrice;
	private String editBoardType, editBoardName, editBoardInstallation, editBoardNema, editBoardBarCapacity, editBoardBarType, editBoardCircuits, editBoardVoltage, editBoardGround, editBoardInterruption, editBoardLockType;
	private JTextField textBoardEditName, textBoardEditPrice;
	private JComboBox<String> comboBoardEditType, comboBoardEditInstallation, comboBoardEditNema, comboBoardEditBarCapacity, comboBoardEditBarType, comboBoardEditCircuits, comboBoardEditVoltage, comboBoardEditPhases, comboBoardEditInterruption, comboBoardEditLockType;
	private JCheckBox checkBoardEditGround;
	private JTextArea textBoardEditDescription;
	private JButton buttonBoardEditSave, buttonBoardEditCancel;
	
	//*** Budget Global Variables and objects ***//
	private MyInternalFrame budgetsFrame = new MyInternalFrame();
	private MyInternalFrame startersFrame = new MyInternalFrame();
	private MyInternalFrame tracingFrame = new MyInternalFrame();
	
	//Budget add Objects
	private JButton buttonBudgetAdd;
	private JButton buttonBudgetEdit;
	private JPanel budgetSwitchesPanel;
	private JPanel panelWrapperBudgetDescription;
	private JScrollPane tableBudgetScrollPane;
	private JTable tableBudgetsResult;
	private JPanel panelBudgetDescription;
	private JPanel budgetBoxesPanel;
	private Component budgetBoardsPanel;
	private JPanel budgetMaterialsPanel;
	private JPanel budgetSpecialsPanel;
	private JPanel budgetNotesPanel;
	
	public static void main(String[] args) {
		new MainView();
	}
	
	protected MainView() {
		
		db = new Db();
		
		/*LoginDialog lDialog = new LoginDialog(null);
		
		lDialog.setVisible(true);
		
		if(!lDialog.isSucceeded()) {
			System.exit(0);
		}*/
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800,600));
		this.setIconImage(new ImageIcon("resources/logo.png").getImage());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		int x = (dim.width / 2) - (this.getWidth()/2);
		int y = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setPreferredSize(dim);
		this.setLocation(x, y);
		this.setTitle("MasterCircuito / Ventas - ( areyalp ) Jesus Flores");
		this.setLayout(new BorderLayout(50,50));
		
		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
		toolBarPanel.add(createToolbar());
		this.add(toolBarPanel, BorderLayout.NORTH);
		
		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		this.add(desktop, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	private JPanel createToolbar() {
		theToolBarPanel = new JPanel(new BorderLayout());
		
		ToolbarButtonListener lForToolbarButton = new ToolbarButtonListener();
		
		toolBar = new JToolBar("Barra de Herramientas");
		
		toolBarButtonSwitches = new JButton("Interruptores", new ImageIcon("resources/interruptor_32x32.jpg"));
		
		toolBarButtonSwitches.setActionCommand("bar.button.switches");
		
		toolBarButtonSwitches.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonSwitches);
		
		toolBar.addSeparator();
		
		toolBarButtonBoxes = new JButton("Cajas", new ImageIcon("resources/caja_industrial_32x32.jpg"));
		
		toolBarButtonBoxes.setActionCommand("bar.button.boxes");
		
		toolBarButtonBoxes.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonBoxes);
		
		toolBar.addSeparator();
		
		toolBarButtonPanels = new JButton("Tableros", new ImageIcon("resources/tablero_32x32.jpg"));
		
		toolBarButtonPanels.setActionCommand("bar.button.panels");
		
		toolBarButtonPanels.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonPanels);
		
		toolBar.addSeparator();
		
		toolBarButtonBudgets = new JButton("Presupuestos", new ImageIcon("resources/presupuesto_32x32.jpg"));
		
		toolBarButtonBudgets.setActionCommand("bar.button.budgets");
		
		toolBarButtonBudgets.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonBudgets);
		
		toolBar.addSeparator();
		
		toolBarButtonStarters = new JButton("Arrancadores", new ImageIcon("resources/arrancador_32x32.jpg"));
		
		toolBarButtonStarters.setActionCommand("bar.button.starters");
		
		toolBarButtonStarters.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonStarters);
		
		toolBar.addSeparator();
		
		toolBarButtonTracing = new JButton("Seguimiento", new ImageIcon("resources/seguimiento_32x32.jpg"));
		
		toolBarButtonTracing.setActionCommand("bar.button.tracing");
		
		toolBarButtonTracing.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonTracing);
		
		toolBar.setFloatable(false);
		
		theToolBarPanel.add(toolBar, BorderLayout.NORTH);
		
		return theToolBarPanel;
	}
	
	private void createSwitchesFrame() {
		switchesFrame = new MyInternalFrame("Interruptores");
		switchesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		switchesFrame.setMaximizable(false);
		switchesFrame.setFrameIcon(new ImageIcon("resources/interruptor_32x32.jpg"));
		switchesFrame.updateUI();
		double aspectRatio = 0.85;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int)(desktop.getHeight()*aspectRatio);
		switchesFrame.setSize(new Dimension(w,h));
		int x = (desktop.getWidth() / 2) - (switchesFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (switchesFrame.getHeight() / 2);
		switchesFrame.setLocation(new Point(x, y));
		switchesFrame.setLayout(new BorderLayout(50,50));
		
		Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		JTabbedPane switchesTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		switchesTabbedPane.addTab(Fa.fa_search, null, createSwitchMainPanel(), "Buscar");
		switchesTabbedPane.addTab(Fa.fa_pencil_square_o, null, createSwitchSettingsPanel(), "Editar");
		switchesTabbedPane.setFont(fa);
		
		switchesFrame.add(switchesTabbedPane);
		switchesFrame.setVisible(true);
		desktop.add(switchesFrame);
		try{
			switchesFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private void createBoxesFrame() {
		boxesFrame = new MyInternalFrame("Cajas");
		boxesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		boxesFrame.setMaximizable(false);
		boxesFrame.setFrameIcon(new ImageIcon("resources/caja_industrial_32x32.jpg"));
		boxesFrame.updateUI();
		double aspectRatio = 0.99;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int) (desktop.getHeight() * aspectRatio);
		boxesFrame.setSize(new Dimension(w,h));
		
		int x = (desktop.getWidth() / 2) - (boxesFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (boxesFrame.getHeight() / 2);
		boxesFrame.setLocation(new Point(x, y));
		
		boxesFrame.setLayout(new BorderLayout(50, 50));
		
		Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		JTabbedPane boxesTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		boxesTabbedPane.addTab(Fa.fa_search, null, createBoxMainPanel(), "Buscar");
		boxesTabbedPane.addTab(Fa.fa_pencil_square_o, null, createBoxSettingsPanel(), "Editar");
		boxesTabbedPane.setFont(fa);
		
		boxesFrame.add(boxesTabbedPane);
		boxesFrame.setVisible(true);
		desktop.add(boxesFrame);
		try{
			boxesFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private void createBoardsFrame() {
		boardsFrame = new MyInternalFrame("Tableros");
		boardsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		boardsFrame.setMaximizable(false);
		boardsFrame.setFrameIcon(new ImageIcon("resources/tablero_32x32.jpg"));
		double aspectRatio = 0.99;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int) (desktop.getHeight() * aspectRatio);
		boardsFrame.setSize(new Dimension(w,h));
		
		int x = (desktop.getWidth() / 2) - (boardsFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (boardsFrame.getHeight() / 2);
		boardsFrame.setLocation(new Point(x, y));
		
		boardsFrame.setLayout(new BorderLayout(50, 50));
		
		Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		JTabbedPane boardsTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		boardsTabbedPane.addTab(Fa.fa_search, null, createBoardMainPanel(), "Buscar");
		boardsTabbedPane.addTab(Fa.fa_pencil_square_o, null, createBoardSettingsPanel(), "Editar");
		boardsTabbedPane.setFont(fa);
		
		boardsFrame.add(boardsTabbedPane);
		
		boardsFrame.setVisible(true);
		desktop.add(boardsFrame);
		try{
			boardsFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private void createBudgetsFrame() {
		budgetsFrame = new MyInternalFrame("Presupuestos");
		budgetsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		budgetsFrame.setMaximizable(false);
		budgetsFrame.setFrameIcon(new ImageIcon("resources/presupuesto_32x32.jpg"));
		double aspectRatio = 0.99;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int)(desktop.getHeight()*aspectRatio);
		budgetsFrame.setSize(new Dimension(w,h));
		
		int x = (desktop.getWidth() / 2) - (budgetsFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (budgetsFrame.getHeight() / 2);
		budgetsFrame.setLocation(new Point(x, y));
		
		budgetsFrame.setLayout(new BorderLayout(50, 50));
						
		Font fa = null;
		
		
		JTabbedPane budgetsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		budgetsTabbedPane.addTab(Fa.fa_search, null, createBudgetMainPanel(), "Buscar");
//		budgetsTabbedPane.addTab(Fa.fa_pencil_square_o, null, createBudgetSettingsPanel(), "Editar");
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		//This tabbed pane is duplicated above
//		JTabbedPane budgetsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		budgetsTabbedPane.addTab("Buscar", null, createBudgetMainPanel(), "Presupuesto");
//		budgetsTabbedPane.addTab("Interruptores", null, createBudgetSettingsPanel(), "Interruptores");
//		budgetsTabbedPane.addTab("Cajas", null, createBudgetSettingsPanel(), "Cajas");
//		budgetsTabbedPane.addTab("Tableros", null, createBudgetSettingsPanel(), "Tableros");
//		budgetsTabbedPane.addTab("Adicionales", null, createBudgetSettingsPanel(), "Adicionales");
		
		budgetsTabbedPane.setFont(fa);
		
		budgetsFrame.add(budgetsTabbedPane);
		
		budgetsFrame.setVisible(true);
		
		desktop.add(budgetsFrame);
		try{
			budgetsFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}

	}
	
	private JPanel createBudgetSettingsPanel() {
		JPanel panelBudgetSettings = new JPanel();
		return panelBudgetSettings;
	}

	private void createStartersFrame() {
		startersFrame = new MyInternalFrame("Arrancadores");
		startersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		startersFrame.setMaximizable(false);
		startersFrame.setFrameIcon(new ImageIcon("resources/arrancador_32x32.jpg"));
		double aspectRatio = 0.7;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int)(desktop.getHeight()*aspectRatio);
		startersFrame.setSize(new Dimension(w,h));
		
		int x = (desktop.getWidth() / 2) - (startersFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (startersFrame.getHeight() / 2);
		startersFrame.setLocation(new Point(x, y));
		
		startersFrame.setVisible(true);
		desktop.add(startersFrame);
		try{
			startersFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private void createTracingFrame() {
		tracingFrame = new MyInternalFrame("Seguimiento");
		tracingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tracingFrame.setMaximizable(false);
		tracingFrame.setFrameIcon(new ImageIcon("resources/seguimiento_32x32.jpg"));
		double aspectRatio = 0.7;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int)(desktop.getHeight()*aspectRatio);
		tracingFrame.setSize(new Dimension(w,h));
		
		int x = (desktop.getWidth() / 2) - (tracingFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (tracingFrame.getHeight() / 2);
		tracingFrame.setLocation(new Point(x, y));
		
		tracingFrame.setVisible(true);
		desktop.add(tracingFrame);
		try{
			tracingFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private JPanel createSwitchMainPanel() {
		JPanel switchMainPanel = new JPanel();
		switchMainPanel.setLayout(new BorderLayout(20,20));
		
		switchMainPanel.add(createSwitchSearchBarPanel(), BorderLayout.NORTH);
		
		tableSwitchScrollPane = new JScrollPane(createSwitchTablePanel());
		tableSwitchesResult.setFillsViewportHeight(true);
		switchMainPanel.add(tableSwitchScrollPane, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		ButtonListener lForButton = new ButtonListener();
		
		buttonSwitchAdd = new JButton("Agregar");
		buttonSwitchAdd.setActionCommand("switch.search.buttons.add");
		buttonSwitchAdd.addActionListener(lForButton);
		panelButtons.add(buttonSwitchAdd);
		
		buttonSwitchEdit = new JButton("Editar");
		buttonSwitchEdit.setActionCommand("switch.search.buttons.edit");
		buttonSwitchEdit.addActionListener(lForButton);
		buttonSwitchEdit.setEnabled(false);
		
		panelButtons.add(buttonSwitchEdit);
		
		panelSwitchRight = new JPanel(new BorderLayout(20,20));
		
		panelSwitchDescription = createSwitchDescriptionPanel();
		
		panelSwitchAddNew = createSwitchAddPanel();
		panelSwitchAddNew.setVisible(false);
		
		panelSwitchEdit = createSwitchEditPanel();
		panelSwitchEdit.setVisible(false);
		
		panelWrapperSwitchDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		panelWrapperSwitchDescription.add(panelSwitchDescription);
		panelSwitchRight.add(panelWrapperSwitchDescription, BorderLayout.CENTER);
		panelSwitchRight.add(panelButtons, BorderLayout.SOUTH);
		
		switchMainPanel.add(panelSwitchRight, BorderLayout.EAST);
		
		return switchMainPanel;
	}
	
	private JPanel createSwitchSettingsPanel() {
		JPanel panelSwitchSettings = new JPanel();
		panelSwitchSettings.setLayout(new BorderLayout(20, 20));
		
		JPanel subPanelSwitchSettings = new JPanel();
		subPanelSwitchSettings.setLayout(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0,0,5,5);
		
		String queryBrands = "SELECT brand FROM switch_brands";
		String queryTypes = "SELECT type FROM switch_types";
		String queryCurrents = "SELECT current FROM currents";
		String queryVoltages = "SELECT voltage FROM switch_voltages";
		String queryInterruptions = "SELECT interruption FROM interruptions";
		
		JLabel labelBrand = new JLabel("Marca");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(labelBrand, cs);
		
		comboSwitchSettingsBrands = new JComboBox<String>(new Vector<String>(loadComboList(queryBrands, "brand")));
		comboSwitchSettingsBrands.removeItem("Todas");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelSwitchSettings.add(comboSwitchSettingsBrands, cs);
		
		cs.gridx = 2;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(createSettingsPanelAddRemove("switch", "brands"), cs);
		
		JLabel labelType = new JLabel("Tipo");
		cs.gridx = 3;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(labelType, cs);
		
		comboSwitchSettingsTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboSwitchSettingsTypes.removeItem("Todas");
		cs.gridx = 3;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelSwitchSettings.add(comboSwitchSettingsTypes, cs);
		
		cs.gridx = 5;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(createSettingsPanelAddRemove("switch", "types"), cs);
		
		JLabel labelCurrents = new JLabel("Amperaje");
		cs.gridx = 6;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(labelCurrents, cs);
		
		comboSwitchSettingsCurrents = new JComboBox<String>(new Vector<String>(loadComboList(queryCurrents, "current")));
		comboSwitchSettingsCurrents.removeItem("Todas");
		cs.gridx = 6;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelSwitchSettings.add(comboSwitchSettingsCurrents, cs);
		
		cs.gridx = 8;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(createSettingsPanelAddRemove("switch", "currents"), cs);
		
		JLabel labelVoltages = new JLabel("Voltaje");
		cs.gridx = 9;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(labelVoltages, cs);
		
		comboSwitchSettingsVoltages = new JComboBox<String>(new Vector<String>(loadComboList(queryVoltages, "voltage")));
		comboSwitchSettingsVoltages.removeItem("Todas");
		cs.gridx = 9;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelSwitchSettings.add(comboSwitchSettingsVoltages, cs);
		
		cs.gridx = 11;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(createSettingsPanelAddRemove("switch", "voltages"), cs);
		
		JLabel labelInterruptions = new JLabel("Interrupcion");
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(labelInterruptions, cs);
		
		comboSwitchSettingsInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboSwitchSettingsInterruptions.removeItem("Todas");
		cs.gridx = 12;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelSwitchSettings.add(comboSwitchSettingsInterruptions, cs);
		
		cs.gridx = 14;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelSwitchSettings.add(createSettingsPanelAddRemove("switch", "interruptions"), cs);
		
		panelSwitchSettings.add(subPanelSwitchSettings, BorderLayout.NORTH);
		
		return panelSwitchSettings;
	}
	
	private JPanel createSettingsPanelAddRemove(String element, String actionCommand) {
		MouseActionListener lForMouse = new MouseActionListener();
		JPanel panelSwitchSettingsAddRemove = new JPanel();
		
		Font fa = null;
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 18f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		panelSwitchSettingsAddRemove.setLayout(new BoxLayout(panelSwitchSettingsAddRemove, BoxLayout.PAGE_AXIS));
		
		JButton buttonSwitchSettingBrandAdd = new JButton(Fa.fa_plus);
		buttonSwitchSettingBrandAdd.setContentAreaFilled(false);
		buttonSwitchSettingBrandAdd.setActionCommand(element + ".settings." + actionCommand + ".add");
		buttonSwitchSettingBrandAdd.addMouseListener(lForMouse);
		buttonSwitchSettingBrandAdd.addActionListener(lForMouse);
		buttonSwitchSettingBrandAdd.setMargin(new Insets(0, 0, 0, 0));
		buttonSwitchSettingBrandAdd.setFocusPainted(true);
		buttonSwitchSettingBrandAdd.setBorderPainted(false);
		buttonSwitchSettingBrandAdd.setFont(fa);
		buttonSwitchSettingBrandAdd.setForeground(Color.GREEN);
		panelSwitchSettingsAddRemove.add(buttonSwitchSettingBrandAdd);
		
		JButton buttonSwitchSettingBrandRemove = new JButton(Fa.fa_remove);
		buttonSwitchSettingBrandRemove.setContentAreaFilled(false);
		buttonSwitchSettingBrandRemove.setActionCommand(element + ".settings." + actionCommand + ".remove");
		buttonSwitchSettingBrandRemove.addMouseListener(lForMouse);
		buttonSwitchSettingBrandRemove.addActionListener(lForMouse);
		buttonSwitchSettingBrandRemove.setMargin(new Insets(0, 0, 0, 0));
		buttonSwitchSettingBrandRemove.setFocusPainted(true);
		buttonSwitchSettingBrandRemove.setBorderPainted(false);
		buttonSwitchSettingBrandRemove.setFont(fa);
		buttonSwitchSettingBrandRemove.setForeground(Color.RED);
		panelSwitchSettingsAddRemove.add(buttonSwitchSettingBrandRemove);
		
		return panelSwitchSettingsAddRemove;
	}
	
	private JPanel createSwitchSearchBarPanel() {
		JPanel searchBarPanel = new JPanel();
		searchBarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryBrands = "SELECT brand "
				+ "FROM switch_brands, switches "
				+ "WHERE switch_brands.id = switches.brand_id "
				+ "GROUP BY switch_brands.brand";
		
		String queryTypes = "SELECT type "
				+ "FROM switch_types, switches "
				+ "WHERE switch_types.id = switches.type_id "
				+ "GROUP BY switch_types.type";
		
		String queryPhases = "SELECT phases "
				+ "FROM switches "
				+ "GROUP BY phases";
		
		String queryCurrents = "SELECT current "
				+ "FROM currents, switches "
				+ "WHERE currents.id = switches.current_id "
				+ "GROUP BY currents.current";
		
		String queryInterruptions = "SELECT interruption "
				+ "FROM interruptions, switches "
				+ "WHERE interruptions.id = switches.interruption_id "
				+ "GROUP BY interruptions.interruption";
		
		JLabel brandsLabel = new JLabel("Marca:");
		JLabel typesLabel = new JLabel("Tipo:");
		JLabel phasesLabel = new JLabel("Fases:");
		JLabel currentsLabel = new JLabel("Amperaje:");
		JLabel interruptionsLabel = new JLabel("Interrupcion:");
		
		ComboBoxListener lForCombo = new ComboBoxListener();
		
		comboSwitchBrands = new JComboBox<String>(new Vector<String>(loadComboList(queryBrands, "brand")));
		comboSwitchBrands.setActionCommand("switch.bar.brand");
		comboSwitchBrands.addActionListener(lForCombo);
		searchBarPanel.add(brandsLabel);
		searchBarPanel.add(comboSwitchBrands);
		
		comboSwitchTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboSwitchTypes.setActionCommand("switch.bar.type");
		comboSwitchTypes.addActionListener(lForCombo);
		searchBarPanel.add(typesLabel);
		searchBarPanel.add(comboSwitchTypes);
		
		comboSwitchPhases = new JComboBox<String>(new Vector<String>(loadComboList(queryPhases, "phases")));
		comboSwitchPhases.setActionCommand("switch.bar.phases");
		comboSwitchPhases.addActionListener(lForCombo);
		searchBarPanel.add(phasesLabel);
		searchBarPanel.add(comboSwitchPhases);
		
		comboSwitchCurrents = new JComboBox<String>(new Vector<String>(loadComboList(queryCurrents, "current")));
		comboSwitchCurrents.setActionCommand("switch.bar.current");
		comboSwitchCurrents.addActionListener(lForCombo);
		searchBarPanel.add(currentsLabel);
		searchBarPanel.add(comboSwitchCurrents);
		
		comboSwitchInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboSwitchInterruptions.setActionCommand("switch.bar.interruption");
		comboSwitchInterruptions.addActionListener(lForCombo);
		searchBarPanel.add(interruptionsLabel);
		searchBarPanel.add(comboSwitchInterruptions);
		
		searchBarPanel.add(separator());
		
		JButton searchButton = new JButton("Buscar");
		searchButton.setActionCommand("switch.search.bar.button");
		SearchButtonListener lForSearchButton = new SearchButtonListener();
		searchButton.addActionListener(lForSearchButton);
		searchBarPanel.add(searchButton);
		
		return searchBarPanel;
	}
	
	private JPanel createSwitchTablePanel() {
		String switchesQuery = "SELECT switches.id, "
									+ "switches.model, "
									+ "switch_brands.brand, "
									+ "switch_types.type, "
									+ "switches.phases, "
									+ "currents.current, "
									+ "interruptions.interruption, "
									+ "switch_voltages.voltage, "
									+ "switches.price "
								+ "FROM switches, "
									+ "switch_brands, "
									+ "switch_types, "
									+ "currents, "
									+ "interruptions, "
									+ "switch_voltages "
								+ "WHERE switches.brand_id = switch_brands.id "
								+ "AND switches.type_id = switch_types.id "
								+ "AND switches.current_id = currents.id "
								+ "AND switches.interruption_id = interruptions.id "
								+ "AND switches.voltage_id = switch_voltages.id "
								+ "AND switches.active = '1'";
		
		switchesData = db.fetchAll(db.select(switchesQuery));
		
		String[] switchesColumnNames = { "Id", "Modelo", "Marca", "Tipo", "Fases", "Amperaje", "Interrupcion", "Voltaje", "Precio"};
		
		MyTableModel mForTable = new MyTableModel(switchesData, switchesColumnNames);
		
		tableSwitchesResult = new JTable();
		tableSwitchesResult.setModel(mForTable);
		tableSwitchesResult.setAutoCreateRowSorter(true);
		tableSwitchesResult.getTableHeader().setReorderingAllowed(false);
		
		SharedListSelectionListener lForList = new SharedListSelectionListener();
		
		listSwitchSelectionModel = tableSwitchesResult.getSelectionModel();
		listSwitchSelectionModel.addListSelectionListener(lForList);
		tableSwitchesResult.setSelectionModel(listSwitchSelectionModel);
		tableSwitchesResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableSwitchesResult.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(tableSwitchesResult, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private JPanel createSwitchDescriptionPanel() {
		JPanel descriptionPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPrice, cs);
		
		textSwitchPrice = new JTextField("", 8);
		textSwitchPrice.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 8;
		descriptionPanel.add(textSwitchPrice, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 9;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPhases, cs);
		
		textSwitchPhases = new JTextField("", 2);
		textSwitchPhases.setEditable(false);
		cs.gridx = 10;
		cs.gridy = 0;
		cs.gridwidth = 2;
		descriptionPanel.add(textSwitchPhases, cs);
		
		JLabel labelCurrent = new JLabel("Amperaje:");
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelCurrent, cs);
		
		textSwitchCurrent = new JTextField("", 3);
		textSwitchCurrent.setEditable(false);
		cs.gridx = 13;
		cs.gridy = 0;
		cs.gridwidth = 3;
		descriptionPanel.add(textSwitchCurrent, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelType, cs);
		
		textSwitchType = new JTextField("", 4);
		textSwitchType.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textSwitchType, cs);
		
		JLabel labelBrand = new JLabel("Marca:");
		cs.gridx = 5;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelBrand, cs);
		
		textSwitchBrand = new JTextField("", 10);
		textSwitchBrand.setEditable(false);
		cs.gridx = 6;
		cs.gridy = 1;
		cs.gridwidth = 10;
		descriptionPanel.add(textSwitchBrand, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		descriptionPanel.add(labelDescription, cs);
		
		textSwitchDescription = new JTextArea(3, 16);
		textSwitchDescription.setEditable(false);
		textSwitchDescription.setLineWrap(true);
		textSwitchDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 16;
		descriptionPanel.add(textSwitchDescription, cs);
		
		SearchButtonListener lForButton = new SearchButtonListener();
		
		JButton buttonCopy = new JButton("Copiar");
		buttonCopy.addActionListener(lForButton);
		buttonCopy.setActionCommand("search.description.copy");
		cs.gridx = 0;
		cs.gridy = 6;
		cs.gridwidth = 4;
		descriptionPanel.add(buttonCopy, cs);
		
		labelSwitchCopy = new JLabel("");
		cs.gridx = 4;
		cs.gridy = 6;
		cs.gridwidth = 12;
		descriptionPanel.add(labelSwitchCopy, cs);
		
		return descriptionPanel;
	}
	
	private JPanel createSwitchAddPanel() {
		JPanel addPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		String queryBrands = "SELECT switch_brands.brand "
				+ "FROM switch_brands "
				+ "GROUP BY switch_brands.brand";
		
		String queryCurrents = "SELECT current "
				+ "FROM currents "
				+ "GROUP BY currents.current";
		
		String queryInterruptions = "SELECT interruption "
				+ "FROM interruptions "
				+ "GROUP BY interruptions.interruption";
		
		String queryVoltages = "SELECT voltage "
				+ "FROM switch_voltages "
				+ "GROUP BY switch_voltages.voltage";
		
		List<String> listPhases = new ArrayList<String>();
		listPhases.add("1");
		listPhases.add("2");
		listPhases.add("3");
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelPrice, cs);
		
		textSwitchAddPrice = new JTextField("", 6);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(textSwitchAddPrice, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 7;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelPhases, cs);
		
		comboSwitchAddPhases = new JComboBox<String>(new Vector<String>(listPhases));
		comboSwitchAddPhases.setActionCommand("switch.description.add.phases");
		comboSwitchAddPhases.addActionListener(lForCombo);
		cs.gridx = 8;
		cs.gridy = 0;
		cs.gridwidth = 2;
		addPanel.add(comboSwitchAddPhases, cs);
		
		JLabel labelCurrent = new JLabel("Amperaje:");
		cs.gridx = 10;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelCurrent, cs);
		
		comboSwitchAddCurrents = new JComboBox<String>(new Vector<String>(loadComboList(queryCurrents, "current")));
		comboSwitchAddCurrents.removeItem("Todas");
		comboSwitchAddCurrents.setActionCommand("switch.description.add.current");
		comboSwitchAddCurrents.addActionListener(lForCombo);
		cs.gridx = 11;
		cs.gridy = 0;
		cs.gridwidth = 2;
		addPanel.add(comboSwitchAddCurrents, cs);
		
		JLabel labelBrand = new JLabel("Marca:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelBrand, cs);
		
		comboSwitchAddBrands = new JComboBox<String>(new Vector<String>(loadComboList(queryBrands, "brand")));
		comboSwitchAddBrands.removeItem("Todas");
		comboSwitchAddBrands.setActionCommand("switch.description.add.brand");
		comboSwitchAddBrands.addActionListener(lForCombo);
		String selectedBrand = comboSwitchAddBrands.getSelectedItem().toString();
		String additionalFrom = "";
		String additionalWhere = "";
		if(selectedBrand != null && !selectedBrand.isEmpty()) {
			additionalFrom = ", switch_brands ";
			additionalWhere = "AND switch_brands.brand = '" + selectedBrand + "' ";
		}
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 8;
		addPanel.add(comboSwitchAddBrands, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 9;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelType, cs);
		
		String queryTypes = "SELECT type "
				+ "FROM switch_types "
				+ additionalFrom
				+ "WHERE switch_types.brand_id = switch_brands.id "
				+ additionalWhere
				+ "GROUP BY switch_types.type ";
		
		comboSwitchAddTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboSwitchAddTypes.removeItem("Todas");
		comboSwitchAddTypes.setActionCommand("switch.description.add.type");
		comboSwitchAddTypes.addActionListener(lForCombo);
		cs.gridx = 10;
		cs.gridy = 1;
		cs.gridwidth = 4;
		addPanel.add(comboSwitchAddTypes, cs);
		
		JLabel labelInterruption = new JLabel("Int.:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelInterruption, cs);
		
		comboSwitchAddInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboSwitchAddInterruptions.removeItem("Todas");
		comboSwitchAddInterruptions.setActionCommand("switch.description.add.interruption");
		comboSwitchAddInterruptions.addActionListener(lForCombo);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 2;
		addPanel.add(comboSwitchAddInterruptions, cs);
		
		JLabel labelModel = new JLabel("Modelo:");
		cs.gridx = 3;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelModel, cs);
		
		textSwitchAddModel = new JTextField("", 6);
		cs.gridx = 4;
		cs.gridy = 2;
		cs.gridwidth = 6;
		addPanel.add(textSwitchAddModel, cs);
		
		JLabel labelVoltage = new JLabel("Voltaje:");
		cs.gridx = 10;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelVoltage, cs);
		
		comboSwitchAddVoltages = new JComboBox<String>(new Vector<String>(loadComboList(queryVoltages, "voltage")));
		comboSwitchAddVoltages.removeItem("Todas");
		cs.gridx = 11;
		cs.gridy = 2;
		cs.gridwidth = 3;
		addPanel.add(comboSwitchAddVoltages, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 14;
		addPanel.add(labelDescription, cs);
		
		textSwitchAddDescription = new JTextArea(3, 14);
		textSwitchAddDescription.setEditable(false);
		textSwitchAddDescription.setLineWrap(true);
		textSwitchAddDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 14;
		addPanel.add(textSwitchAddDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		buttonSwitchAddSave = new JButton("Guardar");
		buttonSwitchAddSave.setActionCommand("switch.description.add.save");
		buttonSwitchAddSave.addActionListener(lForButton);
		panelButtons.add(buttonSwitchAddSave);
		
		buttonSwitchAddCancel = new JButton("Cancelar");
		buttonSwitchAddCancel.setActionCommand("switch.description.add.cancel");
		buttonSwitchAddCancel.addActionListener(lForButton);
		panelButtons.add(buttonSwitchAddCancel);
		
		cs.gridx = 0;
		cs.gridy = 7;
		cs.gridwidth = 16;
		addPanel.add(panelButtons, cs);
		
		return addPanel;
	}
	
	private JPanel createSwitchEditPanel() {
		JPanel editPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelPrice, cs);
		
		textSwitchEditPrice = new JTextField("", 8);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 8;
		editPanel.add(textSwitchEditPrice, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 9;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelPhases, cs);
		
		comboSwitchEditPhases = new JComboBox<String>();
		comboSwitchEditPhases.setActionCommand("switch.description.edit.phases");
		comboSwitchEditPhases.addActionListener(lForCombo);
		cs.gridx = 10;
		cs.gridy = 0;
		cs.gridwidth = 2;
		editPanel.add(comboSwitchEditPhases, cs);
		
		JLabel labelCurrent = new JLabel("Amperaje:");
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelCurrent, cs);
		
		comboSwitchEditCurrent = new JComboBox<String>();
		comboSwitchEditCurrent.setActionCommand("switch.description.edit.current");
		comboSwitchEditCurrent.addActionListener(lForCombo);
		cs.gridx = 13;
		cs.gridy = 0;
		cs.gridwidth = 3;
		editPanel.add(comboSwitchEditCurrent, cs);
		
		JLabel labelBrand = new JLabel("Marca:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelBrand, cs);
		
		comboSwitchEditBrand = new JComboBox<String>();
		comboSwitchEditBrand.setActionCommand("switch.description.edit.brand");
		comboSwitchEditBrand.addActionListener(lForCombo);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 10;
		editPanel.add(comboSwitchEditBrand, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 11;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelType, cs);
		
		comboSwitchEditType = new JComboBox<String>();
		comboSwitchEditType.setActionCommand("switch.description.edit.type");
		comboSwitchEditType.addActionListener(lForCombo);
		cs.gridx = 12;
		cs.gridy = 1;
		cs.gridwidth = 4;
		editPanel.add(comboSwitchEditType, cs);
		
		JLabel labelInterruption = new JLabel("Interrupcion:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelInterruption, cs);
		
		comboSwitchEditInterruption = new JComboBox<String>();
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 2;
		editPanel.add(comboSwitchEditInterruption, cs);
		
		JLabel labelModel = new JLabel("Modelo:");
		cs.gridx = 3;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelModel, cs);
		
		textSwitchEditModel = new JTextField("",8);
		cs.gridx = 4;
		cs.gridy = 2;
		cs.gridwidth = 8;
		editPanel.add(textSwitchEditModel, cs);
		
		JLabel labelVoltage = new JLabel("Voltaje:");
		cs.gridx = 12;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelVoltage, cs);
		
		comboSwitchEditVoltage = new JComboBox<String>();
		cs.gridx = 13;
		cs.gridy = 2;
		cs.gridwidth = 3;
		editPanel.add(comboSwitchEditVoltage, cs);
		
		textSwitchEditDescription = new JTextArea(3,16);
		textSwitchEditDescription.setEditable(false);
		textSwitchEditDescription.setLineWrap(true);
		textSwitchEditDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 16;
		editPanel.add(textSwitchEditDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton buttonSwitchEditSave = new JButton("Guardar");
		buttonSwitchEditSave.setActionCommand("switch.description.edit.save");
		buttonSwitchEditSave.addActionListener(lForButton);
		panelButtons.add(buttonSwitchEditSave);
		
		JButton buttonSwitchEditCancel = new JButton("Cancelar");
		buttonSwitchEditCancel.setActionCommand("switch.description.edit.cancel");
		buttonSwitchEditCancel.addActionListener(lForButton);
		panelButtons.add(buttonSwitchEditCancel);
		
		cs.gridx = 0;
		cs.gridy = 7;
		cs.gridwidth = 16;
		editPanel.add(panelButtons, cs);
		
		return editPanel;
	}
	
	private void setSwitchesMode(int mode) {
		
		if(mode == MainView.VIEW_MODE) {
			if(panelSwitchAddNew.isVisible()) {
				buttonSwitchAdd.setEnabled(true);
				textSwitchAddPrice.setText("");
				textSwitchAddModel.setText("");
				textSwitchAddDescription.setText("");
				loadSwitchTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperSwitchDescription.remove(panelSwitchAddNew);
						panelWrapperSwitchDescription.validate();
						panelWrapperSwitchDescription.repaint();
						panelSwitchAddNew.setVisible(false);
						
						panelSwitchDescription.setVisible(true);
						panelWrapperSwitchDescription.add(panelSwitchDescription);
						panelWrapperSwitchDescription.validate();
						panelWrapperSwitchDescription.repaint();
						
						switchesFrame.validate();
						switchesFrame.repaint();
					}
				});
			} else if (panelSwitchEdit.isVisible()) {
				buttonSwitchAdd.setEnabled(true);
				loadSwitchTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperSwitchDescription.remove(panelSwitchEdit);
						panelWrapperSwitchDescription.validate();
						panelWrapperSwitchDescription.repaint();
						panelSwitchEdit.setVisible(false);
						
						panelSwitchDescription.setVisible(true);
						panelWrapperSwitchDescription.add(panelSwitchDescription);
						panelWrapperSwitchDescription.validate();
						panelWrapperSwitchDescription.repaint();
						
						switchesFrame.validate();
						switchesFrame.repaint();
					}
				});
			}
		} else if(mode == MainView.ADD_MODE) {
			buttonSwitchAdd.setEnabled(false);
			buttonSwitchEdit.setEnabled(false);
			textSwitchPrice.setText("");
			textSwitchPhases.setText("");
			textSwitchCurrent.setText("");
			textSwitchType.setText("");
			textSwitchBrand.setText("");
			textSwitchDescription.setText("");
			tableSwitchesResult.clearSelection();
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperSwitchDescription.remove(panelSwitchDescription);
					panelWrapperSwitchDescription.validate();
					panelWrapperSwitchDescription.repaint();
					panelSwitchDescription.setVisible(false);
					
					panelSwitchAddNew.setVisible(true);
					panelWrapperSwitchDescription.add(panelSwitchAddNew);
					panelWrapperSwitchDescription.validate();
					panelWrapperSwitchDescription.repaint();
					
					switchesFrame.validate();
					switchesFrame.repaint();
				}
			});
		} else if(mode == MainView.EDIT_MODE) {
			buttonSwitchAdd.setEnabled(false);
			buttonSwitchEdit.setEnabled(false);
			// save everything to variables before going to edit form and after editing return the new values to
			// table and to description form
			editSwitchId = Integer.valueOf(String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_ID_COLUMN)));
			editSwitchPrice = Double.valueOf(String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_PRICE_COLUMN)));
			editSwitchPhases = String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_PHASES_COLUMN));
			editSwitchCurrent = Integer.valueOf(String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_CURRENT_COLUMN)));
			editSwitchBrand = String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_BRAND_COLUMN));
			editSwitchType = String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_TYPE_COLUMN));
			editSwitchInterruption = Integer.valueOf(String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_INTERRUPTION_COLUMN)));
			editSwitchModel = String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_MODEL_COLUMN));
			editSwitchVoltage = String.valueOf(tableSwitchesResult.getValueAt(switchTableSelectedIndex, SharedListSelectionListener.SWITCH_VOLTAGE_COLUMN));
			
			String queryCurrents = "SELECT current "
					+ "FROM currents "
					+ "GROUP BY currents.current";
			
			String queryBrands = "SELECT switch_brands.brand "
					+ "FROM switch_brands "
					+ "GROUP BY switch_brands.brand";
			
			String queryInterruptions = "SELECT interruption "
					+ "FROM interruptions "
					+ "GROUP BY interruptions.interruption";
			
			String queryVoltages = "SELECT voltage "
					+ "FROM switch_voltages "
					+ "GROUP BY switch_voltages.voltage";
			
			textSwitchEditPrice.setText(String.valueOf(editSwitchPrice));
			
			List<String> listPhases = new ArrayList<String>();
			listPhases.add("1");
			listPhases.add("2");
			listPhases.add("3");
			
			comboSwitchEditPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(listPhases)));
			comboSwitchEditPhases.setSelectedItem(editSwitchPhases);
			
			comboSwitchEditCurrent.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryCurrents, "current"))));
			comboSwitchEditCurrent.removeItem("Todas");
			comboSwitchEditCurrent.setSelectedItem(String.valueOf(editSwitchCurrent));
			
			comboSwitchEditBrand.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryBrands, "brand"))));
			comboSwitchEditBrand.removeItem("Todas");
			comboSwitchEditBrand.setSelectedItem(editSwitchBrand);
			
			String additionalFrom = "";
			String additionalWhere = "";
			if(editSwitchBrand != null && !editSwitchBrand.isEmpty()) {
				additionalFrom = ", switch_brands ";
				additionalWhere = "AND switch_brands.brand = '" + editSwitchBrand + "' ";
			}
			String queryTypes = "SELECT type "
					+ "FROM switch_types "
					+ additionalFrom
					+ "WHERE switch_types.brand_id = switch_brands.id "
					+ additionalWhere
					+ "GROUP BY switch_types.type ";
			
			comboSwitchEditType.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryTypes, "type"))));
			comboSwitchEditType.removeItem("Todas");
			comboSwitchEditType.setSelectedItem(editSwitchType);
			
			comboSwitchEditInterruption.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption"))));
			comboSwitchEditInterruption.removeItem("Todas");
			comboSwitchEditInterruption.setSelectedItem(String.valueOf(editSwitchInterruption));
			
			textSwitchEditModel.setText(editSwitchModel);
			
			comboSwitchEditVoltage.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryVoltages, "voltage"))));
			comboSwitchEditVoltage.removeItem("Todas");
			comboSwitchEditVoltage.setSelectedItem(editSwitchVoltage);
			
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperSwitchDescription.remove(panelSwitchDescription);
					panelWrapperSwitchDescription.validate();
					panelWrapperSwitchDescription.repaint();
					panelSwitchDescription.setVisible(false);
					
					panelSwitchEdit.setVisible(true);
					panelWrapperSwitchDescription.add(panelSwitchEdit);
					panelWrapperSwitchDescription.validate();
					panelWrapperSwitchDescription.repaint();
					
					switchesFrame.validate();
					switchesFrame.repaint();
				}
			});
			
			this.updateSwitchTextEditDescription();
		}
		
	}
	
	private void loadSwitchTable(String whereQuery) {
		String switchesQuery = "SELECT switches.id, "
				+ "switches.model, "
				+ "switch_brands.brand, "
				+ "switch_types.type, "
				+ "switches.phases, "
				+ "currents.current, "
				+ "interruptions.interruption, "
				+ "switch_voltages.voltage, "
				+ "switches.price "
			+ "FROM switches, "
				+ "switch_brands, "
				+ "switch_types, "
				+ "currents, "
				+ "interruptions, "
				+ "switch_voltages "
			+ "WHERE switches.brand_id = switch_brands.id "
			+ "AND switches.type_id = switch_types.id "
			+ "AND switches.current_id = currents.id "
			+ "AND switches.interruption_id = interruptions.id "
			+ "AND switches.voltage_id = switch_voltages.id "
			+ "AND switches.active = '1' "
			+ whereQuery;

		switchesData = db.fetchAll(db.select(switchesQuery));
		
		String[] switchesColumnNames = { "Id", "Modelo", "Marca", "Tipo", "Fases", "Amperaje", "Interrupcion", "Voltaje", "Precio"};
		
		if(switchesData.length > 0) {
			tableSwitchesResult.setModel(new MyTableModel(switchesData, switchesColumnNames));
		} else {
			tableSwitchesResult.setModel(new DefaultTableModel());
		}
	}
	
	private void updateSwitchTextAddDescription() {
		textSwitchAddDescription.setText("Interruptor " +
				((null != comboSwitchAddPhases.getSelectedItem())?comboSwitchAddPhases.getSelectedItem().toString():"") +
				"X" +
				((null != comboSwitchAddCurrents.getSelectedItem())?comboSwitchAddCurrents.getSelectedItem().toString():"") +
				" A, " +
				((null != comboSwitchAddTypes.getSelectedItem())?comboSwitchAddTypes.getSelectedItem().toString():"") +
				", " +
				((null != comboSwitchAddBrands.getSelectedItem())?comboSwitchAddBrands.getSelectedItem().toString():"")
				);
	}
	
	private void updateSwitchTextEditDescription() {
		if(comboSwitchEditPhases.getSelectedIndex() > -1 && comboSwitchEditType.getSelectedIndex() > -1 && comboSwitchEditBrand.getSelectedIndex() > -1) {
			textSwitchEditDescription.setText("Interruptor " +
					((null != comboSwitchEditPhases.getSelectedItem())?comboSwitchEditPhases.getSelectedItem().toString():"") +
					"X" +
					((null != comboSwitchEditCurrent.getSelectedItem())?comboSwitchEditCurrent.getSelectedItem().toString():"") +
					" A, " +
					((null != comboSwitchEditType.getSelectedItem())?comboSwitchEditType.getSelectedItem().toString():"") +
					", " +
					((null != comboSwitchEditBrand.getSelectedItem())?comboSwitchEditBrand.getSelectedItem().toString():"")
					);
		}
	}
	
	private void updateSwitchComboSettings() {
		String queryBrands = "SELECT brand FROM switch_brands";
		String queryTypes = "SELECT type FROM switch_types";
		String queryCurrents = "SELECT current FROM currents";
		String queryVoltages = "SELECT voltage FROM switch_voltages";
		String queryInterruptions = "SELECT interruption FROM interruptions";
		
		comboSwitchSettingsBrands.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryBrands, "brand"))));
		comboSwitchSettingsBrands.removeItem("Todas");
		comboSwitchSettingsTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryTypes, "type"))));
		comboSwitchSettingsTypes.removeItem("Todas");
		comboSwitchSettingsCurrents.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryCurrents, "current"))));
		comboSwitchSettingsCurrents.removeItem("Todas");
		comboSwitchSettingsVoltages.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryVoltages, "voltage"))));
		comboSwitchSettingsVoltages.removeItem("Todas");
		comboSwitchSettingsInterruptions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption"))));
		comboSwitchSettingsInterruptions.removeItem("Todas");
	}
	
	private JPanel createBoxMainPanel() {
		JPanel boxMainPanel = new JPanel();
		boxMainPanel.setLayout(new BorderLayout(20,20));
		
		boxMainPanel.add(createBoxSearchBarPanel(), BorderLayout.NORTH);
		
		tableBoxScrollPane = new JScrollPane(createBoxTablePanel());
		tableBoxesResult.setFillsViewportHeight(true);
		boxMainPanel.add(tableBoxScrollPane, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		ButtonListener lForButton = new ButtonListener();
		
		buttonBoxAdd = new JButton("Agregar");
		buttonBoxAdd.setActionCommand("box.description.buttons.add");
		buttonBoxAdd.addActionListener(lForButton);
		panelButtons.add(buttonBoxAdd);
		
		buttonBoxEdit = new JButton("Editar");
		buttonBoxEdit.setActionCommand("box.description.buttons.edit");
		buttonBoxEdit.addActionListener(lForButton);
		buttonBoxEdit.setEnabled(false);
		panelButtons.add(buttonBoxEdit);
		
		JPanel panelBoxLower = new JPanel(new BorderLayout(20,20));
		panelBoxDescription = createBoxDescriptionPanel();
		
		panelWrapperBoxDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		panelWrapperBoxDescription.add(panelBoxDescription);
		
		panelBoxAddNew = createBoxAddPanel();
		panelBoxAddNew.setVisible(false);
		
		panelBoxEdit = createBoxEditPanel();
		panelBoxEdit.setVisible(false);
		
		panelBoxLower.add(panelWrapperBoxDescription, BorderLayout.CENTER);
		panelBoxLower.add(panelButtons, BorderLayout.SOUTH);
		
		boxMainPanel.add(panelBoxLower, BorderLayout.SOUTH);
		
		return boxMainPanel;
	}
	
	private JPanel createBoxSettingsPanel() {
		JPanel panelBoxSettings = new JPanel();
		panelBoxSettings.setLayout(new BorderLayout(20, 20));
		
		JPanel subPanelBoxSettings = new JPanel();
		subPanelBoxSettings.setLayout(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0,0,5,5);
		
		String queryColors = "SELECT color FROM box_colors";
		String queryCalibers = "SELECT caliber FROM box_calibers";
		
		JLabel labelColor = new JLabel("Color");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelBoxSettings.add(labelColor, cs);
		
		comboBoxSettingsColors = new JComboBox<String>(new Vector<String>(loadComboList(queryColors, "color")));
		comboBoxSettingsColors.removeItem("Todas");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelBoxSettings.add(comboBoxSettingsColors, cs);
		
		cs.gridx = 2;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelBoxSettings.add(createSettingsPanelAddRemove("box", "colors"), cs);
		
		JLabel labelCaliber = new JLabel("Calibre");
		cs.gridx = 3;
		cs.gridy = 0;
		cs.gridwidth = 1;
		subPanelBoxSettings.add(labelCaliber, cs);
		
		comboBoxSettingsCalibers = new JComboBox<String>(new Vector<String>(loadComboList(queryCalibers, "caliber")));
		comboBoxSettingsCalibers.removeItem("Todas");
		cs.gridx = 3;
		cs.gridy = 1;
		cs.gridwidth = 2;
		subPanelBoxSettings.add(comboBoxSettingsCalibers, cs);
		
		cs.gridx = 5;
		cs.gridy = 1;
		cs.gridwidth = 1;
		subPanelBoxSettings.add(createSettingsPanelAddRemove("box", "calibers"), cs);
		
		panelBoxSettings.add(subPanelBoxSettings, BorderLayout.NORTH);
		
		return panelBoxSettings;
	}
	
	private JPanel createBoxSearchBarPanel() {
		JPanel superSearchBarPanel = new JPanel();
		superSearchBarPanel.setLayout(new BoxLayout(superSearchBarPanel, BoxLayout.PAGE_AXIS));
		JPanel searchBarPanel1 = new JPanel();
		searchBarPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel searchBarPanel2 = new JPanel();
		searchBarPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryTypes = "SELECT " + MainView.BOX_TYPE_FIELD
				+ " FROM " + MainView.BOX_TYPES_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.BOX_TYPES_TABLE + ".id = " + MainView.BOXES_TABLE + ".type_id "
				+ " GROUP BY box_types.type";
		
		String queryInstallations = "SELECT " + MainView.INSTALLATION_FIELD
				+ " FROM " + MainView.INSTALLATIONS_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.INSTALLATIONS_TABLE + ".id = " + MainView.BOXES_TABLE + ".installation_id "
				+ " GROUP BY installations.installation";
		
		String queryNemas = "SELECT " + MainView.NEMA_FIELD
				+ " FROM " + MainView.NEMAS_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.NEMAS_TABLE + ".id = " + MainView.BOXES_TABLE + ".nema_id "
				+ " GROUP BY nemas.nema";
		
		String queryPairs = "SELECT boxes.pairs "
				+ " FROM " + MainView.BOXES_TABLE
				+ " WHERE boxes.pairs > 0 "
				+ " GROUP BY " + MainView.BOXES_TABLE + ".pairs";
		
		String querySheets = "SELECT " + MainView.BOX_SHEET_FIELD
				+ " FROM " + MainView.BOX_SHEETS_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.BOX_SHEETS_TABLE + ".id = " + MainView.BOXES_TABLE + ".sheet_id "
				+ " GROUP BY box_sheets.sheet";
		
		String queryFinishes = "SELECT " + MainView.BOX_FINISH_FIELD
				+ " FROM " + MainView.BOX_FINISHES_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.BOX_FINISHES_TABLE + ".id = " + MainView.BOXES_TABLE + ".finish_id "
				+ " GROUP BY box_finishes.finish";
		
		String queryColors = "SELECT " + MainView.BOX_COLOR_FIELD
				+ " FROM " + MainView.BOX_COLORS_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.BOX_COLORS_TABLE + ".id = " + MainView.BOXES_TABLE + ".color_id "
				+ " GROUP BY box_colors.color";
		
		String queryCalibers = "SELECT " + MainView.BOX_CALIBER_FIELD
				+ " FROM " + MainView.BOX_CALIBERS_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.BOX_CALIBERS_TABLE + ".id = " + MainView.BOXES_TABLE + ".caliber_id "
				+ " GROUP BY box_calibers.caliber";
		
		String queryLockTypes = "SELECT " + MainView.BOX_LOCK_TYPE_FIELD
				+ " FROM " + MainView.LOCK_TYPES_TABLE + "," + MainView.BOXES_TABLE
				+ " WHERE " + MainView.LOCK_TYPES_TABLE + ".id = " + MainView.BOXES_TABLE + ".lock_type_id "
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
		
		searchBarPanel2.add(separator());
		
		JButton searchButton = new JButton("Buscar");
		searchButton.setActionCommand("box.search.bar.button");
		SearchButtonListener lForSearchButton = new SearchButtonListener();
		searchButton.addActionListener(lForSearchButton);
		searchBarPanel2.add(searchButton);
		
		superSearchBarPanel.add(searchBarPanel1);
		superSearchBarPanel.add(searchBarPanel2);
		
		return superSearchBarPanel;
	}
	
	private JPanel createBoxTablePanel() {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(MainView.BOX_ID_FIELD);
		fields.add(MainView.BOX_TYPE_FIELD);
		fields.add(MainView.INSTALLATION_FIELD);
		fields.add(MainView.NEMA_FIELD);
		fields.add(MainView.BOX_PAIRS_FIELD);
		fields.add(MainView.BOX_SHEET_FIELD);
		fields.add(MainView.BOX_FINISH_FIELD);
		fields.add(MainView.BOX_COLOR_FIELD);
		fields.add(MainView.BOX_HEIGHT_FIELD);
		fields.add(MainView.BOX_WIDTH_FIELD);
		fields.add(MainView.BOX_DEPTH_FIELD);
		fields.add(MainView.BOX_UNITS_FIELD);
		fields.add(MainView.BOX_CALIBER_FIELD);
		fields.add(MainView.BOX_LOCK_TYPE_FIELD);
		fields.add(MainView.BOX_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(MainView.BOXES_TABLE);
		tables.add(MainView.BOX_TYPES_TABLE);
		tables.add(MainView.INSTALLATIONS_TABLE);
		tables.add(MainView.NEMAS_TABLE);
		tables.add(MainView.BOX_SHEETS_TABLE);
		tables.add(MainView.BOX_FINISHES_TABLE);
		tables.add(MainView.BOX_COLORS_TABLE);
		tables.add(MainView.BOX_UNITS_TABLE);
		tables.add(MainView.BOX_CALIBERS_TABLE);
		tables.add(MainView.LOCK_TYPES_TABLE);
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boxesQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE boxes.type_id = box_types.id "
						+ "AND ( (boxes.sheet_id > 0 "
							+ "AND boxes.sheet_id = box_sheets.id) "
							+ "OR boxes.sheet_id = 0)"
						+ "AND ( (boxes.finish_id > 0 "
							+ "AND boxes.finish_id = box_finishes.id) "
							+ "OR boxes.finish_id = 0)"
						+ "AND ( (boxes.color_id > 0 "
							+ "AND boxes.color_id = box_colors.id) "
							+ "OR boxes.color_id = 0)"
						+ "AND ( (boxes.units_id > 0 "
							+ "AND boxes.units_id = box_measure_units.id) "
							+ "OR boxes.units_id = 0)"
						+ "AND ( (boxes.caliber_id > 0 "
							+ "AND boxes.caliber_id = box_calibers.id) "
							+ "OR boxes.caliber_id = 0)"
						+ "AND ( (boxes.lock_type_id > 0 "
							+ "AND boxes.lock_type_id = lock_types.id) "
							+ "OR boxes.lock_type_id = 0)"
						+ "AND boxes.installation_id = installations.id "
						+ "AND boxes.nema_id = nemas.id "
						+ "AND boxes.active = '1' "
						+ "GROUP BY boxes.id";
		
		String[] boxesColumnNames = { "Id", "Tipo", "Instalacion", "Nema", "Pares Tel.", "Lamina", "Acabado", "Color", "Alto", "Ancho", "Profundidad", "Und.", "Calibre", "Cerradura", "Precio"};
//		ArrayList<String> listBoxesColumnNames = new ArrayList<String>(Arrays.asList(originalColumns));
//		listBoxesColumnNames.add("Button");
//		String [] boxesColumnNames = listBoxesColumnNames.toArray(new String[0]);
		// The line below is when not adding a Button column at the end of the table
		boxesData = db.fetchAll(db.select(boxesQuery));
		tableBoxesResult = new JTable();
//		boxesData = db.fetchAllAddButton(db.select(boxesQuery), boxesColumnNames.length);
		
		MyTableModel mForTable = new MyTableModel(boxesData, boxesColumnNames);
		
		tableBoxesResult.setModel(mForTable);
		tableBoxesResult.setAutoCreateRowSorter(true);
		tableBoxesResult.getTableHeader().setReorderingAllowed(false);
		
		listBoxSelectionModel = tableBoxesResult.getSelectionModel();
		listBoxSelectionModel.addListSelectionListener(new SharedListSelectionListener());
		tableBoxesResult.setSelectionModel(listBoxSelectionModel);
		tableBoxesResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
//		TableColumn buttonColumn = tableBoxesResult.getColumnModel().getColumn(boxesColumnNames.length-1);
//		TableButton buttons = new TableButton();
//		buttons.addHandler(new TableButtonPressedHandler() {
//			
//			@Override
//			public void onButtonPress(int row, int column) {
//				int viewRow = tableBoxesResult.getSelectedRow();
//				Object tableId = tableBoxesResult.getValueAt(viewRow, 0);
//				JOptionPane.showMessageDialog(null, "Row:" + row + "-Column:" + column + "-ViewRow:" + viewRow + "-TableId:" + tableId);
//			}
//		});
//		
//		buttonColumn.setHeaderValue("Botones");
//		buttonColumn.setCellRenderer(buttons);
//		buttonColumn.setCellEditor(buttons);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableBoxesResult.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(tableBoxesResult, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private JPanel createBoxDescriptionPanel() {
		JPanel descriptionPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelType, cs);
		
		textBoxDescriptionType = new JTextField("", 10);
		textBoxDescriptionType.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 10;
		descriptionPanel.add(textBoxDescriptionType, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 11;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelInstallation, cs);
		
		textBoxDescriptionInstallation = new JTextField("", 8);
		textBoxDescriptionInstallation.setEditable(false);
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionInstallation, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 20;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelNema, cs);
		
		textBoxDescriptionNema = new JTextField("", 4);
		textBoxDescriptionNema.setEditable(false);
		cs.gridx = 21;
		cs.gridy = 0;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionNema, cs);
		
		JLabel labelPairs = new JLabel("Pares Tel.:");
		cs.gridx = 25;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPairs, cs);
		
		textBoxDescriptionPairs = new JTextField("", 4);
		textBoxDescriptionPairs.setEditable(false);
		cs.gridx = 26;
		cs.gridy = 0;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionPairs, cs);
		
		JLabel labelSheet = new JLabel("Lamina:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelSheet, cs);
		
		textBoxDescriptionSheet = new JTextField("", 8);
		textBoxDescriptionSheet.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionSheet, cs);
		
		JLabel labelFinish = new JLabel("Acabado:");
		cs.gridx = 9;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelFinish, cs);
		
		textBoxDescriptionFinish = new JTextField("", 8);
		textBoxDescriptionFinish.setEditable(false);
		cs.gridx = 10;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionFinish, cs);
		
		JLabel labelColor = new JLabel("Color:");
		cs.gridx = 18;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelColor, cs);
		
		textBoxDescriptionColor = new JTextField("", 8);
		textBoxDescriptionColor.setEditable(false);
		cs.gridx = 19;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionColor, cs);
		
		JLabel labelHeight = new JLabel("Alto:");
		cs.gridx = 27;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelHeight, cs);
		
		textBoxDescriptionHeight = new JTextField("", 4);
		textBoxDescriptionHeight.setEditable(false);
		cs.gridx = 28;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionHeight, cs);
		
		JLabel labelWidth = new JLabel("Ancho:");
		cs.gridx = 32;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelWidth, cs);
		
		textBoxDescriptionWidth = new JTextField("", 4);
		textBoxDescriptionWidth.setEditable(false);
		cs.gridx = 33;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionWidth, cs);
		
		JLabel labelDepth = new JLabel("Profundidad:");
		cs.gridx = 37;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelDepth, cs);
		
		textBoxDescriptionDepth = new JTextField("", 4);
		textBoxDescriptionDepth.setEditable(false);
		cs.gridx = 38;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionDepth, cs);
		
		JLabel labelUnits = new JLabel("Unidades:");
		cs.gridx = 42;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelUnits, cs);
		
		textBoxDescriptionUnits = new JTextField("", 4);
		textBoxDescriptionUnits.setEditable(false);
		cs.gridx = 43;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionUnits, cs);
		
		JLabel labelCaliber = new JLabel("Calibre:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		descriptionPanel.add(labelCaliber, cs);
		
		textBoxDescriptionCaliber = new JTextField("", 4);
		textBoxDescriptionCaliber.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoxDescriptionCaliber, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 5;
		cs.gridy = 2;
		cs.gridwidth = 1;
		descriptionPanel.add(labelLockType, cs);
		
		textBoxDescriptionLockType = new JTextField("", 8);
		textBoxDescriptionLockType.setEditable(false);
		cs.gridx = 6;
		cs.gridy = 2;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionLockType, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 14;
		cs.gridy = 2;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPrice, cs);
		
		textBoxDescriptionPrice = new JTextField("", 8);
		textBoxDescriptionPrice.setEditable(false);
		cs.gridx = 15;
		cs.gridy = 2;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoxDescriptionPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		descriptionPanel.add(labelDescription, cs);
		
		textBoxDescription = new JTextArea(1, 26);
		textBoxDescription.setEditable(false);
		textBoxDescription.setLineWrap(true);
		textBoxDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 26;
		descriptionPanel.add(textBoxDescription, cs);
		
		SearchButtonListener lForButton = new SearchButtonListener();
		
		JButton buttonCopy = new JButton("Copiar");
		buttonCopy.addActionListener(lForButton);
		buttonCopy.setActionCommand("box.description.copy");
		cs.gridx = 0;
		cs.gridy = 5;
		cs.gridwidth = 4;
		descriptionPanel.add(buttonCopy, cs);
		
		labelBoxCopy = new JLabel("");
		cs.gridx = 4;
		cs.gridy = 5;
		cs.gridwidth = 12;
		descriptionPanel.add(labelBoxCopy, cs);
		
		return descriptionPanel;
	}
	
	private JPanel createBoxAddPanel() {
		JPanel addPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		TextFieldListener lForText = new TextFieldListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		String queryTypes = "SELECT box_types.type "
				+ "FROM box_types "
				+ "GROUP BY box_types.type";
		
		String queryInstallations = "SELECT installation "
				+ "FROM installations "
				+ "GROUP BY installations.installation";
		
		String queryNemas = "SELECT nemas.nema "
				+ "FROM nemas "
				+ "GROUP BY nemas.nema";
		
		String querySheets = "SELECT box_sheets.sheet "
				+ "FROM box_sheets "
				+ "GROUP BY box_sheets.sheet";
		
		String queryFinishes = "SELECT box_finishes.finish "
				+ "FROM box_finishes "
				+ "GROUP BY box_finishes.finish";
		
		String queryColors = "SELECT box_colors.color "
				+ "FROM box_colors "
				+ "GROUP BY box_colors.color";
		
		String queryUnits = "SELECT box_measure_units.units "
				+ "FROM box_measure_units "
				+ "GROUP BY box_measure_units.units";
		
		String queryCalibers = "SELECT box_calibers.caliber "
				+ "FROM box_calibers "
				+ "GROUP BY box_calibers.caliber";
		
		String queryLockTypes = "SELECT lock_types.lock_type "
				+ "FROM lock_types "
				+ "GROUP BY lock_types.lock_type";
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelType, cs);
		
		comboBoxAddTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoxAddTypes.removeItem("Todas");
		comboBoxAddTypes.setActionCommand("box.description.add.type");
		comboBoxAddTypes.addActionListener(lForCombo);
		addSelectedBoxType = comboBoxAddTypes.getSelectedItem().toString();
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddTypes, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 7;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelInstallation, cs);
		
		comboBoxAddInstallations = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoxAddInstallations.removeItem("Todas");
		cs.gridx = 8;
		cs.gridy = 0;
		cs.gridwidth = 2;
		addPanel.add(comboBoxAddInstallations, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 10;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelNema, cs);
		
		comboBoxAddNemas = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoxAddNemas.removeItem("Todas");
		comboBoxAddNemas.setActionCommand("box.description.add.nema");
		comboBoxAddNemas.addActionListener(lForCombo);
		cs.gridx = 11;
		cs.gridy = 0;
		cs.gridwidth = 8;
		addPanel.add(comboBoxAddNemas, cs);
		
		JLabel labelPairs = new JLabel("Pares Telefonicos:");
		cs.gridx = 19;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelPairs, cs);
		
		textBoxAddPairs = new JTextField(4);
		if(!addSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
			textBoxAddPairs.setEnabled(false);
		} else {
			textBoxAddPairs.setEnabled(true);
		}
		cs.gridx = 20;
		cs.gridy = 0;
		cs.gridwidth = 4;
		addPanel.add(textBoxAddPairs, cs);
		
		JLabel labelSheet = new JLabel("Lamina:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelSheet, cs);
		
		comboBoxAddSheets = new JComboBox<String>(new Vector<String>(loadComboList(querySheets, "sheet")));
		comboBoxAddSheets.removeItem("Todas");
		comboBoxAddSheets.setActionCommand("box.description.add.sheet");
		comboBoxAddSheets.addActionListener(lForCombo);
		addSelectedBoxSheet = comboBoxAddSheets.getSelectedItem().toString();
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddSheets, cs);
		
		JLabel labelFinish = new JLabel("Acabado:");
		cs.gridx = 7;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelFinish, cs);
		
		comboBoxAddFinishes = new JComboBox<String>(new Vector<String>(loadComboList(queryFinishes, "finish")));
		comboBoxAddFinishes.removeItem("Todas");
		if(!addSelectedBoxSheet.equalsIgnoreCase("HNF")) {
			comboBoxAddFinishes.setEnabled(false);
		} else {
			comboBoxAddFinishes.setEnabled(true);
		}
		cs.gridx = 8;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddFinishes, cs);
		
		JLabel labelColor = new JLabel("Color:");
		cs.gridx = 14;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelColor, cs);
		
		comboBoxAddColors = new JComboBox<String>(new Vector<String>(loadComboList(queryColors, "color")));
		comboBoxAddColors.removeItem("Todas");
		if(!addSelectedBoxSheet.equalsIgnoreCase("HNF")) {
			comboBoxAddColors.setEnabled(false);
		} else {
			comboBoxAddColors.setEnabled(true);
		}
		cs.gridx = 15;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddColors, cs);
		
		JLabel labelHeight = new JLabel("Alto:");
		cs.gridx = 21;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelHeight, cs);
		
		textBoxAddHeight = new JTextField(4);
		textBoxAddHeight.addKeyListener(lForText);;
		cs.gridx = 22;
		cs.gridy = 1;
		cs.gridwidth = 4;
		addPanel.add(textBoxAddHeight, cs);
		
		JLabel labelWidth = new JLabel("Ancho:");
		cs.gridx = 26;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelWidth, cs);
		
		textBoxAddWidth = new JTextField(4);
		textBoxAddWidth.addKeyListener(lForText);
		cs.gridx = 27;
		cs.gridy = 1;
		cs.gridwidth = 4;
		addPanel.add(textBoxAddWidth, cs);
		
		JLabel labelDepth = new JLabel("Profundidad:");
		cs.gridx = 31;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelDepth, cs);
		
		textBoxAddDepth = new JTextField(4);
		textBoxAddDepth.addKeyListener(lForText);
		cs.gridx = 32;
		cs.gridy = 1;
		cs.gridwidth = 4;
		addPanel.add(textBoxAddDepth, cs);
		
		JLabel labelUnits = new JLabel("Unidades:");
		cs.gridx = 36;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelUnits, cs);
		
		comboBoxAddUnits = new JComboBox<String>(new Vector<String>(loadComboList(queryUnits, "units")));
		comboBoxAddUnits.removeItem("Todas");
		cs.gridx = 37;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddUnits, cs);
		
		JLabel labelCaliber = new JLabel("Calibre:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelCaliber, cs);
		
		comboBoxAddCalibers = new JComboBox<String>(new Vector<String>(loadComboList(queryCalibers, "caliber")));
		comboBoxAddCalibers.removeItem("Todas");
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddCalibers, cs);
		
		JLabel labelCaliberComment = new JLabel("Comentarios de Calibre:");
		cs.gridx = 7;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelCaliberComment, cs);
		
		textBoxAddCaliberComments = new JTextField(10);
		cs.gridx = 8;
		cs.gridy = 2;
		cs.gridwidth = 10;
		addPanel.add(textBoxAddCaliberComments, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 18;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelLockType, cs);
		
		comboBoxAddLockTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoxAddLockTypes.removeItem("Todas");
		comboBoxAddLockTypes.setActionCommand("box.description.add.lock_type");
		comboBoxAddLockTypes.addActionListener(lForCombo);
		if(!addSelectedBoxType.equalsIgnoreCase("de paso")) {
			comboBoxAddLockTypes.setEnabled(true);
		} else {
			comboBoxAddLockTypes.setEnabled(false);
		}
		cs.gridx = 19;
		cs.gridy = 2;
		cs.gridwidth = 6;
		addPanel.add(comboBoxAddLockTypes, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 25;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelPrice, cs);
		
		textBoxAddPrice = new JTextField(6);
		cs.gridx = 26;
		cs.gridy = 2;
		cs.gridwidth = 6;
		addPanel.add(textBoxAddPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		addPanel.add(labelDescription, cs);
		
		textBoxAddDescription = new JTextArea(1,16);
		textBoxAddDescription.setEditable(false);
		textBoxAddDescription.setLineWrap(true);
		textBoxAddDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 16;
		addPanel.add(textBoxAddDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		buttonBoxAddSave = new JButton("Guardar");
		buttonBoxAddSave.setActionCommand("box.description.add.save");
		buttonBoxAddSave.addActionListener(lForButton);
		panelButtons.add(buttonBoxAddSave);
		
		buttonBoxAddCancel = new JButton("Cancelar");
		buttonBoxAddCancel.setActionCommand("box.description.add.cancel");
		buttonBoxAddCancel.addActionListener(lForButton);
		panelButtons.add(buttonBoxAddCancel);
		
		cs.gridx = 0;
		cs.gridy = 5;
		cs.gridwidth = 43;
		addPanel.add(panelButtons, cs);
		
		updateBoxTextAddDescription();
		
		return addPanel;
	}
	
	private JPanel createBoxEditPanel() {
		JPanel editPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		TextFieldListener lForText = new TextFieldListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		String queryTypes = "SELECT box_types.type "
				+ "FROM box_types "
				+ "GROUP BY box_types.type";
		
		String queryInstallations = "SELECT installation "
				+ "FROM installations "
				+ "GROUP BY installations.installation";
		
		String queryNemas = "SELECT nemas.nema "
				+ "FROM nemas "
				+ "GROUP BY nemas.nema";
		
		String querySheets = "SELECT box_sheets.sheet "
				+ "FROM box_sheets "
				+ "GROUP BY box_sheets.sheet";
		
		String queryFinishes = "SELECT box_finishes.finish "
				+ "FROM box_finishes "
				+ "GROUP BY box_finishes.finish";
		
		String queryColors = "SELECT box_colors.color "
				+ "FROM box_colors "
				+ "GROUP BY box_colors.color";
		
		String queryUnits = "SELECT box_measure_units.units "
				+ "FROM box_measure_units "
				+ "GROUP BY box_measure_units.units";
		
		String queryCalibers = "SELECT box_calibers.caliber "
				+ "FROM box_calibers "
				+ "GROUP BY box_calibers.caliber";
		
		String queryLockTypes = "SELECT lock_types.lock_type "
				+ "FROM lock_types "
				+ "GROUP BY lock_types.lock_type";
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelType, cs);
		
		comboBoxEditTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoxEditTypes.removeItem("Todas");
		comboBoxEditTypes.setActionCommand("box.description.edit.type");
		comboBoxEditTypes.addActionListener(lForCombo);
		editBoxType = comboBoxEditTypes.getSelectedItem().toString();
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditTypes, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 7;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelInstallation, cs);
		
		comboBoxEditInstallations = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoxEditInstallations.removeItem("Todas");
		cs.gridx = 8;
		cs.gridy = 0;
		cs.gridwidth = 2;
		editPanel.add(comboBoxEditInstallations, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 10;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelNema, cs);
		
		comboBoxEditNemas = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoxEditNemas.removeItem("Todas");
		cs.gridx = 11;
		cs.gridy = 0;
		cs.gridwidth = 8;
		editPanel.add(comboBoxEditNemas, cs);
		
		JLabel labelPairs = new JLabel("Pares Telefonicos:");
		cs.gridx = 19;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelPairs, cs);
		
		textBoxEditPairs = new JTextField(4);
		if(!editBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
			textBoxEditPairs.setEnabled(false);
		} else {
			textBoxEditPairs.setEnabled(true);
		}
		cs.gridx = 20;
		cs.gridy = 0;
		cs.gridwidth = 4;
		editPanel.add(textBoxEditPairs, cs);
		
		JLabel labelSheet = new JLabel("Lamina:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelSheet, cs);
		
		comboBoxEditSheets = new JComboBox<String>(new Vector<String>(loadComboList(querySheets, "sheet")));
		comboBoxEditSheets.removeItem("Todas");
		comboBoxEditSheets.setActionCommand("box.description.edit.sheet");
		comboBoxEditSheets.addActionListener(lForCombo);
		editBoxSheet = comboBoxEditSheets.getSelectedItem().toString();
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditSheets, cs);
		
		JLabel labelFinish = new JLabel("Acabado:");
		cs.gridx = 7;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelFinish, cs);
		
		comboBoxEditFinishes = new JComboBox<String>(new Vector<String>(loadComboList(queryFinishes, "finish")));
		comboBoxEditFinishes.removeItem("Todas");
		if(!editBoxSheet.equalsIgnoreCase("HNF")) {
			comboBoxEditFinishes.setEnabled(false);
		} else {
			comboBoxEditFinishes.setEnabled(true);
		}
		cs.gridx = 8;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditFinishes, cs);
		
		JLabel labelColor = new JLabel("Color:");
		cs.gridx = 14;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelColor, cs);
		
		comboBoxEditColors = new JComboBox<String>(new Vector<String>(loadComboList(queryColors, "color")));
		comboBoxEditColors.removeItem("Todas");
		if(!editBoxSheet.equalsIgnoreCase("HNF")) {
			comboBoxEditColors.setEnabled(false);
		} else {
			comboBoxEditColors.setEnabled(true);
		}
		cs.gridx = 15;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditColors, cs);
		
		JLabel labelHeight = new JLabel("Alto:");
		cs.gridx = 21;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelHeight, cs);
		
		textBoxEditHeight = new JTextField(4);
		textBoxEditHeight.addKeyListener(lForText);;
		cs.gridx = 22;
		cs.gridy = 1;
		cs.gridwidth = 4;
		editPanel.add(textBoxEditHeight, cs);
		
		JLabel labelWidth = new JLabel("Ancho:");
		cs.gridx = 26;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelWidth, cs);
		
		textBoxEditWidth = new JTextField(4);
		textBoxEditWidth.addKeyListener(lForText);
		cs.gridx = 27;
		cs.gridy = 1;
		cs.gridwidth = 4;
		editPanel.add(textBoxEditWidth, cs);
		
		JLabel labelDepth = new JLabel("Profundidad:");
		cs.gridx = 31;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelDepth, cs);
		
		textBoxEditDepth = new JTextField(4);
		textBoxEditDepth.addKeyListener(lForText);
		cs.gridx = 32;
		cs.gridy = 1;
		cs.gridwidth = 4;
		editPanel.add(textBoxEditDepth, cs);
		
		JLabel labelUnits = new JLabel("Unidades:");
		cs.gridx = 36;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelUnits, cs);
		
		comboBoxEditUnits = new JComboBox<String>(new Vector<String>(loadComboList(queryUnits, "units")));
		comboBoxEditUnits.removeItem("Todas");
		cs.gridx = 37;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditUnits, cs);
		
		JLabel labelCaliber = new JLabel("Calibre:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelCaliber, cs);
		
		comboBoxEditCalibers = new JComboBox<String>(new Vector<String>(loadComboList(queryCalibers, "caliber")));
		comboBoxEditCalibers.removeItem("Todas");
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditCalibers, cs);
		
		JLabel labelCaliberComment = new JLabel("Comentarios de Calibre:");
		cs.gridx = 7;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelCaliberComment, cs);
		
		textBoxEditCaliberComments = new JTextField(10);
		cs.gridx = 8;
		cs.gridy = 2;
		cs.gridwidth = 10;
		editPanel.add(textBoxEditCaliberComments, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 18;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelLockType, cs);
		
		comboBoxEditLockTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoxEditLockTypes.removeItem("Todas");
		if(!editBoxType.equalsIgnoreCase("de paso")) {
			comboBoxEditLockTypes.setEnabled(true);
		} else {
			comboBoxEditLockTypes.setEnabled(false);
		}
		cs.gridx = 19;
		cs.gridy = 2;
		cs.gridwidth = 6;
		editPanel.add(comboBoxEditLockTypes, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 25;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelPrice, cs);
		
		textBoxEditPrice = new JTextField(6);
		cs.gridx = 26;
		cs.gridy = 2;
		cs.gridwidth = 6;
		editPanel.add(textBoxEditPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		editPanel.add(labelDescription, cs);
		
		textBoxEditDescription = new JTextArea(1,16);
		textBoxEditDescription.setEditable(false);
		textBoxEditDescription.setLineWrap(true);
		textBoxEditDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 16;
		editPanel.add(textBoxEditDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		buttonBoxEditSave = new JButton("Guardar");
		buttonBoxEditSave.setActionCommand("box.description.edit.save");
		buttonBoxEditSave.addActionListener(lForButton);
		panelButtons.add(buttonBoxEditSave);
		
		buttonBoxEditCancel = new JButton("Cancelar");
		buttonBoxEditCancel.setActionCommand("box.description.edit.cancel");
		buttonBoxEditCancel.addActionListener(lForButton);
		panelButtons.add(buttonBoxEditCancel);
		
		cs.gridx = 0;
		cs.gridy = 5;
		cs.gridwidth = 43;
		editPanel.add(panelButtons, cs);
		
		updateBoxTextEditDescription();
		
		return editPanel;
	}
	
	private void setBoxesMode(int mode) {
		
		if(mode == MainView.VIEW_MODE) {
			if(panelBoxAddNew.isVisible()) {
				buttonBoxAdd.setEnabled(true);
				textBoxAddPairs.setText("");
				textBoxAddHeight.setText("");
				textBoxAddWidth.setText("");
				textBoxAddDepth.setText("");
				textBoxAddCaliberComments.setText("");
				textBoxAddPrice.setText("");
				loadBoxTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperBoxDescription.remove(panelBoxAddNew);
						panelWrapperBoxDescription.validate();
						panelWrapperBoxDescription.repaint();
						panelBoxAddNew.setVisible(false);
						
						panelBoxDescription.setVisible(true);
						panelWrapperBoxDescription.add(panelBoxDescription);
						panelWrapperBoxDescription.validate();
						panelWrapperBoxDescription.repaint();
						
						boxesFrame.validate();
						boxesFrame.repaint();
					}
				});
			} else if (panelBoxEdit.isVisible()) {
				buttonBoxAdd.setEnabled(true);
				loadBoxTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperBoxDescription.remove(panelBoxEdit);
						panelWrapperBoxDescription.validate();
						panelWrapperBoxDescription.repaint();
						panelBoxEdit.setVisible(false);
						
						panelBoxDescription.setVisible(true);
						panelWrapperBoxDescription.add(panelBoxDescription);
						panelWrapperBoxDescription.validate();
						panelWrapperBoxDescription.repaint();
						
						boxesFrame.validate();
						boxesFrame.repaint();
					}
				});
			}
		} else if(mode == MainView.ADD_MODE) {
			buttonBoxAdd.setEnabled(false);
			buttonBoxEdit.setEnabled(false);
			textBoxDescriptionPairs.setText("");
			textBoxDescriptionPrice.setText("");
			textBoxAddPairs.setText("");
			textBoxAddPrice.setText("");
			tableBoxesResult.clearSelection();
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperBoxDescription.remove(panelBoxDescription);
					panelWrapperBoxDescription.validate();
					panelWrapperBoxDescription.repaint();
					panelBoxDescription.setVisible(false);
					
					panelBoxAddNew.setVisible(true);
					panelWrapperBoxDescription.add(panelBoxAddNew);
					panelWrapperBoxDescription.validate();
					panelWrapperBoxDescription.repaint();
					
					boxesFrame.validate();
					boxesFrame.repaint();
				}
			});
		} else if(mode == MainView.EDIT_MODE) {
			buttonBoxAdd.setEnabled(false);
			buttonBoxEdit.setEnabled(false);
			editBoxId = Integer.valueOf(String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_ID_COLUMN)));
			editBoxType = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_TYPE_COLUMN));
			editBoxInstallation = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_INSTALLATION_COLUMN));
			editBoxNema = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_NEMA_COLUMN));
			String boxPairs = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_PAIRS_COLUMN));
			editBoxPairs = (Numbers.isNumeric(boxPairs))?Integer.valueOf(boxPairs):0;
			editBoxSheet = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_SHEET_COLUMN));
			editBoxFinish = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_FINISH_COLUMN));
			editBoxColor = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_COLOR_COLUMN));
			editBoxHeight = Integer.valueOf(String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_HEIGHT_COLUMN)));
			editBoxWidth = Integer.valueOf(String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_WIDTH_COLUMN)));
			editBoxDepth = Integer.valueOf(String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_DEPTH_COLUMN)));
			editBoxUnits = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_UNITS_COLUMN));
			editBoxCaliber = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_CALIBER_COLUMN));
			editBoxCaliberComments = String.valueOf(db.getBoxCaliberComments(editBoxId));
			editBoxLockType = String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_LOCK_TYPE_COLUMN));
			editBoxPrice = Double.valueOf(String.valueOf(tableBoxesResult.getValueAt(boxTableSelectedIndex, SharedListSelectionListener.BOX_PRICE_COLUMN)));
			
			String queryTypes = "SELECT box_types.type "
					+ "FROM box_types "
					+ "GROUP BY box_types.type";
			
			String queryInstallations = "SELECT installation "
					+ "FROM installations "
					+ "GROUP BY installations.installation";
			
			String queryNemas = "SELECT nemas.nema "
					+ "FROM nemas "
					+ "GROUP BY nemas.nema";
			
			String querySheets = "SELECT box_sheets.sheet "
					+ "FROM box_sheets "
					+ "GROUP BY box_sheets.sheet";
			
			String queryFinishes = "SELECT box_finishes.finish "
					+ "FROM box_finishes "
					+ "GROUP BY box_finishes.finish";
			
			String queryColors = "SELECT box_colors.color "
					+ "FROM box_colors "
					+ "GROUP BY box_colors.color";
			
			String queryUnits = "SELECT box_measure_units.units "
					+ "FROM box_measure_units "
					+ "GROUP BY box_measure_units.units";
			
			String queryCalibers = "SELECT box_calibers.caliber "
					+ "FROM box_calibers "
					+ "GROUP BY box_calibers.caliber";
			
			String queryLockTypes = "SELECT lock_types.lock_type "
					+ "FROM lock_types "
					+ "GROUP BY lock_types.lock_type";
			
			comboBoxEditTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryTypes, "type"))));
			comboBoxEditTypes.removeItem("Todas");
			comboBoxEditTypes.setSelectedItem(editBoxType);
			
			comboBoxEditInstallations.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryInstallations, "installation"))));
			comboBoxEditInstallations.removeItem("Todas");
			comboBoxEditInstallations.setSelectedItem(editBoxInstallation);
			
			comboBoxEditNemas.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryNemas, "nema"))));
			comboBoxEditNemas.removeItem("Todas");
			comboBoxEditNemas.setSelectedItem(editBoxNema);
			
			if(editBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
				textBoxEditPairs.setEnabled(true);
				textBoxEditPairs.setText(String.valueOf(editBoxPairs));
			} else {
				textBoxEditPairs.setText("");
				textBoxEditPairs.setEnabled(false);
			}
			
			comboBoxEditSheets.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(querySheets, "sheet"))));
			comboBoxEditSheets.removeItem("Todas");
			comboBoxEditSheets.setSelectedItem(editBoxSheet);
			
			comboBoxEditFinishes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryFinishes, "finish"))));
			comboBoxEditFinishes.removeItem("Todas");
			comboBoxEditFinishes.setSelectedItem(editBoxFinish);
			
			if(!editBoxColor.equalsIgnoreCase("N/A")) {
				comboBoxEditColors.setEnabled(true);
				comboBoxEditColors.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryColors, "color"))));
				comboBoxEditColors.removeItem("Todas");
				comboBoxEditColors.setSelectedItem(editBoxColor);
			} else {
				comboBoxEditColors.setSelectedIndex(-1);
				comboBoxEditColors.setEnabled(false);
			}
			
			textBoxEditHeight.setText(String.valueOf(editBoxHeight));
			textBoxEditWidth.setText(String.valueOf(editBoxWidth));
			textBoxEditDepth.setText(String.valueOf(editBoxDepth));
			
			comboBoxEditUnits.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryUnits, "units"))));
			comboBoxEditUnits.removeItem("Todas");
			comboBoxEditUnits.setSelectedItem(editBoxUnits);
			
			comboBoxEditCalibers.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryCalibers, "caliber"))));
			comboBoxEditCalibers.removeItem("Todas");
			comboBoxEditCalibers.setSelectedItem(editBoxCaliber);
			
			textBoxEditCaliberComments.setText(String.valueOf(editBoxCaliberComments));
			
			if(!editBoxLockType.equalsIgnoreCase("N/A")) {
				comboBoxEditLockTypes.setEnabled(true);
				comboBoxEditLockTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type"))));
				comboBoxEditLockTypes.removeItem("Todas");
				comboBoxEditLockTypes.setSelectedItem(editBoxLockType);
			} else {
				comboBoxEditLockTypes.setSelectedIndex(-1);
				comboBoxEditLockTypes.setEnabled(false);
			}
			
			textBoxEditPrice.setText(String.valueOf(editBoxPrice));
			
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperBoxDescription.remove(panelBoxDescription);
					panelWrapperBoxDescription.validate();
					panelWrapperBoxDescription.repaint();
					panelBoxDescription.setVisible(false);
					
					panelBoxEdit.setVisible(true);
					panelWrapperBoxDescription.add(panelBoxEdit);
					panelWrapperBoxDescription.validate();
					panelWrapperBoxDescription.repaint();
					
					boxesFrame.validate();
					boxesFrame.repaint();
				}
			});
			this.updateBoxTextEditDescription();
		}
		
	}
	
	private void loadBoxTable(String whereQuery) {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(MainView.BOX_ID_FIELD);
		fields.add(MainView.BOX_TYPE_FIELD);
		fields.add(MainView.INSTALLATION_FIELD);
		fields.add(MainView.NEMA_FIELD);
		fields.add(MainView.BOX_PAIRS_FIELD);
		fields.add(MainView.BOX_SHEET_FIELD);
		fields.add(MainView.BOX_FINISH_FIELD);
		fields.add(MainView.BOX_COLOR_FIELD);
		fields.add(MainView.BOX_HEIGHT_FIELD);
		fields.add(MainView.BOX_WIDTH_FIELD);
		fields.add(MainView.BOX_DEPTH_FIELD);
		fields.add(MainView.BOX_UNITS_FIELD);
		fields.add(MainView.BOX_CALIBER_FIELD);
		fields.add(MainView.BOX_LOCK_TYPE_FIELD);
		fields.add(MainView.BOX_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(MainView.BOXES_TABLE);
		tables.add(MainView.BOX_TYPES_TABLE);
		tables.add(MainView.INSTALLATIONS_TABLE);
		tables.add(MainView.NEMAS_TABLE);
		tables.add(MainView.BOX_SHEETS_TABLE);
		tables.add(MainView.BOX_FINISHES_TABLE);
		tables.add(MainView.BOX_COLORS_TABLE);
		tables.add(MainView.BOX_UNITS_TABLE);
		tables.add(MainView.BOX_CALIBERS_TABLE);
		tables.add(MainView.LOCK_TYPES_TABLE);
		
		if(whereQuery.isEmpty()) {
			whereQuery += " AND ( (boxes.sheet_id > 0 "
							+ " AND boxes.sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id) "
							+ " OR boxes.sheet_id = 0) "
						+ " AND ( (boxes.finish_id > 0 "
							+ " AND boxes.finish_id = " + MainView.BOX_FINISHES_TABLE + ".id) "
							+ " OR boxes.finish_id = 0) "
						+ " AND ( (boxes.color_id > 0 "
							+ " AND boxes.color_id = " + MainView.BOX_COLORS_TABLE + ".id) "
							+ " OR boxes.color_id = 0) "
						+ " AND ( (boxes.units_id > 0 "
							+ " AND boxes.units_id = " + MainView.BOX_UNITS_TABLE + ".id) "
							+ " OR boxes.units_id = 0) "
						+ " AND ( (boxes.caliber_id > 0 "
							+ " AND boxes.caliber_id = " + MainView.BOX_CALIBERS_TABLE + ".id) "
							+ " OR boxes.caliber_id = 0) "
						+ " AND ( (boxes.lock_type_id > 0 "
							+ " AND boxes.lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id) "
							+ " OR boxes.lock_type_id = 0) ";
		}
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boxesQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE boxes.type_id = box_types.id "
						// Fix these conditions to make them disappear when filtering
						
						+ "AND boxes.installation_id = installations.id "
						+ "AND boxes.nema_id = nemas.id "
						+ "AND boxes.active = '1' "
						+ whereQuery
						+ " GROUP BY boxes.id";

		boxesData = db.fetchAll(db.select(boxesQuery));
		
		String[] boxesColumnNames = { "Id", "Tipo", "Instalacion", "Nema", "Pares Tel.", "Lamina", "Acabado", "Color", "Alto", "Ancho", "Profundidad", "Und.", "Calibre", "Cerradura", "Precio"};
		
		if(boxesData.length > 0) {
			tableBoxesResult.setModel(new MyTableModel(boxesData, boxesColumnNames));
		} else {
			tableBoxesResult.setModel(new DefaultTableModel());
		}
	}
	
	private void updateBoxTextAddDescription() {
		String boxType = (null != comboBoxAddTypes.getSelectedItem().toString())?comboBoxAddTypes.getSelectedItem().toString():"";
		String boxHeight =(null != textBoxAddHeight)?textBoxAddHeight.getText():"";
		String boxWidth =(null != textBoxAddWidth)?textBoxAddWidth.getText():"";
		String boxDepth =(null != textBoxAddDepth)?textBoxAddDepth.getText():"";
		String boxNema =(null != comboBoxAddNemas.getSelectedItem().toString())?comboBoxAddNemas.getSelectedItem().toString():"";
		textBoxAddDescription.setText("Caja " +
				boxType +
				", " +
				boxHeight +
				" x " +
				boxWidth +
				" x " +
				boxDepth +
				", " +
				boxNema);
	}
	
	private void updateBoxTextEditDescription() {
		String boxType = (null != comboBoxEditTypes)?comboBoxEditTypes.getSelectedItem().toString():"";
		String boxHeight =(null != textBoxEditHeight)?textBoxEditHeight.getText():"";
		String boxWidth =(null != textBoxEditWidth)?textBoxEditWidth.getText():"";
		String boxDepth =(null != textBoxEditDepth)?textBoxEditDepth.getText():"";
		String boxNema =(null != comboBoxEditNemas.getSelectedItem().toString())?comboBoxEditNemas.getSelectedItem().toString():"";
		textBoxEditDescription.setText("Caja " +
				boxType +
				", " +
				boxHeight +
				" x " +
				boxWidth +
				" x " +
				boxDepth +
				", " +
				boxNema);
	}
	
	private void updateBoxComboSettings() {
		String queryColors = "SELECT color FROM box_colors";
		String queryCalibers = "SELECT caliber FROM box_calibers";
		
		comboBoxSettingsColors.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryColors, "color"))));
		comboBoxSettingsColors.removeItem("Todas");
		comboBoxSettingsCalibers.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryCalibers, "caliber"))));
		comboBoxSettingsCalibers.removeItem("Todas");
	}
	
	private JTabbedPane createBoardMainPanel() {
		JTabbedPane boardViewTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel boardMainPanel = new JPanel();
		boardMainPanel.setLayout(new BorderLayout(20,20));
		
		boardMainPanel.add(createBoardSearchBarPanel(), BorderLayout.NORTH);
		
		tableBoardScrollPane = new JScrollPane(createBoardTablePanel());
		tableBoardsResult.setFillsViewportHeight(true);
		boardMainPanel.add(tableBoardScrollPane, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		ButtonListener lForButton = new ButtonListener();
		
		buttonBoardAdd = new JButton("Agregar");
		buttonBoardAdd.setActionCommand("board.description.buttons.add");
		buttonBoardAdd.addActionListener(lForButton);
		panelButtons.add(buttonBoardAdd);
		
		buttonBoardEdit = new JButton("Editar");
		buttonBoardEdit.setActionCommand("board.description.buttons.edit");
		buttonBoardEdit.addActionListener(lForButton);
		buttonBoardEdit.setEnabled(false);
		panelButtons.add(buttonBoardEdit);
		
		JPanel panelBoardLower = new JPanel(new BorderLayout(20,20));
		panelBoardDescription = createBoardDescriptionPanel();
		
		panelWrapperBoardDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelWrapperBoardDescription.add(panelBoardDescription);
		
		panelBoardAddNew = createBoardAddPanel();
		panelBoardAddNew.setVisible(false);
		
		panelBoardEdit = createBoardEditPanel();
		panelBoardEdit.setVisible(false);
		
		panelBoardLower.add(panelWrapperBoardDescription, BorderLayout.CENTER);
		panelBoardLower.add(panelButtons, BorderLayout.SOUTH);
		
		boardMainPanel.add(panelBoardLower, BorderLayout.SOUTH);
		boardViewTabbedPane.addTab("Buscar", null, boardMainPanel, "Buscar");
		
		boardSwitchesPanel = createBoardSwitchesPanel();
		
		boardViewTabbedPane.addTab("Interruptores", null, boardSwitchesPanel, "Interruptores");
		boardViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
		
		return boardViewTabbedPane;
	}
	
	private JPanel createBoardSearchBarPanel() {
		JPanel superSearchBarPanel = new JPanel();
		superSearchBarPanel.setLayout(new BoxLayout(superSearchBarPanel, BoxLayout.PAGE_AXIS));
		JPanel searchBarPanel1 = new JPanel();
		searchBarPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel searchBarPanel2 = new JPanel();
		searchBarPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
//		String queryNames = "SELECT " + MainView.BOARD_NAME_FIELD
//				+ " FROM " + MainView.BOARD_TABLE
//				+ " GROUP BY " + MainView.BOARD_TABLE + ".name";
		
		String queryTypes = "SELECT " + MainView.BOARD_TYPE_FIELD
				+ " FROM " + MainView.BOARD_TYPES_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.BOARD_TYPES_TABLE + ".id = " + MainView.BOARD_TABLE + ".type_id "
				+ " GROUP BY " + MainView.BOARD_TYPES_TABLE + ".type";
		
		String queryInstallations = "SELECT " + MainView.INSTALLATION_FIELD
				+ " FROM " + MainView.INSTALLATIONS_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.INSTALLATIONS_TABLE + ".id = " + MainView.BOARD_TABLE + ".installation_id "
				+ " GROUP BY " + MainView.INSTALLATIONS_TABLE + ".installation";
		
		String queryNemas = "SELECT " + MainView.NEMA_FIELD
				+ " FROM " + MainView.NEMAS_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.NEMAS_TABLE + ".id = " + MainView.BOARD_TABLE + ".nema_id "
				+ " GROUP BY " + MainView.NEMAS_TABLE + ".nema";
		
		String queryBarCapacities = "SELECT " + MainView.BOARD_BAR_CAPACITY_FIELD
				+ " FROM " + MainView.BOARD_BAR_CAPACITIES_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id = " + MainView.BOARD_TABLE + ".bar_capacity_id "
				+ " GROUP BY " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity";
		
		String queryBarTypes = "SELECT " + MainView.BOARD_BAR_TYPE_FIELD
				+ " FROM " + MainView.BOARD_BAR_TYPES_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.BOARD_BAR_TYPES_TABLE + ".id = " + MainView.BOARD_TABLE + ".bar_type_id "
				+ " GROUP BY " + MainView.BOARD_BAR_TYPES_TABLE + ".bar_type";
		
		String queryCircuits = "SELECT " + MainView.BOARD_CIRCUITS_FIELD
				+ " FROM " + MainView.BOARD_CIRCUITS_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.BOARD_CIRCUITS_TABLE + ".id = " + MainView.BOARD_TABLE + ".circuits_id "
				+ " GROUP BY " + MainView.BOARD_CIRCUITS_TABLE + ".circuits";
		
		String queryVoltages = "SELECT " + MainView.BOARD_VOLTAGE_FIELD
				+ " FROM " + MainView.BOARD_VOLTAGES_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.BOARD_VOLTAGES_TABLE + ".id = " + MainView.BOARD_TABLE + ".voltage_id "
				+ " GROUP BY " + MainView.BOARD_VOLTAGES_TABLE + ".voltage";
		
		String queryPhases = "SELECT " + MainView.BOARD_PHASES_FIELD
				+ " FROM " + MainView.BOARD_TABLE
				+ " GROUP BY " + MainView.BOARD_TABLE + ".phases";
		
		String queryGround = "SELECT " + MainView.BOARD_GROUND_FIELD
				+ " FROM " + MainView.BOARD_TABLE
				+ " GROUP BY " + MainView.BOARD_TABLE + ".ground";
		
		String queryInterruptions = "SELECT " + MainView.INTERRUPTION_FIELD
				+ " FROM " + MainView.INTERRUPTIONS_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.INTERRUPTIONS_TABLE + ".id = " + MainView.BOARD_TABLE + ".interruption_id "
				+ " GROUP BY " + MainView.INTERRUPTIONS_TABLE + ".interruption";
		
		String queryLockTypes = "SELECT " + MainView.BOARD_LOCK_TYPE_FIELD
				+ " FROM " + MainView.LOCK_TYPES_TABLE + "," + MainView.BOARD_TABLE
				+ " WHERE " + MainView.LOCK_TYPES_TABLE + ".id = " + MainView.BOARD_TABLE + ".lock_type_id "
				+ " GROUP BY " + MainView.LOCK_TYPES_TABLE + ".lock_type";
		
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
		textBoardSearchNames.setActionCommand("board.search.name");
		textBoardSearchNames.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String whereQuery = "";
				if(null != textBoardSearchNames && !textBoardSearchNames.getText().isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".name LIKE '%" + textBoardSearchNames.getText() + "%'";
				}
				if(null != searchSelectedBoardType  && !searchSelectedBoardType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TYPES_TABLE + ".type = '" + searchSelectedBoardType + "'";
				}
				if(searchSelectedBoardInstallation != null && !searchSelectedBoardInstallation.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardInstallation.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id ";
					whereQuery += " AND " + MainView.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoardInstallation + "'";
				}
				if(searchSelectedBoardNema != null && !searchSelectedBoardNema.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardNema.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id ";
					whereQuery += " AND " + MainView.NEMAS_TABLE + ".nema = '" + searchSelectedBoardNema + "'";
				}
				if(searchSelectedBoardBarCapacity != null && !searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardBarCapacity.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".bar_capacity_id = " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '" + searchSelectedBoardBarCapacity + "'";
				}
				if(searchSelectedBoardBarType != null && !searchSelectedBoardBarType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardBarType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".bar_type_id = " + MainView.BOARD_BAR_TYPES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_BAR_TYPES_TABLE + ".bar_type = '" + searchSelectedBoardBarType + "'";
				}
				if(searchSelectedBoardCircuits != null && !searchSelectedBoardCircuits.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardCircuits.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".circuits_id = " + MainView.BOARD_CIRCUITS_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_CIRCUITS_TABLE + ".circuits = '" + searchSelectedBoardCircuits + "'";
				}
				if(searchSelectedBoardVoltage != null && !searchSelectedBoardVoltage.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardVoltage.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".voltage_id = " + MainView.BOARD_VOLTAGES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_VOLTAGES_TABLE + ".voltage = '" + searchSelectedBoardVoltage + "'";
				}
				if(searchSelectedBoardPhases != null && !searchSelectedBoardPhases.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardPhases.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "'";
				}
				if(searchSelectedBoardGround != null && !searchSelectedBoardGround.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardGround.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".ground = '" + (searchSelectedBoardGround.equalsIgnoreCase("SI")?"1":"0") + "'";
				}
				if(searchSelectedBoardInterruption != null && !searchSelectedBoardInterruption.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardInterruption.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".interruption_id = " + MainView.INTERRUPTIONS_TABLE + ".id ";
					whereQuery += " AND " + MainView.INTERRUPTIONS_TABLE + ".interruption = '" + searchSelectedBoardInterruption + "'";
				}
				if(searchSelectedBoardLockType != null && !searchSelectedBoardLockType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardLockType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id ";
					whereQuery += " AND " + MainView.LOCK_TYPES_TABLE + ".lock_type = '" + searchSelectedBoardLockType + "'";
				}
				loadBoardTable(whereQuery);
				textBoardDescriptionName.setText("");
				textBoardDescriptionType.setText("");
				textBoardDescriptionInstallation.setText("");
				textBoardDescriptionNema.setText("");
				textBoardDescriptionBarCapacity.setText("");
				textBoardDescriptionBarType.setText("");
				textBoardDescriptionCircuits.setText("");
				textBoardDescriptionVoltage.setText("");
				textBoardDescriptionPhases.setText("");
				textBoardDescriptionGround.setText("");
				textBoardDescriptionInterruption.setText("");
				textBoardDescriptionLockType.setText("");
				textBoardDescriptionPrice.setText("");
				textBoardDescription.setText("");
				buttonBoardAdd.setEnabled(true);
				buttonBoardEdit.setEnabled(false);
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
		searchBarPanel2.add(labelLockType);
		searchBarPanel2.add(comboBoardLockTypes);
		
		searchBarPanel2.add(separator());
		
		JButton searchButton = new JButton("Buscar");
		searchButton.setActionCommand("board.search.bar.button");
		SearchButtonListener lForSearchButton = new SearchButtonListener();
		searchButton.addActionListener(lForSearchButton);
		searchBarPanel2.add(searchButton);
		
		superSearchBarPanel.add(searchBarPanel1);
		superSearchBarPanel.add(searchBarPanel2);
		
		return superSearchBarPanel;
	}
	
	private JPanel createBoardSwitchesPanel() {
		JPanel boardSwitchesPanel = new JPanel();
		boardSwitchesPanel.setLayout(new BorderLayout(20, 20));
		
		boardSwitchesPanel.add(createBoardSwitchesTablePanel(), BorderLayout.CENTER);
		return boardSwitchesPanel;
	}
	
	private JPanel createBoardSettingsPanel() {
		
		return null;
	}
	
	private JPanel createBoardTablePanel() {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(MainView.BOARD_ID_FIELD);
		fields.add(MainView.BOARD_NAME_FIELD);
		fields.add(MainView.BOARD_TYPE_FIELD);
		fields.add(MainView.INSTALLATION_FIELD);
		fields.add(MainView.NEMA_FIELD);
		fields.add(MainView.BOARD_BAR_CAPACITY_FIELD);
		fields.add(MainView.BOARD_BAR_TYPE_FIELD);
		fields.add(MainView.BOARD_CIRCUITS_FIELD);
		fields.add(MainView.BOARD_VOLTAGE_FIELD);
		fields.add(MainView.BOARD_PHASES_FIELD);
		fields.add(MainView.BOARD_GROUND_FIELD);
		fields.add(MainView.INTERRUPTION_FIELD);
		fields.add(MainView.BOARD_LOCK_TYPE_FIELD);
		fields.add(MainView.BOARD_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(MainView.BOARD_TABLE);
		tables.add(MainView.BOARD_BAR_CAPACITIES_TABLE);
		tables.add(MainView.BOARD_BAR_TYPES_TABLE);
		tables.add(MainView.BOARD_CIRCUITS_TABLE);
		tables.add(MainView.BOARD_TYPES_TABLE);
		tables.add(MainView.BOARD_VOLTAGES_TABLE);
		tables.add(MainView.INSTALLATIONS_TABLE);
		tables.add(MainView.INTERRUPTIONS_TABLE);
		tables.add(MainView.LOCK_TYPES_TABLE);
		tables.add(MainView.NEMAS_TABLE);
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boxesQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE boards.type_id = board_types.id "
						+ "AND boards.installation_id = installations.id "
						+ "AND boards.nema_id = nemas.id "
						+ "AND boards.bar_capacity_id = board_bar_capacities.id "
						+ "AND boards.bar_type_id = board_bar_types.id "
						+ "AND boards.circuits_id = board_circuits.id "
						+ "AND boards.voltage_id = board_voltages.id "
						+ "AND boards.interruption_id = interruptions.id "
						+ "AND boards.lock_type_id = lock_types.id "
						+ "AND boards.active = '1' "
						+ "GROUP BY boards.id";
		
		String[] boxesColumnNames = { "Id", "Nombre", "Tipo", "Instalacion", "Nema", "Cap. Barra", "Tipo Barra", "Circuitos", "Voltaje", "Fases", "Tierra", "Interrupcion", "Cerradura", "Precio"};
//		ArrayList<String> listBoxesColumnNames = new ArrayList<String>(Arrays.asList(originalColumns));
//		listBoxesColumnNames.add("Button");
//		String [] boxesColumnNames = listBoxesColumnNames.toArray(new String[0]);
		// The line below is when not adding a Button column at the end of the table
		boardsData = db.fetchAll(db.select(boxesQuery));
		tableBoardsResult = new JTable();
//		boxesData = db.fetchAllAddButton(db.select(boxesQuery), boxesColumnNames.length);
		
		tableBoardsResult.setModel(new MyTableModel(boardsData, boxesColumnNames));
		tableBoardsResult.setAutoCreateRowSorter(true);
		tableBoardsResult.getTableHeader().setReorderingAllowed(false);
		
		listBoardSelectionModel = tableBoardsResult.getSelectionModel();
		listBoardSelectionModel.addListSelectionListener(new SharedListSelectionListener());
		tableBoardsResult.setSelectionModel(listBoardSelectionModel);
		tableBoardsResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
//		TableColumn buttonColumn = tableBoxesResult.getColumnModel().getColumn(boxesColumnNames.length-1);
//		TableButton buttons = new TableButton();
//		buttons.addHandler(new TableButtonPressedHandler() {
//			
//			@Override
//			public void onButtonPress(int row, int column) {
//				int viewRow = tableBoxesResult.getSelectedRow();
//				Object tableId = tableBoxesResult.getValueAt(viewRow, 0);
//				JOptionPane.showMessageDialog(null, "Row:" + row + "-Column:" + column + "-ViewRow:" + viewRow + "-TableId:" + tableId);
//			}
//		});
//		
//		buttonColumn.setHeaderValue("Botones");
//		buttonColumn.setCellRenderer(buttons);
//		buttonColumn.setCellEditor(buttons);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableBoardsResult.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(tableBoardsResult, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	private JPanel createBoardSwitchesTablePanel() {
		tableBoardSwitchesResult = new JTable();
		tableBoardSwitchesResult.setModel(new DefaultTableModel());
		tableBoardSwitchesResult.setAutoCreateRowSorter(true);
		tableBoardSwitchesResult.getTableHeader().setReorderingAllowed(false);
		
		listBoardSwitchesSelectionModel = tableBoardSwitchesResult.getSelectionModel();
		listBoardSwitchesSelectionModel.addListSelectionListener(new SharedListSelectionListener());
		tableBoardSwitchesResult.setSelectionModel(listBoardSwitchesSelectionModel);
		tableBoardSwitchesResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableBoardSwitchesResult.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(tableBoardSwitchesResult, BorderLayout.CENTER);
		
		Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		// Add the add & remove buttons here for the Board Switches
		buttonAddBoardSwitch = new JButton(Fa.fa_plus);
		buttonAddBoardSwitch.setFont(fa);
		buttonAddBoardSwitch.setForeground(Color.GREEN);
		buttonAddBoardSwitch.setActionCommand("board.switch.add");
		buttonAddBoardSwitch.addActionListener(lForButton);
		buttonAddBoardSwitch.setEnabled(false);
		panelButtons.add(buttonAddBoardSwitch);
		
		buttonRemoveBoardSwitch = new JButton(Fa.fa_remove);
		buttonRemoveBoardSwitch.setFont(fa);
		buttonRemoveBoardSwitch.setForeground(Color.RED);
		buttonRemoveBoardSwitch.setActionCommand("board.switch.remove");
		buttonRemoveBoardSwitch.addActionListener(lForButton);
		buttonRemoveBoardSwitch.setEnabled(false);
		panelButtons.add(buttonRemoveBoardSwitch);
		
		tablePanel.add(panelButtons, BorderLayout.PAGE_END);
		
		return tablePanel;
	}
	
	private JPanel createBoardSwitchAddSearchPanel() {
		JPanel panelBoardSwitchAddSearch = new JPanel();
		
		panelBoardSwitchAddSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryBrands = "SELECT brand "
				+ "FROM switch_brands, switches "
				+ "WHERE switch_brands.id = switches.brand_id "
				+ "GROUP BY switch_brands.brand";
		
		String queryTypes = "SELECT type "
				+ "FROM switch_types, switches "
				+ "WHERE switch_types.id = switches.type_id "
				+ "GROUP BY switch_types.type";
		
		String queryPhases = "SELECT phases "
				+ "FROM switches "
				+ "GROUP BY phases";
		
		String queryCurrents = "SELECT current "
				+ "FROM currents, switches "
				+ "WHERE currents.id = switches.current_id "
				+ "GROUP BY currents.current";
		
		String queryInterruptions = "SELECT interruption "
				+ "FROM interruptions, switches "
				+ "WHERE interruptions.id = switches.interruption_id "
				+ "GROUP BY interruptions.interruption";
		
		JLabel brandsLabel = new JLabel("Marca:");
		JLabel typesLabel = new JLabel("Tipo:");
		JLabel phasesLabel = new JLabel("Fases:");
		JLabel currentsLabel = new JLabel("Amperaje:");
		JLabel interruptionsLabel = new JLabel("Interrupcion:");
		
		ComboBoxListener lForCombo = new ComboBoxListener();
		
		comboBoardSwitchBrands = new JComboBox<String>(new Vector<String>(loadComboList(queryBrands, "brand")));
		comboBoardSwitchBrands.setActionCommand("board.switch.bar.brand");
		comboBoardSwitchBrands.addActionListener(lForCombo);
		panelBoardSwitchAddSearch.add(brandsLabel);
		panelBoardSwitchAddSearch.add(comboBoardSwitchBrands);
		
		comboBoardSwitchTypes = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoardSwitchTypes.setActionCommand("board.switch.bar.type");
		comboBoardSwitchTypes.addActionListener(lForCombo);
		panelBoardSwitchAddSearch.add(typesLabel);
		panelBoardSwitchAddSearch.add(comboBoardSwitchTypes);
		
		comboBoardSwitchPhases = new JComboBox<String>(new Vector<String>(loadComboList(queryPhases, "phases")));
		comboBoardSwitchPhases.setActionCommand("board.switch.bar.phases");
		comboBoardSwitchPhases.addActionListener(lForCombo);
		panelBoardSwitchAddSearch.add(phasesLabel);
		panelBoardSwitchAddSearch.add(comboBoardSwitchPhases);
		
		comboBoardSwitchCurrents = new JComboBox<String>(new Vector<String>(loadComboList(queryCurrents, "current")));
		comboBoardSwitchCurrents.setActionCommand("board.switch.bar.current");
		comboBoardSwitchCurrents.addActionListener(lForCombo);
		panelBoardSwitchAddSearch.add(currentsLabel);
		panelBoardSwitchAddSearch.add(comboBoardSwitchCurrents);
		
		comboBoardSwitchInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboBoardSwitchInterruptions.setActionCommand("board.switch.bar.interruption");
		comboBoardSwitchInterruptions.addActionListener(lForCombo);
		panelBoardSwitchAddSearch.add(interruptionsLabel);
		panelBoardSwitchAddSearch.add(comboBoardSwitchInterruptions);
		
		panelBoardSwitchAddSearch.add(separator());
		
		JButton searchButton = new JButton("Buscar");
		searchButton.setActionCommand("board.switch.search.bar.button");
		SearchButtonListener lForSearchButton = new SearchButtonListener();
		searchButton.addActionListener(lForSearchButton);
		panelBoardSwitchAddSearch.add(searchButton);
		
		return panelBoardSwitchAddSearch;
	}
	
	private JPanel createBoardSwitchAddTablePanel() {
		String switchesQuery = "SELECT switches.id, "
						+ "switches.model, "
						+ "switch_brands.brand, "
						+ "switch_types.type, "
						+ "switches.phases, "
						+ "currents.current, "
						+ "interruptions.interruption, "
						+ "switch_voltages.voltage, "
						+ "switches.price "
					+ "FROM switches, "
						+ "switch_brands, "
						+ "switch_types, "
						+ "currents, "
						+ "interruptions, "
						+ "switch_voltages "
					+ "WHERE switches.brand_id = switch_brands.id "
					+ "AND switches.type_id = switch_types.id "
					+ "AND switches.current_id = currents.id "
					+ "AND switches.interruption_id = interruptions.id "
					+ "AND switches.voltage_id = switch_voltages.id "
					+ "AND switches.active = '1'";
		
		boardSwitchesSearchData = db.fetchAll(db.select(switchesQuery));
		
		String[] switchesColumnNames = { "Id", "Modelo", "Marca", "Tipo", "Fases", "Amperaje", "Interrupcion", "Voltaje", "Precio"};
		
		MyTableModel mForTable = new MyTableModel(boardSwitchesSearchData, switchesColumnNames);
		
		tableBoardSwitchesSearchResult = new JTable();
		tableBoardSwitchesSearchResult.setModel(mForTable);
		tableBoardSwitchesSearchResult.setAutoCreateRowSorter(true);
		tableBoardSwitchesSearchResult.getTableHeader().setReorderingAllowed(false);
		
		SharedListSelectionListener lForList = new SharedListSelectionListener();
		
		listBoardSwitchSearchSelectionModel = tableBoardSwitchesSearchResult.getSelectionModel();
		listBoardSwitchSearchSelectionModel.addListSelectionListener(lForList);
		tableBoardSwitchesSearchResult.setSelectionModel(listBoardSwitchSearchSelectionModel);
		tableBoardSwitchesSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelBoardSwitchAddTable = new JPanel(new BorderLayout());
		panelBoardSwitchAddTable.add(tableBoardSwitchesSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelBoardSwitchAddTable.add(tableBoardSwitchesSearchResult, BorderLayout.CENTER);
		
		return panelBoardSwitchAddTable;
	}
	
	private JPanel createBoardSwitchAddCountPanel() {
		JPanel panelCount = new JPanel();
		panelCount.setLayout(new GridLayout(1, 3));
		JButton buttonDecrease = new JButton("-");
		JButton buttonIncrease = new JButton("+");
		JLabel labelQuantity = new JLabel("1");
		boardSwitchAddQuantity = 1;
		int min = 1;
		int max = 100;
		
		panelCount.add(buttonDecrease);
		panelCount.add(labelQuantity);
		panelCount.add(buttonIncrease);
		
		buttonDecrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boardSwitchAddQuantity > min) {
					boardSwitchAddQuantity--;
					labelQuantity.setText(String.valueOf(boardSwitchAddQuantity));
				}
			}
		});
		
		buttonIncrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(boardSwitchAddQuantity < max) {
					boardSwitchAddQuantity++;
					labelQuantity.setText(String.valueOf(boardSwitchAddQuantity));
				}
			}
		});
		
		return panelCount;
	}
	
	private JPanel createBoardSwitchAddButtonPanel() {
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new GridLayout(1, 2));
		JButton buttonAccept = new JButton("Aceptar");
		JButton buttonCancel = new JButton("Cancelar");
		panelButtons.add(buttonAccept);
		panelButtons.add(buttonCancel);
		
		buttonAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableBoardSwitchesSearchResult.getSelectedRow() > -1) {
					boardSwitchSearchId = Integer.valueOf((String)tableBoardSwitchesSearchResult.getValueAt(tableBoardSwitchesSearchResult.getSelectedRow(), 0));
					boardSwitchSearchQuantity = boardSwitchAddQuantity;
					dialogBoardSwitchAdd.dispose();
					
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boardSwitchSearchId = 0;
				dialogBoardSwitchAdd.dispose();
			}
		});
		
		return panelButtons;
	}
	
	private JPanel createBoardDescriptionPanel() {
		JPanel descriptionPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		JLabel labelName = new JLabel("Nombre:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelName, cs);
		
		textBoardDescriptionName = new JTextField("", 10);
		textBoardDescriptionName.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 10;
		descriptionPanel.add(textBoardDescriptionName, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 11;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelType, cs);
		
		textBoardDescriptionType = new JTextField("", 10);
		textBoardDescriptionType.setEditable(false);
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 10;
		descriptionPanel.add(textBoardDescriptionType, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 22;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelInstallation, cs);
		
		textBoardDescriptionInstallation = new JTextField("", 8);
		textBoardDescriptionInstallation.setEditable(false);
		cs.gridx = 23;
		cs.gridy = 0;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionInstallation, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 31;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelNema, cs);
		
		textBoardDescriptionNema = new JTextField("", 4);
		textBoardDescriptionNema.setEditable(false);
		cs.gridx = 32;
		cs.gridy = 0;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoardDescriptionNema, cs);
		
		JLabel labelBarCapacity = new JLabel("Cap. Barra:");
		cs.gridx = 36;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelBarCapacity, cs);
		
		textBoardDescriptionBarCapacity = new JTextField("", 4);
		textBoardDescriptionBarCapacity.setEditable(false);
		cs.gridx = 37;
		cs.gridy = 0;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoardDescriptionBarCapacity, cs);
		
		JLabel labelBarType = new JLabel("Tipo Barra:");
		cs.gridx = 41;
		cs.gridy = 0;
		cs.gridwidth = 1;
		descriptionPanel.add(labelBarType, cs);
		
		textBoardDescriptionBarType = new JTextField("", 8);
		textBoardDescriptionBarType.setEditable(false);
		cs.gridx = 42;
		cs.gridy = 0;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionBarType, cs);
		
		JLabel labelCircuits = new JLabel("Circuitos:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelCircuits, cs);
		
		textBoardDescriptionCircuits = new JTextField("", 8);
		textBoardDescriptionCircuits.setEditable(false);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionCircuits, cs);
		
		JLabel labelVoltage = new JLabel("Voltaje:");
		cs.gridx = 9;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelVoltage, cs);
		
		textBoardDescriptionVoltage = new JTextField("", 8);
		textBoardDescriptionVoltage.setEditable(false);
		cs.gridx = 10;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionVoltage, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 18;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPhases, cs);
		
		textBoardDescriptionPhases = new JTextField("", 4);
		textBoardDescriptionPhases.setEditable(false);
		cs.gridx = 19;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoardDescriptionPhases, cs);
		
		JLabel labelGround = new JLabel("Tierra:");
		cs.gridx = 23;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelGround, cs);
		
		textBoardDescriptionGround = new JTextField("", 4);
		textBoardDescriptionGround.setEditable(false);
		cs.gridx = 24;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoardDescriptionGround, cs);
		
		JLabel labelInterruption = new JLabel("Interrupcion:");
		cs.gridx = 28;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelInterruption, cs);
		
		textBoardDescriptionInterruption = new JTextField("", 4);
		textBoardDescriptionInterruption.setEditable(false);
		cs.gridx = 29;
		cs.gridy = 1;
		cs.gridwidth = 4;
		descriptionPanel.add(textBoardDescriptionInterruption, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 33;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelLockType, cs);
		
		textBoardDescriptionLockType = new JTextField("", 8);
		textBoardDescriptionLockType.setEditable(false);
		cs.gridx = 34;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionLockType, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 42;
		cs.gridy = 1;
		cs.gridwidth = 1;
		descriptionPanel.add(labelPrice, cs);
		
		textBoardDescriptionPrice = new JTextField("", 8);
		textBoardDescriptionPrice.setEditable(false);
		cs.gridx = 43;
		cs.gridy = 1;
		cs.gridwidth = 8;
		descriptionPanel.add(textBoardDescriptionPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		descriptionPanel.add(labelDescription, cs);
		
		textBoardDescription = new JTextArea(1, 26);
		textBoardDescription.setEditable(false);
		textBoardDescription.setLineWrap(true);
		textBoardDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 26;
		descriptionPanel.add(textBoardDescription, cs);
		
		SearchButtonListener lForButton = new SearchButtonListener();
		
		JButton buttonCopy = new JButton("Copiar");
		buttonCopy.addActionListener(lForButton);
		buttonCopy.setActionCommand("board.description.copy");
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 4;
		descriptionPanel.add(buttonCopy, cs);
		
		labelBoardCopy = new JLabel("");
		cs.gridx = 4;
		cs.gridy = 4;
		cs.gridwidth = 12;
		descriptionPanel.add(labelBoardCopy, cs);
		
		return descriptionPanel;
	}
	
	private JPanel createBoardAddPanel() {
		JPanel addPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		new TextFieldListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		String queryTypes = "SELECT board_types.type "
				+ "FROM board_types "
				+ "GROUP BY board_types.type";
		
		String queryInstallations = "SELECT installation "
				+ "FROM installations "
				+ "GROUP BY installations.installation";
		
		String queryNemas = "SELECT nemas.nema "
				+ "FROM nemas "
				+ "GROUP BY nemas.nema";
		
		String queryBarCapacities = "SELECT board_bar_capacities.bar_capacity "
				+ "FROM board_bar_capacities "
				+ "GROUP BY board_bar_capacities.bar_capacity";
		
		String queryBarTypes = "SELECT board_bar_types.bar_type "
				+ "FROM board_bar_types "
				+ "GROUP BY board_bar_types.bar_type";
		
		String queryCircuits = "SELECT board_circuits.circuits "
				+ "FROM board_circuits "
				+ "GROUP BY board_circuits.circuits";
		
		String queryVoltages = "SELECT board_voltages.voltage "
				+ "FROM board_voltages "
				+ "GROUP BY board_voltages.voltage";
		
		String[] phases = {"1", "2", "3"};
				
		String queryInterruptions = "SELECT interruptions.interruption "
				+ "FROM interruptions "
				+ "GROUP BY interruptions.interruption";
		
		String queryLockTypes = "SELECT lock_types.lock_type "
				+ "FROM lock_types "
				+ "GROUP BY lock_types.lock_type";
		
		JLabel labelName = new JLabel("Nombre:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelName, cs);
		
		textBoardAddName = new JTextField(6);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(textBoardAddName, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 7;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelType, cs);
		
		comboBoardAddType = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoardAddType.removeItem("Todas");
		comboBoardAddType.setActionCommand("board.description.add.type");
		comboBoardAddType.addActionListener(lForCombo);
		cs.gridx = 8;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddType, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 14;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelInstallation, cs);
		
		comboBoardAddInstallation = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoardAddInstallation.removeItem("Todas");
		cs.gridx = 15;
		cs.gridy = 0;
		cs.gridwidth = 2;
		addPanel.add(comboBoardAddInstallation, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 17;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelNema, cs);
		
		comboBoardAddNema = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoardAddNema.removeItem("Todas");
		comboBoardAddNema.setActionCommand("board.description.add.nema");
		comboBoardAddNema.addActionListener(lForCombo);
		cs.gridx = 18;
		cs.gridy = 0;
		cs.gridwidth = 8;
		addPanel.add(comboBoardAddNema, cs);
		
		JLabel labelBarCapacity = new JLabel("Cap. Barra:");
		cs.gridx = 26;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelBarCapacity, cs);
		
		comboBoardAddBarCapacity = new JComboBox<String>(new Vector<String>(loadComboList(queryBarCapacities, "bar_capacity")));
		comboBoardAddBarCapacity.removeItem("Todas");
		cs.gridx = 27;
		cs.gridy = 0;
		cs.gridwidth = 2;
		addPanel.add(comboBoardAddBarCapacity, cs);
		
		JLabel labelBarType = new JLabel("Tipo Barra:");
		cs.gridx = 29;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelBarType, cs);
		
		comboBoardAddBarType = new JComboBox<String>(new Vector<String>(loadComboList(queryBarTypes, "bar_type")));
		comboBoardAddBarType.removeItem("Todas");
		cs.gridx = 30;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddBarType, cs);
		
		JLabel labelCircuits = new JLabel("Circuitos:");
		cs.gridx = 36;
		cs.gridy = 0;
		cs.gridwidth = 1;
		addPanel.add(labelCircuits, cs);
		
		comboBoardAddCircuits = new JComboBox<String>(new Vector<String>(loadComboList(queryCircuits, "circuits")));
		comboBoardAddCircuits.removeItem("Todas");
		comboBoardAddCircuits.setActionCommand("board.description.add.circuits");
		comboBoardAddCircuits.addActionListener(lForCombo);
		cs.gridx = 37;
		cs.gridy = 0;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddCircuits, cs);
		
		JLabel labelVoltage = new JLabel("Voltaje:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelVoltage, cs);
		
		comboBoardAddVoltage = new JComboBox<String>(new Vector<String>(loadComboList(queryVoltages, "voltage")));
		comboBoardAddVoltage.removeItem("Todas");
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddVoltage, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 7;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelPhases, cs);
		
		comboBoardAddPhases = new JComboBox<String>(new Vector<String>(Arrays.asList(phases)));
		comboBoardAddPhases.setActionCommand("board.description.add.phases");
		comboBoardAddPhases.addActionListener(lForCombo);
		cs.gridx = 8;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddPhases, cs);
		
		checkBoardAddGround = new JCheckBox("Tierra");
		cs.gridx = 14;
		cs.gridy = 1;
		cs.gridwidth = 2;
		addPanel.add(checkBoardAddGround, cs);
		
		JLabel labelInterruption = new JLabel("Interrupcion:");
		cs.gridx = 16;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelInterruption, cs);
		
		comboBoardAddInterruption = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboBoardAddInterruption.removeItem("Todas");
		cs.gridx = 17;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddInterruption, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 23;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelLockType, cs);
		
		comboBoardAddLockType = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoardAddLockType.removeItem("Todas");
		cs.gridx = 24;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(comboBoardAddLockType, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 30;
		cs.gridy = 1;
		cs.gridwidth = 1;
		addPanel.add(labelPrice, cs);
		
		textBoardAddPrice = new JTextField(6);
		cs.gridx = 31;
		cs.gridy = 1;
		cs.gridwidth = 6;
		addPanel.add(textBoardAddPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		addPanel.add(labelDescription, cs);
		
		textBoardAddDescription = new JTextArea(1,16);
		textBoardAddDescription.setEditable(false);
		textBoardAddDescription.setLineWrap(true);
		textBoardAddDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 16;
		addPanel.add(textBoardAddDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		buttonBoardAddSave = new JButton("Guardar");
		buttonBoardAddSave.setActionCommand("board.description.add.save");
		buttonBoardAddSave.addActionListener(lForButton);
		panelButtons.add(buttonBoardAddSave);
		
		buttonBoardAddCancel = new JButton("Cancelar");
		buttonBoardAddCancel.setActionCommand("board.description.add.cancel");
		buttonBoardAddCancel.addActionListener(lForButton);
		panelButtons.add(buttonBoardAddCancel);
		
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 43;
		addPanel.add(panelButtons, cs);
		
		updateBoardTextAddDescription();
		
		return addPanel;
	}
	
	private JPanel createBoardEditPanel() {
		JPanel editPanel = new JPanel(new GridBagLayout());
		ComboBoxListener lForCombo = new ComboBoxListener();
		TextFieldListener lForText = new TextFieldListener();
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.insets = new Insets(0, 0, 5, 5);
		
		String queryTypes = "SELECT board_types.type "
				+ "FROM board_types "
				+ "GROUP BY board_types.type";
		
		String queryInstallations = "SELECT installations.installation "
				+ "FROM installations "
				+ "GROUP BY installations.installation";
		
		String queryNemas = "SELECT nemas.nema "
				+ "FROM nemas "
				+ "GROUP BY nemas.nema";
		
		String queryBarCapacities = "SELECT board_bar_capacities.bar_capacity "
				+ "FROM board_bar_capacities "
				+ "GROUP BY board_bar_capacities.bar_capacity";
		
		String queryBarTypes = "SELECT board_bar_types.bar_type "
				+ "FROM board_bar_types "
				+ "GROUP BY board_bar_types.bar_type";
		
		String queryCircuits = "SELECT board_circuits.circuits "
				+ "FROM board_circuits "
				+ "GROUP BY board_circuits.circuits";
		
		String queryVoltages = "SELECT board_voltages.voltage "
				+ "FROM board_voltages "
				+ "GROUP BY board_voltages.voltage";
		
		String[] phases = {"1", "2", "3"};
		
		String queryInterruptions = "SELECT interruptions.interruption "
				+ "FROM interruptions "
				+ "GROUP BY interruptions.interruption";
		
		String queryLockTypes = "SELECT lock_types.lock_type "
				+ "FROM lock_types "
				+ "GROUP BY lock_types.lock_type";
		
		JLabel labelName = new JLabel("Nombre:");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelName, cs);
		
		textBoardEditName = new JTextField(4);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 6;
		editPanel.add(textBoardEditName, cs);
		
		JLabel labelType = new JLabel("Tipo:");
		cs.gridx = 7;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelType, cs);
		
		comboBoardEditType = new JComboBox<String>(new Vector<String>(loadComboList(queryTypes, "type")));
		comboBoardEditType.removeItem("Todas");
		comboBoardEditType.setActionCommand("board.description.edit.type");
		comboBoardEditType.addActionListener(lForCombo);
		editBoxType = comboBoardEditType.getSelectedItem().toString();
		cs.gridx = 8;
		cs.gridy = 0;
		cs.gridwidth = 6;
		editPanel.add(comboBoardEditType, cs);
		
		JLabel labelInstallation = new JLabel("Instalacion:");
		cs.gridx = 14;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelInstallation, cs);
		
		comboBoardEditInstallation = new JComboBox<String>(new Vector<String>(loadComboList(queryInstallations, "installation")));
		comboBoardEditInstallation.removeItem("Todas");
		cs.gridx = 15;
		cs.gridy = 0;
		cs.gridwidth = 4;
		editPanel.add(comboBoardEditInstallation, cs);
		
		JLabel labelNema = new JLabel("Nema:");
		cs.gridx = 19;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelNema, cs);
		
		comboBoardEditNema = new JComboBox<String>(new Vector<String>(loadComboList(queryNemas, "nema")));
		comboBoardEditNema.removeItem("Todas");
		comboBoardEditNema.setActionCommand("board.description.edit.nema");
		comboBoardEditNema.addActionListener(lForCombo);
		cs.gridx = 20;
		cs.gridy = 0;
		cs.gridwidth = 2;
		editPanel.add(comboBoardEditNema, cs);
		
		JLabel labelBarCapacity = new JLabel("Cap. Barra:");
		cs.gridx = 22;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelBarCapacity, cs);
		
		comboBoardEditBarCapacity = new JComboBox<String>(new Vector<String>(loadComboList(queryBarCapacities, "bar_capacity")));
		comboBoardEditBarCapacity.removeItem("Todas");
		cs.gridx = 23;
		cs.gridy = 0;
		cs.gridwidth = 2;
		editPanel.add(comboBoardEditBarCapacity, cs);
		
		JLabel labelBarType = new JLabel("Tipo Barra:");
		cs.gridx = 25;
		cs.gridy = 0;
		cs.gridwidth = 1;
		editPanel.add(labelBarType, cs);
		
		comboBoardEditBarType = new JComboBox<String>(new Vector<String>(loadComboList(queryBarTypes, "bar_type")));
		comboBoardEditBarType.removeItem("Todas");
		cs.gridx = 26;
		cs.gridy = 0;
		cs.gridwidth = 4;
		editPanel.add(comboBoardEditBarType, cs);
		
		JLabel labelCircuits = new JLabel("Circuitos:");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelCircuits, cs);
		
		comboBoardEditCircuits = new JComboBox<String>(new Vector<String>(loadComboList(queryCircuits, "circuits")));
		comboBoardEditCircuits.removeItem("Todas");
		comboBoardEditCircuits.setActionCommand("board.description.edit.circuits");
		comboBoardEditCircuits.addActionListener(lForCombo);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		editPanel.add(comboBoardEditCircuits, cs);
		
		JLabel labelVoltage = new JLabel("Voltaje:");
		cs.gridx = 3;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelVoltage, cs);
		
		comboBoardEditVoltage = new JComboBox<String>(new Vector<String>(loadComboList(queryVoltages, "voltage")));
		comboBoardEditVoltage.removeItem("Todas");
		cs.gridx = 4;
		cs.gridy = 1;
		cs.gridwidth = 4;
		editPanel.add(comboBoardEditVoltage, cs);
		
		JLabel labelPhases = new JLabel("Fases:");
		cs.gridx = 8;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelPhases, cs);
		
		comboBoardEditPhases = new JComboBox<String>(new Vector<String>(Arrays.asList(phases)));
		comboBoardEditPhases.addKeyListener(lForText);
		cs.gridx = 9;
		cs.gridy = 1;
		cs.gridwidth = 2;
		editPanel.add(comboBoardEditPhases, cs);
		
		checkBoardEditGround = new JCheckBox("Tierra");
		cs.gridx = 11;
		cs.gridy = 1;
		cs.gridwidth = 2;
		editPanel.add(checkBoardEditGround, cs);
		
		JLabel labelInterruption = new JLabel("Interrupcion:");
		cs.gridx = 13;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelInterruption, cs);
		
		comboBoardEditInterruption = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboBoardEditInterruption.removeItem("Todas");
		cs.gridx = 14;
		cs.gridy = 1;
		cs.gridwidth = 2;
		editPanel.add(comboBoardEditInterruption, cs);
		
		JLabel labelLockType = new JLabel("Cerradura:");
		cs.gridx = 16;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelLockType, cs);
		
		comboBoardEditLockType = new JComboBox<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type")));
		comboBoardEditLockType.removeItem("Todas");
		cs.gridx = 17;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(comboBoardEditLockType, cs);
		
		JLabel labelPrice = new JLabel("Precio:");
		cs.gridx = 23;
		cs.gridy = 1;
		cs.gridwidth = 1;
		editPanel.add(labelPrice, cs);
		
		textBoardEditPrice = new JTextField(6);
		cs.gridx = 24;
		cs.gridy = 1;
		cs.gridwidth = 6;
		editPanel.add(textBoardEditPrice, cs);
		
		JLabel labelDescription = new JLabel("Descripcion:");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		editPanel.add(labelDescription, cs);
		
		textBoardEditDescription = new JTextArea(1,16);
		textBoardEditDescription.setEditable(false);
		textBoardEditDescription.setLineWrap(true);
		textBoardEditDescription.setWrapStyleWord(true);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 16;
		editPanel.add(textBoardEditDescription, cs);
		
		ButtonListener lForButton = new ButtonListener();
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		buttonBoardEditSave = new JButton("Guardar");
		buttonBoardEditSave.setActionCommand("board.description.edit.save");
		buttonBoardEditSave.addActionListener(lForButton);
		panelButtons.add(buttonBoardEditSave);
		
		buttonBoardEditCancel = new JButton("Cancelar");
		buttonBoardEditCancel.setActionCommand("board.description.edit.cancel");
		buttonBoardEditCancel.addActionListener(lForButton);
		panelButtons.add(buttonBoardEditCancel);
		
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 30;
		editPanel.add(panelButtons, cs);
		
		updateBoardTextEditDescription();
		
		return editPanel;
	}
	
	private void setBoardsMode(int mode) {
		
		if(mode == MainView.VIEW_MODE) {
			if(panelBoardAddNew.isVisible()) {
				buttonBoardAdd.setEnabled(true);
				textBoardAddName.setText("");
				textBoardAddPrice.setText("");
				textBoardAddDescription.setText("");
				loadBoardTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperBoardDescription.remove(panelBoardAddNew);
						panelWrapperBoardDescription.validate();
						panelWrapperBoardDescription.repaint();
						panelBoardAddNew.setVisible(false);
						
						panelBoardDescription.setVisible(true);
						panelWrapperBoardDescription.add(panelBoardDescription);
						panelWrapperBoardDescription.validate();
						panelWrapperBoardDescription.repaint();
						
						boardsFrame.validate();
						boardsFrame.repaint();
					}
				});
			} else if (panelBoardEdit.isVisible()) {
				buttonBoardAdd.setEnabled(true);
				loadBoardTable("");
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						panelWrapperBoardDescription.remove(panelBoardEdit);
						panelWrapperBoardDescription.validate();
						panelWrapperBoardDescription.repaint();
						panelBoardEdit.setVisible(false);
						
						panelBoardDescription.setVisible(true);
						panelWrapperBoardDescription.add(panelBoardDescription);
						panelWrapperBoardDescription.validate();
						panelWrapperBoardDescription.repaint();
						
						boardsFrame.validate();
						boardsFrame.repaint();
					}
				});
			}
		} else if(mode == MainView.ADD_MODE) {
			buttonBoardAdd.setEnabled(false);
			buttonBoardEdit.setEnabled(false);
			textBoardDescriptionName.setText("");
			textBoardDescriptionPrice.setText("");
			tableBoardsResult.clearSelection();
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperBoardDescription.remove(panelBoardDescription);
					panelWrapperBoardDescription.validate();
					panelWrapperBoardDescription.repaint();
					panelBoardDescription.setVisible(false);
					
					panelBoardAddNew.setVisible(true);
					panelWrapperBoardDescription.add(panelBoardAddNew);
					panelWrapperBoardDescription.validate();
					panelWrapperBoardDescription.repaint();
					
					updateBoardTextAddDescription();
					
					boardsFrame.validate();
					boardsFrame.repaint();
				}
			});
		} else if(mode == MainView.EDIT_MODE) {
			buttonBoardAdd.setEnabled(false);
			buttonBoardEdit.setEnabled(false);
			editBoardId = Integer.valueOf(String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_ID_COLUMN)));
			editBoardName = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_NAME_COLUMN));
			editBoardType = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_TYPE_COLUMN));
			editBoardInstallation = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_INSTALLATION_COLUMN));
			editBoardNema = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_NEMA_COLUMN));
			editBoardBarCapacity = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_BAR_CAPACITY_COLUMN));
			editBoardBarType = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_BAR_TYPE_COLUMN));
			editBoardCircuits = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_CIRCUITS_COLUMN));
			editBoardVoltage = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_VOLTAGE_COLUMN));
			editBoardPhases = Integer.valueOf(String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_PHASES_COLUMN)));
			editBoardGround = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_GROUND_COLUMN));
			editBoardInterruption = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_INTERRUPTION_COLUMN));
			editBoardLockType = String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_LOCK_TYPE_COLUMN));
			editBoardPrice = Double.valueOf(String.valueOf(tableBoardsResult.getValueAt(boardsTableSelectedIndex, SharedListSelectionListener.BOARD_PRICE_COLUMN)));
			
			String queryTypes = "SELECT board_types.type "
					+ "FROM board_types "
					+ "GROUP BY board_types.type";
			
			String queryInstallations = "SELECT installation "
					+ "FROM installations "
					+ "GROUP BY installations.installation";
			
			String queryNemas = "SELECT nemas.nema "
					+ "FROM nemas "
					+ "GROUP BY nemas.nema";
			
			String queryBarCapacities = "SELECT board_bar_capacities.bar_capacity "
					+ "FROM board_bar_capacities "
					+ "GROUP BY board_bar_capacities.bar_capacity";
			
			String queryBarTypes = "SELECT board_bar_types.bar_type "
					+ "FROM board_bar_types "
					+ "GROUP BY board_bar_types.bar_type";
			
			String queryCircuits = "SELECT board_circuits.circuits "
					+ "FROM board_circuits "
					+ "GROUP BY board_circuits.circuits";
			
			String queryVoltages = "SELECT board_voltages.voltage "
					+ "FROM board_voltages "
					+ "GROUP BY board_voltages.voltage";
			
			String[] phases = {"1", "2", "3"};
					
			String queryInterruptions = "SELECT interruptions.interruption "
					+ "FROM interruptions "
					+ "GROUP BY interruptions.interruption";
			
			String queryLockTypes = "SELECT lock_types.lock_type "
					+ "FROM lock_types "
					+ "GROUP BY lock_types.lock_type";
			
			textBoardEditName.setText(editBoardName);
			
			comboBoardEditType.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryTypes, "type"))));
			comboBoardEditType.removeItem("Todas");
			comboBoardEditType.setSelectedItem(editBoardType);
			
			comboBoardEditInstallation.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryInstallations, "installation"))));
			comboBoardEditInstallation.removeItem("Todas");
			comboBoardEditInstallation.setSelectedItem(editBoardInstallation);
			
			comboBoardEditNema.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryNemas, "nema"))));
			comboBoardEditNema.removeItem("Todas");
			comboBoardEditNema.setSelectedItem(editBoardNema);
			
			comboBoardEditBarCapacity.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryBarCapacities, "bar_capacity"))));
			comboBoardEditBarCapacity.removeItem("Todas");
			comboBoardEditBarCapacity.setSelectedItem(editBoardBarCapacity);
			
			comboBoardEditBarType.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryBarTypes, "bar_type"))));
			comboBoardEditBarType.removeItem("Todas");
			comboBoardEditBarType.setSelectedItem(editBoardBarType);
			
			comboBoardEditCircuits.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryCircuits, "circuits"))));
			comboBoardEditCircuits.removeItem("Todas");
			comboBoardEditCircuits.setSelectedItem(editBoardCircuits);
			
			comboBoardEditVoltage.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryVoltages, "voltage"))));
			comboBoardEditVoltage.removeItem("Todas");
			comboBoardEditVoltage.setSelectedItem(editBoardVoltage);
			
			comboBoardEditPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(Arrays.asList(phases))));
			comboBoardEditPhases.setSelectedItem(String.valueOf(editBoardPhases));
			
			checkBoardEditGround.setSelected((editBoardGround.equalsIgnoreCase("SI")?true:false));
			
			comboBoardEditInterruption.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption"))));
			comboBoardEditInterruption.removeItem("Todas");
			comboBoardEditInterruption.setSelectedItem(editBoardInterruption);
			
			comboBoardEditLockType.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryLockTypes, "lock_type"))));
			comboBoardEditLockType.removeItem("Todas");
			comboBoardEditLockType.setSelectedItem(editBoardLockType);

			textBoardEditPrice.setText(String.valueOf(editBoardPrice));
			
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					panelWrapperBoardDescription.remove(panelBoardDescription);
					panelWrapperBoardDescription.validate();
					panelWrapperBoardDescription.repaint();
					panelBoardDescription.setVisible(false);
					
					panelBoardEdit.setVisible(true);
					panelWrapperBoardDescription.add(panelBoardEdit);
					panelWrapperBoardDescription.validate();
					panelWrapperBoardDescription.repaint();
					
					boardsFrame.validate();
					boardsFrame.repaint();
				}
			});
			this.updateBoardTextEditDescription();
			textBoardDescriptionName.setText("");
			textBoardDescriptionType.setText("");
			textBoardDescriptionInstallation.setText("");
			textBoardDescriptionNema.setText("");
			textBoardDescriptionBarCapacity.setText("");
			textBoardDescriptionBarType.setText("");
			textBoardDescriptionCircuits.setText("");
			textBoardDescriptionVoltage.setText("");
			textBoardDescriptionPhases.setText("");
			textBoardDescriptionGround.setText("");
			textBoardDescriptionInterruption.setText("");
			textBoardDescriptionLockType.setText("");
			textBoardDescriptionPrice.setText("");
			textBoardDescription.setText("");
		}
		
	}
	
	private void loadBoardTable(String whereQuery) {
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(MainView.BOARD_ID_FIELD);
		fields.add(MainView.BOARD_NAME_FIELD);
		fields.add(MainView.BOARD_TYPE_FIELD);
		fields.add(MainView.INSTALLATION_FIELD);
		fields.add(MainView.NEMA_FIELD);
		fields.add(MainView.BOARD_BAR_CAPACITY_FIELD);
		fields.add(MainView.BOARD_BAR_TYPE_FIELD);
		fields.add(MainView.BOARD_CIRCUITS_FIELD);
		fields.add(MainView.BOARD_VOLTAGE_FIELD);
		fields.add(MainView.BOARD_PHASES_FIELD);
		fields.add(MainView.BOARD_GROUND_FIELD);
		fields.add(MainView.INTERRUPTION_FIELD);
		fields.add(MainView.BOARD_LOCK_TYPE_FIELD);
		fields.add(MainView.BOARD_PRICE_FIELD);
		
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(MainView.BOARD_TABLE);
		tables.add(MainView.BOARD_TYPES_TABLE);
		tables.add(MainView.INSTALLATIONS_TABLE);
		tables.add(MainView.NEMAS_TABLE);
		tables.add(MainView.BOARD_BAR_CAPACITIES_TABLE);
		tables.add(MainView.BOARD_BAR_TYPES_TABLE);
		tables.add(MainView.BOARD_CIRCUITS_TABLE);
		tables.add(MainView.BOARD_VOLTAGES_TABLE);
		tables.add(MainView.INTERRUPTIONS_TABLE);
		tables.add(MainView.LOCK_TYPES_TABLE);
		
//		if(whereQuery.isEmpty()) {
//			whereQuery += " AND ( (boxes.sheet_id > 0 "
//							+ " AND boxes.sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id) "
//							+ " OR boxes.sheet_id = 0) "
//						+ " AND ( (boxes.finish_id > 0 "
//							+ " AND boxes.finish_id = " + MainView.BOX_FINISHES_TABLE + ".id) "
//							+ " OR boxes.finish_id = 0) "
//						+ " AND ( (boxes.color_id > 0 "
//							+ " AND boxes.color_id = " + MainView.BOX_COLORS_TABLE + ".id) "
//							+ " OR boxes.color_id = 0) "
//						+ " AND ( (boxes.units_id > 0 "
//							+ " AND boxes.units_id = " + MainView.BOX_UNITS_TABLE + ".id) "
//							+ " OR boxes.units_id = 0) "
//						+ " AND ( (boxes.caliber_id > 0 "
//							+ " AND boxes.caliber_id = " + MainView.BOX_CALIBERS_TABLE + ".id) "
//							+ " OR boxes.caliber_id = 0) "
//						+ " AND ( (boxes.lock_type_id > 0 "
//							+ " AND boxes.lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id) "
//							+ " OR boxes.lock_type_id = 0) ";
//		}
		
		String fieldsQuery = StringTools.implode(",", fields);
		String tablesQuery = StringTools.implode(",", tables);
		String boardsQuery = "SELECT " + fieldsQuery
						+ " FROM "
						+ tablesQuery
						+ " WHERE boards.type_id = board_types.id "
						// Fix these conditions to make them disappear when filtering
						
						+ "AND boards.installation_id = installations.id "
						+ "AND boards.nema_id = nemas.id "
						+ "AND boards.bar_capacity_id = board_bar_capacities.id "
						+ "AND boards.bar_type_id = board_bar_types.id "
						+ "AND boards.circuits_id = board_circuits.id "
						+ "AND boards.voltage_id = board_voltages.id "
						+ "AND boards.interruption_id = interruptions.id "
						+ "AND boards.lock_type_id = lock_types.id "
						+ "AND boards.active = '1' "
						+ whereQuery
						+ " GROUP BY boards.id";

		boardsData = db.fetchAll(db.select(boardsQuery));
		
		String[] boardsColumnNames = { "Id", "Nombre", "Tipo", "Instalacion", "Nema", "Cap. Barra", "Tipo Barra", "Circuitos", "Voltaje", "Fases", "Tierra", "Interrupcion", "Cerradura", "Precio"};
		
		if(boardsData.length > 0) {
			tableBoardsResult.setModel(new MyTableModel(boardsData, boardsColumnNames));
		} else {
			tableBoardsResult.setModel(new DefaultTableModel());
		}
		tableBoardSwitchesResult.setModel(new DefaultTableModel());
		buttonAddBoardSwitch.setEnabled(false);
		buttonRemoveBoardSwitch.setEnabled(false);
	}
	
	private void updateBoardTextAddDescription() {
		String boardType = (null != comboBoardAddType.getSelectedItem().toString())?comboBoardAddType.getSelectedItem().toString():"";
		String boardPhases =(null != comboBoardAddPhases.getSelectedItem().toString())?comboBoardAddPhases.getSelectedItem().toString():"";
		String boardCircuits =(null != comboBoardAddCircuits.getSelectedItem().toString())?comboBoardAddCircuits.getSelectedItem().toString():"";
		String boardNema =(null != comboBoardAddNema.getSelectedItem().toString())?comboBoardAddNema.getSelectedItem().toString():"";
		if(null != textBoardAddDescription) {
			textBoardAddDescription.setText("Caja para Tablero, " +
					boardType +
					", " +
					boardPhases +
					", de " +
					boardCircuits +
					" circuitos, " +
					boardNema);
		}
	}
	
	private void updateBoardTextEditDescription() {
		String boardType = (null != comboBoardEditType.getSelectedItem().toString())?comboBoardEditType.getSelectedItem().toString():"";
		String boardPhases =(null != comboBoardEditPhases.getSelectedItem().toString())?comboBoardEditPhases.getSelectedItem().toString():"";
		String boardCircuits =(null != comboBoardEditCircuits.getSelectedItem().toString())?comboBoardEditCircuits.getSelectedItem().toString():"";
		String boardNema =(null != comboBoardEditNema.getSelectedItem().toString())?comboBoardEditNema.getSelectedItem().toString():"";
		if(null != textBoardEditDescription) {
			textBoardEditDescription.setText("Caja para Tablero, " +
					boardType +
					", " +
					boardPhases +
					", de " +
					boardCircuits +
					" circuitos, " +
					boardNema);
		}
	}
	
	private JTabbedPane createBudgetMainPanel() {
		
		JTabbedPane budgetViewTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel budgetMainPanel = new JPanel();
		budgetMainPanel.setLayout(new BorderLayout(20,20));
		
		budgetMainPanel.add(createBudgetSearchBarPanel(), BorderLayout.NORTH);
		
		tableBudgetsResult = new JTable();
				
		tableBudgetScrollPane = new JScrollPane(createBudgetTablePanel());
		tableBudgetsResult.setFillsViewportHeight(true);
		budgetMainPanel.add(tableBudgetScrollPane, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		ButtonListener lForButton = new ButtonListener();
		
		buttonBudgetAdd = new JButton("Agregar");
		buttonBudgetAdd.setActionCommand("budget.description.buttons.add");
		buttonBudgetAdd.addActionListener(lForButton);
		panelButtons.add(buttonBudgetAdd);
		
		buttonBudgetEdit = new JButton("Editar");
		buttonBudgetEdit.setActionCommand("budget.description.buttons.edit");
		buttonBudgetEdit.addActionListener(lForButton);
		buttonBudgetEdit.setEnabled(false);
		panelButtons.add(buttonBudgetEdit);
		
		JPanel panelBudgetLower = new JPanel(new BorderLayout(20,20));
		createBudgetDescriptionPanel();
		
		panelWrapperBudgetDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		panelWrapperBudgetDescription.add(panelBudgetDescription);
		/*
		panelBudgetAddNew = createBudgetAddPanel();
		panelBudgetAddNew.setVisible(false);
		
		panelBudgetEdit = createBudgetEditPanel();
		panelBudgetEdit.setVisible(false);
		*/
		
		JPanel panelWrapperBudgetDescription = new JPanel(new BorderLayout(20,20));
		panelBudgetLower.add(panelWrapperBudgetDescription, BorderLayout.CENTER);
		panelBudgetLower.add(panelButtons, BorderLayout.SOUTH);
		
		budgetMainPanel.add(panelBudgetLower, BorderLayout.SOUTH);
		budgetViewTabbedPane.addTab("Buscar", null, budgetMainPanel, "Buscar");
		
		budgetSwitchesPanel = createBudgetSwitchesPanel();
		
		budgetViewTabbedPane.addTab("Interruptores", null, budgetSwitchesPanel, "Interruptores");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
	
		budgetBoxesPanel = createBudgetBoxesPanel();
		
		budgetViewTabbedPane.addTab("Cajas", null, budgetBoxesPanel, "Cajas");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
		
		budgetBoardsPanel = createBudgetBoardsPanel();
		
		budgetViewTabbedPane.addTab("Tableros", null, budgetBoardsPanel, "Tableros");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
		
		budgetMaterialsPanel = createBudgetMaterialsPanel();
		
		budgetViewTabbedPane.addTab("Materiales", null, budgetMaterialsPanel, "Materiales");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
		
		budgetSpecialsPanel = createBudgetSpecialsPanel();
		
		budgetViewTabbedPane.addTab("Especiales", null, budgetSpecialsPanel, "Especiales");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
		
		budgetNotesPanel = createBudgetNotesPanel();
		
		budgetViewTabbedPane.addTab("Notas", null, budgetNotesPanel, "Notas");
		budgetViewTabbedPane.setFont(new Font(null, Font.BOLD, 16));
					
		return budgetViewTabbedPane;
	}
	
	private JPanel createBudgetBoardsPanel() {
		JPanel createBudgetBoardsPanel = new JPanel();
		return createBudgetBoardsPanel;
	}

	private JPanel createBudgetMaterialsPanel() {
		JPanel createBudgetMaterialsPanel = new JPanel();
		return createBudgetMaterialsPanel;
	}

	private JPanel createBudgetSpecialsPanel() {
		JPanel createBudgetSpecialsPanel = new JPanel();
		return createBudgetSpecialsPanel;
	}

	private JPanel createBudgetNotesPanel() {
		JPanel createBudgetNotes = new JPanel();
		return createBudgetNotes;
	}

	private JPanel createBudgetBoxesPanel() {
		JPanel panelBudgetBoxes = new JPanel();
		return panelBudgetBoxes;
	}

	private JPanel createBudgetSwitchesPanel() {
		JPanel panelBudgetSwitches = new JPanel();		
		
//		budgetSwitchesPanel.setLayout(new BorderLayout(20, 20));		
//		budgetSwitchesPanel.add(createBudgetSwitchesTablePanel(), BorderLayout.CENTER);		
//		
		return panelBudgetSwitches;
	}

	private JPanel createBudgetSwitchesTablePanel() {
		JPanel tablePanel = new JPanel();
		return tablePanel;
	}

	private JPanel createBudgetDescriptionPanel() {
		JPanel panelBudgetDescription = new JPanel();
		return panelBudgetDescription;
	}

	private JPanel createBudgetTablePanel() {
		JPanel panelBudgetTable = new JPanel();
		return panelBudgetTable;
	}

	private JPanel createBudgetSearchBarPanel() {
		JPanel panelBudgetSearch = new JPanel();
		
		panelBudgetSearch.setLayout(new BoxLayout(panelBudgetSearch, BoxLayout.PAGE_AXIS));
		JPanel searchBarPanel1 = new JPanel();
		searchBarPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel searchBarPanel2 = new JPanel();
		searchBarPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel labelName = new JLabel("Nombre: ");
		JLabel labelDate = new JLabel("Fecha:");
		JLabel labelExpiration = new JLabel("Días de Vencimiento:");
		JLabel labelWorkName = new JLabel("Nombre de la Obra:");
		JLabel labelMethodPayment = new JLabel("Forma de Pago:");
		JLabel labelBusinessName = new JLabel("Nombre de Empresa:");
		JLabel labelBusinessRepresentaive = new JLabel("Representate de Empresa:");
		JLabel labelDeliveryPlace = new JLabel("Lugar de Entrega:");
		JLabel labelDeliveryTime = new JLabel("Tiempo de Entrega:");
		JLabel labelSeller = new JLabel("Vendedor:");
		JLabel labelTracing = new JLabel("Seguimiento:");		
		
		
		
		return panelBudgetSearch;
	}
	
	private List<String> loadComboList(String queryString, String columnName) {
		List<String> comboList = new ArrayList<String>();
		comboList.add("Todas");
		comboList.addAll(db.fetchColumnAsList(db.select(queryString), columnName));
		return comboList;
	}
	
	private JSeparator separator() {
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		Dimension size = new Dimension(
		    separator.getPreferredSize().width,
		    separator.getMaximumSize().height);
		separator.setMaximumSize(size);
		return separator;
	}
	
	private class ToolbarButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equalsIgnoreCase("bar.button.switches")) {
				if(switchesFrame.isClosed()){
					createSwitchesFrame();
				}
			}else if(e.getActionCommand().equalsIgnoreCase("bar.button.boxes")) {
				if(boxesFrame.isClosed()){
					createBoxesFrame();
				}
			}else if(e.getActionCommand().equalsIgnoreCase("bar.button.panels")) {
				if(boardsFrame.isClosed()){
					createBoardsFrame();
				}
			}else if(e.getActionCommand().equalsIgnoreCase("bar.button.budgets")) {
				if(budgetsFrame.isClosed()){
					createBudgetsFrame();
				}
			}else if(e.getActionCommand().equalsIgnoreCase("bar.button.starters")) {
				if(startersFrame.isClosed()){
					createStartersFrame();
				}
			}else if(e.getActionCommand().equalsIgnoreCase("bar.button.tracing")) {
				if(tracingFrame.isClosed()){
					createTracingFrame();
				}
			}
		}
		
	}
	
	private class ComboBoxListener implements ActionListener {

		String fromQuery = "";
		String whereQuery = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			whereQuery = "";
			if(actionCommand.equalsIgnoreCase("switch.bar.brand")) {
				fromQuery = "switches, switch_types ";
				searchSelectedSwitchBrand = comboSwitchBrands.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("type");
				if(!searchSelectedSwitchBrand.equalsIgnoreCase("Todas")) {
					fromQuery += ", switch_brands ";
					whereQuery = "WHERE switches.brand_id = switch_brands.id "
									+ "AND switch_types.brand_id = switch_brands.id "
									+ "AND switch_types.id = switches.type_id "
									+ "AND switch_brands.brand = '"+searchSelectedSwitchBrand+"' ";
				} else {
					whereQuery = "WHERE switch_types.id = switches.type_id ";
				}
				this.addSwitchCommonQuery();
				this.loadComboSwitch("types");
			} else if (actionCommand.equalsIgnoreCase("switch.bar.type")) {
				fromQuery = "switches ";
				searchSelectedSwitchType = comboSwitchTypes.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("phases");
				if(!searchSelectedSwitchType.equalsIgnoreCase("Todas")) {
					fromQuery += ", switch_types ";
					whereQuery = "WHERE switches.type_id = switch_types.id "
									+ "AND switch_types.type = '"+searchSelectedSwitchType+"' ";
				} else {
					whereQuery = "";
				}
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("phases");
			} else if (actionCommand.equalsIgnoreCase("switch.bar.phases")) {
				fromQuery = "switches ";
				searchSelectedSwitchPhases = comboSwitchPhases.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("current");
				this.switchPhasesQuery();
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("currents");
			} else if (actionCommand.equalsIgnoreCase("switch.bar.current")) {
				fromQuery = "switches ";
				searchSelectedSwitchCurrent = comboSwitchCurrents.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("interruption");
				if(!searchSelectedSwitchCurrent.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += " switches.current_id = currents.id "
									+ "AND currents.current = '" + searchSelectedSwitchCurrent + "' ";
				}
				this.switchPhasesQuery();
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("interruptions");
			} else if (actionCommand.equalsIgnoreCase("switch.bar.interruption")) {
				searchSelectedSwitchInterruption = comboSwitchInterruptions.getSelectedItem().toString();
			} else if (actionCommand.equalsIgnoreCase("switch.description.add.brand")) {
				addSelectedSwitchBrand = comboSwitchAddBrands.getSelectedItem().toString();
				fromQuery = "switch_types, switch_brands ";
				whereQuery = "WHERE switch_brands.brand = '" + addSelectedSwitchBrand + "' "
							+ "AND switch_types.brand_id = switch_brands.id ";
				String queryType = "SELECT switch_types.type "
								 + "FROM " + fromQuery
								 + whereQuery
								 + "GROUP BY switch_types.type ";
				comboSwitchAddTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryType, "type"))));
				comboSwitchAddTypes.removeItem("Todas");
				updateSwitchTextAddDescription();
			} else if (actionCommand.equalsIgnoreCase("switch.description.add.type") || e.getActionCommand().equalsIgnoreCase("description.add.phases") || e.getActionCommand().equalsIgnoreCase("description.add.current")){
				updateSwitchTextAddDescription();
			} else if (actionCommand.equalsIgnoreCase("switch.description.edit.phases") || e.getActionCommand().equalsIgnoreCase("description.edit.current") || e.getActionCommand().equalsIgnoreCase("description.edit.type")) {
				updateSwitchTextEditDescription();
			} else if (actionCommand.equalsIgnoreCase("switch.description.edit.brand")) {
				editSelectedSwitchBrand = comboSwitchEditBrand.getSelectedItem().toString();
				fromQuery = "switch_types, switch_brands ";
				whereQuery = "WHERE switch_brands.brand = '" + editSelectedSwitchBrand + "' "
							+ "AND switch_types.brand_id = switch_brands.id ";
				String queryType = "SELECT switch_types.type "
								+ "FROM " + fromQuery
								+ whereQuery
								+ "GROUP BY switch_types.type";
				comboSwitchEditType.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadComboList(queryType, "type"))));
				comboSwitchEditType.removeItem("Todas");
				if(editSwitchBrand.equalsIgnoreCase(editSelectedSwitchBrand)) {
					comboSwitchEditType.setSelectedItem(editSwitchType);
				}
			} else if (actionCommand.equalsIgnoreCase("box.search.type")) {
				fromQuery = MainView.BOXES_TABLE + "," + MainView.BOX_TYPES_TABLE;
				whereQuery = " WHERE " + MainView.BOXES_TABLE + ".type_id = " + MainView.BOX_TYPES_TABLE + ".id ";
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
					whereQuery += MainView.BOX_TYPES_TABLE + ".type = '"+searchSelectedBoxType+"' ";
				}
				this.loadComboBox("types");
			} else if (actionCommand.equalsIgnoreCase("box.search.installation")) {
				searchSelectedBoxInstallation = comboBoxInstallations.getSelectedItem().toString();
				this.clearSelectedBoxOptions("installation");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.INSTALLATIONS_TABLE;
				if(!searchSelectedBoxInstallation.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.INSTALLATIONS_TABLE + ".installation = '"+searchSelectedBoxInstallation+"' ";
				}
				this.loadComboBox("installations");
			} else if (actionCommand.equalsIgnoreCase("box.search.nema")) {
				searchSelectedBoxNema = comboBoxNemas.getSelectedItem().toString();
				this.clearSelectedBoxOptions("nema");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.NEMAS_TABLE;
				if(!searchSelectedBoxNema.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.NEMAS_TABLE + ".nema = '"+searchSelectedBoxNema+"' ";
				}
				this.loadComboBox("nemas");
			} else if (actionCommand.equalsIgnoreCase("box.search.pairs")) {
				searchSelectedBoxPairs = comboBoxPairs.getSelectedItem().toString();
				this.clearSelectedBoxOptions("pairs");
				fromQuery = MainView.BOXES_TABLE;
				if(!searchSelectedBoxPairs.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".pairs = '"+searchSelectedBoxPairs+"' ";
				}
				this.loadComboBox("pairs");
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
				fromQuery = MainView.BOXES_TABLE + "," + MainView.BOX_SHEETS_TABLE;
				if(!searchSelectedBoxSheet.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOX_SHEETS_TABLE + ".sheet = '"+searchSelectedBoxSheet+"' ";
				}
				this.loadComboBox("sheets");
			} else if (actionCommand.equalsIgnoreCase("box.search.finish")) {
				searchSelectedBoxFinish = comboBoxFinishes.getSelectedItem().toString();
				this.clearSelectedBoxOptions("finish");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.BOX_FINISHES_TABLE;
				if(!searchSelectedBoxFinish.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOX_FINISHES_TABLE + ".finish = '"+searchSelectedBoxFinish+"' ";
				}
				this.loadComboBox("finishes");
			} else if (actionCommand.equalsIgnoreCase("box.search.color")) {
				searchSelectedBoxColor = comboBoxColors.getSelectedItem().toString();
				this.clearSelectedBoxOptions("color");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.BOX_COLORS_TABLE;
				if(!searchSelectedBoxColor.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOX_COLORS_TABLE + ".color = '"+searchSelectedBoxColor+"' ";
				}
				this.loadComboBox("colors");
			} else if (actionCommand.equalsIgnoreCase("box.search.caliber")) {
				searchSelectedBoxCaliber = comboBoxCalibers.getSelectedItem().toString();
				this.clearSelectedBoxOptions("caliber");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.BOX_CALIBERS_TABLE;
				if(!searchSelectedBoxCaliber.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOX_CALIBERS_TABLE + ".caliber = '"+searchSelectedBoxCaliber+"' ";
				}
				this.loadComboBox("calibers");
			} else if (actionCommand.equalsIgnoreCase("box.search.lock_type")) {
				searchSelectedBoxLockType = comboBoxLockTypes.getSelectedItem().toString();
				this.clearSelectedBoxOptions("lock_type");
				fromQuery = MainView.BOXES_TABLE + "," + MainView.LOCK_TYPES_TABLE;
				if(!searchSelectedBoxLockType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.LOCK_TYPES_TABLE + ".lock_type = '"+searchSelectedBoxLockType+"' ";
				}
				this.loadComboBox("lock_types");
			} else if (actionCommand.equalsIgnoreCase("box.description.add.type")) {
				addSelectedBoxType = comboBoxAddTypes.getSelectedItem().toString();
				if(!addSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
					textBoxAddPairs.setEnabled(false);
				} else {
					textBoxAddPairs.setEnabled(true);
				}
				if(!addSelectedBoxType.equalsIgnoreCase("de paso")) {
					comboBoxAddLockTypes.setEnabled(true);
				} else {
					comboBoxAddLockTypes.setEnabled(false);
				}
				updateBoxTextAddDescription();
			} else if (actionCommand.equalsIgnoreCase("box.description.add.sheet")) {
				addSelectedBoxSheet = comboBoxAddSheets.getSelectedItem().toString();
				if(!addSelectedBoxSheet.equalsIgnoreCase("HNF")) {
					comboBoxAddFinishes.setEnabled(false);
					comboBoxAddColors.setEnabled(false);
				} else {
					comboBoxAddFinishes.setEnabled(true);
					comboBoxAddColors.setEnabled(true);
				}
			} else if (actionCommand.equalsIgnoreCase("box.description.add.nema")) {
				updateBoxTextAddDescription();
			} else if (actionCommand.equalsIgnoreCase("box.description.edit.type")) {
				editSelectedBoxType = comboBoxEditTypes.getSelectedItem().toString();
				if(!editSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
					textBoxEditPairs.setText("");
					textBoxEditPairs.setEnabled(false);
				} else {
					textBoxEditPairs.setEnabled(true);
				}
				if(!editSelectedBoxType.equalsIgnoreCase("de paso")) {
					comboBoxEditLockTypes.setEnabled(true);
				} else {
					comboBoxEditLockTypes.setSelectedIndex(-1);
					comboBoxEditLockTypes.setEnabled(false);
				}
			} else if (actionCommand.equalsIgnoreCase("box.description.edit.sheet")) {
				editSelectedBoxSheet = comboBoxEditSheets.getSelectedItem().toString();
				if(editSelectedBoxSheet.equalsIgnoreCase("Acero Inoxidable")) {
					comboBoxEditFinishes.setSelectedItem("Acero Inoxidable");
				} else if (editSelectedBoxSheet.equalsIgnoreCase("Galvanizada")) {
					comboBoxEditFinishes.setSelectedItem("Galvanizado");
				} else if (editSelectedBoxSheet.equalsIgnoreCase("Aluminizada")) {
					comboBoxEditFinishes.setSelectedItem("Aluminizado");
				}
				if(!editSelectedBoxSheet.equalsIgnoreCase("HNF")) {
					comboBoxEditFinishes.setEnabled(false);
					comboBoxEditColors.setEnabled(false);
					comboBoxEditColors.setSelectedIndex(-1);
				} else {
					comboBoxEditFinishes.setSelectedItem("Pintado");
					comboBoxEditFinishes.setEnabled(true);
					comboBoxEditColors.setEnabled(true);
				}
			} else if (actionCommand.equalsIgnoreCase("board.search.type")) {
				searchSelectedBoardType = comboBoardTypes.getSelectedItem().toString();
				this.clearSelectedBoardOptions("type");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.BOARD_TYPES_TABLE;
				if(!searchSelectedBoardType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TYPES_TABLE + ".type = '"+searchSelectedBoardType+"' ";
				}
				this.loadComboBoard("types");
			} else if (actionCommand.equalsIgnoreCase("board.search.installation")) {
				searchSelectedBoardInstallation = comboBoardInstallations.getSelectedItem().toString();
				this.clearSelectedBoardOptions("installation");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.INSTALLATIONS_TABLE;
				if(!searchSelectedBoardInstallation.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.INSTALLATIONS_TABLE + ".installation = '"+searchSelectedBoardInstallation+"' ";
				}
				this.loadComboBoard("installations");
			} else if (actionCommand.equalsIgnoreCase("board.search.nema")) {
				searchSelectedBoardNema = comboBoardNemas.getSelectedItem().toString();
				this.clearSelectedBoardOptions("nema");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.NEMAS_TABLE;
				if(!searchSelectedBoardNema.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.NEMAS_TABLE + ".nema = '"+searchSelectedBoardNema+"' ";
				}
				this.loadComboBoard("nemas");
			} else if (actionCommand.equalsIgnoreCase("board.search.bar_capacity")) {
				searchSelectedBoardBarCapacity = comboBoardBarCapacities.getSelectedItem().toString();
				this.clearSelectedBoardOptions("bar_capacity");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.BOARD_BAR_CAPACITIES_TABLE;
				if(!searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '"+searchSelectedBoardBarCapacity+"' ";
				}
				this.loadComboBoard("bar_capacities");
			} else if (actionCommand.equalsIgnoreCase("board.search.bar_type")) {
				searchSelectedBoardBarType = comboBoardBarTypes.getSelectedItem().toString();
				this.clearSelectedBoardOptions("bar_type");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.BOARD_BAR_TYPES_TABLE;
				if(!searchSelectedBoardBarType.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_BAR_TYPES_TABLE + ".bar_type = '"+searchSelectedBoardBarType+"' ";
				}
				this.loadComboBoard("bar_types");
			} else if (actionCommand.equalsIgnoreCase("board.search.circuits")) {
				searchSelectedBoardCircuits = comboBoardCircuits.getSelectedItem().toString();
				this.clearSelectedBoardOptions("circuits");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.BOARD_CIRCUITS_TABLE;
				if(!searchSelectedBoardCircuits.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_CIRCUITS_TABLE + ".circuits = '"+searchSelectedBoardCircuits+"' ";
				}
				this.loadComboBoard("circuits");
			} else if (actionCommand.equalsIgnoreCase("board.search.voltage")) {
				searchSelectedBoardVoltage = comboBoardVoltages.getSelectedItem().toString();
				this.clearSelectedBoardOptions("voltage");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.BOARD_VOLTAGES_TABLE;
				if(!searchSelectedBoardVoltage.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_VOLTAGES_TABLE + ".voltage = '"+searchSelectedBoardVoltage+"' ";
				}
				this.loadComboBoard("voltages");
			} else if (actionCommand.equalsIgnoreCase("board.search.phases")) {
					searchSelectedBoardPhases = comboBoardPhases.getSelectedItem().toString();
					this.clearSelectedBoardOptions("phases");
					fromQuery = MainView.BOARD_TABLE;
					if(!searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".phases = '"+searchSelectedBoardPhases+"' ";
					}
					this.loadComboBoard("phases");
			} else if (actionCommand.equalsIgnoreCase("board.search.ground")) {
				searchSelectedBoardGround = comboBoardGround.getSelectedItem().toString();
				this.clearSelectedBoardOptions("ground");
				fromQuery = MainView.BOARD_TABLE;
				this.loadComboBoard("ground");
			} else if (actionCommand.equalsIgnoreCase("board.search.interruption")) {
				searchSelectedBoardInterruption = comboBoardInterruptions.getSelectedItem().toString();
				this.clearSelectedBoardOptions("interruption");
				fromQuery = MainView.BOARD_TABLE + "," + MainView.INTERRUPTIONS_TABLE;
				if(!searchSelectedBoardInterruption.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.INTERRUPTIONS_TABLE + ".interruption = '"+searchSelectedBoardInterruption+"' ";
				}
				this.loadComboBoard("interruptions");
			} else if (actionCommand.equalsIgnoreCase("board.description.add.type") || 
					actionCommand.equalsIgnoreCase("board.description.add.nema") || 
					actionCommand.equalsIgnoreCase("board.description.add.circuits") || 
					actionCommand.equalsIgnoreCase("board.description.add.phases")) {
				updateBoardTextAddDescription();
			}
		}
		
		private void clearSelectedSwitchOptions(String start) {
			switch(start) {
				case "type":
					searchSelectedSwitchType = "";
				case "phases":
					searchSelectedSwitchPhases = "";
				case "current":
					searchSelectedSwitchCurrent = "";
				case "interruption":
					searchSelectedSwitchInterruption = "";
					break;
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
		
		private String getSelectWhereQuery(String whereQuery) {
			if(whereQuery.isEmpty()) {
				whereQuery = " WHERE ";
			} else {
				whereQuery += " AND ";
			}
			return whereQuery;
		}
		
		private void addSwitchCommonQuery() {
			fromQuery += ", currents, interruptions ";
			this.selectWhereQuery();
			whereQuery += " switches.current_id = currents.id ";
			this.selectWhereQuery();
			whereQuery += " interruptions.id = switches.interruption_id ";
		}
		
		private void switchPhasesQuery() {
			if(null != searchSelectedSwitchPhases && !searchSelectedSwitchPhases.equalsIgnoreCase("Todas") && 
					!searchSelectedSwitchPhases.isEmpty()) {
				this.selectWhereQuery();
				whereQuery += " switches.phases = '" + searchSelectedSwitchPhases + "' ";
			} else {
				fromQuery = "switches ";
				whereQuery = "";
			}
		}
		
		private void switchBrandQuery() {
			if(null != searchSelectedSwitchBrand && !searchSelectedSwitchBrand.equalsIgnoreCase("Todas") && 
					!searchSelectedSwitchBrand.isEmpty()) {
				this.selectWhereQuery();
				fromQuery += ", switch_brands ";
				whereQuery += " switches.brand_id = switch_brands.id "
								+ "AND switch_brands.brand = '" + searchSelectedSwitchBrand + "' ";
				if(!searchSelectedSwitchType.equalsIgnoreCase("Todas")) {
					if(!fromQuery.contains("switch_types")) {
						fromQuery += ", switch_types ";
					}
					whereQuery += "AND switch_types.brand_id = switch_brands.id "
								+ "AND switch_types.id = switches.type_id ";
				}
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
		
		private void loadComboSwitch(String start) {
			switch(start) {
				case "types":
					comboSwitchTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("switch_types", "type"))));
				case "phases":
					comboSwitchPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("switches", "phases"))));
				case "currents":
					comboSwitchCurrents.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("currents", "current"))));
				case "interruptions":
					comboSwitchInterruptions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("interruptions", "interruption"))));
					break;
			}
		}
		
		private void loadComboBox(String start) {
			switch(start) {
				case "types":
					this.selectWhereQuery();
					fromQuery += "," + MainView.INSTALLATIONS_TABLE;
					whereQuery += MainView.BOXES_TABLE + ".type_id = " + MainView.BOX_TYPES_TABLE + ".id";
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id ";
					comboBoxInstallations.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.INSTALLATIONS_TABLE, "installation"))));
				case "installations":
					fromQuery += "," + MainView.NEMAS_TABLE;
					if(!searchSelectedBoxInstallation.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("installation");
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id ";
					comboBoxNemas.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.NEMAS_TABLE, "nema"))));
				case "nemas":
					if (!searchSelectedBoxNema.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("nema");
					if(searchSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos")) {
						this.selectWhereQuery();
						whereQuery += "boxes.pairs > 0";
						comboBoxPairs.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOXES_TABLE, "pairs"))));
					} else if(((!searchSelectedBoxInstallation.equalsIgnoreCase("Todas") ||
							searchSelectedBoxInstallation.isEmpty()) ||
							!searchSelectedBoxNema.equalsIgnoreCase("Todas")) && 
							(searchSelectedBoxType.equalsIgnoreCase("Todas") ||
									searchSelectedBoxType.isEmpty())){
						String tempWhereQuery = this.getSelectWhereQuery(whereQuery) + "boxes.pairs > 0";
						comboBoxPairs.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOXES_TABLE, "pairs", tempWhereQuery))));
					}
				case "pairs":
					fromQuery += "," + MainView.BOX_SHEETS_TABLE;
					if(!searchSelectedBoxPairs.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "' ";
					}
					this.addBoxAdditionalWhere("pairs");
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id ";
					comboBoxSheets.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOX_SHEETS_TABLE, "sheet"))));
				case "sheets":
					fromQuery += "," + MainView.BOX_FINISHES_TABLE;
					if(!searchSelectedBoxSheet.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("sheet");
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".finish_id = " + MainView.BOX_FINISHES_TABLE + ".id ";
					comboBoxFinishes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOX_FINISHES_TABLE, "finish"))));
				case "finishes":
					fromQuery += "," + MainView.BOX_COLORS_TABLE;
					if(!searchSelectedBoxFinish.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".finish_id = " + MainView.BOX_FINISHES_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("finish");
					this.selectWhereQuery();
					whereQuery += "((boxes.color_id > 0 "
							+ "AND boxes.color_id = box_colors.id) "
							+ "OR boxes.color_id = 0)";
					String tempWhereQuery = this.getSelectWhereQuery(whereQuery) + MainView.BOXES_TABLE + ".color_id = " + MainView.BOX_COLORS_TABLE + ".id";
					comboBoxColors.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOX_COLORS_TABLE, "color", tempWhereQuery))));
				case "colors":
					fromQuery += "," + MainView.BOX_CALIBERS_TABLE;
					if(!searchSelectedBoxColor.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += "((boxes.color_id > 0 "
								+ "AND boxes.color_id = box_colors.id) "
								+ "OR boxes.color_id = 0)";
					}
					this.addBoxAdditionalWhere("color");
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".caliber_id = " + MainView.BOX_CALIBERS_TABLE + ".id ";
					comboBoxCalibers.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOX_CALIBERS_TABLE, "caliber"))));
				case "calibers":
					fromQuery += "," + MainView.LOCK_TYPES_TABLE;
					if(!searchSelectedBoxCaliber.isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".caliber_id = " + MainView.BOX_CALIBERS_TABLE + ".id";
					}
					this.addBoxAdditionalWhere("caliber");
					this.selectWhereQuery();
					whereQuery += MainView.BOXES_TABLE + ".lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id ";
					comboBoxLockTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.LOCK_TYPES_TABLE, "lock_type"))));
					break;
			}
		}
		
		private void addBoxAdditionalWhere(String start) {
			//Add this to the filters to check if any option has been selected before and add it to the whereQuery
			//Need to fix this to add the filters backward, NOT ADDING YET WELL
			switch(start) {
				case "caliber":
					if(!fromQuery.contains(MainView.BOX_COLORS_TABLE)) {
						fromQuery += "," + MainView.BOX_COLORS_TABLE;
					}
					if(!searchSelectedBoxColor.isEmpty() && !searchSelectedBoxColor.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOX_COLORS_TABLE + ".color = '" + searchSelectedBoxColor + "'";
						this.selectWhereQuery();
						whereQuery += "((boxes.color_id > 0 "
								+ "AND boxes.color_id = box_colors.id) "
								+ "OR boxes.color_id = 0)";
					}
				case "color":
					if(!fromQuery.contains(MainView.BOX_FINISHES_TABLE)) {
						fromQuery += "," + MainView.BOX_FINISHES_TABLE;
					}
					if(!searchSelectedBoxFinish.isEmpty() && !searchSelectedBoxFinish.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOX_FINISHES_TABLE + ".finish = '" + searchSelectedBoxFinish + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".finish_id = " + MainView.BOX_FINISHES_TABLE + ".id";
					}
				case "finish":
					if(!fromQuery.contains(MainView.BOX_SHEETS_TABLE)) {
						fromQuery += "," + MainView.BOX_SHEETS_TABLE;
					}
					if(!searchSelectedBoxSheet.isEmpty() && !searchSelectedBoxSheet.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOX_SHEETS_TABLE + ".sheet = '" + searchSelectedBoxSheet + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id";
					}
				case "sheet":
					if(!fromQuery.contains(MainView.BOXES_TABLE)) {
						fromQuery += "," + MainView.BOXES_TABLE;
					}
					if(!searchSelectedBoxPairs.isEmpty() && !searchSelectedBoxPairs.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "' ";
					}
				case "pairs":
					if(!fromQuery.contains(MainView.NEMAS_TABLE)) {
						fromQuery += "," + MainView.NEMAS_TABLE;
					}
					if (!searchSelectedBoxNema.isEmpty() && !searchSelectedBoxNema.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.NEMAS_TABLE + ".nema = '" + searchSelectedBoxNema + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id";
					}
				case "nema":
					if(!fromQuery.contains(MainView.INSTALLATIONS_TABLE)) {
						fromQuery += "," + MainView.INSTALLATIONS_TABLE;
					}
					if(!searchSelectedBoxInstallation.isEmpty() && !searchSelectedBoxInstallation.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoxInstallation + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id";
					}
				case "installation":
					if(!fromQuery.contains(MainView.BOX_TYPES_TABLE)) {
						fromQuery += "," + MainView.BOX_TYPES_TABLE;
					}
					if(!searchSelectedBoxType.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOX_TYPES_TABLE + ".type = '" + searchSelectedBoxType + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOXES_TABLE + ".type_id = " + MainView.BOX_TYPES_TABLE + ".id";
					}
					break;
			}
		}
		
		private void addBoardAdditionalWhere(String start) {
			switch(start) {
				case "lock_type":
					if(!fromQuery.contains(MainView.INTERRUPTIONS_TABLE)) {
						fromQuery += "," + MainView.INTERRUPTIONS_TABLE;
					}
					if(!searchSelectedBoardInterruption.isEmpty() && !searchSelectedBoardInterruption.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.INTERRUPTIONS_TABLE + ".interruption = '" + searchSelectedBoardInterruption + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".interruption_id = " + MainView.INTERRUPTIONS_TABLE + ".id";
					}
				case "interruption":
					if(!fromQuery.contains(MainView.BOARD_TABLE)) {
						fromQuery += "," + MainView.BOARD_TABLE;
					}
					if(!searchSelectedBoardGround.isEmpty() && !searchSelectedBoardGround.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".ground = '" + ((searchSelectedBoardGround.equalsIgnoreCase("SI"))?"1":"0") + "'";
					}
				case "ground":
					if(!fromQuery.contains(MainView.BOARD_TABLE)) {
						fromQuery += "," + MainView.BOARD_TABLE;
					}
					if(!searchSelectedBoardPhases.isEmpty() && !searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "'";
					}
				case "phases":
					if(!fromQuery.contains(MainView.BOARD_VOLTAGES_TABLE)) {
						fromQuery += "," + MainView.BOARD_VOLTAGES_TABLE;
					}
					if(!searchSelectedBoardVoltage.isEmpty() && !searchSelectedBoardVoltage.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_VOLTAGES_TABLE + ".voltage = '" + searchSelectedBoardVoltage + "' ";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".voltage_id = " + MainView.BOARD_VOLTAGES_TABLE + ".id";
					}
				case "voltage":
					if(!fromQuery.contains(MainView.BOARD_CIRCUITS_TABLE)) {
						fromQuery += "," + MainView.BOARD_CIRCUITS_TABLE;
					}
					if (!searchSelectedBoardCircuits.isEmpty() && !searchSelectedBoardCircuits.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_CIRCUITS_TABLE + ".circuits = '" + searchSelectedBoardCircuits + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".circuits_id = " + MainView.BOARD_CIRCUITS_TABLE + ".id";
					}
				case "circuits":
					if(!fromQuery.contains(MainView.BOARD_BAR_TYPES_TABLE)) {
						fromQuery += "," + MainView.BOARD_BAR_TYPES_TABLE;
					}
					if (!searchSelectedBoardBarType.isEmpty() && !searchSelectedBoardBarType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_BAR_TYPES_TABLE + ".bar_type = '" + searchSelectedBoardBarType + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".bar_type_id = " + MainView.BOARD_BAR_TYPES_TABLE + ".id";
					}
				case "bar_type":
					if(!fromQuery.contains(MainView.BOARD_BAR_CAPACITIES_TABLE)) {
						fromQuery += "," + MainView.BOARD_BAR_CAPACITIES_TABLE;
					}
					if (!searchSelectedBoardBarCapacity.isEmpty() && !searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '" + searchSelectedBoardBarCapacity + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".bar_capacity_id = " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id";
					}
				case "bar_capacity":
					if(!fromQuery.contains(MainView.NEMAS_TABLE)) {
						fromQuery += "," + MainView.NEMAS_TABLE;
					}
					if (!searchSelectedBoardNema.isEmpty() && !searchSelectedBoardNema.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.NEMAS_TABLE + ".nema = '" + searchSelectedBoardNema + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id";
					}
				case "nema":
					if(!fromQuery.contains(MainView.INSTALLATIONS_TABLE)) {
						fromQuery += "," + MainView.INSTALLATIONS_TABLE;
					}
					if(!searchSelectedBoardInstallation.isEmpty() && !searchSelectedBoardInstallation.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoardInstallation + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id";
					}
				case "installation":
					if(!fromQuery.contains(MainView.BOARD_TYPES_TABLE)) {
						fromQuery += "," + MainView.BOARD_TYPES_TABLE;
					}
					if(!searchSelectedBoardType.isEmpty() && !searchSelectedBoardType.equalsIgnoreCase("Todas")) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TYPES_TABLE + ".type = '" + searchSelectedBoardType + "'";
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".type_id = " + MainView.BOARD_TYPES_TABLE + ".id";
					}
					if(null != textBoardSearchNames && !textBoardSearchNames.getText().isEmpty()) {
						this.selectWhereQuery();
						whereQuery += MainView.BOARD_TABLE + ".name LIKE '%" + textBoardSearchNames.getText() + "%'";
					}
					break;
			}
		}
		
		private void loadComboBoard(String start) {
			switch(start) {
			case "types":
				this.selectWhereQuery();
				fromQuery += "," + MainView.INSTALLATIONS_TABLE;
				whereQuery += MainView.BOARD_TABLE + ".type_id = " + MainView.BOARD_TYPES_TABLE + ".id";
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id ";
				comboBoardInstallations.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.INSTALLATIONS_TABLE, "installation"))));
			case "installations":
				fromQuery += "," + MainView.NEMAS_TABLE;
				if(!searchSelectedBoardInstallation.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("installation");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id ";
				comboBoardNemas.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.NEMAS_TABLE, "nema"))));
			case "nemas":
				fromQuery += "," + MainView.BOARD_BAR_CAPACITIES_TABLE;
				if(!searchSelectedBoardNema.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("nema");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".bar_capacity_id = " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id ";
				comboBoardBarCapacities.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_BAR_CAPACITIES_TABLE, "bar_capacity"))));
			case "bar_capacity":
				fromQuery += "," + MainView.BOARD_BAR_TYPES_TABLE;
				if(!searchSelectedBoardBarCapacity.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".bar_capacity_id = " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("bar_capacity");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".bar_type_id = " + MainView.BOARD_BAR_TYPES_TABLE + ".id ";
				comboBoardBarTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_BAR_TYPES_TABLE, "bar_type"))));
			case "bar_type":
				fromQuery += "," + MainView.BOARD_CIRCUITS_TABLE;
				if(!searchSelectedBoardBarType.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".bar_type_id = " + MainView.BOARD_BAR_TYPES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("bar_type");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".circuits_id = " + MainView.BOARD_CIRCUITS_TABLE + ".id ";
				comboBoardCircuits.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_CIRCUITS_TABLE, "circuits"))));
			case "circuits":
				fromQuery += "," + MainView.BOARD_VOLTAGES_TABLE;
				if(!searchSelectedBoardCircuits.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".circuits_id = " + MainView.BOARD_CIRCUITS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("circuits");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".voltage_id = " + MainView.BOARD_VOLTAGES_TABLE + ".id ";
				comboBoardVoltages.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_VOLTAGES_TABLE, "voltage"))));
			case "voltage":
				if(!fromQuery.contains(MainView.BOARD_TABLE)) {
					fromQuery += "," + MainView.BOARD_TABLE;
				}
				if(!searchSelectedBoardVoltage.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".voltage_id = " + MainView.BOARD_VOLTAGES_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("voltage");
				comboBoardPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_TABLE, "phases"))));
			case "phases":
				if(!fromQuery.contains(MainView.BOARD_TABLE)) {
					fromQuery += "," + MainView.BOARD_TABLE;
				}
				if(!searchSelectedBoardPhases.isEmpty() && !searchSelectedBoardPhases.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "' ";
				}
				this.addBoardAdditionalWhere("phases");
				comboBoardGround.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.BOARD_TABLE, "ground", MainView.BOARD_GROUND_FIELD, true))));
			case "ground":
				fromQuery += "," + MainView.INTERRUPTIONS_TABLE;
				if(!searchSelectedBoardGround.isEmpty() && !searchSelectedBoardGround.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".ground = '" + (searchSelectedBoardGround.equalsIgnoreCase("SI")?"1":"0") + "'";
				}
				this.addBoardAdditionalWhere("ground");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".interruption_id = " + MainView.INTERRUPTIONS_TABLE + ".id ";
				comboBoardInterruptions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.INTERRUPTIONS_TABLE, "interruption"))));
			case "interruption":
				fromQuery += "," + MainView.LOCK_TYPES_TABLE;
				if(!searchSelectedBoardInterruption.isEmpty()) {
					this.selectWhereQuery();
					whereQuery += MainView.BOARD_TABLE + ".interruption_id = " + MainView.INTERRUPTIONS_TABLE + ".id";
				}
				this.addBoardAdditionalWhere("interruption");
				this.selectWhereQuery();
				whereQuery += MainView.BOARD_TABLE + ".lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id ";
				comboBoardLockTypes.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList(MainView.LOCK_TYPES_TABLE, "lock_type"))));
				break;
			}
		}
		
	}
	
	private class SearchButtonListener implements ActionListener {
		
		String whereQuery = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			if(actionCommand.equalsIgnoreCase("switch.search.description.copy")) {
				StringSelection textSelection = new StringSelection(textSwitchDescription.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(textSelection, null);
				labelSwitchCopy.setText("Copiado!");
				int delay = 3000;
				Timer timer = new Timer(delay, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						labelSwitchCopy.setText("");
					}
					
				});
				timer.start();
			} else if(actionCommand.equalsIgnoreCase("box.search.description.copy")) {
				StringSelection textSelection = new StringSelection(textBoxDescription.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(textSelection, null);
				labelBoxCopy.setText("Copiado!");
				int delay = 3000;
				Timer timer = new Timer(delay, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						labelBoxCopy.setText("");
					}
					
				});
				timer.start();
			} else if(actionCommand.equalsIgnoreCase("switch.search.bar.button")) {
				whereQuery = "";
				if(searchSelectedSwitchBrand != null && !searchSelectedSwitchBrand.equalsIgnoreCase("Todas") &&
						!searchSelectedSwitchBrand.isEmpty()) {
					whereQuery += " AND switch_brands.brand = '" + searchSelectedSwitchBrand + "'";
				}
				if(searchSelectedSwitchType != null && !searchSelectedSwitchType.equalsIgnoreCase("Todas") &&
						!searchSelectedSwitchType.isEmpty()) {
					whereQuery += " AND switch_types.type = '" + searchSelectedSwitchType + "'";
				}
				if(searchSelectedSwitchPhases != null && !searchSelectedSwitchPhases.equalsIgnoreCase("Todas") &&
						!searchSelectedSwitchPhases.isEmpty()) {
					whereQuery += " AND switches.phases = '" + searchSelectedSwitchPhases + "'";
				}
				if(searchSelectedSwitchCurrent != null && !searchSelectedSwitchCurrent.equalsIgnoreCase("Todas") &&
						!searchSelectedSwitchCurrent.isEmpty()) {
					whereQuery += " AND currents.current = '" + searchSelectedSwitchCurrent + "'";
				}
				if(searchSelectedSwitchInterruption != null && !searchSelectedSwitchInterruption.equalsIgnoreCase("Todas") &&
						!searchSelectedSwitchInterruption.isEmpty()) {
					whereQuery += " AND interruptions.interruption = '" + searchSelectedSwitchInterruption + "'";
				}
				
				loadSwitchTable(whereQuery);
			} else if (actionCommand.equalsIgnoreCase("box.search.bar.button")) {
				whereQuery = "";
				searchSelectedBoxHeight = textBoxHeight.getText();
				searchSelectedBoxWidth = textBoxWidth.getText();
				searchSelectedBoxDepth = textBoxDepth.getText();
				if(searchSelectedBoxType != null && !searchSelectedBoxType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxType.isEmpty()) {
					whereQuery += " AND " + MainView.BOX_TYPES_TABLE + ".type = '" + searchSelectedBoxType + "'";
				}
				if(searchSelectedBoxInstallation != null && !searchSelectedBoxInstallation.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxInstallation.isEmpty()) {
					whereQuery += " AND boxes.installation_id = installations.id ";
					whereQuery += " AND " + MainView.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoxInstallation + "'";
				}
				if(searchSelectedBoxNema != null && !searchSelectedBoxNema.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxNema.isEmpty()) {
					whereQuery += " AND " + MainView.NEMAS_TABLE + ".nema = '" + searchSelectedBoxNema + "'";
				}
				if(searchSelectedBoxPairs != null && !searchSelectedBoxPairs.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxPairs.isEmpty() && (searchSelectedBoxType.equalsIgnoreCase("Todas") ||
								searchSelectedBoxType.equalsIgnoreCase("para Pares Telefonicos") ||
								searchSelectedBoxType.isEmpty())) {
					whereQuery += " AND " + MainView.BOXES_TABLE + ".pairs = '" + searchSelectedBoxPairs + "'";
				}
				if(searchSelectedBoxSheet != null && !searchSelectedBoxSheet.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxSheet.isEmpty()) {
					whereQuery += " AND " + MainView.BOX_SHEETS_TABLE + ".id = " + MainView.BOXES_TABLE + ".sheet_id";
					whereQuery += " AND " + MainView.BOX_SHEETS_TABLE + ".sheet = '" + searchSelectedBoxSheet + "'";
				} else {
					whereQuery += "AND ( (boxes.sheet_id > 0 "
							+ "AND boxes.sheet_id = " + MainView.BOX_SHEETS_TABLE + ".id) "
							+ "OR boxes.sheet_id = 0)";
				}
				if(searchSelectedBoxFinish != null && !searchSelectedBoxFinish.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxFinish.isEmpty()) {
					whereQuery += " AND " + MainView.BOX_FINISHES_TABLE + ".id = " + MainView.BOXES_TABLE + ".finish_id";
					whereQuery += " AND " + MainView.BOX_FINISHES_TABLE + ".finish = '" + searchSelectedBoxFinish + "'";
				} else {
					whereQuery += "AND ( (boxes.finish_id > 0 "
							+ "AND boxes.finish_id = " + MainView.BOX_FINISHES_TABLE + ".id) "
							+ "OR boxes.finish_id = 0)";
				}
				if(searchSelectedBoxColor != null && !searchSelectedBoxColor.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxColor.isEmpty()) {
					whereQuery += " AND " + MainView.BOX_COLORS_TABLE + ".id = " + MainView.BOXES_TABLE + ".color_id";
					whereQuery += " AND " + MainView.BOX_COLORS_TABLE + ".color = '" + searchSelectedBoxColor + "'";
				} else {
					whereQuery += "AND ( (boxes.color_id > 0 "
							+ "AND boxes.color_id = " + MainView.BOX_COLORS_TABLE + ".id) "
							+ "OR boxes.color_id = 0)";
				}
				if(searchSelectedBoxHeight != null && !searchSelectedBoxHeight.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxHeight.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
					whereQuery += " AND " + MainView.BOXES_TABLE + ".height = '" + searchSelectedBoxHeight + "'";
				}
				if(searchSelectedBoxWidth != null && !searchSelectedBoxWidth.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxWidth.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
					whereQuery += " AND " + MainView.BOXES_TABLE + ".width = '" + searchSelectedBoxWidth + "'";
				}
				if(searchSelectedBoxDepth != null && !searchSelectedBoxDepth.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxDepth.isEmpty() && !searchSelectedBoxType.equalsIgnoreCase("para Tablero")) {
					whereQuery += " AND " + MainView.BOXES_TABLE + ".depth = '" + searchSelectedBoxDepth + "'";
				}
				whereQuery += "AND ( (boxes.units_id > 0 "
						+ "AND boxes.units_id = " + MainView.BOX_UNITS_TABLE + ".id) "
						+ "OR boxes.units_id = 0)";
				if(searchSelectedBoxCaliber != null && !searchSelectedBoxCaliber.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxCaliber.isEmpty()) {
					whereQuery += " AND " + MainView.BOX_CALIBERS_TABLE + ".id = " + MainView.BOXES_TABLE + ".caliber_id";
					whereQuery += " AND " + MainView.BOX_CALIBERS_TABLE + ".caliber = '" + searchSelectedBoxCaliber + "'";
				} else {
					whereQuery += "AND ( (boxes.caliber_id > 0 "
							+ "AND boxes.caliber_id = " + MainView.BOX_CALIBERS_TABLE + ".id) "
							+ "OR boxes.caliber_id = 0)";
				}
				if(searchSelectedBoxLockType != null && !searchSelectedBoxLockType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoxLockType.isEmpty()) {
					whereQuery += " AND boxes.lock_type_id = lock_types.id ";
					whereQuery += "AND " + MainView.LOCK_TYPES_TABLE + ".lock_type = '" + searchSelectedBoxLockType + "'";
				} else {
					whereQuery += "AND ( (boxes.lock_type_id > 0 "
							+ "AND boxes.lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id) "
							+ "OR boxes.lock_type_id = 0)";
				}
				
				loadBoxTable(whereQuery);
			} else if (actionCommand.equalsIgnoreCase("board.search.bar.button")) {
				whereQuery = "";
				if(null != textBoardSearchNames && !textBoardSearchNames.getText().isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".name LIKE '%" + textBoardSearchNames.getText() + "%'";
				}
				if(null != searchSelectedBoardType  && !searchSelectedBoardType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TYPES_TABLE + ".type = '" + searchSelectedBoardType + "'";
				}
				if(searchSelectedBoardInstallation != null && !searchSelectedBoardInstallation.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardInstallation.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".installation_id = " + MainView.INSTALLATIONS_TABLE + ".id ";
					whereQuery += " AND " + MainView.INSTALLATIONS_TABLE + ".installation = '" + searchSelectedBoardInstallation + "'";
				}
				if(searchSelectedBoardNema != null && !searchSelectedBoardNema.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardNema.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".nema_id = " + MainView.NEMAS_TABLE + ".id ";
					whereQuery += " AND " + MainView.NEMAS_TABLE + ".nema = '" + searchSelectedBoardNema + "'";
				}
				if(searchSelectedBoardBarCapacity != null && !searchSelectedBoardBarCapacity.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardBarCapacity.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".bar_capacity_id = " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_BAR_CAPACITIES_TABLE + ".bar_capacity = '" + searchSelectedBoardBarCapacity + "'";
				}
				if(searchSelectedBoardBarType != null && !searchSelectedBoardBarType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardBarType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".bar_type_id = " + MainView.BOARD_BAR_TYPES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_BAR_TYPES_TABLE + ".bar_type = '" + searchSelectedBoardBarType + "'";
				}
				if(searchSelectedBoardCircuits != null && !searchSelectedBoardCircuits.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardCircuits.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".circuits_id = " + MainView.BOARD_CIRCUITS_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_CIRCUITS_TABLE + ".circuits = '" + searchSelectedBoardCircuits + "'";
				}
				if(searchSelectedBoardVoltage != null && !searchSelectedBoardVoltage.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardVoltage.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".voltage_id = " + MainView.BOARD_VOLTAGES_TABLE + ".id ";
					whereQuery += " AND " + MainView.BOARD_VOLTAGES_TABLE + ".voltage = '" + searchSelectedBoardVoltage + "'";
				}
				if(searchSelectedBoardPhases != null && !searchSelectedBoardPhases.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardPhases.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".phases = '" + searchSelectedBoardPhases + "'";
				}
				if(searchSelectedBoardGround != null && !searchSelectedBoardGround.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardGround.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".ground = '" + (searchSelectedBoardGround.equalsIgnoreCase("SI")?"1":"0") + "'";
				}
				if(searchSelectedBoardInterruption != null && !searchSelectedBoardInterruption.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardInterruption.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".interruption_id = " + MainView.INTERRUPTIONS_TABLE + ".id ";
					whereQuery += " AND " + MainView.INTERRUPTIONS_TABLE + ".interruption = '" + searchSelectedBoardInterruption + "'";
				}
				if(searchSelectedBoardLockType != null && !searchSelectedBoardLockType.equalsIgnoreCase("Todas") &&
						!searchSelectedBoardLockType.isEmpty()) {
					whereQuery += " AND " + MainView.BOARD_TABLE + ".lock_type_id = " + MainView.LOCK_TYPES_TABLE + ".id ";
					whereQuery += " AND " + MainView.LOCK_TYPES_TABLE + ".lock_type = '" + searchSelectedBoardLockType + "'";
				}
				loadBoardTable(whereQuery);
				textBoardDescriptionName.setText("");
				textBoardDescriptionType.setText("");
				textBoardDescriptionInstallation.setText("");
				textBoardDescriptionNema.setText("");
				textBoardDescriptionBarCapacity.setText("");
				textBoardDescriptionBarType.setText("");
				textBoardDescriptionCircuits.setText("");
				textBoardDescriptionVoltage.setText("");
				textBoardDescriptionPhases.setText("");
				textBoardDescriptionGround.setText("");
				textBoardDescriptionInterruption.setText("");
				textBoardDescriptionLockType.setText("");
				textBoardDescriptionPrice.setText("");
				textBoardDescription.setText("");
				buttonBoardAdd.setEnabled(true);
				buttonBoardEdit.setEnabled(false);
			}
		}
		
	}
	
	public class SharedListSelectionListener implements ListSelectionListener {
		
		public static final int SWITCH_ID_COLUMN = 0;
		public static final int SWITCH_MODEL_COLUMN = 1;
		public static final int SWITCH_BRAND_COLUMN = 2;
		public static final int SWITCH_TYPE_COLUMN = 3;
		public static final int SWITCH_PHASES_COLUMN = 4;
		public static final int SWITCH_CURRENT_COLUMN = 5;
		public static final int SWITCH_INTERRUPTION_COLUMN = 6;
		public static final int SWITCH_VOLTAGE_COLUMN = 7;
		public static final int SWITCH_PRICE_COLUMN = 8;
		
		public static final int BOX_ID_COLUMN = 0;
		public static final int BOX_TYPE_COLUMN = 1;
		public static final int BOX_INSTALLATION_COLUMN = 2;
		public static final int BOX_NEMA_COLUMN = 3;
		public static final int BOX_PAIRS_COLUMN = 4;
		public static final int BOX_SHEET_COLUMN = 5;
		public static final int BOX_FINISH_COLUMN = 6;
		public static final int BOX_COLOR_COLUMN = 7;
		public static final int BOX_HEIGHT_COLUMN = 8;
		public static final int BOX_WIDTH_COLUMN = 9;
		public static final int BOX_DEPTH_COLUMN = 10;
		public static final int BOX_UNITS_COLUMN = 11;
		public static final int BOX_CALIBER_COLUMN = 12;
		public static final int BOX_LOCK_TYPE_COLUMN = 13;
		public static final int BOX_PRICE_COLUMN = 14;
		
		public static final int BOARD_ID_COLUMN = 0;
		public static final int BOARD_NAME_COLUMN = 1;
		public static final int BOARD_TYPE_COLUMN = 2;
		public static final int BOARD_INSTALLATION_COLUMN = 3;
		public static final int BOARD_NEMA_COLUMN = 4;
		public static final int BOARD_BAR_CAPACITY_COLUMN = 5;
		public static final int BOARD_BAR_TYPE_COLUMN = 6;
		public static final int BOARD_CIRCUITS_COLUMN = 7;
		public static final int BOARD_VOLTAGE_COLUMN = 8;
		public static final int BOARD_PHASES_COLUMN = 9;
		public static final int BOARD_GROUND_COLUMN = 10;
		public static final int BOARD_INTERRUPTION_COLUMN = 11;
		public static final int BOARD_LOCK_TYPE_COLUMN = 12;
		public static final int BOARD_PRICE_COLUMN = 13;
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			
			if(null != tableSwitchesResult && tableSwitchesResult.isFocusOwner()) {
				switchTableSelectedIndex = lsm.getMinSelectionIndex();
				if(lsm.isSelectionEmpty()) {
					buttonSwitchEdit.setEnabled(false);
					textSwitchPrice.setText("");
					textSwitchPhases.setText("");
					textSwitchCurrent.setText("");
					textSwitchType.setText("");
					textSwitchBrand.setText("");
					textSwitchDescription.setText("");
				} else {
					if(panelSwitchDescription.isShowing()) {
						buttonSwitchEdit.setEnabled(true);
					}
					textSwitchPrice.setText("BsF " + tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_PRICE_COLUMN));
					textSwitchPhases.setText((String) tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_PHASES_COLUMN));
					textSwitchCurrent.setText((String) tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_CURRENT_COLUMN));
					textSwitchType.setText((String) tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_TYPE_COLUMN));
					textSwitchBrand.setText((String) tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_BRAND_COLUMN));
					textSwitchDescription.setText("Interruptor " +
											tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_PHASES_COLUMN) +
											"X" +
											tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_CURRENT_COLUMN) +
											" A, " +
											tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_TYPE_COLUMN) +
											", " +
											tableSwitchesResult.getValueAt(switchTableSelectedIndex, SWITCH_BRAND_COLUMN)
											);
				}
			} else if (null != tableBoxesResult && tableBoxesResult.isFocusOwner()) {
				boxTableSelectedIndex = lsm.getMinSelectionIndex();
				if(lsm.isSelectionEmpty()) {
					buttonBoxEdit.setEnabled(false);
					textBoxDescriptionType.setText("");
					textBoxDescriptionInstallation.setText("");
//					textBoxDescriptionBarCapacity.setText("");
//					textBoxDescriptionBarType.setText("");
					textBoxDescriptionNema.setText("");
//					textBoxDescriptionCircuits.setText("");
					textBoxDescriptionPairs.setText("");
					textBoxDescriptionSheet.setText("");
					textBoxDescriptionFinish.setText("");
					textBoxDescriptionColor.setText("");
					textBoxDescriptionHeight.setText("");
					textBoxDescriptionWidth.setText("");
					textBoxDescriptionDepth.setText("");
					textBoxDescriptionUnits.setText("");
					textBoxDescriptionCaliber.setText("");
					textBoxDescriptionLockType.setText("");
					textBoxDescriptionPrice.setText("");
					textBoxDescription.setText("");
				} else {
					if(panelBoxDescription.isShowing()) {
						buttonBoxEdit.setEnabled(true);
					}
					textBoxDescriptionType.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_TYPE_COLUMN));
					textBoxDescriptionInstallation.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_INSTALLATION_COLUMN));
					textBoxDescriptionNema.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_NEMA_COLUMN));
					textBoxDescriptionPairs.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_PAIRS_COLUMN));
					textBoxDescriptionSheet.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_SHEET_COLUMN));
					textBoxDescriptionFinish.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_FINISH_COLUMN));
					textBoxDescriptionColor.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_COLOR_COLUMN));
					textBoxDescriptionHeight.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_HEIGHT_COLUMN));
					textBoxDescriptionWidth.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_WIDTH_COLUMN));
					textBoxDescriptionDepth.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_DEPTH_COLUMN));
					textBoxDescriptionUnits.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_UNITS_COLUMN));
					textBoxDescriptionCaliber.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_CALIBER_COLUMN));
					textBoxDescriptionLockType.setText((String) tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_LOCK_TYPE_COLUMN));
					textBoxDescriptionPrice.setText("BsF " + tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_PRICE_COLUMN));
					textBoxDescription.setText("Caja " +
							tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_TYPE_COLUMN) +
							", " +
							tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_HEIGHT_COLUMN) +
							" x " +
							tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_WIDTH_COLUMN) +
							" x " +
							tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_DEPTH_COLUMN) +
							", " +
							tableBoxesResult.getValueAt(boxTableSelectedIndex, BOX_NEMA_COLUMN));
				}
			} else if (null != tableBoardsResult && tableBoardsResult.isFocusOwner() && lsm.getMinSelectionIndex() > -1) {
				boardsTableSelectedIndex = lsm.getMinSelectionIndex();
				String[] boardSwitchesColumnNames = { "Id", "Descripcion", "Cantidad", "Precio", "Total", "Principal"};
				if(lsm.isSelectionEmpty()) {
					buttonAddBoardSwitch.setEnabled(false);
					tableBoardSwitchesResult.setModel(new DefaultTableModel());
				} else {
					selectedBoardId = Integer.valueOf((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, 0));
					
					String boardSwitchesQuery = "SELECT board_switches.id, "
								+ " CONCAT('Interruptor ', boards.phases, 'X', currents.current, 'A,', switch_types.type, ',', switch_brands.brand) as description, "
								+ " board_switches.quantity, "
								+ " switches.price, "
								+ " (board_switches.quantity * switches.price) as total, "
								+ " IF(boards.main_switch_id=board_switches.id,'1','0') as principal "
							+ " FROM boards, board_switches, switches, switch_types, switch_brands, currents "
							+ " WHERE board_switches.board_container_id = " + selectedBoardId
							+ " AND boards.id = board_switches.board_container_id"
							+ " AND switches.id = board_switches.switch_id "
							+ " AND switches.current_id = currents.id"
							+ " AND switches.type_id = switch_types.id"
							+ " AND switches.brand_id = switch_brands.id";
					
					boardSwitchesData = db.fetchAllAddBoolean(db.select(boardSwitchesQuery), boardSwitchesColumnNames.length);
					
					if(boardSwitchesData.length > 0) {
						MyTableModel mForTable = new MyTableModel(boardSwitchesData, boardSwitchesColumnNames);
						tableBoardSwitchesResult.setModel(mForTable);
						if(null != tableBoardSwitchesResult && tableBoardSwitchesResult.getSelectedRow() == -1) {
							buttonRemoveBoardSwitch.setEnabled(false);
						}
					} else {
						tableBoardSwitchesResult.setModel(new DefaultTableModel());
						buttonRemoveBoardSwitch.setEnabled(false);
					}
					buttonAddBoardSwitch.setEnabled(true);
					if(lsm.isSelectionEmpty()) {
						buttonBoardEdit.setEnabled(false);
						textBoardDescriptionName.setText("");
						textBoardDescriptionType.setText("");
						textBoardDescriptionInstallation.setText("");
						textBoardDescriptionNema.setText("");
						textBoardDescriptionBarCapacity.setText("");
						textBoardDescriptionBarType.setText("");
						textBoardDescriptionCircuits.setText("");
						textBoardDescriptionVoltage.setText("");
						textBoardDescriptionPhases.setText("");
						textBoardDescriptionGround.setText("");
						textBoardDescriptionInterruption.setText("");
						textBoardDescriptionLockType.setText("");
						textBoardDescriptionPrice.setText("");
						textBoardDescription.setText("");
					} else {
						if(panelBoardDescription.isShowing()) {
							buttonBoardEdit.setEnabled(true);
						}
						textBoardDescriptionName.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_NAME_COLUMN));
						textBoardDescriptionType.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_TYPE_COLUMN));
						textBoardDescriptionInstallation.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_INSTALLATION_COLUMN));
						textBoardDescriptionNema.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_NEMA_COLUMN));
						textBoardDescriptionBarCapacity.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_BAR_CAPACITY_COLUMN));
						textBoardDescriptionBarType.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_BAR_TYPE_COLUMN));
						textBoardDescriptionCircuits.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_CIRCUITS_COLUMN));
						textBoardDescriptionVoltage.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_VOLTAGE_COLUMN));
						textBoardDescriptionPhases.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_PHASES_COLUMN));
						textBoardDescriptionGround.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_GROUND_COLUMN));
						textBoardDescriptionInterruption.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_INTERRUPTION_COLUMN));
						textBoardDescriptionLockType.setText((String) tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_LOCK_TYPE_COLUMN));
						textBoardDescriptionPrice.setText("BsF " + tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_PRICE_COLUMN));
						textBoardDescription.setText("Caja para Tablero, " +
								tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_TYPE_COLUMN) +
								", " +
								tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_PHASES_COLUMN) +
								", de " +
								tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_CIRCUITS_COLUMN) +
								" circuitos, " +
								tableBoardsResult.getValueAt(boardsTableSelectedIndex, BOARD_NEMA_COLUMN));
					}
				}
			} else if (null != tableBoardSwitchesResult && tableBoardSwitchesResult.isFocusOwner() && lsm.getMinSelectionIndex() > -1) {
				boardSwitchesTableSelectedIndex = lsm.getMinSelectionIndex();
				selectedBoardSwitchId = Integer.valueOf((String) tableBoardSwitchesResult.getValueAt(boardSwitchesTableSelectedIndex, BOARD_ID_COLUMN));
				buttonRemoveBoardSwitch.setEnabled(true);
			}
		}
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			if(actionCommand.equalsIgnoreCase("switch.search.buttons.add")) {
				setSwitchesMode(MainView.ADD_MODE);
			} else if (actionCommand.equalsIgnoreCase("switch.search.buttons.edit")) {
				setSwitchesMode(MainView.EDIT_MODE);
			} else if (actionCommand.equalsIgnoreCase("switch.description.add.save")) {
				if(textSwitchAddPrice.getText() == null || textSwitchAddPrice.getText().isEmpty() || !Numbers.isNumeric(textSwitchAddPrice.getText())) {
					JOptionPane.showMessageDialog(null, "Precio invalido");
					textSwitchAddPrice.setText("");
				} else if (textSwitchAddModel.getText() == null || textSwitchAddModel.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Debe escribir el modelo de interruptor");
				} else if (comboSwitchAddBrands.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay marcas disponibles, debe agregar una");
				} else if (comboSwitchAddTypes.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay tipos disponibles, debe agregar uno");
				} else if (comboSwitchAddPhases.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay fases disponibles, debe agregar una");
				} else if (comboSwitchAddCurrents.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay corrientes disponibles, debe agregar una");
				} else if (comboSwitchAddInterruptions.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay interrupciones disponibles, debe agregar una");
				} else if (comboSwitchAddVoltages.getItemCount() < 1) {
					JOptionPane.showMessageDialog(null, "No hay voltajes disponibles, debe agregar uno");
				} else {
					if( !db.switchExists(comboSwitchAddPhases.getSelectedItem().toString(), 
							comboSwitchAddCurrents.getSelectedItem().toString(), 
							comboSwitchAddBrands.getSelectedItem().toString(), 
							comboSwitchAddTypes.getSelectedItem().toString(), 
							comboSwitchAddInterruptions.getSelectedItem().toString(), 
							textSwitchAddModel.getText())) {
						if(db.addSwitch(textSwitchAddModel.getText(),
								comboSwitchAddBrands.getSelectedItem().toString(),
								comboSwitchAddTypes.getSelectedItem().toString(),
								comboSwitchAddPhases.getSelectedItem().toString(),
								Integer.valueOf(comboSwitchAddCurrents.getSelectedItem().toString()),
								comboSwitchAddVoltages.getSelectedItem().toString(),
								Integer.valueOf(comboSwitchAddInterruptions.getSelectedItem().toString()),
								textSwitchAddPrice.getText())) {
							JOptionPane.showMessageDialog(null, "Interruptor agregado exitosamente");
							setSwitchesMode(MainView.VIEW_MODE);
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el interruptor");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este interruptor ya existe");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.description.add.cancel") || e.getActionCommand().equalsIgnoreCase("description.edit.cancel")) {
				setSwitchesMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("switch.description.edit.save")) {
				ArrayList<Object> listFields = new ArrayList<Object>();
				ArrayList<Object> listValues = new ArrayList<Object>();
				int brandComboCount = comboSwitchEditBrand.getItemCount();
				int typeComboCount = comboSwitchEditType.getItemCount();
				if (brandComboCount > 0 && !editSwitchBrand.equals(comboSwitchEditBrand.getSelectedItem().toString())) {
					listFields.add("brand_id");
					listValues.add("'" + db.getSwitchBrandId(comboSwitchEditBrand.getSelectedItem().toString()) + "'");
				}
				if (typeComboCount > 0 && !editSwitchType.equals(comboSwitchEditType.getSelectedItem().toString())) {
					listFields.add("type_id");
					listValues.add("'" + db.getSwitchTypeId(comboSwitchEditType.getSelectedItem().toString()) + "'");
				}
				if (!editSwitchPhases.equals(comboSwitchEditPhases.getSelectedItem().toString())) {
					listFields.add("phases");
					listValues.add("'" + comboSwitchEditPhases.getSelectedItem().toString() + "'");
				}
				if (!String.valueOf(editSwitchCurrent).equals(comboSwitchEditCurrent.getSelectedItem().toString())) {
					listFields.add("current_id");
					listValues.add(db.getCurrentId(Integer.valueOf(comboSwitchEditCurrent.getSelectedItem().toString())));
				}
				if (!String.valueOf(editSwitchInterruption).equals(comboSwitchEditInterruption.getSelectedItem().toString())) {
					listFields.add("interruption_id");
					listValues.add(db.getInterruptionId(Integer.valueOf(comboSwitchEditInterruption.getSelectedItem().toString())));
				}
				if (!editSwitchVoltage.equals(comboSwitchEditVoltage.getSelectedItem().toString())) {
					listFields.add("voltage_id");
					listValues.add(db.getVoltageId(comboSwitchEditVoltage.getSelectedItem().toString()));
				}
				if (!editSwitchModel.equals(textSwitchEditModel.getText())) {
					listFields.add("model");
					listValues.add("'" + textSwitchEditModel.getText() + "'");
				}
				if (!String.valueOf(editSwitchPrice).equals(textSwitchEditPrice.getText())) {
					listFields.add("price");
					listValues.add("'" + textSwitchEditPrice.getText() + "'");
				}
				if(listFields.size() > 0 && listValues.size() > 0 && textSwitchEditPrice.getText() != null &&
						!textSwitchEditPrice.getText().isEmpty() && Numbers.isNumeric(textSwitchEditPrice.getText()) &&
						comboSwitchEditPhases.getItemCount() > 0 && comboSwitchEditCurrent.getItemCount() > 0 &&
						comboSwitchEditBrand.getItemCount() > 0 && comboSwitchEditType.getItemCount() > 0 &&
						comboSwitchEditInterruption.getItemCount() > 0 && textSwitchEditModel.getText() != null &&
						!textSwitchEditModel.getText().isEmpty() && comboSwitchEditVoltage.getItemCount() > 0) {
					boolean switchEdited = db.editSwitch(editSwitchId, listFields, listValues);
				if (switchEdited) {
					setSwitchesMode(MainView.VIEW_MODE);
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo actualizar el interruptor debido a un error");
				}
				} else {
					JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos");
				}
			} else if (actionCommand.equalsIgnoreCase("switch.description.edit.cancel")) {
				setSwitchesMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("box.description.buttons.add")) {
				setBoxesMode(MainView.ADD_MODE);
			} else if (actionCommand.equalsIgnoreCase("box.description.buttons.edit")) {
				setBoxesMode(MainView.EDIT_MODE);
			} else if (actionCommand.equalsIgnoreCase("box.description.add.save")) {
				String boxType = comboBoxAddTypes.getSelectedItem().toString();
				String boxInstallation = comboBoxAddInstallations.getSelectedItem().toString();
				String boxNema = comboBoxAddNemas.getSelectedItem().toString();
				String boxPairs = "0";
				String boxSheet = comboBoxAddSheets.getSelectedItem().toString();
				String boxFinish = "";
				String boxColor = comboBoxAddColors.getSelectedItem().toString();
				String boxHeight = textBoxAddHeight.getText();
				String boxWidth = textBoxAddWidth.getText();
				String boxDepth = textBoxAddDepth.getText();
				String boxUnits = comboBoxAddUnits.getSelectedItem().toString();
				String boxCaliber = comboBoxAddCalibers.getSelectedItem().toString();
				String boxCaliberComments = textBoxAddCaliberComments.getText();
				String boxLockType = "";
				String boxPrice = textBoxAddPrice.getText();
				Errors err = new Errors();
				// Check if fields are not empty before saving the data
				if(boxType.equalsIgnoreCase("para Pares Telefonicos")) {
					boxPairs = textBoxAddPairs.getText();
					if(boxPairs.isEmpty()) {
						err.add("Debe escribir la cantidad de pares telefonicos");
					}
				}
				if(boxSheet.equalsIgnoreCase("HNF")) {
					boxFinish = comboBoxAddFinishes.getSelectedItem().toString();
					boxColor = comboBoxAddColors.getSelectedItem().toString();
				} else if(boxSheet.equalsIgnoreCase("Acero Inoxidable")) {
					boxFinish = "Acero Inoxidable";
				} else if(boxSheet.equalsIgnoreCase("Galvanizada")) {
					boxFinish = "Galvanizado";
				}
				if(!boxType.equalsIgnoreCase("de paso")) {
					boxLockType = comboBoxAddLockTypes.getSelectedItem().toString();
				}
				if(boxHeight.isEmpty() || !Numbers.isNumeric(boxHeight)) {
					err.add("La altura debe ser numerica y no debe estar vacia");
				}
				if(boxWidth.isEmpty() || !Numbers.isNumeric(boxWidth)) {
					err.add("El ancho debe ser numerico y no debe estar vacio");
				}
				if(boxDepth.isEmpty() || !Numbers.isNumeric(boxDepth)) {
					err.add("La profundidad debe ser numerica y no debe estar vacia");
				}
				if(boxPrice.isEmpty() || !Numbers.isNumeric(boxPrice)) {
					err.add("El precio no puede estar vacio y solo debe contener digitos numericos");
				}
				if(err.isEmpty()) {
					boolean saved = db.addBox(boxType, boxInstallation, boxNema, Integer.valueOf(boxPairs), boxSheet, boxFinish, boxColor, Double.valueOf(boxHeight), Double.valueOf(boxWidth), Double.valueOf(boxDepth), boxUnits, boxCaliber, boxCaliberComments, boxLockType, Double.valueOf(boxPrice));
					if(saved) {
						JOptionPane.showMessageDialog(null, "Caja creada exitosamente");
						setBoxesMode(MainView.VIEW_MODE);
					}
				} else {
					err.dump();
				}
			} else if (actionCommand.equalsIgnoreCase("box.description.add.cancel")) {
				setBoxesMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("box.description.edit.save")) {
				ArrayList<Object> listFields = new ArrayList<Object>();
				ArrayList<Object> listValues = new ArrayList<Object>();
				Errors err = new Errors();
				if(comboBoxEditTypes.getItemCount() > 0) {
					if (comboBoxEditTypes.getSelectedIndex() > -1) {
						if (!editBoxType.equals(comboBoxEditTypes.getSelectedItem().toString())) {
							listFields.add("type_id");
							listValues.add("'" + db.getBoxTypeId(comboBoxEditTypes.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de caja");
					}
				} else {
					err.add("No hay ningun tipo de caja registrado, debe registrar uno primero");
				}
				if(comboBoxEditInstallations.getItemCount() > 0) {
					if (comboBoxEditInstallations.getSelectedIndex() > -1) {
						if (!editBoxInstallation.equals(comboBoxEditInstallations.getSelectedItem().toString())) {
							listFields.add("installation_id");
							listValues.add("'" + db.getInstallationId(comboBoxEditInstallations.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de instalacion de caja");
					}
				} else {
					err.add("No hay ninguna instalacion de caja registrada, debe registrar una primero");
				}
				if(comboBoxEditNemas.getItemCount() > 0) {
					if (comboBoxEditNemas.getSelectedIndex() > -1) {
						if (!editBoxNema.equals(comboBoxEditNemas.getSelectedItem().toString())) {
							listFields.add("nema_id");
							listValues.add("'" + db.getNemaId(comboBoxEditNemas.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar una nema para la caja");
					}
				} else {
					err.add("No hay ninguna nema registrada, debe registrar una primero");
				}
				if(comboBoxEditTypes.getSelectedItem().toString().equalsIgnoreCase("para Pares Telefonicos")) {
					if(!textBoxEditPairs.getText().isEmpty()) {
						if(!Numbers.isNumeric(textBoxEditPairs.getText())) {
							err.add("Los pares telefonicos deben ser numericos");
						} else {
							if (!String.valueOf(editBoxPairs).equals(textBoxEditPairs.getText())) {
								listFields.add("pairs");
								listValues.add(Integer.valueOf(textBoxEditPairs.getText()));
							}
						}
					} else {
						err.add("Los pares telefonicos no pueden estar vacios para este tipo de caja");
					}
				}
				if(comboBoxEditSheets.getItemCount() > 0) {
					if (comboBoxEditSheets.getSelectedIndex() > -1) {
						if (!editBoxSheet.equals(comboBoxEditSheets.getSelectedItem().toString())) {
							listFields.add("sheet_id");
							listValues.add("'" + db.getBoxSheetId(comboBoxEditSheets.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar una lamina para la caja");
					}
				} else {
					err.add("No hay ninguna lamina de caja registrada, debe registrar una primero");
				}
				if (!editBoxFinish.equals(comboBoxEditFinishes.getSelectedItem().toString())) {
					if(comboBoxEditFinishes.getItemCount() > 0) {
						if(comboBoxEditSheets.getSelectedItem().toString().equalsIgnoreCase("HNF")) {
							listFields.add("finish_id");
							listValues.add("'" + db.getBoxFinishId(comboBoxEditFinishes.getSelectedItem().toString()) + "'");
						} else if (comboBoxEditSheets.getSelectedItem().toString().equalsIgnoreCase("Galvanizada")) {
							listFields.add("finish_id");
							listValues.add("'" + db.getBoxFinishId("Galvanizado") + "'");
						} else if (comboBoxEditSheets.getSelectedItem().toString().equalsIgnoreCase("Acero Inoxidable")) {
							listFields.add("finish_id");
							listValues.add("'" + db.getBoxFinishId("Acero Inoxidable") + "'");
						} else if (comboBoxEditSheets.getSelectedItem().toString().equalsIgnoreCase("Aluminizada")) {
							listFields.add("finish_id");
							listValues.add("'" + db.getBoxFinishId("Aluminizado") + "'");
						}
					} else {
						err.add("No hay ningun acabado de caja registrado, debe registrar uno primero");
					}
				}
				if(comboBoxEditSheets.getSelectedItem().toString().equalsIgnoreCase("HNF")) {
					if(comboBoxEditColors.getItemCount() > 0) {
						if (comboBoxEditColors.getSelectedIndex() > -1) {
							if (!editBoxColor.equals(comboBoxEditColors.getSelectedItem().toString())) {
								listFields.add("color_id");
								listValues.add("'" + db.getBoxColorId(comboBoxEditColors.getSelectedItem().toString()) + "'");
							}
						} else {
							err.add("Debe seleccionar un color para la caja");
						}
					} else {
						err.add("No hay ningun color de caja registrado, debe registrar uno primero");
					}
				} else {
					listFields.add("color_id");
					listValues.add("0");
				}
				if(!textBoxEditHeight.getText().isEmpty() && Numbers.isNumeric(textBoxEditHeight.getText())) {
					if (!String.valueOf(editBoxHeight).equals(textBoxEditHeight.getText())) {
						listFields.add("height");
						listValues.add(Double.valueOf(textBoxEditHeight.getText()));
					}
				} else {
					err.add("Debe introducir un valor numerico en la Altura de la caja");
				}
				if(!textBoxEditWidth.getText().isEmpty() && Numbers.isNumeric(textBoxEditWidth.getText())) {
					if (!String.valueOf(editBoxWidth).equals(textBoxEditWidth.getText())) {
						listFields.add("width");
						listValues.add(Double.valueOf(textBoxEditWidth.getText()));
					}
				} else {
					err.add("Debe introducir un valor numerico en el Ancho de la caja");
				}
				if(!textBoxEditDepth.getText().isEmpty() && Numbers.isNumeric(textBoxEditDepth.getText())) {
					if (!String.valueOf(editBoxDepth).equals(textBoxEditDepth.getText())) {
						listFields.add("depth");
						listValues.add(Double.valueOf(textBoxEditDepth.getText()));
					}
				} else {
					err.add("Debe introducir un valor numerico en la Profundidad de la caja");
				}
				if(comboBoxEditUnits.getSelectedIndex() > -1) {
					if(!editBoxUnits.equals(comboBoxEditUnits.getSelectedItem().toString())) {
						listFields.add("units_id");
						listValues.add("'" + db.getBoxUnitsId(comboBoxEditUnits.getSelectedItem().toString()) + "'");
					}
				} else {
					err.add("Debe seleccionar una unidad de medida");
				}
				if(comboBoxEditCalibers.getItemCount() > 0) {
					if (comboBoxEditCalibers.getSelectedIndex() > -1) {
						if (!editBoxCaliber.equals(comboBoxEditCalibers.getSelectedItem().toString())) {
							listFields.add("caliber_id");
							listValues.add("'" + db.getBoxCaliberId(comboBoxEditCalibers.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un calibre para la caja");
					}
				} else {
					err.add("No hay ningun calibre de caja registrado, debe registrar uno primero");
				}
				if (!editBoxCaliberComments.equals(textBoxEditCaliberComments.getText())) {
					listFields.add("caliber_comments");
					listValues.add("'" + textBoxEditCaliberComments.getText() + "'");
				}
				if(!comboBoxEditTypes.getSelectedItem().toString().equalsIgnoreCase("de Paso")) {
					if (comboBoxEditLockTypes.getItemCount() > 0) {
						if (comboBoxEditLockTypes.getSelectedIndex() > -1) {
							if(!editBoxLockType.equals(comboBoxEditLockTypes.getSelectedItem().toString())) {
								listFields.add("lock_type_id");
								listValues.add("'" + db.getLockTypeId(comboBoxEditLockTypes.getSelectedItem().toString()) + "'");
							}
						} else {
							err.add("Debe seleccionar un tipo de cerradura");
						}
					} else {
						err.add("No hay ninguna cerradura registrada, debe registrar una primero");
					}
				} else {
					listFields.add("lock_type_id");
					listValues.add("0");
				}
				if(!textBoxEditPrice.getText().isEmpty() && Numbers.isNumeric(textBoxEditPrice.getText())) {
					if (!String.valueOf(editBoxPrice).equals(textBoxEditPrice.getText())) {
						listFields.add("price");
						listValues.add(Double.valueOf(textBoxEditPrice.getText()));
					}
				} else {
					err.add("El precio debe ser un valor numerico");
				}
				if(err.isEmpty()) {
					if(listFields.size() > 0 && listValues.size() > 0) {
						boolean boxEdited = db.editBox(editBoxId, listFields, listValues);
						if (boxEdited) {
							setBoxesMode(MainView.VIEW_MODE);
						} else {
							JOptionPane.showMessageDialog(null, "No se pudo actualizar la caja debido a un error");
						}
					} else {
						setBoxesMode(MainView.VIEW_MODE);
					}
				} else {
					err.dump();
				}
			} else if (actionCommand.equalsIgnoreCase("box.description.edit.cancel")) {
				setBoxesMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("board.description.buttons.add")) {
				setBoardsMode(MainView.ADD_MODE);
			} else if (actionCommand.equalsIgnoreCase("board.description.buttons.edit")) {
				setBoardsMode(MainView.EDIT_MODE);
			} else if (actionCommand.equalsIgnoreCase("board.description.add.save")) {
				String boardName = textBoardAddName.getText();
				String boardType = comboBoardAddType.getSelectedItem().toString();
				String boardInstallation = comboBoardAddInstallation.getSelectedItem().toString();
				String boardNema = comboBoardAddNema.getSelectedItem().toString();
				String boardBarCapacity = comboBoardAddBarCapacity.getSelectedItem().toString();
				String boardBarType = comboBoardAddBarType.getSelectedItem().toString();
				String boardCircuits = comboBoardAddCircuits.getSelectedItem().toString();
				String boardVoltage = comboBoardAddVoltage.getSelectedItem().toString();
				String boardPhases = comboBoardAddPhases.getSelectedItem().toString();
				String boardGround = (checkBoardAddGround.isSelected())?"1":"0";
				String boardInterruption = comboBoardAddInterruption.getSelectedItem().toString();
				String boardLockType = comboBoardAddLockType.getSelectedItem().toString();
				String boardPrice = textBoardAddPrice.getText();
				Errors err = new Errors();
				
				if(boardName.isEmpty()) {
					err.add("Debe escribir un nombre para el tablero");
				}
				if(boardPrice.isEmpty() || !Numbers.isNumeric(boardPrice)) {
					err.add("El precio no puede estar vacio y solo debe contener digitos numericos");
				}
				if(err.isEmpty()) {
					boolean saved = db.addBoard(boardName, boardType, boardInstallation, boardNema, Integer.valueOf(boardBarCapacity), boardBarType, Integer.valueOf(boardCircuits), boardVoltage, Integer.valueOf(boardPhases), boardGround, Integer.valueOf(boardInterruption), boardLockType, Double.valueOf(boardPrice));
					if(saved) {
						JOptionPane.showMessageDialog(null, "Tablero creado exitosamente");
						setBoardsMode(MainView.VIEW_MODE);
					}
				} else {
					err.dump();
				}
			} else if (actionCommand.equalsIgnoreCase("board.description.add.cancel")) {
				setBoardsMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("board.description.edit.save")) {
				ArrayList<Object> listFields = new ArrayList<Object>();
				ArrayList<Object> listValues = new ArrayList<Object>();
				Errors err = new Errors();
				
				if(!textBoardEditName.getText().isEmpty()) {
					if (!String.valueOf(editBoardName).equals(textBoardEditName.getText())) {
						listFields.add("`name`");
						listValues.add("'" + textBoardEditName.getText() + "'");
					}
				} else {
					err.add("Debe introducir un nombre para el tablero");
				}
				if(comboBoardEditType.getItemCount() > 0) {
					if (comboBoardEditType.getSelectedIndex() > -1) {
						if (!editBoardType.equals(comboBoardEditType.getSelectedItem().toString())) {
							listFields.add("type_id");
							listValues.add("'" + db.getBoardTypeId(comboBoardEditType.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de tablero");
					}
				} else {
					err.add("No hay ningun tipo de tablero registrado, debe registrar uno primero");
				}
				if(comboBoardEditInstallation.getItemCount() > 0) {
					if (comboBoardEditInstallation.getSelectedIndex() > -1) {
						if (!editBoardInstallation.equals(comboBoardEditInstallation.getSelectedItem().toString())) {
							listFields.add("installation_id");
							listValues.add("'" + db.getInstallationId(comboBoardEditInstallation.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de instalacion de tablero");
					}
				} else {
					err.add("No hay ninguna instalacion de tablero registrada, debe registrar una primero");
				}
				if(comboBoardEditNema.getItemCount() > 0) {
					if (comboBoardEditNema.getSelectedIndex() > -1) {
						if (!editBoardNema.equals(comboBoardEditNema.getSelectedItem().toString())) {
							listFields.add("nema_id");
							listValues.add("'" + db.getNemaId(comboBoardEditNema.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar una nema para el tablero");
					}
				} else {
					err.add("No hay ninguna nema registrada, debe registrar una primero");
				}
				if(comboBoardEditBarCapacity.getItemCount() > 0) {
					if(comboBoardEditBarCapacity.getSelectedIndex() > -1) {
						if(!Numbers.isNumeric(comboBoardEditBarCapacity.getSelectedItem().toString())) {
							err.add("La capacidad de barra debe ser numerica");
						} else {
							if (!String.valueOf(editBoardBarCapacity).equals(comboBoardEditBarCapacity.getSelectedItem().toString())) {
								listFields.add("bar_capacity_id");
								listValues.add(db.getBoardBarCapacityId(Integer.valueOf(comboBoardEditBarCapacity.getSelectedItem().toString())));
							}
						}
					} else {
						err.add("Debe seleccionar una capacidad de barra para el tablero");
					}
				} else {
					err.add("No hay ninguna capacidad de barra registrada, debe registrar una primero");
				}
				if(comboBoardEditBarType.getItemCount() > 0) {
					if (comboBoardEditBarType.getSelectedIndex() > -1) {
						if (!editBoardBarType.equals(comboBoardEditBarType.getSelectedItem().toString())) {
							listFields.add("bar_type_id");
							listValues.add("'" + db.getBoardBarTypeId(comboBoardEditBarType.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de barra para el tablero");
					}
				} else {
					err.add("No hay ningun tipo de barra registrada, debe registrar una primero");
				}
				if(comboBoardEditCircuits.getItemCount() > 0) {
					if(comboBoardEditCircuits.getSelectedIndex() > -1) {
						if(!Numbers.isNumeric(comboBoardEditCircuits.getSelectedItem().toString())) {
							err.add("Los circuitos deben ser numericos");
						} else {
							if (!String.valueOf(editBoardCircuits).equals(comboBoardEditCircuits.getSelectedItem().toString())) {
								listFields.add("circuits_id");
								listValues.add(db.getBoardCircuitsId(Integer.valueOf(comboBoardEditCircuits.getSelectedItem().toString())));
							}
						}
					} else {
						err.add("Debe seleccionar la cantidad de circuitos para el tablero");
					}
				} else {
					err.add("No hay ninguna cantidad de circuitos registrados, debe registrar alguno primero");
				}
				if(comboBoardEditVoltage.getItemCount() > 0) {
					if (comboBoardEditVoltage.getSelectedIndex() > -1) {
						if (!editBoardVoltage.equals(comboBoardEditVoltage.getSelectedItem().toString())) {
							listFields.add("voltage_id");
							listValues.add("'" + db.getBoardVoltageId(comboBoardEditVoltage.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un voltaje para el tablero");
					}
				} else {
					err.add("No hay ningun voltaje registrado, debe registrar uno primero");
				}
				if(comboBoardEditPhases.getItemCount() > 0) {
					if(comboBoardEditPhases.getSelectedIndex() > -1) {
						if(!Numbers.isNumeric(comboBoardEditPhases.getSelectedItem().toString())) {
							err.add("La cantidad de fases debe ser numerica");
						} else {
							if (!String.valueOf(editBoardPhases).equals(comboBoardEditPhases.getSelectedItem().toString())) {
								listFields.add("phases");
								listValues.add(Integer.valueOf(comboBoardEditPhases.getSelectedItem().toString()));
							}
						}
					} else {
						err.add("Debe seleccionar la cantidad de fases para el tablero");
					}
				} else {
					err.add("No hay ninguna cantidad de fases registrada, debe registrar una primero");
				}
				if(!editBoardGround.equals(checkBoardEditGround.isSelected()?"SI":"NO")) {
					listFields.add("ground");
					listValues.add(checkBoardEditGround.isSelected()?"1":"0");
				}
				if(comboBoardEditInterruption.getItemCount() > 0) {
					if(comboBoardEditInterruption.getSelectedIndex() > -1) {
						if(!Numbers.isNumeric(comboBoardEditInterruption.getSelectedItem().toString())) {
							err.add("La interrupcion debe ser numerica");
						} else {
							if (!String.valueOf(editBoardInterruption).equals(comboBoardEditInterruption.getSelectedItem().toString())) {
								listFields.add("interruption_id");
								listValues.add(db.getInterruptionId(Integer.valueOf(comboBoardEditInterruption.getSelectedItem().toString())));
							}
						}
					} else {
						err.add("Debe seleccionar la interrupcion para el tablero");
					}
				} else {
					err.add("No hay ninguna interrupcion registrada, debe registrar una primero");
				}
				if (comboBoardEditLockType.getItemCount() > 0) {
					if (comboBoardEditLockType.getSelectedIndex() > -1) {
						if(!editBoardLockType.equals(comboBoardEditLockType.getSelectedItem().toString())) {
							listFields.add("lock_type_id");
							listValues.add("'" + db.getLockTypeId(comboBoardEditLockType.getSelectedItem().toString()) + "'");
						}
					} else {
						err.add("Debe seleccionar un tipo de cerradura");
					}
				} else {
					err.add("No hay ninguna cerradura registrada, debe registrar una primero");
				}
				if(!textBoardEditPrice.getText().isEmpty() && Numbers.isNumeric(textBoardEditPrice.getText())) {
					if (!String.valueOf(editBoardPrice).equals(textBoardEditPrice.getText())) {
						listFields.add("price");
						listValues.add(Double.valueOf(textBoardEditPrice.getText()));
					}
				} else {
					err.add("El precio debe ser un valor numerico");
				}
				if(err.isEmpty()) {
					if(listFields.size() > 0 && listValues.size() > 0) {
						boolean boardEdited = db.editBoard(editBoardId, listFields, listValues);
						if (boardEdited) {
							setBoardsMode(MainView.VIEW_MODE);
						} else {
							JOptionPane.showMessageDialog(null, "No se pudo actualizar el tablero debido a un error");
						}
					} else {
						setBoardsMode(MainView.VIEW_MODE);
					}
				} else {
					err.dump();
				}
			} else if (actionCommand.equalsIgnoreCase("board.description.edit.cancel")) {
				setBoardsMode(MainView.VIEW_MODE);
			} else if (actionCommand.equalsIgnoreCase("board.switch.add")) {
				dialogBoardSwitchAdd = new JDialog(null, "Agregar Interruptor", Dialog.DEFAULT_MODALITY_TYPE);
				dialogBoardSwitchAdd.setMinimumSize(new Dimension(800, 300));
				dialogBoardSwitchAdd.setSize(800, 300);
				dialogBoardSwitchAdd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int x = (dim.width / 2) - (dialogBoardSwitchAdd.getWidth() / 2);
				int y = (dim.height / 2) - (dialogBoardSwitchAdd.getHeight() / 2);
				dialogBoardSwitchAdd.setLocation(x, y);
				// finish the createBoardSwitchAddPanel() method and replace it at the line below
				// add the listener to handle the accept button
				JPanel panelCenter = new JPanel();
				panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));
				panelCenter.add(createBoardSwitchAddSearchPanel());
				panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
				panelCenter.add(createBoardSwitchAddTablePanel());
				dialogBoardSwitchAdd.add(panelCenter, BorderLayout.CENTER);
				
				JPanel panelLower = new JPanel();
				panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
				panelLower.add(createBoardSwitchAddCountPanel());
				panelLower.add(createBoardSwitchAddButtonPanel());
				dialogBoardSwitchAdd.add(panelLower, BorderLayout.SOUTH);
				
				WindowsListener lForWindow = new WindowsListener();
				dialogBoardSwitchAdd.addWindowListener(lForWindow);
				
				dialogBoardSwitchAdd.setVisible(true);
			} else if (actionCommand.equalsIgnoreCase("board.switch.remove")) {
				int response = JOptionPane.showConfirmDialog(null, "Esta seguro que desea remover este interruptor del tablero?", "Remover interruptor del tablero", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_OPTION) {
					if(db.removeBoardSwitch(selectedBoardSwitchId)) {
						if(tableBoardsResult.getSelectedRow() > -1) {
							String[] boardSwitchesColumnNames = { "Id", "Descripcion", "Cantidad", "Precio", "Total", "Principal"};
							
							selectedBoardId = Integer.valueOf( (String) tableBoardsResult.getValueAt(tableBoardsResult.getSelectedRow(), SharedListSelectionListener.BOARD_ID_COLUMN));
							
							String boardSwitchesQuery = "SELECT board_switches.id, "
										+ " CONCAT('Interruptor ', boards.phases, 'X', currents.current, 'A,', switch_types.type, ',', switch_brands.brand) as description, "
										+ " board_switches.quantity, "
										+ " switches.price, "
										+ " (board_switches.quantity * switches.price) as total, "
										+ " IF(boards.main_switch_id=board_switches.id,'1','0') as principal "
									+ " FROM boards, board_switches, switches, switch_types, switch_brands, currents "
									+ " WHERE board_switches.board_container_id = " + selectedBoardId
									+ " AND boards.id = board_switches.board_container_id"
									+ " AND switches.id = board_switches.switch_id "
									+ " AND switches.current_id = currents.id"
									+ " AND switches.type_id = switch_types.id"
									+ " AND switches.brand_id = switch_brands.id";
							
							boardSwitchesData = db.fetchAllAddBoolean(db.select(boardSwitchesQuery), boardSwitchesColumnNames.length);
							
							if(boardSwitchesData.length > 0) {
								MyTableModel mForTable = new MyTableModel(boardSwitchesData, boardSwitchesColumnNames);
								tableBoardSwitchesResult.setModel(mForTable);
							} else {
								tableBoardSwitchesResult.setModel(new DefaultTableModel());
								buttonRemoveBoardSwitch.setEnabled(false);
							}
							buttonAddBoardSwitch.setEnabled(true);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Error al remover interruptor del tablero");
					}
				}
			}
		}
		
	}
	
	private class WindowsListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			if(boardSwitchSearchId > 0) {
				JOptionPane.showMessageDialog(null, "El id seleccionado fue: " + boardSwitchSearchId + " y una cantidad de " + boardSwitchSearchQuantity + " interruptores");
				
			}
		}

		@Override
		public void windowIconified(WindowEvent e) {
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			
		}
		
	}
	
	private class MouseActionListener implements MouseListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			e.getComponent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			// Need to add or remove settings from database and combos
			if(actionCommand.equalsIgnoreCase("switch.settings.brands.add")) {
				String brandAdd = JOptionPane.showInputDialog("Introduzca la marca que desea agregar");
				if(null != brandAdd && !brandAdd.isEmpty()) {
					if(!db.switchBrandExists(brandAdd)) {
						if(db.addSwitchBrand(brandAdd)) {
							JOptionPane.showMessageDialog(null, "Marca agregada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar la marca");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta marca ya existe, no puede registrarla de nuevo");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir la marca que desea agregar, de lo contrario no se guardara");
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.brands.remove")) {
				String brandRemove = comboSwitchSettingsBrands.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar la marca " + brandRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.switchBrandExists(brandRemove)) {
						if(db.removeSwitchBrand(brandRemove)) {
							JOptionPane.showMessageDialog(null, "Marca eliminada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar la marca");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta marca no existe, asi que no puede ser eliminada");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.types.add")) {
				String typeAdd = JOptionPane.showInputDialog(null, "Ingrese el tipo de interruptor que desea agregar");
				
				if(null != typeAdd && !typeAdd.isEmpty()) {
					if(!db.switchTypeExists(typeAdd)) {
						String queryString = "SELECT brand FROM switch_brands";
						
						Object[] brands = db.fetchColumnAsArray(db.select(queryString), "brand");
						Object typeBrand = JOptionPane.showInputDialog(null, "A que marca pertenece este tipo?", "Agregar tipo", JOptionPane.QUESTION_MESSAGE, null, brands, null);
						
						if(null != typeBrand && null != typeAdd && !typeAdd.isEmpty() && !typeBrand.toString().isEmpty()) {
							if(db.addSwitchType(typeAdd, typeBrand.toString())) {
								JOptionPane.showMessageDialog(null, "Tipo agregado exitosamente");
								updateSwitchComboSettings();
							} else {
								JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el tipo");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Debe introducir el tipo y marca que desea agregar, de lo contrario no se guardara");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este tipo ya existe, no puede registrarlo de nuevo");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.types.remove")) {
				String typeRemove = comboSwitchSettingsTypes.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el tipo " + typeRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.switchTypeExists(typeRemove)) {
						if(db.removeSwitchType(typeRemove)) {
							JOptionPane.showMessageDialog(null, "Tipo eliminado exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar el tipo");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este tipo no existe, asi que no puede ser eliminado");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.currents.add")) {
				String currentAdd = JOptionPane.showInputDialog("Introduzca la corriente que desea agregar");
				if(null != currentAdd && !currentAdd.isEmpty() && Numbers.isNumeric(currentAdd)) {
					if(!db.currentExists(currentAdd)) {
						if(db.addCurrent(currentAdd)) {
							JOptionPane.showMessageDialog(null, "Corriente agregada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar la corriente");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta corriente ya existe, no puede registrarla de nuevo");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir la corriente que desea agregar y solo puede ser numerica, de lo contrario no se guardara");
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.currents.remove")) {
				String currentRemove = comboSwitchSettingsCurrents.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar la corriente " + currentRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.currentExists(currentRemove)) {
						if(db.removeCurrent(currentRemove)) {
							JOptionPane.showMessageDialog(null, "Corriente eliminada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar la corriente");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta corriente no existe, asi que no puede ser eliminada");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.voltages.add")) {
				String voltageAdd = JOptionPane.showInputDialog(null, "Ingrese el voltaje que desea agregar");
				
				if(null != voltageAdd && !voltageAdd.isEmpty()) {
					Object[] types = {"AC", "DC"};
					Object voltageType = JOptionPane.showInputDialog(null, "Que tipo de voltaje es?", "Agregar tipo de voltaje", JOptionPane.QUESTION_MESSAGE, null, types, null);
					if(!db.voltageExists(voltageAdd + voltageType)) {
						if(null != voltageType && null != voltageAdd && !voltageAdd.isEmpty() && !voltageType.toString().isEmpty()) {
							if(db.addVoltage(voltageAdd, voltageType.toString())) {
								JOptionPane.showMessageDialog(null, "Voltaje agregado exitosamente");
								updateSwitchComboSettings();
							} else {
								JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el voltaje");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Debe introducir el voltaje y el tipo de voltage que desea agregar, de lo contrario no se guardara");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este tipo ya existe, no puede registrarlo de nuevo");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.voltages.remove")) {
				String voltageRemove = comboSwitchSettingsVoltages.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el voltaje " + voltageRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.voltageExists(voltageRemove)) {
						if(db.removeVoltage(voltageRemove)) {
							JOptionPane.showMessageDialog(null, "Voltaje eliminado exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar el voltaje");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este voltaje no existe, asi que no puede ser eliminado");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.interruptions.add")) {
				String interruptionAdd = JOptionPane.showInputDialog("Introduzca la interrupcion que desea agregar");
				if(null != interruptionAdd && !interruptionAdd.isEmpty() && Numbers.isNumeric(interruptionAdd)) {
					if(!db.interruptionExists(interruptionAdd)) {
						if(db.addInterruption(interruptionAdd)) {
							JOptionPane.showMessageDialog(null, "Interrupcion agregada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar la interrupcion");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta interrupcion ya existe, no puede registrarla de nuevo");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir la interrupcion que desea agregar y solo puede ser numerica, de lo contrario no se guardara");
				}
			} else if (actionCommand.equalsIgnoreCase("switch.settings.interruptions.remove")) {
				String interruptionRemove = comboSwitchSettingsInterruptions.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar la interrupcion " + interruptionRemove + "KA? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.interruptionExists(interruptionRemove)) {
						if(db.removeInterruption(interruptionRemove)) {
							JOptionPane.showMessageDialog(null, "Interrupcion eliminada exitosamente");
							updateSwitchComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar la interrupcion");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Esta interrupcion no existe, asi que no puede ser eliminada");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("box.settings.colors.add")) {
				String colorAdd = JOptionPane.showInputDialog("Introduzca el color que desea agregar");
				if(null != colorAdd && !colorAdd.isEmpty()) {
					if(!db.colorExists(colorAdd)) {
						if(db.addColor(colorAdd)) {
							JOptionPane.showMessageDialog(null, "Color agregado exitosamente");
							updateBoxComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el color");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este color ya existe, no puede registrarlo de nuevo");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir el color que desea agregar, de lo contrario no se guardara");
				}
			} else if (actionCommand.equalsIgnoreCase("box.settings.colors.remove")) {
				String colorRemove = comboBoxSettingsColors.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el color " + colorRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.colorExists(colorRemove)) {
						if(db.removeColor(colorRemove)) {
							JOptionPane.showMessageDialog(null, "Color eliminado exitosamente");
							updateBoxComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar el color");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este color no existe, asi que no puede ser eliminado");
					}
				}
			} else if (actionCommand.equalsIgnoreCase("box.settings.calibers.add")) {
				String caliberAdd = JOptionPane.showInputDialog("Introduzca el calibre que desea agregar");
				if(null != caliberAdd && !caliberAdd.isEmpty() && Numbers.isNumeric(caliberAdd)) {
					if(!db.caliberExists(caliberAdd)) {
						if(db.addCaliber(caliberAdd)) {
							JOptionPane.showMessageDialog(null, "Calibre agregado exitosamente");
							updateBoxComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el calibre");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este calibre ya existe, no puede registrarlo de nuevo");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir el calibre que desea agregar y solo puede ser numerico, de lo contrario no se guardara");
				}
			} else if (actionCommand.equalsIgnoreCase("box.settings.calibers.remove")) {
				String caliberRemove = comboBoxSettingsCalibers.getSelectedItem().toString();
				int answer = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el calibre " + caliberRemove + "? Puede perder datos importantes", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.YES_OPTION) {
					if(db.caliberExists(caliberRemove)) {
						if(db.removeCaliber(caliberRemove)) {
							JOptionPane.showMessageDialog(null, "Calibre eliminado exitosamente");
							updateBoxComboSettings();
						} else {
							JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar el calibre");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Este calibre no existe, asi que no puede ser eliminado");
					}
				}
			}
		}
		
	}
	
	private class TextFieldListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			updateBoxTextAddDescription();
		}
		
	}

}