package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jamlParser.main.JAMLParser;
import javafx.application.Application;
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
import javafx.stage.Window;
import main.download.Download;
import main.plugin.Actions;
import main.plugin.EventConstants;
import main.plugin.FindAndCall;
import main.plugin.PluginList;

public class Main extends Application {
	
	private static List<Monster> parsers;
	private static Monster[] myMonster;
	protected static Monster[] enemyMonster;
	public static Window window = null;
	protected static BorderPane borderpane;
	public static Dimension screenSize;
	public static Random r = new Random(System.currentTimeMillis());
	private static Monster active = null;
	public static boolean isoneMonsteractive = false;
	public static Scene s;
	public static Properties p = new Properties();
	public static Monster actEnemy;
	public static boolean game_won = false;
	
	
	public static void main(String[] args) {
		TestCases(true, false);
		setup();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		s = new Scene(borderpane);
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
		
		Monster enemymo = enemyMonster[r.nextInt(enemyMonster.length)];
		actEnemy = enemymo;
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
		
		primaryStage.setFullScreen(true);
		primaryStage.setTitle("Test");
		primaryStage.setFullScreenExitHint("Do nothing");
		primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.POWER, KeyCombination.CONTROL_ANY));
		primaryStage.setScene(s);
		primaryStage.show();
		window = primaryStage.getScene().getWindow();
		PluginList.trigger(Actions.FullScreenStart);
	}
	
	public static BorderPane getLayoutPane () {
		return borderpane;
	}
	
	private static boolean isnotin (Object[] array, Object obj) {
		for (Object o : array) {
			if (o != null && o.equals(obj)) {
				return false;
			}
		}
		return true;
	}
	
	private static void TestCases (boolean exit, boolean should_i_do_it) {
		if (should_i_do_it) {
			File config = new File("config.properties");
			try {
				if (Files.size(config.toPath()) == 0L) {
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileInputStream inStream = null;
			try {
				if (config.exists() && Files.size(config.toPath()) != 0L) {
					config.createNewFile();
					inStream = new FileInputStream(config);
					p.load(inStream);
					EventConstants.monsterBaseUrl = p.getProp("baseMonster");
					EventConstants.monsterDefi = p.getProp("monsterDefi");
					EventConstants.AttackBaseUrl = p.getProp("AttackBaseUrl");
					EventConstants.AttackDefi = p.getProp("AttackDefi");
				} else {
					System.err.println("Configuration file does not exist or is empty, cannot proceed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (exit) {
				System.exit(0);
			}
		}
	}
	
	private static void setup () {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		FindAndCall.start();
		
		Typen.setup();
		read_attacks();
		prepare_Monster();
		
		
		myMonster = new Monster[6];
		enemyMonster = new Monster[6];
		for (int i = 0; i < 6; i++) {
			Monster m = parsers.get(r.nextInt(parsers.size()));
			if (isnotin(myMonster, m)) {
				myMonster[i] = m;
			} else {
				i--;
			}
			
		}
		
		for (int i = 0; i < 6; i++) {
			Monster m = parsers.get(r.nextInt(parsers.size()));
			if (isnotin(myMonster, m) && isnotin(enemyMonster, m)) {
				enemyMonster[i] = m;
			} else {
				i--;
			}
		}
		PluginList.trigger(Actions.SetupDone);
	}
	
	private static void prepare_Monster () {
		File folder = new File("Monster");
		
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		Download.Download_Monster();
		
		File[] files = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(".monster")) {
					return true;
				}
				return false;
			}
		});
		
		if (files.length == 0) {
			System.err.println("Well I'm fucked, there are no files...");
			return;
		}
		borderpane = new BorderPane();
		parsers = new ArrayList<Monster>();
		
		for (File f : files) {
			try {
				parsers.add(new Monster(f));
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
		}
		if (parsers.size() == 0) {
			System.err.println("Well I'm fucked, there are no valid files...");
			return;
		}
	}
	
	private static void read_attacks () {
		File dir = new File ("Attacken");
		if (!dir.exists() || dir.isFile()) {
			System.err.println("Unable to read Attacks because the Folder \"Attacken\" does not exist");
			Download.Download_Atacken();
		} else {
			dir.delete();
			dir.mkdir();
		}
		File[] attacks = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jaml")) {
					return true;
				}
				return false;
			}
		});
		for (File att : attacks) {
			JAMLParser parser = new JAMLParser(att);
			Attacken.addAttack(new Attacke(parser));
		}
	}
	
	
	//Ignore this
	/*Stufe
	 * 0-99 US Unterstufe
	 * 100-199 MS Mittelstufe
	 * 200-299 OS Oberstufe
	 * 300-399 KS Königsstufe
	 * 400-499 ES Endstufe
	 * 500 SS / SS0 Schlusstufe
	 * 
	 * Generation
	 * 501-999 NG Neugeneration
	 * 1000-9999 MAG Mittelalte Generation
	 * 10.000-99.999 AG Alte Generation
	 * 100.000-999.999 EG Entgeneration
	 * 1.000.000 SG Schlussgeneration
	 */
}
