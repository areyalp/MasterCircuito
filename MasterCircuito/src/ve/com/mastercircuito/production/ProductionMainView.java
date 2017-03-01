package ve.com.mastercircuito.production;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.joda.time.DateTime;

import ve.com.mastercircuito.components.MyInternalFrame;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.dialogs.LoginDialog;
import ve.com.mastercircuito.dialogs.PrintDialog;
import ve.com.mastercircuito.font.Fa;
import ve.com.mastercircuito.objects.Board;
import ve.com.mastercircuito.objects.Box;
import ve.com.mastercircuito.objects.Budget;
import ve.com.mastercircuito.objects.Product;
import ve.com.mastercircuito.objects.ProductType;
import ve.com.mastercircuito.objects.ProductionOrder;
import ve.com.mastercircuito.objects.Switch;
import ve.com.mastercircuito.objects.User;
import ve.com.mastercircuito.objects.WorkOrder;

public class ProductionMainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717733090547682708L;
	public static final Integer LIST_INBOX = 0;
	public static final Integer LIST_PRODUCTION = 1;
	public static final Integer LIST_WORK = 2;
	
	private User user;
	
	private JDesktopPane desktop;
	private JPanel theToolBarPanel;
	private JToolBar toolBar;
	private JButton toolBarButtonProduction;
	private PrintDialog workOrderPrintDialog;
	private PrintDialog productionOrderPrintDialog;
	
	private MyInternalFrame productionFrame;
	
	private Integer selectedList = -1;
	private JList<Budget> listInbox;
	private JList<ProductionOrder> listProductionOrders;
	private JList<WorkOrder> listWorkOrders;
	private ListSelectionModel listInboxSelectionModel, listProductionOrdersSelectionModel, listWorkOrdersSelectionModel;
	private JButton buttonProcessBudget, buttonProcessProductionOrder;
	private Integer selectedProductionOrderId, selectedWorkOrderBudgetId, selectedWorkOrderId, selectedWorkOrderProductId;
	
	public static void main(String[] args) {
		new ve.com.mastercircuito.production.ProductionMainView();
	}
	
	protected ProductionMainView() {
		
		LoginDialog lDialog = new LoginDialog(null);
		
		lDialog.setVisible(true);
		
		if(!lDialog.isSucceeded()) {
			System.exit(0);
		}
		
		user = new User(lDialog.getUsername());
		
		user.fetchUserInfo();
		
		DateTime dt = new DateTime();
		
		if(dt.isAfter(new DateTime(2017, 03, 01, 0, 0))) {
			JOptionPane.showMessageDialog(null, "Error, debe comunicarse con el programador");
			System.exit(0);
		}
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800,600));
		this.setIconImage(new ImageIcon("resources/logo.png").getImage());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		int x = (dim.width / 2) - (this.getWidth()/2);
		int y = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setPreferredSize(dim);
		this.setLocation(x, y);
		this.setTitle("MasterCircuito / Produccion - ( areyalp ) Jesus Flores");
		this.setLayout(new BorderLayout(50,50));
		
		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
		toolBarPanel.add(createToolbar());
		this.add(toolBarPanel, BorderLayout.NORTH);
		
		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		this.add(desktop, BorderLayout.CENTER);
		
		this.selectedList = -1;
		
		this.setVisible(true);
	}
	
	private JPanel createToolbar() {
		theToolBarPanel = new JPanel(new BorderLayout());
		
		ToolbarButtonListener lForToolbarButton = new ToolbarButtonListener();
		
		toolBar = new JToolBar("Barra de Herramientas");
		
		toolBarButtonProduction = new JButton("Produccion", new ImageIcon("resources/interruptor_32x32.jpg"));
		
		toolBarButtonProduction.setActionCommand("bar.button.production");
		
		toolBarButtonProduction.addActionListener(lForToolbarButton);
		
		toolBar.add(toolBarButtonProduction);
		
		toolBar.setFloatable(false);
		
		theToolBarPanel.add(toolBar, BorderLayout.NORTH);
		
		return theToolBarPanel;
	}
	
	private void createProductionFrame() {
		productionFrame = new MyInternalFrame("Produccion");
		productionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		productionFrame.setMaximizable(false);
		productionFrame.setFrameIcon(new ImageIcon("resources/interruptor_32x32.jpg"));
		productionFrame.updateUI();
		double aspectRatio = 0.85;
		int w = (int) (desktop.getWidth() * aspectRatio);
		int h = (int)(desktop.getHeight()*aspectRatio);
		productionFrame.setSize(new Dimension(w,h));
		int x = (desktop.getWidth() / 2) - (productionFrame.getWidth() / 2);
		int y = (desktop.getHeight() / 2) - (productionFrame.getHeight() / 2);
		productionFrame.setLocation(new Point(x, y));
		productionFrame.setLayout(new BorderLayout(50,50));
		
		productionFrame.add(createProductionTopPanel(), BorderLayout.NORTH);
		
		productionFrame.add(createProductionMainPanel(), BorderLayout.CENTER);
		productionFrame.setVisible(true);
		desktop.add(productionFrame);
		try{
			productionFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
	}
	
	private JPanel createProductionTopPanel() {
		JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		
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
		
		PrinterButtonListener lForPrinterButton = new PrinterButtonListener();
		
		JButton buttonProductionPrint = new JButton(Fa.fa_print);
		buttonProductionPrint.setContentAreaFilled(false);
		buttonProductionPrint.setActionCommand("print.order");
		buttonProductionPrint.addActionListener(lForPrinterButton);
		buttonProductionPrint.setMargin(new Insets(0, 0, 0, 0));
		buttonProductionPrint.setFocusPainted(true);
		buttonProductionPrint.setBorderPainted(false);
		buttonProductionPrint.setFont(fa);
		buttonProductionPrint.setForeground(Color.GREEN);
		panelTop.add(buttonProductionPrint);
		
		return panelTop;
	}
	
	private JPanel createProductionMainPanel() {
		JPanel panelProduction = new JPanel();
		
		panelProduction.setLayout(new GridBagLayout());
		
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.BOTH;
		cs.insets = new Insets(0, 0, 5, 5);
		
		JLabel labelInbox = new JLabel("Bandeja de Entrada");
		cs.weightx = 0.2;
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 9;
		cs.gridheight = 1;
		panelProduction.add(labelInbox, cs);
		
		ListListener lForList = new ListListener();
		
		listInbox = new JList<Budget>(new DefaultListModel<Budget>());
		listInbox.setLayoutOrientation(JList.VERTICAL);
		listInbox.setVisibleRowCount(-1);
		listInboxSelectionModel = listInbox.getSelectionModel();
		listInboxSelectionModel.addListSelectionListener(lForList);
		listInbox.setSelectionModel(listInboxSelectionModel);
		listInbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listInboxScroller = new JScrollPane(listInbox);
		listInboxScroller.setPreferredSize(new Dimension(300, 400));
		
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 10;
		cs.gridheight = 10;
		panelProduction.add(listInboxScroller, cs);
		
		Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is;
			is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 24f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		ButtonListener lForButton = new ButtonListener();
		
		JButton buttonBudgetsRefresh = new JButton(Fa.fa_refresh);
		buttonBudgetsRefresh.setFont(fa);
		buttonBudgetsRefresh.setActionCommand("button.budget.refresh");
		buttonBudgetsRefresh.addActionListener(lForButton);
		cs.weightx = 0.1;
		cs.gridx = 9;
		cs.gridy = 0;
		cs.gridwidth = 1;
		cs.gridheight = 1;
		panelProduction.add(buttonBudgetsRefresh, cs);
		
		buttonProcessBudget = new JButton("Procesar");
		buttonProcessBudget.setActionCommand("button.order.production.process");
		buttonProcessBudget.addActionListener(lForButton);
		buttonProcessBudget.setEnabled(false);
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.gridx = 11;
		cs.gridy = 10;
		cs.gridwidth = 1;
		cs.gridheight = 1;
		panelProduction.add(buttonProcessBudget, cs);
		
		JLabel labelProductionOrders = new JLabel("Ordenes de Produccion");
		cs.fill = GridBagConstraints.BOTH;
		cs.gridx = 12;
		cs.gridy = 0;
		cs.gridwidth = 10;
		cs.gridheight = 1;
		panelProduction.add(labelProductionOrders, cs);
		
		listProductionOrders = new JList<ProductionOrder>(new DefaultListModel<ProductionOrder>());
		listProductionOrders.setLayoutOrientation(JList.VERTICAL);
		listProductionOrders.setVisibleRowCount(-1);
		listProductionOrdersSelectionModel = listProductionOrders.getSelectionModel();
		listProductionOrdersSelectionModel.addListSelectionListener(lForList);
		listProductionOrders.setSelectionModel(listProductionOrdersSelectionModel);
		listProductionOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listProductionOrdersScroller = new JScrollPane(listProductionOrders);
		listProductionOrdersScroller.setPreferredSize(new Dimension(300, 400));
		
		cs.gridx = 12;
		cs.gridy = 1;
		cs.gridwidth = 10;
		cs.gridheight = 10;
		panelProduction.add(listProductionOrdersScroller, cs);
		
		buttonProcessProductionOrder = new JButton("Procesar");
		buttonProcessProductionOrder.setActionCommand("button.order.work.process");
		buttonProcessProductionOrder.addActionListener(lForButton);
		buttonProcessProductionOrder.setEnabled(false);
		cs.fill = GridBagConstraints.HORIZONTAL;
		cs.gridx = 23;
		cs.gridy = 10;
		cs.gridwidth = 1;
		cs.gridheight = 1;
		panelProduction.add(buttonProcessProductionOrder, cs);
		
		JLabel labelWorkOrders = new JLabel("Ordenes de Trabajo");
		cs.fill = GridBagConstraints.BOTH;
		cs.gridx = 24;
		cs.gridy = 0;
		cs.gridwidth = 10;
		cs.gridheight = 1;
		panelProduction.add(labelWorkOrders, cs);
		
		listWorkOrders = new JList<WorkOrder>(new DefaultListModel<WorkOrder>());
		listWorkOrders.setLayoutOrientation(JList.VERTICAL);
		listWorkOrders.setVisibleRowCount(-1);
		listWorkOrdersSelectionModel = listWorkOrders.getSelectionModel();
		listWorkOrdersSelectionModel.addListSelectionListener(lForList);
		listWorkOrders.setSelectionModel(listWorkOrdersSelectionModel);
		listWorkOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listWorkOrdersScroller = new JScrollPane(listWorkOrders);
		listWorkOrdersScroller.setPreferredSize(new Dimension(300, 400));
		
		cs.gridx = 24;
		cs.gridy = 1;
		cs.gridwidth = 10;
		cs.gridheight = 10;
		panelProduction.add(listWorkOrdersScroller, cs);
		
		return panelProduction;
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {
			String actionCommand = ev.getActionCommand();
			Db db = new Db();
			
			if (actionCommand.equalsIgnoreCase("button.budget.refresh")) {
				
				ArrayList<Budget> budgets = new ArrayList<Budget>();
				
				budgets = db.getBudgets(1);
				
				DefaultListModel<Budget> model = new DefaultListModel<Budget>();
				for (Budget budget: budgets) {
					model.addElement(budget);
				}
				listInbox.setModel(model);
				
			} else if (actionCommand.equalsIgnoreCase("button.order.production.process")) {
				if(listInbox.getSelectedIndex() > -1) {
					listWorkOrders.setModel(new DefaultListModel<WorkOrder>());
					Budget selectedBudget = new Budget(listInbox.getSelectedValue());
					if (!ProductionOrder.exists(selectedBudget.getId())) {
						Integer orderId = ProductionOrder.pushToDb(selectedBudget.getId(), user.getId());
						ProductionOrder newProductionOrder = new ProductionOrder();
						newProductionOrder.pullFromDb(orderId);
						DefaultListModel<ProductionOrder> model = new DefaultListModel<ProductionOrder>();
						model.addElement(newProductionOrder);
						listProductionOrders.setModel(model);
					} else {
						ProductionOrder productionOrder = new ProductionOrder();
						productionOrder.pullByBudget(selectedBudget.getId());
						DefaultListModel<ProductionOrder> model = new DefaultListModel<ProductionOrder>();
						model.addElement(productionOrder);
						listProductionOrders.setModel(model);
					}
				}
			} else if (actionCommand.equalsIgnoreCase("button.order.work.process")) {
				if (listProductionOrders.getSelectedIndex() > -1) {
					DefaultListModel<WorkOrder> model = new DefaultListModel<WorkOrder>();
					ProductionOrder selectedProductionOrder = new ProductionOrder(listProductionOrders.getSelectedValue());
					ArrayList<Product> products = new ArrayList<Product>();
					products = db.getProductionOrderProducts(selectedProductionOrder.getId());
					if (!selectedProductionOrder.isProcessed() && products.size() > 0) {
						for (Product product:products) {
							Integer orderId = 0;
							if (product instanceof Switch) {
								orderId = WorkOrder.pushToDb(selectedProductionOrder.getId(), product.getId(), ProductType.SWITCH_TYPE, user.getId());
							} else if (product instanceof Box) {
								orderId = WorkOrder.pushToDb(selectedProductionOrder.getId(), product.getId(), ProductType.BOX_TYPE, user.getId());
							} else if (product instanceof Board) {
								orderId = WorkOrder.pushToDb(selectedProductionOrder.getId(), product.getId(), ProductType.BOARD_TYPE, user.getId());
							}
							WorkOrder workOrder = new WorkOrder();
							workOrder.pullFromDb(orderId);
							model.addElement(workOrder);
						}
						listWorkOrders.setModel(model);
						listProductionOrders.getSelectedValue().setProcessed(true);
						db.setProductionOrderProcessed(selectedProductionOrder.getId());
					} else {
						ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();
						workOrders = WorkOrder.pullAllFromDb(selectedProductionOrder.getId());
						for (WorkOrder workOrder:workOrders) {
							model.addElement(workOrder);
							listWorkOrders.setModel(model);
						}
					}
				}
			}
		}
		
	}
	
	private class ListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent ev) {
			
			ListSelectionModel lsm = (ListSelectionModel)ev.getSource();
			
			if (!ev.getValueIsAdjusting()) {
				selectedList = 0;
				if (listInbox.getSelectedIndex() != -1 && listInbox.isFocusOwner()) {
					buttonProcessBudget.setEnabled(true);
				} else if (listProductionOrders.getSelectedIndex() != -1 && listProductionOrders.isFocusOwner()) {
					buttonProcessProductionOrder.setEnabled(true);
					selectedProductionOrderId = listProductionOrders.getSelectedValue().getId();
					selectedList = ProductionMainView.LIST_PRODUCTION;
				} else if (listWorkOrders.getSelectedIndex() != -1 && listWorkOrders.isFocusOwner()) {
					Db db = new Db();
					Integer WorkOrderId = listWorkOrders.getSelectedValue().getId();
					selectedWorkOrderBudgetId = db.getWorkOrderBudgetId(WorkOrderId);
					selectedWorkOrderId = WorkOrderId;
					selectedWorkOrderProductId = listWorkOrders.getSelectedValue().getProductTypeId();
					selectedList = ProductionMainView.LIST_WORK;
				}
				
				if (lsm.isSelectionEmpty()) {
					buttonProcessBudget.setEnabled(false);
					buttonProcessProductionOrder.setEnabled(false);
				}
				
			}
			
		}
		
	}
	
	private class ToolbarButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equalsIgnoreCase("bar.button.production")) {
				if(null == productionFrame || productionFrame.isClosed()){
					createProductionFrame();
				}
			}
		}
		
	}
	
	private class PrinterButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {
			String actionCommand = ev.getActionCommand();
			
			if (actionCommand.equalsIgnoreCase("print.order")) {
				if(selectedList == ProductionMainView.LIST_PRODUCTION) {
					Map<String, Object> parametersMap = new HashMap<String, Object>();
					parametersMap.put("action_command", "production_order");
					parametersMap.put("production_order_id", selectedProductionOrderId);
					
					productionOrderPrintDialog = new PrintDialog(null, "Imprimir Orden de Produccion", parametersMap);
//					WindowsListener lForWindow = new WindowsListener();
//					productionOrderPrintDialog.addWindowListener(lForWindow);
				} else if(selectedList == ProductionMainView.LIST_WORK) {
					String product = "";
					if (selectedWorkOrderProductId == 1) {
						product = "switch";
					} else if (selectedWorkOrderProductId == 2) {
						product = "box";
					} else if (selectedWorkOrderProductId == 3) {
						product = "board";
					}
					Map<String, Object> parametersMap = new HashMap<String, Object>();
					parametersMap.put("action_command", "work_order");
					parametersMap.put("budgetid", selectedWorkOrderBudgetId);
					parametersMap.put("work_order_id", selectedWorkOrderId);
					parametersMap.put("work_order_product", product);
					
					workOrderPrintDialog = new PrintDialog(null, "Imprimir Orden de Trabajo", parametersMap);
//					WindowsListener lForWindow = new WindowsListener();
//					workOrderPrintDialog.addWindowListener(lForWindow);
				}
			}
		}
	}
	
	private class WindowsListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent e) {
			
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			Window window = (Window)e.getSource();
			if (window.equals(workOrderPrintDialog)) {
				
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			
			
		}
		
	}
	
}