package visitor_practice.visitors;

import visitor_practice.parsing_hell.parser.expression_terms.Expression;
import visitor_practice.parsing_hell.parser.expression_terms.Variable;

import java.util.Map;

/**
 * Evaluator
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public class Evaluator extends EvaluatorBase {
	public Evaluator() {
		super();
	}

	public Evaluator(Map<String, Expression> context) {
		super(context);
	}

	@Override
	public Expression visit(Variable variable)  {
		if (!context.containsKey(variable.name)) {
			throw new UnsupportedOperationException(variable.name);
		} else {
			return context.get(variable.name).accept(this);
		}
	}
}
