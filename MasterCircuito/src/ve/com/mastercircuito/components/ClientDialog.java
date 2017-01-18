package ve.com.mastercircuito.components;

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

import ve.com.mastercircuito.db.Db;

public class ClientDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104723883961973726L;
	
	// Common Tables
		private final String CLIENTS_TABLE = "clients";
	
	private Integer height = 300;
	private Integer width = 600;
	
	private Db db;
	private Object[][] companiesSearchData;
	private JTable tableCompaniesSearchResult;
	private ListSelectionModel listCompanySearchSelectionModel;
	private JTextField textSearchClient, textSearchClientCode, textSearchRepresentative, textSearchRif;
	
	private String[] companiesColumnNames = { "Id", "Cliente", "Codigo Cliente", "Representante", "Rif"};
	
	private Integer searchId = 0;
	private String searchClient;
	private String searchClientCode;
	private String searchRepresentative;
	private String searchRif;

 	public ClientDialog(Window owner) {
		this(owner, "");
	}
	
	public ClientDialog(Window owner, String title) {
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
		panelCenter.add(createCompanyAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCenter.add(createCompanyAddTablePanel());
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createSwitchAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public Integer getSearchId() {
		return this.searchId;
	}
	
	public String getSearchClient() {
		return this.searchClient;
	}
	
	public String getSearchClientCode() {
		return this.searchClientCode;
	}
	
	public String getSearchRepresentative() {
		return this.searchRepresentative;
	}
	
	public String getSearchRif() {
		return this.searchRif;
	}
	
	private JPanel createCompanyAddSearchPanel() {
		JPanel panelCompanyAddSearch = new JPanel();
		panelCompanyAddSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel labelClient = new JLabel("Cliente:");
		JLabel labelClientCode = new JLabel("Codigo Cliente:");
		JLabel labelRepresentative = new JLabel("Representante");
		JLabel labelRif = new JLabel("Rif:");
		
		textSearchClient = new JTextField(6);
		textSearchClient.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadCompanySearchTable("");
				
				if(companiesSearchData.length > 0) {
					tableCompaniesSearchResult.setModel(new MyTableModel(companiesSearchData, companiesColumnNames));
				} else {
					tableCompaniesSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelCompanyAddSearch.add(labelClient);
		panelCompanyAddSearch.add(textSearchClient);
		
		textSearchClientCode = new JTextField(6);
		textSearchClientCode.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadCompanySearchTable("");
				
				if(companiesSearchData.length > 0) {
					tableCompaniesSearchResult.setModel(new MyTableModel(companiesSearchData, companiesColumnNames));
				} else {
					tableCompaniesSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelCompanyAddSearch.add(labelClientCode);
		panelCompanyAddSearch.add(textSearchClientCode);
		
		textSearchRepresentative = new JTextField(6);
		textSearchRepresentative.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadCompanySearchTable("");
				
				if(companiesSearchData.length > 0) {
					tableCompaniesSearchResult.setModel(new MyTableModel(companiesSearchData, companiesColumnNames));
				} else {
					tableCompaniesSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelCompanyAddSearch.add(labelRepresentative);
		panelCompanyAddSearch.add(textSearchRepresentative);
		
		textSearchRif = new JTextField(6);
		textSearchRif.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				loadCompanySearchTable("");
				
				if(companiesSearchData.length > 0) {
					tableCompaniesSearchResult.setModel(new MyTableModel(companiesSearchData, companiesColumnNames));
				} else {
					tableCompaniesSearchResult.setModel(new DefaultTableModel());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		panelCompanyAddSearch.add(labelRif);
		panelCompanyAddSearch.add(textSearchRif);
		
		return panelCompanyAddSearch;
	}
	
	private JPanel createCompanyAddTablePanel() {
		String companiesQuery = "SELECT clients.id, "
				+ "clients.client, "
				+ "clients.client_code, "
				+ "clients.representative, "
				+ "clients.rif "
			+ "FROM clients "
			+ "ORDER BY clients.id ASC "
			+ "LIMIT 5";
		
		companiesSearchData = db.fetchAll(db.select(companiesQuery));
		
		MyTableModel mForTable = new MyTableModel(companiesSearchData, companiesColumnNames);
		
		tableCompaniesSearchResult = new JTable();
		tableCompaniesSearchResult.setModel(mForTable);
		tableCompaniesSearchResult.setAutoCreateRowSorter(true);
		tableCompaniesSearchResult.getTableHeader().setReorderingAllowed(false);
		
		listCompanySearchSelectionModel = tableCompaniesSearchResult.getSelectionModel();
		tableCompaniesSearchResult.setSelectionModel(listCompanySearchSelectionModel);
		tableCompaniesSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelCompanyTable = new JPanel(new BorderLayout());
		panelCompanyTable.add(tableCompaniesSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelCompanyTable.add(tableCompaniesSearchResult, BorderLayout.CENTER);
		
		return panelCompanyTable;
	}
	
	private JPanel createSwitchAddButtonPanel() {
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
				if(tableCompaniesSearchResult.getSelectedRow() > -1) {
					searchId = Integer.valueOf((String)tableCompaniesSearchResult.getValueAt(tableCompaniesSearchResult.getSelectedRow(), 0));
					searchClient = (String)tableCompaniesSearchResult.getValueAt(tableCompaniesSearchResult.getSelectedRow(), 1);
					searchClientCode = (String)tableCompaniesSearchResult.getValueAt(tableCompaniesSearchResult.getSelectedRow(), 2);
					searchRepresentative = (String)tableCompaniesSearchResult.getValueAt(tableCompaniesSearchResult.getSelectedRow(), 3);
					searchRif = (String)tableCompaniesSearchResult.getValueAt(tableCompaniesSearchResult.getSelectedRow(), 4);
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchId = 0;
				searchClient = "";
				searchClientCode = "";
				searchRepresentative = "";
				searchRif = "";
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	private void loadCompanySearchTable(String whereQuery) {
		if(null != textSearchClient && !textSearchClient.getText().isEmpty()) {
			whereQuery = this.selectWhereQuery(whereQuery);
			whereQuery += this.CLIENTS_TABLE + ".client LIKE '%" + textSearchClient.getText() + "%'";
		}
		if(null != textSearchClientCode && !textSearchClientCode.getText().isEmpty()) {
			whereQuery = this.selectWhereQuery(whereQuery);
			whereQuery += this.CLIENTS_TABLE + ".client_code LIKE '%" + textSearchClientCode.getText() + "%'";
		}
		if(null != textSearchRepresentative && !textSearchRepresentative.getText().isEmpty()) {
			whereQuery = this.selectWhereQuery(whereQuery);
			whereQuery += this.CLIENTS_TABLE + ".representative LIKE '%" + textSearchRepresentative.getText() + "%'";
		}
		if(null != textSearchRif && !textSearchRif.getText().isEmpty()) {
			whereQuery = this.selectWhereQuery(whereQuery);
			whereQuery += this.CLIENTS_TABLE + ".rif LIKE '%" + textSearchRif.getText() + "%'";
		}
		
		String companiesQuery = "SELECT clients.id, "
				+ " clients.client, "
				+ " clients.client_code, "
				+ " clients.representative, "
				+ " clients.rif "
			+ " FROM clients "
			+ whereQuery
			+ " ORDER BY id ASC";
		
		companiesSearchData = db.fetchAll(db.select(companiesQuery));
	}
	
	private String selectWhereQuery(String whereQuery) {
		if(whereQuery.isEmpty()) {
			whereQuery = " WHERE ";
		} else {
			whereQuery += " AND ";
		}
		return whereQuery;
	}
	
}
