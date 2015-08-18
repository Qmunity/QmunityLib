package uk.co.qmunity.lib.client.gui;

import java.awt.Color;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import uk.co.qmunity.lib.client.texture.CustomIcon;

public final class GuiTexture {

    public static GuiTexture blockTexture(IIcon itemIcon) {

        return texture(TextureMap.locationBlocksTexture, itemIcon);
    }

    public static GuiTexture itemTexture(IIcon itemIcon) {

        return texture(TextureMap.locationItemsTexture, itemIcon);
    }

    public static GuiTexture texture(String path) {

        return texture(path, CustomIcon.FULL_ICON);
    }

    public static GuiTexture texture(ResourceLocation resource) {

        return texture(resource, CustomIcon.FULL_ICON);
    }

    public static GuiTexture texture(String path, IIcon icon) {

        return texture(new ResourceLocation(path), icon);
    }

    public static GuiTexture texture(ResourceLocation resource, IIcon icon) {

        return new GuiTexture(resource, icon);
    }

    private final ResourceLocation texture;
    private final IIcon icon;
    private final int tint;

    public GuiTexture(ResourceLocation texture, IIcon icon, int tint) {

        this.texture = texture;
        this.icon = icon;
        this.tint = tint;
    }

    private GuiTexture(ResourceLocation texture, IIcon icon) {

        this(texture, icon, 0xFFFFFF);
    }

    public ResourceLocation getTexture() {

        return texture;
    }

    public IIcon getIcon() {

        return icon;
    }

    public int getTint() {

        return tint;
    }

    public GuiTexture withTint(double r, double g, double b) {

        return new GuiTexture(texture, icon, new Color((float) r, (float) g, (float) b).getRGB());
    }

    public GuiTexture withTint(int tint) {

        return new GuiTexture(texture, icon, tint);
    }

}
