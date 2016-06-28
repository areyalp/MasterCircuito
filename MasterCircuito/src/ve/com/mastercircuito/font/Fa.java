/*This is how this should be used
 * 
 * Font fa = null;
		
		try {
			URL url = getClass().getResource("fontawesome-webfont.ttf");
			InputStream is = url.openStream();
			fa = Font.createFont(Font.TRUETYPE_FONT, is);
			fa = fa.deriveFont(Font.PLAIN, 36f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
*
*
*/

package ve.com.mastercircuito.font;

import java.awt.Font;

public abstract class Fa extends Font{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1735794833972384448L;

	public static final String fa_500px = "\uf26e";
	public static final String fa_adjust = "\uf042";
	public static final String fa_adn = "\uf170";
	public static final String fa_align_center = "\uf037";
	public static final String fa_align_justify = "\uf039";
	public static final String fa_align_left = "\uf036";
	public static final String fa_align_right = "\uf038";
	public static final String fa_amazon = "\uf270";
	public static final String fa_ambulance = "\uf0f9";
	public static final String fa_anchor = "\uf13d";
	public static final String fa_android = "\uf17b";
	public static final String fa_angellist = "\uf209";
	public static final String fa_angle_double_down = "\uf103";
	public static final String fa_angle_double_left = "\uf100";
	public static final String fa_angle_double_right = "\uf101";
	public static final String fa_angle_double_up = "\uf102";
	public static final String fa_angle_down = "\uf107";
	public static final String fa_angle_left = "\uf104";
	public static final String fa_angle_right = "\uf105";
	public static final String fa_angle_up = "\uf106";
	public static final String fa_apple = "\uf179";
	public static final String fa_archive = "\uf187";
	public static final String fa_area_chart = "\uf1fe";
	public static final String fa_arrow_circle_down = "\uf0ab";
	public static final String fa_arrow_circle_left = "\uf0a8";
	public static final String fa_arrow_circle_o_down = "\uf01a";
	public static final String fa_arrow_circle_o_left = "\uf190";
	public static final String fa_arrow_circle_o_right = "\uf18e";
	public static final String fa_arrow_circle_o_up = "\uf01b";
	public static final String fa_arrow_circle_right = "\uf0a9";
	public static final String fa_arrow_circle_up = "\uf0aa";
	public static final String fa_arrow_down = "\uf063";
	public static final String fa_arrow_left = "\uf060";
	public static final String fa_arrow_right = "\uf061";
	public static final String fa_arrow_up = "\uf062";
	public static final String fa_arrows = "\uf047";
	public static final String fa_arrows_alt = "\uf0b2";
	public static final String fa_arrows_h = "\uf07e";
	public static final String fa_arrows_v = "\uf07d";
	public static final String fa_asterisk = "\uf069";
	public static final String fa_at = "\uf1fa";
	public static final String fa_automobile = "\uf1b9";
	public static final String fa_backward = "\uf04a";
	public static final String fa_balance_scale = "\uf24e";
	
	
	public static final String fa_caret_left = "\uf0d9";
	public static final String fa_caret_right = "\uf0da";
	public static final String fa_pencil_square_o = "\uf044";
	public static final String fa_plus = "\uf067";
	public static final String fa_plus_circle = "\uf055";
	public static final String fa_plus_square_o = "\uf196";
	public static final String fa_remove = "\uf00d";
	public static final String fa_search = "\uf002";
	public static final String fa_step_backward = "\uf048";
	public static final String fa_step_forward = "\uf051";
	
	protected Fa(Font font) {
		super(font);
		// TODO Auto-generated constructor stub
	}

}
