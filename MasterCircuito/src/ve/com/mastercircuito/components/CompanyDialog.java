package ve.com.mastercircuito.components;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ve.com.mastercircuito.db.Db;

public class CompanyDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104723883961973726L;
	
	
	
	private Integer height = 300;
	private Integer width = 1200;
	
	private Db db;
	private Object[][] companiesSearchData;
	private JTable tableCompaniesSearchResult;
	private ListSelectionModel listCompanySearchSelectionModel;
	
	public CompanyDialog() {
		
	}
	
	
	
}
