package algorithms;

public class Interpreter {

	public String numberToLetter(int number, boolean sharp) {

		switch (number) {
		case 21:
			return "A0";
		case 22:
			if (sharp)
				return "A#0";
			else
				return "Bb0";
		case 23:
			return "B0";
		}

		int pitch = number % 12;
		int octave = (number - pitch) / 12 - 1;

		switch (number % 12) {
		case 0:
			return "C" + octave;
		case 1:
			if (sharp)
				return "C#" + octave;
			else
				return "Db" + octave;
		case 2:
			return "D" + octave;
		case 3:
			if (sharp)
				return "D#" + octave;
			else
				return "Eb" + octave;
		case 4:
			return "E" + octave;
		case 5:
			return "F" + octave;
		case 6:
			if (sharp)
				return "F#" + octave;
			else
				return "Gb" + octave;
		case 7:
			return "G" + octave;
		case 8:
			if (sharp)
				return "G#" + octave;
			else
				return "Ab" + octave;
		case 9:
			return "A" + octave;
		case 10:
			if (sharp)
				return "A#" + octave;
			else
				return "Bb" + octave;
		case 11:
			return "B" + octave;
		default:
			return "No note found" + octave;
		}
	}

	public int letterToNumber(String letter) {

		int octave = Character.getNumericValue(letter.charAt(letter.length() - 1));
		String pitch = letter.substring(0, letter.length() - 1);

		switch (pitch) {
		case "C":
			return 12 * (octave + 1);
		case "C#":
			return 12 * (octave + 1) + 1;
		case "Db":
			return 12 * (octave + 1) + 1;
		case "D":
			return 12 * (octave + 1) + 2;
		case "D#":
			return 12 * (octave + 1) + 3;
		case "Eb":
			return 12 * (octave + 1) + 3;
		case "E":
			return 12 * (octave + 1) + 4;
		case "F":
			return 12 * (octave + 1) + 5;
		case "F#":
			return 12 * (octave + 1) + 6;
		case "Gb":
			return 12 * (octave + 1) + 6;
		case "G":
			return 12 * (octave + 1) + 7;
		case "G#":
			return 12 * (octave + 1) + 8;
		case "Ab":
			return 12 * (octave + 1) + 8;
		case "A":
			return 12 * (octave + 1) + 9;
		case "A#":
			return 12 * (octave + 1) + 10;
		case "Bb":
			return 12 * (octave + 1) + 10;
		case "B":
			return 12 * (octave + 1) + 11;
		default:
			return 0;
		}
	}
}