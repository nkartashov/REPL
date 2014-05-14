/**
 * Parser
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.parser;

import visitor_practice.parsing_hell.lexer.Lexeme;
import visitor_practice.parsing_hell.lexer.LexemeType;
import visitor_practice.parsing_hell.parser.expression_terms.*;
import visitor_practice.parsing_hell.parser.expression_terms.Number;

import java.util.List;

public class Parser {
	public Parser(List<Lexeme> lexemes, String source) {
		this.lexemes = lexemes;
		this.source = source;
	}

	public Expression parse() {
		return parseExpression();
	}

	public int getLastParsedSymbol() {
		if (lexemes.size() == 0 || lastParsedLexeme == -1) {
			return 0;
		}

		Lexeme lexeme;
		if (lastParsedLexeme >= lexemes.size())
		{
			lexeme = lexemes.get(lexemes.size() - 1);
		}
		else
		{
			lexeme = lexemes.get(lastParsedLexeme);
		}
		return lexeme.start + lexeme.length;
	}

	private Expression parseExpression() {
		Expression result;
		save();
		result = parseAssignment();
		if (result == null) {
			restore();
			result = parseArithmetic();
		}
		return result;
	}

	private Assignment parseAssignment() {
		if (!matchCurrent(LexemeType.ID)) {
			return null;
		}
		int start = currentLexeme().start;
		String variable = source.substring(start, start + currentLexeme().length);
		nextLexeme();
		if (!matchCurrent(LexemeType.ASSIGNMENT)) {
			return null;
		}
		nextLexeme();
		Expression expression = parseExpression();
		if (expression == null) {
			return null;
		}
		saveLastParsed();
		return new Assignment(variable, expression, start, variable.length() + expression.length + 1);
	}

	private Expression parseArithmetic() {
		Expression left = parseTerm();
		if (left == null) {
			return null;
		}
		while (!hasFinished() && matchSimpleArithmetic())
		{
			Lexeme lexeme = currentLexeme();
			nextLexeme();
			Expression right = parseTerm();
			if (right == null) {
				return null;
			}
			switch (lexeme.type) {
				case ADDITION:
					left = new Addition(left, right, left.start, left.length + right.length + 1);
					break;
				case SUBTRACTION:
					left = new Subtraction(left, right, left.start, left.length + right.length + 1);
					break;
			}
		}
		saveLastParsed();
		return left;
	}

	private Expression parseTerm() {
		Expression left = parseFactor();

		if (left == null) {
			return null;
		}

		while (!hasFinished() && matchComplexArithmetic()) {
			Lexeme lexeme = currentLexeme();
			nextLexeme();
			Expression right = parseFactor();
			if (right == null) {
				return null;
			}

			switch (lexeme.type) {
				case MULTIPLICATION:
					left = new Multiplication(left, right, left.start, left.length + right.length + 1);
					break;
				case DIVISION:
					left = new Division(left, right, left.start, left.length + right.length + 1);
					break;
			}
		}
		saveLastParsed();
		return left;
	}

	private Expression parseFactor() {
		if (matchCurrent(LexemeType.SUBTRACTION)) {
			nextLexeme();

			Expression value = parseFactor();
			if (value == null) {
				return null;
			}

			saveLastParsed();
			return new Subtraction(new Number(0, 0, 0), value, value.start - 1, value.length + 1);
		}

		if (matchCurrent(LexemeType.LEFT_BRACKET)) {
			 nextLexeme();
			Expression expression = parseExpression();

			if (!matchCurrent(LexemeType.RIGHT_BRACKET)) {
				return null;
			}
			nextLexeme();
			return expression;
		}
		Expression result = parseConstant();
		if (result == null) {
			result = parseVariable();
		}

		saveLastParsed();
		return result;
	}

	private Expression parseConstant() {
		if (!matchCurrent(LexemeType.NUMERIC)) {
			return null;
		}
		int start = currentLexeme().start;
		String value = source.substring(start, start + currentLexeme().length);
		saveLastParsed();
		nextLexeme();
		return new Number(Integer.parseInt(value), start, value.length());
	}

	private Expression parseVariable() {
		if (!matchCurrent(LexemeType.ID)) {
			return null;
		}
		int start = currentLexeme().start;
		String value = source.substring(start, start + currentLexeme().length);
		saveLastParsed();
		nextLexeme();
		return new Variable(value, start, value.length());
	}

	private boolean hasFinished() {
		return current == lexemes.size();
	}

	private boolean basicMatch() {
		return currentLexeme() != null;
	}

	private boolean matchCurrent(LexemeType type) {
		return basicMatch() && currentLexeme().type == type;
	}

	private boolean matchSimpleArithmetic() {
		return basicMatch() && matchCurrent(LexemeType.ADDITION) || matchCurrent(LexemeType.SUBTRACTION);
	}

	private boolean matchComplexArithmetic() {
		return basicMatch() && matchCurrent(LexemeType.MULTIPLICATION) || matchCurrent(LexemeType.DIVISION);
	}

	private Lexeme currentLexeme() {
		return current < lexemes.size() ? lexemes.get(current) : null;
	}

	private void nextLexeme() {
		current++;
		while (matchCurrent(LexemeType.WHITESPACE)) {
			current++;
		}
	}

	private void saveLastParsed() {
		lastParsedLexeme = current;
	}

	private void save() {
		savedPosition = current;
	}

	private void restore() {
		current = savedPosition;
	}

	private int savedPosition = -1;
	private int current = 0;
	private int lastParsedLexeme = -1;
	private List<Lexeme> lexemes;
	private String source;
}
