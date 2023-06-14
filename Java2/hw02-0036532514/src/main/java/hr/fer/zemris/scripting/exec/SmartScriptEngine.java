package hr.fer.zemris.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;

import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.scripting.elems.Element;
import hr.fer.zemris.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.scripting.elems.ElementFunction;
import hr.fer.zemris.scripting.elems.ElementOperator;
import hr.fer.zemris.scripting.elems.ElementString;
import hr.fer.zemris.scripting.elems.ElementVariable;
import hr.fer.zemris.scripting.elems.ElementsConstantInteger;
import hr.fer.zemris.scripting.nodes.DocumentNode;
import hr.fer.zemris.scripting.nodes.EchoNode;
import hr.fer.zemris.scripting.nodes.ForLoopNode;
import hr.fer.zemris.scripting.nodes.INodeVisitor;
import hr.fer.zemris.scripting.nodes.TextNode;
import hr.fer.zemris.scripting.parser.ArrayIndexedCollection;

public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();

	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ElementVariable varijabla = node.getVariable();
			varijabla.setValue(node.getStartExpression());
			multistack.push(varijabla);
			Element endValue = node.getEndExpression();
			ElementsConstantInteger step = null;
			if(node.getStepExpression() instanceof ElementsConstantInteger) {
				step = (ElementsConstantInteger) node.getStepExpression();
			}

			if(varijabla.getValue() instanceof ElementsConstantInteger && endValue instanceof ElementsConstantInteger) {
				while(((ElementsConstantInteger) varijabla.getValue()).getValue() <= ((ElementsConstantInteger) endValue).getValue()) {
					int broj = node.numberOfChildren();
					for(int i = 0; i < broj; i++) {
						node.getChild(i).accept(this);
					}

					varijabla = (ElementVariable) multistack.pop();
					ElementsConstantInteger integer = new ElementsConstantInteger(((ElementsConstantInteger) varijabla.getValue()).getValue() + step.getValue());
					varijabla.setValue((Element) integer);
					multistack.push(varijabla);
				}

				multistack.pop();
			} else {
				return;
			}

		}

		@Override
		public void visitEchoNode(EchoNode node) {
			ObjectMultistack tmpStack = new ObjectMultistack();
			for(Element element : node.getElements()) {
				if(element instanceof ElementConstantDouble) {
					ElementConstantDouble d = (ElementConstantDouble) element;
					tmpStack.push(d.getValue());
				} else if(element instanceof ElementsConstantInteger) {
					ElementsConstantInteger d = (ElementsConstantInteger) element;
					tmpStack.push(d.getValue());
				} else if(element instanceof ElementString) {
					ElementString d = (ElementString) element;
					tmpStack.push(d.getValue());
				} else if(element instanceof ElementVariable) {
					String name = ((ElementVariable) element).getName();
					ElementVariable fromStack = null;
					ArrayIndexedCollection lista = multistack.getElements();

					for(int i = 0; i < lista.size(); i++) {
						Object value = lista.get(i);
						if(value instanceof ElementVariable) {
							ElementVariable el = (ElementVariable) value;
							if(name.equals((el).getName())) {
								fromStack = el;
							}
						}
					}

					if(fromStack != null) {
						if(fromStack.getValue() instanceof ElementConstantDouble) {
							tmpStack.push(((ElementConstantDouble) fromStack.getValue()).getValue());
						} else if(fromStack.getValue() instanceof ElementsConstantInteger) {
							tmpStack.push(((ElementsConstantInteger) fromStack.getValue()).getValue());
						} else if(fromStack.getValue() instanceof ElementString) {
							tmpStack.push(((ElementString) fromStack.getValue()).getValue());
						}

					}
				} else if(element instanceof ElementOperator) {
					String symbol = ((ElementOperator) element).getSymbol();
					double first = 0.;
					double second = 0.;
					Object x;
					switch(symbol) {
						case "+":
							x = tmpStack.pop();
							if(x instanceof Double) {
								first = (double) x;
							} else if(x instanceof Integer) {
								first = (int) x;
							} else if(x instanceof String) {
								try {
									first = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									first = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							x = tmpStack.pop();
							if(x instanceof Double) {
								second = (double) x;
							} else if(x instanceof Integer) {
								second = (int) x;
							} else if(x instanceof String) {
								try {
									second = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									second = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							tmpStack.push(first + second);
							break;
						case "-":
							x = tmpStack.pop();
							if(x instanceof Double) {
								first = (double) x;
							} else if(x instanceof Integer) {
								first = (int) x;
							} else if(x instanceof String) {
								try {
									first = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									first = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							x = tmpStack.pop();
							if(x instanceof Double) {
								second = (double) x;
							} else if(x instanceof Integer) {
								second = (int) x;
							} else if(x instanceof String) {
								try {
									second = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									second = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}
							tmpStack.push(first - second);
							break;
						case "*":

							x = tmpStack.pop();
							if(x instanceof Double) {
								first = (double) x;
							} else if(x instanceof Integer) {
								first = (int) x;
							} else if(x instanceof String) {
								try {
									first = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									first = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							x = tmpStack.pop();
							if(x instanceof Double) {
								second = (double) x;
							} else if(x instanceof Integer) {
								second = (int) x;
							} else if(x instanceof String) {
								try {
									second = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									second = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}
							tmpStack.push(first * second);
							break;
						case "/":
							x = tmpStack.pop();
							if(x instanceof Double) {
								first = (double) x;
							} else if(x instanceof Integer) {
								first = (int) x;
							} else if(x instanceof String) {
								try {
									first = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									first = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							x = tmpStack.pop();
							if(x instanceof Double) {
								second = (double) x;
							} else if(x instanceof Integer) {
								second = (int) x;
							} else if(x instanceof String) {
								try {
									second = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									second = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}

							tmpStack.push(first / second);
							break;
						default:

							break;
					}
				} else if(element instanceof ElementFunction) {
					String function = ((ElementFunction) element).getName();

					Object x;
					switch(function) {
						case "sin":
							x = tmpStack.pop();
							double val = 0.;
							if(x instanceof Double) {
								val = (double) x;
							} else if(x instanceof Integer) {
								val = (int) x;
							} else if(x instanceof String) {
								try {
									val = Double.parseDouble((String) x);
								} catch(NumberFormatException e) {}

								try {
									val = Integer.parseInt((String) x);
								} catch(NumberFormatException e) {}
							}
							double radiani = val * Math.PI / 180;
							double sinResult = Math.sin(radiani);
							tmpStack.push(sinResult);

							break;
						case "decfmt":
							String f = (String) tmpStack.pop();
							Object obj = tmpStack.pop();
							String formatted = formatDecimal(obj, f);
							tmpStack.push(formatted);
							break;
						case "dup":
							Object top = tmpStack.pop();
							tmpStack.push(top);
							tmpStack.push(top);
							break;
						case "swap":
							Object a = tmpStack.pop();
							Object b = tmpStack.pop();
							tmpStack.push(a);
							tmpStack.push(b);
							break;
						case "setMimeType":
							String mimeType = (String) tmpStack.pop();
							requestContext.setMimeType(mimeType);
							break;
						case "paramGet":
							Object defValue = tmpStack.pop();
							String name = (String) tmpStack.pop();
							String paramValue = requestContext.getParameter(name);
							Object result = (paramValue == null) ? defValue : paramValue;
							tmpStack.push(result);
							break;
						case "pparamGet":
							defValue = tmpStack.pop();
							name = (String) tmpStack.pop();
							paramValue = requestContext.getPersistentParameter(name);
							result = (paramValue == null) ? defValue : paramValue;
							tmpStack.push(result);
							break;
						case "pparamSet":
							name = (String) tmpStack.pop();
							Object value = tmpStack.pop();

							requestContext.setPersistentParameter(name, value.toString());
							break;
						case "pparamDel":
							name = (String) tmpStack.pop();
							requestContext.removePersistentParameter(name);
							break;
						case "tparamGet":
							defValue = tmpStack.pop();
							name = (String) tmpStack.pop();
							paramValue = requestContext.getTemporaryParameter(name);
							result = (paramValue == null) ? defValue : paramValue;
							tmpStack.push(result);
							break;
						case "tparamSet":

							name = (String) tmpStack.pop();
							value = tmpStack.pop();
							requestContext.setTemporaryParameters(name, value.toString());
							break;
						case "tparamDel":
							name = (String) tmpStack.pop();
							requestContext.removeTemporaryParameter(name);
							break;
						default:
							break;
					}
				}
			}

			ArrayIndexedCollection lista = tmpStack.getElements();
			tmpStack = new ObjectMultistack();
			for(int i = 0; i < lista.size(); i++) {
				Object el = lista.get(i);
				String strValue = null;
				try {
					if(el.toString().endsWith(".0")) {
						strValue = el.toString().substring(0, el.toString().lastIndexOf(".0"));
					}
					if(strValue != null) {
						requestContext.write(strValue);
					} else {
						requestContext.write((el.toString()));
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int broj = node.numberOfChildren();
			for(int i = 0; i < broj; i++) {
				node.getChild(i).accept(this);
			}
		}
	};

	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	public void execute() {
		documentNode.accept(visitor);
	}

	private String formatDecimal(Object obj, String format) {
		if(obj instanceof Integer) {
			int value = (Integer) obj;
			DecimalFormat decimalFormat = new DecimalFormat(format);
			return decimalFormat.format(value);
		} else if(obj instanceof Double) {
			double value = (Double) obj;
			DecimalFormat decimalFormat = new DecimalFormat(format);
			return decimalFormat.format(value);
		} else if(obj instanceof String) {
			String value = (String) obj;
			double doubleValue = Double.parseDouble(value);
			DecimalFormat decimalFormat = new DecimalFormat(format);
			return decimalFormat.format(doubleValue);
		} else {
			throw new IllegalArgumentException("Invalid argument type for decfmt: " + obj.getClass().getName());
		}
	}
}
