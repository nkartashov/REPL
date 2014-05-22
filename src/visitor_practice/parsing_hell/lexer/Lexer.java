/**
 * Lexer
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.lexer;

import visitor_practice.parsing_hell.lexer.lexer_states.LexerState;
import visitor_practice.parsing_hell.lexer.lexer_states.NoState;

import java.util.List;

public class Lexer {
	public void setState(LexerState state) {
		this.state = state;
	}

	public void lex(String input) {
		for (int i = 0; i < input.length(); i++) {
			state.nextChar(input.charAt(i), i);
		}
		state.flush(input.length());
	}

	public List<Lexeme> getResult() {
		return end();
	}

	private List<Lexeme> end() {
		List<Lexeme> result = state.getResult();
		if (result.size() == 0) {
			result.add(new Lexeme(LexemeType.END, 0, 0));
		} else {
			Lexeme last = result.get(result.size() - 1);
			result.add(new Lexeme(LexemeType.END, last.start + last.length, 0));
		}

		return result;
	}

	private LexerState state = new NoState(this);
}
