package com.qmunity.lib.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureUtil;

import org.lwjgl.opengl.GL11;

public class Texture {

    private int textureId = -1;
    private BufferedImage bi;

    public Texture(URL input) throws IOException {

        bi = ImageIO.read(input);
    }

    public BufferedImage getImage() {

        return bi;
    }

    public int getGLTextureId() {

        if (textureId == -1)
            textureId = TextureUtil.uploadTextureImage(GL11.glGenTextures(), getImage());

        return textureId;
    }

    public void bind() {

        int tex = getGLTextureId();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
    }

}
