package ve.com.mastercircuito.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.Errors;

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
		JScrollPane scrollPane = new JScrollPane(createCompanyAddTablePanel());
		tableCompaniesSearchResult.setFillsViewportHeight(true);
		panelCenter.add(scrollPane);
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
				loadClientSearchTable("");
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
				loadClientSearchTable("");
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
				loadClientSearchTable("");
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
				loadClientSearchTable("");
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
		
		tableCompaniesSearchResult = new JTable();
		
		HashSet<Integer> editableColumns = new HashSet<Integer>();
		editableColumns.add(1);
		editableColumns.add(2);
		editableColumns.add(3);
		editableColumns.add(4);
		
		if(tableCompaniesSearchResult.getModel() instanceof MyTableModel) {
			((MyTableModel) tableCompaniesSearchResult.getModel()).setQuery(companiesQuery);
		} else {
			MyTableModel mForTable = new MyTableModel(companiesQuery, companiesColumnNames, "clients", editableColumns);
			tableCompaniesSearchResult.setModel(mForTable);
		}
		
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
		JButton buttonCreateClient = new JButton("Crear Cliente");
		JButton buttonAccept = new JButton("Aceptar");
		JButton buttonCancel = new JButton("Cancelar");
		panelInner.add(buttonCreateClient);
		panelInner.add(buttonAccept);
		panelInner.add(buttonCancel);
		
		buttonCreateClient.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog();
				dialog.setTitle("Crear Cliente");
				dialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setSize(new Dimension(600,500));
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new GridBagLayout());
				GridBagConstraints cs = new GridBagConstraints();
				
				cs.fill = GridBagConstraints.HORIZONTAL;
				cs.insets = new Insets(0, 0, 5, 5);
				
				JLabel labelClient = new JLabel("Cliente");
				cs.gridx = 0;
				cs.gridy = 0;
				cs.gridwidth = 2;
				dialog.add(labelClient, cs);
				
				JTextField textClient = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 0;
				cs.gridwidth = 30;
				dialog.add(textClient, cs);
				
				JLabel labelClientCode = new JLabel("Codigo Cliente");
				cs.gridx = 0;
				cs.gridy = 1;
				cs.gridwidth = 2;
				dialog.add(labelClientCode, cs);
				
				JTextField textClientCode = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 1;
				cs.gridwidth = 30;
				dialog.add(textClientCode, cs);
				
				JLabel labelRepresentative = new JLabel("Representante");
				cs.gridx = 0;
				cs.gridy = 2;
				cs.gridwidth = 2;
				dialog.add(labelRepresentative, cs);
				
				JTextField textRepresentative = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 2;
				cs.gridwidth = 30;
				dialog.add(textRepresentative, cs);
				
				JLabel labelRif = new JLabel("Rif");
				cs.gridx = 0;
				cs.gridy = 3;
				cs.gridwidth = 2;
				dialog.add(labelRif, cs);
				
				JTextField textRif = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 3;
				cs.gridwidth = 30;
				dialog.add(textRif, cs);
				
				JLabel labelAddress = new JLabel("Direccion");
				cs.gridx = 0;
				cs.gridy = 4;
				cs.gridwidth = 2;
				dialog.add(labelAddress, cs);
				
				JTextField textAddress = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 4;
				cs.gridwidth = 30;
				dialog.add(textAddress, cs);
				
				JLabel labelPhone = new JLabel("Telefono");
				cs.gridx = 0;
				cs.gridy = 5;
				cs.gridwidth = 2;
				dialog.add(labelPhone, cs);
				
				JTextField textPhone = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 5;
				cs.gridwidth = 30;
				dialog.add(textPhone, cs);
				
				JLabel labelEmail = new JLabel("Email");
				cs.gridx = 0;
				cs.gridy = 6;
				cs.gridwidth = 2;
				dialog.add(labelEmail, cs);
				
				JTextField textEmail = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 6;
				cs.gridwidth = 30;
				dialog.add(textEmail, cs);
				
				JLabel labelInstagram = new JLabel("Instagram");
				cs.gridx = 0;
				cs.gridy = 7;
				cs.gridwidth = 2;
				dialog.add(labelInstagram, cs);
				
				JTextField textInstagram = new JTextField(10);
				JLabel labelInstagramOptional = new JLabel("(Opcional)");
				JPanel panelInstagram = new JPanel(new FlowLayout());
				panelInstagram.add(textInstagram);
				panelInstagram.add(labelInstagramOptional);
				cs.gridx = 2;
				cs.gridy = 7;
				cs.gridwidth = 11;
				dialog.add(panelInstagram, cs);
				
				JLabel labelFacebook = new JLabel("Facebook");
				cs.gridx = 0;
				cs.gridy = 8;
				cs.gridwidth = 2;
				dialog.add(labelFacebook, cs);
				
				JTextField textFacebook = new JTextField(10);
				JLabel labelFacebookOptional = new JLabel("(Opcional)");
				JPanel panelFacebook = new JPanel(new FlowLayout());
				panelFacebook.add(textFacebook);
				panelFacebook.add(labelFacebookOptional);
				cs.gridx = 2;
				cs.gridy = 8;
				cs.gridwidth = 11;
				dialog.add(panelFacebook, cs);
				
				JLabel labelTwitter = new JLabel("Twitter");
				cs.gridx = 0;
				cs.gridy = 9;
				cs.gridwidth = 2;
				dialog.add(labelTwitter, cs);
				
				JTextField textTwitter = new JTextField(10);
				JLabel labelTwitterOptional = new JLabel("(Opcional)");
				JPanel panelTwitter = new JPanel(new FlowLayout());
				panelTwitter.add(textTwitter);
				panelTwitter.add(labelTwitterOptional);
				cs.gridx = 2;
				cs.gridy = 9;
				cs.gridwidth = 11;
				dialog.add(panelTwitter, cs);
				
				JButton buttonCreate = new JButton("Crear");
				buttonCreate.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent paramActionEvent) {
						String client = textClient.getText();
						String clientCode = textClientCode.getText();
						String representative = textRepresentative.getText();
						String rif = textRif.getText();
						String address = textAddress.getText();
						String phone = textPhone.getText();
						String email = textEmail.getText();
						String instagram = textInstagram.getText();
						String facebook = textFacebook.getText();
						String twitter = textTwitter.getText();
						Errors err = new Errors();
						
						if(client.isEmpty()) {
							err.add("El nombre del cliente no puede estar vacio");
						}
						
						if (client.length() < 4) {
							err.add("El nombre del cliente debe tener como minimo 4 caracteres");
						}
						
						if(clientCode.isEmpty()) {
							err.add("El codigo del cliente no puede estar vacio");
						}
						
						if (clientCode.length() < 4) {
							err.add("El codigo del cliente debe tener como minimo 4 caracteres");
						}
						
						if(representative.isEmpty()) {
							err.add("El nombre del representante no puede estar vacio");
						}
						
						if (representative.length() < 4) {
							err.add("El nombre del representante debe tener como minimo 4 caracteres");
						}
						
						// Regex para rif
						String regex = "^[vejpgVEJPG]{1}[-]\\d{8}[-]\\d{1}$";
						Pattern pattern = Pattern.compile(regex);
						Matcher match = pattern.matcher(rif);
						
						if (!match.find()) {
							err.add("El rif del cliente no tiene un formato valido. El formato debe ser j-12345678-9");
						}
						
						if(address.isEmpty()) {
							err.add("La direccion del cliente no puede estar vacia");
						}
						
						if (address.length() < 4) {
							err.add("La direccion del cliente debe tener como minimo 4 caracteres");
						}
						
						if(phone.isEmpty()) {
							err.add("El telefono del cliente no puede estar vacio");
						}
						
						if (phone.length() < 7) {
							err.add("El telefono del cliente debe tener como minimo 7 caracteres");
						}
						
						// Regex para email
						regex = "^\\S+[@]\\S+[.]\\S+$";
						pattern = Pattern.compile(regex);
						match = pattern.matcher(email);
						
						if(!match.find()) {
							err.add("El email del cliente tiene un formato invalido. El formato debe ser usuario@correo.com");
						}
						
						if(err.isEmpty()) {
							if(db.addClient(client, clientCode, representative, rif, address, phone, email, instagram, facebook, twitter)) {
								loadClientSearchTable("");
								dialog.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el cliente");
							}
						} else {
							err.dump();
						}
					}
				});
				cs.gridx = 0;
				cs.gridy = 10;
				cs.gridwidth = 6;
				dialog.add(buttonCreate, cs);
				JRootPane rootPane = SwingUtilities.getRootPane(buttonCreate);
				rootPane.setDefaultButton(buttonCreate);
				
				JButton buttonCancel = new JButton("Cancelar");
				buttonCancel.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent paramActionEvent) {
						dialog.dispose();
					}
				});
				cs.gridx = 6;
				cs.gridy = 10;
				cs.gridwidth = 6;
				dialog.add(buttonCancel, cs);
				
				dialog.setVisible(true);
			}
		});
		
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
	
	private void loadClientSearchTable(String whereQuery) {
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
		
		HashSet<Integer> editableColumns = new HashSet<Integer>();
		editableColumns.add(1);
		editableColumns.add(2);
		editableColumns.add(3);
		editableColumns.add(4);
		
		if(tableCompaniesSearchResult.getModel() instanceof MyTableModel) {
			((MyTableModel) tableCompaniesSearchResult.getModel()).setQuery(companiesQuery);
		} else {
			MyTableModel mForTable = new MyTableModel(companiesQuery, companiesColumnNames, "clients", editableColumns);
			tableCompaniesSearchResult.setModel(mForTable);
		}
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
