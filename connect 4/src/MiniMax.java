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

	public static int alphaBeta(Board b, BitSet[] state, int player, int depth) {
		board = b;
		int result = 0;
		int value;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = minValue(board.getResult(state, player, move), nextPlayer,
					depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			//if (value > utilityValue) {
			if (Math.abs(value) >= utilityValue) {
				result = move;
				utilityValue = value;
				// break hvis winning value
				// if (utilityValue == 1) {
				// break;
				// }
			}
			board.undoMove(state, player, move);
		}
		// System.out.println("MOVE: " + result);
		// System.out.println("UTIL: " + utilityValue);
		return result;
	}

	private static int maxValue(BitSet[] state, int player, int depth,
			int alpha, int beta) {

		// System.out.println("NEW MAX");
		System.out.println(depth);
		if (depth <= 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player, true);
		}
		int utilityValue = alpha;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			utilityValue = Math.max(
					utilityValue,
					minValue(board.getResult(state, player, move), nextPlayer,
							depth--, utilityValue, beta));
			board.undoMove(state, player, move);
			if (utilityValue >= beta) {
				return utilityValue;
			}
		}
		return utilityValue;
	}

	private static int minValue(BitSet[] state, int player, int depth,
			int alpha, int beta) {
		// System.out.println("NEW MIN");
		if (depth <= 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player, false);
		}
		int utilityValue = beta;
		// int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			utilityValue = Math.min(
					utilityValue,
					maxValue(board.getResult(state, player, move), nextPlayer,
							depth--, alpha, utilityValue));
			board.undoMove(state, player, move);
			if (utilityValue <= alpha) {
				return utilityValue;
			}
		}
		return utilityValue;
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
