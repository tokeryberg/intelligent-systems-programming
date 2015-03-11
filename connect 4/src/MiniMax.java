import java.util.ArrayList;
import java.util.BitSet;

public class MiniMax {

	private static Board board;

	// returns column to fill
	public static int miniMax(Board b, BitSet[] state, int player, int depth) {
		board = b;
		int result = 0;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		// for hvert move tjek value
		for (int move : board.getPossibleMoves(state)) {
			int value = minValue(board.getResult(state, player, move),
					nextPlayer, depth);
			if (value > utilityValue) {
				result = move;
				utilityValue = value;
				// break hvis winning value
				if (utilityValue == 1) {
					break;
				}
			}
		}
		return result;
	}

	private static int maxValue(BitSet[] state, int player, int depth) {
		if (depth == 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player);
		}
		depth--;
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = minValue(board.getResult(state, player, move), nextPlayer,
					depth);
			if (value > utilityValue) {
				result = move;
				utilityValue = value;
			}
		}
		return result;
	}

	private static int minValue(BitSet[] state, int player, int depth) {
		if (depth == 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player);
		}
		depth--;
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MAX_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = maxValue(board.getResult(state, player, move), nextPlayer,
					depth);
			if (value < utilityValue) {
				result = move;
				utilityValue = value;
			}
		}
		return result;
	}
}
