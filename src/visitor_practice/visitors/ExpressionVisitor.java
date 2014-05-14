package visitor_practice.visitors;

import visitor_practice.parsing_hell.parser.expression_terms.*;
import visitor_practice.parsing_hell.parser.expression_terms.Number;

/**
 * ExpressionVisitor
 *
 * Version 1.0.0
 *
 * Created on 16/04/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */

public  interface ExpressionVisitor<T> {
	T visit(Number number);
	T visit(Addition addition);
	T visit(Subtraction subtraction);
	T visit(Multiplication multiplication);
	T visit(Division division);
	T visit(Variable variable);
	T visit(Assignment assignment);
}
