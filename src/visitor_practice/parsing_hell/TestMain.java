/**
 * TestMain
 *
 * Version 1.0.0
 *
 * Created on 06/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell;

import visitor_practice.parsing_hell.lexer.Lexeme;
import visitor_practice.parsing_hell.lexer.Lexer;
import visitor_practice.parsing_hell.parser.Parser;
import visitor_practice.parsing_hell.parser.expression_terms.Expression;
import visitor_practice.visitors.Evaluator;
import visitor_practice.visitors.EvaluatorBase;
import visitor_practice.visitors.PrettyPrinter;
import visitor_practice.visitors.Simplifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TestMain {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean simplify = false;
		Stack<Map<String, Expression>> contextStack = new Stack<>();
		while (true) {
			String source = reader.readLine();
			Preprocessor preprocessor = new Preprocessor();
			source = preprocessor.preprocess(source);
			Lexer lexer = new Lexer();
			lexer.lex(source);
			List<Lexeme> lexemes = lexer.getResult();
			Parser parser = new Parser(lexemes, source);
			Expression ast = parser.parse();
			Map<String, Expression> context = contextStack.empty()
					? new HashMap<String, Expression>()
					: contextStack.peek();
			EvaluatorBase visitor = simplify ? new Simplifier(context) : new Evaluator(context);
			Expression result = ast.accept(visitor);
			contextStack.push(visitor.context);
			PrettyPrinter printer = new PrettyPrinter();
			String resultString = result.accept(printer);
			System.out.println(resultString);
		}
	}
}
