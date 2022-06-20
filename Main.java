package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class Main extends Application implements EventHandler<ActionEvent>{
	
	Button button;
	Button button2;
	Stage stage;
	File f = null;
	Scene scene;
	Stage primaryStage;
	Text textBox;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			primaryStage.setTitle("Word occurrence counter");
			
			button = new Button("Select file");
			button2 = new Button("Count Words");
			textBox = new Text();
			textBox.setWrappingWidth(800);
			
			button.setOnAction(this);
			button2.setOnAction(this);
			
			HBox layout = new HBox(button, button2, textBox);
			
			scene = new Scene(layout,960,540);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void handle(ActionEvent event) {
		
		ExtensionFilter ext = new ExtensionFilter("Text Files", "*.txt");
		
		if (event.getSource() == button) {
			
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().add(ext);
			f = filechooser.showOpenDialog(stage);
			
		}
		
		if (event.getSource()==button2) {
			
			if (f != null) {
				
				HashMap<String, Integer> words = new HashMap<>();
				
				File textFile = f;
				
				try {
					
					Scanner scan = new Scanner(textFile);
					
					while (scan.hasNext()) {
						String word = scan.next();
						
						//remove non-letters
						for (int i=0; i<word.length(); i++) {
							
							if (word.charAt(i) < 'A' || word.charAt(i) > 'z') {
								word = word.substring(0,i) + word.substring(i+1);
								i--;
							}
							
						}
						
						//make words lower case and puts them in the hash map or adds one to frequency if it is already there
						if (word.length() > 0) {
							
							word = word.toLowerCase();
							
							if (words.containsKey(word)) {
								words.put(word, words.get(word) + 1);
							} else {
								words.put(word, 1);
							}
						}
					}
					
					//sorts the hash map in descending order
					Map<String, Integer> descendingWords = words.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue())).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),(entry1, entry2) -> entry2, LinkedHashMap::new));
					
					System.out.println(descendingWords);
					
					textBox.setText(descendingWords.toString());
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				}
			}
		}
	}
}
