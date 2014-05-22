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
		if (number.length == 0) {
			return null;
		}
		if (!number.isValid()) {
			addHighlight(number.start, number.length, "error");
			return null;
		}
		addHighlight(number.start, number.length, "number");
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
		if (!variable.isValid()) {
			addHighlight(variable.start, variable.length, "error");
			return null;
		}
		String style = "variable";
		if (evaluation && !context.containsKey(variable.name)) {
			style = "error";
		}
		addHighlight(variable.start, variable.length, style);
		return null;
	}

	@Override
	public Void visit(Assignment assignment) {
		assignment.variable.accept(this);
		assignment.expression.accept(this);
		return null;
	}

	public List<Highlight> getHighlights() {
		return highlights;
	}

	private Void visitBinaryExpression(BinaryExpression expression)
	{
		if (!expression.isValid()) {
			addHighlight(expression.left.start + expression.left.length + 1, 1, "error");
			return null;
		}
		expression.left.accept(this);
		String signStyle = "sign";
		if (!expression.left.isValid() || !expression.right.isValid()) {
			signStyle = "error";
		}
		addHighlight(expression.signPosition, 1, signStyle);
		expression.right.accept(this);
		return null;
	}

	private void addHighlight(int start, int length, String style) {
		highlights.add(new Highlight(start, length, style));
	}

	private List<Highlight> highlights = new ArrayList<>();
	private Map<String, Expression> context;
	private boolean evaluation;
}
