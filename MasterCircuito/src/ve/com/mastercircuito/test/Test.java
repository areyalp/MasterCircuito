package ve.com.mastercircuito.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.joda.time.DateTime;

import com.java4less.xreport.fop.FOProcessor;

import ve.com.mastercircuito.db.Db;
import ve.com.mastercircuito.print.PurchaseOrderHeader;
import ve.com.mastercircuito.print.PurchaseOrderItem;
import ve.com.mastercircuito.print.BuyerInformation;


public class Test {

	public static void main(String[] args) {
		DateTime dt = new DateTime(new Date());
		int year = dt.getYear();
		Db db = new Db();
		String queryInsert = "INSERT INTO budget_code_ids (budget_code_id, year) VALUES(2, " + year + "),(3, " + year + ")";
		db.insert(queryInsert);
		db.delete("DELETE FROM budget_code_ids WHERE year < 2017");
		db.query("ALTER TABLE budget_code_ids AUTO_INCREMENT=1");
		
		//test implementation of the print XML file
		//TODO insert query for the client data
		
	// 1. create order
			PurchaseOrderHeader po = new PurchaseOrderHeader("1");
			po.setBuyer(new BuyerInformation("Cliente 1","avenida 123","Barquisimeto","3001","VE")); //name,direction,city,zip code, country
			PurchaseOrderItem[] items={ new PurchaseOrderItem("X1","tablero",1),  //article,description,quantity
					 new PurchaseOrderItem("R4","Interruptor2",1),
					 new PurchaseOrderItem("M3","Caja123",1),
					 new PurchaseOrderItem("X4","Tablero2",2)};
			po.setItems(items);
			
			try {

				// 2. create now XML representation of the order			
				JAXBContext jc = JAXBContext.newInstance(PurchaseOrderHeader.class);
				Marshaller marshaller=jc.createMarshaller();
				ByteArrayOutputStream ba=new ByteArrayOutputStream();
				marshaller.marshal(po,ba);
				
				System.out.println("Created XML= "+new String(ba.toByteArray()));
				
				// 3. create now the PDF output for the XML data
				FOProcessor processor=new FOProcessor();
				processor.process(new ByteArrayInputStream(ba.toByteArray()), new FileInputStream("src/ve/com/mastercircuito/print/JavaPOExample.fo") , new FileOutputStream("BudgetExample.pdf"));
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	
	}
}
