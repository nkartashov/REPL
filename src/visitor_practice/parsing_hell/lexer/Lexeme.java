/**
 * Lexeme
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.lexer;

public class Lexeme {
	final public LexemeType type;
	final public int start;
	final public int length;

	public Lexeme(LexemeType type, int start, int length) {
		this.type = type;
		this.start = start;
		this.length = length;
	}
}
