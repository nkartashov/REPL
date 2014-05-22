package visitor_practice.parsing_hell.parser.expression_terms;

import visitor_practice.visitors.ExpressionVisitor;



/**
 * Assignment
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public class Assignment extends Expression {
	public final Variable variable;
	public final Expression expression;
	public final int signPosition;

	public Assignment(
			Expression variable,
			Expression expression,
			int start,
			int length,
			int signPosition) {
		super(start, length);
		this.variable = (Variable) variable;
		this.expression = expression;
		this.signPosition = signPosition;
		setHasBeenValid(variable.getHasBeenInvalid() || expression.getHasBeenInvalid());
		setValidity(variable.isValid() || variable.isValid());
		setDepth(Math.max(variable.getDepth(), expression.getDepth()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
