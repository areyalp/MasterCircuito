package ve.com.mastercircuito.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;

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
		this(owner, "", "", "", "", "");
	}
	
	@SuppressWarnings("rawtypes")
	public PrintDialog(Window owner, String title, String key, Object value, String key2, Object value2) {
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
		this.setParameter(key2, value2);
		this.setParameter("logoini", this.getClass().getResource("logoini.png").getPath());
		this.setParameter("logofondonorma", this.getClass().getResource("logofondonorma.png").getPath());
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
				 
				try {
					JasperReport report = (JasperReport) JRLoader.loadObjectFromFile( "Presupuesto.jasper" );
					JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametersMap, db.getConnection());
					String savePath = "";
					
					String documentsDirPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
					String desktopDirPath = System.getProperty("user.home") + "/Desktop";
					
					File softparkDocumentsDir = new File(documentsDirPath + "/SoftPark");
					File softparkDesktopDir = new File(desktopDirPath + "/SoftPark");
					if(!softparkDesktopDir.exists()) {
						if(softparkDesktopDir.mkdir()) {
							savePath = softparkDesktopDir.getPath() + "/";
						} else {
							if(!softparkDocumentsDir.exists()) {
								if(softparkDocumentsDir.mkdir()) {
									savePath = softparkDocumentsDir.getPath() + "/";
								}
							}
						}
					} else {
						savePath = softparkDesktopDir.getPath() + "/";
					}
					File pdf = new File(savePath + "presupuesto " + parametersMap.get("budgetcode")+".pdf");
					if(pdf.exists()) {
						Integer answer = JOptionPane.showConfirmDialog(null, "Este pdf ya esta creado en " + pdf.getPath() + ", desea sobreescribirlo?", "Confirmar sobreescritura", JOptionPane.YES_NO_OPTION);
						if(answer == JOptionPane.YES_OPTION) {
							FileOutputStream fos = new FileOutputStream(pdf);
							JasperExportManager.exportReportToPdfStream(jasperPrint, fos);
							fos.close();
						}
					}
				}
				catch( JRException | IOException  ex ) {
					ex.printStackTrace();
				}
				finally {
					dispose();
				}
			}
		});				
		
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
