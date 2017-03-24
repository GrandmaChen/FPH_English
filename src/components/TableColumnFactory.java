package components;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableColumnFactory {

	public static TableColumn<Chord, String> getColumnColumn(String name) {

		String lable = "";

		switch (name) {
		case "bass":
			lable = "bass";
			break;
		case "tenor":
			lable = "tenor";
			break;
		case "alto":
			lable = "alto";
			break;
		case "soprano":
			lable = "soprano";
			break;
		}

		TableColumn<Chord, String> tableColumn = new TableColumn<>(lable);
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(name));
		return tableColumn;
	}

}
