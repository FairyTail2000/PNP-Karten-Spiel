package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import jamlParser.main.JAMLParser;

public class Monster extends JAMLParser {
	
	private boolean active = false, i_am_the_enemy = false;
	private int live, level;
	private List<Attacke> attacken = new ArrayList<Attacke>();
	private List<Typ> typen = new ArrayList<Typ>();
	
	/**
	 * Main Contructor
	 * @param target
	 */
	
	public Monster(File target) {
		super(target);
		setup();
	}
	
	/**
	 * Alternative Main Constructor
	 * @param target
	 */
	public Monster(List<String> target) {
		super(target);
		setup();
	}

	/**
	 * Convenience Method to get the Name
	 * @return The Name
	 */
	public String getName() {
		return this.getValue("Name");
	}
	
	/**
	 * A setup Method, should only be used when the class gets instantiated
	 */
	private void setup () {
		this.level = Integer.parseInt(this.getValue("Rang_zahl"));
		this.live = Integer.parseInt(this.getValue("HP"));
		String[] att = this.getValue("Attacken").split(",");
		for (int i = 0; i < att.length; i++) {
			att[i] = att[i].substring(0, att[i].indexOf("(") - 1).replace(" ", "");
		}
		for (String a : att) {
			try {
				Attacke at = Attacken.getAttacke(a);
				this.attacken.add(at);
			
			} catch (NoSuchElementException e) {
				System.err.println("Attacke nicht gefunden: " + a + " vom Monster: " + this.getName());
			}
		}
		
		for (String s : this.getValue("Typen").split(",")) {
			Typ t = Typen.getByName(s.replace(" ", ""));
			this.typen.add(t);
		}
	}
	
	/**
	 * 
	 * @return A List of Attacks this Monster have
	 */
	public List<Attacke> getAttacken () {
		//Workaround because I'm getting a List filled with null objects when I'm returning it right away
		return attacken;
	}
	
	/**
	 * I need to calculate the Damage which has a depency on the Rang
	 * @return
	 */
	public int getRangAsInt () {
		return this.level;
	}
	
	/**
	 * 
	 * @return The current HP of the Monster
	 */
	public int getHPasInt () {
		return this.live;
	}
	
	/**
	 * 
	 * @return The Current HP as a String
	 */
	public String getHP () {
		return this.getValue("HP");
	}
	
	/**
	 * Set the HP of the Monster
	 * @param hp
	 */
	public void setHPasInt (int hp) {
		this.live = hp;
		this.getMap().replace("HP", String.valueOf(hp));
	}
	
	/**
	 * Set the HP as a String
	 * @param hp
	 */
	public void setHP (String hp) {
		this.live = Integer.parseInt(hp);
		this.getMap().replace("HP", hp);
	}
	
	
	@Override
	public String toString() {
		String return_able = "";
		return_able += "Name: " + this.getName() + "\n";
		return_able += "HP: " + this.getHP() + "\n";
		return_able += "Attacken:\n";
		
		for (Attacke a : this.attacken) {
			try {
				return_able += "\t" + a.getName() + ",\n";
			} catch (Exception e) {
			}
		}
		
		String one_ore_more = "Typ";
		
		if (this.typen.size() <= 1) {
			one_ore_more += "en";
		}
		
		return_able += one_ore_more + ":\n";
		
		for (Typ t : typen) {
			return_able += "\t" + t.getName() + ",\n";
		}
		
		return_able += "Rang (as int): " + this.level + ", Rang " + this.getValue("Rang") + "\n";
		return return_able;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Monster) {
			Monster a = (Monster) obj;
			if (a.getName().equals(this.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return Is this Monster an Enemy
	 */
	public boolean am_I_the_Enemy() {
		return i_am_the_enemy;
	}

	/**
	 * Set if this Monster is an Enemy
	 * @param am_i_the_enemy
	 */
	public void setI_am_the_enemy(boolean am_i_the_enemy) {
		this.i_am_the_enemy = am_i_the_enemy;
	}
	
}
