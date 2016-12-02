package ve.com.mastercircuito.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ve.com.mastercircuito.db.Db;



public class PrintDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4154898197696584979L;
	
	private Integer height = 200;
	private Integer width = 300;
	
	private Map<String, Object> parametersMap;
	
	private Db db;	
	
	public PrintDialog(Window owner) {
		this(owner, "", "", "");
	}
	
	@SuppressWarnings("rawtypes")
	public PrintDialog(Window owner, String title, String key, Object value) {
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
		
		this.parametersMap = new HashMap<String, Object>();		
		this.setParameter(key, value);
		this.setParameter("logoini", ClassLoader.getSystemResource("logoini.png").getPath());
		this.setParameter("logofondonorma", ClassLoader.getSystemResource("logofondonorma.png").getPath());
		this.setVisible(true);
	}
	
//	@SuppressWarnings("unchecked")
	public void setParameter(String key, Object value){
		this.parametersMap.put(key, value);
	}
	
	@SuppressWarnings("unused")
	private Object getParameter(String key){
		return this.parametersMap.get(key);
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
		buttonAccept.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
//				Map parametersMap = new HashMap();  
//				parametersMap.put("budgetid",1);	//Change number 1 for the budgetid from the budget
				 
				try {
					
					InputStream jasperStream = getClass().getResourceAsStream("Presupuesto.jasper");
					JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);
					String pdfFile = "src\\Presupuesto.pdf";
//					JasperReport report = (JasperReport) JRLoader.loadObjectFromFile( "Presupuesto.jasper" );
					JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametersMap, db.getConnection());					
					JasperViewer.viewReport(jasperPrint);    //VIEWER OF THE JASPER PRINT OBJECT					 
//					JasperExportManager.exportReportToPdfFile("src\\presupuesto"+parametersMap.get("budgetid")+".pdf");
//					File file = new File("presupuesto.pdf");
//					file.getParentFile().mkdir();
//					file.createNewFile();				
					
					JasperExportManager.exportReportToPdfFile(pdfFile);
					//checkear si el archivo no existe debe  crearlo
					//si existe preguntar si lo quiere sobreescribir filenotfoundexception 
					
					
				}
				catch( JRException  ex ) {
					ex.printStackTrace();
				}
				dispose();
//				JOptionPane.showMessageDialog(null, "Imprimiendo");
				}
			}
		);				
		//
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
