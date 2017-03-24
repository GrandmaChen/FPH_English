import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import algorithms.Interpreter;
import components.Bar;

public class Main extends Application {

	Bar head;
	Stage window;
	Button next;
	Button saveAsMidi;
	Button saveAsText;
	Button load;
	Button add;
	Button delete;
	Button playAll;
	int barCount;
	TilePane centreContainer;
	CheckBox inSharp;
	Scene scene2;
	Interpreter interpreter;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Launch algorithm components
		interpreter = new Interpreter();
		window = primaryStage;
		window.setTitle("四部和声计算器");
		// Page 1
		HBox functionalButtons = new HBox();
		next = new Button("下一步");
		next.setOnAction(e -> {
			window.setScene(scene2);
		});
		inSharp = new CheckBox("按升号显示");
		inSharp.setSelected(true);
		functionalButtons.getChildren().addAll(next);
		// Page 2
		HBox buttons = new HBox();
		barCount = 0;
		saveAsMidi = new Button("保存音列文件");
		saveAsText = new Button("保存文本文件");
		load = new Button("读取和弦文件");
		saveAsMidi.setOnAction(e -> {
			Bar temp = head;
			String toSave = "";
			while (temp.getNext() != null) {
				temp = temp.getNext();
				if (temp.chosenResult != null) {
					String duration = "";
					switch (Integer.parseInt(temp.getDuration())) {
					case 0:
						duration = "w";
						break;
					case 1:
						duration = "";
						break;
					case 2:
						duration = "q";
						break;
					}
					for (int i = 0; i < temp.chosenResult.getNotes().length; i++) {
						toSave += interpreter.numberToLetter(temp.chosenResult.getNotes()[i], temp.isInSharp())
								+ duration;
						if (i != 3)
							toSave += "+";
						else
							toSave += " ";
					}
				}
			}
			try {
				if (!toSave.equals("")) {
					FileChooser fileChooser = new FileChooser();
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MIDI 文件 (*.mid)", "*.mid");
					fileChooser.getExtensionFilters().add(extFilter);
					fileChooser.setTitle("保存音列文件");
					File file = fileChooser.showSaveDialog(primaryStage);
					MidiFileManager.savePatternToMidi(new Pattern(toSave.substring(0, toSave.length() - 1)), file);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		saveAsText.setOnAction(e -> {
			Bar temp = head;
			BufferedWriter writer;
			try {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT 文件 (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setTitle("保存文本文件");
				File file = fileChooser.showSaveDialog(primaryStage);
				writer = new BufferedWriter(new FileWriter(file));
				while (temp.getNext() != null) {
					String toSave = "";
					temp = temp.getNext();
					if (temp.getNext() == null) {
						toSave += temp.getChosenRoot() + " " + temp.getChosenType() + " " + temp.getInversion() + " "
								+ temp.getDuration() + " " + temp.getResult() + " ";
						if (temp.isInSharp())
							toSave += "#";
						else
							toSave += "b";
					} else {
						toSave += temp.getChosenRoot() + " " + temp.getChosenType() + " " + temp.getInversion() + " "
								+ temp.getDuration() + " " + temp.getResult() + " ";
						if (temp.isInSharp())
							toSave += "#";
						else
							toSave += "b";
						toSave += "\n";
					}
					writer.append(toSave);
				}
				writer.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		load.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("打开和弦文件");
			File file = fileChooser.showOpenDialog(primaryStage);
			while (barCount > 0) {
				deleteBar();
			}
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str = "";
				while ((str = br.readLine()) != null) {
					String[] chord = str.split("\\s+");
					Bar bar;
					if (chord[5].equals("#"))
						bar = new Bar(barCount + 1, true);
					else
						bar = new Bar(barCount + 1, false);
					bar.setRoot(chord[0]);
					bar.setType(chord[1]);
					bar.setInversion(chord[2]);
					bar.setDuration(chord[3]);
					bar.setChosenResult(chord[4]);
					addBar(bar);
				}
				br.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		add = new Button("添加");
		delete = new Button("删除");
		buttons.getChildren().addAll(add, delete, saveAsMidi, saveAsText, load);
		centreContainer = new TilePane();
		centreContainer.setPadding(new Insets(5));
		centreContainer.setHgap(5);
		centreContainer.setVgap(5);
		head = new Bar(barCount + 1, inSharp.isSelected());
		add.setOnAction(e -> {
			addBar();
		});
		delete.setOnAction(e -> {
			deleteBar();
		});
		playAll = new Button("播放所有小节");
		playAll.setOnAction(e -> {
			Bar temp = head;
			while (temp.getNext() != null) {
				temp = temp.getNext();
				temp.play(temp.chosenResult);
			}
		});
		buttons.getChildren().addAll(playAll, inSharp);
		BorderPane Page2 = new BorderPane();
		Page2.setTop(buttons);
		Page2.setCenter(centreContainer);
		scene2 = new Scene(Page2, 725, 450);
		window.setScene(scene2);
		window.show();
		window.setOnHidden(e -> Platform.exit());
	}

	public void addBar() {
		Bar newBar;
		try {
			newBar = (new Bar(barCount + 1, inSharp.isSelected()));
			addBar(newBar);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void addBar(Bar bar) {
		Bar temp = head;
		for (int i = 0; i < barCount; i++) {
			temp = temp.getNext();
		}
		temp.setNext(bar);
		bar.setPrev(temp);
		centreContainer.getChildren().add(bar.getContainer());
		barCount++;
	}

	public void deleteBar() {
		if (barCount != 0) {
			Bar temp = head;
			for (int i = 0; i < barCount - 1; i++) {
				temp = temp.getNext();
			}
			temp.closeWindow();
			temp.setNext(null);
			centreContainer.getChildren().remove(barCount - 1);
			barCount--;
		}
	}

	public static void main(String[] args) throws NumberFormatException {
		launch(args);
	}
}