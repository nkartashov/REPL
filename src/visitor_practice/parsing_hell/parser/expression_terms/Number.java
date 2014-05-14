package visitor_practice.parsing_hell.parser.expression_terms;

import visitor_practice.visitors.ExpressionVisitor;

/**
 * Number
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public class Number extends Expression {
	public final java.lang.Number number;
	public Number(java.lang.Number number, int start, int length) {
		super(start, length);
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Number) && ((Number) obj).number.equals(number);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
