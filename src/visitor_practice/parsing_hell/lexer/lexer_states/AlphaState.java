/**
 * AlphaState
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.lexer.lexer_states;

import visitor_practice.parsing_hell.lexer.LexemeType;
import visitor_practice.parsing_hell.lexer.LexemeTyper;
import visitor_practice.parsing_hell.lexer.Lexer;

public class AlphaState extends LexerState {
	public AlphaState(Lexer lexer, LexerState state) {
		super(lexer, state);
	}

	@Override
	public void nextChar(char c, int index) {
		LexemeType type = LexemeTyper.getType(c);
		switch (type) {
			case ID:
				break;
			case NUMERIC:
				returnLexeme(LexemeType.ID, index);
				start(index);
				getLexer().setState(new NumericState(getLexer(), this));
				break;
			default:
				returnLexeme(LexemeType.ID, index);
				start(index);
				returnLexeme(type, index);
				getLexer().setState(new NoState(getLexer(), this));
		}
	}

	@Override
	public void flush(int end) {
		returnLexeme(LexemeType.ID, end);
	}
}
