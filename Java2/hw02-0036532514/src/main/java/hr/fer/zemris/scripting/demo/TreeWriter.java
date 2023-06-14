package hr.fer.zemris.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.scripting.elems.Element;
import hr.fer.zemris.scripting.elems.ElementFunction;
import hr.fer.zemris.scripting.elems.ElementString;
import hr.fer.zemris.scripting.nodes.DocumentNode;
import hr.fer.zemris.scripting.nodes.EchoNode;
import hr.fer.zemris.scripting.nodes.ForLoopNode;
import hr.fer.zemris.scripting.nodes.INodeVisitor;
import hr.fer.zemris.scripting.nodes.TextNode;
import hr.fer.zemris.scripting.parser.SmartScriptParser;

public class TreeWriter {

	public static void main(String[] args) {
		String docBody = null;
		try {
			docBody = Files.readString(Paths.get(args[0]));
		} catch(IOException e) {
			e.printStackTrace();
		}
		SmartScriptParser p = new SmartScriptParser(docBody);

		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);

	}

	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			String text = node.getText();

			System.out.printf(text);
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			int broj = node.numberOfChildren();
			StringBuilder sb = new StringBuilder();

			sb.append("{$FOR ");
			sb.append(node.getVariable().asText() + " ");
			sb.append(node.getStartExpression().asText() + " ");
			sb.append(node.getEndExpression().asText() + " ");
			if(node.getStepExpression() != null)
				sb.append(node.getStepExpression().asText());
			sb.append(" $}");

			System.out.printf(sb.toString());
			for(int i = 0; i < broj; i++) {
				node.getChild(i).accept(this);
			}
			sb = new StringBuilder();
			sb.append("{$END$}");
			String text = sb.toString();

			System.out.printf(text);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Element[] lista = node.getElements();
			StringBuilder sb = new StringBuilder();
			sb.append("{$= ");
			for(int i = 0; i < lista.length; i++) {
				if(lista[i] instanceof ElementString) {
					sb.append('\"' + lista[i].asText() + "\" ");
				} else if(lista[i] instanceof ElementFunction) {
					sb.append('@' + lista[i].asText() + " ");
				} else {
					sb.append(lista[i].asText() + " ");
				}

			}
			sb.append("$}");
			String text = sb.toString();

			System.out.printf(text);
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int broj = node.numberOfChildren();
			for(int i = 0; i < broj; i++) {
				node.getChild(i).accept(this);
			}
		}

	}
}
