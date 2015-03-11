import java.util.BitSet;

public class MiniMax {

	private static Board board;
	private static int depth;
	// returns column to fill
	public static int miniMax(Board b, BitSet[] state, int player) {
		board = b;
		int result = 0;
		int resultValue = Integer.MIN_VALUE;
		for (int move : board.getPossibleMoves(state)) {
			int value = minValue(board.getResult(state, player, move), player);
			if (value > resultValue) {
				result = move;
				resultValue = value;
			}
		}
		return result;
	}

	private static int maxValue(BitSet[] state, int player) {
		if (/*board.isTerminal(state*/ depth == 5) {
			return board.getUtilityValue(state, player);
		}
		depth++;
		int result = 0;
		int resultValue = Integer.MIN_VALUE;
		for (int move : board.getPossibleMoves(state)) {
			 result = Math.max(resultValue, minValue(board.getResult(state, player, move), player));
			/*if (value > resultValue) {
				result = move;
				resultValue = value;
			}*/
		}
		System.out.println("RESULT : " + result);
		return result;
	}

	private static int minValue(BitSet[] state, int player) {
		if (/*board.isTerminal(state)*/ depth == 5) {
			System.out.println("returned");
			return board.getUtilityValue(state, player);
		}
		System.out.println("------------ " + depth + " ------------");
		int result = 0;
		int resultValue = Integer.MAX_VALUE;
		for (int move : board.getPossibleMoves(state)) {
			result = Math.min(resultValue, maxValue(board.getResult(state, player, move), player));
			/*if (value < resultValue) {
				result = move;
				resultValue = value;
			}*/
		}
		return result;
	}
}
