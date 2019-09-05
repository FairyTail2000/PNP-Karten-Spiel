package main;

import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;//Jar is basically a zip file
import javafx.scene.layout.GridPane;
import main.plugin.Actions;
import main.plugin.EventConstants;
import main.plugin.PluginList;//I dont need to import it, i could always just write the full name 
import main.util.ArrayUtils;

public class HandleAttackClick implements EventHandler<MouseEvent> {

	Attacke a = null;

	public HandleAttackClick(Attacke a) {
		this.a = a;
	}

	@Override
	public void handle(MouseEvent event) {
		// Rule: A W100 must be rolled and only if the Attacker is higher than the
		// defender he can attack

		if (a.wie_oft_kann_diese_attacke_noch_eingesetzt_werden() == 0) {
			return;
		}

		int attacker = Main.r.nextInt(100), defender = Main.r.nextInt(100);
		if (attacker <= defender) {
			Text t = new Text("success_me", "Angriff nicht erfolgreich... ;)" + "\nDu: " + attacker + "\nGegner: "
					+ defender + "\nDies ist ein kritischer Fehler\nBitte beim Developer melden");
			VBox vbox = (VBox) SinglePlayer.borderpane.getRight();
			if (vbox == null) {
				vbox = new VBox();
				vbox.setSpacing(20);
			}
			vbox.getChildren().remove(vbox.lookup("#success_me"));
			VBox.setMargin(t, new Insets(0, 20, 0, 0));
			vbox.getChildren().add(t);
			

			this.a.set_wie_oft_kann_diese_attacke_noch_eingesetzt_werden(this.a.wie_oft_kann_diese_attacke_noch_eingesetzt_werden() - 1);
			t = (Text) Main.getLayoutPane().lookup("#" + this.a.getName());
			if (t == null) {
				System.err.println(ErrorText.housten + " " + "#" + this.a.getName());
			} else {
			t.setText(this.a.getName() + " (" + this.a.wie_oft_kann_diese_attacke_noch_eingesetzt_werden() + ")");
			}
			return;
		}

		int atk_result = a.doAttack();
		EventConstants.damage = atk_result;

		Text t = new Text("success", "Angriff erfolgreich mit Schaden: " + atk_result);
		VBox vbox = (VBox) SinglePlayer.borderpane.getRight();
		if (vbox == null) {
			vbox = new VBox();
			vbox.setSpacing(20);
		} else {
			vbox.getChildren().remove(vbox.lookup("#" + t.getId()));
		}
		vbox.getChildren().add(t);
		vbox.setAlignment(Pos.CENTER);
		VBox.setMargin(t, new Insets(0, 20, 0, 0));

		PluginList.trigger(Actions.Attack);
		if (Main.actEnemy == null) {
			System.out.println("Um? Gewonnen?");
			System.out.println(SinglePlayer.getTime());
			finish();
			Game.ShowMenu();
			return;
		}

		t = (Text) Main.getLayoutPane().lookup("#" + this.a.getName());
		if (t == null) {
			System.out.println("HandleAttackClick.handle() " + "#" + this.a.getName());
		}
		
		
		t.setText(a.getName() + " (" + a.wie_oft_kann_diese_attacke_noch_eingesetzt_werden() + ")");

		GridPane bottom_grid = (GridPane) Main.getLayoutPane().lookup("#Bottom");
		// Find the active Monster Card
		Optional<Node> optional = bottom_grid.getChildren().stream().filter(new Predicate<Node>() {

			@Override
			public boolean test(Node n) {
				if (n instanceof VBox) {
					VBox act = (VBox) n;
					if (act.isActive()) {
						return true;
					}
				}
				return false;
			}
		}).findFirst();
		Monster me = ((VBox) optional.get()).getMonster();
		//
		int dmg = Main.actEnemy.getHPasInt() - computeDamageWithRanks(me.getRangAsInt(), Main.actEnemy.getRangAsInt(), atk_result);

		int hp = Main.actEnemy.getHPasInt() - dmg;
		Main.actEnemy.setHPasInt(hp);
		newRandomEnemyMonster(hp);
		SinglePlayer.KIMove();
	}

	private void finish() {
		for (Attacke a : Attacken.getAttacks()) {
			a.set_wie_oft_kann_diese_attacke_noch_eingesetzt_werden(Attacke.standart_count);
		}

		for (Monster s : Main.parsers) {
			s.setHPasInt(s.standard_life);
			s.setI_am_the_enemy(false);
		}
	}
	
	public static int computeDamageWithRanks (int rank_one, int rank_two, int damage) {
		int dmg, returnable;
		int diff = rank_one - rank_two;
		if (diff < 0) {// Negative
			int tmp = (int) Math.floor(Math.abs(diff / 20) + 1);
			dmg = damage / tmp;
			returnable = Main.actEnemy.getHPasInt() - dmg;
		} else {// Here it must be positive
			int tmp = (int) Math.floor(diff / 20) + 1;
			dmg = damage * tmp;
			returnable = Main.actEnemy.getHPasInt() - dmg;
		}
		return returnable;
		
	}
	/**
	 * Also sets the enemy health
	 */
	public static void newRandomEnemyMonster (int hp) {
		if (hp <= 0) {

			Monster s;
			for (int i = 0; i < SinglePlayer.enemyMonster.length; i++) {
				s = SinglePlayer.enemyMonster[i];
				if (s == null) {
					continue;
				}
				if (s.equals(Main.actEnemy)) {
					SinglePlayer.enemyMonster[i] = null;
					SinglePlayer.enemyMonster = ArrayUtils.filterZeros(SinglePlayer.enemyMonster, Monster.class);
					Main.actEnemy = null;
					SinglePlayer.borderpane.setTop(null);
					Monster new_monster = (Monster) ArrayUtils.randomObject(SinglePlayer.enemyMonster);
					if (new_monster == null) {
						Main.game_won = true;
						return;
					}
					new_monster.setI_am_the_enemy(true);
					VBox new_box = new VBox(new_monster);
					new_box.setId("EnemyMonster");
					new_box.setAlignment(Pos.CENTER);
					Main.actEnemy = new_monster;
					SinglePlayer.borderpane.setTop(new_box);
				}
			}
		} else {
			VBox enemy = (VBox) Main.getLayoutPane().lookup("#EnemyMonster");
			ObservableList<Node> children = enemy.getChildren();
			for (Node n : children) {
				if (n.getId().equals("EnemyMonsterHP")) {
					((Text) n).setText(String.valueOf(hp));
				}
			}
		}
	}
	
	
	
	
	
	
}