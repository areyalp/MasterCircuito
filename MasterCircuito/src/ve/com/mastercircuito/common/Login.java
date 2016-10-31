package ve.com.mastercircuito.common;

import java.sql.ResultSet;

import ve.com.mastercircuito.db.MysqlDriver;
import ve.com.mastercircuito.utils.PasswordEncryptor;

public class Login {

	protected static Boolean authenticate(String username, String plainPassword){
		Boolean isPasswordOk = false;
		Boolean authenticated = false;
		ResultSet rows;
		try{
			MysqlDriver db = new MysqlDriver();
			rows = db.select("SELECT id, password FROM Users WHERE username = '"+ username +"';");
			if(rows.next()){
				String encryptedPassword = rows.getString("Password");
				isPasswordOk = PasswordEncryptor.checkPassword(plainPassword, encryptedPassword);
				if(isPasswordOk){
					authenticated = true;
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return authenticated;
	}
	
}
