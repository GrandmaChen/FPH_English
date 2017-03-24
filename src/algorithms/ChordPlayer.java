package algorithms;

import org.jfugue.player.Player;

import components.Chord;

/*
 * ChordPlayer is used to play a chord in a new thread
 */
public class ChordPlayer extends Thread {

	private Chord chord;
	private int duration;
	private Player player;

	public ChordPlayer(Chord chord, int duration) {
		this.chord = chord;
		this.player = new Player();
		this.duration = duration;
	}

	public void play() {

		String toPlay = "";
		if (duration == 0)
			toPlay += chord.getBass() + "w+" + chord.getTenor() + "w+" + chord.getAlto() + "w+" + chord.getSoprano()
					+ "w";
		else if (duration == 1)
			toPlay += chord.getBass() + "+" + chord.getTenor() + "+" + chord.getAlto() + "+" + chord.getSoprano();
		else if (duration == 2)
			toPlay += chord.getBass() + "q+" + chord.getTenor() + "q+" + chord.getAlto() + "q+" + chord.getSoprano()
					+ "q";

		player.play(toPlay);

	}
}
