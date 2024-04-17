package othello;

	class StrategyHuman extends Strategy {

		public int getMove(int player, Board board) {
			return -1;
		}

		public boolean isHuman() {
			return(true);
		}
	}

