package cloud.aquacloud.aquacloudapi.function;

import cloud.aquacloud.aquacloudapi.type.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class GenerateImage {
    public static void generateImage(User user) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(640, 640,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        Font font = new Font("Arial Black", Font.BOLD, 350);
        g2d.setFont(font);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        GradientPaint gp = new GradientPaint(0, 0,
                Color.red, 0, 320, Color.black, true);


        g2d.setPaint(gp);
        g2d.fillRect(0, 0, 640, 640);

        g2d.setColor(new Color(255, 153, 0));

        FontMetrics metrics = g2d.getFontMetrics(font);
        Rectangle rect = new Rectangle(0, 0, 640, 640);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(String.valueOf(user.getUsername().charAt(0)))) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2d.drawString(String.valueOf(user.getUsername().charAt(0)), x, y);
        g2d.dispose();
        ImageIO.write(bufferedImage, "jpg", new File("images/" + user.getId() + ".jpg"));
    }

}
