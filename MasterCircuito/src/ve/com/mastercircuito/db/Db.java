package ve.com.mastercircuito.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import ve.com.mastercircuito.utils.StringTools;

public class Db extends MysqlDriver {
	
	public Db() {
		super();
	}
	
	public Db(String host, String dbuser, String dbpassword, String database) {
		super(host,dbuser,dbpassword,database);
	}
	
	public boolean switchExists(String phases, String current, String brand, String type, String interruption, String model) {
		String queryString;
		
		queryString = "SELECT * FROM switches, switch_brands, switch_types, currents, interruptions "
					+ "WHERE switches.brand_id = switch_brands.id "
					+ "AND switches.type_id = switch_types.id "
					+ "AND switches.interruption_id = interruptions.id "
					+ "AND switches.current_id = currents.id "
					+ "AND switches.phases = '" + phases + "' "
					+ "AND switch_types.type = '" + type + "' "
					+ "AND currents.current = '" + current + "' "
					+ "AND switch_brands.brand = '" + brand + "' "
					+ "AND interruptions.interruption = '" + interruption + "' "
					+ "AND switches.model = '" + model + "'";
		
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
			if (this.update(queryString)) {
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
			if (this.update(queryString)) {
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
			if (this.update(queryString)) {
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
	
	public boolean switchTypeExists(String type) {
		String queryString;
		
		queryString = "SELECT id FROM switch_types WHERE type = '" + type + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public boolean addSwitchType(String type, String brand) {
		String queryInsert;
		int brandId = this.getSwitchBrandId(brand);
		
		if(brandId < 1) {
			return false;
		}
		
		queryInsert = "INSERT INTO switch_types (type, brand_id) VALUES ('" + type + "', " + brandId + ")";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean removeSwitchType(String type) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_types WHERE type = '" + type + "'";
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
	
	public int getSwitchTypeId(String type) {
		ResultSet setType;
		int typeId = 0;
		setType = this.select("SELECT id FROM switch_types WHERE type = '" + type + "'");
		
		try {
			setType.first();
			typeId = setType.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener el id del tipo");
		}
		return typeId;
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
			setInterruption.first();
			interruptionId = setInterruption.getInt("id");
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
		setInstallation = this.select("SELECT id FROM installations WHERE installation = '" + installation + "'");
		
		try {
			setInstallation.first();
			installationId = setInstallation.getInt("id");
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
			setLockType.first();
			lockTypeId = setLockType.getInt("id");
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
	
	public int getBoardBarCapacityId(Integer barCapacity) {
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
	
	public int getBoardCircuitsId(Integer circuits) {
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
		setDeliveryPeriod = this.select("SELECT id FROM budget_delivery_period WHERE delivery_period = '" + delivery_period + "'");
		
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
	
	public Integer getBudgetExpiryDays(int budgetId) {
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
	
	
	public boolean addSwitch(String model, String brand, String type, String phases, int current, String voltage, int interruption, String price) {
		int brandId = this.getSwitchBrandId(brand);
		int typeId = this.getSwitchTypeId(type);
		int currentId = this.getCurrentId(current);
		int voltageId = this.getVoltageId(voltage);
		int interruptionId = this.getInterruptionId(interruption);
		
		String queryInsert = "INSERT INTO switches (model, brand_id, type_id, phases, current_id, voltage_id, interruption_id, price) "
				+ "VALUES('" + model + "', " + brandId + ", " + typeId + ", '" + phases + "', " + currentId + ", " + voltageId + ", " + interruptionId + ", '" + price + "')";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean addBox(String type, String installation, String nema, int pairs, String sheet, String finish, String color, Double height, Double width, Double depth, String units, String caliber, String caliberComments, String lockType, Double price) {
		int typeId = this.getBoxTypeId(type);
		int installationId = this.getInstallationId(installation);
		int nemaId = this.getNemaId(nema);
		int sheetId = this.getBoxSheetId(sheet);
		int finishId = (finish.isEmpty())?0:this.getBoxFinishId(finish);
		int colorId = (color.isEmpty())?0:this.getBoxColorId(color);
		int unitsId = this.getBoxUnitsId(units);
		int caliberId = this.getBoxCaliberId(caliber);
		int lockTypeId = this.getLockTypeId(lockType);

		String queryInsert = "INSERT INTO boxes (type_id, installation_id, nema_id, pairs, sheet_id, finish_id, color_id, height, width, depth, units_id, caliber_id, caliber_comments, lock_type_id, price) "
				+ "VALUES(" + typeId + ", " + installationId + ", " + nemaId + ", " + pairs + ", " + sheetId + ", " + finishId + ", " + colorId + ", " + height + ", " + width + ", " + depth + ", " + unitsId + ", " + caliberId + ", '" + caliberComments + "', " + lockTypeId + ", " + price + ")";
		
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public boolean addBoard(String name, String type, String installation, String nema, Integer barCapacity, String barType, Integer circuits, String voltage, Integer phases, String ground, Integer interruption, String lockType, Double price) {
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
	

	public boolean addBudget(String date, Integer expiryDays, String clientId, 
			String workName, String method, String seller,
			String place, Integer deliveryTime, String deliveryPeriod) {
		// TODO Finish this(Add budget method)
			
		int paymentMethodId = this.getPaymentMethodId(method);
		int sellerId = this.getSellerId(seller);
		int dispatchPlaceId = this.getDispachPlaceId(place);
		int deliveryPeriodId= this.getDeliveryPeriodId(deliveryPeriod);	
		
		String queryInsert = "INSERT INTO budgets (code, `date`, expiry_days, client_id, work_name, payment_method_id, seller_id, dispatch_place_id, delivery_time, delivery_period_id) "
				+ "VALUES('" + date + "', " + expiryDays + ", " + clientId + ", '" + workName + "', " + paymentMethodId + ", " + sellerId + ", '" + dispatchPlaceId + "', '" + deliveryTime + "', " + deliveryPeriod + ")";
		
		this.insert(queryInsert);
		int insertedId = this.getInsertId();
		if(insertedId > 0) {
			
			DecimalFormat df = new DecimalFormat("00");
			DateTime dt = new DateTime();
			String month = df.format(dt.getMonthOfYear());
			String year = df.format(dt.getYearOfCentury());
			
			String leadingBudgetCode = "p-" + month + year ;
			int lastBudgetCode = Integer.valueOf(this.getLastBudgetCode(insertedId));
			int newBudgetCode = lastBudgetCode + 1;
			
			String trailingBudgetCode = StringTools.fillWithZeros(newBudgetCode, 5);
			
			this.update("UPDATE budgets SET code = '" + leadingBudgetCode + trailingBudgetCode + "' WHERE id = " + insertedId);
		}		

		return (insertedId > 0)? true:false;		
		
	}
	
	private String getLastBudgetCode(int insertId) {
		ResultSet setBudgetCode;
		String fullCode = "";
		setBudgetCode = this.select("SELECT code FROM budgets WHERE code <> '' ORDER BY id DESC LIMIT 1");
		
		try {
			//setBudgetCode.first();
			if(setBudgetCode.next()) {
				fullCode = setBudgetCode.getString("code");
			} else {
				fullCode = "00000";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener la fecha");
		}
		if(null != fullCode && !fullCode.isEmpty()) {
			return fullCode.substring(6);
		} else {
			// For revision, I'm not sure if this is the correct value that should be returned when
			// fullCode contains null or it's empty
			return "";
		}
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
			if (this.update(queryString)) {
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
	
	public String getBoardSwitchMainId(int boardId) {
		String queryString;
		ResultSet setBoardSwitchMainId;
		
		queryString = "SELECT boards.main_switch_id FROM boards "
					+ "WHERE boards.id = '" + boardId + "'";
		
		setBoardSwitchMainId = this.select(queryString);
		
		try {
			if (setBoardSwitchMainId.next()) {
				return setBoardSwitchMainId.getString("boards.main_switch_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public boolean addBoardSwitch(int containerId, int switchId, int quantity) {
		int boardSwitchId = 0;
		if(this.boardSwitchExists(containerId, switchId)) {
			boardSwitchId = this.getBoardSwitchId(containerId, switchId);
			if(boardSwitchId > 0) {
				return this.increaseBoardSwitch(boardSwitchId, quantity);
			} else {
				return false;
			}
		} else {
			String queryString = "INSERT INTO board_switches (board_container_id, switch_id, quantity) "
								+ "VALUES (" + containerId + ", " + switchId + ", " + quantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private boolean increaseBoardSwitch(int boardSwitchId, int quantity) {
		String queryString = "UPDATE board_switches SET quantity = quantity + " + quantity 
							+ " WHERE id = " + boardSwitchId;
		
		return this.update(queryString);
	}
	
	public String getBoardMaterials(int boardId) {
		ResultSet setBoardMaterials;
		String materials = "";
		setBoardMaterials = this.select("SELECT materials FROM boards WHERE id = '" + boardId + "'");
		
		try {
			setBoardMaterials.first();
			materials = setBoardMaterials.getString("materials");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los materiales del tablero");
		}
		return materials;
	}
	
	public Double getBoardMaterialsPrice(int boardId) {
		ResultSet setBoardMaterialsPrice;
		Double materialsPrice = 0.00;
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
	
}
