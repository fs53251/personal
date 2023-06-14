package hr.fer.zemris.scripting.nodes;

import hr.fer.zemris.scripting.elems.Element;
import hr.fer.zemris.scripting.elems.ElementVariable;

/**
 * Razred <code>ForLoopNode</code> predstavlja čvor koji reprezentira petlju FOR.
 * @author Filip
 *
 */
public class ForLoopNode extends Node {
	/**
	 * Varijabla petlje for
	 */
	ElementVariable variable;

	/**
	 * Početni izraz petlje
	 */
	Element startExpression;

	/**
	 * Krajnji izraz petlje
	 */
	Element endExpression;

	/**
	 * Varijabla koja čuva korak petlje for
	 */
	Element stepExpression;

	/**
	 * Konstruktor 
	 * 
	 * @param variable varijabla petlje
	 * @param startExpression početni izraz petlje
	 * @param endExpression krajnji izraz petlje
	 * @param stepExpression skok petlje
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * getter varijable petlje
	 * 
	 * @return ElementVariable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * getter varijable početnog izraza
	 * @return
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * getter varijable krajnjeg izraza 
	 * @return
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * getter varijable skoka
	 * @return
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * Nadjačana metoda za ispis
	 */
	@Override
	public String toString() {
		return "ForLoopNode [variable=" + variable + ", startExpression=" + startExpression + ", endExpression=" + endExpression + ", stepExpression=" + stepExpression + "]";
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
