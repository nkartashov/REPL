/**
 * HighlightTraverser
 *
 * Version 1.0.0
 *
 * Created on 14/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.visitors;

import visitor_practice.parsing_hell.lexer.Lexeme;
import visitor_practice.parsing_hell.lexer.LexemeType;

import java.util.ArrayList;
import java.util.List;

public class HighlightTraverser {
	public static List<Highlight> highlight(List<Lexeme> lexemes) {
		List<Highlight> result = new ArrayList<>();
		for (Lexeme lexeme : lexemes) {
			result.add(new Highlight(lexeme.start, lexeme.length, getStyle(lexeme)));
		}
		return result;
	}

	private static String getStyle(Lexeme lexeme) {
		if (lexeme.type == LexemeType.ID) {
			return "variable";
		}
		if (lexeme.type == LexemeType.NUMERIC) {
			return "number";
		}
		if (lexeme.type == LexemeType.ADDITION
				|| lexeme.type == LexemeType.SUBTRACTION
				|| lexeme.type == LexemeType.MULTIPLICATION
				|| lexeme.type == LexemeType.DIVISION)     {
			return "sign";
		}
		if (lexeme.type == LexemeType.ERROR) {
			return "error";
		}
		return "default";
	}
}
