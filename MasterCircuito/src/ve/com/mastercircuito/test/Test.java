package ve.com.mastercircuito.test;

import java.util.Date;

import org.joda.time.DateTime;

import ve.com.mastercircuito.db.Db;

public class Test {

	public static void main(String[] args) {
		DateTime dt = new DateTime(new Date());
		int year = dt.getYear();
		Db db = new Db();
		String queryInsert = "INSERT INTO budget_code_ids (budget_code_id, year) VALUES(2, " + year + "),(3, " + year + ")";
		db.insert(queryInsert);
		db.delete("DELETE FROM budget_code_ids WHERE year < 2017");
		db.query("ALTER TABLE budget_code_ids AUTO_INCREMENT=1");
	}

}
