
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
				System.out.println("Round " + round);
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
				winner = checkVertical();
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
	
	private int checkVertical(){
		
		for (int column = 0 ; column < columns; column++) {
			for (int row = rows - 4; rows >= 0; row--) {
				System.out.println("row " + row);
				if (board[row].get(column * 2)) {
					BitSet result = new BitSet(8);

					//result.and(player1Win); 
				System.out.println(result.length());
				System.out.println(result.length()/2);
					for (int i = 0; i < result.length()/ 2; i++) {
						System.out.println(i);
						if(!board[row + i].get(column * 2)){
							break;
						}
						result.set(i*2);
						if(board[row+i].get(column*2+1)){
							result.set(i*2+1);
						}
					}
					System.out.println(result);
					
					
					
					/*	
					Boolean[] cv = new Boolean[8];
					cv[0] = board[row].get((column) * 2);
					cv[1] = board[row].get((column) * 2 + 2); 
					cv[2] = board[row+1].get((column) * 2);
					cv[3] = board[row+1].get(column * 2 + 2);
					cv[4] = board[row+2].get((column) * 2);				
					cv[5] = board[row+2].get((column) * 2 + 2);
					cv[6] = board[row+3].get((column) * 2);		
					cv[7] = board[row+3].get((column) * 2 + 2);		  
							  
					for (int i = 0; i < cv.length; i++) {
						System.out.print(cv[i] +" ");
					}
					*/
					/*
					System.out.println(result.toString());
					if (result.equals(player1Win)){
						return 2;
					} else if (result.equals(player2Win)) {
						return 1;
					}*/
				} else {
					break;
				}
			}
		}
		return 0;
	}

	private int checkHorizontal() {
		for (int row = rows - 1; row >= 0; row--) {
			for (int column = 3; column < columns; column++) {
				if (board[row].get(column * 2)) {
					BitSet result = board[row].get((column - 3) * 2,
							column * 2 + 2);
					result.and(player1Win);
					if (result.equals(player1Win)){
						return 2;
					} else if (result.equals(player2Win)) {
						return 1;
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
