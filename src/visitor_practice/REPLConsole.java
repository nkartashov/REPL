package visitor_practice;

import visitor_practice.parsing_hell.lexer.Lexeme;
import visitor_practice.parsing_hell.lexer.Lexer;
import visitor_practice.parsing_hell.parser.Parser;
import visitor_practice.parsing_hell.parser.expression_terms.Expression;
import visitor_practice.visitors.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class REPLConsole {

	private JFrame frame;
	private JComboBox<String> optionPane;
	private UndoManager undoManager = new UndoManager();


	public static final String SIMPLIFY = "Simplify";
	public static final String EVALUATE = "Evaluate";
	public static final String GREETING = System.lineSeparator() + ">";
	private Stack<Map<String, Expression>> contextStack = new Stack<>();

	private Tuple<Expression, Integer> buildAST(String source) {
		Lexer lexer = new Lexer();
		lexer.lex(source);
		Parser parser = new Parser(lexer.getResult(), source);
		Expression result = parser.parse();
		return new Tuple<>(result, parser.getLastParsedSymbol());
	}

	private StyledDocument setupStyles() {
		StyledDocument styledDocument = new DefaultStyledDocument();
		Style base = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style def = styledDocument.addStyle("default", base);
		StyleConstants.setForeground(def, Color.BLACK);
		Style variable = styledDocument.addStyle("variable", base);
		StyleConstants.setForeground(variable, Color.BLUE);
		StyleConstants.setBold(variable, true);
		Style number = styledDocument.addStyle("number", base);
		StyleConstants.setForeground(number, Color.ORANGE);
		Style sign = styledDocument.addStyle("sign", base);
		StyleConstants.setForeground(sign, Color.GREEN);
		Style error = styledDocument.addStyle("error", base);
		StyleConstants.setForeground(error, Color.RED);
		StyleConstants.setUnderline(error, true);
		return styledDocument;
	}

	private void init() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setLayout(new BorderLayout());

		optionPane = new JComboBox<>();
		optionPane.addItem(SIMPLIFY);
		optionPane.addItem(EVALUATE);
		frame.add(optionPane, "North");

		final JTextPane textArea = new JTextPane(setupStyles());
		final AbstractDocument document = (AbstractDocument) textArea.getDocument();
		document.addUndoableEditListener(undoManager);
		document.setDocumentFilter(new Filter((StyledDocument) document));
		textArea.setText("Welcome to REPL Console! " + System.lineSeparator() + ">");
		textArea.setEditable(true);
		frame.add(textArea, "Center");
		undoManager.discardAllEdits();
		textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control Z"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canUndo()) {
					undoManager.undo();
				}
			}
		});

		textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control shift Z"),
				new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (contextStack.empty()) {
							return;
						}
						undo();
						removeLines();
					}

					private void removeLines() {
						try {
							String text = document.getText(0, document.getLength());
							int prevToLastLineIndex = text.lastIndexOf(System.lineSeparator());
							text = text.substring(0, prevToLastLineIndex);
							int indexToTrim = text.lastIndexOf('>') + 1;

							document.setDocumentFilter(null);
							document.remove(indexToTrim, document.getLength() - indexToTrim);
							document.setDocumentFilter(new Filter((StyledDocument) document));
						} catch (BadLocationException e1) {
							//Ignored
						}
					}

					private void undo() {
						contextStack.pop();
					}
				});
		textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JEditorPane source = (JEditorPane) e.getSource();
				Document document = source.getDocument();
				try {
					String text = document.getText(0, document.getLength());
					String userInput = text.substring(lastLineIndex(document) + GREETING.length());
					Expression ast = buildAST(userInput).fst;
					Map<String, Expression> context = getContext();
					EvaluatorBase visitor = simplifyMode() ? new Simplifier(context) : new Evaluator(context);
					String resultToPrint = userInput;
					try {
						Expression resultExpression = ast.accept(visitor);
						renewContext(visitor.context);
						if (resultExpression != null) {
							PrettyPrinter printer = new PrettyPrinter();
							resultToPrint = resultExpression.accept(printer);
						}
					} catch (UnsupportedOperationException ex) {
						resultToPrint = "ERROR: Variable " + ex.getMessage() + " is not in context";
					}
					document.insertString(endOffset(document), System.lineSeparator() + resultToPrint, null);
					document.insertString(endOffset(document), GREETING, null);
					source.setCaretPosition(endOffset(document));
					undoManager.discardAllEdits();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}

			private void renewContext(Map<String, Expression> newContext) {
				contextStack.push(newContext);
			}

			private int endOffset(Document document) {
				return document.getEndPosition().getOffset() - 1;
			}

			public boolean simplifyMode() {
				return SIMPLIFY.equals(optionPane.getSelectedItem());
			}
		});

		frame.setVisible(true);
		frame.setSize(500, 300);
	}

	private Map<String, Expression> getContext() {
		Map<String, Expression> result = new HashMap<>();
		if (!contextStack.empty()) {
			result.putAll(contextStack.peek());
		}
		return result;
	}

	public static void main(String[] args) {
		REPLConsole replConsole = new REPLConsole();
		replConsole.init();
	}

	private class Filter extends DocumentFilter {
		public Filter(StyledDocument styledDocument) {
			this.styledDocument = styledDocument;
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			if (cursorOnLastLine(offset, fb)) {
				super.insertString(fb, offset, string, attr);
				highlightInput();
			}
		}

		public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
			if (offset > lastLineIndex(fb.getDocument()) + 1) {
				super.remove(fb, offset, length);
				highlightInput();
			}
		}

		public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
				throws BadLocationException {
			if (cursorOnLastLine(offset, fb)) {
				super.replace(fb, offset, length, text, attrs);
				highlightInput();
			}
		}

		public void highlightInput() throws BadLocationException {
			styledDocument.removeUndoableEditListener(undoManager);
			String inputText = styledDocument.getText(0, styledDocument.getLength());
			if (inputText.length() == 0) {
				return;
			}
			if (inputText.lastIndexOf(System.lineSeparator()) > inputText.lastIndexOf('>'))
				return;
			highlightDefault(inputText);
			highlightBasedOnLexer(inputText);
			highlightBasedOnParser(inputText);
			styledDocument.addUndoableEditListener(undoManager);
		}

		private void highlightBasedOnLexer(String inputText) throws BadLocationException {
			int start = lastLineIndex(styledDocument) + GREETING.length();
			String userInput = inputText.substring(start);
			Map<String, Expression> context = getContext();
			Lexer lexer = new Lexer();
			lexer.lex(userInput);
			List<Lexeme> lexemes = lexer.getResult();
			List<Highlight> highlights = HighlightTraverser.highlight(lexemes);
			for (int i = 0; i < highlights.size(); i++) {
				Highlight highlight = highlights.get(i);
				Lexeme lexeme = lexemes.get(i);
				String style = highlight.style;
				if (style.equals("variable")
						&& !simplifyMode()
						&& !context.containsKey(userInput.substring(
						lexeme.start,
						lexeme.start + lexeme.length))) {
					style = "error";
				}
				styledDocument.setCharacterAttributes(
						start + highlight.start, highlight.length,
						styledDocument.getStyle(style), true);
			}
		}

		private void highlightBasedOnParser(String inputText) throws BadLocationException {
			int start = lastLineIndex(styledDocument) + GREETING.length();
			String userInput = inputText.substring(start);
			Map<String, Expression> context = getContext();
			Tuple<Expression, Integer> parseTuple = buildAST(userInput);
			Expression ast = parseTuple.fst;
			int lastParsedSymbol = 0;
			if (ast != null)
			{
				lastParsedSymbol = parseTuple.snd;
				HighlightingVisitor highlightingVisitor = new HighlightingVisitor(context, !simplifyMode());
				List<Highlight> highlights = highlightingVisitor.getHighlights();
				for (Highlight highlight: highlights) {
					styledDocument.setCharacterAttributes(
							start + highlight.start, highlight.length,
							styledDocument.getStyle(highlight.style), true);
				}
			}
			highlightError(inputText, lastParsedSymbol);
		}

		private void highlightError(String inputText, int startingFrom) throws BadLocationException {
			int start = lastLineIndex(styledDocument) + GREETING.length();
			String userInput = inputText.substring(start);
			styledDocument.setCharacterAttributes(
					start + startingFrom,
					userInput.length() - (startingFrom - start),
					styledDocument.getStyle("error"), true);
		}

		private void highlightDefault(String inputText) throws BadLocationException {
			int start = lastLineIndex(styledDocument) + GREETING.length();
			String userInput = inputText.substring(start);
			styledDocument.setCharacterAttributes(
					start, userInput.length(), styledDocument.getStyle("default"), true);
		}

		public boolean simplifyMode() {
			return SIMPLIFY.equals(optionPane.getSelectedItem());
		}

		private final StyledDocument styledDocument;
	}

	private static boolean cursorOnLastLine(int offset, DocumentFilter.FilterBypass fb) {
		return cursorOnLastLine(offset, fb.getDocument());
	}

	private static boolean cursorOnLastLine(int offset, Document document) {
		int lastLineIndex;
		try {
			lastLineIndex = lastLineIndex(document);
		} catch (BadLocationException e) {
			return false;
		}
		return offset > lastLineIndex;
	}

	public static int lastLineIndex(Document document) throws BadLocationException {
		return document.getText(0, document.getLength()).lastIndexOf(System.lineSeparator());
	}
}