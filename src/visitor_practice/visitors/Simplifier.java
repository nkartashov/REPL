/**
 * Simplifier
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

import java.util.Map;

public class Simplifier extends EvaluatorBase {
	public Simplifier() {
		super();
	}

	public Simplifier(Map<String, Expression> context) {
		super(context);
	}

	@Override
	public Expression visit(Variable variable) {
		if (!context.containsKey(variable.name)) {
			return variable;
		} else {
			return context.get(variable.name).accept(this);
		}
	}
}
