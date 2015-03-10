import java.util.BitSet;

public class MiniMax {

	private static Board board;

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
		if (board.isTerminal(state)) {
			return board.getUtilityValue(state, player);
		}
		int result = 0;
		int resultValue = Integer.MIN_VALUE;
		for (int move : board.getPossibleMoves(state)) {
			int value = maxValue(board.getResult(state, player, move), player);
			if (value > resultValue) {
				result = move;
				resultValue = value;
			}
		}
		return result;
	}

	private static int minValue(BitSet[] state, int player) {
		if (board.isTerminal(state)) {
			return board.getUtilityValue(state, player);
		}
		int result = 0;
		int resultValue = Integer.MAX_VALUE;
		for (int move : board.getPossibleMoves(state)) {
			int value = maxValue(board.getResult(state, player, move), player);
			if (value < resultValue) {
				result = move;
				resultValue = value;
			}
		}
		return result;
	}
}
