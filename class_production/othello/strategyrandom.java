package othello;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

class StrategyRandom extends Strategy {
	Random aRD;

	StrategyRandom() {
		Calendar calendar = new GregorianCalendar();
		int sec = calendar.get(Calendar.SECOND);
		aRD = new Random(sec);
	}

	public int getMove(int player, Board board) {
		int[] legalMoves = new int[100];
		int k = 0;
		for (int p = 11; p <= 88; p++) {
			if (board.validMove(p) && board.legalMove(p, player)) {
				legalMoves[k] = p;
				k++;
			}
		}
		;
		int aNum = aRD.nextInt(k);
		int move = legalMoves[aNum];
		return move;
	}

}
