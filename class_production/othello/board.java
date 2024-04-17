package othello;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Board {
	static ImageIcon blackOn = null;
	static ImageIcon whiteOn = null;
	static ImageIcon emptyStill = null;

	static int empty = 0;
	static int black = 1;
	static int white = 2;
	static int outer = 3;
	static int boardSize = 8;
	static int[] allDirs = { -11, -10, -9, -1, 1, 9, 10, 11 };
	private int[] state;

	public Board() {
		if(blackOn == null) blackOn = new ImageIcon(Toolkit.getDefaultToolkit().getImage("BlackPiece.gif")); // 黒 (black)
		if(whiteOn == null) whiteOn = new ImageIcon(Toolkit.getDefaultToolkit().getImage("WhitePiece.gif")); // 白 (white)
		if(emptyStill == null) emptyStill = new ImageIcon( Toolkit.getDefaultToolkit().getImage("BlankSquare.gif" ) ); // 空白 (empty)

		initialState();
	}

	public Board(Board board) {
		state = new int[100];
		System.arraycopy(board.getState(), 0, state, 0, 100);
	}

	public int[] initialState() {
		state = new int[100];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0 || i == 9 || j == 0 || j == 9) {
					state[10 * i + j] = outer;
				} else {
					state[10 * i + j] = empty;
				}
			}
		}
		state[44] = white;
		state[45] = black;
		state[54] = black;
		state[55] = white;
		return state;
	}

	public JButton[][] display(JButton[][] buttonGrid) {
		int i, j;
		for (int k = 0; k < 100; k++) {
			if (state[k] != outer) {
				i = k / 10 - 1;
				j = k % 10 - 1;
				if (state[k] == black) {
					buttonGrid[i][j].setIcon(blackOn);
				} else {
					if (state[k] == white) {
						buttonGrid[i][j].setIcon(whiteOn);
					} else {
						buttonGrid[i][j].setIcon(emptyStill);
					}
				}
			}
		}
		return (buttonGrid);
	}

	public String getScore() {
		String scoretext = "黒: " + count(black) + "  白: " + count(white);
		return (scoretext);
	}

	public int count(int player) {
		int n = 0;
		for (int k = 0; k < 100; k++) {
			if (state[k] == player)
				n++;
		}
		return n;
	}

	boolean validMove(int move) {
		int k = move % 10;
		if (11 <= move && move <= 88 && k > 0 && k < 9) {
			return true;
		} else {
			return false;
		}
	}

	public boolean legalMove(int move, int player){
		if (state[move] != empty) {
			return false;
		} else {
			for (int k = 0; k < 8; k++) {
				if (causeFlipQ(move, allDirs[k], player) >= 0)
					return true;
			}
			return false;
		}
	}

	public int[] makeMove(int move, int player) {
		this.state[move] = player;
		for (int k = 0; k < 8; k++) {
			makeFlips(move, allDirs[k], player);
		}
		return (this.state);
	}


	public int[] makeFlips(int move, int dir, int player) {
		int bracket = causeFlipQ(move,dir,player);
		if (bracket >= 0) {
			for (int c = move + dir; c != bracket; c = c + dir) {
				state[c] = player;
			}
		}
		return state;
	}

	public int causeFlipQ(int move, int dir, int player) {
		int c = move + dir;
		if (state[c] != opponent(player))
			return -1;
		return findBracket(c + dir, dir, player);
	}

	public int findBracket(int c, int dir, int player) {
		if (state[c] == player) {
			return c;
		} else if (state[c] == opponent(player)) {
			return findBracket(c+dir,dir,player);
		} else {
			return -1;
		}
	}

	public int nextPlayer(int currentPlayer) {
		int opp = opponent(currentPlayer);
		if (anyLegalMove(opp)) {
			return opp;
		} else if (anyLegalMove(currentPlayer)) {
			return currentPlayer;
		} else {
			return -1;
		}
	}

	public boolean anyLegalMove(int player) {
		for (int p = 11; p <= 88; p++) {
			if (validMove(p) && legalMove(p, player)) {
				return true;
			}
		}
		return false;
	}

	public int[] getState() {
		return state;
	}

	public void setState(int[] state) {
		this.state = state;
	}

	static public int opponent(int player) {
		if (player == black) {
			return white;
		} else {
			return black;
		}
	}

}
