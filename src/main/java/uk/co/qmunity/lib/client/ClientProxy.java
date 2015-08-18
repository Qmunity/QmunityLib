package uk.co.qmunity.lib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import uk.co.qmunity.lib.CommonProxy;
import uk.co.qmunity.lib.client.helper.InputHelper;
import uk.co.qmunity.lib.client.helper.ShaderHelper;
import uk.co.qmunity.lib.client.render.RenderHooks;
import uk.co.qmunity.lib.client.render.RenderMultipart;
import uk.co.qmunity.lib.tile.TileMultipart;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        RenderHooks.initRenderHooks();
        ShaderHelper.initShaders();

        RenderMultipart multipartRenderer = new RenderMultipart();
        RenderMultipart.RENDER_ID = RenderHooks.registerStaticRenderer(multipartRenderer);
        RenderHooks.registerTileEntityRenderer(TileMultipart.class, multipartRenderer);

        FMLCommonHandler.instance().bus().register(new InputHelper());
    }

    @Override
    public World getWorld() {

        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public boolean isShiftDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    @Override
    public boolean isCtrlDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    @Override
    public boolean isAltDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    private float frame = 0;

    @Override
    public float getFrame() {

        return frame;
    }

    @Override
    public void setFrame(float frame) {

        this.frame = frame;
    }

}
