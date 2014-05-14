/**
 * HighlightingVisitor
 *
 * Version 1.0.0
 *
 * Created on 14/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.visitors;

import visitor_practice.parsing_hell.parser.expression_terms.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighlightingVisitor implements ExpressionVisitor<Void> {
	public HighlightingVisitor(Map<String, Expression> context, boolean evaluation) {
		this.context = context;
		this.evaluation = evaluation;
	}

	@Override
	public Void visit(visitor_practice.parsing_hell.parser.expression_terms.Number number) {
		highlights.add(new Highlight(number.start, number.length, "number"));
		return null;
	}

	@Override
	public Void visit(Addition addition) {
		return visitBinaryExpression(addition);
	}

	@Override
	public Void visit(Subtraction subtraction) {
		return visitBinaryExpression(subtraction);
	}

	@Override
	public Void visit(Multiplication multiplication) {
		return visitBinaryExpression(multiplication);
	}

	@Override
	public Void visit(Division division) {
		return visitBinaryExpression(division);
	}

	@Override
	public Void visit(Variable variable) {
		String style = "variable";
		if (evaluation && !context.containsKey(variable.name)) {
			style = "error";
		}
		highlights.add(new Highlight(variable.start, variable.length, style));
		return null;
	}

	@Override
	public Void visit(Assignment assignment) {
		highlights.add(new Highlight(assignment.start, assignment.variable.length(), "variable"));
		assignment.expression.accept(this);
		return null;
	}

	public List<Highlight> getHighlights() {
		return highlights;
	}

	private Void visitBinaryExpression(BinaryExpression expression)
	{
		expression.left.accept(this);
		highlights.add(new Highlight(expression.left.start + expression.left.length + 1, 1, "sign"));
		expression.right.accept(this);
		return null;
	}

	private List<Highlight> highlights = new ArrayList<>();
	private Map<String, Expression> context;
	private boolean evaluation;
}
