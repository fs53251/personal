package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class CircleWorker implements IWebWorker {

	@Override
	public synchronized void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(Color.red);
		g2d.setBackground(Color.black);

		int cx = bim.getWidth() / 2;
		int cy = bim.getHeight() / 2;
		int r = cx;
		int width = 2 * r;
		int height = 2 * r;

		g2d.fillOval(cx - r, cy - r, width, height);
		g2d.dispose();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			byte[] slika = bos.toByteArray();
			context.setMimeType("image/png");
			context.setContentLength((long) slika.length);
			context.write(slika);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
