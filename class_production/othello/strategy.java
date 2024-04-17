package othello;

abstract class Strategy {

	abstract int getMove(int player, Board board);


	boolean isHuman(){
		return false;
	}

}
