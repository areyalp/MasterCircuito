package ve.com.mastercircuito.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import ve.com.mastercircuito.objects.BarCapacity;
import ve.com.mastercircuito.objects.BarType;
import ve.com.mastercircuito.objects.Board;
import ve.com.mastercircuito.objects.BoardType;
import ve.com.mastercircuito.objects.BoardVoltage;
import ve.com.mastercircuito.objects.Box;
import ve.com.mastercircuito.objects.BoxType;
import ve.com.mastercircuito.objects.Budget;
import ve.com.mastercircuito.objects.BudgetStage;
import ve.com.mastercircuito.objects.Caliber;
import ve.com.mastercircuito.objects.Circuits;
import ve.com.mastercircuito.objects.Client;
import ve.com.mastercircuito.objects.Color;
import ve.com.mastercircuito.objects.DeliveryPeriod;
import ve.com.mastercircuito.objects.DispatchPlace;
import ve.com.mastercircuito.objects.Finish;
import ve.com.mastercircuito.objects.Installation;
import ve.com.mastercircuito.objects.Interruption;
import ve.com.mastercircuito.objects.LockType;
import ve.com.mastercircuito.objects.Material;
import ve.com.mastercircuito.objects.MeasureUnits;
import ve.com.mastercircuito.objects.Nema;
import ve.com.mastercircuito.objects.PaymentMethod;
import ve.com.mastercircuito.objects.Product;
import ve.com.mastercircuito.objects.ProductionOrder;
import ve.com.mastercircuito.objects.Sheet;
import ve.com.mastercircuito.objects.Switch;
import ve.com.mastercircuito.objects.User;
import ve.com.mastercircuito.objects.UserType;
import ve.com.mastercircuito.objects.WorkOrder;
import ve.com.mastercircuito.utils.PasswordEncryptor;
import ve.com.mastercircuito.utils.StringTools;

public class Db extends MysqlDriver {
	
	public Db() {
		super();
	}
	
	public Db(String host, String dbuser, String dbpassword, String database) {
		super(host,dbuser,dbpassword,database);
	}
	
	public Connection getConnection(){
		return this.conn;
		
	}
	
	public boolean switchExists(String phases, String current, String brand, String model, String interruption, String reference) {
		String queryString;
		
		queryString = "SELECT * FROM switches, switch_brands, switch_models, currents, interruptions "
					+ "WHERE switches.brand_id = switch_brands.id "
					+ "AND switches.model_id = switch_models.id "
					+ "AND switches.interruption_id = interruptions.id "
					+ "AND switches.current_id = currents.id "
					+ "AND switches.phases = '" + phases + "' "
					+ "AND switch_models.model = '" + model + "' "
					+ "AND currents.current = '" + current + "' "
					+ "AND switch_brands.brand = '" + brand + "' "
					+ "AND interruptions.interruption = '" + interruption + "' "
					+ "AND switches.reference = '" + reference + "'";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean editSwitch(int switchId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
		if (listFields.size() > 0) {
			String queryFields = "";
			Iterator<Object> it1 = listFields.iterator();
			Iterator<Object> it2 = listValues.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				queryFields += it1.next().toString() + " = " + it2.next().toString() + ",";
			}
			queryFields = StringTools.removeLastChar(queryFields);
			String queryString = "UPDATE switches SET " + queryFields + " WHERE id = " + switchId;
			if (Db.update(queryString)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean editBox(int boxId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
		if (listFields.size() > 0) {
			String queryFields = "";
			Iterator<Object> it1 = listFields.iterator();
			Iterator<Object> it2 = listValues.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				queryFields += it1.next().toString() + " = " + it2.next().toString() + ",";
			}
			queryFields = StringTools.removeLastChar(queryFields);
			String queryString = "UPDATE boxes SET " + queryFields + " WHERE id = " + boxId;
			if (Db.update(queryString)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean editBoard(int boardId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
		if (listFields.size() > 0) {
			String queryFields = "";
			Iterator<Object> it1 = listFields.iterator();
			Iterator<Object> it2 = listValues.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				queryFields += it1.next().toString() + " = " + it2.next().toString() + ",";
			}
			queryFields = StringTools.removeLastChar(queryFields);
			String queryString = "UPDATE boards SET " + queryFields + " WHERE id = " + boardId;
			if (Db.update(queryString)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean switchBrandExists(String brand) {
		String queryString;
		
		queryString = "SELECT id FROM switch_brands WHERE brand = '" + brand + "'";
		this.select(queryString);

		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addSwitchBrand(String brand) {
		String queryInsert;
		
		queryInsert = "INSERT INTO switch_brands (brand) VALUES ('" + brand + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeSwitchBrand(String brand) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_brands WHERE brand = '" + brand + "'";
		return this.delete(queryDelete);
	}
	
	public boolean switchTypeExists(String model) {
		String queryString;
		
		queryString = "SELECT id FROM switch_models WHERE model = '" + model + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addSwitchType(String model, String brand) {
		String queryInsert;
		int brandId = this.getSwitchBrandId(brand);
		
		if(brandId < 1) {
			return false;
		}
		
		queryInsert = "INSERT INTO switch_models (model, brand_id) VALUES ('" + model + "', " + brandId + ")";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeSwitchModel(String model) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_models WHERE model = '" + model + "'";
		return this.delete(queryDelete);
	}
	
	public boolean currentExists(String current) {
		String queryString;
		
		queryString = "SELECT id FROM currents WHERE current = '" + current + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addCurrent(String current) {
		String queryInsert;
		
		queryInsert = "INSERT INTO currents (current) VALUES ('" + current + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeCurrent(String current) {
		String queryDelete;
		
		queryDelete = "DELETE FROM currents WHERE current = '" + current + "'";
		return this.delete(queryDelete);
	}
	
	public boolean voltageExists(String voltage) {
		String queryString;
		
		queryString = "SELECT id FROM switch_voltages WHERE voltage = '" + voltage + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addVoltage(String voltage, String type) {
		String queryInsert;
		
		queryInsert = "INSERT INTO switch_voltages (voltage) VALUES ('" + voltage + "V" + type + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeVoltage(String voltage) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_voltages WHERE voltage = '" + voltage + "'";
		return this.delete(queryDelete);
	}
	
	public boolean interruptionExists(String interruption) {
		String queryString;
		
		queryString = "SELECT id FROM interruptions WHERE interruption = '" + interruption + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addInterruption(String interruption) {
		String queryInsert;
		
		queryInsert = "INSERT INTO interruptions (interruption) VALUES ('" + interruption + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeInterruption(String interruption) {
		String queryDelete;
		
		queryDelete = "DELETE FROM interruptions WHERE interruption = '" + interruption + "'";
		return this.delete(queryDelete);
	}
	
	public int getSwitchBrandId(String brand) {
		ResultSet setBrand;
		int brandId = 0;
		setBrand = this.select("SELECT id FROM switch_brands WHERE brand = '" + brand + "'");
		
		try {
			setBrand.first();
			brandId = setBrand.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la marca");
		}
		return brandId;
	}
	
	public int getSwitchModelId(String model) {
		ResultSet setModel;
		int modelId = 0;
		setModel = this.select("SELECT id FROM switch_models WHERE model = '" + model + "'");
		
		try {
			setModel.first();
			modelId = setModel.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del modelo");
		}
		return modelId;
	}
	
	public int getCurrentId(int current) {
		ResultSet setCurrent;
		int currentId = 0;
		setCurrent = this.select("SELECT id FROM currents WHERE current = '" + current + "'");
		
		try {
			if(setCurrent.next()) {
				currentId = setCurrent.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la corriente");
		}
		return currentId;
	}
	
	public int getVoltageId(String voltage) {
		ResultSet setVoltage;
		int voltageId = 0;
		setVoltage = this.select("SELECT id FROM switch_voltages WHERE voltage = '" + voltage + "'");
		
		try {
			setVoltage.first();
			voltageId = setVoltage.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del voltaje");
		}
		return voltageId;
	}
	
	public int getInterruptionId(int interruption) {
		ResultSet setInterruption;
		int interruptionId = 0;
		setInterruption = this.select("SELECT id FROM interruptions WHERE interruption = " + interruption);
		
		try {
			if(setInterruption.next()) {
				interruptionId = setInterruption.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la interrupcion");
		}
		return interruptionId;
	}
	
	public int getBoxTypeId(String type) {
		ResultSet setType;
		int typeId = 0;
		setType = this.select("SELECT id FROM box_types WHERE type = '" + type + "'");
		
		try {
			setType.first();
			typeId = setType.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo");
		}
		return typeId;
	}
	
	public int getInstallationId(String installation) {
		ResultSet setInstallation;
		int installationId = 0;
		String query = "SELECT id FROM installations WHERE installation = '" + installation + "'";
		setInstallation = this.select(query);
		
		try {
			if(setInstallation.next()) {
				installationId = setInstallation.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la instalacion");
		}
		return installationId;
	}
	
	public int getNemaId(String nema) {
		ResultSet setNema;
		int nemaId = 0;
		setNema = this.select("SELECT id FROM nemas WHERE nema = '" + nema + "'");
		
		try {
			setNema.first();
			nemaId = setNema.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la nema");
		}
		return nemaId;
	}
	
	public int getBoxSheetId(String sheet) {
		ResultSet setSheet;
		int sheetId = 0;
		setSheet = this.select("SELECT id FROM box_sheets WHERE sheet = '" + sheet + "'");
		
		try {
			setSheet.first();
			sheetId = setSheet.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la lamina");
		}
		return sheetId;
	}
	
	public int getBoxFinishId(String finish) {
		ResultSet setFinish;
		int finishId = 0;
		setFinish = this.select("SELECT id FROM box_finishes WHERE finish = '" + finish + "'");
		
		try {
			setFinish.first();
			finishId = setFinish.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del acabado");
		}
		return finishId;
	}
	
	public int getBoxColorId(String color) {
		ResultSet setColor;
		int colorId = 0;
		setColor = this.select("SELECT id FROM box_colors WHERE color = '" + color + "'");
		
		try {
			setColor.first();
			colorId = setColor.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del color");
		}
		return colorId;
	}
	
	public boolean colorExists(String color) {
		String queryString;
		
		queryString = "SELECT id FROM box_colors WHERE color = '" + color + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addColor(String color) {
		String queryInsert;
		
		queryInsert = "INSERT INTO box_colors (color) VALUES ('" + color + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeColor(String color) {
		String queryDelete;
		
		queryDelete = "DELETE FROM box_colors WHERE color = '" + color + "'";
		return this.delete(queryDelete);
	}
	
	public int getBoxUnitsId(String units) {
		ResultSet setUnits;
		int unitsId = 0;
		setUnits = this.select("SELECT id FROM box_measure_units WHERE units = '" + units + "'");
		
		try {
			setUnits.first();
			unitsId = setUnits.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de las unidades");
		}
		return unitsId;
	}
	
	public int getBoxCaliberId(String caliber) {
		ResultSet setCaliber;
		int caliberId = 0;
		setCaliber = this.select("SELECT id FROM box_calibers WHERE caliber = '" + caliber + "'");
		
		try {
			setCaliber.first();
			caliberId = setCaliber.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del calibre");
		}
		return caliberId;
	}
	
	public boolean caliberExists(String caliber) {
		String queryString;
		
		queryString = "SELECT id FROM box_calibers WHERE caliber = '" + caliber + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addCaliber(String caliber) {
		String queryInsert;
		
		queryInsert = "INSERT INTO box_calibers (caliber) VALUES ('" + caliber + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeCaliber(String caliber) {
		String queryDelete;
		
		queryDelete = "DELETE FROM box_calibers WHERE caliber = '" + caliber + "'";
		return this.delete(queryDelete);
	}
	
	public int getLockTypeId(String lockType) {
		ResultSet setLockType;
		int lockTypeId = 0;
		setLockType = this.select("SELECT id FROM lock_types WHERE lock_type = '" + lockType + "'");
		
		try {
			if(setLockType.first()) {
				lockTypeId = setLockType.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la cerradura");
		}
		return lockTypeId;
	}
	
	public String getBoxCaliberComments(int id) {
		ResultSet setCaliber;
		String caliberComments = "";
		setCaliber = this.select("SELECT caliber_comments FROM boxes WHERE id = " + id);
		
		try {
			setCaliber.first();
			caliberComments = setCaliber.getString("caliber_comments");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el comentario del calibre");
		}
		return caliberComments;
	}
	
	public int getBoardTypeId(String type) {
		ResultSet setType;
		int typeId = 0;
		setType = this.select("SELECT id FROM board_types WHERE type = '" + type + "'");
		
		try {
			setType.first();
			typeId = setType.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo");
		}
		return typeId;
	}
	
	public int getBoardBarCapacityId(int barCapacity) {
		ResultSet setBarCapacity;
		int barCapacityId = 0;
		setBarCapacity = this.select("SELECT id FROM board_bar_capacities WHERE bar_capacity = '" + barCapacity + "'");
		
		try {
			setBarCapacity.first();
			barCapacityId = setBarCapacity.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo");
		}
		return barCapacityId;
	}
	
	public int getBoardBarTypeId(String barType) {
		ResultSet setBarType;
		int barTypeId = 0;
		setBarType = this.select("SELECT id FROM board_bar_types WHERE bar_type = '" + barType + "'");
		
		try {
			setBarType.first();
			barTypeId = setBarType.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo de barra");
		}
		return barTypeId;
	}
	
	public int getBoardCircuitsId(int circuits) {
		ResultSet setCircuits;
		int circuitsId = 0;
		setCircuits = this.select("SELECT id FROM board_circuits WHERE circuits = '" + circuits + "'");
		
		try {
			setCircuits.first();
			circuitsId = setCircuits.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de los circuitos");
		}
		return circuitsId;
	}
	
	public int getBoardVoltageId(String voltage) {
		ResultSet setVoltage;
		int voltageId = 0;
		setVoltage = this.select("SELECT id FROM board_voltages WHERE voltage = '" + voltage + "'");
		
		try {
			setVoltage.first();
			voltageId = setVoltage.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del voltaje");
		}
		return voltageId;
	}
	
	public int getBoardContainerId(int switchId) {
		ResultSet setSwitchBoard;
		int switchBoardId = 0;
		setSwitchBoard = this.select("SELECT board_container_id FROM board_switches WHERE id = '" + switchId + "'");
		
		try {
			setSwitchBoard.first();
			switchBoardId = setSwitchBoard.getInt("board_container_id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tablero para este interruptor");
		}
		return switchBoardId;
	}
	
	public int getSwitchBoardId(int boardSwitchId) {
		ResultSet setBoardSwitch;
		int boardId = 0;
		setBoardSwitch = this.select("SELECT board_container_id FROM board_switches WHERE id = '" + boardSwitchId + "'");
		
		try {
			setBoardSwitch.first();
			boardId = setBoardSwitch.getInt("board_container_id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tablero");
		}
		return boardId;
	}
	
	public int getBoardSwitchId(int boardSwitchId) {
		ResultSet setBoardSwitch;
		int switchId = 0;
		setBoardSwitch = this.select("SELECT switch_id FROM board_switches WHERE id = '" + boardSwitchId + "'");
		
		try {
			setBoardSwitch.first();
			switchId = setBoardSwitch.getInt("switch_id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del interruptor");
		}
		return switchId;
	}
	
	public int getDispachPlaceId (String place){
		ResultSet setDispatchPlace;
		int dispatchPlaceId = 0;
		setDispatchPlace = this.select("SELECT id FROM budget_dispatch_places WHERE place = '" + place + "'");
	
		try {
			setDispatchPlace.first();
			dispatchPlaceId = setDispatchPlace.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de el sitio de entrega");
		}
		return dispatchPlaceId;
	}
	
	public int getPaymentMethodId (String method){
		ResultSet setPaymentMethod;
		int paymentMethodId = 0;
		setPaymentMethod = this.select("SELECT id FROM budget_payment_methods WHERE method = '" + method + "'");
	
		try {
			setPaymentMethod.first();
			paymentMethodId = setPaymentMethod.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de el metodo de pago");
		}
		return paymentMethodId;
	}
	
	public int getSellerId (String seller){
		ResultSet setSeller;
		int sellerId = 0;
		setSeller = this.select("SELECT id FROM budget_sellers WHERE seller = '" + seller + "'");
	
		try {
			setSeller.first();
			sellerId = setSeller.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de el vendedor");
		}
		return sellerId;
	}
	
	public int getDeliveryPeriodId(String delivery_period) {
		ResultSet setDeliveryPeriod;
		int deliveryPeriodId = 0;
		setDeliveryPeriod = this.select("SELECT id FROM budget_delivery_periods WHERE delivery_period = '" + delivery_period + "'");
		
		try {
			setDeliveryPeriod.first();
			deliveryPeriodId = setDeliveryPeriod.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de el tiempo de entrega");
		}
		return deliveryPeriodId;
	}
	
	public String getBudgetCode(int budgetId) {
		ResultSet setBudgetCode;
		String code = "";
		setBudgetCode = this.select("SELECT code FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetCode.first();
			code = setBudgetCode.getString("code");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el codigo del presupuesto");
		}
		return code;
	}
	
	public int getBudgetExpiryDays(int budgetId) {
		ResultSet setBudgetExpiryDays;
		int expiryDays = 0;
		setBudgetExpiryDays = this.select("SELECT expiry_days FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetExpiryDays.first();
			expiryDays = setBudgetExpiryDays.getInt("expiry_days");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los días de vencimiento");
		}
		return expiryDays;
	}	
	
	public String getBudgetWorkName(int budgetId) {
		ResultSet setBudgetWorkName;
		String workName = "";
		setBudgetWorkName = this.select("SELECT work_name FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetWorkName.first();
			workName = setBudgetWorkName.getString("work_name");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el nombre de la obra");
		}
		return workName;
	}
	
	public String getBudgetDate(int budgetId) {
		ResultSet setBudgetDate;
		String date = "";
		setBudgetDate = this.select("SELECT date FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetDate.first();
			date = setBudgetDate.getString("date");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la fecha");
		}
		return date;
	}
	
	public boolean addSwitch(String reference, String brand, String model, String phases, int current, String voltage, int interruption, String price) {
		int brandId = this.getSwitchBrandId(brand);
		int modelId = this.getSwitchModelId(model);
		int currentId = this.getCurrentId(current);
		int voltageId = this.getVoltageId(voltage);
		int interruptionId = this.getInterruptionId(interruption);
		
		String queryInsert = "INSERT INTO switches (reference, brand_id, model_id, phases, current_id, voltage_id, interruption_id, price) "
				+ "VALUES('" + reference + "', " + brandId + ", " + modelId + ", '" + phases + "', " + currentId + ", " + voltageId + ", " + interruptionId + ", '" + price + "')";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean addBox(String type, String installation, String nema, int pairs, String sheet, String finish, String color, double height, double width, double depth, String units, String caliber, String caliberComments, String lockType, double price, String boxComments) {
		int typeId = this.getBoxTypeId(type);
		int installationId = this.getInstallationId(installation);
		int nemaId = this.getNemaId(nema);
		int sheetId = this.getBoxSheetId(sheet);
		int finishId = (finish.isEmpty())?0:this.getBoxFinishId(finish);
		int colorId = (color.isEmpty())?0:this.getBoxColorId(color);
		int unitsId = this.getBoxUnitsId(units);
		int caliberId = this.getBoxCaliberId(caliber);
		int lockTypeId = this.getLockTypeId(lockType);

		String queryInsert = "INSERT INTO boxes (type_id, installation_id, nema_id, pairs, sheet_id, finish_id, color_id, height, width, depth, units_id, caliber_id, caliber_comments, lock_type_id, price, comments) "
				+ "VALUES(" + typeId + ", " + installationId + ", " + nemaId + ", " + pairs + ", " + sheetId + ", " + finishId + ", " + colorId + ", " + height + ", " + width + ", " + depth + ", " + unitsId + ", " + caliberId + ", '" + caliberComments + "', " + lockTypeId + ", " + price + ", '" + boxComments + "')";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean addBoard(String name, String type, String installation, String nema, int barCapacity, String barType, int circuits, String voltage, int phases, String ground, int interruption, String lockType, double price) {
		int typeId = this.getBoardTypeId(type);
		int installationId = this.getInstallationId(installation);
		int nemaId = this.getNemaId(nema);
		int barCapacityId = this.getBoardBarCapacityId(barCapacity);
		int barTypeId = this.getBoardBarTypeId(barType);
		int circuitsId = this.getBoardCircuitsId(circuits);
		int voltageId = this.getBoardVoltageId(voltage);
		int interruptionId = this.getInterruptionId(interruption);
		int lockTypeId = this.getLockTypeId(lockType);

		String queryInsert = "INSERT INTO boards (name, type_id, installation_id, nema_id, bar_capacity_id, bar_type_id, circuits_id, voltage_id, phases, ground, interruption_id, lock_type_id, price) "
				+ "VALUES('" + name + "', " + typeId + ", " + installationId + ", " + nemaId + ", " + barCapacityId + ", " + barTypeId + ", " + circuitsId + ", " + voltageId + ", '" + phases + "', '" + ground + "', " + interruptionId + ", " + lockTypeId + ", " + price + ")";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean addBudget(String date, int expiryDays, String clientId, 
			String workName, String method, int sellerId,
			String place, int deliveryTime, String deliveryPeriod, Integer creatorId) {
		
		int paymentMethodId = this.getPaymentMethodId(method);
		int dispatchPlaceId = this.getDispachPlaceId(place);
		int deliveryPeriodId= this.getDeliveryPeriodId(deliveryPeriod);
		int budgetCodeId = 0;
		int budgetId = 0;
		
		try {
			DateTime serverDateTime = new DateTime(new java.util.Date());
			int serverYear = serverDateTime.getYear();
			
			boolean deleteResult = this.delete("DELETE FROM budget_code_ids WHERE year < " + serverYear);
			this.select("SELECT * FROM budget_code_ids");
			int totalRows = this.getNumRows();
			if (deleteResult && totalRows == 0) {
				this.query("ALTER TABLE budget_code_ids AUTO_INCREMENT=1");
			}
			if((deleteResult && totalRows == 0) || (!deleteResult && totalRows == 0) 
					|| (!deleteResult && totalRows > 0)){
				String queryInsertBudgetCode = "INSERT INTO budget_code_ids (year) VALUES(" + serverYear + ")";
				this.insert(queryInsertBudgetCode);
				budgetCodeId = this.getInsertId();
				String stringBudgetCodeId = this.prepareBudgetCode(serverDateTime, budgetCodeId);
				
				String queryInsertBudget = "INSERT INTO budgets (code, `date`, expiry_days, client_id, work_name, payment_method_id, seller_id, dispatch_place_id, delivery_time, delivery_period_id, creator_id) "
						+ "VALUES('" + stringBudgetCodeId + "', '" + date + "', " + expiryDays + ", " + clientId + ", '" + workName + "', " + paymentMethodId + ", " + sellerId + ", '" + dispatchPlaceId + "', '" + deliveryTime + "', " + deliveryPeriodId + ", " + creatorId + ")";
				this.insert(queryInsertBudget);
				budgetId = this.getInsertId();
				
				String queryUpdateBudgetCodeId = "UPDATE budget_code_ids SET budget_code_id = " + budgetId + " WHERE id = " + budgetCodeId;
				Db.update(queryUpdateBudgetCodeId);
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el presupuesto, por favor contacte al programador");
		}
		
		return (budgetId > 0)? true:false;		
		
	}
	
	private String prepareBudgetCode(DateTime serverDateTime, int budgetCodeId) {
		DecimalFormat df = new DecimalFormat("00");
		String serverDay = df.format(serverDateTime.getDayOfMonth());
		String serverMonth = df.format(serverDateTime.getMonthOfYear());
		String serverYear = df.format(serverDateTime.getYearOfCentury());
		DecimalFormat df2 = new DecimalFormat("000000");
		String stringBudgetCodeId = df2.format(budgetCodeId);
		
		String budgetCode = "p-" + serverDay + serverMonth + serverYear + stringBudgetCodeId;
		return budgetCode;
	}
	
	public boolean editBudget(int budgetId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
		if (listFields.size() > 0) {
			String queryFields = "";
			Iterator<Object> it1 = listFields.iterator();
			Iterator<Object> it2 = listValues.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				queryFields += it1.next().toString() + " = " + it2.next().toString() + ",";
			}
			queryFields = StringTools.removeLastChar(queryFields);
			String queryString = "UPDATE budgets SET " + queryFields + " WHERE id = " + budgetId;
			if (Db.update(queryString)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeBoardSwitch(int switchId) {
		String queryDelete;
		queryDelete = "DELETE FROM board_switches WHERE id = '" + switchId + "'";
		return this.delete(queryDelete);
	}
	
	public ArrayList<Integer> getBoardSwitchMainIds(int boardId) {
		ArrayList<Integer> boardSwitchMainIds = new ArrayList<Integer>();
		ResultSet setBoardSwitchMainIds;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BOARD_MAIN_SWITCHES(?)}");
			statement.setInt(1, boardId);
			setBoardSwitchMainIds = statement.executeQuery();
			
			while(setBoardSwitchMainIds.next()) {
				boardSwitchMainIds.add(setBoardSwitchMainIds.getInt(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return boardSwitchMainIds;
	}
	
	public int getBoardSwitchesQuantity(int boardId) {
		ResultSet setBoardSwitchesQuantity;
		int boardSwitchesQuantity = 0;
		setBoardSwitchesQuantity = this.select("SELECT IFNULL(SUM(board_switches.quantity * switches.phases),0) as cnt " +
													"FROM board_switches, switches " +
													"WHERE board_switches.board_container_id = '" + boardId + "' " +
													"AND board_switches.switch_id = switches.id");
		
		try {
			setBoardSwitchesQuantity.first();
			boardSwitchesQuantity = setBoardSwitchesQuantity.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la cantidad de interruptores en este tablero");
		}
		return boardSwitchesQuantity;
	}
	
	public int getSwitchPhases(int switchId) {
		ResultSet setSwitchPhases;
		int switchPhases = 0;
		setSwitchPhases = this.select("SELECT phases FROM switches WHERE switches.id = '" + switchId + "'");
		
		try {
			setSwitchPhases.first();
			switchPhases = setSwitchPhases.getInt("phases");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la cantidad de fases del interruptor");
		}
		return switchPhases;
	}
	
	public boolean boardSwitchExists(int containerId, int switchId) {
		String queryString;
		
		queryString = "SELECT * FROM board_switches "
					+ "WHERE board_switches.board_container_id = '" + containerId + "' "
					+ "AND board_switches.switch_id = '" + switchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public int getBoardSwitchId(int containerId, int switchId) {
		String queryString;
		ResultSet setBoardSwitchId;
		
		queryString = "SELECT board_switches.id FROM board_switches "
					+ "WHERE board_switches.board_container_id = '" + containerId + "' "
					+ "AND board_switches.switch_id = '" + switchId + "' "
					+ "LIMIT 1";
		
		setBoardSwitchId = this.select(queryString);
		
		try {
			if (setBoardSwitchId.next()) {
				return setBoardSwitchId.getInt("board_switches.id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean addBoardSwitch(int containerId, int switchId, int quantity, double switchPrice) {
		if(this.boardSwitchExists(containerId, switchId)) {
			JOptionPane.showMessageDialog(null, "Este Interruptor ya existe dentro del tablero");
			return false;
		} else {
			String queryString = "INSERT INTO board_switches (board_container_id, switch_id, quantity, price) "
								+ "VALUES (" + containerId + ", " + switchId + ", " + quantity + ", " + switchPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private boolean increaseBoardSwitch(int boardSwitchId, int quantity) {
		String queryString = "UPDATE board_switches SET quantity = quantity + " + quantity 
							+ " WHERE id = " + boardSwitchId;
		
		return Db.update(queryString);
	}
	
	public ArrayList<Material> getBoardMaterials(int boardId) {
		ResultSet setBoardMaterials;
		ArrayList<Material> materials = new ArrayList<Material>();
		setBoardMaterials = this.select("SELECT * FROM materials WHERE id = '" + boardId + "'");
		
		try {
			while(setBoardMaterials.next()) {
				int id = setBoardMaterials.getInt("id");
				String material = setBoardMaterials.getString("material");
				double price = setBoardMaterials.getDouble("price");
				materials.add(new Material(id, material, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los materiales del tablero");
		}
		return materials;
	}
	
	public double getBoardMaterialsPrice(int boardId) {
		ResultSet setBoardMaterialsPrice;
		double materialsPrice = 0.00;
		setBoardMaterialsPrice = this.select("SELECT materials_price FROM boards WHERE id = '" + boardId + "'");
		
		try {
			setBoardMaterialsPrice.first();
			materialsPrice = setBoardMaterialsPrice.getDouble("materials_price");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el precio de los materiales del tablero");
		}
		return materialsPrice;
	}
	
	public String getBoardComments(int boardId) {
		ResultSet setBoardCommentsPrice;
		String comments = "";
		setBoardCommentsPrice = this.select("SELECT comments FROM boards WHERE id = '" + boardId + "'");
		
		try {
			setBoardCommentsPrice.first();
			comments = setBoardCommentsPrice.getString("comments");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener las observaciones del tablero");
		}
		return comments;
	}
	
	public int getBudgetClientId(int budgetId) {
		String queryString;
		ResultSet setBudgetClientId;
		
		queryString = "SELECT budgets.client_id FROM budgets "
					+ "WHERE budgets.id = " + budgetId;
		
		setBudgetClientId = this.select(queryString);
		
		try {
			if (setBudgetClientId.next()) {
				return setBudgetClientId.getInt("budgets.client_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getBudgetClientName(int budgetId) {
		String queryString;
		ResultSet setBudgetClientName;
		
		queryString = "SELECT clients.client FROM budgets, clients "
					+ "WHERE budgets.id = '" + budgetId + "' "
					+ "AND budgets.client_id = clients.id";
		
		setBudgetClientName = this.select(queryString);
		
		try {
			if (setBudgetClientName.next()) {
				return setBudgetClientName.getString("clients.client");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getBudgetClientRepresentative(int budgetId) {
		String queryString;
		ResultSet setBudgetClientRepresentative;
		
		queryString = "SELECT clients.representative FROM budgets, clients "
					+ "WHERE budgets.id = '" + budgetId + "' "
					+ "AND budgets.client_id = clients.id";
		
		setBudgetClientRepresentative = this.select(queryString);
		
		try {
			if (setBudgetClientRepresentative.next()) {
				return setBudgetClientRepresentative.getString("clients.representative");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getBudgetPaymentMethod(int budgetId) {
		ResultSet setBudgetPaymentMethod;
		String method = "";
		setBudgetPaymentMethod = this.select("SELECT method FROM budgets, budget_payment_methods WHERE id = '" + budgetId + "' AND budgets.payment_method_id = budget_payment_methods.id");
		
		try {
			setBudgetPaymentMethod.first();
			method = setBudgetPaymentMethod.getString("method");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el metodo de pago");
		}
		return method;
	}
	
	public String getBudgetDispatchPlace(int budgetId) {
		ResultSet setBudgetDispatchPlace;
		String place = "";
		setBudgetDispatchPlace = this.select("SELECT place FROM budgets, budget_dispatch_places WHERE id = '" + budgetId + "' AND budgets.dispatch_place_id = budget_dispatch_places.id");
		
		try {
			setBudgetDispatchPlace.first();
			place = setBudgetDispatchPlace.getString("place");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el lugar de entrega");
		}
		return place;
	}
	
	public int getBudgetDeliveryTime(int budgetId) {
		ResultSet setBudgetDeliveryTime;
		int deliveryTime = 0;
		setBudgetDeliveryTime = this.select("SELECT delivery_time FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetDeliveryTime.first();
			deliveryTime = setBudgetDeliveryTime.getInt("delivery_time");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el tiempo de entrega");
		}
		return deliveryTime;
	}
	
	public String getBudgetDeliveryPeriod(int budgetId) {
		ResultSet setBudgetDeliveryPeriod;
		String deliveryPeriod = "";
		setBudgetDeliveryPeriod = this.select("SELECT delivery_period FROM budgets, budget_delivery_periods WHERE id = '" + budgetId + "' AND budgets.delivery_period_id = budget_delivery_periods.id");
		
		try {
			setBudgetDeliveryPeriod.first();
			deliveryPeriod = setBudgetDeliveryPeriod.getString("delivery_period");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el periodo de entrega");
		}
		return deliveryPeriod;
	}
	
	public int getBudgetCreatorId(int budgetId) {
		ResultSet setBudgetCreator;
		int creatorId = 0;
		setBudgetCreator = this.select("SELECT creator_id FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			if (setBudgetCreator.next()) {
				creatorId = setBudgetCreator.getInt("creator_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del creador del presupuesto");
		}
		return creatorId;
	}
	
	public boolean addBudgetBox(int selectedBudgetId, int boxSearchId, int boxQuantity, double boxPrice) {
//		int budgetBoxId = 0;
		if(this.budgetBoxExists(selectedBudgetId, boxSearchId)) {
			JOptionPane.showMessageDialog(null, "Esta caja ya existe en el presupuesto");
			return false;
//			budgetBoxId = this.getBudgetBoxId(selectedBudgetId, boxSearchId);
//			if(budgetBoxId > 0) {
//				return this.increaseBudgetBox(budgetBoxId, boxQuantity);
//			} else {
//				return false;
//			}
		} else {
			String queryString = "INSERT INTO budget_boxes (budget_container_id, box_id, quantity, price) "
								+ "VALUES (" + selectedBudgetId + ", " + boxSearchId + ", " + boxQuantity + ", " + boxPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private boolean increaseBudgetBox(int budgetBoxId, int boxQuantity) {
		String queryString = "UPDATE budget_boxes SET quantity = quantity + " + boxQuantity 
				+ " WHERE id = " + budgetBoxId;

		return Db.update(queryString);
	}
	
	private int getBudgetBoxId(int selectedBudgetId, int boxSearchId) {
		String queryString;
		ResultSet setBudgetBoxId;
		
		queryString = "SELECT budget_boxes.id FROM budget_boxes "
					+ "WHERE budget_boxes.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boxes.box_id = '" + boxSearchId + "' "
					+ "LIMIT 1";
		
		setBudgetBoxId = this.select(queryString);
		
		try {
			if (setBudgetBoxId.next()) {
				return setBudgetBoxId.getInt("budget_boxes.id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private boolean budgetBoxExists(int selectedBudgetId, int boxSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_boxes "
					+ "WHERE budget_boxes.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boxes.box_id = '" + boxSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addBudgetSwitch(int selectedBudgetId, int switchSearchId, int switchQuantity, double switchPrice) {
//		int budgetSwitchId = 0;
		if(this.budgetSwitchExists(selectedBudgetId, switchSearchId)) {
			JOptionPane.showMessageDialog(null, "El interruptor ya existe dentro del presupuesto");
			return false;
//			budgetSwitchId = this.getBudgetSwitchId(selectedBudgetId, switchSearchId);
//			if(budgetSwitchId > 0) {
//				return this.increaseBudgetSwitch(budgetSwitchId, switchQuantity);
//			} else {
				
//			}
		} else {
			String queryString = "INSERT INTO budget_switches (budget_container_id, switch_id, quantity, price) "
								+ "VALUES (" + selectedBudgetId + ", " + switchSearchId + ", " + switchQuantity + ", " + switchPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
//	private boolean increaseBudgetSwitch(int budgetSwitchId, int switchQuantity) {
//		String queryString = "UPDATE budget_switches SET quantity = quantity + " + switchQuantity 
//				+ " WHERE id = " + budgetSwitchId;
//
//		return Db.update(queryString);
//	}
//	
//	private int getBudgetSwitchId(int selectedBudgetId, int switchSearchId) {
//		String queryString;
//		ResultSet setBudgetSwitchId;
//		
//		queryString = "SELECT budget_switches.id FROM budget_boxes "
//					+ "WHERE budget_switches.budget_container_id = '" + selectedBudgetId + "' "
//					+ "AND budget_switches.switch_id = '" + switchSearchId + "' "
//					+ "LIMIT 1";
//		
//		setBudgetSwitchId = this.select(queryString);
//		
//		try {
//			if (setBudgetSwitchId.next()) {
//				return setBudgetSwitchId.getInt("budget_switches.id");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
	
	private boolean budgetSwitchExists(int selectedBudgetId, int switchSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_switches "
					+ "WHERE budget_switches.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_switches.switch_id = '" + switchSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean removeBudgetSwitch(int selectedBudgetSwitchId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_switches WHERE id = '" + selectedBudgetSwitchId + "'";
		return this.delete(queryDelete);
	}
	
	public boolean removeBudgetBox(int selectedBudgetBoxId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_boxes WHERE id = '" + selectedBudgetBoxId + "'";
		return this.delete(queryDelete);
	}
	
	public boolean removeBudgetBoard(int selectedBudgetBoardId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_boards WHERE id = '" + selectedBudgetBoardId + "'";
		return this.delete(queryDelete);
	}
	
	public boolean addBudgetBoard(int selectedBudgetId, int boardSearchId, int boardQuantity, double boardPrice) {
		if(this.budgetBoardExists(selectedBudgetId, boardSearchId)) {
			JOptionPane.showMessageDialog(null, "Este tablero ya existe dentro del presupuesto");
			return false;
		} else {
			String queryString = "INSERT INTO budget_boards (budget_container_id, board_id, price, quantity) "
								+ "VALUES (" + selectedBudgetId + ", " + boardSearchId + ", " + boardPrice + ", " + boardQuantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private boolean increaseBudgetBoard(int budgetBoardId, int boardQuantity) {
		String queryString = "UPDATE budget_boards SET quantity = quantity + " + boardQuantity 
				+ " WHERE id = " + budgetBoardId;

		return Db.update(queryString);
	}
	
	private int getBudgetBoardId(int selectedBudgetId, int boardSearchId) {
		String queryString;
		ResultSet setBudgetBoardId;
		
		queryString = "SELECT budget_boards.id FROM budget_boards "
					+ "WHERE budget_boards.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boards.board_id = '" + boardSearchId + "' "
					+ "LIMIT 1";
		
		setBudgetBoardId = this.select(queryString);
		
		try {
			if (setBudgetBoardId.next()) {
				return setBudgetBoardId.getInt("budget_boards.id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private boolean budgetBoardExists(int selectedBudgetId, int boardSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_boards "
					+ "WHERE budget_boards.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boards.board_id = '" + boardSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public String getBudgetClientCode(int budgetId) {
		String queryString;
		ResultSet setBudgetClientCode;
		
		queryString = "SELECT clients.client_code FROM budgets, clients "
					+ "WHERE budgets.id = '" + budgetId + "' "
					+ "AND budgets.client_id = clients.id";
		
		setBudgetClientCode = this.select(queryString);
		
		try {
			if (setBudgetClientCode.next()) {
				return setBudgetClientCode.getString("clients.client_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getBudgetSellerId(int budgetId) {
		String queryString;
		ResultSet setBudgetSellerId;
		
		queryString = "SELECT budgets.seller_id FROM budgets "
					+ "WHERE budgets.id = " + budgetId;
		
		setBudgetSellerId = this.select(queryString);
		
		try {
			if (setBudgetSellerId.next()) {
				return setBudgetSellerId.getInt("budgets.seller_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getBudgetNotes(int budgetId) {
		ResultSet setBudgetNotes;
		String notes = "";
		setBudgetNotes = this.select("SELECT notes FROM budgets WHERE id = '" + budgetId + "'");
		
		try {
			setBudgetNotes.first();
			notes = setBudgetNotes.getString("notes");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener las notas del presupuesto");
		}
		return notes;
	}
	
	public ArrayList<Switch> getBudgetSwitches(int budgetId) {
		ArrayList<Switch> switches = new ArrayList<Switch>();
		ResultSet setBudgetSwitches = null;
		CallableStatement statement = null;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_SWITCHES(?)}");
			statement.setInt(1, budgetId);
			setBudgetSwitches = statement.executeQuery();
			
			while(setBudgetSwitches.next()) {
				int id = setBudgetSwitches.getInt("id");
				int brandId = setBudgetSwitches.getInt("brand_id");
				String brand = setBudgetSwitches.getString("brand");
				int modelId = setBudgetSwitches.getInt("model_id");
				String model = setBudgetSwitches.getString("model");
				String reference = setBudgetSwitches.getString("reference");
				int phases = setBudgetSwitches.getInt("phases");
				int currentId = setBudgetSwitches.getInt("current_id");
				String current = setBudgetSwitches.getString("current");
				int voltageId = setBudgetSwitches.getInt("voltage_id");
				String voltage = setBudgetSwitches.getString("voltage");
				int interruptionId = setBudgetSwitches.getInt("interruption_id");
				String interruption = setBudgetSwitches.getString("interruption");
				double price = setBudgetSwitches.getDouble("price");
				boolean active = (setBudgetSwitches.getInt("active")==1?true:false);
				int containerId = setBudgetSwitches.getInt("container_id");
				int quantity = setBudgetSwitches.getInt("quantity");
				switches.add(new Switch(id, brandId, brand, modelId, model, reference, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los interruptores del presupuesto");
		}
		
		return switches;
	}
	
	public ArrayList<Switch> getBoardSwitches(int budgetId) {
		ArrayList<Switch> switches = new ArrayList<Switch>();
		ResultSet setBoardSwitches = null;
		CallableStatement statement = null;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BOARD_SWITCHES(?)}");
			statement.setInt(1, budgetId);
			setBoardSwitches = statement.executeQuery();
			
			while(setBoardSwitches.next()) {
				int id = setBoardSwitches.getInt("id");
				int brandId = setBoardSwitches.getInt("brand_id");
				String brand = setBoardSwitches.getString("brand");
				int modelId = setBoardSwitches.getInt("model_id");
				String model = setBoardSwitches.getString("model");
				String reference = setBoardSwitches.getString("reference");
				int phases = setBoardSwitches.getInt("phases");
				int currentId = setBoardSwitches.getInt("current_id");
				String current = setBoardSwitches.getString("current");
				int voltageId = setBoardSwitches.getInt("voltage_id");
				String voltage = setBoardSwitches.getString("voltage");
				int interruptionId = setBoardSwitches.getInt("interruption_id");
				String interruption = setBoardSwitches.getString("interruption");
				double price = setBoardSwitches.getDouble("price");
				boolean active = (setBoardSwitches.getInt("active")==1?true:false);
				int containerId = setBoardSwitches.getInt("container_id");
				int quantity = setBoardSwitches.getInt("quantity");
				switches.add(new Switch(id, brandId, brand, modelId, model, reference, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los interruptores del tablero");
		}
		
		return switches;
	}
	
	public ArrayList<Box> getBudgetBoxes(int budgetId) {
		ArrayList<Box> boxes = new ArrayList<Box>();
		ResultSet setBudgetBoxes;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_BOXES(?)}");
			statement.setInt(1, budgetId);
			setBudgetBoxes = statement.executeQuery();
			
			while(setBudgetBoxes.next()) {
				int id = setBudgetBoxes.getInt("id");
				int typeId = setBudgetBoxes.getInt("type_id");
				String type = setBudgetBoxes.getString("type");
				BoxType boxType = new BoxType(typeId, type);
				
				int installationId = setBudgetBoxes.getInt("installation_id");
				String strInstallation = setBudgetBoxes.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				int nemaId = setBudgetBoxes.getInt("nema_id");
				String strNema = setBudgetBoxes.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				String strPairs = setBudgetBoxes.getString("pairs");
				String strPairsNumeric = (strPairs.equals("N/A"))?"0":strPairs;
				int pairs = Integer.valueOf(strPairsNumeric);
				
				int sheetId = setBudgetBoxes.getInt("sheet_id");
				String strSheet = setBudgetBoxes.getString("sheet");
				Sheet sheet = new Sheet(sheetId, strSheet);
				
				int finishId = setBudgetBoxes.getInt("finish_id");
				String strFinish = setBudgetBoxes.getString("finish");
				Finish finish = new Finish(finishId, strFinish);
				
				int colorId = setBudgetBoxes.getInt("color_id");
				String strColor = setBudgetBoxes.getString("color");
				Color color = new Color(colorId, strColor);
				
				int height = setBudgetBoxes.getInt("height");
				int width = setBudgetBoxes.getInt("width");
				int depth = setBudgetBoxes.getInt("depth");
				int unitsId = setBudgetBoxes.getInt("units_id");
				String strUnits = setBudgetBoxes.getString("units");
				MeasureUnits units = new MeasureUnits(unitsId, strUnits);
				
				int caliberId = setBudgetBoxes.getInt("caliber_id");
				String strCaliber = setBudgetBoxes.getString("caliber");
				int intCaliber = Integer.valueOf(((strCaliber.equals("N/A"))?"0":strCaliber));
				Caliber caliber = new Caliber(caliberId, intCaliber);
				
				String caliberComments = setBudgetBoxes.getString("caliber_comments");
				int lockTypeId = setBudgetBoxes.getInt("lock_type_id");
				String strLockType = setBudgetBoxes.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				double price = setBudgetBoxes.getDouble("price");
				String comments = setBudgetBoxes.getString("comments");
				boolean active = (setBudgetBoxes.getInt("active")==1?true:false);
				boxes.add(new Box(id, boxType, installation, nema, pairs, sheet, finish, color, height, width, depth, units, caliber, caliberComments, lockType, price, comments, active));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener las cajas del presupuesto");
		}
		
		return boxes;
	}
	
	public ArrayList<Board> getBudgetBoards(int budgetId) {
		ArrayList<Board> boards = new ArrayList<Board>();
		ResultSet setBudgetBoards;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_BOARDS(?)}");
			statement.setInt(1, budgetId);
			setBudgetBoards = statement.executeQuery();
			
			while(setBudgetBoards.next()) {
				int id = setBudgetBoards.getInt("id");
				String name = setBudgetBoards.getString("name");
				int boardTypeId = setBudgetBoards.getInt("type_id");
				String strBoardtype = setBudgetBoards.getString("type");
				BoardType boardType = new BoardType(boardTypeId, strBoardtype);
				
				int installationId = setBudgetBoards.getInt("installation_id");
				String strInstallation = setBudgetBoards.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				int nemaId = setBudgetBoards.getInt("nema_id");
				String strNema = setBudgetBoards.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				int barCapacityId = setBudgetBoards.getInt("bar_capacity_id");
				int strBarCapacity = setBudgetBoards.getInt("bar_capacity");
				BarCapacity barCapacity = new BarCapacity(barCapacityId, strBarCapacity);
				
				int barTypeId = setBudgetBoards.getInt("bar_type_id");
				String strBarType = setBudgetBoards.getString("nema");
				BarType barType = new BarType(barTypeId, strBarType);
				
				int circuitsId = setBudgetBoards.getInt("circuits_id");
				int strCircuits = setBudgetBoards.getInt("circuits");
				Circuits circuits = new Circuits(circuitsId, strCircuits);
				
				int boardVoltageId = setBudgetBoards.getInt("voltage_id");
				String strBoardVoltage = setBudgetBoards.getString("voltage");
				BoardVoltage boardVoltage = new BoardVoltage(boardVoltageId, strBoardVoltage);
				
				int phases = setBudgetBoards.getInt("phases");
				boolean ground = setBudgetBoards.getBoolean("ground");
				
				int interruptionId = setBudgetBoards.getInt("interruption_id");
				int strInterruption = setBudgetBoards.getInt("interruption");
				Interruption interruption = new Interruption(interruptionId, strInterruption);
				
				int lockTypeId = setBudgetBoards.getInt("lock_type_id");
				String strLockType = setBudgetBoards.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				ArrayList<Integer> mainSwitchIds = this.getBoardSwitchMainIds(id);
				
				ArrayList<Material> materials = this.getBoardMaterials(id);
				
				double price = setBudgetBoards.getDouble("price");
				String comments = setBudgetBoards.getString("comments");
				boolean active = (setBudgetBoards.getInt("active")==1?true:false);
				
				ArrayList<Switch> switches = new ArrayList<Switch>();
				switches = this.getBoardSwitches(id);
				boards.add(new Board(id, name, boardType, installation, nema, barCapacity, barType, circuits, boardVoltage, phases, ground, interruption, lockType, mainSwitchIds, materials, price, comments, active, switches));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los tableros del presupuesto");
		}
		
		return boards;
	}
	
	public int cloneBudget(int selectedBudgetId) {
		int clonedBudgetId = 0;
		
		String date = this.getBudgetDate(selectedBudgetId);
		int expiryDays = this.getBudgetExpiryDays(selectedBudgetId);
		String clientId = String.valueOf(this.getBudgetClientId(selectedBudgetId));
		String workName = this.getBudgetWorkName(selectedBudgetId);
		String method = this.getBudgetPaymentMethod(selectedBudgetId);
		int sellerId = this.getBudgetSellerId(selectedBudgetId);
		String place = this.getBudgetDispatchPlace(selectedBudgetId);
		int deliveryTime = this.getBudgetDeliveryTime(selectedBudgetId);
		String deliveryPeriod = this.getBudgetDeliveryPeriod(selectedBudgetId);
		int creatorId = this.getBudgetCreatorId(selectedBudgetId);
		
		boolean added = this.addBudget(date, expiryDays, clientId, workName, method, sellerId, place, deliveryTime, deliveryPeriod, creatorId);
		
		if(added) {
			clonedBudgetId = this.getInsertId();
			
			this.cloneBudgetSwitches(selectedBudgetId, clonedBudgetId);
			this.cloneBudgetBoxes(selectedBudgetId, clonedBudgetId);
			this.cloneBudgetBoards(selectedBudgetId, clonedBudgetId);
			// TODO Clone budget materials and control boards here
//			this.cloneBudgetMaterials(selectedBudgetId, clonedBudgetId);
//			this.cloneBudgetSpecials(selectedBudgetId, clonedBudgetId);
		}
		
		return clonedBudgetId;
	}
	
	private void cloneBudgetSwitches(int selectedBudgetId, int clonedBudgetId) {
		ResultSet setBudgetSwitches;
		ArrayList<Switch> switches = this.getBudgetSwitches(selectedBudgetId);
		ArrayList<Switch> clonedSwitches = (ArrayList<Switch>) switches.subList(0, switches.size() - 1);
		Iterator<Switch> it = clonedSwitches.iterator();
		
		// TODO Iterate through all of them and change the budget container
		it.next();
	}

	private void cloneBudgetBoxes(int selectedBudgetId, int clonedBudgetId) {
		// TODO Clone Budget Boxes
		
	}
	
	private void cloneBudgetBoards(int selectedBudgetId, int clonedBudgetId) {
		// TODO Clone Budget Boards
		
	}

	public ArrayList<Budget> getBudgets(int stageId) {
		ArrayList<Budget> budgets = new ArrayList<Budget>();
		ResultSet setBudgets;
		
		setBudgets = this.select("SELECT * FROM budgets WHERE stage_id = " + stageId);
		
		try {
			while(setBudgets.next()) {
				int id = setBudgets.getInt("id");
				String code = setBudgets.getString("code");
				java.sql.Date date = setBudgets.getDate("date");
				int expiryDays = setBudgets.getInt("expiry_days");
				
				int clientId = setBudgets.getInt("client_id");
				Client client = new Client(this.getClientInfo(clientId));
				
				String workName = setBudgets.getString("work_name");
				
				int paymentMethodId = setBudgets.getInt("payment_method_id");
				PaymentMethod paymentMethod = new PaymentMethod(this.getPaymentMethodInfo(paymentMethodId));
				
				int sellerId = setBudgets.getInt("seller_id");
				User seller = new User(this.getSellerInfo(sellerId));
				
				int dispatchPlaceId = setBudgets.getInt("dispatch_place_id");
				DispatchPlace dispatchPlace = new DispatchPlace(this.getDispachPlaceInfo(dispatchPlaceId));
				
				int deliveryTime = setBudgets.getInt("delivery_time");
				
				int deliveryPeriodId = setBudgets.getInt("delivery_period_id");
				DeliveryPeriod deliveryPeriod = new DeliveryPeriod(this.getDeliveryPeriodInfo(deliveryPeriodId));
				
				boolean tracing = (setBudgets.getInt("tracing")==1)?true:false;
				
				BudgetStage stage = new BudgetStage(this.getBudgetStageInfo(stageId)); 
				
				String notes = setBudgets.getString("notes");
				ArrayList<Switch> switches = new ArrayList<Switch>();
				switches = this.getBudgetSwitches(id);
				ArrayList<Box> boxes = new ArrayList<Box>();
				boxes = this.getBudgetBoxes(id);
				ArrayList<Board> boards = new ArrayList<Board>();
				boards = this.getBudgetBoards(id);
				budgets.add(new Budget(id, code, date, expiryDays, client, workName, paymentMethod, seller, dispatchPlace, deliveryTime, deliveryPeriod, tracing, stage, notes, switches, boxes, boards));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los interruptores del presupuesto");
		}
		
		return budgets;
	}

	private BudgetStage getBudgetStageInfo(int stageId) {
		BudgetStage budgetStage = null;
		ResultSet setBudgetStage;
		
		setBudgetStage = this.select("SELECT * FROM budget_stages WHERE budget_stages.id = " + stageId);
		
		try {
			if (setBudgetStage.next()) {
				String stage = setBudgetStage.getString("stage");
				budgetStage = new BudgetStage(stageId, stage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return budgetStage;
	}

	private DeliveryPeriod getDeliveryPeriodInfo(int deliveryPeriodId) {
		DeliveryPeriod deliveryPeriod = null;
		ResultSet setDeliveryPeriod;
		
		setDeliveryPeriod = this.select("SELECT * FROM budget_delivery_periods WHERE budget_delivery_periods.id = " + deliveryPeriodId);
		
		try {
			if (setDeliveryPeriod.next()) {
				String period = setDeliveryPeriod.getString("delivery_period");
				deliveryPeriod = new DeliveryPeriod(deliveryPeriodId, period);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return deliveryPeriod;
	}

	private DispatchPlace getDispachPlaceInfo(int dispatchPlaceId) {
		DispatchPlace dispatchPlace = null;
		ResultSet setDispatchPlace;
		
		setDispatchPlace = this.select("SELECT * FROM budget_dispatch_places WHERE budget_dispatch_places.id = " + dispatchPlaceId);
		
		try {
			if (setDispatchPlace.next()) {
				String place = setDispatchPlace.getString("place");
				dispatchPlace = new DispatchPlace(dispatchPlaceId, place);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dispatchPlace;
	}

	private User getSellerInfo(int sellerId) {
		User seller = null;
		ResultSet setSeller;
		String query = "SELECT *, UNIX_TIMESTAMP(date_created) as date_timestamp FROM users WHERE users.id = " + sellerId;
		
		setSeller = this.select(query);
		
		try {
			if (setSeller.next()) {
				String username = setSeller.getString("username");
				String passport = setSeller.getString("passport");
				String password = setSeller.getString("password");
				int userTypeId = setSeller.getInt("user_type_id");
				UserType userType = new UserType(this.getUserTypeInfo(userTypeId));
				String firstName = setSeller.getString("first_name");
				String lastName = setSeller.getString("last_name");
				String email = setSeller.getString("email");
				String phone = setSeller.getString("phone");
				Timestamp dateCreated = setSeller.getTimestamp("date_timestamp");
				boolean status = (setSeller.getInt("status")==1)?true:false;
				seller = new User(sellerId, username, passport, password, userType, firstName, lastName, email, phone, dateCreated, status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seller;
	}

	private UserType getUserTypeInfo(int userTypeId) {
		UserType userType = null;
		ResultSet setUserType;
		
		setUserType = this.select("SELECT * FROM user_types WHERE user_types.id = " + userTypeId);
		
		try {
			if (setUserType.next()) {
				String type = setUserType.getString("type");
				userType = new UserType(userTypeId, type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userType;
	}

	private PaymentMethod getPaymentMethodInfo(int paymentMethodId) {
		PaymentMethod paymentMethod = null;
		ResultSet setPaymentMethod;
		
		setPaymentMethod = this.select("SELECT * FROM budget_payment_methods WHERE budget_payment_methods.id = " + paymentMethodId);
		
		try {
			if (setPaymentMethod.next()) {
				String method = setPaymentMethod.getString("method");
				paymentMethod = new PaymentMethod(paymentMethodId, method);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return paymentMethod;
	}

	private Client getClientInfo(int clientId) {
		Client client = null;
		ResultSet setClient;
		
		setClient = this.select("SELECT * FROM clients WHERE clients.id = " + clientId);
		
		try {
			if (setClient.next()) {
				String clientName = setClient.getString("client");
				String code = setClient.getString("client_code");
				String representative = setClient.getString("representative");
				String rif = setClient.getString("rif");
				String address = setClient.getString("address");
				String phone = setClient.getString("phone");
				String email = setClient.getString("email");
				String facebookProfile = setClient.getString("facebook_profile");
				String twitterUser = setClient.getString("twitter_user");
				String InstagramUser = setClient.getString("instagram_user");
				client = new Client(clientId, clientName, code, representative, rif, address, phone, email, facebookProfile, twitterUser, InstagramUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return client;
	}

	public User fetchUserInfo(String username) {
		User user = null;
		ResultSet setUserInfo;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_USER_INFO(?)}");
			statement.setString(1, username);
			setUserInfo = statement.executeQuery();
			
			if (setUserInfo.next()) {
				int id = setUserInfo.getInt("id");
				String password = setUserInfo.getString("password");
				String passport = setUserInfo.getString("passport");
				int userTypeId = setUserInfo.getInt("user_type_id");
				String strUserType = setUserInfo.getString("type");
				UserType userType = new UserType(userTypeId, strUserType);
				String firstName = setUserInfo.getString("first_name");
				String lastName = setUserInfo.getString("last_name");
				String email = setUserInfo.getString("email");
				String phone = setUserInfo.getString("phone");
				Timestamp dateCreated = setUserInfo.getTimestamp("date_timestamp");
				boolean status = (setUserInfo.getInt("status")==1?true:false);
				
				user = new User(id, username, password, passport, userType, firstName, lastName, email, phone, dateCreated, status);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public ProductionOrder pullProductionOrder(int orderId) {
		ProductionOrder productionOrder = new ProductionOrder();
		ResultSet setProductionOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_INFO(?)}");
			statement.setInt(1, orderId);
			setProductionOrder = statement.executeQuery();
			if (setProductionOrder.next()) {
				int id = setProductionOrder.getInt("id");
				int budgetId = setProductionOrder.getInt("budget_id");
				Timestamp timestampProcessed = setProductionOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setProductionOrder.getTimestamp("date_finished");
				int creatorId = setProductionOrder.getInt("creator_id");
				int authorizerId = setProductionOrder.getInt("authorizer_id");
				boolean processed = (setProductionOrder.getInt("processed")==1?true:false);
				productionOrder = new ProductionOrder(id, budgetId, timestampProcessed, timestampFinished, creatorId, authorizerId, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return productionOrder;
	}
	
	public boolean productionOrderExists(int budgetId) {
		String queryString;
		
		queryString = "SELECT id FROM production_orders WHERE budget_id = '" + budgetId + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public ProductionOrder pullProductionOrderByBudget(int budgetId) {
		ProductionOrder productionOrder = new ProductionOrder();
		ResultSet setProductionOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_INFO_BY_BUDGET(?)}");
			statement.setInt(1, budgetId);
			setProductionOrder = statement.executeQuery();
			if (setProductionOrder.next()) {
				int id = setProductionOrder.getInt("id");
				Timestamp timestampProcessed = setProductionOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setProductionOrder.getTimestamp("date_finished");
				int creatorId = setProductionOrder.getInt("creator_id");
				int authorizerId = setProductionOrder.getInt("authorizer_id");
				boolean processed = (setProductionOrder.getInt("processed")==1?true:false);
				productionOrder = new ProductionOrder(id, budgetId, timestampProcessed, timestampFinished, creatorId, authorizerId, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return productionOrder;
	}

	public ArrayList<Product> getProductionOrderProducts(int productionOrderId) {
		ArrayList<Product> products = new ArrayList<Product>();
		ResultSet setProducts;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_SWITCHES(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				int id = setProducts.getInt("id");
				int brandId = setProducts.getInt("brand_id");
				String brand = setProducts.getString("brand");
				int modelId = setProducts.getInt("model_id");
				String model = setProducts.getString("model");
				String reference = setProducts.getString("reference");
				int phases = setProducts.getInt("phases");
				int currentId = setProducts.getInt("current_id");
				String current = setProducts.getString("current");
				int voltageId = setProducts.getInt("voltage_id");
				String voltage = setProducts.getString("voltage");
				int interruptionId = setProducts.getInt("interruption_id");
				String interruption = setProducts.getString("interruption");
				double price = setProducts.getDouble("price");
				boolean active = (setProducts.getInt("active")==1?true:false);
				int containerId = setProducts.getInt("container_id");
				int quantity = setProducts.getInt("quantity");
				products.add(new Switch(id, brandId, brand, modelId, model, reference, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
			
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_BOXES(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				int id = setProducts.getInt("id");
				int typeId = setProducts.getInt("type_id");
				String type = setProducts.getString("type");
				BoxType boxType = new BoxType(typeId, type);
				
				int installationId = setProducts.getInt("installation_id");
				String strInstallation = setProducts.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				int nemaId = setProducts.getInt("nema_id");
				String strNema = setProducts.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				String strPairs = setProducts.getString("pairs");
				String strPairsNumeric = (strPairs.equals("N/A"))?"0":strPairs;
				int pairs = Integer.valueOf(strPairsNumeric);
				
				int sheetId = setProducts.getInt("sheet_id");
				String strSheet = setProducts.getString("sheet");
				Sheet sheet = new Sheet(sheetId, strSheet);
				
				int finishId = setProducts.getInt("finish_id");
				String strFinish = setProducts.getString("finish");
				Finish finish = new Finish(finishId, strFinish);
				
				int colorId = setProducts.getInt("color_id");
				String strColor = setProducts.getString("color");
				Color color = new Color(colorId, strColor);
				
				int height = setProducts.getInt("height");
				int width = setProducts.getInt("width");
				int depth = setProducts.getInt("depth");
				int unitsId = setProducts.getInt("units_id");
				String strUnits = setProducts.getString("units");
				MeasureUnits units = new MeasureUnits(unitsId, strUnits);
				
				int caliberId = setProducts.getInt("caliber_id");
				String strCaliber = setProducts.getString("caliber");
				int intCaliber = Integer.valueOf(((strCaliber.equals("N/A"))?"0":strCaliber));
				Caliber caliber = new Caliber(caliberId, intCaliber);
				
				String caliberComments = setProducts.getString("caliber_comments");
				int lockTypeId = setProducts.getInt("lock_type_id");
				String strLockType = setProducts.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				double price = setProducts.getDouble("price");
				String comments = setProducts.getString("comments");
				boolean active = (setProducts.getInt("active")==1?true:false);
				products.add(new Box(id, boxType, installation, nema, pairs, sheet, finish, color, height, width, depth, units, caliber, caliberComments, lockType, price, comments, active));
			}
			
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_BOARDS(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				int id = setProducts.getInt("id");
				String name = setProducts.getString("name");
				int boardTypeId = setProducts.getInt("type_id");
				String strBoardtype = setProducts.getString("type");
				BoardType boardType = new BoardType(boardTypeId, strBoardtype);
				
				int installationId = setProducts.getInt("installation_id");
				String strInstallation = setProducts.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				int nemaId = setProducts.getInt("nema_id");
				String strNema = setProducts.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				int barCapacityId = setProducts.getInt("bar_capacity_id");
				int strBarCapacity = setProducts.getInt("bar_capacity");
				BarCapacity barCapacity = new BarCapacity(barCapacityId, strBarCapacity);
				
				int barTypeId = setProducts.getInt("bar_type_id");
				String strBarType = setProducts.getString("nema");
				BarType barType = new BarType(barTypeId, strBarType);
				
				int circuitsId = setProducts.getInt("circuits_id");
				int strCircuits = setProducts.getInt("circuits");
				Circuits circuits = new Circuits(circuitsId, strCircuits);
				
				int boardVoltageId = setProducts.getInt("voltage_id");
				String strBoardVoltage = setProducts.getString("voltage");
				BoardVoltage boardVoltage = new BoardVoltage(boardVoltageId, strBoardVoltage);
				
				int phases = setProducts.getInt("phases");
				boolean ground = setProducts.getBoolean("ground");
				
				int interruptionId = setProducts.getInt("interruption_id");
				int strInterruption = setProducts.getInt("interruption");
				Interruption interruption = new Interruption(interruptionId, strInterruption);
				
				int lockTypeId = setProducts.getInt("lock_type_id");
				String strLockType = setProducts.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				ArrayList<Integer> mainSwitchIds = this.getBoardSwitchMainIds(id);
				
				ArrayList<Material> materials = this.getBoardMaterials(id);
				
				double price = setProducts.getDouble("price");
				String comments = setProducts.getString("comments");
				boolean active = (setProducts.getInt("active")==1?true:false);
				
				ArrayList<Switch> switches = new ArrayList<Switch>();
				switches = this.getBoardSwitches(id);
				products.add(new Board(id, name, boardType, installation, nema, barCapacity, barType, circuits, boardVoltage, phases, ground, interruption, lockType, mainSwitchIds, materials, price, comments, active, switches));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	public WorkOrder pullWorkOrder(int orderId) {
		WorkOrder workOrder = new WorkOrder();
		ResultSet setWorkOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_WORK_ORDER_INFO_BY_ID(?)}");
			statement.setInt(1, orderId);
			setWorkOrder = statement.executeQuery();
			if (setWorkOrder.next()) {
				int id = setWorkOrder.getInt("id");
				int productionOrderId = setWorkOrder.getInt("production_order_id");
				int productId = setWorkOrder.getInt("product_id");
				int productTypeId = setWorkOrder.getInt("product_type_id");
				int creatorId = setWorkOrder.getInt("creator_id");
				int authorizerId = setWorkOrder.getInt("authorizer_id");
				Timestamp timestampProcessed = setWorkOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setWorkOrder.getTimestamp("date_finished");
				boolean processed = (setWorkOrder.getInt("processed")==1?true:false);
				workOrder = new WorkOrder(id, productionOrderId, productId, productTypeId, creatorId, authorizerId, timestampProcessed, timestampFinished, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return workOrder;
	}
	
	public ArrayList<WorkOrder> pullAllWorkOrders(int productionOrderId) {
		ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();
		ResultSet setWorkOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_WORK_ORDER_INFO_BY_PRODUCTION_ORDER_ID(?)}");
			statement.setInt(1, productionOrderId);
			setWorkOrder = statement.executeQuery();
			while (setWorkOrder.next()) {
				int id = setWorkOrder.getInt("id");
				int productId = setWorkOrder.getInt("product_id");
				int productTypeId = setWorkOrder.getInt("product_type_id");
				int creatorId = setWorkOrder.getInt("creator_id");
				int authorizerId = setWorkOrder.getInt("authorizer_id");
				Timestamp timestampProcessed = setWorkOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setWorkOrder.getTimestamp("date_finished");
				boolean processed = (setWorkOrder.getInt("processed")==1?true:false);
				workOrders.add(new WorkOrder(id, productionOrderId, productId, productTypeId, creatorId, authorizerId, timestampProcessed, timestampFinished, processed));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return workOrders;
	}
	
	public boolean setProductionOrderProcessed(int orderId) {
		return Db.update("UPDATE production_orders SET processed = 1 WHERE id = " + orderId);
	}
	
	public boolean addMainSwitch(String table, int switchId) {
		return Db.update("UPDATE " + table + " SET main = 1 WHERE id = " + switchId);
	}
	
	public boolean removeMainSwitch(String table, int switchId) {
		return Db.update("UPDATE " + table + " SET main = 0 WHERE id = " + switchId);
	}

	public int getWorkOrderBudgetId(int workOrderId) {
		String queryString;
		ResultSet setWorkOrderBudgetId;
		
		queryString = "SELECT production_orders.budget_id FROM production_orders "
					+ "INNER JOIN work_orders ON work_orders.production_order_id = production_orders.id "
					+ "WHERE work_orders.id = " + workOrderId;
		
		setWorkOrderBudgetId = this.select(queryString);
		
		try {
			if (setWorkOrderBudgetId.next()) {
				return setWorkOrderBudgetId.getInt("production_orders.budget_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getBoxComments(int boxId) {
		ResultSet setComments;
		String comments = "";
		setComments = this.select("SELECT comments FROM boxes WHERE id = " + boxId);
		
		try {
			setComments.first();
			comments = setComments.getString("comments");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los comentarios de la caja");
		}
		return comments;
	}
	
	public int getControlBoardTypeId(String type) {
		ResultSet setType;
		int typeId = 0;
		setType = this.select("SELECT id FROM control_board_types WHERE type = '" + type + "'");
		
		try {
			if(setType.next()) {
				typeId = setType.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo");
		}
		return typeId;
	}

	public int getControlBoardBarCapacityId(Integer barCapacity) {
		ResultSet setBarCapacity;
		int barCapacityId = 0;
		setBarCapacity = this.select("SELECT id FROM control_board_bar_capacities WHERE bar_capacity = '" + barCapacity + "'");
		
		try {
			if(setBarCapacity.next()) {
				barCapacityId = setBarCapacity.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de la capacidad de barra");
		}
		return barCapacityId;
	}

	public int getControlBoardBarTypeId(String barType) {
		ResultSet setBarType;
		int barTypeId = 0;
		setBarType = this.select("SELECT id FROM control_board_bar_types WHERE bar_type = '" + barType + "'");
		
		try {
			if(setBarType.next()) {
				barTypeId = setBarType.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo de barra");
		}
		return barTypeId;
	}

	public int getControlBoardCircuitsId(Integer circuits) {
		ResultSet setCircuits;
		int circuitsId = 0;
		setCircuits = this.select("SELECT id FROM control_board_circuits WHERE circuits = '" + circuits + "'");
		
		try {
			if(setCircuits.next()) {
				circuitsId = setCircuits.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id de los circuitos");
		}
		return circuitsId;
	}

	public int getControlBoardVoltageId(String voltage) {
		ResultSet setVoltage;
		int voltageId = 0;
		setVoltage = this.select("SELECT id FROM control_board_voltages WHERE voltage = '" + voltage + "'");
		
		try {
			if(setVoltage.next()) {
				voltageId = setVoltage.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del voltaje");
		}
		return voltageId;
	}

	public boolean editControlBoard(Integer controlBoardId, ArrayList<Object> listFields,
			ArrayList<Object> listValues) {
		if (listFields.size() > 0) {
			String queryFields = "";
			Iterator<Object> it1 = listFields.iterator();
			Iterator<Object> it2 = listValues.iterator();
			while (it1.hasNext() && it2.hasNext()) {
				queryFields += it1.next().toString() + " = " + it2.next().toString() + ",";
			}
			queryFields = StringTools.removeLastChar(queryFields);
			String queryString = "UPDATE control_boards SET " + queryFields + " WHERE id = " + controlBoardId;
			if (Db.update(queryString)) {
				return true;
			}
		}
		return false;
	}

	public boolean removeControlBoardSwitch(Integer switchId) {
		String queryDelete;
		queryDelete = "DELETE FROM control_board_switches WHERE id = '" + switchId + "'";
		return this.delete(queryDelete);
	}

	public boolean addControlBoard(String controlBoardName, String controlBoardType, String controlBoardInstallation,
			String controlBoardNema, Integer controlBoardBarCapacity, String controlBoardBarType, Integer controlBoardCircuits,
			String controlBoardVoltage, Integer controlBoardPhases, String controlBoardGround, Integer controlBoardInterruption,
			String controlBoardLockType, Double controlBoardPrice) {
		int typeId = this.getControlBoardTypeId(controlBoardType);
		int installationId = this.getInstallationId(controlBoardInstallation);
		int nemaId = this.getNemaId(controlBoardNema);
		int barCapacityId = this.getControlBoardBarCapacityId(controlBoardBarCapacity);
		int barTypeId = this.getControlBoardBarTypeId(controlBoardBarType);
		int circuitsId = this.getControlBoardCircuitsId(controlBoardCircuits);
		int voltageId = this.getControlBoardVoltageId(controlBoardVoltage);
		int interruptionId = this.getInterruptionId(controlBoardInterruption);
		int lockTypeId = this.getLockTypeId(controlBoardLockType);

		String queryInsert = "INSERT INTO control_boards (name, type_id, installation_id, nema_id, bar_capacity_id, bar_type_id, circuits_id, voltage_id, phases, ground, interruption_id, lock_type_id, price) "
				+ "VALUES('" + controlBoardName + "', " + typeId + ", " + installationId + ", " + nemaId + ", " + barCapacityId + ", " + barTypeId + ", " + circuitsId + ", " + voltageId + ", '" + controlBoardPhases + "', '" + controlBoardGround + "', " + interruptionId + ", " + lockTypeId + ", " + controlBoardPrice + ")";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}

	public String getControlBoardComments(Integer controlBoardId) {
		ResultSet setBoardCommentsPrice;
		String comments = "";
		setBoardCommentsPrice = this.select("SELECT comments FROM control_boards WHERE id = '" + controlBoardId + "'");
		
		try {
			setBoardCommentsPrice.first();
			comments = setBoardCommentsPrice.getString("comments");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener las observaciones del tablero de control");
		}
		return comments;
	}

	public int getControlBoardSwitchesQuantity(Integer controlBoardId) {
		ResultSet setControlBoardSwitchesQuantity;
		int controlBoardSwitchesQuantity = 0;
		setControlBoardSwitchesQuantity = this.select("SELECT IFNULL(SUM(control_board_switches.quantity * switches.phases),0) as cnt " +
													"FROM control_board_switches, switches " +
													"WHERE control_board_switches.control_board_container_id = '" + controlBoardId + "' " +
													"AND control_board_switches.switch_id = switches.id");
		
		try {
			setControlBoardSwitchesQuantity.first();
			controlBoardSwitchesQuantity = setControlBoardSwitchesQuantity.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la cantidad de interruptores en este tablero de control");
		}
		return controlBoardSwitchesQuantity;
	}

	public boolean addControlBoardSwitch(Integer containerId, Integer switchId, Integer quantity,
			Double switchPrice) {
		if(this.controlBoardSwitchExists(containerId, switchId)) {
			JOptionPane.showMessageDialog(null, "Este Interruptor ya existe dentro del tablero de control");
			return false;
		} else {
			String queryString = "INSERT INTO control_board_switches (control_board_container_id, switch_id, quantity, price) "
								+ "VALUES (" + containerId + ", " + switchId + ", " + quantity + ", " + switchPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean controlBoardSwitchExists(Integer containerId, Integer switchId) {
		String queryString;
		
		queryString = "SELECT * FROM control_board_switches "
					+ "WHERE control_board_switches.control_board_container_id = '" + containerId + "' "
					+ "AND control_board_switches.switch_id = '" + switchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean lockTypeExists(String lockType) {
		String queryString;
		
		queryString = "SELECT id FROM lock_types WHERE lock_type = '" + lockType + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean addLockType(String lockType) {
		String queryInsert;
		
		queryInsert = "INSERT INTO lock_types (lock_type) VALUES ('" + lockType + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}

	public boolean removeLockType(String lockType) {
		String queryDelete;
		
		queryDelete = "DELETE FROM lock_types WHERE lock_type = '" + lockType + "'";
		return this.delete(queryDelete);
	}

	public boolean boardCircuitsExists(String boardCircuits) {
		String queryString;
		
		queryString = "SELECT circuits FROM board_circuits WHERE circuits = " + boardCircuits;
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean addBoardCircuits(String boardCircuits) {
		String queryInsert;
		
		queryInsert = "INSERT INTO board_circuits (circuits) VALUES (" + boardCircuits + ")";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}

	public boolean removeBoardCircuits(String boardCircuits) {
		String queryDelete;
		
		queryDelete = "DELETE FROM board_circuits WHERE circuits = '" + boardCircuits + "'";
		return this.delete(queryDelete);
	}

	public boolean boardVoltageExists(String boardVoltage) {
		String queryString;
		
		queryString = "SELECT voltage FROM board_voltages WHERE voltage = '" + boardVoltage + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean addBoardVoltage(String boardVoltage) {
		String queryInsert;
		
		queryInsert = "INSERT INTO board_voltages (voltage) VALUES ('" + boardVoltage + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}

	public boolean removeBoardVoltage(String boardVoltage) {
		String queryDelete;
		
		queryDelete = "DELETE FROM board_voltages WHERE voltage = '" + boardVoltage + "'";
		return this.delete(queryDelete);
	}

	public boolean budgetPaymentMethodExists(String paymentMethod) {
		String queryString;
		
		queryString = "SELECT method FROM budget_payment_methods WHERE method = '" + paymentMethod + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean addBudgetPaymentMethod(String paymentMethod) {
		String queryInsert;
		
		queryInsert = "INSERT INTO budget_payment_methods (method) VALUES ('" + paymentMethod + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}

	public boolean removeBudgetPaymentMethod(String paymentMethod) {
		String queryDelete;
		
		queryDelete = "DELETE FROM budget_payment_methods WHERE method = '" + paymentMethod + "'";
		return this.delete(queryDelete);
	}

	public boolean addBudgetControlBoard(int selectedBudgetId, int controlBoardSearchId,
			int controlBoardQuantity, double controlBoardPrice) {
		if(this.budgetControlBoardExists(selectedBudgetId, controlBoardSearchId)) {
			JOptionPane.showMessageDialog(null, "Este tablero de control ya existe dentro del presupuesto");
			return false;
		} else {
			String queryString = "INSERT INTO budget_control_boards (budget_container_id, control_board_id, price, quantity) "
								+ "VALUES (" + selectedBudgetId + ", " + controlBoardSearchId + ", " + controlBoardPrice + ", " + controlBoardQuantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean budgetControlBoardExists(int selectedBudgetId, int controlBoardSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_control_boards "
					+ "WHERE budget_control_boards.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_control_boards.control_board_id = '" + controlBoardSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean removeBudgetControlBoard(int selectedBudgetControlBoardId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_control_boards WHERE id = '" + selectedBudgetControlBoardId + "'";
		return this.delete(queryDelete);
	}

	public int getMaterialBoardId(int boardMaterialId) {
		ResultSet setBoardMaterial;
		int boardId = 0;
		setBoardMaterial = this.select("SELECT board_container_id FROM board_materials WHERE id = '" + boardMaterialId + "'");
		
		try {
			setBoardMaterial.first();
			boardId = setBoardMaterial.getInt("board_container_id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tablero");
		}
		return boardId;
	}

	public boolean removeBoardMaterial(int boardMaterialId) {
		String queryDelete;
		queryDelete = "DELETE FROM board_materials WHERE id = '" + boardMaterialId + "'";
		return this.delete(queryDelete);
	}

	public boolean addBoardMaterial(int containerId, int materialId, int quantity,
			double materialPrice) {
		if(this.boardMaterialExists(containerId, materialId)) {
			JOptionPane.showMessageDialog(null, "Este material ya existe dentro del tablero");
			return false;
		} else {
			String queryString = "INSERT INTO board_materials (board_container_id, material_id, quantity, price) "
								+ "VALUES (" + containerId + ", " + materialId + ", " + quantity + ", " + materialPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean boardMaterialExists(int containerId, int materialId) {
		String queryString;
		
		queryString = "SELECT * FROM board_materials "
					+ "WHERE board_materials.board_container_id = '" + containerId + "' "
					+ "AND board_materials.material_id = '" + materialId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean addMaterial(String reference, String description, double price) {
		if(this.materialExists(reference, description)) {
			JOptionPane.showMessageDialog(null, "Este material ya existe");
			return false;
		} else {
			String queryString = "INSERT INTO materials (reference, material, price) "
								+ "VALUES ('" + reference + "', '" + description + "', " + price + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean materialExists(String reference, String description) {
		String queryString;
		
		queryString = "SELECT * FROM materials "
					+ "WHERE materials.reference = '" + reference + "' "
					+ "AND materials.material = '" + description + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean removeControlBoardMaterial(int controlBoardMaterialId) {
		String queryDelete;
		queryDelete = "DELETE FROM control_board_materials WHERE id = '" + controlBoardMaterialId + "'";
		return this.delete(queryDelete);
	}

	public boolean addControlBoardMaterial(int containerId, int materialId, int quantity,
			double materialPrice) {
		if(this.controlBoardMaterialExists(containerId, materialId)) {
			JOptionPane.showMessageDialog(null, "Este material ya existe dentro del tablero de control");
			return false;
		} else {
			String queryString = "INSERT INTO control_board_materials (board_container_id, material_id, quantity, price) "
								+ "VALUES (" + containerId + ", " + materialId + ", " + quantity + ", " + materialPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean controlBoardMaterialExists(int containerId, int materialId) {
		String queryString;
		
		queryString = "SELECT * FROM control_board_materials "
					+ "WHERE control_board_materials.board_container_id = '" + containerId + "' "
					+ "AND control_board_materials.material_id = '" + materialId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public boolean removeBudgetMaterial(int budgetMaterialId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_materials WHERE id = '" + budgetMaterialId + "'";
		return this.delete(queryDelete);
	}

	public boolean addBudgetMaterial(int containerId, int materialId, int quantity,
			double materialPrice) {
		if(this.budgetMaterialExists(containerId, materialId)) {
			JOptionPane.showMessageDialog(null, "Este material ya existe dentro del presupuesto");
			return false;
		} else {
			String queryString = "INSERT INTO budget_materials (budget_container_id, material_id, quantity, price) "
								+ "VALUES (" + containerId + ", " + materialId + ", " + quantity + ", " + materialPrice + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean budgetMaterialExists(int containerId, int materialId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_materials "
					+ "WHERE budget_materials.budget_container_id = '" + containerId + "' "
					+ "AND budget_materials.material_id = '" + materialId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public int getUserTypeId(String userType) {
		int userTypeId = 0;
		String queryString;
		
		queryString = "SELECT id FROM user_types "
					+ "WHERE user_types.type = '" + userType + "'";
		
		ResultSet setUserType = this.select(queryString);
		
		try {
			if (setUserType.next()) {
				userTypeId = setUserType.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener id de tipo de usuario");
		}
		
		return userTypeId;
	}

	public boolean addUser(String username, String password, String passport, int userTypeId, String firstName,
			String lastName, String email, String phone, int status) {
		String encryptedPassword = PasswordEncryptor.encryptPassword(password);
		if(this.userExists(username)) {
			JOptionPane.showMessageDialog(null, "Este usuario ya existe");
			return false;
		} else {
			String queryString = "INSERT INTO users (username, password, passport, user_type_id, first_name, last_name, email, phone, status) "
								+ "VALUES ('" + username + "', '" + encryptedPassword + "', '" + passport + "', " + userTypeId + ", '" + firstName + "', '" + lastName + "', '" + email + "', '" + phone + "', " + status + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}

	private boolean userExists(String username) {
		String queryString;
		
		queryString = "SELECT * FROM users "
					+ "WHERE users.username = '" + username + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}

	public static int getUserId(String username) {
		Db db = new Db();
		int userId = 0;
		ResultSet rowUser = db.select("SELECT id FROM users WHERE username = '" + username + "'");
		
		try {
			if(rowUser.next()) {
				userId = rowUser.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			db.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userId;
	}

	public User loadUserInfo(int userId) {
		User user = null;
		ResultSet rowsUser = this.select(
				"SELECT u.id, "
					+ "u.username, "
					+ "u.password, "
					+ "u.passport, "
					+ "u.user_type_id, "
					+ "t.type, "
					+ "u.first_name, "
					+ "u.last_name, "
					+ "u.email, "
					+ "u.phone, "
					+ "u.date_created, "
					+ "u.status "
				+ "FROM users u, user_types t "
				+ "WHERE u.id = " + userId + " AND u.user_type_id = t.id");
		try {
			while(rowsUser.next()){
				user = new User(rowsUser.getInt("id"),
						rowsUser.getString("username"),
						rowsUser.getString("password"),
						rowsUser.getString("passport"),
						new UserType(rowsUser.getInt("user_type_id"), rowsUser.getString("type")),
						rowsUser.getString("first_name"),
						rowsUser.getString("last_name"),
						rowsUser.getString("email"),
						rowsUser.getString("phone"),
						rowsUser.getTimestamp("date_created"),
						(rowsUser.getInt("status")==0)?false:true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return user;
	}
	
}
