/**
 * NoState
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

public class NoState extends LexerState {
	public NoState(Lexer lexer) {
		super(lexer);
	}

	public NoState(Lexer lexer, LexerState state) {
		super(lexer, state);
	}

	@Override
	public void nextChar(char c, int index) {
		start(index);
		LexemeType type = LexemeTyper.getType(c);
		switch (type) {
			case NUMERIC:
				getLexer().setState(new NumericState(getLexer(), this));
				break;
			case ID:
				getLexer().setState(new AlphaState(getLexer(), this));
				break;
			default:
				returnLexeme(type, index);
		}
	}

	@Override
	public void flush(int end) {
		// Nothing to do here
	}
}
