package ve.com.mastercircuito.production;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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

import ve.com.mastercircuito.components.Budget;
import ve.com.mastercircuito.components.MyInternalFrame;
import ve.com.mastercircuito.components.ProductionOrder;
import ve.com.mastercircuito.components.WorkOrder;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.font.Fa;

public class MainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717733090547682708L;
	
	private Db db;
	
	private JDesktopPane desktop;
	private JPanel theToolBarPanel;
	private JToolBar toolBar;
	private JButton toolBarButtonProduction;
	
	private MyInternalFrame productionFrame;
	
	private JList<Budget> listInbox;
	private ListSelectionModel listInboxSelectionModel;
	private JList<ProductionOrder> listProductionOrders;
	private JList<WorkOrder> listWorkOrders;
	private JButton buttonProcessBudget, buttonProcessProductionOrder;
	
	public static void main(String[] args) {
		new ve.com.mastercircuito.production.MainView();
	}
	
	protected MainView() {
		
		db = new Db();
		
		/*LoginDialog lDialog = new LoginDialog(null);
		
		lDialog.setVisible(true);
		
		if(!lDialog.isSucceeded()) {
			System.exit(0);
		}*/
		
		DateTime dt = new DateTime();
		
		if(dt.isAfter(new DateTime(2016, 10, 30, 0, 0))) {
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
		productionFrame = new MyInternalFrame("Interruptores");
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
		
		productionFrame.add(createProductionMainPanel());
		productionFrame.setVisible(true);
		desktop.add(productionFrame);
		try{
			productionFrame.setSelected(true);
		} catch(java.beans.PropertyVetoException e) {
			
		}
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
		listProductionOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProductionOrders.setLayoutOrientation(JList.VERTICAL);
		listProductionOrders.setVisibleRowCount(-1);
		listProductionOrders.addListSelectionListener(lForList);
		
		JScrollPane listProductionOrdersScroller = new JScrollPane(listProductionOrders);
		listProductionOrdersScroller.setPreferredSize(new Dimension(300, 400));
		
		cs.gridx = 12;
		cs.gridy = 1;
		cs.gridwidth = 10;
		cs.gridheight = 10;
		panelProduction.add(listProductionOrdersScroller, cs);
		
		buttonProcessProductionOrder = new JButton("Procesar");
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
		listWorkOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listWorkOrders.setLayoutOrientation(JList.VERTICAL);
		listWorkOrders.setVisibleRowCount(-1);
		listWorkOrders.addListSelectionListener(lForList);
		
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
			
			if (actionCommand.equalsIgnoreCase("button.budget.refresh")) {
				Db db = new Db();
				ArrayList<Budget> budgets = new ArrayList<Budget>();
				
				budgets = db.getBudgets(1);
				
				DefaultListModel<Budget> model = new DefaultListModel<Budget>();
				for (Budget budget: budgets) {
					model.addElement(budget);
				}
				listInbox.setModel(model);
				
			}
		}
		
	}
	
	private class ListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent ev) {
			
			ListSelectionModel lsm = (ListSelectionModel)ev.getSource();
			
			if (!ev.getValueIsAdjusting()) {
				
				if (listInbox.getSelectedIndex() != -1 && listInbox.isFocusOwner()) {
					buttonProcessBudget.setEnabled(true);
				} else if (listProductionOrders.getSelectedIndex() != -1 && listProductionOrders.isFocusOwner()) {
					buttonProcessProductionOrder.setEnabled(true);
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
	
}