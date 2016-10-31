package ve.com.mastercircuito.test;

public class Description {

	private Integer quantity;
	private String nro;
	private String name;
	private String type;
	private String price;
	private String total_price;
	
	public Description() {
		
	}
	
	public Description(String string, String string2, String string3, String string4, String string5) {
		// TODO Auto-generated constructor stub
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	

	
	
//	 budget_boards.quantity,
//	 CONCAT('Tablero ',boards.`name`,', Tipo ',board_types.type) AS description,
//	 boards.price,
//	 (budget_boards.quantity * boards.price) AS total_price
//	FROM budgets
//	LEFT JOIN budget_boards
//	 ON budget_boards.budget_container_id = budgets.id
//	LEFT JOIN boards
//	 ON boards.id = budget_boards.board_id
//	LEFT JOIN board_types
//	 ON board_types.id = boards.type_id
//	WHERE budgets.id = 1


}
