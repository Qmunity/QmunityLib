package uk.co.qmunity.lib.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.transform.Transformation;
import uk.co.qmunity.lib.transform.TransformationList;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec2d;
import uk.co.qmunity.lib.vec.Vec2dRect;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {

    public static final RenderHelper instance = new RenderHelper();

    private IBlockAccess world = null;
    private Vec3i location = new Vec3i(0, 0, 0);
    private LightingHelper lightingHelper = null;

    protected TransformationList transformations = new TransformationList();

    private Vec3d normal = new Vec3d(0, 0, 0);

    private int[] rotations = new int[] { 0, 0, 0, 0, 0, 0 };

    private boolean[] sides = new boolean[] { true, true, true, true, true, true };

    private boolean renderFromInside = false;

    private int color = 0xFFFFFF;
    private double opacity = 1;

    private IIcon overrideTexture = null;

    private ExtensionRendering renderingMethod = ExtensionRendering.SAME_TEXTURE;

    private boolean ignoreLighting = false;
    private int lightingOverride = 0;

    private Transformation vertexTransformation = null;

    public void reset() {

        setRenderCoords(null, 0, 0, 0);
        transformations.clear();
        setNormal(0, 0, 0);
        resetTextureRotations();
        resetRenderedSides();
        resetTransformations();
        renderFromInside = false;
        color = 0xFFFFFF;
        opacity = 1;
        renderingMethod = ExtensionRendering.SAME_TEXTURE;
        ignoreLighting = false;
        lightingOverride = 0;
        vertexTransformation = null;
    }

    public void fullReset() {

        reset();
        setOverrideTexture(null);
    }

    public void resetTextureRotations() {

        rotations = new int[] { 0, 0, 0, 0, 0, 0 };
    }

    public void resetRenderedSides() {

        sides = new boolean[] { true, true, true, true, true, true };
    }

    public void resetTransformations() {

        transformations.clear();
    }

    public void setOverrideTexture(IIcon texture) {

        overrideTexture = texture;
    }

    public IIcon getOverrideTexture() {

        return overrideTexture;
    }

    public void setRenderFromInside(boolean render) {

        renderFromInside = render;
    }

    public void setRenderCoords(IBlockAccess world, int x, int y, int z) {

        this.world = world;
        location = new Vec3i(x, y, z);
        if (world != null)
            lightingHelper = new LightingHelper(world, location);
    }

    public void addTransformation(Transformation transformation) {

        transformations.add(transformation);
    }

    public void removeTransformation() {

        if (transformations.size() > 0)
            transformations.remove(transformations.size() - 1);
    }

    public void removeTransformations(int amount) {

        for (int i = 0; i < amount && transformations.size() > 0; i++)
            transformations.remove(transformations.size() - 1);
    }

    public void setNormal(double x, double y, double z) {

        normal = new Vec3d(x, y, z);
    }

    public void setTextureRotation(ForgeDirection side, int times) {

        times %= 4;
        if (times < 0)
            times += 4;

        rotations[side.ordinal()] = times;
    }

    public void setTextureRotations(int down, int up, int west, int east, int north, int south) {

        setTextureRotation(ForgeDirection.DOWN, down);
        setTextureRotation(ForgeDirection.UP, up);
        setTextureRotation(ForgeDirection.WEST, west);
        setTextureRotation(ForgeDirection.EAST, east);
        setTextureRotation(ForgeDirection.NORTH, north);
        setTextureRotation(ForgeDirection.SOUTH, south);
    }

    public void setRenderSide(ForgeDirection side, boolean rendered) {

        sides[side.ordinal()] = rendered;
    }

    public void setRenderSides(boolean down, boolean up, boolean west, boolean east, boolean north, boolean south) {

        setRenderSide(ForgeDirection.DOWN, down);
        setRenderSide(ForgeDirection.UP, up);
        setRenderSide(ForgeDirection.WEST, west);
        setRenderSide(ForgeDirection.EAST, east);
        setRenderSide(ForgeDirection.NORTH, north);
        setRenderSide(ForgeDirection.SOUTH, south);
    }

    public void setColor(int color) {

        this.color = color;
    }

    public void setOpacity(double opacity) {

        this.opacity = opacity;
    }

    public int getColor() {

        return color;
    }

    public double getOpacity() {

        return opacity;
    }

    public void setRenderingMethod(ExtensionRendering renderingMethod) {

        if (renderingMethod != null)
            this.renderingMethod = renderingMethod;
    }

    public ExtensionRendering getRenderingMethod() {

        return renderingMethod;
    }

    public void setIgnoreLighting(boolean ignoreLighting) {

        this.ignoreLighting = ignoreLighting;
    }

    public void setLightingOverride(int lightingOverride) {

        this.lightingOverride = lightingOverride;
    }

    public void addVertex(double x, double y, double z) {

        addVertex(x, y, z, 0, 0);
    }

    public void addVertex(double x, double y, double z, double u, double v) {

        Vec3d vertex = new Vec3d(x, y, z);
        if (vertexTransformation != null)
            vertex = vertex.transform(vertexTransformation);
        vertex = vertex.transform(transformations);
        Vec3d normal = this.normal.clone().add(0.5, 0.5, 0.5).transform(transformations).sub(0.5, 0.5, 0.5);
        Vec3d normalTranslations = new Vec3d(0.5, 0.5, 0.5).transform(transformations).sub(0.5, 0.5, 0.5);
        normal.sub(normalTranslations);

        addVertex_do(vertex, normal, color, (int) (opacity * 255),
                world != null && lightingHelper != null ? (ignoreLighting ? lightingHelper.getFaceBrightness(lightingOverride, normal)
                        : lightingHelper.getVertexBrightness(vertex, normal)) : 0xF000F0, u, v);
    }

    public void addVertex_do(Vec3d vertex, Vec3d normal, int color, int opacity, int brightness, double u, double v) {

        if (brightness == -1) {
            brightness = world != null && lightingHelper != null ? (ignoreLighting ? lightingHelper.getFaceBrightness(lightingOverride,
                    normal) : lightingHelper.getVertexBrightness(vertex, normal)) : 0xF000F0;
        }

        Tessellator.instance.setTextureUV(u, v);
        Tessellator.instance.setBrightness(brightness);
        Tessellator.instance.setColorRGBA_I(color, opacity);
        Tessellator.instance.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());
        Tessellator.instance.addVertex(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    public Vec3i getLocation() {

        return location;
    }

    public IBlockAccess getWorld() {

        return world;
    }

    public void renderBox(Vec3dCube cube) {

        renderBox(cube, (IIcon) null);
    }

    public void renderBox(Vec3dCube cube, IIcon icon) {

        renderBox(cube, icon, icon, icon, icon, icon, icon);
    }

    public void renderBox(Vec3dCube cube, IIcon[] icons) {

        renderBox(cube, icons[0], icons[1], icons[2], icons[3], icons[4], icons[5]);
    }

    public void renderBox(Vec3dCube cube, IIcon down, IIcon up, IIcon west, IIcon east, IIcon north, IIcon south) {

        if (cube.getMinX() < 0 || cube.getMinY() < 0 || cube.getMinZ() < 0 || cube.getMaxX() > 1 || cube.getMaxY() > 1
                || cube.getMaxZ() > 1) {
            if (renderingMethod == ExtensionRendering.SAME_TEXTURE) {
                LightingHelper h = lightingHelper;

                for (Pair<Pair<Vec3dCube, Translation>, boolean[]> data : cube.splitInto1x1()) {
                    sides = data.getValue();
                    Translation tr = data.getKey().getValue();

                    if (tr.getX() == 0 && tr.getY() == 0 && tr.getZ() == 0) {
                        lightingHelper = h;
                    } else {
                        Vec3d t = new Vec3d(tr.getX(), tr.getY(), tr.getZ()).transform(transformations);
                        lightingHelper = new LightingHelper(getWorld(),
                                new Vec3i((int) t.getX(), (int) t.getY(), (int) t.getZ()).add(getLocation()));
                    }

                    vertexTransformation = tr;
                    renderBox_do(data.getKey().getKey(), down, up, west, east, north, south);
                    vertexTransformation = null;
                }
                resetRenderedSides();

                lightingHelper = h;
            }
        } else {
            renderBox_do(cube, down, up, west, east, north, south);
        }
    }

    public void renderBox_do(Vec3dCube cube, IIcon down, IIcon up, IIcon west, IIcon east, IIcon north, IIcon south) {

        Tessellator.instance.addTranslation(location.getX(), location.getY(), location.getZ());

        if (sides[ForgeDirection.WEST.ordinal()]) {
            setNormal(-1, 0, 0);
            renderFaceXNeg(cube.getFace(ForgeDirection.WEST), cube.getMinX(), west);
        }
        if (sides[ForgeDirection.EAST.ordinal()]) {
            setNormal(1, 0, 0);
            renderFaceXPos(cube.getFace(ForgeDirection.EAST), cube.getMaxX(), east);
        }
        if (sides[ForgeDirection.DOWN.ordinal()]) {
            setNormal(0, -1, 0);
            renderFaceYNeg(cube.getFace(ForgeDirection.DOWN), cube.getMinY(), down);
        }
        if (sides[ForgeDirection.UP.ordinal()]) {
            setNormal(0, 1, 0);
            renderFaceYPos(cube.getFace(ForgeDirection.UP), cube.getMaxY(), up);
        }
        if (sides[ForgeDirection.NORTH.ordinal()]) {
            setNormal(0, 0, -1);
            renderFaceZNeg(cube.getFace(ForgeDirection.NORTH), cube.getMinZ(), north);
        }
        if (sides[ForgeDirection.SOUTH.ordinal()]) {
            setNormal(0, 0, 1);
            renderFaceZPos(cube.getFace(ForgeDirection.SOUTH), cube.getMaxZ(), south);
        }

        Tessellator.instance.addTranslation(-location.getX(), -location.getY(), -location.getZ());
    }

    public void renderFaceXNeg(Vec2dRect face, double x, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(x, face.getMinX(), face.getMinY());
        Vec3d v2 = new Vec3d(x, face.getMinX(), face.getMaxY());
        Vec3d v3 = new Vec3d(x, face.getMaxX(), face.getMaxY());
        Vec3d v4 = new Vec3d(x, face.getMaxX(), face.getMinY());

        double a = rotations[ForgeDirection.WEST.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinY(), 1 - face.getMinX()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMaxY(), 1 - face.getMinX()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxY(), 1 - face.getMaxX()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMinY(), 1 - face.getMaxX()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    public void renderFaceXPos(Vec2dRect face, double x, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(x, face.getMinX(), face.getMinY());
        Vec3d v2 = new Vec3d(x, face.getMaxX(), face.getMinY());
        Vec3d v3 = new Vec3d(x, face.getMaxX(), face.getMaxY());
        Vec3d v4 = new Vec3d(x, face.getMinX(), face.getMaxY());

        double a = rotations[ForgeDirection.EAST.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinY(), 1 - face.getMinX()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMinY(), 1 - face.getMaxX()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxY(), 1 - face.getMaxX()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMaxY(), 1 - face.getMinX()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    public void renderFaceYNeg(Vec2dRect face, double y, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(face.getMinX(), y, face.getMinY());
        Vec3d v2 = new Vec3d(face.getMaxX(), y, face.getMinY());
        Vec3d v3 = new Vec3d(face.getMaxX(), y, face.getMaxY());
        Vec3d v4 = new Vec3d(face.getMinX(), y, face.getMaxY());

        double a = rotations[ForgeDirection.DOWN.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinX(), face.getMinY()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), face.getMinY()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), face.getMaxY()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMinX(), face.getMaxY()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    public void renderFaceYPos(Vec2dRect face, double y, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(face.getMinX(), y, face.getMinY());
        Vec3d v2 = new Vec3d(face.getMinX(), y, face.getMaxY());
        Vec3d v3 = new Vec3d(face.getMaxX(), y, face.getMaxY());
        Vec3d v4 = new Vec3d(face.getMaxX(), y, face.getMinY());

        double a = rotations[ForgeDirection.UP.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinX(), face.getMinY()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMinX(), face.getMaxY()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), face.getMaxY()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), face.getMinY()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    public void renderFaceZNeg(Vec2dRect face, double z, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(face.getMinX(), face.getMinY(), z);
        Vec3d v2 = new Vec3d(face.getMinX(), face.getMaxY(), z);
        Vec3d v3 = new Vec3d(face.getMaxX(), face.getMaxY(), z);
        Vec3d v4 = new Vec3d(face.getMaxX(), face.getMinY(), z);

        double a = rotations[ForgeDirection.NORTH.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinX(), 1 - face.getMinY()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMinX(), 1 - face.getMaxY()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), 1 - face.getMaxY()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), 1 - face.getMinY()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    public void renderFaceZPos(Vec2dRect face, double z, IIcon icon) {

        if (overrideTexture != null)
            icon = overrideTexture;
        if (icon == null)
            return;

        Vec3d v1 = new Vec3d(face.getMinX(), face.getMinY(), z);
        Vec3d v2 = new Vec3d(face.getMaxX(), face.getMinY(), z);
        Vec3d v3 = new Vec3d(face.getMaxX(), face.getMaxY(), z);
        Vec3d v4 = new Vec3d(face.getMinX(), face.getMaxY(), z);

        double a = rotations[ForgeDirection.SOUTH.ordinal()] * 90;

        Vec2d t1 = UVHelper.rotateUV(new Vec2d(face.getMinX(), 1 - face.getMinY()), a, icon);
        Vec2d t2 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), 1 - face.getMinY()), a, icon);
        Vec2d t3 = UVHelper.rotateUV(new Vec2d(face.getMaxX(), 1 - face.getMaxY()), a, icon);
        Vec2d t4 = UVHelper.rotateUV(new Vec2d(face.getMinX(), 1 - face.getMaxY()), a, icon);

        renderFace(v1, v2, v3, v4, t1, t2, t3, t4);
    }

    private void renderFace(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, Vec2d t1, Vec2d t2, Vec2d t3, Vec2d t4) {

        Vec3d normal = this.normal;

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            this.normal = new Vec3d(0, 0, 0).sub(normal);
        }

        addVertex(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());
        addVertex(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());
        addVertex(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());
        addVertex(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());

        this.normal = normal;
    }

    public void renderBakedModel(BakedModel model) {

        model.render(this);
    }
}
