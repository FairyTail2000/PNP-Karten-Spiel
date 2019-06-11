package main;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import main.util.ArrayUtils;

public class HandleAttackClick implements EventHandler<MouseEvent>{

	Attacke a = null;
	
	public HandleAttackClick(Attacke a) {
		this.a = a;
	}
	
	
	@Override
	public void handle(MouseEvent event) {
		int atk_result = a.doAttack();
		int hp = Main.actEnemy.getHPasInt() - atk_result;
		
		Main.actEnemy.setHPasInt(Main.actEnemy.getHPasInt() - atk_result);
		
		if (hp <= 0) {
			Monster s;
			for (int i = 0; i < Main.enemyMonster.length; i++) {
				s = Main.enemyMonster[i];
				if (s == null) {
					continue;
				}
				if (s.equals(Main.actEnemy)) {
					Main.enemyMonster[i] = null;
					Main.enemyMonster = ArrayUtils.filterZeros(Main.enemyMonster, Monster.class);
					Main.actEnemy = null;
					Main.borderpane.setTop(null);
					Monster new_monster = (Monster) ArrayUtils.randomObject(Main.enemyMonster);
					if (new_monster == null) {
						Main.game_won = true;
						return;
					}
					new_monster.setI_am_the_enemy(true);
					VBox new_box = new VBox(new_monster);
					new_box.setId("EnemyMonster");
					Main.actEnemy = new_monster;
					Main.borderpane.setTop(new_box);
					
				}
			}
		} else {
			VBox enemy = (VBox) Main.s.lookup("#EnemyMonster");
			ObservableList<Node> children = enemy.getChildren();
			for (Node n : children) {
				if (n.getId().equals("EnemyMonsterHP")) {
					((Text) n).setText(String.valueOf(hp));
				}
			}
		}
		
		
		
	}

}
