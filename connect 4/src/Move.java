import java.util.BitSet;

public class Move {

	private static Board board;

	public static int decideMove(Board b, BitSet[] state, int player,
			int depth, int round) {
		long start = System.currentTimeMillis();
		if (round == 0) {
			System.out.println("Move made in: "
					+ (System.currentTimeMillis() - start) / 1000.00 + " s");
			return b.getColumns() / 2;
		}
		board = b;
		int result = 0;
		int value;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			BitSet[] newState = board.getResult(state, player, move);
			if (board.isTerminal(newState)) {
				value = board.getUtilityValue(state, player, true);
			} else {
				value = minValue(newState, nextPlayer, depth,
						Integer.MIN_VALUE, Integer.MAX_VALUE);
			}
			if (value >= utilityValue) {
				result = move;
				utilityValue = value;
			}
			board.undoMove(state, player, move);
		}
		System.out.println("Move made in: "
				+ (System.currentTimeMillis() - start)/1000.00 + " s.");
		return result;
	}

	private static int maxValue(BitSet[] state, int player, int depth,
			int alpha, int beta) {
		if (depth <= 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player, true);
		}
		depth--;
		int utilityValue = alpha;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			utilityValue = Math.max(
					utilityValue,
					minValue(board.getResult(state, player, move), nextPlayer,
							depth, utilityValue, beta));
			board.undoMove(state, player, move);
			if (utilityValue >= beta) {
				return utilityValue;
			}
		}
		return utilityValue;
	}

	private static int minValue(BitSet[] state, int player, int depth,
			int alpha, int beta) {
		if (depth <= 0 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player, false);
		}
		depth--;
		int utilityValue = beta;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			utilityValue = Math.min(
					utilityValue,
					maxValue(board.getResult(state, player, move), nextPlayer,
							depth, alpha, utilityValue));
			board.undoMove(state, player, move);
			if (utilityValue <= alpha) {
				return utilityValue;
			}
		}
		return utilityValue;
	}
}
