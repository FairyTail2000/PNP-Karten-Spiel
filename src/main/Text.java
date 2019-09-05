package main;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text extends javafx.scene.text.Text {
	private Attacke att;
	/**
	 * Convenience Constructor
	 * 
	 * @param id: The id of this card, so we can find later with lookup(# + id), usually the Name of the Monster
	 * @param text: Well the Text which should be in the Text object
	 */
	public Text (String id, String text) {
		super(placeNewline(text));
		this.setId(id);
		this.setWrappingWidth(0);
		this.setFill(Color.WHITE);
	}
	
	public Text (String id, String text, Attacke this_atk) {
		super(placeNewline(text));
		this.setId(id);
		this.setWrappingWidth(0);
		this.att = this_atk;
	}
	
	
	/**
	 * Convenience Constructor
	 * 
	 * @param text: Well the Text which should be in the Text object
	 * @param FontSize: The size of the Font
	 * @param FontColor: The Color of the text
	 * @param id: The ID of the Object to find it with lookop()
	 */
	public Text (int FontSize, Color FontColor, String text, String id) {
		super(placeNewline(text));
		this.setFont(Font.font(FontSize));
		this.setFill(FontColor);
		this.setWrappingWidth(0);
		this.setId(id);
	}
	
	public Text (int FontSize, Color FontColor, String text) {
		super(placeNewline(text));
		this.setFont(Font.font(FontSize));
		this.setFill(FontColor);
		this.setWrappingWidth(0);
	}
	/**
	 * Convenience Constructor
	 * 
	 * @param text: Well the Text which should be in the Text object
	 * @param FontSize: The size of the Font
	 * @param FontColor: The Color of the text
	 */
	public Text (int FontSize, Color FontColor, String text, Cursor cursor) {
		super(placeNewline(text));
		this.setFont(Font.font(FontSize));
		this.setFill(FontColor);
		this.setCursor(cursor);
		this.setWrappingWidth(0);
	}
	
	/**
	 * Default Constructor, it does set the WrappingWidth to zero, so the Text should fit itself inside of the available space
	 */
	public Text () {
		super();
		this.setWrappingWidth(0);
		this.setFill(Color.WHITE);
	}
	
	/**
	 * This Method places a newline in the String so it fits the Screen
	 * 
	 * @return The modified inputstring
	 */
	public static String placeNewline (String target) {
		//A variable for working with String
		String s = target;
		try {
			//It is not neccesarry to modifie this String
			if (s.length() <= 100) {
				return s;
			} else if (s.length() >= 152) {
				if (s.charAt(150) == ' ') {
					return s.substring(0, 150) + "\n" + s.substring(151, s.length() - 1);
				} else if (s.length() > 150 && s.charAt(151) == ' ') {
					return s.substring(0, 151) + "\n" + s.substring(152, s.length() - 1);
				} else {
					return s.substring(0, 150) + "-\n" + s.substring(151, s.length() - 1);
				}
				
			}
		} catch (Exception e) {
			//We will ignore this
		}
		
		return s; // To shut up eclipse
	}

	public Attacke getAtt() {
		return att;
	}
	
}
