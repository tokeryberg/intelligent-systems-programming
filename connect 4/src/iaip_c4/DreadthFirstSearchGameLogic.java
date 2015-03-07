package iaip_c4;

import java.util.BitSet;
import java.util.Random;

public class DreadthFirstSearchGameLogic implements IGameLogic {
	private int player;
	private int columns;
	private int rows;
	private int round;
	private static BitSet[] board;
	private final int BIT_SEQUENCE_LENGTH = 2;
	private BitSet player1Win, player2Win;

	@Override
	public void initializeGame(int columns, int rows, int player) {
		// TODO Auto-generated method stub
		this.columns = columns;
		this.rows = rows;
		this.player = player;
		this.round = 0;
		player1Win = new BitSet(8);
		player2Win = new BitSet(8);
		player1Win.set(0, 8);
		player2Win.set(0, 8);
		player2Win.flip(1);
		player2Win.flip(3);
		player2Win.flip(5);
		player2Win.flip(7);
		System.out.println("X - " + columns);
		System.out.println("Y - " + rows);

		makeGameBoard(rows, columns);
		printBoard();
	}

	private static void makeGameBoard(int rows, int columns) {
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
				round++;
				break;
			}
		}
		printBoard();

		System.out.println(column);
		System.out.println(player);
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
		int winner = 0;
		if (round > 6) {
			// checkDraw();
			winner = checkHorizontal();
			if (winner == 0) {
				// winner = checkVertical();
				if (winner == 0) {
					// winner = checkDiagonal();
				}
			}
			;
		}
		// TODO Auto-generated method stub
		// if horizontal
		// if vertical
		// if diagonal x 2
		if (winner == 1) {
			return Winner.PLAYER1;
		} else if (winner == 2) {
			return Winner.PLAYER2;
		}
		return Winner.NOT_FINISHED;
	}

	private int checkHorizontal() {
		for (int row = rows - 1; row < 0; row--) {
			for (int column = 4; column < columns; column++) {
				if (board[row].get(column * 2)) {
					BitSet result = board[row].get((column - 4) * 2,
							column * 2 + 2);
					result.and(player1Win);
					if (result == player1Win) {
						return 1;
					} else if (result == player2Win) {
						return 2;
					}
				}
			}
		}
		return 0;
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
