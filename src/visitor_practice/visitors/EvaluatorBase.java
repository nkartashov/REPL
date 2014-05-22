/**
 * EvaluatorBase
 *
 * Version 1.0.0
 *
 * Created on 08/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.visitors;

import visitor_practice.parsing_hell.parser.expression_terms.*;
import visitor_practice.parsing_hell.parser.expression_terms.Number;

import java.util.HashMap;
import java.util.Map;

public abstract class EvaluatorBase implements ExpressionVisitor<Expression> {
	public final Map<String, Expression> context;

	public EvaluatorBase() {
		this.context = new HashMap<>();
	}

	public EvaluatorBase(Map<String, Expression> context) {
		this.context = context;
	}

	@Override
	public Expression visit(visitor_practice.parsing_hell.parser.expression_terms.Number number) {
		return number;
	}

	@Override
	public Expression visit(Addition addition) {
		Expression left = addition.left.accept(this);
		Expression right = addition.right.accept(this);
		if (left instanceof Number && right instanceof Number) {
			double result = ((Number) left).number.doubleValue() + ((Number) right).number.doubleValue();
			return new Number(result, left.start, left.length + right.length + 1);
		} else {
			return new Addition(
					left,
					right,
					left.start,
					left.length + right.length + 1,
					addition.signPosition);
		}
	}

	@Override
	public Expression visit(Subtraction subtraction) {
		Expression left = subtraction.left.accept(this);
		Expression right = subtraction.right.accept(this);
		if (left instanceof Number && right instanceof Number) {
			double result = ((Number) left).number.doubleValue() - ((Number) right).number.doubleValue();
			return new Number(result, left.start, left.length + right.length + 1);
		} else {
			return new Subtraction(
					left,
					right,
					left.start,
					left.length + right.length + 1,
					subtraction.signPosition);
		}
	}

	@Override
	public Expression visit(Multiplication multiplication) {
		Expression left = multiplication.left.accept(this);
		Expression right = multiplication.right.accept(this);
		if (left instanceof Number && right instanceof Number) {
			double result = ((Number) left).number.doubleValue() * ((Number) right).number.doubleValue();
			return new Number(result, left.start, left.length + right.length + 1);
		} else {
			return new Multiplication(
					left,
					right,
					left.start,
					left.length + right.length + 1,
					multiplication.signPosition);
		}
	}

	@Override
	public Expression visit(Division division) {
		Expression left = division.left.accept(this);
		Expression right = division.right.accept(this);
		if (left instanceof Number && right instanceof Number) {
			double result = ((Number) left).number.doubleValue() / ((Number) right).number.doubleValue();
			return new Number(result, left.start, left.length + right.length + 1);
		} else {
			return new Division(
					left,
					right,
					left.start,
					left.length + right.length + 1,
					division.signPosition);
		}
	}

	@Override
	public Expression visit(Assignment assignment) {
		Expression expression = assignment.expression.accept(this);
		context.put(assignment.variable.name, expression);
		return expression;
	}
}
