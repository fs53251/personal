package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji prima parametre a, b i računa vrijednost sin(x) i cos(x) za svaki x iz [a, b].
 * Servlet priprama samo model i stvara kolekciju parova (vrijednost x, (sin(x), cos(x))) koju postavlja kao 
 * parametar zahtjeva i generiranje sadržaja prepušta trigonometric.jsp.
 * @author Filip
 *
 */
@WebServlet(name = "trigonometric", urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Pair<Integer, List<Double>>> rez = new ArrayList<>();
		int a = 0;
		int b = 360;

		a = req.getParameter("a") == null ? a : Integer.parseInt(req.getParameter("a"));
		b = req.getParameter("b") == null ? b : Integer.parseInt(req.getParameter("b"));

		if(a > b) {
			int tmp = b;
			b = a;
			a = tmp;
		}

		if(b > a + 720) {
			b = a + 720;
		}

		for(int i = a; i <= b; i++) {
			List<Double> set = new ArrayList<>();
			set.add(Math.sin(Math.toRadians(i)));
			set.add(Math.cos(Math.toRadians(i)));
			rez.add(new Pair<Integer, List<Double>>(i, set));
		}

		req.setAttribute("rez", rez);

		req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

	public static class Pair<K, V> {
		K key;
		V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key);
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			Pair<K, V> other = (Pair<K, V>) obj;
			return Objects.equals(key, other.key);
		}
	}
}
