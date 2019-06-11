package main;

import java.util.ArrayList;
import java.util.List;

public class Typ {
	//Ist der Angriff bei + doppelt stark, bei - nur halb und bei X wirkungslos
	
	private String name;
	private List<String> double_damage = new ArrayList<>(), half_damage = new ArrayList<>(), no_damage = new ArrayList<>();
	
	/**
	 * Don't use this constructor
	 */
	private Typ () throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Just a for debug purporses!!!!");
	}
	
	/**
	 * 
	 * The default Constructor
	 * 
	 * @param Name: The Name of this Typ
	 * @param double_damage: A list where this Typ deals double Damage
	 * @param half_damage: A list where this Typ deals half Damage
	 * @param no_damage: A list where this Typ deals half Damage
	 */
	public Typ (String Name, List<String> double_damage, List<String> half_damage,List<String> no_damage) {
		this.name = Name;
		this.double_damage = double_damage;
		this.half_damage = half_damage;
		this.no_damage = no_damage;
	}
	
	/**
	 * Here you can the Name of this Typ
	 * @return The name of this Typ
	 */
	public String getName () {
		return this.name;
	}
	
	/**
	 * @return A list which contains every Typ where this Typ deals double damage
	 */
	public List<String> getDoubleDamage () {
		return double_damage;
	}
	
	/**
	 * @return A list which contains every Typ where this Typ deals half damage
	 */
	public List<String> getHalfDamage () {
		return half_damage;
	}
	
	/**
	 * @return A list which contains every Typ where this Typ deals no damage
	 */
	public List<String> getNoDamage () {
		return no_damage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Typ) {
			Typ a = (Typ) obj;
			if (a.getName().equals(this.name)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}