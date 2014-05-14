/**
 * LexerState
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.lexer.lexer_states;

import visitor_practice.parsing_hell.lexer.Lexeme;
import visitor_practice.parsing_hell.lexer.LexemeType;
import visitor_practice.parsing_hell.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

public abstract class LexerState {
	public LexerState(Lexer lexer) {
		this.lexer = lexer;
	}

	public LexerState(Lexer lexer, LexerState previousState) {
		this.lexer = lexer;
		this.result.addAll(previousState.getResult());
		this.lexemeStart = previousState.lexemeStart;
	}

	public abstract void nextChar(char c, int index);

	public abstract void flush(int end);

	public List<Lexeme> getResult() {
		List<Lexeme> value = result;
		result = new ArrayList<>();
		return value;
	}

	protected void start(int index) {
		lexemeStart = index;
	}

	protected void returnLexeme(LexemeType type, int finish) {
		if (lexemeStart == -1) {
			throw new UnsupportedOperationException("Lexer hasn't been started");
		}
		int length = finish - lexemeStart;
		length += finish == lexemeStart
				? 1
				: 0;
		result.add(new Lexeme(type, lexemeStart, length));
		lexemeStart = -1;
	}

	protected Lexer getLexer() {
		return lexer;
	}

	private Lexer lexer;
	private List<Lexeme> result = new ArrayList<>();
	private int lexemeStart = -1;
}
