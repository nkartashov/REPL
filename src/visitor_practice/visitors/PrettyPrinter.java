package visitor_practice.visitors;

import visitor_practice.parsing_hell.parser.expression_terms.*;
import visitor_practice.parsing_hell.parser.expression_terms.Number;

/**
 * PrettyPrinter
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public class PrettyPrinter implements ExpressionVisitor<String> {
	public String visit(Number exp) {
		return Double.toString(exp.number.doubleValue());
	}

	public String visit(Division exp) {
		return exp.left.accept(this) + " / " + exp.right.accept(this);
	}

	@Override
	public String visit(Variable variable) {
		return variable.name;
	}

	@Override
	public String visit(Assignment assignment) {
		return assignment.variable + "=" + assignment.expression.accept(this);
	}

	public String visit(Multiplication exp) {
		return exp.left.accept(this) + "*" + exp.right.accept(this);
	}

	public String visit(Addition exp) {
		return exp.left.accept(this) + "+" + exp.right.accept(this);
	}

	@Override
	public String visit(Subtraction exp) {
		return exp.left.accept(this) + "-" + exp.right.accept(this);
	}
}
