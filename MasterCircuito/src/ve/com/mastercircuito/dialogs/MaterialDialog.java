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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.Errors;
import ve.com.mastercircuito.utils.Numbers;

public class MaterialDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5681423257155599360L;
	/**
	 * 
	 */
	
	private Integer height = 300;
	private Integer width = 800;
	
	private Db db;
	private Object[][] materialsSearchData;
	private JTable tableMaterialsSearchResult;
	private ListSelectionModel listMaterialSearchSelectionModel;
	private JTextField textSearchMaterialReference, textSearchMaterialDescription;
	
	private String[] materialsColumnNames = { "Id", "Referencia", "Descripcion", "Precio"};
	
	private int materialSearchId = 0;
	private int materialAddQuantity = 1;
	private double materialAddPrice = 0.00;
	
	public MaterialDialog(Window owner) {
		this(owner, "");
	}
	
	public MaterialDialog(Window owner, String title) {
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
		panelCenter.add(createMaterialAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCenter.add(createMaterialAddTablePanel());
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createMaterialAddCountPanel());
		panelLower.add(createMaterialAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public int getMaterialSearchId() {
		return this.materialSearchId;
	}
	
	public Integer getMaterialAddQuantity() {
		return this.materialAddQuantity;
	}
	
	public double getMaterialAddPrice() {
		return this.materialAddPrice;
	}
	
	private JPanel createMaterialAddSearchPanel() {
		JPanel panelMaterialAddSearch = new JPanel();
		
		panelMaterialAddSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel labelReference = new JLabel("Referencia:");
		JLabel labelDescription = new JLabel("Descripcion:");
		
		textSearchMaterialReference = new JTextField(10);
		textSearchMaterialReference.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				loadMaterialSearchTable();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
		});
		panelMaterialAddSearch.add(labelReference);
		panelMaterialAddSearch.add(textSearchMaterialReference);
		
		textSearchMaterialDescription = new JTextField(10);
		textSearchMaterialDescription.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				loadMaterialSearchTable();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
		});
		panelMaterialAddSearch.add(labelDescription);
		panelMaterialAddSearch.add(textSearchMaterialDescription);
		
		return panelMaterialAddSearch;
	}
	
	private JPanel createMaterialAddTablePanel() {
		
		String materialsQuery = "SELECT materials.id, "
				+ "materials.reference, "
				+ "materials.material, "
				+ "materials.price "
			+ "FROM materials "
			+ "LIMIT 20";

		materialsSearchData = db.fetchAll(db.select(materialsQuery));
		
		HashSet<Integer> editableColumns = new HashSet<Integer>();
		editableColumns.add(new Integer(1));
		editableColumns.add(new Integer(2));
		editableColumns.add(new Integer(3));
		
		tableMaterialsSearchResult = new JTable();
		
		if(materialsSearchData.length > 0) {
			tableMaterialsSearchResult.setModel(new MyTableModel(materialsQuery, materialsColumnNames, "materials", editableColumns));
		} else {
			tableMaterialsSearchResult.setModel(new DefaultTableModel());
		}
		
		tableMaterialsSearchResult.setAutoCreateRowSorter(true);
		tableMaterialsSearchResult.getTableHeader().setReorderingAllowed(false);
		
		MyListSelectionListener lForList = new MyListSelectionListener();
		
		listMaterialSearchSelectionModel = tableMaterialsSearchResult.getSelectionModel();
		listMaterialSearchSelectionModel.addListSelectionListener(lForList);
		tableMaterialsSearchResult.setSelectionModel(listMaterialSearchSelectionModel);
		tableMaterialsSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelMaterialAddTable = new JPanel(new BorderLayout());
		panelMaterialAddTable.add(tableMaterialsSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelMaterialAddTable.add(tableMaterialsSearchResult, BorderLayout.CENTER);
		
		return panelMaterialAddTable;
	}
	
	private JPanel createMaterialAddCountPanel() {
		JPanel panelCountOuter = new JPanel(new BorderLayout());
		JPanel panelCountInner = new JPanel();
		
		panelCountInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonDecrease = new JButton("-");
		JButton buttonIncrease = new JButton("+");
		JLabel labelQuantity = new JLabel("1");
		materialAddQuantity = 1;
		materialAddPrice = 0.00;
		Integer min = 1;
		Integer max = 100;
		
		panelCountInner.add(buttonDecrease);
		panelCountInner.add(labelQuantity);
		panelCountInner.add(buttonIncrease);
		
		buttonDecrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(materialAddQuantity > min) {
					materialAddQuantity--;
					labelQuantity.setText(String.valueOf(materialAddQuantity));
				}
			}
		});
		
		buttonIncrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(materialAddQuantity < max) {
					materialAddQuantity++;
					labelQuantity.setText(String.valueOf(materialAddQuantity));
				}
			}
		});
		
		panelCountOuter.add(panelCountInner, BorderLayout.CENTER);
		
		return panelCountOuter;
	}
	
	private JPanel createMaterialAddButtonPanel() {
		JPanel panelOuter = new JPanel(new BorderLayout());
		JPanel panelInner = new JPanel();
		panelInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonAddNewMaterial = new JButton("Crear Nuevo");
		JButton buttonAccept = new JButton("Aceptar");
		JButton buttonCancel = new JButton("Cancelar");
		panelInner.add(buttonAddNewMaterial);
		panelInner.add(buttonAccept);
		panelInner.add(buttonCancel);
		
		buttonAddNewMaterial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				JDialog dialog = new JDialog();
				dialog.setTitle("Crear Material");
				dialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setSize(new Dimension(500,300));
				dialog.setLocationRelativeTo(null);
				dialog.setLayout(new GridBagLayout());
				GridBagConstraints cs = new GridBagConstraints();
				
				cs.fill = GridBagConstraints.HORIZONTAL;
				cs.insets = new Insets(0, 0, 5, 5);
				
				JLabel labelReference = new JLabel("Referencia");
				cs.gridx = 0;
				cs.gridy = 0;
				cs.gridwidth = 2;
				dialog.add(labelReference, cs);
				
				JTextField textReference = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 0;
				cs.gridwidth = 30;
				dialog.add(textReference, cs);
				
				JLabel labelDescription = new JLabel("Descripcion");
				cs.gridx = 0;
				cs.gridy = 1;
				cs.gridwidth = 2;
				dialog.add(labelDescription, cs);
				
				JTextField textDescription = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 1;
				cs.gridwidth = 30;
				dialog.add(textDescription, cs);
				
				JLabel labelPrice = new JLabel("Precio");
				cs.gridx = 0;
				cs.gridy = 2;
				cs.gridwidth = 2;
				dialog.add(labelPrice, cs);
				
				JTextField textPrice = new JTextField(10);
				cs.gridx = 2;
				cs.gridy = 2;
				cs.gridwidth = 30;
				dialog.add(textPrice, cs);
				
				JButton buttonCreate = new JButton("Crear");
				buttonCreate.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent paramActionEvent) {
						String reference = textReference.getText();
						String description = textDescription.getText();
						String strPrice = textPrice.getText();
						Errors err = new Errors();
						
						if(reference.isEmpty()) {
							err.add("La referencia no puede estar vacia");
						}
						
						if(description.isEmpty()) {
							err.add("La descripcion del material no puede estar vacia");
						}
						
						if(!Numbers.isNumeric(strPrice)) {
							err.add("Precio invalido");
						}
						
						if(err.isEmpty()) {
							double price = Double.parseDouble(strPrice);
							if(db.addMaterial(reference, description, price)) {
								
								loadMaterialSearchTable();
								dialog.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar el material");
							}
						} else {
							err.dump();
						}
					}
				});
				cs.gridx = 0;
				cs.gridy = 3;
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
				cs.gridy = 3;
				cs.gridwidth = 6;
				dialog.add(buttonCancel, cs);
				
				dialog.setVisible(true);
			}
			
		});
		
		buttonAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(tableMaterialsSearchResult.getSelectedRow() > -1) {
					materialAddPrice = Double.valueOf((String) tableMaterialsSearchResult.getValueAt(tableMaterialsSearchResult.getSelectedRow(), 3));
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				materialSearchId = 0;
				materialAddQuantity = 0;
				materialAddPrice = 0.00;
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	private void loadMaterialSearchTable() {
		
		String whereQuery = "";
		
		if(null != textSearchMaterialReference && !textSearchMaterialReference.getText().isEmpty()) {
			whereQuery += this.selectWhereQuery(whereQuery) + " materials.reference LIKE '%" + textSearchMaterialReference.getText() + "%'";
		}
		if(null != textSearchMaterialDescription && !textSearchMaterialDescription.getText().isEmpty()) {
			whereQuery += this.selectWhereQuery(whereQuery) + " materials.material LIKE '%" + textSearchMaterialDescription.getText() + "%'";
		}
		
		String materialsQuery = "SELECT materials.id, "
				+ "materials.reference, "
				+ "materials.material, "
				+ "materials.price "
			+ "FROM materials "
			+ whereQuery
			+ " LIMIT 20";

		materialsSearchData = db.fetchAll(db.select(materialsQuery));
		
		HashSet<Integer> editableColumns = new HashSet<Integer>();
		editableColumns.add(new Integer(1));
		editableColumns.add(new Integer(2));
		editableColumns.add(new Integer(3));
		
		if(materialsSearchData.length > 0) {
			tableMaterialsSearchResult.setModel(new MyTableModel(materialsQuery, materialsColumnNames, "materials", editableColumns));
		} else {
			tableMaterialsSearchResult.setModel(new DefaultTableModel());
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
	
	private class MyListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent ev) {
			ListSelectionModel lsm = (ListSelectionModel) ev.getSource();
			
			if(null != tableMaterialsSearchResult 
					&& tableMaterialsSearchResult.isFocusOwner() 
					&& lsm.getMinSelectionIndex() > -1) {
				materialSearchId = Integer.valueOf((String)tableMaterialsSearchResult.getValueAt(tableMaterialsSearchResult.getSelectedRow(), 0));
			}
		}
		
	}
	
}
