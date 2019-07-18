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
		
		if (Main.actEnemy == null) {
			System.out.println("Um? Gewonnen?");
			
			
			
			System.exit(0);
		}
		
		Text t = (Text) Main.s.lookup("#" + this.a.getName());
		t.setText(a.getName() + " (" + a.wie_oft_kann_diese_attacke_noch_eingesetzt_werden() + ")");
		int hp = Main.actEnemy.getHPasInt() - atk_result;
		Main.actEnemy.setHPasInt(Main.actEnemy.getHPasInt() - atk_result);
		
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
					Main.actEnemy = new_monster;
					SinglePlayer.borderpane.setTop(new_box);
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