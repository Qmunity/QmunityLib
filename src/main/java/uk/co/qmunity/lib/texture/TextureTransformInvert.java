package uk.co.qmunity.lib.texture;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextureTransformInvert implements ITextureTransform {

    private int transformedComponents;

    public TextureTransformInvert(int components) {

        transformedComponents = components;
    }

    @Override
    public Texture transform(Texture tex) {

        BufferedImage bi = tex.getImage();
        Graphics2D g = (Graphics2D) bi.getGraphics();

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Object rasterData = bi.getRaster().getDataElements(x, y, null);
                int red = bi.getColorModel().getRed(rasterData);
                int green = bi.getColorModel().getGreen(rasterData);
                int blue = bi.getColorModel().getBlue(rasterData);
                int alpha = bi.getColorModel().getAlpha(rasterData);

                if (TransformableParameters.contains(transformedComponents, TransformableParameters.RED))
                    red = 256 - red;
                if (TransformableParameters.contains(transformedComponents, TransformableParameters.GREEN))
                    green = 256 - green;
                if (TransformableParameters.contains(transformedComponents, TransformableParameters.BLUE))
                    blue = 256 - blue;
                if (TransformableParameters.contains(transformedComponents, TransformableParameters.ALPHA))
                    alpha = 256 - alpha;

                g.clearRect(x, y, 1, 1);
                g.setComposite(AlphaComposite.getInstance(alpha));
                g.setColor(new Color(red, green, blue));
                g.fillRect(x, y, 1, 1);
            }
        }

        return tex;
    }

}
