package components;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableColumnFactory {

	public static TableColumn<Chord, String> getColumnColumn(String name) {

		String lable = "";

		switch (name) {
		case "bass":
			lable = "低音";
			break;
		case "tenor":
			lable = "次中音";
			break;
		case "alto":
			lable = "中音";
			break;
		case "soprano":
			lable = "高音";
			break;
		}

		TableColumn<Chord, String> tableColumn = new TableColumn<>(lable);
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(name));
		return tableColumn;
	}

}
