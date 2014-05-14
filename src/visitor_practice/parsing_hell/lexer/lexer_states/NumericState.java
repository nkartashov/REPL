/**
 * NumericState
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

public class NumericState extends LexerState {
	public NumericState(Lexer lexer, LexerState state) {
		super(lexer, state);
	}

	@Override
	public void nextChar(char c, int index) {
		LexemeType type = LexemeTyper.getType(c);
		switch (type) {
			case NUMERIC:
				break;
			case ID:
				returnLexeme(LexemeType.NUMERIC, index);
				start(index);
				getLexer().setState(new AlphaState(getLexer(), this));
				break;
			default:
				returnLexeme(LexemeType.NUMERIC, index);
				start(index);
				returnLexeme(type, index);
				getLexer().setState(new NoState(getLexer(), this));
		}
	}

	@Override
	public void flush(int end) {
		returnLexeme(LexemeType.NUMERIC, end);
	}
}
