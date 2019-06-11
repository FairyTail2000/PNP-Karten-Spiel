package main;

import java.util.ArrayList;
import java.util.List;

import jamlParser.main.JAMLParser;

public class Attacke {
	private boolean attack_makes_damage = true, suicide_attack = false;
	private Würfel würfel;
	private List<Typ> typen = new ArrayList<Typ>(3);
	private String name;
	private String beschreibung;
	private int count = 10;
	
	/**
	 * I override the default Constructor because i don't want empty Objects
	 * DON'T EVER USE THIS IT WILL THROW MORE NULLPOINTERS THAN YOU CAN IMAGINE
	 * @throws UnsupportedOperationException
	 */
	public Attacke () throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This Constructor should not be used");
	}
	
	/**
	 * Main Constructor, Constructs the Attacke from a JAML parser
	 * @param p The Parser holding the Attack definition
	 */
	public Attacke(JAMLParser p) {
		this.name = p.getValue("Titel");
		for (String s : p.getValue("Typen").split(",")) {
			this.typen.add(Typen.getByName(s));
		}
		try {
			this.würfel = new Würfel(p.getValue("Schaden"));
		} catch (NumberFormatException e) {
			this.würfel = null;
			this.attack_makes_damage = false;
		}
		this.beschreibung = p.getValue("Beschreibung");
	}
	
	/**
	 * Computes the damage this Attack makes
	 * @return The damage
	 */
	public int doAttack () {
		if (this.attack_makes_damage && this.count > 0) {
			this.count--;
			return this.würfel.würfeln();
		}
		return 0;
	}
	
	/**
	 * Returns the Name of this Attack
	 * @return The Name
	 */
	public String getName () {
		String name = this.name;
		return name;
	}
	
	/**
	 * Gives you the Description of this Attack
	 * @return This description
	 */
	public String getDescription () {
		return this.beschreibung;
	}
	
	/**
	 * Gives you the Würfel object this Attack has
	 * @return A Würfel object
	 */
	public Würfel getWürfel () {
		return this.würfel;
	}
	
	/**
	 * Gives you a List of all Typs this Attack has
	 * @return The List
	 */
	public List<Typ> getTypen () {
		return this.typen;
	}
	
	/**
	 * Does this Attack make Damage?
	 * @return Damage jes or no
	 */
	public boolean doesDamage () {
		return this.attack_makes_damage;
	}
	
	/**
	 * Does the Monster kill itself with this Attack?
	 * @return suicide
	 */
	public boolean isSuicide () {
		return this.suicide_attack;
	}
	
	/**
	 * I need to keep track of how many times Attack can be used, when its zero, it cannot be used anymore
	 * @return How often this attack could be used
	 */
	public int wie_oft_kann_diese_attacke_noch_eingesetzt_werden () {
		return count;
	}
	
	/**
	 * An overriden toString Method, for debugging
	 */
	@Override
	public String toString() {
		String able = ""; 
		
		able += "Name: " + this.name + "\n";
		able += "Suizid: " + this.suicide_attack + "\n";
		able += "Attacke macht Schaden: " + this.attack_makes_damage + "\n";
		able += "Typen:\n";
		for (Typ p : this.typen) {
			able += "\t" + p.getName() + "\n";
		}
		able += "Würfel: " + würfel + "\n";
		able += "Verbleindene Einsätze: " + count + "\n\n\n";
		
		return able;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Attacke) {
			Attacke a = (Attacke) obj;
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
