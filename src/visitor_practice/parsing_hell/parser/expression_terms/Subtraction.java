package visitor_practice.parsing_hell.parser.expression_terms;

import visitor_practice.visitors.ExpressionVisitor;

/**
 * Addition
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public  class Subtraction extends BinaryExpression {
	public Subtraction(Expression left, Expression right, int start, int length) {
		super(left, right, start, length);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}