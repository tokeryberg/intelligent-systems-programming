import java.util.ArrayList;
import java.util.BitSet;

public class Board {

	private int columns;
	private int rows;
	private BitSet[] state;
	private final int BIT_SEQUENCE_LENGTH = 2;
	private BitSet player1Win, player2Win, draw;

	public Board(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		this.state = makeGameBoard(rows, columns);
		makeBitMasks();

	}

	public void makeBitMasks() {
		player1Win = new BitSet(8);
		player2Win = new BitSet(8);
		draw = new BitSet(columns * 2);
		player1Win.set(0, 8);
		player2Win.set(0, 8);
		player2Win.flip(1);
		player2Win.flip(3);
		player2Win.flip(5);
		player2Win.flip(7);
		draw.clear();
		// for (int i = 0; i < columns * 2; i++) {
		// if (i % 2 == 0)
		// draw.set(i);
		// }
	}

	public Board(Board board) {
		this.columns = board.columns;
		this.rows = board.rows;
		this.state = makeGameBoardClone(board.state);
		makeBitMasks();
	}

	public BitSet[] getResult(BitSet[] state, int player, int move) {
		// find next row to fill
		for (int i = this.rows - 1; i >= 0; i--) {
			if (state[i].get(move * BIT_SEQUENCE_LENGTH,
					move * BIT_SEQUENCE_LENGTH + BIT_SEQUENCE_LENGTH)
					.cardinality() == 0) {
				state[i].set(move * BIT_SEQUENCE_LENGTH, move
						* BIT_SEQUENCE_LENGTH + player);
				break;
			}
		}
		System.out.println("Player: " + player);
		System.out.println(move);
		//printBoard();

		//return state;
		return makeGameBoardClone(state);
	}

	public void setState(int move, int player) {
		this.state = getResult(state, player, move);
	}

	public int getColumns() {
		return this.columns;
	}

	public int getRows() {
		return this.rows;
	}

	public BitSet[] getState() {
		return this.state;
	}

	private BitSet[] makeGameBoard(int rows, int columns) {
		BitSet[] b = new BitSet[rows];
		for (int i = 0; i < b.length; i++) {
			b[i] = new BitSet(columns * BIT_SEQUENCE_LENGTH);
		}
		return b;
	}

	private BitSet[] makeGameBoardClone(BitSet[] state) {
		BitSet[] b = new BitSet[state.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (BitSet) state[i].clone();
		}
		return b;
	}

	public ArrayList<Integer> getPossibleMoves(BitSet[] state) {
		ArrayList<Integer> possibleMoves = new ArrayList<>();
		for (int i = 0; i < this.columns; i++) {
			// check if top row is occupied
			if (!state[0].get(i * 2)) {
				possibleMoves.add(i);
			}
		}
		return possibleMoves;
	}

	public boolean isTerminal(BitSet[] state) {

		switch (getStatus()) {
		case -1:
			return true;

		case 0:
			return true;

		case 1:
			return true;

		default:
			return false;
		}
	}

	public int getUtilityValue(BitSet[] state, int player) {
		System.out.println("GetUtility");
		int status = getStatus();
		System.out.println(status);
		if (status == player) {
			return 10000;
		} else {
			return -10000;
		}
		// returner negativ, hvis player ikke har et fordelagtigt træk.
		// for (int i = 0; i < state.length; i++) {
		// System.out.println(state[i]);
		// }
		// return 1;
	}

	public void printBoard() {
		System.out.println();
		for (int i = 0; i < this.state.length; i++) {

			System.out.print(i + " - | ");
			for (int j = 0; j < columns * 2; j++) {

				System.out.print(this.state[i].get(j));

				if ((j % 2 != 0))
					System.out.print(" | ");
			}
			System.out.println();
		}
	}

	public int getStatus() {
		// checkDraw();

		int winner = checkHorizontal();
		if (winner == 0) {
			winner = checkVertical();
			if (winner == 0) {
				winner = checkDiagonal();
			}
		}
		return winner;
	}

	private int checkDraw() {

		return 0;
	}

	private int checkVertical() {
		BitSet r = new BitSet(8);
		for (int c = 0; c < columns; c++) {
			for (int i = rows - 4; i >= 0; i--) {
				if (!state[i].get(c * 2)) {
					break;
				}
				r.set(0, state[i].get(c * 2));
				r.set(1, state[i].get(c * 2 + 1));
				r.set(2, state[i + 1].get(c * 2));
				r.set(3, state[i + 1].get(c * 2 + 1));
				r.set(4, state[i + 2].get(c * 2));
				r.set(5, state[i + 2].get(c * 2 + 1));
				r.set(6, state[i + 3].get(c * 2));
				r.set(7, state[i + 3].get(c * 2 + 1));
				r.and(player1Win);
				if (r.equals(player1Win)) {
					return 2;
				} else if (r.equals(player2Win)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int checkDiagonal() {
		for (int c = 0; c < this.columns; c++) {
			for (int i = state.length - 4; i >= 0; i--) {
				if (!state[i].get(c * 2)) {
					break;
				}
				if (c <= this.columns - 4) {
					return checkLeftToRightDiagonal(i, c);
				} else {
					return checkRightToLeftDiagonal(i, c);
				}
			}
		}
		return 0;
	}

	private int checkLeftToRightDiagonal(int row, int column) {
		BitSet r = new BitSet(8);
		r.set(0, state[row].get(column * 2));
		r.set(1, state[row].get(column * 2 + 1));
		r.set(2, state[row + 1].get(column * 2 + 2));
		r.set(3, state[row + 1].get(column * 2 + 3));
		r.set(4, state[row + 2].get(column * 2 + 4));
		r.set(5, state[row + 2].get(column * 2 + 5));
		r.set(6, state[row + 3].get(column * 2 + 6));
		r.set(7, state[row + 3].get(column * 2 + 7));
		r.and(player1Win);
		if (r.equals(player1Win)) {
			return 2;
		} else if (r.equals(player2Win)) {
			return 1;
		}
		return 0;
	}

	private int checkRightToLeftDiagonal(int row, int column) {
		BitSet r = new BitSet(8);
		r.set(0, state[row].get(column * 2));
		r.set(1, state[row].get(column * 2 + 1));
		r.set(2, state[row + 1].get(column * 2 - 2));
		r.set(3, state[row + 1].get(column * 2 - 1));
		r.set(4, state[row + 2].get(column * 2 - 4));
		r.set(5, state[row + 2].get(column * 2 - 3));
		r.set(6, state[row + 3].get(column * 2 - 6));
		r.set(7, state[row + 3].get(column * 2 - 5));
		r.and(player1Win);
		if (r.equals(player1Win)) {
			return 2;
		} else if (r.equals(player2Win)) {
			return 1;
		}
		return 0;
	}

	private int checkHorizontal() {
		for (int row = rows - 1; row >= 0; row--) {
			for (int column = 3; column < columns; column++) {
				if (state[row].get(column * 2)) {
					BitSet result = state[row].get((column - 3) * 2,
							column * 2 + 2);
					result.and(player1Win);
					if (result.equals(player1Win)) {
						return 2;
					} else if (result.equals(player2Win)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}
}
