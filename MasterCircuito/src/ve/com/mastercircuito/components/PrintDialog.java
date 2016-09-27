package ve.com.mastercircuito.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ve.com.mastercircuito.db.Db;

public class PrintDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4154898197696584979L;
	
	private Integer height = 200;
	private Integer width = 300;
	
	private Db db;
	
	
	
	public PrintDialog(Window owner) {
		this(owner, "");
	}
	
	public PrintDialog(Window owner, String title) {
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
		panelCenter.add(createPrintMainPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCenter.add(createPrintButtonPanel());
		this.add(panelCenter, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	private JPanel createPrintMainPanel() {
		JRadioButton detailedPrint = new JRadioButton("Detallado (Interno)");
		detailedPrint.setSelected(true);
		JRadioButton normalPrint = new JRadioButton("Sencillo (Cliente)");
		JRadioButton pricelessPrint = new JRadioButton("Sin precios");
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(detailedPrint);
		radioGroup.add(normalPrint);
		radioGroup.add(pricelessPrint);
		
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(detailedPrint);
        radioPanel.add(normalPrint);
        radioPanel.add(pricelessPrint);
		return radioPanel;
	}
	
	private JPanel createPrintButtonPanel() {
		JPanel panelPrintButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton buttonAccept = new JButton("Aceptar");
		panelPrintButtons.add(buttonAccept);
		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panelPrintButtons.add(buttonCancel);
		return panelPrintButtons;
	}
	
}
