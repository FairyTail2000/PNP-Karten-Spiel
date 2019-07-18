package main;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
		
		primary.setFullScreen(true);
		primary.setTitle("Singleplayer");
		primary.setFullScreenExitHint("Do nothing");
		primary.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.POWER, KeyCombination.CONTROL_ANY));
		primary.setScene(s);
		primary.show();
		
		Thread timerThread = new Thread (new Runnable() {
			
			@Override
			public void run() {
				while (run) {
					time++;
					try {
						wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		timerThread.start();
		Main.window = primary.getScene().getWindow();
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
	
	
	public static void setStage (Stage stage) {
		primary = stage;
	}
	
}
