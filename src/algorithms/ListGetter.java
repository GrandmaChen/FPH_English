package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListGetter {

	public ObservableList<String> getChordList() throws IOException {

		ObservableList<String> chords = FXCollections.observableArrayList();

		String str = "";
		BufferedReader br = new BufferedReader(new FileReader("Chords.md"));

		while ((str = br.readLine()) != null) {
			chords.add(str);
			br.readLine();
		}
		br.close();
		return chords;
	}

	public int[] getNotesList(String root, String type) throws NumberFormatException, IOException {

		List<Integer> notes = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader("Notes.md"));
		String str = "";

		while ((str = br.readLine()) != null) {
			for (String s : str.split("\\s+")) {
				if (s.equals(root)) {
					notes.add(new Integer(br.readLine()));
					break;
				}
			}
			br.readLine();
		}
		br.close();

		br = new BufferedReader(new FileReader("Chords.md"));

		while ((str = br.readLine()) != null) {
			if (str.equals(type)) {
				for (String s : br.readLine().split("\\s+"))
					notes.add(new Integer(s) + notes.get(0));
				break;
			} else
				br.readLine();
		}
		br.close();

		if (notes.size() == 3) {
			int[] list = new int[3];
			for (int i = 0; i < 3; i++) {
				list[i] = notes.get(i);
			}
			return list;
		} else if (notes.size() == 4) {
			int[] list = new int[4];
			for (int i = 0; i < 4; i++) {
				list[i] = notes.get(i);
			}
			return list;
		}

		return null;
	}
}