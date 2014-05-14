package visitor_practice.parsing_hell.lexer;

/*
* Version1.0.0
*
* Created on 06/05/2014
*
* The following text is protected by GPLv2 licence
* (http://www.gnu.org/licenses/gpl-2.0.html)
*/

public enum LexemeType {
	ERROR,
	ID,
	NUMERIC,
	ADDITION,
	SUBTRACTION,
	DIVISION,
	MULTIPLICATION,
	ASSIGNMENT,
	LEFT_BRACKET,
	RIGHT_BRACKET,
	WHITESPACE,
}
