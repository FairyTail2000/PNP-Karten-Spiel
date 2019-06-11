package main;

import java.util.ArrayList;
import java.util.List;

public class Attacken {
	//I don't want to comment the Main class...
	//http://vstein.pythonanywhere.com/wiki/monster_attacken
	//A List were I save all the Attacks for later use
	private static List<Attacke> attacken = new ArrayList<Attacke>();
	
	/**
	 * Add an Attack to the List of available Attacks
	 * @param attack to add to the List
	 */
	public static void addAttack (Attacke attack) {
		attacken.add(attack);
	}
	
	/**
	 * When I need to do very much with this List, like filtering and sorting, I need the complete List
	 * @return the current List of Attacks
	 */
	public static List<Attacke> getAttacks () {
		return attacken;
	}
	
	/**
	 * Here I can get an Instance of an Attack with the name
	 * @param name of the Attack
	 * @return An Instance of the Attack with <code>name</code> or null if not found
	 */
	public static Attacke getAttacke (String name) {
		for (Attacke a : attacken) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
}
