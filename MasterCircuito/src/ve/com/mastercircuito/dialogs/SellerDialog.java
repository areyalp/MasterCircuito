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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;

public class SellerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4333537381573226215L;
	
	// Common Tables
		private final String USERS_TABLE = "users";
		private final String USER_TYPES_TABLE = "user_types";
	
	private Integer height = 300;
	private Integer width = 600;
	
	private Db db;
	private Object[][] sellersSearchData;
	private JTable tableSellersSearchResult;
	private ListSelectionModel listSellerSearchSelectionModel;
	private JTextField textSearchSellerUser, textSearchSellerName, textSearchSellerType;
	
	private String[] sellersColumnNames = { "Id", "Usuario", "Tipo", "Nombre", "Apellido"};
	
	private Integer searchId = 0;
	private String sellerUser;
	private String sellerFirstName;
	private String sellerLastName;
	private String sellerType;
	
	public SellerDialog(Window owner) {
		this(owner, "");
	}
	
	public SellerDialog(Window owner, String title) {
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
		panelCenter.add(createSellerAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCenter.add(createSellerAddTablePanel());
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createSellerAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public Integer getSearchId() {
		return this.searchId;
	}
	
	public String getSellerUser() {
		return this.sellerUser;
	}
	
	public String getSellerFirstName() {
		return this.sellerFirstName;
	}
	
	public String getSellerLastName() {
		return this.sellerLastName;
	}
	
	public String getSellerType() {
		return this.sellerType;
	}
	
	private JPanel createSellerAddSearchPanel() {
		JPanel panelSellerAddSearch = new JPanel();
		panelSellerAddSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel labelSellerUser = new JLabel("Usuario:");
		JLabel labelSellerName = new JLabel("Nombre/Apellido:");
		JLabel labelSellerType = new JLabel("Representante");
		
		textSearchSellerUser = new JTextField(6);
		textSearchSellerUser.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadSellerSearchTable("");
				
				if(sellersSearchData.length > 0) {
					tableSellersSearchResult.setModel(new MyTableModel(sellersSearchData, sellersColumnNames));
				} else {
					tableSellersSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelSellerAddSearch.add(labelSellerUser);
		panelSellerAddSearch.add(textSearchSellerUser);
		
		textSearchSellerName = new JTextField(6);
		textSearchSellerName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadSellerSearchTable("");
				
				if(sellersSearchData.length > 0) {
					tableSellersSearchResult.setModel(new MyTableModel(sellersSearchData, sellersColumnNames));
				} else {
					tableSellersSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelSellerAddSearch.add(labelSellerName);
		panelSellerAddSearch.add(textSearchSellerName);
		
		textSearchSellerType = new JTextField(6);
		textSearchSellerType.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadSellerSearchTable("");
				
				if(sellersSearchData.length > 0) {
					tableSellersSearchResult.setModel(new MyTableModel(sellersSearchData, sellersColumnNames));
				} else {
					tableSellersSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelSellerAddSearch.add(labelSellerType);
		panelSellerAddSearch.add(textSearchSellerType);
		
		return panelSellerAddSearch;
	}
	
	private JPanel createSellerAddTablePanel() {
		String sellersQuery = "SELECT users.id, "
				+ "users.username, "
				+ "users.passport, "
				+ "users.first_name, "
				+ "users.last_name, "
				+ "users.phone "
			+ "FROM users "
			+ "ORDER BY users.id ASC "
			+ "LIMIT 5";
		
		sellersSearchData = db.fetchAll(db.select(sellersQuery));
		
		MyTableModel mForTable = new MyTableModel(sellersSearchData, sellersColumnNames);
		
		tableSellersSearchResult = new JTable();
		tableSellersSearchResult.setModel(mForTable);
		tableSellersSearchResult.setAutoCreateRowSorter(true);
		tableSellersSearchResult.getTableHeader().setReorderingAllowed(false);
		
		listSellerSearchSelectionModel = tableSellersSearchResult.getSelectionModel();
		tableSellersSearchResult.setSelectionModel(listSellerSearchSelectionModel);
		tableSellersSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelSellerAddTable = new JPanel(new BorderLayout());
		panelSellerAddTable.add(tableSellersSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelSellerAddTable.add(tableSellersSearchResult, BorderLayout.CENTER);
		
		return panelSellerAddTable;
	}
	
	private JPanel createSellerAddButtonPanel() {
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
				if(tableSellersSearchResult.getSelectedRow() > -1) {
					searchId = Integer.valueOf((String)tableSellersSearchResult.getValueAt(tableSellersSearchResult.getSelectedRow(), 0));
					sellerUser = (String)tableSellersSearchResult.getValueAt(tableSellersSearchResult.getSelectedRow(), 1);
					sellerFirstName = (String)tableSellersSearchResult.getValueAt(tableSellersSearchResult.getSelectedRow(), 2);
					sellerLastName = (String)tableSellersSearchResult.getValueAt(tableSellersSearchResult.getSelectedRow(), 3);
					sellerType = (String)tableSellersSearchResult.getValueAt(tableSellersSearchResult.getSelectedRow(), 4);
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchId = 0;
				sellerUser = "";
				sellerFirstName = "";
				sellerLastName = "";
				sellerType = "";
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	protected void loadSellerSearchTable(String whereQuery) {
		if(null != textSearchSellerUser && !textSearchSellerUser.getText().isEmpty()) {
			whereQuery += " AND " + this.USERS_TABLE + ".username LIKE '%" + textSearchSellerUser.getText() + "%'";
		}
		
		if (null != textSearchSellerName && !textSearchSellerName.getText().isEmpty()) {
			whereQuery += " AND " + this.USERS_TABLE + ".first_name LIKE '%" + textSearchSellerName.getText() + "%' "
					+ " OR " + this.USERS_TABLE + ".last_name LIKE '%" + textSearchSellerName.getText() + "%' ";
		}
		
		if (null != textSearchSellerType && !textSearchSellerType.getText().isEmpty()) {
			whereQuery += " AND " + USER_TYPES_TABLE + ".type LIKE '%" + textSearchSellerType.getText() + "%' ";
		}
		
		String budgetSellersQuery = "SELECT users.id, "
				+ " users.username, "
				+ " user_types.type, "
				+ " users.first_name, "
				+ " users.last_name "
			+ " FROM users, user_types "
			+ " WHERE " + this.USERS_TABLE + ".user_type_id = " + USER_TYPES_TABLE + ".id "
			+ whereQuery
			+ " ORDER BY users.id ASC";
		
		sellersSearchData = db.fetchAll(db.select(budgetSellersQuery));
	}
	
}
