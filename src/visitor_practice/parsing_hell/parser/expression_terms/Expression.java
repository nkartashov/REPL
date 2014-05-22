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

	public boolean isValid() {
		return validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public abstract <T> T accept(ExpressionVisitor<T> visitor);

	public static Expression invalidExpression(int start, int length) {
		Expression result = new Number(0, start, length);
		result.validity = false;
		result.hasBeenInvalid = true;
		return result;
	}

	public boolean getHasBeenInvalid() {
		return hasBeenInvalid;
	}

	public void setHasBeenValid(boolean hasBeenInvalid) {
		this.hasBeenInvalid = hasBeenInvalid;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	private boolean hasBeenInvalid = false;
	private boolean validity = true;
	private int depth = 0;
}
