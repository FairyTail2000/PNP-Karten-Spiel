package main;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.plugin.Actions;
import main.plugin.PluginList;

public class Game {

	private static GameState state = GameState.Menu;

	private static BorderPane borderpane = new BorderPane();
	private static Scene s = new Scene(borderpane);
	private static Stage stage = null;
	
	
	public static void ShowMenu() {
		Text[] buttons = ConstructButtons();
		
		//Buttons Normal
		VBox vbox = new VBox();
		vbox.setSpacing(30);
		vbox.getChildren().addAll(buttons);
		vbox.setAlignment(Pos.CENTER);
		borderpane.setCenter(vbox);

		//Überschrift
		Text t = new Text();
		t.setId("Ueberschrift");
		t.setText("\nPEN AND PAPER KARTENSPIEL");
		t.setFont(Font.font(80));
		t.setFill(Color.AQUA);
		t.setTextAlignment(TextAlignment.CENTER);
		
		ScaleTransition transition = new ScaleTransition(new Duration(1000), t);
		transition.setAutoReverse(true);
		transition.setByX(0.1d);
		transition.setByY(0.1d);
		transition.setCycleCount(ScaleTransition.INDEFINITE);
		transition.play();
		
		
		VBox v = new VBox();
		v.getChildren().add(t);
		v.setAlignment(Pos.CENTER);
		v.setSpacing(20);
		
		borderpane.setTop(v);
		
		s.setFill(Color.BLACK);
		s.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode().compareTo(KeyCode.Q) == 0) {
                	System.exit(0);
                }
            }
        });
		stage.setFullScreen(true);
		stage.setTitle("Pen & Paper Monster Kartenspiel");
		stage.setFullScreenExitHint("Do nothing");
		stage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.POWER, KeyCombination.CONTROL_ANY));
		stage.setScene(s);
		stage.show();
		PluginList.trigger(Actions.FullScreenStart);
	}

	private static Text[] ConstructButtons() {
		int buttonMenge = 4;
		Text[] text_arr = new Text[buttonMenge];
		Text t = new Text();
		// SinglePlayer Button
		t.setId("SinglePlayerButton");
		t.setText("Einzelspieler");
		t.setFont(Font.font(40));
		t.setTextAlignment(TextAlignment.CENTER);
		t.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
				+ "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 0 0;");
		t.setOnMouseClicked(new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().compareTo(MouseButton.PRIMARY) == 0) {
					SinglePlayer.setup();
				}
				
			}
			
		});
		text_arr[0] = t;
		// Multiplayer
		t = new Text();
		t.setId("MultiPlayerButton");
		t.setText("Mehrspieler");
		t.setTextAlignment(TextAlignment.CENTER);
		t.setFont(Font.font(40));
		t.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
				+ "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 0 0;");
		text_arr[1] = t;
		// Options
		t = new Text();
		t.setId("OptionsButton");
		t.setText("Optionen");
		t.setTextAlignment(TextAlignment.CENTER);
		t.setFont(Font.font(40));
		t.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
				+ "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 0 0;");
		text_arr[2] = t;
		// Quit
		t = new Text();
		t.setId("QuitButton");
		t.setText("Schließen");
		t.setFill(Color.RED);
		t.setTextAlignment(TextAlignment.CENTER);
		t.setFont(Font.font(40));
		t.setStyle("-fx-border-style: solid inside; -fx-border-width: 5; -fx-border-insets: 5;"
				+ "-fx-border-radius: 5; -fx-border-color: transparent; -fx-border-radius: 10 10 0 0;");
		RotateTransition r = new RotateTransition(new Duration(3000), t);
		r.setByAngle(180d);
		r.setAutoReverse(true);
		r.setCycleCount(2);
		t.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.exit(0);
				
			}});
		
		t.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
					r.play();
			}
		});
		t.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
					r.stop();
				
			}
		});
		
		text_arr[3] = t;

		return text_arr;
	}

	public static GameState getState() {
		return state;
	}

	protected static void setStage (Stage s) {
		stage = s;
	}
	
	
	
	public static void setState(GameState State) {
		state = State;
	}
}
