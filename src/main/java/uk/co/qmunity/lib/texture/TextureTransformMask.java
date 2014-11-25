package uk.co.qmunity.lib.texture;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextureTransformMask implements ITextureTransform {

    private Texture mask;
    private boolean partialMask = true;
    private boolean invertMask = false;

    public TextureTransformMask(Texture mask) {

        this.mask = mask;
    }

    public TextureTransformMask(Texture mask, boolean partialMask) {

        this(mask);
        this.partialMask = partialMask;
    }

    public TextureTransformMask(Texture mask, boolean partialMask, boolean invertMask) {

        this(mask, partialMask);
        this.invertMask = invertMask;
    }

    @Override
    public Texture transform(Texture tex) {

        BufferedImage bi = tex.getImage();
        BufferedImage mask = this.mask.getImage();
        Graphics2D g = (Graphics2D) bi.getGraphics();

        for (int x = 0; x < bi.getWidth(); x++) {
            if (x >= mask.getWidth())
                continue;
            for (int y = 0; y < bi.getHeight(); y++) {
                if (y >= mask.getHeight())
                    continue;
                Object rasterData = bi.getRaster().getDataElements(x, y, null);
                int red = bi.getColorModel().getRed(rasterData);
                int green = bi.getColorModel().getGreen(rasterData);
                int blue = bi.getColorModel().getBlue(rasterData);
                int alpha = bi.getColorModel().getAlpha(rasterData);

                Object rasterDataMask = mask.getRaster().getDataElements(x, y, null);
                int alphaMask = mask.getColorModel().getAlpha(rasterDataMask);
                if (invertMask)
                    alphaMask = 255 - alphaMask;
                if (!partialMask)
                    alphaMask = (int) ((double) Math.round(alphaMask / 255D) * 255);

                alpha = (int) (alpha * (alphaMask / 255D));

                g.clearRect(x, y, 1, 1);
                g.setComposite(AlphaComposite.getInstance(alpha));
                g.setColor(new Color(red, green, blue));
                g.fillRect(x, y, 1, 1);
            }
        }

        return tex;
    }

}
