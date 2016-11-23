package ve.com.mastercircuito.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ve.com.mastercircuito.components.Client;
import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.utils.StringTools;

import java.awt.EventQueue;

public class Test {
	private static ConnectionDB con;
	
	public static void main(String[] args) throws Exception {
//		DateTime dt = new DateTime(new Date());
//		int year = dt.getYear();
//		Db db = new Db();
//		String queryInsert = "INSERT INTO budget_code_ids (budget_code_id, year) VALUES(2, " + year + "),(3, " + year + ")";
//		db.insert(queryInsert);
//		db.delete("DELETE FROM budget_code_ids WHERE year < 2017");
//		db.query("ALTER TABLE budget_code_ids AUTO_INCREMENT=1");
		
//		String ClientCode = "SELECT clients.client_code"
//				+ "FROM clients "
//				+ "WHERE clients.id = '1'";
//		
//		String Cliente = "SELECT clients.client "
//				+ "FROM clients "
//				+ "WHERE clients.id = '1'";
//		
//		String Representative = "SELECT clients.representative "
//				+ "FROM clients "
//				+ "WHERE clients.id = '1'";
//		
//		String WorkName = "SELECT budgets.work_name "
//				+ "FROM budgets "
//				+ "WHERE budgets.id = '1'";
//		
//		String Date = "SELECT budgets.date "
//				+ "FROM budgets "
//				+ "WHERE budgets.id = '1'";
//		
//		String Code = "SELECT budgets.code "
//				+ "FROM budgets "
//				+ "WHERE budgets.id = '1'";
//		
//		//lOADING THE REPORT
//		
//		JasperReport report = (JasperReport) JRLoader.loadObjectFromFile( "C:\\Users\\Daniel y Brenda\\JaspersoftWorkspace\\MyReports\\Presupuesto.jasper" );
//
//		//FILLING THE REPORT WITH THE NEW DATA
//		//LOADING THE PARAMETERS
//		Map<String, Object> parametros = new HashMap<String, Object>();
//        parametros.put("code", Code);
//        parametros.put("client", Cliente );
//        parametros.put("clientcode", ClientCode);
//        parametros.put("date", Date);
//        parametros.put("work_name", WorkName);
//        parametros.put("representative", Representative );
//        
//        //Adding the fields from the budget
//        
////        List<Description> listaDescription = new ArrayList<Description>();
////        Description d = new Description("nro ", "quantity ", "description_budget ", "price ", "total_price ");
//
//        List<Client> listaClient = new ArrayList<Client>();       
//
//        for (int i = 1; i <= 2; i++)
//        {
//        	 Client d = new Client(i, "id " + i, "client " + i, "code  " + i, "representative " + i,  "rif " + i, "address " + i, "phone " + i, "email " + i, "facebookProfile " + i, "twitterUser " + i);
//        	 listaClient.add(d);
//        }
//        
//		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(listaClient));
//		JasperViewer.viewReport(jasperPrint);    //VIEWER OF THE JASPER PRINT OBJECT
//		//EXPORTING THE PDF FILE
//		try { 
//			JasperExportManager.exportReportToPdfFile("src\\reporte2.pdf");
//		}
//		catch( JRException ex ) {
//			ex.printStackTrace();
//		}
	
		conectToDatabase();

		Map parametersMap = new HashMap();  
		parametersMap.put("budgetid",2);
		
		JasperReport report = (JasperReport) JRLoader.loadObjectFromFile( "Presupuesto.jasper" );

		
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametersMap, con.getConn());
		
		JasperViewer.viewReport(jasperPrint);    //VIEWER OF THE JASPER PRINT OBJECT

		
//		openReportFrame();
	}
	
	private static void conectToDatabase()
	{
		String	driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/";
		String	db	= "mastercircuito";

		con = new ConnectionDB( driver, db, url );
		con.connect();
	}
	
	private static void openReportFrame()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReportFrame frame = new ReportFrame( con.getConn() );
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
		
		
	}

