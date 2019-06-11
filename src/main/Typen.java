package main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jamlParser.main.JAMLParser;

public class Typen {
	
	/**
	 * The List were we store all the Typs
	 */
	private static List<Typ> alle_Typen = new ArrayList<>();
	
	/**
	 * Get the Typ by name so we can use it in our Attacks and Monsters
	 * @param The name of the Typ
	 * @return The Typ, if found, <code>null</code> if not
	 */
	public static Typ getByName (String name) {
		if (name == null) {
			return null;
		}
		//Iterate through the list and search for the correct Typ
		for (Typ t : alle_Typen) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Does this Typ deals double Damage to this Typ?
	 * @param One Typ
	 * @param Another Typ
	 * @return true when Typ one deals double Damage to Typ two
	 */
	public static boolean isDoubleDamage (Typ one, Typ two) {
		List<String> two_list = two.getDoubleDamage();
		String name = one.getName();
		
		for (String t : two_list) {
			if (t.equals(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Does this Typ deals half Damage to this Typ?
	 * @param One Typ
	 * @param Another Typ
	 * @return true when Typ one deals half Damage to Typ two
	 */
	public static boolean isHalfDamage (Typ one, Typ two) {
		List<String> two_list = two.getHalfDamage();
		String name = one.getName();
		
		for (String t : two_list) {
			if (t.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Does this Typ deals no Damage to this Typ?
	 * @param One Typ
	 * @param Another Typ
	 * @return true when Typ one deals no Damage to Typ two
	 */
	public static boolean isNoDamage (Typ one, Typ two) {
		List<String> two_list  = two.getNoDamage();
		String name = one.getName();
		
		for (String t : two_list) {
			if (t.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * This function just sets everything up
	 * Its some black magic happening here so, just go to the next function ;)
	 */
	public static void setup () {
		File folder = new File("Typen");
		if (folder.exists() && folder.isDirectory()) {
			List<String> double_damage;
			List<String> half_damage;
			List<String> no_damage;
			
			for (File f : folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".jaml")) {
						return true;
					}
					return false;
				}})) {
				JAMLParser p = new JAMLParser(f);
				Map<String, String> map = p.getMap();
				double_damage = Arrays.asList(map.get("double_damage").split(","));
				half_damage = Arrays.asList(map.get("half_damage").split(","));
				no_damage = Arrays.asList(map.get("no_damage").split(","));
				alle_Typen.add(new Typ(f.getName().replace(".jaml", ""), double_damage, half_damage, no_damage));
			}
		} else {
			folder.delete();
			folder.mkdir();
		}
		
	}
	
}
