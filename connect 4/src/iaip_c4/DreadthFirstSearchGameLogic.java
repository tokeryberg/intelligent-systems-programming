package iaip_c4;

import java.util.BitSet;
import java.util.Random;

public class DreadthFirstSearchGameLogic implements IGameLogic {
	private int x = 0;
	private int y = 0;
	private int playerID;
	private int columns;
	private int rows;
	private static BitSet[] board;
	private final int BIT_SEQUENCE_LENGTH = 2;

	@Override
	public void initializeGame(int columns, int rows, int player) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.columns = columns;
		this.rows = rows;
		this.playerID = playerID;
		System.out.println("X - " + columns);
		System.out.println("Y - " + rows);

		makeGameBoard(rows, columns);
		printBoard();

	}

	public static void makeGameBoard(int rows, int columns) {
		board = new BitSet[rows];
		for (int i = 0; i < board.length; i++) {
			board[i] = new BitSet(columns * 2);
		}
	}

	@Override
	public void insertCoin(int column, int playerID) {
		for (int i = rows - 1; i >= 0; i--) {
			if (board[i].get(column * BIT_SEQUENCE_LENGTH,
					column * BIT_SEQUENCE_LENGTH + BIT_SEQUENCE_LENGTH)
					.cardinality() == 0) {
				// ex. column 3 = 3 * 2 (6th bit) to 3 * 2 + (0 or 1) + 1 (if 0
				// 7th bit not set. if 1 7th bit set) (0 = player 0, 1 = player
				// 1)
				board[i].set(column * BIT_SEQUENCE_LENGTH, column
						* BIT_SEQUENCE_LENGTH + playerID);
				break;
			}
		}
		printBoard();

		System.out.println(column);
		System.out.println(playerID);
	}

	public int getCell(int row, int column) {
		// returns the number of bits set to true in this cell.
		// 0 = no coin in this cell.
		// 1 = black coin.
		// 2 = white coin.
		return board[row].get(column * 2, column * 2 + BIT_SEQUENCE_LENGTH)
				.cardinality();
	}

	@Override
	public int decideNextMove() {
		// TODO Auto-generated method stub

		return doMiniMax(board);
	}

	@Override
	public Winner gameFinished() {
		// TODO Auto-generated method stub
		return Winner.NOT_FINISHED;
	}

	public int doMiniMax(BitSet board[]) {

		return 0;
	}

	public void printBoard() {
		System.out.println();
		for (int i = 0; i < board.length; i++) {

			System.out.print(i + " - | ");
			for (int j = 0; j < columns * 2; j++) {

				System.out.print(board[i].get(j));

				if ((j % 2 != 0))
					System.out.print(" | ");
			}
			System.out.println();
		}
	}

}
