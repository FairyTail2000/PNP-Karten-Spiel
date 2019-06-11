package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class VBox extends javafx.scene.layout.VBox {

	//To track if this Card is active
	private boolean active = false;
	//The reference to the Monster this Card represents
	private Monster monster = null;
	//If there is a column index I use it to put it back in place
	private int column_index;
	
	private boolean shows_attacks = false;
	
	/*
	 * Default Constructor
	 */
	public VBox () {
		super();
	}
	
	/*
	 * @param A Monster were Values can get read or written
	 */
	public VBox (Monster m) {
		super();
		//storing the Monster for this Node, we will need it later
		this.monster = m;
		setup();
		Color white = Color.WHITE;
		this.getChildren().addAll(new Text(15, white, m.getValue("HP"), "EnemyMonsterHP"), new Text(15, white, m.getValue("Name"), "EnemyMonsterName"), new Text(15, white, m.getValue("Rang"), "EnemyMonsterRang"), new Text(15, white, m.getValue("Typen"), "EnemyMonsterTypes"), new Text(15, white, m.getValue("Größe") + "m", "EnemyMonsterSize"), new Text(15, white, m.getValue("Gewicht") + "kg", "EnemyMonsterWeight"));
	}
	
	/*
	 * @param A Monster where Values can get read or written
	 */
	public VBox(Monster m, int i) {
		super();
		//storing the Monster for this Node, we will need it later
		this.monster = m;
		//We need it to know where it was in the Grid
		this.column_index = i;
		setup();
		Color white = Color.WHITE;
		this.getChildren().addAll(new Text(15, white, m.getValue("HP"), "EnemyMonsterHP"), new Text(15, white, m.getValue("Name"), "EnemyMonsterName"), new Text(15, white, m.getValue("Rang"), "EnemyMonsterRang"), new Text(15, white, m.getValue("Typen"), "EnemyMonsterTypes"), new Text(15, white, m.getValue("Größe") + "m", "EnemyMonsterSize"), new Text(15, white, m.getValue("Gewicht") + "kg", "EnemyMonsterWeight"));
		
	}

	
	/*
	 * This Activates the current card
	 */
	public void setActive () {
		//set the node active, will be needed to determined if it is active or not
		active = true;
		//Get the Bottom Grid 
		GridPane bottom_grid = (GridPane) Main.getLayoutPane().lookup("#Bottom");
		if (bottom_grid == null) {
			//We really have a problem because we need this to proceed
			System.err.println("Huston, we have a Problem");
			
		} else {
			//Well i dont know how pass a reference to this object to an abstract Method or whatever this is called
			VBox v = this;
			//I need to it because JavaFX keeps complaining about modifying from non FX thread (WTF?)
			Task<Void> lat = new Task<Void>() {
				
				@Override
				protected Void call() throws Exception {
					ObservableList<Node> list = bottom_grid.getChildren();
					//Well this should be self explanotory, but i will explain it
					//I iterate through every Member of the grid, find this card and remove it from the grid to readd the card to 
					for (Node x : list) {
						if (getId() == x.getId()) {
							if (list.remove(x)) {
								bottom_grid.add(v, 3, 1);
								bottom_grid.setAlignment(Pos.CENTER);
							} else {
								System.err.println("Well... \nWhat now?");
							}
							
						}
					}
					
					//Why return something? I dont want to know if it was successful or not 
					return null;
				}
			};
			lat.run();
		}
		//Set the style, obviously setStyle overrides every existent rule
		this.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
		        + "-fx-border-radius: 5; -fx-border-color: blue; -fx-border-radius: 10 10 10 10;");
		this.applyCss();
	}
	
	/*
	 * This deactivates the current card
	 */
	public void setInActive () {
		active = false;
		//Its the same as setActive but now we normalize it and restore the default state
		GridPane bottom_grid = (GridPane) Main.getLayoutPane().lookup("#Bottom");
		if (bottom_grid == null) {
			System.err.println("Huston, we have a Problem");
		} else {
			VBox v = this;
			Task<Void> lat = new Task<Void>() {
				
				@Override
				protected Void call() throws Exception {
					ObservableList<Node> list = bottom_grid.getChildren();
					Iterator<Node> it = list.iterator();
					while (it.hasNext()) {
						Node x = it.next();
						if (getId() == x.getId()) {
							if (list.remove(x)) {
								bottom_grid.add(v, column_index, 2);
							} else {
								System.err.println("Well... \nWhat now?");
							}
							
						}
					}
					return null;
				}
			};
			lat.run();
		}
		//Set the style, obviously setStyle overrides every existent rule
		this.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
		        + "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 10 10;");
		this.applyCss();
	}
	
	/*
	 * @return Is this Card active?
	 */
	public boolean isActive () {
		return active;
	}
	
	private void setup() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (monster != null && monster.am_I_the_Enemy()) {
					//Well the Enemy should do nothing because its not controlled from this site
					return;
				}
				
				if (event.getButton().compareTo(MouseButton.SECONDARY) == 0 && active ) {
					//Active Monster right click Handler
					//This feels like a hack, but it work, vs the straight forward method, which does not work
					Attacke[] attacken = monster.getAttacken().toArray(new Attacke[0]);
					List<Text> boxen = new ArrayList<>();
					
					for (int i = 0; i < attacken.length; i++) {
						if (null == attacken[i]) {
							System.err.println("Ääähm wtf? " + attacken.length);
							System.exit(-1);
						}
						Text t = new Text(attacken[i].getName(), 
								attacken[i].getName(), 
								attacken[i]);
						t.setStroke(Color.WHITE);
						t.setOnMouseClicked(new HandleAttackClick(attacken[i]));
						boxen.add(t);
					}
					GridPane p = (GridPane) Main.s.lookup("#Bottom");
					p.addRow(0, boxen.toArray(new Node[0]));
					return;
				} else if (event.getButton().compareTo(MouseButton.PRIMARY) == 0) {
					if (monster == null) {
						return;
					}
					//Left click handle
					if (!active && !Main.isoneMonsteractive) {
						setActive();
						Main.isoneMonsteractive = true;
					} else {
						//Another if, because of the very edge case that a plugin messed isoneMonsteractive up
						if (active) {
							Main.isoneMonsteractive = false;
							setInActive();
							GridPane p = (GridPane) Main.s.lookup("#Bottom");
							ObservableList<Node> list = p.getChildren();
							Task<Void> t = new Task<Void>() {

								@Override
								protected Void call() throws Exception {
									List<Attacke> a = monster.getAttacken();
									for (int i = 0; i < list.size(); i++) {
										Node n = list.get(i);
										System.out.println(n.getId());
										if (n.getId().equals(a.get(i).getName())) {
											list.remove(n);									
										}
									}
									return null;
								}
							};
							t.run();
						}
					}
				}
				
			}
		});
		
		//For displaying the description of this monster
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//No monster? Yes? We are done, bye
				if (monster == null) {
					return;
				}
				Text t = new Text(15, Color.WHITE, Text.placeNewline(monster.getValue("Beschreibung")), "DescriptionText");
				//Set the Text object above this comment to the Center of the Screen
				//Should use a static place instead of a dynamic
				Main.getLayoutPane().setCenter(t);
				//Does autosize even work?
				t.autosize();
				
			}
		});
		
		this.setOnMouseExited(new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				//No Monster? Bye
				if (monster == null || shows_attacks) {
					return;
				} else {
					//Remove this damn shit, i hope this doesn't get called after a new Text is already posted, maybe i should delay the posting...
					//Or even worse, if there is something i need to stay there, like the Attacks...
					Main.getLayoutPane().setCenter(null);
				}
			}
			
		});
		
		
		//set the style for this card
		this.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
		        + "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 0 0;");
		this.applyCss();
		
	}
	
	
}
