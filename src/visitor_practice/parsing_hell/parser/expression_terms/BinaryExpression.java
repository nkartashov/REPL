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
	public final int signPosition;

	protected BinaryExpression(
			Expression left,
			Expression right,
			int start,
			int length,
			int signPosition) {
		super(start, length);
		this.left = left;
		this.right = right;
		this.signPosition = signPosition;
		setHasBeenValid(left.getHasBeenInvalid() || right.getHasBeenInvalid());
		setValidity(left.isValid() || right.isValid());
		setDepth(Math.max(left.getDepth(), right.getDepth()));
	}
}
