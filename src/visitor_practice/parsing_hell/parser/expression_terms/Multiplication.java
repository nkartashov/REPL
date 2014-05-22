package visitor_practice.parsing_hell.parser.expression_terms;

import visitor_practice.visitors.ExpressionVisitor;

/**
 * Multiplication
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public class Multiplication extends BinaryExpression {
	public Multiplication(
			Expression left,
			Expression right,
			int start,
			int length,
			int signPosition) {
		super(left, right, start, length, signPosition);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
