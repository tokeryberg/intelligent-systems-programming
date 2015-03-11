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
			int value = minValue(board.getResult(state, player, move), player, depth);
			System.out.println(board.getResult(state, player, move).toString());
			if (value > utilityValue) {
				result = move;
				utilityValue = value;
				// if(utilityValue == WIN) {
				// break;
				// }
			}
		}
		System.out.println("minimax val:" + utilityValue);
		System.out.println("minimax move:" + result);
		return result;
	}

	private static int maxValue(BitSet[] state, int player, int depth) {
		if (depth == 10 || board.isTerminal(state)) {
			return board.getUtilityValue(state, player);
		}
		depth++;
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MIN_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = minValue(board.getResult(state, player, move), nextPlayer, depth);
			if (value > utilityValue) {
				result = move;
				utilityValue = value;
				System.out.println("maxutil: " + utilityValue);
			}
		}
		System.out.println("max val:" + utilityValue);
		return result;
	}

	private static int minValue(BitSet[] state, int player, int depth) {
		if (depth == 10 || board.isTerminal(state)) {
			System.out.println("returned");
			return board.getUtilityValue(state, player);
		}
		int result = 0;
		int value = 0;
		int utilityValue = Integer.MAX_VALUE;
		int nextPlayer = player % 2 + 1;
		for (int move : board.getPossibleMoves(state)) {
			value = maxValue(board.getResult(state, player, move), nextPlayer, depth);
			if (value < utilityValue) {
				result = move;
				utilityValue = value;
				System.out.println("minutil: " + utilityValue);
			}
		}
		System.out.println("min val:" + utilityValue);
		return result;
	}
}
