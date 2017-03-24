package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FourPartHarmony {

	public FourPartHarmony() {
	}

	public List<int[]> getRawTriads(int[] NoteList, int inversion) {

		int lowerBound = 21;
		int upperBound = 108;

		List<Integer> rootNotes = new ArrayList<Integer>();
		List<Integer> thirdNotes = new ArrayList<Integer>();
		List<Integer> fifthNotes = new ArrayList<Integer>();
		List<Integer> allPossibilities = new ArrayList<Integer>();
		List<int[]> rawList = new ArrayList<int[]>();

		for (int i = 0; i < 10; i++) {

			if (NoteList[0] + 12 * i <= upperBound && NoteList[0] + 12 * i >= lowerBound) {
				rootNotes.add(NoteList[0] + 12 * i);
			}

			if (NoteList[1] + 12 * i <= upperBound && NoteList[1] + 12 * i >= lowerBound) {
				thirdNotes.add(NoteList[1] + 12 * i);
			}

			if (NoteList[2] + 12 * i <= upperBound && NoteList[2] + 12 * i >= lowerBound) {
				fifthNotes.add(NoteList[2] + 12 * i);
			}
		}

		allPossibilities.addAll(rootNotes);
		allPossibilities.addAll(thirdNotes);
		allPossibilities.addAll(fifthNotes);

		// "inversion" decides which note to be bass note
		List<Integer> bass = null;
		if (inversion == 0)
			bass = rootNotes;
		else if (inversion == 1)
			bass = thirdNotes;
		else if (inversion == 2)
			bass = fifthNotes;

		// Get all possible combinations of 4 notes
		for (int n1 : bass) {
			if (n1 >= lowerBound && n1 <= upperBound) {
				for (int n2 : allPossibilities) {
					if (n2 > n1 && n2 <= upperBound) {
						for (int n3 : allPossibilities) {
							if (n3 > n2 && n3 <= upperBound) {
								for (int n4 : allPossibilities) {
									if (n4 > n3 && n4 <= upperBound) {
										List<Integer> check = new ArrayList<Integer>();
										check.add(n1 % 12);
										check.add(n2 % 12);
										check.add(n3 % 12);
										check.add(n4 % 12);
										Set<Integer> set = new HashSet<Integer>(check);
										if (set.size() == check.size() - 1 && n2 - n1 <= 24 && n3 - n2 <= 12
												&& n4 - n3 <= 12) {
											int[] chord = new int[4];
											chord[0] = n1;
											chord[1] = n2;
											chord[2] = n3;
											chord[3] = n4;
											rawList.add(chord);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rawList;
	}

	// Similar to previous one
	public List<int[]> getRawSeventhOrNinthChords(int[] NoteList, int inversion) {

		int lowerBound = 21;
		int upperBound = 108;

		List<Integer> rootNotes = new ArrayList<Integer>();
		List<Integer> thirdNotes = new ArrayList<Integer>();
		List<Integer> fifthNotes = new ArrayList<Integer>();
		List<Integer> seventhNotes = new ArrayList<Integer>();
		List<Integer> allPossibilities = new ArrayList<Integer>();
		List<int[]> rawList = new ArrayList<int[]>();

		for (int i = 0; i < 10; i++) {

			if (NoteList[0] + 12 * i <= upperBound && NoteList[0] + 12 * i >= lowerBound) {
				rootNotes.add(NoteList[0] + 12 * i);
			}

			if (NoteList[1] + 12 * i <= upperBound && NoteList[1] + 12 * i >= lowerBound) {
				thirdNotes.add(NoteList[1] + 12 * i);
			}

			if (NoteList[2] + 12 * i <= upperBound && NoteList[2] + 12 * i >= lowerBound) {
				fifthNotes.add(NoteList[2] + 12 * i);
			}

			if (NoteList[3] + 12 * i <= upperBound && NoteList[3] + 12 * i >= lowerBound) {
				seventhNotes.add(NoteList[3] + 12 * i);
			}

		}

		allPossibilities.addAll(rootNotes);
		allPossibilities.addAll(thirdNotes);
		allPossibilities.addAll(fifthNotes);
		allPossibilities.addAll(seventhNotes);

		List<Integer> bass = null;
		if (inversion == 0)
			bass = rootNotes;
		else if (inversion == 1)
			bass = thirdNotes;
		else if (inversion == 2)
			bass = fifthNotes;
		else if (inversion == 3)
			bass = seventhNotes;

		for (int n1 : bass) {
			if (n1 >= lowerBound && n1 <= upperBound) {
				for (int n2 : allPossibilities) {
					if (n2 > n1 && n2 <= upperBound) {
						for (int n3 : allPossibilities) {
							if (n3 > n2 && n3 <= upperBound) {
								for (int n4 : allPossibilities) {
									if (n4 > n3 && n4 <= upperBound) {
										List<Integer> check = new ArrayList<Integer>();
										check.add(n1 % 12);
										check.add(n2 % 12);
										check.add(n3 % 12);
										check.add(n4 % 12);
										Set<Integer> set = new HashSet<Integer>(check);
										if (set.size() == 4 && n2 - n1 <= 24 && n3 - n2 <= 12 && n4 - n3 <= 12) {
											int[] chord = new int[4];
											chord[0] = n1;
											chord[1] = n2;
											chord[2] = n3;
											chord[3] = n4;
											rawList.add(chord);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return rawList;
	}

}