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

import ve.com.mastercircuito.components.BarCapacity;
import ve.com.mastercircuito.components.BarType;
import ve.com.mastercircuito.components.Board;
import ve.com.mastercircuito.components.BoardType;
import ve.com.mastercircuito.components.BoardVoltage;
import ve.com.mastercircuito.components.Box;
import ve.com.mastercircuito.components.BoxType;
import ve.com.mastercircuito.components.Budget;
import ve.com.mastercircuito.components.BudgetStage;
import ve.com.mastercircuito.components.Caliber;
import ve.com.mastercircuito.components.Circuits;
import ve.com.mastercircuito.components.Client;
import ve.com.mastercircuito.components.Color;
import ve.com.mastercircuito.components.DeliveryPeriod;
import ve.com.mastercircuito.components.DispatchPlace;
import ve.com.mastercircuito.components.Finish;
import ve.com.mastercircuito.components.Installation;
import ve.com.mastercircuito.components.Interruption;
import ve.com.mastercircuito.components.LockType;
import ve.com.mastercircuito.components.Material;
import ve.com.mastercircuito.components.MeasureUnits;
import ve.com.mastercircuito.components.Nema;
import ve.com.mastercircuito.components.PaymentMethod;
import ve.com.mastercircuito.components.Product;
import ve.com.mastercircuito.components.ProductionOrder;
import ve.com.mastercircuito.components.User;
import ve.com.mastercircuito.components.Sheet;
import ve.com.mastercircuito.components.Switch;
import ve.com.mastercircuito.components.UserType;
import ve.com.mastercircuito.components.WorkOrder;
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
	
	public Boolean switchExists(String phases, String current, String brand, String type, String interruption, String model) {
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
	
	public Boolean editSwitch(Integer switchId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
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
	
	public Boolean editBox(Integer boxId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
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
	
	public Boolean editBoard(Integer boardId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
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
	
	public Boolean switchBrandExists(String brand) {
		String queryString;
		
		queryString = "SELECT id FROM switch_brands WHERE brand = '" + brand + "'";
		this.select(queryString);

		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addSwitchBrand(String brand) {
		String queryInsert;
		
		queryInsert = "INSERT INTO switch_brands (brand) VALUES ('" + brand + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeSwitchBrand(String brand) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_brands WHERE brand = '" + brand + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean switchTypeExists(String type) {
		String queryString;
		
		queryString = "SELECT id FROM switch_types WHERE type = '" + type + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addSwitchType(String type, String brand) {
		String queryInsert;
		int brandId = this.getSwitchBrandId(brand);
		
		if(brandId < 1) {
			return false;
		}
		
		queryInsert = "INSERT INTO switch_types (type, brand_id) VALUES ('" + type + "', " + brandId + ")";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeSwitchType(String type) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_types WHERE type = '" + type + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean currentExists(String current) {
		String queryString;
		
		queryString = "SELECT id FROM currents WHERE current = '" + current + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addCurrent(String current) {
		String queryInsert;
		
		queryInsert = "INSERT INTO currents (current) VALUES ('" + current + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeCurrent(String current) {
		String queryDelete;
		
		queryDelete = "DELETE FROM currents WHERE current = '" + current + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean voltageExists(String voltage) {
		String queryString;
		
		queryString = "SELECT id FROM switch_voltages WHERE voltage = '" + voltage + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addVoltage(String voltage, String type) {
		String queryInsert;
		
		queryInsert = "INSERT INTO switch_voltages (voltage) VALUES ('" + voltage + "V" + type + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeVoltage(String voltage) {
		String queryDelete;
		
		queryDelete = "DELETE FROM switch_voltages WHERE voltage = '" + voltage + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean interruptionExists(String interruption) {
		String queryString;
		
		queryString = "SELECT id FROM interruptions WHERE interruption = '" + interruption + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addInterruption(String interruption) {
		String queryInsert;
		
		queryInsert = "INSERT INTO interruptions (interruption) VALUES ('" + interruption + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeInterruption(String interruption) {
		String queryDelete;
		
		queryDelete = "DELETE FROM interruptions WHERE interruption = '" + interruption + "'";
		return this.delete(queryDelete);
	}
	
	public Integer getSwitchBrandId(String brand) {
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
	
	public Integer getSwitchTypeId(String type) {
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
	
	public Integer getCurrentId(Integer current) {
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
	
	public Integer getVoltageId(String voltage) {
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
	
	public Integer getInterruptionId(Integer interruption) {
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
	
	public Integer getBoxTypeId(String type) {
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
	
	public Integer getInstallationId(String installation) {
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
	
	public Integer getNemaId(String nema) {
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
	
	public Integer getBoxSheetId(String sheet) {
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
	
	public Integer getBoxFinishId(String finish) {
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
	
	public Integer getBoxColorId(String color) {
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
	
	public Boolean colorExists(String color) {
		String queryString;
		
		queryString = "SELECT id FROM box_colors WHERE color = '" + color + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addColor(String color) {
		String queryInsert;
		
		queryInsert = "INSERT INTO box_colors (color) VALUES ('" + color + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeColor(String color) {
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
	
	public Boolean caliberExists(String caliber) {
		String queryString;
		
		queryString = "SELECT id FROM box_calibers WHERE caliber = '" + caliber + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addCaliber(String caliber) {
		String queryInsert;
		
		queryInsert = "INSERT INTO box_calibers (caliber) VALUES ('" + caliber + "')";
		this.insert(queryInsert);
		
		return (this.getInsertId() > 0)? true:false;
	}
	
	public Boolean removeCaliber(String caliber) {
		String queryDelete;
		
		queryDelete = "DELETE FROM box_calibers WHERE caliber = '" + caliber + "'";
		return this.delete(queryDelete);
	}
	
	public Integer getLockTypeId(String lockType) {
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
	
	public String getBoxCaliberComments(Integer id) {
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
	
	public Integer getBoardTypeId(String type) {
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
	
	public Integer getBoardBarCapacityId(Integer barCapacity) {
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
	
	public Integer getBoardBarTypeId(String barType) {
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
	
	public Integer getBoardCircuitsId(Integer circuits) {
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
	
	public Integer getBoardVoltageId(String voltage) {
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
	
	public Integer getBoardContainerId(Integer switchId) {
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
	
	public Integer getSwitchBoardId(Integer boardSwitchId) {
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
	
	public Integer getBoardSwitchId(Integer boardSwitchId) {
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
	
	public Integer getDispachPlaceId (String place){
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
	
	public Integer getPaymentMethodId (String method){
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
	
	public Integer getSellerId (String seller){
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
	
	public Integer getDeliveryPeriodId(String delivery_period) {
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
	
	public String getBudgetCode(Integer budgetId) {
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
	
	public Integer getBudgetExpiryDays(Integer budgetId) {
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
	
	public String getBudgetWorkName(Integer budgetId) {
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
	
	public String getBudgetDate(Integer budgetId) {
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
	
	public Boolean addSwitch(String model, String brand, String type, String phases, Integer current, String voltage, Integer interruption, String price) {
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
	
	public Boolean addBox(String type, String installation, String nema, int pairs, String sheet, String finish, String color, Double height, Double width, Double depth, String units, String caliber, String caliberComments, String lockType, Double price) {
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
	
	public Boolean addBoard(String name, String type, String installation, String nema, Integer barCapacity, String barType, Integer circuits, String voltage, Integer phases, String ground, Integer interruption, String lockType, Double price) {
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
	
	public Boolean addBudget(String date, Integer expiryDays, String clientId, 
			String workName, String method, Integer sellerId,
			String place, Integer deliveryTime, String deliveryPeriod) {
		
		Integer paymentMethodId = this.getPaymentMethodId(method);
		Integer dispatchPlaceId = this.getDispachPlaceId(place);
		Integer deliveryPeriodId= this.getDeliveryPeriodId(deliveryPeriod);
		Integer budgetCodeId = 0;
		Integer budgetId = 0;
		
		try {
			DateTime serverDateTime = new DateTime(new java.util.Date());
			Integer serverYear = serverDateTime.getYear();
			
			Boolean deleteResult = this.delete("DELETE FROM budget_code_ids WHERE year < " + serverYear);
			this.select("SELECT * FROM budget_code_ids");
			Integer totalRows = this.getNumRows();
			if (deleteResult && totalRows == 0) {
				this.query("ALTER TABLE budget_code_ids AUTO_INCREMENT=1");
			}
			if((deleteResult && totalRows == 0) || (!deleteResult && totalRows == 0) 
					|| (!deleteResult && totalRows > 0)){
				String queryInsertBudgetCode = "INSERT INTO budget_code_ids (year) VALUES(" + serverYear + ")";
				this.insert(queryInsertBudgetCode);
				budgetCodeId = this.getInsertId();
				String stringBudgetCodeId = this.prepareBudgetCode(serverDateTime, budgetCodeId);
				
				String queryInsertBudget = "INSERT INTO budgets (code, `date`, expiry_days, client_id, work_name, payment_method_id, seller_id, dispatch_place_id, delivery_time, delivery_period_id) "
						+ "VALUES('" + stringBudgetCodeId + "', '" + date + "', " + expiryDays + ", " + clientId + ", '" + workName + "', " + paymentMethodId + ", " + sellerId + ", '" + dispatchPlaceId + "', '" + deliveryTime + "', " + deliveryPeriodId + ")";
				this.insert(queryInsertBudget);
				budgetId = this.getInsertId();
				
				String queryUpdateBudgetCodeId = "UPDATE budget_code_ids SET budget_code_id = " + budgetId + " WHERE id = " + budgetCodeId;
				this.update(queryUpdateBudgetCodeId);
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el presupuesto, por favor contacte al programador");
		}
		
		return (budgetId > 0)? true:false;		
		
	}
	
	private String prepareBudgetCode(DateTime serverDateTime, Integer budgetCodeId) {
		DecimalFormat df = new DecimalFormat("00");
		String serverDay = df.format(serverDateTime.getDayOfMonth());
		String serverMonth = df.format(serverDateTime.getMonthOfYear());
		String serverYear = df.format(serverDateTime.getYearOfCentury());
		DecimalFormat df2 = new DecimalFormat("000000");
		String stringBudgetCodeId = df2.format(budgetCodeId);
		
		String budgetCode = "p-" + serverDay + serverMonth + serverYear + stringBudgetCodeId;
		return budgetCode;
	}
	
	public Boolean editBudget(Integer budgetId, ArrayList<Object> listFields, ArrayList<Object> listValues) {
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
	
	public Boolean removeBoardSwitch(Integer switchId) {
		String queryDelete;
		queryDelete = "DELETE FROM board_switches WHERE id = '" + switchId + "'";
		return this.delete(queryDelete);
	}
	
	public ArrayList<Integer> getBoardSwitchMainIds(Integer boardId) {
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
	
	public Integer getBoardSwitchesQuantity(Integer boardId) {
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
	
	public Integer getSwitchPhases(Integer switchId) {
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
	
	public Boolean boardSwitchExists(Integer containerId, Integer switchId) {
		String queryString;
		
		queryString = "SELECT * FROM board_switches "
					+ "WHERE board_switches.board_container_id = '" + containerId + "' "
					+ "AND board_switches.switch_id = '" + switchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Integer getBoardSwitchId(Integer containerId, Integer switchId) {
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
	
	public Boolean addBoardSwitch(Integer containerId, Integer switchId, Integer quantity) {
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
	
	private Boolean increaseBoardSwitch(Integer boardSwitchId, Integer quantity) {
		String queryString = "UPDATE board_switches SET quantity = quantity + " + quantity 
							+ " WHERE id = " + boardSwitchId;
		
		return this.update(queryString);
	}
	
	public ArrayList<Material> getBoardMaterials(Integer boardId) {
		ResultSet setBoardMaterials;
		ArrayList<Material> materials = new ArrayList<Material>();
		setBoardMaterials = this.select("SELECT * FROM materials WHERE id = '" + boardId + "'");
		
		try {
			while(setBoardMaterials.next()) {
				Integer id = setBoardMaterials.getInt("id");
				String material = setBoardMaterials.getString("material");
				Double price = setBoardMaterials.getDouble("price");
				materials.add(new Material(id, material, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los materiales del tablero");
		}
		return materials;
	}
	
	public Double getBoardMaterialsPrice(Integer boardId) {
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
	
	public String getBoardComments(Integer boardId) {
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
	
	public Integer getBudgetClientId(Integer budgetId) {
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
	
	public String getBudgetClientName(Integer budgetId) {
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
	
	public String getBudgetClientRepresentative(Integer budgetId) {
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
	
	public String getBudgetPaymentMethod(Integer budgetId) {
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
	
	public String getBudgetDispatchPlace(Integer budgetId) {
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
	
	public Integer getBudgetDeliveryTime(Integer budgetId) {
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
	
	public String getBudgetDeliveryPeriod(Integer budgetId) {
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
	
	public Boolean addBudgetBox(Integer selectedBudgetId, Integer boxSearchId, Integer boxQuantity) {
		int budgetBoxId = 0;
		if(this.budgetBoxExists(selectedBudgetId, boxSearchId)) {
			budgetBoxId = this.getBudgetBoxId(selectedBudgetId, boxSearchId);
			if(budgetBoxId > 0) {
				return this.increaseBudgetBox(budgetBoxId, boxQuantity);
			} else {
				return false;
			}
		} else {
			String queryString = "INSERT INTO budget_boxes (budget_container_id, box_id, quantity) "
								+ "VALUES (" + selectedBudgetId + ", " + boxSearchId + ", " + boxQuantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private Boolean increaseBudgetBox(Integer budgetBoxId, Integer boxQuantity) {
		String queryString = "UPDATE budget_boxes SET quantity = quantity + " + boxQuantity 
				+ " WHERE id = " + budgetBoxId;

		return this.update(queryString);
	}
	
	private Integer getBudgetBoxId(Integer selectedBudgetId, Integer boxSearchId) {
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
	
	private Boolean budgetBoxExists(Integer selectedBudgetId, Integer boxSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_boxes "
					+ "WHERE budget_boxes.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boxes.box_id = '" + boxSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean addBudgetSwitch(Integer selectedBudgetId, Integer switchSearchId, Integer switchQuantity) {
		int budgetSwitchId = 0;
		if(this.budgetSwitchExists(selectedBudgetId, switchSearchId)) {
			budgetSwitchId = this.getBudgetSwitchId(selectedBudgetId, switchSearchId);
			if(budgetSwitchId > 0) {
				return this.increaseBudgetSwitch(budgetSwitchId, switchQuantity);
			} else {
				return false;
			}
		} else {
			String queryString = "INSERT INTO budget_switches (budget_container_id, switch_id, quantity) "
								+ "VALUES (" + selectedBudgetId + ", " + switchSearchId + ", " + switchQuantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private Boolean increaseBudgetSwitch(Integer budgetSwitchId, Integer switchQuantity) {
		String queryString = "UPDATE budget_switches SET quantity = quantity + " + switchQuantity 
				+ " WHERE id = " + budgetSwitchId;

		return this.update(queryString);
	}
	
	private Integer getBudgetSwitchId(Integer selectedBudgetId, Integer switchSearchId) {
		String queryString;
		ResultSet setBudgetSwitchId;
		
		queryString = "SELECT budget_switches.id FROM budget_boxes "
					+ "WHERE budget_switches.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_switches.switch_id = '" + switchSearchId + "' "
					+ "LIMIT 1";
		
		setBudgetSwitchId = this.select(queryString);
		
		try {
			if (setBudgetSwitchId.next()) {
				return setBudgetSwitchId.getInt("budget_switches.id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private Boolean budgetSwitchExists(Integer selectedBudgetId, Integer switchSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_switches "
					+ "WHERE budget_switches.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_switches.switch_id = '" + switchSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public Boolean removeBudgetSwitch(Integer selectedBudgetSwitchId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_switches WHERE id = '" + selectedBudgetSwitchId + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean removeBudgetBox(Integer selectedBudgetBoxId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_boxes WHERE id = '" + selectedBudgetBoxId + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean removeBudgetBoard(Integer selectedBudgetBoardId) {
		String queryDelete;
		queryDelete = "DELETE FROM budget_boards WHERE id = '" + selectedBudgetBoardId + "'";
		return this.delete(queryDelete);
	}
	
	public Boolean addBudgetBoard(Integer selectedBudgetId, Integer boardSearchId, Integer boardQuantity) {
		int budgetBoardId = 0;
		if(this.budgetBoardExists(selectedBudgetId, boardSearchId)) {
			budgetBoardId = this.getBudgetBoardId(selectedBudgetId, boardSearchId);
			if(budgetBoardId > 0) {
				return this.increaseBudgetBoard(budgetBoardId, boardQuantity);
			} else {
				return false;
			}
		} else {
			String queryString = "INSERT INTO budget_boards (budget_container_id, board_id, quantity) "
								+ "VALUES (" + selectedBudgetId + ", " + boardSearchId + ", " + boardQuantity + ")";
			this.insert(queryString);
			return (this.getInsertId() > 0)? true:false;
		}
	}
	
	private Boolean increaseBudgetBoard(Integer budgetBoardId, Integer boardQuantity) {
		String queryString = "UPDATE budget_boards SET quantity = quantity + " + boardQuantity 
				+ " WHERE id = " + budgetBoardId;

		return this.update(queryString);
	}
	
	private Integer getBudgetBoardId(Integer selectedBudgetId, Integer boardSearchId) {
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
	
	private Boolean budgetBoardExists(Integer selectedBudgetId, Integer boardSearchId) {
		String queryString;
		
		queryString = "SELECT * FROM budget_boards "
					+ "WHERE budget_boards.budget_container_id = '" + selectedBudgetId + "' "
					+ "AND budget_boards.board_id = '" + boardSearchId + "' ";
		
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public String getBudgetClientCode(Integer budgetId) {
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
	
	public Integer getBudgetSellerId(Integer budgetId) {
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
	
	public String getBudgetNotes(Integer budgetId) {
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
	
	public ArrayList<Switch> getBudgetSwitches(Integer budgetId) {
		ArrayList<Switch> switches = new ArrayList<Switch>();
		ResultSet setBudgetSwitches = null;
		CallableStatement statement = null;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_SWITCHES(?)}");
			statement.setInt(1, budgetId);
			setBudgetSwitches = statement.executeQuery();
			
			while(setBudgetSwitches.next()) {
				Integer id = setBudgetSwitches.getInt("id");
				Integer brandId = setBudgetSwitches.getInt("brand_id");
				String brand = setBudgetSwitches.getString("brand");
				Integer typeId = setBudgetSwitches.getInt("type_id");
				String type = setBudgetSwitches.getString("type");
				String model = setBudgetSwitches.getString("model");
				Integer phases = setBudgetSwitches.getInt("phases");
				Integer currentId = setBudgetSwitches.getInt("current_id");
				String current = setBudgetSwitches.getString("current");
				Integer voltageId = setBudgetSwitches.getInt("voltage_id");
				String voltage = setBudgetSwitches.getString("voltage");
				Integer interruptionId = setBudgetSwitches.getInt("interruption_id");
				String interruption = setBudgetSwitches.getString("interruption");
				Double price = setBudgetSwitches.getDouble("price");
				Boolean active = (setBudgetSwitches.getInt("active")==1?true:false);
				Integer containerId = setBudgetSwitches.getInt("container_id");
				Integer quantity = setBudgetSwitches.getInt("quantity");
				switches.add(new Switch(id, brandId, brand, typeId, type, model, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los interruptores del presupuesto");
		}
		
		return switches;
	}
	
	public ArrayList<Switch> getBoardSwitches(Integer budgetId) {
		ArrayList<Switch> switches = new ArrayList<Switch>();
		ResultSet setBoardSwitches = null;
		CallableStatement statement = null;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BOARD_SWITCHES(?)}");
			statement.setInt(1, budgetId);
			setBoardSwitches = statement.executeQuery();
			
			while(setBoardSwitches.next()) {
				Integer id = setBoardSwitches.getInt("id");
				Integer brandId = setBoardSwitches.getInt("brand_id");
				String brand = setBoardSwitches.getString("brand");
				Integer typeId = setBoardSwitches.getInt("type_id");
				String type = setBoardSwitches.getString("type");
				String model = setBoardSwitches.getString("model");
				Integer phases = setBoardSwitches.getInt("phases");
				Integer currentId = setBoardSwitches.getInt("current_id");
				String current = setBoardSwitches.getString("current");
				Integer voltageId = setBoardSwitches.getInt("voltage_id");
				String voltage = setBoardSwitches.getString("voltage");
				Integer interruptionId = setBoardSwitches.getInt("interruption_id");
				String interruption = setBoardSwitches.getString("interruption");
				Double price = setBoardSwitches.getDouble("price");
				Boolean active = (setBoardSwitches.getInt("active")==1?true:false);
				Integer containerId = setBoardSwitches.getInt("container_id");
				Integer quantity = setBoardSwitches.getInt("quantity");
				switches.add(new Switch(id, brandId, brand, typeId, type, model, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener los interruptores del tablero");
		}
		
		return switches;
	}
	
	public ArrayList<Box> getBudgetBoxes(Integer budgetId) {
		ArrayList<Box> boxes = new ArrayList<Box>();
		ResultSet setBudgetBoxes;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_BOXES(?)}");
			statement.setInt(1, budgetId);
			setBudgetBoxes = statement.executeQuery();
			
			while(setBudgetBoxes.next()) {
				Integer id = setBudgetBoxes.getInt("id");
				Integer typeId = setBudgetBoxes.getInt("type_id");
				String type = setBudgetBoxes.getString("type");
				BoxType boxType = new BoxType(typeId, type);
				
				Integer installationId = setBudgetBoxes.getInt("installation_id");
				String strInstallation = setBudgetBoxes.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				Integer nemaId = setBudgetBoxes.getInt("nema_id");
				String strNema = setBudgetBoxes.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				String strPairs = setBudgetBoxes.getString("pairs");
				String strPairsNumeric = (strPairs.equals("N/A"))?"0":strPairs;
				Integer pairs = Integer.valueOf(strPairsNumeric);
				
				Integer sheetId = setBudgetBoxes.getInt("sheet_id");
				String strSheet = setBudgetBoxes.getString("sheet");
				Sheet sheet = new Sheet(sheetId, strSheet);
				
				Integer finishId = setBudgetBoxes.getInt("finish_id");
				String strFinish = setBudgetBoxes.getString("finish");
				Finish finish = new Finish(finishId, strFinish);
				
				Integer colorId = setBudgetBoxes.getInt("color_id");
				String strColor = setBudgetBoxes.getString("color");
				Color color = new Color(colorId, strColor);
				
				Integer height = setBudgetBoxes.getInt("height");
				Integer width = setBudgetBoxes.getInt("width");
				Integer depth = setBudgetBoxes.getInt("depth");
				Integer unitsId = setBudgetBoxes.getInt("units_id");
				String strUnits = setBudgetBoxes.getString("units");
				MeasureUnits units = new MeasureUnits(unitsId, strUnits);
				
				Integer caliberId = setBudgetBoxes.getInt("caliber_id");
				String strCaliber = setBudgetBoxes.getString("caliber");
				Integer intCaliber = Integer.valueOf(((strCaliber.equals("N/A"))?"0":strCaliber));
				Caliber caliber = new Caliber(caliberId, intCaliber);
				
				String caliberComments = setBudgetBoxes.getString("caliber_comments");
				Integer lockTypeId = setBudgetBoxes.getInt("lock_type_id");
				String strLockType = setBudgetBoxes.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				Double price = setBudgetBoxes.getDouble("price");
				String comments = setBudgetBoxes.getString("comments");
				Boolean active = (setBudgetBoxes.getInt("active")==1?true:false);
				boxes.add(new Box(id, boxType, installation, nema, pairs, sheet, finish, color, height, width, depth, units, caliber, caliberComments, lockType, price, comments, active));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al obtener las cajas del presupuesto");
		}
		
		return boxes;
	}
	
	public ArrayList<Board> getBudgetBoards(Integer budgetId) {
		ArrayList<Board> boards = new ArrayList<Board>();
		ResultSet setBudgetBoards;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_BUDGET_BOARDS(?)}");
			statement.setInt(1, budgetId);
			setBudgetBoards = statement.executeQuery();
			
			while(setBudgetBoards.next()) {
				Integer id = setBudgetBoards.getInt("id");
				String name = setBudgetBoards.getString("name");
				Integer boardTypeId = setBudgetBoards.getInt("type_id");
				String strBoardtype = setBudgetBoards.getString("type");
				BoardType boardType = new BoardType(boardTypeId, strBoardtype);
				
				Integer installationId = setBudgetBoards.getInt("installation_id");
				String strInstallation = setBudgetBoards.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				Integer nemaId = setBudgetBoards.getInt("nema_id");
				String strNema = setBudgetBoards.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				Integer barCapacityId = setBudgetBoards.getInt("bar_capacity_id");
				Integer strBarCapacity = setBudgetBoards.getInt("bar_capacity");
				BarCapacity barCapacity = new BarCapacity(barCapacityId, strBarCapacity);
				
				Integer barTypeId = setBudgetBoards.getInt("bar_type_id");
				String strBarType = setBudgetBoards.getString("nema");
				BarType barType = new BarType(barTypeId, strBarType);
				
				Integer circuitsId = setBudgetBoards.getInt("circuits_id");
				Integer strCircuits = setBudgetBoards.getInt("circuits");
				Circuits circuits = new Circuits(circuitsId, strCircuits);
				
				Integer boardVoltageId = setBudgetBoards.getInt("voltage_id");
				String strBoardVoltage = setBudgetBoards.getString("voltage");
				BoardVoltage boardVoltage = new BoardVoltage(boardVoltageId, strBoardVoltage);
				
				Integer phases = setBudgetBoards.getInt("phases");
				Boolean ground = setBudgetBoards.getBoolean("ground");
				
				Integer interruptionId = setBudgetBoards.getInt("interruption_id");
				Integer strInterruption = setBudgetBoards.getInt("interruption");
				Interruption interruption = new Interruption(interruptionId, strInterruption);
				
				Integer lockTypeId = setBudgetBoards.getInt("lock_type_id");
				String strLockType = setBudgetBoards.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				ArrayList<Integer> mainSwitchIds = this.getBoardSwitchMainIds(id);
				
				ArrayList<Material> materials = this.getBoardMaterials(id);
				
				Double price = setBudgetBoards.getDouble("price");
				String comments = setBudgetBoards.getString("comments");
				Boolean active = (setBudgetBoards.getInt("active")==1?true:false);
				
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
	
	public Integer cloneBudget(Integer selectedBudgetId) {
		Integer clonedBudgetId = 0;
		
		String date = this.getBudgetDate(selectedBudgetId);
		Integer expiryDays = this.getBudgetExpiryDays(selectedBudgetId);
		String clientId = String.valueOf(this.getBudgetClientId(selectedBudgetId));
		String workName = this.getBudgetWorkName(selectedBudgetId);
		String method = this.getBudgetPaymentMethod(selectedBudgetId);
		Integer sellerId = this.getBudgetSellerId(selectedBudgetId);
		String place = this.getBudgetDispatchPlace(selectedBudgetId);
		Integer deliveryTime = this.getBudgetDeliveryTime(selectedBudgetId);
		String deliveryPeriod = this.getBudgetDeliveryPeriod(selectedBudgetId);
		
		Boolean added = this.addBudget(date, expiryDays, clientId, workName, method, sellerId, place, deliveryTime, deliveryPeriod);
		
		if(added) {
			clonedBudgetId = this.getInsertId();
			
			this.cloneBudgetSwitches(selectedBudgetId, clonedBudgetId);
			this.cloneBudgetBoxes(selectedBudgetId, clonedBudgetId);
			this.cloneBudgetBoards(selectedBudgetId, clonedBudgetId);
			// TODO Clone budget materials and specials here
//			this.cloneBudgetMaterials(selectedBudgetId, clonedBudgetId);
//			this.cloneBudgetSpecials(selectedBudgetId, clonedBudgetId);
		}
		
		return clonedBudgetId;
	}
	
	private void cloneBudgetSwitches(Integer selectedBudgetId, Integer clonedBudgetId) {
		ResultSet setBudgetSwitches;
		ArrayList<Switch> switches = this.getBudgetSwitches(selectedBudgetId);
		ArrayList<Switch> clonedSwitches = (ArrayList<Switch>) switches.clone();
		Iterator<Switch> it = clonedSwitches.iterator();
		
		// TODO Iterate through all of them and change the budget container
		it.next();
	}

	private void cloneBudgetBoxes(Integer selectedBudgetId, Integer clonedBudgetId) {
		// TODO Auto-generated method stub
		
	}
	
	private void cloneBudgetBoards(Integer selectedBudgetId, Integer clonedBudgetId) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Budget> getBudgets(Integer stageId) {
		ArrayList<Budget> budgets = new ArrayList<Budget>();
		ResultSet setBudgets;
		
		setBudgets = this.select("SELECT * FROM budgets WHERE stage_id = " + stageId);
		
		try {
			while(setBudgets.next()) {
				Integer id = setBudgets.getInt("id");
				String code = setBudgets.getString("code");
				java.sql.Date date = setBudgets.getDate("date");
				Integer expiryDays = setBudgets.getInt("expiry_days");
				
				Integer clientId = setBudgets.getInt("client_id");
				Client client = new Client(this.getClientInfo(clientId));
				
				String workName = setBudgets.getString("work_name");
				
				Integer paymentMethodId = setBudgets.getInt("payment_method_id");
				PaymentMethod paymentMethod = new PaymentMethod(this.getPaymentMethodInfo(paymentMethodId));
				
				Integer sellerId = setBudgets.getInt("seller_id");
				User seller = new User(this.getSellerInfo(sellerId));
				
				Integer dispatchPlaceId = setBudgets.getInt("dispatch_place_id");
				DispatchPlace dispatchPlace = new DispatchPlace(this.getDispachPlaceInfo(dispatchPlaceId));
				
				Integer deliveryTime = setBudgets.getInt("delivery_time");
				
				Integer deliveryPeriodId = setBudgets.getInt("delivery_period_id");
				DeliveryPeriod deliveryPeriod = new DeliveryPeriod(this.getDeliveryPeriodInfo(deliveryPeriodId));
				
				Boolean tracing = (setBudgets.getInt("tracing")==1)?true:false;
				
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

	private BudgetStage getBudgetStageInfo(Integer stageId) {
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

	private DeliveryPeriod getDeliveryPeriodInfo(Integer deliveryPeriodId) {
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

	private DispatchPlace getDispachPlaceInfo(Integer dispatchPlaceId) {
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

	private User getSellerInfo(Integer sellerId) {
		User seller = null;
		ResultSet setSeller;
		String query = "SELECT *, UNIX_TIMESTAMP(date_created) as date_timestamp FROM users WHERE users.id = " + sellerId;
		
		setSeller = this.select(query);
		
		try {
			if (setSeller.next()) {
				String username = setSeller.getString("username");
				String passport = setSeller.getString("passport");
				String password = setSeller.getString("password");
				Integer userTypeId = setSeller.getInt("user_type_id");
				UserType userType = new UserType(this.getUserTypeInfo(userTypeId));
				String firstName = setSeller.getString("first_name");
				String lastName = setSeller.getString("last_name");
				String email = setSeller.getString("email");
				String phone = setSeller.getString("phone");
				Timestamp dateCreated = setSeller.getTimestamp("date_timestamp");
				Boolean status = (setSeller.getInt("status")==1)?true:false;
				seller = new User(sellerId, username, passport, password, userType, firstName, lastName, email, phone, dateCreated, status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seller;
	}

	private UserType getUserTypeInfo(Integer userTypeId) {
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

	private PaymentMethod getPaymentMethodInfo(Integer paymentMethodId) {
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

	private Client getClientInfo(Integer clientId) {
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
				Integer id = setUserInfo.getInt("id");
				String password = setUserInfo.getString("password");
				String passport = setUserInfo.getString("passport");
				Integer userTypeId = setUserInfo.getInt("user_type_id");
				String strUserType = setUserInfo.getString("type");
				UserType userType = new UserType(userTypeId, strUserType);
				String firstName = setUserInfo.getString("first_name");
				String lastName = setUserInfo.getString("last_name");
				String email = setUserInfo.getString("email");
				String phone = setUserInfo.getString("phone");
				Timestamp dateCreated = setUserInfo.getTimestamp("date_timestamp");
				Boolean status = (setUserInfo.getInt("status")==1?true:false);
				
				user = new User(id, username, password, passport, userType, firstName, lastName, email, phone, dateCreated, status);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public ProductionOrder pullProductionOrder(Integer orderId) {
		ProductionOrder productionOrder = new ProductionOrder();
		ResultSet setProductionOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_INFO(?)}");
			statement.setInt(1, orderId);
			setProductionOrder = statement.executeQuery();
			if (setProductionOrder.next()) {
				Integer id = setProductionOrder.getInt("id");
				Integer budgetId = setProductionOrder.getInt("budget_id");
				Timestamp timestampProcessed = setProductionOrder.getTimestamp("date_processed_ts");
				Timestamp timestampFinished = setProductionOrder.getTimestamp("date_finished_ts");
				Integer creatorId = setProductionOrder.getInt("creator_id");
				Integer authorizerId = setProductionOrder.getInt("authorizer_id");
				Boolean processed = (setProductionOrder.getInt("processed")==1?true:false);
				productionOrder = new ProductionOrder(id, budgetId, timestampProcessed, timestampFinished, creatorId, authorizerId, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return productionOrder;
	}
	
	public Boolean productionOrderExists(Integer budgetId) {
		String queryString;
		
		queryString = "SELECT id FROM production_orders WHERE budget_id = '" + budgetId + "'";
		this.select(queryString);
		
		return (this.getNumRows() > 0)? true:false;
	}
	
	public ProductionOrder pullProductionOrderByBudget(Integer budgetId) {
		ProductionOrder productionOrder = new ProductionOrder();
		ResultSet setProductionOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_INFO_BY_BUDGET(?)}");
			statement.setInt(1, budgetId);
			setProductionOrder = statement.executeQuery();
			if (setProductionOrder.next()) {
				Integer id = setProductionOrder.getInt("id");
				Timestamp timestampProcessed = setProductionOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setProductionOrder.getTimestamp("date_finished");
				Integer creatorId = setProductionOrder.getInt("creator_id");
				Integer authorizerId = setProductionOrder.getInt("authorizer_id");
				Boolean processed = (setProductionOrder.getInt("processed")==1?true:false);
				productionOrder = new ProductionOrder(id, budgetId, timestampProcessed, timestampFinished, creatorId, authorizerId, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return productionOrder;
	}

	public ArrayList<Product> getProductionOrderProducts(Integer productionOrderId) {
		ArrayList<Product> products = new ArrayList<Product>();
		ResultSet setProducts;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_SWITCHES(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				Integer id = setProducts.getInt("id");
				Integer brandId = setProducts.getInt("brand_id");
				String brand = setProducts.getString("brand");
				Integer typeId = setProducts.getInt("type_id");
				String type = setProducts.getString("type");
				String model = setProducts.getString("model");
				Integer phases = setProducts.getInt("phases");
				Integer currentId = setProducts.getInt("current_id");
				String current = setProducts.getString("current");
				Integer voltageId = setProducts.getInt("voltage_id");
				String voltage = setProducts.getString("voltage");
				Integer interruptionId = setProducts.getInt("interruption_id");
				String interruption = setProducts.getString("interruption");
				Double price = setProducts.getDouble("price");
				Boolean active = (setProducts.getInt("active")==1?true:false);
				Integer containerId = setProducts.getInt("container_id");
				Integer quantity = setProducts.getInt("quantity");
				products.add(new Switch(id, brandId, brand, typeId, type, model, phases, currentId, current, voltageId, voltage, interruptionId, interruption, price, active, containerId, quantity));
			}
			
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_BOXES(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				Integer id = setProducts.getInt("id");
				Integer typeId = setProducts.getInt("type_id");
				String type = setProducts.getString("type");
				BoxType boxType = new BoxType(typeId, type);
				
				Integer installationId = setProducts.getInt("installation_id");
				String strInstallation = setProducts.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				Integer nemaId = setProducts.getInt("nema_id");
				String strNema = setProducts.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				String strPairs = setProducts.getString("pairs");
				String strPairsNumeric = (strPairs.equals("N/A"))?"0":strPairs;
				Integer pairs = Integer.valueOf(strPairsNumeric);
				
				Integer sheetId = setProducts.getInt("sheet_id");
				String strSheet = setProducts.getString("sheet");
				Sheet sheet = new Sheet(sheetId, strSheet);
				
				Integer finishId = setProducts.getInt("finish_id");
				String strFinish = setProducts.getString("finish");
				Finish finish = new Finish(finishId, strFinish);
				
				Integer colorId = setProducts.getInt("color_id");
				String strColor = setProducts.getString("color");
				Color color = new Color(colorId, strColor);
				
				Integer height = setProducts.getInt("height");
				Integer width = setProducts.getInt("width");
				Integer depth = setProducts.getInt("depth");
				Integer unitsId = setProducts.getInt("units_id");
				String strUnits = setProducts.getString("units");
				MeasureUnits units = new MeasureUnits(unitsId, strUnits);
				
				Integer caliberId = setProducts.getInt("caliber_id");
				String strCaliber = setProducts.getString("caliber");
				Integer intCaliber = Integer.valueOf(((strCaliber.equals("N/A"))?"0":strCaliber));
				Caliber caliber = new Caliber(caliberId, intCaliber);
				
				String caliberComments = setProducts.getString("caliber_comments");
				Integer lockTypeId = setProducts.getInt("lock_type_id");
				String strLockType = setProducts.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				Double price = setProducts.getDouble("price");
				String comments = setProducts.getString("comments");
				Boolean active = (setProducts.getInt("active")==1?true:false);
				products.add(new Box(id, boxType, installation, nema, pairs, sheet, finish, color, height, width, depth, units, caliber, caliberComments, lockType, price, comments, active));
			}
			
			statement = this.conn.prepareCall("{call SP_GET_PRODUCTION_ORDER_BOARDS(?)}");
			statement.setInt(1, productionOrderId);
			setProducts = statement.executeQuery();
			while (setProducts.next()) {
				Integer id = setProducts.getInt("id");
				String name = setProducts.getString("name");
				Integer boardTypeId = setProducts.getInt("type_id");
				String strBoardtype = setProducts.getString("type");
				BoardType boardType = new BoardType(boardTypeId, strBoardtype);
				
				Integer installationId = setProducts.getInt("installation_id");
				String strInstallation = setProducts.getString("installation");
				Installation installation = new Installation(installationId, strInstallation);
				
				Integer nemaId = setProducts.getInt("nema_id");
				String strNema = setProducts.getString("nema");
				Nema nema = new Nema(nemaId, strNema);
				
				Integer barCapacityId = setProducts.getInt("bar_capacity_id");
				Integer strBarCapacity = setProducts.getInt("bar_capacity");
				BarCapacity barCapacity = new BarCapacity(barCapacityId, strBarCapacity);
				
				Integer barTypeId = setProducts.getInt("bar_type_id");
				String strBarType = setProducts.getString("nema");
				BarType barType = new BarType(barTypeId, strBarType);
				
				Integer circuitsId = setProducts.getInt("circuits_id");
				Integer strCircuits = setProducts.getInt("circuits");
				Circuits circuits = new Circuits(circuitsId, strCircuits);
				
				Integer boardVoltageId = setProducts.getInt("voltage_id");
				String strBoardVoltage = setProducts.getString("voltage");
				BoardVoltage boardVoltage = new BoardVoltage(boardVoltageId, strBoardVoltage);
				
				Integer phases = setProducts.getInt("phases");
				Boolean ground = setProducts.getBoolean("ground");
				
				Integer interruptionId = setProducts.getInt("interruption_id");
				Integer strInterruption = setProducts.getInt("interruption");
				Interruption interruption = new Interruption(interruptionId, strInterruption);
				
				Integer lockTypeId = setProducts.getInt("lock_type_id");
				String strLockType = setProducts.getString("lock_type");
				LockType lockType = new LockType(lockTypeId, strLockType);
				
				ArrayList<Integer> mainSwitchIds = this.getBoardSwitchMainIds(id);
				
				ArrayList<Material> materials = this.getBoardMaterials(id);
				
				Double price = setProducts.getDouble("price");
				String comments = setProducts.getString("comments");
				Boolean active = (setProducts.getInt("active")==1?true:false);
				
				ArrayList<Switch> switches = new ArrayList<Switch>();
				switches = this.getBoardSwitches(id);
				products.add(new Board(id, name, boardType, installation, nema, barCapacity, barType, circuits, boardVoltage, phases, ground, interruption, lockType, mainSwitchIds, materials, price, comments, active, switches));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	public WorkOrder pullWorkOrder(Integer orderId) {
		WorkOrder workOrder = new WorkOrder();
		ResultSet setWorkOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_WORK_ORDER_INFO_BY_ID(?)}");
			statement.setInt(1, orderId);
			setWorkOrder = statement.executeQuery();
			if (setWorkOrder.next()) {
				Integer id = setWorkOrder.getInt("id");
				Integer productionOrderId = setWorkOrder.getInt("production_order_id");
				Integer productId = setWorkOrder.getInt("product_id");
				Integer productTypeId = setWorkOrder.getInt("product_type_id");
				Integer creatorId = setWorkOrder.getInt("creator_id");
				Integer authorizerId = setWorkOrder.getInt("authorizer_id");
				Timestamp timestampProcessed = setWorkOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setWorkOrder.getTimestamp("date_finished");
				Boolean processed = (setWorkOrder.getInt("processed")==1?true:false);
				workOrder = new WorkOrder(id, productionOrderId, productId, productTypeId, creatorId, authorizerId, timestampProcessed, timestampFinished, processed);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return workOrder;
	}
	
	public ArrayList<WorkOrder> pullAllWorkOrders(Integer productionOrderId) {
		ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();
		ResultSet setWorkOrder;
		CallableStatement statement;
		
		try {
			statement = this.conn.prepareCall("{call SP_GET_WORK_ORDER_INFO_BY_PRODUCTION_ORDER_ID(?)}");
			statement.setInt(1, productionOrderId);
			setWorkOrder = statement.executeQuery();
			while (setWorkOrder.next()) {
				Integer id = setWorkOrder.getInt("id");
				Integer productId = setWorkOrder.getInt("product_id");
				Integer productTypeId = setWorkOrder.getInt("product_type_id");
				Integer creatorId = setWorkOrder.getInt("creator_id");
				Integer authorizerId = setWorkOrder.getInt("authorizer_id");
				Timestamp timestampProcessed = setWorkOrder.getTimestamp("date_processed");
				Timestamp timestampFinished = setWorkOrder.getTimestamp("date_finished");
				Boolean processed = (setWorkOrder.getInt("processed")==1?true:false);
				workOrders.add(new WorkOrder(id, productionOrderId, productId, productTypeId, creatorId, authorizerId, timestampProcessed, timestampFinished, processed));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return workOrders;
	}
	
	public Boolean setProductionOrderProcessed(Integer orderId) {
		return this.update("UPDATE production_orders SET processed = 1 WHERE id = " + orderId);
	}
	
	public Boolean addBoardMainSwitch(Integer switchId, int boardContainerId) {
		return this.update("UPDATE board_switches SET main = 1 WHERE id = " + switchId);
	}
	
	public Boolean removeBoardMainSwitch(Integer switchId, int boardContainerId) {
		return this.update("UPDATE board_switches SET main = 0 WHERE id = " + switchId);
	}
	
}
