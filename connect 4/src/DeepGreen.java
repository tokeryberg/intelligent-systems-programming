public class DeepGreen implements IGameLogic {
	private int playerId;
	private int round;
	private Board board;

	@Override
	public void initializeGame(int columns, int rows, int player) {
		this.board = new Board(columns, rows);
		this.playerId = player;
		this.round = 0;
		System.out.println("Initializing game with columns X rows " + columns
				+ " X " + rows);
	}

	@Override
	public void insertCoin(int column, int player) {
		board.setState(column, player);
		round++;
	}

	public int getCell(int row, int column) {
		// returns the number of bits set to true in this cell.
		// 0 = no coin in this cell.
		// 1 = black coin.
		// 2 = white coin.
		// return board[row].get(column * 2, column * 2 +
		// BIT_SEQUENCE_LENGTH).cardinality();
		return 0;
	}

	@Override
	public int decideNextMove() {
		return Move.decideMove(board, board.getState(), playerId, 8, round);
	}

	@Override
	public Winner gameFinished() {
		if (round > 6) {
			int winner = board.getStatus();
			if (winner == 3) {
				return Winner.TIE;
			} else if (winner == 1) {
				return Winner.PLAYER1;
			} else if (winner == 2) {
				return Winner.PLAYER2;
			}
		}
		return Winner.NOT_FINISHED;
	}
}
