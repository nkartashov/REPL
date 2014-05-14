/**
 * LexemeTyper
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.lexer;

public class LexemeTyper {
	public static LexemeType getType(char ch) {
		if (Character.isWhitespace(ch)) {
			return LexemeType.WHITESPACE;
		}
		if (ch == '=') {
			return LexemeType.ASSIGNMENT;
		}
		if (ch == '-') {
			return LexemeType.SUBTRACTION;
		}
		if (ch == '+') {
			return LexemeType.ADDITION;
		}
		if (ch == '*') {
			return LexemeType.MULTIPLICATION;
		}
		if (ch == '/') {
			return LexemeType.DIVISION;
		}
		if (ch == '(') {
			return LexemeType.LEFT_BRACKET;
		}
		if (ch == ')') {
			return LexemeType.RIGHT_BRACKET;
		}
		if ("0123456789".indexOf(ch) > -1) {
			return LexemeType.NUMERIC;
		}
		if ("abcdefghiklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(ch) > -1) {
			return LexemeType.ID;
		}
		return LexemeType.ERROR;
	}
}
