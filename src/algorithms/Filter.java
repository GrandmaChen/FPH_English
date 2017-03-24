package algorithms;

import java.util.List;

import components.Chord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Filter {

	public static boolean[] getParralelIntervals(int[] chord) {

		boolean[] parralel = new boolean[12];
		if (chord[1] - chord[0] == 7) {
			parralel[0] = true;
		}
		if (chord[1] - chord[0] == 12) {
			parralel[1] = true;
		}
		if (chord[2] - chord[0] == 7) {
			parralel[2] = true;
		}
		if (chord[2] - chord[0] == 12) {
			parralel[3] = true;
		}
		if (chord[3] - chord[0] == 7) {
			parralel[4] = true;
		}
		if (chord[3] - chord[0] == 12) {
			parralel[5] = true;
		}
		if (chord[2] - chord[1] == 7) {
			parralel[6] = true;
		}
		if (chord[2] - chord[1] == 12) {
			parralel[7] = true;
		}
		if (chord[3] - chord[1] == 7) {
			parralel[8] = true;
		}
		if (chord[3] - chord[1] == 12) {
			parralel[9] = true;
		}
		if (chord[3] - chord[2] == 7) {
			parralel[10] = true;
		}
		if (chord[3] - chord[2] == 12) {
			parralel[11] = true;
		}
		return parralel;
	}

	public static void deleteCloseFilter(List<Chord> rawChords, boolean isAugFourth) {

		int limit;

		if (isAugFourth)
			limit = 6;
		else
			limit = 5;

		for (int i = 0; i < rawChords.size(); i++) {

			int[] chordContent = rawChords.get(i).getNotes();

			if (chordContent[2] - chordContent[1] <= limit && chordContent[3] - chordContent[2] <= limit) {
				rawChords.remove(i);
				i--;
			}
		}
	}

	public static void deleteOpenFilter(List<Chord> rawChords, boolean isAugFourth) {

		int limit;

		if (isAugFourth)
			limit = 6;
		else
			limit = 5;

		for (int i = 0; i < rawChords.size(); i++) {

			int[] chordContent = rawChords.get(i).getNotes();

			if (chordContent[2] - chordContent[1] > limit && chordContent[3] - chordContent[2] > limit) {
				rawChords.remove(i);
				i--;
			}
		}
	}

	public static ObservableList<Chord> onlyCloseFilter(List<Chord> rawChords, boolean isAugFourth) {

		ObservableList<Chord> result = FXCollections.observableArrayList();

		int limit;

		if (isAugFourth)
			limit = 6;
		else
			limit = 5;

		for (Chord chord : rawChords) {

			int[] chordContent = chord.getNotes();

			if (chordContent[2] - chordContent[1] <= limit && chordContent[3] - chordContent[2] <= limit) {
				result.add(chord);
			}

		}
		return result;

	}

	public static ObservableList<Chord> onlyOpenFilter(List<Chord> rawChords, boolean isAugFourth) {

		ObservableList<Chord> result = FXCollections.observableArrayList();

		int limit;

		if (isAugFourth)
			limit = 6;
		else
			limit = 5;

		for (Chord chord : rawChords) {

			int[] chordContent = chord.getNotes();

			if (chordContent[2] - chordContent[1] > limit && chordContent[3] - chordContent[2] > limit) {
				result.add(chord);
			}

		}

		return result;

	}

	public static void JumpFilter(List<Chord> rawChords, Chord prevChord) {

		for (int i = 0; i < rawChords.size(); i++) {
			int[] currChord = rawChords.get(i).getNotes();
			boolean flag = false;
			for (int j = 1; j < 4; j++) {
				if (Math.abs(currChord[j] - prevChord.getNotes()[j]) > 4) {
					flag = true;
					break;
				}
			}
			if (flag) {
				rawChords.remove(i);
				i--;
			}
		}
	}

	// 外声部隐伏五八度
	// 两个外声部同方向进行到8度或5度，高音部一跳进，就错
	public static void exteriorHiddenParralelFilterToPrev(List<Chord> rawChords, Chord prevChord) {

		int[] prevChordContent = prevChord.getNotes();

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChordContent = rawChords.get(i).getNotes();

			// If not 5th/8th
			if (!((currChordContent[3] - currChordContent[0] == 7)
					|| (currChordContent[3] - currChordContent[0] == 12))) {
				continue;
			}

			// 同向
			if (!((currChordContent[0] - prevChordContent[0] < 0) && (currChordContent[3] - prevChordContent[3] < 0)
					|| (currChordContent[0] - prevChordContent[0] > 0)
							&& (currChordContent[3] - prevChordContent[3] > 0))) {
				continue;
			}

			// 高音跳进
			if (!(Math.abs(currChordContent[3] - prevChordContent[3]) > 3)) {
				continue;
			}

			rawChords.remove(i);
			i--;

		}
		return;
	}

	public static void exteriorHiddenParralelFilterToNext(List<Chord> rawChords, Chord nextChord) {

		// If not 5th/8th

		int[] nextChordContent = nextChord.getNotes();

		if (!((nextChordContent[3] - nextChordContent[0] == 7) || (nextChordContent[3] - nextChordContent[0] == 12))) {
			return;
		}

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChordContent = rawChords.get(i).getNotes();

			// 同向
			if (!((currChordContent[0] - nextChordContent[0] < 0) && (currChordContent[3] - nextChordContent[3] < 0)
					|| (currChordContent[0] - nextChordContent[0] > 0)
							&& (currChordContent[3] - nextChordContent[3] > 0))) {
				continue;
			}

			// 高音跳进
			if (!(Math.abs(currChordContent[3] - nextChordContent[3]) > 3)) {
				continue;
			}
			rawChords.remove(i);
			i--;
		}
		return;
	}

	// Concurrent
	public static void concurrentFilter(List<Chord> rawChords, Chord prevChord) {

		for (int i = 0; i < rawChords.size(); i++) {
			int[] currChordContent = rawChords.get(i).getNotes();
			int[] prevChordContent = prevChord.getNotes();
			if ((currChordContent[0] < prevChordContent[0] && currChordContent[1] < prevChordContent[1]
					&& currChordContent[2] < prevChordContent[2] && currChordContent[3] < prevChordContent[3])
					|| (currChordContent[0] > prevChordContent[0] && currChordContent[1] > prevChordContent[1]
							&& currChordContent[2] > prevChordContent[2]
							&& currChordContent[3] > prevChordContent[3])) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	// No third repetition allowed
	public static void thirdRepetitonFilter(List<Chord> rawChords, int third) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] currChord = rawChords.get(i).getNotes();
			int numberOfThird = 0;

			for (int note : currChord) {
				if (note % 12 == third)
					numberOfThird++;

			}
			if (numberOfThird > 1) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	public static void connectPartFilter(List<Chord> rawChords, Chord prevChord, int part) {

		for (int i = 0; i < rawChords.size(); i++) {

			int[] chordContent = rawChords.get(i).getNotes();
			if (!(chordContent[part] == prevChord.getNotes()[part])) {
				rawChords.remove(i);
				i--;
			}
		}
	}

	// Parallel perfect 5th/8th filter
	public static void parralelFilter(List<Chord> rawChords, Chord prevChord) {

		boolean[] prevParralel = getParralelIntervals(prevChord.getNotes());

		for (int i = 0; i < rawChords.size(); i++) {

			// Parallel check
			boolean[] currentParralel = getParralelIntervals(rawChords.get(i).getNotes());
			boolean parallel = false;

			for (int j = 0; j < 12; j++) {

				if (currentParralel[j] == true && prevParralel[j] == true) {
					parallel = true;
					break;
				}
			}

			if (parallel) {
				rawChords.remove(i);
				i--;
			}
		}
		return;
	}

	public static ObservableList<Chord> rangeFilter(List<Chord> rawChords, int lowerBound, int upperBound) {

		ObservableList<Chord> result = FXCollections.observableArrayList();

		for (Chord chord : rawChords) {

			int[] chordContent = chord.getNotes();
			if (chordContent[0] >= lowerBound && chordContent[3] <= upperBound) {
				result.add(chord);
			}

		}
		return result;
	}

	public static void minor2ndFilter(List<Chord> rawChords) {

		for (int i = 0; i < rawChords.size(); i++) {
			int[] chordContent = rawChords.get(i).getNotes();
			if (chordContent[1] - chordContent[0] == 1 || chordContent[2] - chordContent[1] == 1
					|| chordContent[3] - chordContent[2] == 1) {
				rawChords.remove(i);
				i--;
			}
		}
	}

}