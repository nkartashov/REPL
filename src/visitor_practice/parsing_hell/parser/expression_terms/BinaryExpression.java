package visitor_practice.parsing_hell.parser.expression_terms;

/**
 * BinaryExpression
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public abstract class BinaryExpression extends Expression {
	public final Expression left;
	public final Expression right;

	protected BinaryExpression(Expression left, Expression right, int start, int length) {
		super(start, length);
		this.left = left;
		this.right = right;
	}
}
