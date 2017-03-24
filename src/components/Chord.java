package components;

public class Chord {
	private final String bass;
	private final String tenor;
	private final String alto;
	private final String soprano;
	private final int[] notes;

	public Chord(String bass, String tenor, String alto, String soprano, int[] notes) {
		this.bass = bass;
		this.tenor = tenor;
		this.alto = alto;
		this.soprano = soprano;
		this.notes = notes;
	}

	public String getBass() {
		return this.bass;
	}

	public String getTenor() {
		return this.tenor;
	}

	public String getAlto() {
		return this.alto;
	}

	public String getSoprano() {
		return this.soprano;
	}

	public int[] getNotes() {
		return this.notes;
	}
}