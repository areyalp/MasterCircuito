package ve.com.mastercircuito.test;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.Connection;


public class ReportFrame extends JFrame
{

	public ReportFrame( Connection conn )
	{
//		AbstractJasperReports.createReport( conn, "C:\\Users\\Daniel y Brenda\\JaspersoftWorkspace\\MyReports\\Presupuesto.jasper" );
		AbstractJasperReports.createReport( conn, "Presupuesto.jasper" );

		initComponents();
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 370, 164);
		getContentPane().setLayout(null);
		( (JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnGenerar = new JButton("Generar PDF");
		btnGenerar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				AbstractJasperReports.showViewer();
			}
		});
		btnGenerar.setBounds(44, 31, 110, 46);
		getContentPane().add(btnGenerar);
		
		JButton btnExportarPdf = new JButton("Exportar PDF");
		btnExportarPdf.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				AbstractJasperReports.exportToPDF( "Presupuesto.pdf" );
			}
		});
		btnExportarPdf.setBounds(197, 31, 110, 46);
		getContentPane().add(btnExportarPdf);
	}

}
