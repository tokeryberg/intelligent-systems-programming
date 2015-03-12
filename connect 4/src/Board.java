import java.util.ArrayList;
import java.util.BitSet;

public class Board {

	private static final int[] MOVE_VALUE = { 0, 10, 100, 1000, 100000 };
	private int columns;
	private int rows;
	private BitSet[] state;
	private final int BIT_SEQUENCE_LENGTH = 2;
	private BitSet player1Win, player2Win, draw;
	private BitSet[] players;

	public Board(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		this.state = makeGameBoard(rows, columns);
		makeBitMasks();

	}

	public void makeBitMasks() {
		players = new BitSet[2];
		player1Win = new BitSet(8);
		player2Win = new BitSet(8);
		draw = new BitSet(columns * 2);
		player2Win.set(0, 8);
		player1Win.set(0, 8);
		player1Win.flip(1);
		player1Win.flip(3);
		player1Win.flip(5);
		player1Win.flip(7);
		players[0] = player1Win;
		players[1] = player2Win;
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
		// printBoard();

		// return state;
		return makeGameBoardClone(state);
	}

	public BitSet[] undoMove(BitSet[] state, int player, int move) {
		// find next row to fill
		for (int i = 0; i < this.rows; i++) {
			if (state[i].get(move * BIT_SEQUENCE_LENGTH,
					move * BIT_SEQUENCE_LENGTH + BIT_SEQUENCE_LENGTH)
					.cardinality() != 0) {
				state[i].set(move * BIT_SEQUENCE_LENGTH, move
						* BIT_SEQUENCE_LENGTH + player, false);
				break;
			}
		}
		// printBoard();

		// return state;
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
		// printBoard();
		return getStatus() > 0;
		// switch (getStatus()) {
		// case -1:
		// return true;

		// case 0:
		// return true;

		// case 1:
		// return true;

		// default:
		// return false;
		// }
	}

	public int getUtilityValue(BitSet[] state, int player, boolean isMaximizing) {
		// printBoard();
		int horizontalUtilityValue = checkHorizontalUtility(state, player);
		int verticalUtitlityValue = checkVerticalUtility(state, player);
		int diagonalUtilityValue = checkDiagonalUtility(state, player);
		int utilityValue = Math.max(horizontalUtilityValue,
				verticalUtitlityValue);
		utilityValue = Math.max(utilityValue, diagonalUtilityValue);
		if (!isMaximizing) {
			utilityValue = utilityValue * -1;
		}
		return utilityValue;
		// int status = getStatus();
		// if (status == player) {
		// return 1;
		// } else if (status == player % 2 + 1) {
		// return -1;
		// }
		// return 0;
	}

	private int checkHorizontalUtility(BitSet[] state, int player) {
		BitSet result = new BitSet(8);
		for (int row = rows - 1; row >= 0; row--) {
			for (int column = 0; column < columns; column++) {
				if (column + 4 > columns) {
					break;
				}
				result = state[row].get(column * 2, column * 2 + 8);
				// win
				// result.and(players[player - 1]);
				if (result.equals(players[player - 1])) {
					return MOVE_VALUE[4];
				}
				// 3 på stribe og en tom
				BitSet threeLeftEmpty = players[player - 1].get(0, 8);
				threeLeftEmpty.clear(0, 2);
				BitSet threeRightEmpty = players[player - 1].get(0, 8);
				threeRightEmpty.clear(6, 8);
				BitSet threeLeftDisjoint = players[player - 1].get(0, 8);
				threeLeftDisjoint.clear(2, 4);
				BitSet threeRightDisjoint = players[player - 1].get(0, 8);
				threeRightDisjoint.clear(4, 6);

				if (result.equals(threeLeftEmpty)
						|| result.equals(threeRightEmpty)
						|| result.equals(threeLeftDisjoint)
						|| result.equals(threeRightDisjoint)) {
					return MOVE_VALUE[3];
				}
				// 2 på stribe og to tomme
				BitSet twoLeftEmpty = players[player - 1].get(0, 8);
				twoLeftEmpty.clear(0, 4);
				BitSet twoRightEmpty = players[player - 1].get(0, 8);
				twoRightEmpty.clear(4, 8);
				BitSet twoLeftRightEmpty = players[player - 1].get(0, 8);
				twoLeftRightEmpty.clear(0, 2);
				twoLeftRightEmpty.clear(6, 8);
				BitSet twoMiddleEmpty = players[player - 1].get(0, 8);
				twoMiddleEmpty.clear(2, 6);
				BitSet leftEmptyMiddleEmpty = players[player - 1].get(0, 8);
				leftEmptyMiddleEmpty.clear(0, 2);
				leftEmptyMiddleEmpty.clear(4, 6);
				BitSet rightEmptyMiddleEmpty = players[player - 1].get(0, 8);
				rightEmptyMiddleEmpty.clear(2, 4);
				rightEmptyMiddleEmpty.clear(6, 8);
				if (result.equals(twoLeftEmpty) || result.equals(twoRightEmpty)
						|| result.equals(twoLeftRightEmpty)
						|| result.equals(twoMiddleEmpty)
						|| result.equals(leftEmptyMiddleEmpty)
						|| result.equals(rightEmptyMiddleEmpty)) {

					return MOVE_VALUE[2];
				}

				// 1 på stribe
				BitSet oneLeft = players[player - 1].get(0, 8);
				oneLeft.clear(2, 8);
				BitSet oneRight = players[player - 1].get(0, 8);
				oneRight.clear(0, 6);
				BitSet oneLeftMiddle = players[player - 1].get(0, 8);
				oneLeftMiddle.clear(0, 2);
				oneLeftMiddle.clear(4, 8);
				BitSet oneRightMiddle = players[player - 1].get(0, 8);
				oneRightMiddle.clear(0, 4);
				oneRightMiddle.clear(6, 8);
				if (result.equals(oneLeft) || result.equals(oneRight)
						|| result.equals(oneLeftMiddle)
						|| result.equals(oneRightMiddle)) {
					return MOVE_VALUE[1];
				}
			}
		}
		return MOVE_VALUE[0];
	}

	private int checkVerticalUtility(BitSet[] state, int player) {
		BitSet r = new BitSet(8);
		for (int c = 0; c < columns; c++) {
			for (int ro = rows - 1; ro >= 0; ro--) {
				if (ro - 3 < 0)
					break;
				r.set(0, state[ro].get(c * 2));
				r.set(1, state[ro].get(c * 2 + 1));
				r.set(2, state[ro - 1].get(c * 2));
				r.set(3, state[ro - 1].get(c * 2 + 1));
				r.set(4, state[ro - 2].get(c * 2));
				r.set(5, state[ro - 2].get(c * 2 + 1));
				r.set(6, state[ro - 3].get(c * 2));
				r.set(7, state[ro - 3].get(c * 2 + 1));
				// r.and(players[player - 1]);
				if (r.equals(players[player - 1])) {
					return MOVE_VALUE[4];
				}

				BitSet oneVertical = players[player - 1].get(0, 8);
				oneVertical.clear(2, 8);
				if (oneVertical.equals(r)) {
					return MOVE_VALUE[1];
				}

				BitSet twoVertical = players[player - 1].get(0, 8);
				twoVertical.clear(4, 8);
				if (twoVertical.equals(r)) {
					return MOVE_VALUE[2];
				}

				BitSet threeVertical = players[player - 1].get(0, 8);
				threeVertical.clear(6, 8);
				if (threeVertical.equals(r)) {
					return MOVE_VALUE[3];
				}
			}
		}
		return MOVE_VALUE[0];
	}

	private int checkDiagonalUtility(BitSet[] state, int player) {
		int result = 0;
		for (int c = 0; c < this.columns; c++) {
			for (int i = state.length - 1; i >= 0; i--) {
				if (!(i - 3 < 0)) {
					if (c < 3) {
						result = rightUp(i, c, player);
					} else if (c + 4 > columns) {
						result = leftUp(i, c, player);
					} else {
						result = Math.max(rightUp(i, c, player),
								leftUp(i, c, player));
					}
				}
			}
		}
		return result;
	}

	private int rightUp(int row, int column, int player) {
		BitSet r = new BitSet(8);
		r.set(0, state[row].get(column * 2));
		r.set(1, state[row].get(column * 2 + 1));
		r.set(2, state[row - 1].get(column * 2 + 2));
		r.set(3, state[row - 1].get(column * 2 + 3));
		r.set(4, state[row - 2].get(column * 2 + 4));
		r.set(5, state[row - 2].get(column * 2 + 5));
		r.set(6, state[row - 3].get(column * 2 + 6));
		r.set(7, state[row - 3].get(column * 2 + 7));

		return getDiagonalUtility(r, player);
	}

	private int leftUp(int row, int column, int player) {
		BitSet r = new BitSet(8);
		r.set(0, state[row].get(column * 2));
		r.set(1, state[row].get(column * 2 + 1));
		r.set(2, state[row - 1].get(column * 2 - 2));
		r.set(3, state[row - 1].get(column * 2 - 1));
		r.set(4, state[row - 2].get(column * 2 - 4));
		r.set(5, state[row - 2].get(column * 2 - 3));
		r.set(6, state[row - 3].get(column * 2 - 6));
		r.set(7, state[row - 3].get(column * 2 - 5));

		return getDiagonalUtility(r, player);
	}

	private int getDiagonalUtility(BitSet result, int player) {
		if (result.equals(players[player - 1])) {
			return MOVE_VALUE[4];
		}
		// 3 på stribe og en tom
		BitSet threeLeftEmpty = players[player - 1].get(0, 8);
		threeLeftEmpty.clear(0, 2);
		BitSet threeRightEmpty = players[player - 1].get(0, 8);
		threeRightEmpty.clear(6, 8);
		BitSet threeLeftDisjoint = players[player - 1].get(0, 8);
		threeLeftDisjoint.clear(2, 4);
		BitSet threeRightDisjoint = players[player - 1].get(0, 8);
		threeRightDisjoint.clear(4, 6);

		if (result.equals(threeLeftEmpty) || result.equals(threeRightEmpty)
				|| result.equals(threeLeftDisjoint)
				|| result.equals(threeRightDisjoint)) {
			return MOVE_VALUE[3];
		}
		// 2 på stribe og to tomme
		BitSet twoLeftEmpty = players[player - 1].get(0, 8);
		twoLeftEmpty.clear(0, 4);
		BitSet twoRightEmpty = players[player - 1].get(0, 8);
		twoRightEmpty.clear(4, 8);
		BitSet twoLeftRightEmpty = players[player - 1].get(0, 8);
		twoLeftRightEmpty.clear(0, 2);
		twoLeftRightEmpty.clear(6, 8);
		BitSet twoMiddleEmpty = players[player - 1].get(0, 8);
		twoMiddleEmpty.clear(2, 6);
		BitSet leftEmptyMiddleEmpty = players[player - 1].get(0, 8);
		leftEmptyMiddleEmpty.clear(0, 2);
		leftEmptyMiddleEmpty.clear(4, 6);
		BitSet rightEmptyMiddleEmpty = players[player - 1].get(0, 8);
		rightEmptyMiddleEmpty.clear(2, 4);
		rightEmptyMiddleEmpty.clear(6, 8);
		if (result.equals(twoLeftEmpty) || result.equals(twoRightEmpty)
				|| result.equals(twoLeftRightEmpty)
				|| result.equals(twoMiddleEmpty)
				|| result.equals(leftEmptyMiddleEmpty)
				|| result.equals(rightEmptyMiddleEmpty)) {

			return MOVE_VALUE[2];
		}

		// 1 på stribe
		BitSet oneLeft = players[player - 1].get(0, 8);
		oneLeft.clear(2, 8);
		BitSet oneRight = players[player - 1].get(0, 8);
		oneRight.clear(0, 6);
		BitSet oneLeftMiddle = players[player - 1].get(0, 8);
		oneLeftMiddle.clear(0, 2);
		oneLeftMiddle.clear(4, 8);
		BitSet oneRightMiddle = players[player - 1].get(0, 8);
		oneRightMiddle.clear(0, 4);
		oneRightMiddle.clear(6, 8);
		if (result.equals(oneLeft) || result.equals(oneRight)
				|| result.equals(oneLeftMiddle)
				|| result.equals(oneRightMiddle)) {
			return MOVE_VALUE[1];
		}
		return MOVE_VALUE[0];
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
		int winner = checkHorizontal();
		if (winner == 0) {
			winner = checkVertical();
			if (winner == 0) {
				winner = checkDiagonal();
				if (winner == 0) {
					winner = checkDraw();
				}
			}

		}
		return winner;
	}

	private int checkDraw() {
		int filledColumns = 0;
		for (int i = 0; i < columns; i++) {
			if (state[0].get(i * 2)) {
				filledColumns++;
			}
		}
		if (filledColumns == columns) {
			return 3;
		}
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
				// r.and(player1Win);
				if (r.equals(player2Win)) {
					return 2;
				} else if (r.equals(player1Win)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int checkDiagonal() {
		int result = 0;
		for (int c = 0; c < this.columns; c++) {
			for (int i = state.length - 4; i >= 0; i--) {
				if (!state[i].get(c * 2)) {
					break;
				}
				if (c < 3) {
					result = checkLeftToRightDiagonal(i, c);
				} else if (c > columns - 4) {
					result = checkRightToLeftDiagonal(i, c);
				} else {
					result = Math.max(checkLeftToRightDiagonal(i, c),
							checkRightToLeftDiagonal(i, c));
				}
			}
		}
		return result;
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
		if (r.equals(player2Win)) {
			return 2;
		} else if (r.equals(player1Win)) {
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
		if (r.equals(player2Win)) {
			return 2;
		} else if (r.equals(player1Win)) {
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
					if (result.equals(player2Win)) {
						return 2;
					} else if (result.equals(player1Win)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}
}
