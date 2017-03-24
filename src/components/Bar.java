package components;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import algorithms.ChordPlayer;
import algorithms.Filter;
import algorithms.FourPartHarmony;
import algorithms.Interpreter;
import algorithms.ListGetter;
import algorithms.Position;

public class Bar {
	private Label barNumber;
	private ObservableList<Chord> rawChords;
	private ObservableList<Chord> rangeTempChords;
	private int[] rawNotes;
	public Chord chosenResult;
	private Button choose;
	private TableView<Chord> resultShowcaseTable;
	private BorderPane tablePage;
	private CheckBox isAugFourth;
	private CheckBox play;
	private Stage chordSort;
	private Scene chordEdit;
	private Group imagePane;
	private ListGetter noteList;
	private FourPartHarmony fph;
	private Interpreter interpreter;
	private ComboBox<String> chordRootComboBox;
	private ComboBox<String> ChordtypeComboBox;
	private ComboBox<String> inversionBox;
	private ComboBox<String> duration;
	private boolean inversionBoxExists;
	private boolean tableExists;
	private boolean chooseExists;
	private boolean durationExists;
	private int barNum;
	private Canvas canvas;
	private File file;
	private Image image;
	private GraphicsContext gc;
	private VBox container;
	private HBox rootNote;
	private HBox chordType;
	private boolean inSharp;
	private Slider lowerBoundSlider;
	private Slider upperBoundSlider;
	private Bar next;
	private Bar prev;

	public Bar(int barNumber, boolean inSharp) throws IOException {
		this.lowerBoundSlider = new Slider(21, 108, 47);
		this.upperBoundSlider = new Slider(21, 108, 100);
		this.canvas = new Canvas(626, 55);
		this.file = new File("piano.png");
		this.image = new Image(file.toURI().toString());
		this.gc = canvas.getGraphicsContext2D();
		this.setCanvas(canvas, image);
		this.imagePane = new Group();
		this.inSharp = inSharp;
		this.barNum = barNumber;
		this.next = null;
		this.prev = null;
		this.chosenResult = null;
		this.inversionBoxExists = false;
		this.chooseExists = false;
		this.durationExists = false;
		this.isAugFourth = new CheckBox("三全音算增四度");
		this.play = new CheckBox("选中时弹奏和弦");
		this.play.setSelected(true);
		this.duration = new ComboBox<String>(FXCollections.observableArrayList(new String[] { "全音符", "二分音符", "四分音符" }));
		this.setDefaultDuration();
		this.noteList = new ListGetter();
		this.fph = new FourPartHarmony();
		this.interpreter = new Interpreter();
		this.resultShowcaseTable = new TableView<>();
		this.tablePage = new BorderPane();
		this.barNumber = new Label(Integer.toString(barNumber));
		this.rawChords = FXCollections.observableArrayList();
		this.rangeTempChords = FXCollections.observableArrayList();
		this.container = new VBox();
		this.rootNote = new HBox();
		this.chordType = new HBox();
		if (inSharp) {
			chordRootComboBox = new ComboBox<String>(FXCollections.observableArrayList(
					new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" }));
			this.rootNote.getChildren().addAll(chordRootComboBox);
		} else {
			chordRootComboBox = new ComboBox<String>(FXCollections.observableArrayList(
					new String[] { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B" }));
			this.rootNote.getChildren().addAll(chordRootComboBox);
		}
		this.inversionBox = new ComboBox<String>();
		chordRootComboBox.valueProperty().addListener(e -> {
			if (!this.ChordtypeComboBox.getSelectionModel().isEmpty()) {
				if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位" }));
				} else {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位", "第3转位" }));
				}
				if (this.inversionBoxExists) {
					container.getChildren().set(3, this.inversionBox);
				}
				startResult();
				if (!this.inversionBoxExists)
					container.getChildren().addAll(inversionBox);
				inversionBoxExists = true;
			}
		});
		ChordtypeComboBox = new ComboBox<String>(this.noteList.getChordList());
		ChordtypeComboBox.valueProperty().addListener(e -> {
			if (!this.chordRootComboBox.getSelectionModel().isEmpty()) {
				if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位" }));
				} else {
					this.inversionBox = new ComboBox<String>(
							FXCollections.observableArrayList(new String[] { "原位", "第1转位", "第2转位", "第3转位" }));
				}
				if (this.inversionBoxExists) {
					container.getChildren().set(3, this.inversionBox);
				}
				startResult();
				if (!this.inversionBoxExists)
					container.getChildren().addAll(inversionBox);
				inversionBoxExists = true;
			}
		});
		chordType.getChildren().addAll(ChordtypeComboBox);
		container.getChildren().addAll(this.barNumber, rootNote, chordType);
	}

	@SuppressWarnings("unchecked")
	public void startResult() {
		inversionBox.valueProperty().addListener(e -> {
			try {
				if (!this.tableExists) {
					this.rawNotes = noteList.getNotesList(chordRootComboBox.getValue(), ChordtypeComboBox.getValue());
					List<int[]> chords;
					if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
						chords = fph.getRawTriads(rawNotes, inversionBox.getSelectionModel().getSelectedIndex());
					} else {
						chords = fph.getRawSeventhOrNinthChords(rawNotes,
								inversionBox.getSelectionModel().getSelectedIndex());
					}
					for (int[] tempChord : chords) {
						Chord chord = new Chord(this.interpreter.numberToLetter(tempChord[0], this.inSharp),
								this.interpreter.numberToLetter(tempChord[1], this.inSharp),
								this.interpreter.numberToLetter(tempChord[2], this.inSharp),
								this.interpreter.numberToLetter(tempChord[3], this.inSharp), tempChord);
						this.rawChords.add(chord);
					}
					// Filter
					if (this.prev != null && this.prev.chosenResult != null) {
						Filter.parralelFilter(this.rawChords, this.prev.chosenResult);
						Filter.concurrentFilter(this.rawChords, this.prev.chosenResult);
						Filter.exteriorHiddenParralelFilterToPrev(this.rawChords, this.prev.chosenResult);
					}
					if (this.next != null && this.next.chosenResult != null) {
						Filter.exteriorHiddenParralelFilterToNext(this.rawChords, this.next.chosenResult);
						Filter.concurrentFilter(this.rawChords, this.next.chosenResult);
						Filter.parralelFilter(this.rawChords, this.next.chosenResult);
					}
					if (this.rawNotes[1] == 3 || this.rawNotes[1] == 4)
						Filter.thirdRepetitonFilter(this.rawChords, this.rawNotes[1]);
					for (Chord chord : Filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
							(int) upperBoundSlider.getValue())) {
						this.rangeTempChords.add(chord);
					}
					if (!this.durationExists) {
						this.container.getChildren().addAll(duration);
						this.durationExists = true;
					}
					if (!this.chooseExists) {
						choose = new Button("请选择和弦");
						this.container.getChildren().addAll(choose);
						this.chooseExists = true;
						choose.setOnAction(c -> {
							this.chordSort.show();
						});
					}
					// When a chord is chosen
					resultShowcaseTable.setRowFactory(tv -> {
						TableRow<Chord> row = new TableRow<>();
						row.setOnMouseClicked(event -> {
							if (event.getClickCount() == 1 && (!row.isEmpty())) {
								Chord chord = row.getItem();
								String chordName = chord.getBass() + "+" + chord.getTenor() + "+" + chord.getAlto()
										+ "+" + chord.getSoprano();
								this.chosenResult = chord;
								choose.setText(chordName);
								canvas = new Canvas(626, 55);
								file = new File("piano.png");
								image = new Image(file.toURI().toString());
								gc = canvas.getGraphicsContext2D();
								setCanvas(canvas, image);
								imagePane.getChildren().set(0, canvas);
								this.render();
								if (this.play.isSelected())
									play(chord);
							}
						});
						return row;
					});
					TableColumn<Chord, String> bass = TableColumnFactory.getColumnColumn("bass");
					TableColumn<Chord, String> tenor = TableColumnFactory.getColumnColumn("tenor");
					TableColumn<Chord, String> alto = TableColumnFactory.getColumnColumn("alto");
					TableColumn<Chord, String> soprano = TableColumnFactory.getColumnColumn("soprano");

					resultShowcaseTable.setItems(this.rangeTempChords);
					resultShowcaseTable.getColumns().addAll(bass, tenor, alto, soprano);

					// Sorting buttons
					Button deleteClose = getFilterButton("去除紧密排列");
					Button deleteOpen = getFilterButton("去除开放排列");
					Button onlyClose = getFilterButton("只要紧密排列");
					Button onlyOpen = getFilterButton("只要开放排列");
					Button noJumpToPrev = getFilterButtonRelatedToPrevOrNext("去除对前超过跳进音程", true);
					Button noJumpToNext = getFilterButtonRelatedToPrevOrNext("去除对后超过跳进音程", false);
					Button connectBassToPrev = getFilterButtonRelatedToPrevOrNext("对前低音连接", true);
					Button connectBassToNext = getFilterButtonRelatedToPrevOrNext("对后低音连接", false);
					Button connectTenorToPrev = getFilterButtonRelatedToPrevOrNext("对前次中音连接", true);
					Button connectTenorToNext = getFilterButtonRelatedToPrevOrNext("对后次中音连接", false);
					Button connectAltoToPrev = getFilterButtonRelatedToPrevOrNext("对前中音连接", true);
					Button connectAltoToNext = getFilterButtonRelatedToPrevOrNext("对后中音连接", false);
					Button connectSopranoToPrev = getFilterButtonRelatedToPrevOrNext("对前高音连接", true);
					Button connectSopranoToNext = getFilterButtonRelatedToPrevOrNext("对后高音连接", false);
					Button noMinor2nd = new Button("去除小二度");
					noMinor2nd.setOnAction(filter -> {
						ObservableList<Chord> temp = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							temp.add(chord);
						Filter.minor2ndFilter(temp);
						resultShowcaseTable.setItems(temp);
						rangeTempChords = FXCollections.observableArrayList();
						for (Chord chord : resultShowcaseTable.getItems())
							rangeTempChords.add(chord);
					});
					Button restore = new Button("还原");
					restore.setOnAction(e3 -> {
						this.rangeTempChords.clear();
						for (Chord chord : Filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
								(int) upperBoundSlider.getValue())) {
							this.rangeTempChords.add(chord);
						}
						resultShowcaseTable.setItems(this.rangeTempChords);
					});
					VBox list1 = new VBox();
					VBox list2 = new VBox();
					HBox list3 = new HBox();
					Label connect = new Label("连接筛选");
					Label interval = new Label("音程筛选");
					list1.getChildren().addAll(connect, connectBassToNext, connectBassToPrev, connectTenorToNext,
							connectTenorToPrev, connectAltoToNext, connectAltoToPrev, connectSopranoToNext,
							connectSopranoToPrev, noMinor2nd);
					list2.getChildren().addAll(interval, deleteClose, deleteOpen, onlyClose, onlyOpen, noJumpToPrev,
							noJumpToNext);
					list3.getChildren().addAll(this.isAugFourth, this.play, restore);
					TilePane sortFunctions = new TilePane();
					sortFunctions.getChildren().addAll(list1, list2);
					Label lowerBoundLabel = new Label("下限：");
					Label upperBoundLabel = new Label("上限：");
					Label lowerBoundValue = new Label("A0");
					Label upperBoundValue = new Label("C8");
					lowerBoundSlider.setOrientation(Orientation.VERTICAL);
					upperBoundSlider.setOrientation(Orientation.VERTICAL);
					lowerBoundSlider.valueProperty().addListener(a -> {
						lowerBoundValue.textProperty()
								.setValue(interpreter.numberToLetter((int) lowerBoundSlider.getValue(), this.inSharp));
						ObservableList<Chord> temp = Filter.rangeFilter(this.rawChords,
								(int) lowerBoundSlider.getValue(), (int) upperBoundSlider.getValue());
						resultShowcaseTable.setItems(temp);
					});
					upperBoundSlider.valueProperty().addListener(a -> {
						upperBoundValue.textProperty()
								.setValue(interpreter.numberToLetter((int) upperBoundSlider.getValue(), this.inSharp));
						ObservableList<Chord> temp = Filter.rangeFilter(this.rawChords,
								(int) lowerBoundSlider.getValue(), (int) upperBoundSlider.getValue());
						resultShowcaseTable.setItems(temp);
					});
					HBox range = new HBox();
					range.getChildren().addAll(lowerBoundLabel, lowerBoundValue, lowerBoundSlider, upperBoundLabel,
							upperBoundValue, upperBoundSlider);
					this.imagePane.getChildren().add(canvas);
					this.tablePage.setLeft(resultShowcaseTable);
					this.tablePage.setCenter(sortFunctions);
					this.tablePage.setRight(range);
					TilePane bottom = new TilePane();
					bottom.getChildren().addAll(list3);
					this.tablePage.setBottom(bottom);
					this.tablePage.setTop(imagePane);
					this.chordSort = new Stage();
					this.chordSort.setTitle("第" + this.barNum + "小节");
					this.chordEdit = new Scene(tablePage, 626, 558);
					this.chordSort.setResizable(false);
					this.chordSort.setScene(chordEdit);
					this.chordSort.show();
					this.tableExists = true;
					// If table exists
				} else {
					List<int[]> chords;
					if (ChordtypeComboBox.getSelectionModel().getSelectedIndex() < 6) {
						chords = fph.getRawTriads(rawNotes, inversionBox.getSelectionModel().getSelectedIndex());
					} else {
						chords = fph.getRawSeventhOrNinthChords(rawNotes,
								inversionBox.getSelectionModel().getSelectedIndex());
					}
					this.rawChords = FXCollections.observableArrayList();
					this.rangeTempChords = FXCollections.observableArrayList();
					for (int[] tempChord : chords) {
						Chord chord = new Chord(this.interpreter.numberToLetter(tempChord[0], this.inSharp),
								this.interpreter.numberToLetter(tempChord[1], this.inSharp),
								this.interpreter.numberToLetter(tempChord[2], this.inSharp),
								this.interpreter.numberToLetter(tempChord[3], this.inSharp), tempChord);
						this.rawChords.add(chord);
					}
					// Filter
					if (this.prev != null && this.prev.chosenResult != null) {
						Filter.parralelFilter(this.rawChords, this.prev.chosenResult);
						Filter.concurrentFilter(this.rawChords, this.prev.chosenResult);
						Filter.exteriorHiddenParralelFilterToPrev(this.rawChords, this.prev.chosenResult);
					}
					if (this.next != null && this.next.chosenResult != null) {
						Filter.exteriorHiddenParralelFilterToNext(this.rawChords, this.next.chosenResult);
						Filter.concurrentFilter(this.rawChords, this.next.chosenResult);
						Filter.parralelFilter(this.rawChords, this.next.chosenResult);
					}
					if (this.rawNotes[1] == 3 || this.rawNotes[1] == 4)
						Filter.thirdRepetitonFilter(this.rawChords, this.rawNotes[1]);
					for (Chord chord : Filter.rangeFilter(this.rawChords, (int) lowerBoundSlider.getValue(),
							(int) upperBoundSlider.getValue())) {
						this.rangeTempChords.add(chord);
					}
					resultShowcaseTable.setItems(this.rangeTempChords);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private Button getFilterButtonRelatedToPrevOrNext(String name, boolean prev) {

		Button button = new Button(name);
		button.setOnAction(filter -> {
			if ((prev && this.prev != null && this.prev.chosenResult != null)
					|| (!prev && this.next != null && this.next.chosenResult != null)) {
				ObservableList<Chord> temp = FXCollections.observableArrayList();
				for (Chord chord : resultShowcaseTable.getItems())
					temp.add(chord);

				switch (name) {
				case "去除对前超过跳进音程":
					Filter.JumpFilter(temp, this.prev.chosenResult);
					break;
				case "去除对后超过跳进音程":
					Filter.JumpFilter(temp, this.next.chosenResult);
					break;
				case "对前低音连接":
					Filter.connectPartFilter(temp, this.prev.chosenResult, 0);
					break;
				case "对后低音连接":
					Filter.connectPartFilter(temp, this.next.chosenResult, 0);
					break;
				case "对前次中音连接":
					Filter.connectPartFilter(temp, this.prev.chosenResult, 1);
					break;
				case "对后次中音连接":
					Filter.connectPartFilter(temp, this.next.chosenResult, 1);
					break;
				case "对前中音连接":
					Filter.connectPartFilter(temp, this.prev.chosenResult, 2);
					break;
				case "对后中音连接":
					Filter.connectPartFilter(temp, this.next.chosenResult, 2);
					break;
				case "对前高音连接":
					Filter.connectPartFilter(temp, this.prev.chosenResult, 3);
					break;
				case "对后高音连接":
					Filter.connectPartFilter(temp, this.next.chosenResult, 3);
					break;
				case "去除小二度":
					Filter.minor2ndFilter(temp);
					break;
				}

				resultShowcaseTable.setItems(temp);
				rangeTempChords = FXCollections.observableArrayList();
				for (Chord chord : resultShowcaseTable.getItems())
					rangeTempChords.add(chord);
			}
		});

		return button;
	}

	private Button getFilterButton(String name) {

		Button button = new Button(name);
		button.setOnMouseClicked(filter -> {
			ObservableList<Chord> temp = FXCollections.observableArrayList();
			for (Chord chord : resultShowcaseTable.getItems())
				temp.add(chord);
			resultShowcaseTable.setItems(Filter.onlyOpenFilter(temp, this.isAugFourth.isSelected()));

			switch (name) {
			case "去除紧密排列":
				Filter.deleteCloseFilter(temp, this.isAugFourth.isSelected());
				break;
			case "去除开放排列":
				Filter.deleteOpenFilter(temp, this.isAugFourth.isSelected());
				break;
			case "只要紧密排列":
				resultShowcaseTable.setItems(Filter.onlyCloseFilter(temp, this.isAugFourth.isSelected()));
				break;
			case "只要开放排列":
				resultShowcaseTable.setItems(Filter.onlyOpenFilter(temp, this.isAugFourth.isSelected()));
				break;
			}

			rangeTempChords = FXCollections.observableArrayList();
			for (Chord chord : resultShowcaseTable.getItems())
				rangeTempChords.add(chord);
		});
		return button;
	}

	private void setCanvas(Canvas canvas, Image img) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public String[] resultShowCase(List<int[]> results) {
		String[] resultShowCase = new String[results.size()];
		for (int i = 0; i < results.size(); i++) {
			String combo = "";
			for (int j = 0; j < results.get(i).length; j++)
				if (j != 3)
					combo += interpreter.numberToLetter(results.get(i)[j], this.inSharp) + "+";
				else {
					combo += interpreter.numberToLetter(results.get(i)[j], this.inSharp);
				}
			resultShowCase[i] = combo;
		}
		return resultShowCase;
	}

	// Create a new thread to play this chord
	public void play(Chord chord) {
		if (chord != null) {

			new ChordPlayer(chord, this.duration.getSelectionModel().getSelectedIndex()) {
				public void run() {
					play();
				}
			}.start();
		}
	}

	public void render() {
		this.gc.setFill(Color.RED);
		for (int i : this.chosenResult.getNotes()) {
			int position = i % 12;
			if (position == 1 || position == 3 || position == 6 || position == 8 || position == 10)
				this.gc.fillOval(Position.getPosition(i), 21, 5, 5);
			else
				this.gc.fillOval(Position.getPosition(i), 45, 5, 5);
		}
	}

	public String getChosenRoot() {
		if (!this.chordRootComboBox.getSelectionModel().isEmpty())
			return this.chordRootComboBox.getValue();
		else
			return "null";
	}

	public String getChosenType() {
		if (!this.ChordtypeComboBox.getSelectionModel().isEmpty())
			return this.ChordtypeComboBox.getValue();
		else
			return "null";
	}

	public String getInversion() {
		if (!this.inversionBox.getSelectionModel().isEmpty())
			return this.inversionBox.getSelectionModel().getSelectedIndex() + "";
		else
			return "null";
	}

	public String getDuration() {
		if (!this.duration.getSelectionModel().isEmpty())
			return this.duration.getSelectionModel().getSelectedIndex() + "";
		else
			return "null";
	}

	public String getResult() {
		if (this.chosenResult != null)
			return this.choose.getText();
		else
			return "null";
	}

	public void setRoot(String root) {
		if (!(root.equals("null")))
			this.chordRootComboBox.getSelectionModel().select(root);
	}

	public void setType(String type) {
		if (!(type.equals("null")))
			this.ChordtypeComboBox.getSelectionModel().select(type);
	}

	public void setInversion(String inversion) {
		if (!(inversion.equals("null")))
			this.inversionBox.getSelectionModel().select(Integer.parseInt(inversion));
	}

	public void setDuration(String duration) {
		if (!(duration.equals("null")))
			this.duration.getSelectionModel().select(Integer.parseInt(duration));
	}

	public void setChosenResult(String chosenResult) {
		if (!(chosenResult.equals("null"))) {
			String[] chordContent = chosenResult.split("\\+");
			int[] notes = new int[4];
			for (int i = 0; i < 4; i++) {
				System.out.println(notes[i]);
				notes[i] = this.interpreter.letterToNumber(chordContent[i]);
			}
			Chord chord = new Chord(chordContent[0], chordContent[1], chordContent[2], chordContent[3], notes);
			this.chosenResult = chord;
			this.choose.setText(chosenResult);
			this.resultShowcaseTable.getSelectionModel().select(chord);
		}
	}

	public VBox getContainer() {
		return this.container;
	}

	public List<Chord> getResults() {
		return this.rawChords;
	}

	public void setNext(Bar next) {
		this.next = next;
	}

	public Bar getNext() {
		return this.next;
	}

	public void setPrev(Bar prev) {
		this.prev = prev;
	}

	public Bar getPrev() {
		return this.prev;
	}

	public void setDefaultDuration() {
		this.duration.getSelectionModel().select(0);
	}

	public boolean isInSharp() {
		return this.inSharp;
	}

	public void closeWindow() {
		if (this.tableExists)
			this.chordSort.close();
	}

}