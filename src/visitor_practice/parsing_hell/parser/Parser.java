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
import java.util.Stack;

public class Parser {
	public Parser(List<Lexeme> lexemes, String source) {
		this.lexemes = lexemes;
		this.source = source;
	}

	public Expression parse() {
		skipWhitespace();
		return parseExpression(lexemes.size() - 1);
	}

	private Expression parseExpression(int lastLexemeToMatchIndex) {
		Expression assignment;
		Expression arithmetic;

		save();
		assignment = parseAssignment(lastLexemeToMatchIndex);
		restore();
		arithmetic = parseArithmetic(lastLexemeToMatchIndex);

		jump(lastLexemeToMatchIndex);
		return validatedReturn(
				bestFit(assignment, arithmetic),
				lastLexemeToMatchIndex);
	}

	private Expression parseAssignment(int lastLexemeToMatchIndex) {
		// Ensure that assignment sign is present
		int assignmentSign = nextAssignment(lastLexemeToMatchIndex);
		if (assignmentSign == lastLexemeToMatchIndex) {
			jump(lastLexemeToMatchIndex);
			return validatedReturn(Expression.invalidExpression(
					currentLexeme().start,
					lastLexemeToMatchIndex), lastLexemeToMatchIndex);
		}

		Expression variable;
		Expression expression;

		// Try to match variable, return invalid if fails
		variable = parseVariable(assignmentSign);

		// Match assignment sign, variable is invalid if fails
		nextLexeme();

		// Try to parse an expression in the same borders
		expression = parseExpression(lastLexemeToMatchIndex);

		return validatedReturn(
				new Assignment(
						variable,
						expression,
						variable.start,
						variable.length + expression.length + 1,
						assignmentSign),
				lastLexemeToMatchIndex);
	}

	private Expression parseArithmetic(int lastLexemeToMatchIndex) {
		Expression left;
		Expression right;
		IndexHolder signs = new IndexHolder();

		// Try to find any sign of simple arithmetic
		signs.add(nextSimpleArithmetic(lastLexemeToMatchIndex));

		// If there is no such sign, then it's just a term (possibly invalid)
		if (signs.getLast() == lastLexemeToMatchIndex) {
			return parseTerm(lastLexemeToMatchIndex);
		} else {
			// Parsed term up to an arithmetic sign
			left = parseTerm(signs.getLast());
		}

		while (current != lastLexemeToMatchIndex)
		{
			// Try to match simple arithmetic sign, variable is invalid if fails
			if (!matchSimpleArithmetic()) {
				left = Expression.invalidExpression(
						left.start,
						signs.getLast() - left.start);
				jump(signs.getLast());
			}
			Lexeme lexeme = currentLexeme();
			nextLexeme();

			// Look for the next arithmetic sign, if fails then we match whole right part as a term
			signs.add(nextSimpleArithmetic(lastLexemeToMatchIndex));
			right = parseTerm(signs.getLast());

			switch (lexeme.type) {
				case ADDITION:
					left = new Addition(
							left,
							right,
							left.start,
							left.length + right.length + 1,
							lexemes.get(signs.getSecondToLast()).start);
					break;
				case SUBTRACTION:
					left = new Subtraction(
							left,
							right,
							left.start,
							left.length + right.length + 1,
							lexemes.get(signs.getSecondToLast()).start);
					break;
			}
		}
		// Return when we have matched the whole expression
		return validatedReturn(left, lastLexemeToMatchIndex);
	}

	private Expression parseTerm(int lastLexemeToMatchIndex) {
		Expression left;
		Expression right;
		IndexHolder signs = new IndexHolder();

		// Try to find any sign of simple arithmetic
		signs.add(nextComplexArithmetic(lastLexemeToMatchIndex));

		// If there is no such sign, then it's just a factor (possibly invalid)
		if (signs.getLast() == lastLexemeToMatchIndex) {
			return parseFactor(lastLexemeToMatchIndex);
		} else {
			// Parsed term up to an arithmetic sign
			left = parseFactor(signs.getLast());
		}

		while (!hasFinished() && matchComplexArithmetic()) {
			// Try to match complex arithmetic sign, variable is invalid if fails
			if (!matchComplexArithmetic()) {
				left = Expression.invalidExpression(
						left.start,
						signs.getLast() - left.start);
				jump(signs.getLast());
			}
			Lexeme lexeme = currentLexeme();
			nextLexeme();

			// Look for the next arithmetic sign, if fails then we match whole right part as a term
			signs.add(nextComplexArithmetic(lastLexemeToMatchIndex));
			right = parseFactor(signs.getLast());

			switch (lexeme.type) {
				case MULTIPLICATION:
					left = new Multiplication(
							left,
							right,
							left.start,
							left.length + right.length + 1,
							lexemes.get(signs.getSecondToLast()).start);
					break;
				case DIVISION:
					left = new Division(
							left,
							right,
							left.start,
							left.length + right.length + 1,
							lexemes.get(signs.getSecondToLast()).start);
					break;
			}
		}
		// Return when we have matched the whole expression
		return validatedReturn(left, lastLexemeToMatchIndex);
	}

	private Expression parseFactor(int lastLexemeToMatchIndex) {
		Expression negativeExpression = null;
		Expression bracketedExpression = null;
		Expression constant;
		Expression variable;

		if (matchCurrent(LexemeType.SUBTRACTION)) {
			save();
			negativeExpression = getNegativeExpression(lastLexemeToMatchIndex);
			restore();
		}

		if (matchCurrent(LexemeType.LEFT_BRACKET)) {
			save();
			bracketedExpression = getBracketedExpression(lastLexemeToMatchIndex);
			restore();
		}
		save();
		constant = parseConstant(lastLexemeToMatchIndex);
		restore();

		save();
		variable = parseVariable(lastLexemeToMatchIndex);
		restore();
		jump(lastLexemeToMatchIndex);
		return validatedReturn(
				bestFit(
						negativeExpression,
						bracketedExpression,
						constant,
						variable),
				lastLexemeToMatchIndex);
	}

	private Expression getNegativeExpression(int lastLexemeToMatchIndex) {
		Expression negativeExpression;
		nextLexeme();
		Expression value = parseFactor(lastLexemeToMatchIndex);
		negativeExpression = new Subtraction(
				new Number(0, 0, 0),
				value,
				value.start - 1,
				value.length + 1,
				value.start - 1);
		return negativeExpression;
	}

	private Expression getBracketedExpression(int lastLexemeToMatchIndex) {
		int start = currentLexeme().start;
		Expression bracketedExpression;
		save();
		nextLexeme();
		if (nextRightBracket(lastLexemeToMatchIndex) != lastLexemeToMatchIndex - 1) {
			bracketedExpression = Expression.invalidExpression(
					start,
					lexemes.get(lastLexemeToMatchIndex).start - start);
		} else {
			bracketedExpression = parseExpression(lastLexemeToMatchIndex - 1);
			nextLexeme();
		}
		restore();
		return bracketedExpression;
	}

	private Expression parseConstant(int lastLexemeToMatchIndex) {
		int start = currentLexeme().start;
		if (!matchCurrent(LexemeType.NUMERIC) || current + 1 != lastLexemeToMatchIndex) {
			jump(lastLexemeToMatchIndex);
			return Expression.invalidExpression(
					start,
					lexemes.get(lastLexemeToMatchIndex).start - start);
		}

		String value = source.substring(start, start + currentLexeme().length);
		nextLexeme();
		return validatedReturn(
				new Number(Integer.parseInt(value), start, value.length()),
				lastLexemeToMatchIndex);
	}

	private Expression parseVariable(int lastLexemeToMatchIndex) {
		int start = currentLexeme().start;
		if (!matchCurrent(LexemeType.ID) || current + 1 != lastLexemeToMatchIndex) {
			jump(lastLexemeToMatchIndex);
			return Expression.invalidExpression(
					start,
					lexemes.get(lastLexemeToMatchIndex).start - start);
		}

		String value = source.substring(start, start + currentLexeme().length);
		nextLexeme();
		return validatedReturn(
				new Variable(value, start, value.length()),
				lastLexemeToMatchIndex);
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
		skipWhitespace();
	}

	private void skipWhitespace() {
		while (matchCurrent(LexemeType.WHITESPACE)) {
			current++;
		}
	}

	private void save() {
		savedPositions.push(current);
	}

	private void restore() {
		current = savedPositions.peek();
		savedPositions.pop();
	}

	private void jump(int index) {
		current = index;
	}

	private int nextSimpleArithmetic(int lastLexemeToMatchIndex) {
		return nextLexemeMatchingBeforeBrackets(
				LexemeType.ADDITION,
				LexemeType.SUBTRACTION,
				lastLexemeToMatchIndex);
	}

	private int nextComplexArithmetic(int lastLexemeToMatchIndex) {
		return nextLexemeMatchingBeforeBrackets(
				LexemeType.MULTIPLICATION,
				LexemeType.DIVISION,
				lastLexemeToMatchIndex);
	}

	private int nextRightBracket(int lastLexemeToMatchIndex) {
		return nextLexemeMatching(
				LexemeType.RIGHT_BRACKET,
				lastLexemeToMatchIndex);
	}

	private int nextAssignment(int lastLexemeToMatchIndex) {
		return nextLexemeMatching(
				LexemeType.ASSIGNMENT,
				lastLexemeToMatchIndex);
	}

	private int nextLexemeMatchingBeforeBrackets(
			LexemeType type1,
			LexemeType type2,
			int lastLexemeToMatchIndex) {
		int left = nextLexemeMatchingBeforeBrackets(type1, lastLexemeToMatchIndex);
		int right = nextLexemeMatchingBeforeBrackets(type2, lastLexemeToMatchIndex);
		return Math.min(left, right);
	}


		private int nextLexemeMatchingBeforeBrackets(
			LexemeType type,
			int lastLexemeToMatchIndex) {
			int counter = 0;
			for (int i = current; i < lastLexemeToMatchIndex && counter >= 0; i++) {
				Lexeme lexeme = lexemes.get(i);
				if (lexeme.type == type && counter == 0) {
					return i;
				}
				if (lexeme.type == LexemeType.LEFT_BRACKET) {
					counter++;
				}
				if (lexeme.type == LexemeType.RIGHT_BRACKET) {
					counter--;
				}

			}
			return lastLexemeToMatchIndex;
	}

	private int nextLexemeMatching(LexemeType type, int lastLexemeToMatchIndex) {
		for (int i = current; i < lastLexemeToMatchIndex; i++) {
			if (lexemes.get(i).type == type) {
				return i;
			}
		}
		return lastLexemeToMatchIndex;
	}

	private Expression bestFit(
			Expression e1,
			Expression e2) {
		if (e1 == null) {
			return e2;
		}
		if (e1.isValid()) {
			return e1;
		} else if (e2.isValid()) {
			return e2;
		}
		return e2.getDepth() < e1.getDepth() ? e1 : e2;
	}

	private Expression bestFit(
			Expression e1,
			Expression e2,
			Expression e3) {
		if (e1 == null) {
			return bestFit(e2, e3);
		}
		return bestFit(bestFit(e1, e2), e3);
	}

	private Expression bestFit(
			Expression e1,
			Expression e2,
			Expression e3,
			Expression e4) {
		if (e1 == null) {
			return bestFit(e2, e3, e4);
		}
		return bestFit(bestFit(e1, e2), bestFit(e3, e4));
	}

	private Expression validatedReturn(Expression result, int commitment) {
		if (current != commitment) {
			throw new RuntimeException("Commitment not fulfilled");
		}
		return result;
	}

	private Stack<Integer> savedPositions = new Stack<>();
	private int current = 0;
	private List<Lexeme> lexemes;
	private String source;
}
