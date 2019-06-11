package main;

import java.util.Random;

public class Würfel {
	
	private int anzahl, augen, second_anzahl, second_augen;
	private boolean multi = false;
	//A central private Way to get pseudo random Number
	private static Random r = new Random(System.currentTimeMillis());
	
	/**
	 * Just dont use this...
	 * @throws UnsupportedOperationException
	 */
	public Würfel () throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * The main constructor
	 * @param The String which should get parsed into a dice, it must have the following format: (Number)W(Number) or (Number)W(Number)+(Number)W(Number))
	 */
	public Würfel (String w) {
		String[] split = null;
		String[] arr;
		//There are two Würfel when there is a plus
		if (w.contains("+")) {
			arr = new String[4];
			multi = true;
			//Split it by "+"
			split = w.split("\\+");
			//Then parse the second part, the first will be parsed later
			String[] part_two = split[1].split("W");
			//This is the first, it gets ignored, until the try/catch statement 
			arr = split[0].split("W");
			second_anzahl = Integer.parseInt(part_two[0]);
			try {
				second_augen = Integer.parseInt(part_two[1]);
			} catch (IndexOutOfBoundsException e) {
				//Debug purposes
				System.out.println("Input: " + w + "\nSplit: " + split[0] + "," + split[1] + "\nPart_two: " + part_two + "\narr: " + arr);
			}
		} else {
			arr = w.split("W");
		}
		this.anzahl = Integer.parseInt(arr[0]);
		this.augen = Integer.parseInt(arr[1]);
	}
	
	//A more direct convenience constructor
	public Würfel (int anzahl, int augen) {
		this.anzahl = anzahl;
		this.augen = augen;
	}
	
	/**
	 * Here the dice gets pseudo rolled
	 * 
	 * @return The result of the dice(s)
	 */
	public int würfeln () {
		//A variable to store the results
		int summe = 0;
		if (multi) {
			//Don't know why i did != but well, i think its doing his job :)
			for (int i = 0; i != second_anzahl; i++) {
				//
				summe += r.nextInt(second_augen);
			}
		}
		for (int i = 0; i != anzahl; i++) {
			summe += r.nextInt(augen);
		}
		return summe;
	}
	
	/**
	 * Well i just for the case I want the String representation of this Dice
	 * @return The String representation of the dice
	 */
	@Override
	public String toString () {
		if (multi) {
			return anzahl + "W" + augen + "+" + second_anzahl + "W" + second_augen;
		}
		return anzahl + "W" + augen;
	}
	
}
