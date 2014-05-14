package visitor_practice.parsing_hell.parser.expression_terms;

import visitor_practice.visitors.ExpressionVisitor;

/**
 * Expression
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public abstract class Expression {
	public final int start;
	public final int length;

	public Expression(int start, int length) {
		this.start = start;
		this.length = length;
	}

	public abstract <T> T accept(ExpressionVisitor<T> visitor);
}
