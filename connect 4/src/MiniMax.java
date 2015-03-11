import java.util.BitSet;

public class MiniMax {

	private static Board board;
	private static int depth;

	// returns column to fill
	public static int miniMax(Board b, BitSet[] state, int player) {
		depth = 0;
		board = b;
		int result = 0;
		int resultValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			int value = minValue(board.getResult(state, player, move),
					nextPlayer);
			if (value > resultValue) {
				result = move;
				resultValue = value;
			}
		}
		System.out.println("minimax move:" + result);
		return result;
	}

	private static int maxValue(BitSet[] state, int player) {
		if (/* board.isTerminal(state */depth == 5) {
			return board.getUtilityValue(state, player);
		}
		depth++;
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = minValue(board.getResult(state, player, move), nextPlayer);
			if (value  >utilityValue) {
				result = move;
				utilityValue = value;
				System.out.println("maxutil: " + utilityValue);
			}
		}
		return result;
	}

	private static int minValue(BitSet[] state, int player) {
		if (/* board.isTerminal(state) */depth == 5) {
			System.out.println("returned");
			return board.getUtilityValue(state, player);
		}
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MAX_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = maxValue(board.getResult(state, player, move), nextPlayer);
			if (value < utilityValue) {
				result = move;
				utilityValue = value;
				System.out.println("minutil: " + utilityValue);
			}
		}
		return result;
	}
}
