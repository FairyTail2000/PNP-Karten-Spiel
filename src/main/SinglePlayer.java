package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.plugin.Actions;
import main.plugin.EventConstants;
import main.plugin.PluginList;
import main.util.ArrayUtils;

public class SinglePlayer {

	private static Stage primary = null;
	private static GameState state = GameState.Running;
	private static Monster[] myMonster;
	protected static Monster[] enemyMonster;
	protected static BorderPane borderpane;
	private transient static int time = 0;
	protected transient static boolean run = true;
	/**
	 * Sets up the Scene, and starts it
	 */
	public static void setup () {
		myMonster = new Monster[6];
		enemyMonster = new Monster[6];
		for (int i = 0; i < 6; i++) {
			Monster m = Main.parsers.get(Main.r.nextInt(Main.parsers.size()));
			if (ArrayUtils.isnotin(myMonster, m)) {
				myMonster[i] = m;
			} else {
				i--;
			}
		}
		
		for (int i = 0; i < 6; i++) {
			Monster m = Main.parsers.get(Main.r.nextInt(Main.parsers.size()));
			if (ArrayUtils.isnotin(myMonster, m) && ArrayUtils.isnotin(enemyMonster, m)) {
				enemyMonster[i] = m;
			} else {
				i--;
			}
		}
		
		Scene s = new Scene(borderpane);
		s.setFill(Color.BLACK);
		
		s.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode().compareTo(KeyCode.Q) == 0) {
                	System.exit(0);
                }
            }
        });
		
		List<VBox> downcards = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			Monster mp = myMonster[i];
			VBox v = new VBox(mp, i + 1);
			v.setId(mp.getName());
			v.setSpacing(2);
			downcards.add(v);
		}
		
		
		GridPane bottom_grid = new GridPane();
		bottom_grid.setId("Bottom");
		bottom_grid.setAlignment(Pos.TOP_CENTER);
		GridPane.setMargin(bottom_grid, new Insets(0, 0, 12, 0));
		for (int i = 0; i < downcards.size(); i++) {
			BorderPane.setMargin(downcards.get(i), new Insets(0, 12, 12, 12));
			bottom_grid.add(downcards.get(i), i + 1, 2);
		}
		
		Monster enemymo = enemyMonster[Main.r.nextInt(enemyMonster.length)];
		Main.actEnemy = enemymo;
		enemymo.setI_am_the_enemy(true);
		VBox enemyBox = new VBox(enemymo);
		enemyBox.setId("EnemyMonster");
		enemyBox.setAlignment(Pos.CENTER);
		
		borderpane.setTop(enemyBox);
		borderpane.setBottom(bottom_grid);
		
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventConstants.x = event.getScreenX();
				EventConstants.y = event.getScreenY();
				PluginList.trigger(Actions.MouseClick);
			}
		});
		
		primary.setTitle("Singleplayer");
		primary.setFullScreenExitHint("Do nothing");
		primary.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.POWER, KeyCombination.CONTROL_ANY));
		primary.setScene(s);
		primary.show();
		
		Task<Void> timerTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while (run) {
					time++;
					try {
						wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};
		
		timerTask.run();
		Game.setState(GameState.Running);
		Main.window = primary.getScene().getWindow();
		primary.setFullScreen(true);
	}
	
	/**
	 * How long did this game gone?
	 * @return the time
	 */
	public static String getTime () {
		if (time / 3600 == 0) {
			return String.format("%02d:%02d", time / 60, time % 60);
		}
		return String.format("%02d:%02d:%02d", time / 3600, time / 60, time % 60);
	}
	
	/**
	 * I really whish i could make this Method private...
	 * It sets the Stage where we should work with
	 * @param stage
	 */
	protected static void setStage (Stage stage) {
		primary = stage;
	}
	
	protected static void KIMove () {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		
		}
		//Limit the scope of variables
		if (true) {
			VBox myEnemyBox = (VBox) Main.getLayoutPane().lookup("#EnemyMonster");
			if (myEnemyBox != null) {
				TranslateTransition tt = new TranslateTransition(new Duration(800), myEnemyBox);
				tt.setByY(3d);
			}
		
		}
		
		
		
		
		GridPane bottom_grid = (GridPane) Main.getLayoutPane().lookup("#Bottom");
		Monster me = Main.actEnemy;
		//Find the active Monster Card
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
		Monster enemy = ((VBox) optional.get()).getMonster();
		
		Optional<Attacke> op = me.getAttacken().stream().filter(new Predicate<Attacke>() {

			@Override
			public boolean test(Attacke a) {
				if (a.doesDamage() && !a.isSuicide()) {
					return true;
				}
				return false;
			}
		}).findAny();
		
		
		if (true) {
			Text t = new Text("success_com", "KI macht einen Zug");
			VBox vbox = (VBox) SinglePlayer.borderpane.getRight();
			if (vbox == null) {
				vbox = new VBox();
				vbox.setSpacing(20);
			}
			//Inefficient but lol
			vbox.getChildren().remove(t);
			vbox.getChildren().add(t);
			vbox.setAlignment(Pos.CENTER);
			VBox.setMargin(t, new Insets(0, 20, 0 , 0));
			SinglePlayer.borderpane.setRight(vbox);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		
		}
		
		int defender = Main.r.nextInt(100), attacker = Main.r.nextInt(100);
		Attacke atk = op.get();
		if (attacker < defender) {
			Text t = new Text("success_com", "Angriff nicht erfolgreich... ;)" + "\nPC: " + attacker + "\nDu: " + defender );
			VBox vbox = (VBox) SinglePlayer.borderpane.getRight();
			vbox.getChildren().remove(vbox.lookup("#success_com"));
			vbox.getChildren().add(t);
			
			VBox.setMargin(t, new Insets(0, 20, 0 , 0));
			
			atk.doAttack();
			return;
		}
		
		
		int damage = HandleAttackClick.computeDamageWithRanks(me.getRangAsInt(), enemy.getRangAsInt(), atk.doAttack());
		EventConstants.damage = damage;
		PluginList.trigger(Actions.Attack);
		enemy.setHPasInt(enemy.getHPasInt() - damage);
		GridPane grid = (GridPane) Main.getLayoutPane().lookup("#Bottom");
		ObservableList<Node> children = grid.getChildren();
		String EnemyName = null;
		for (Node n : children) {
			if (n instanceof VBox) {
				VBox v = (VBox) n;
				EnemyName = v.getMonster().getName();
				
			}
		}
		VBox enemyNode =  (VBox) grid.lookup("#" + EnemyName);
		Text t = (Text) enemyNode.lookup("#EnemyMonsterHP");
		t.setText(enemyNode.getMonster().getHP());
		
		
		if (enemy.getHPasInt() <= 0) {
			Stream<Node> stream = bottom_grid.getChildren().stream().filter(new Predicate<Node>() {

				@Override
				public boolean test(Node t) {
					if (t instanceof HBox) {
						return true;
					} else if (t instanceof VBox) {
						VBox x = (VBox) t;
						return x.isActive();
					}
					return false;
				}
			});
			final Consumer<Node> con = new Consumer<Node>() {

				@Override
				public void accept(Node t) {
					if (t instanceof VBox) {
						VBox x = (VBox) t;
						if(x.isActive()) {
							GridPane p = (GridPane) Main.getLayoutPane().lookup("#Bottom");
							ObservableList<Node> list = p.getChildren();
							list.remove(Main.getLayoutPane().lookup("#AttackenBox"));
							bottom_grid.getChildren().remove(x);
						}
					}
					
				}
			};
			
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					stream.forEach(con);
					return null;
				}
				
			};
			task.run();
		}
	}
	
	public static GameState getCurrentGameState () {
		return state;
	}
	
	
}