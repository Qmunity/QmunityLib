package uk.co.qmunity.lib.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Map3D;
import uk.co.qmunity.lib.transform.Transformation;
import uk.co.qmunity.lib.transform.TransformationList;
import uk.co.qmunity.lib.vec.Vec2d;
import uk.co.qmunity.lib.vec.Vec2dRect;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

public class RenderHelper {

    public static final RenderHelper instance = new RenderHelper();

    private IBlockAccess world = null;
    private Vec3i location = new Vec3i(0, 0, 0);

    private TransformationList transformations = new TransformationList();

    private Vec3d normal = new Vec3d(0, 0, 0);

    private int[] rotations = new int[] { 0, 0, 0, 0, 0, 0 };

    private boolean[] sides = new boolean[] { true, true, true, true, true, true };

    private Map3D<Integer> lightmap = new Map3D<Integer>(3, 3, 3, 0);

    private boolean renderFromInside = false;

    private int color = 0xFFFFFF;

    public void reset() {

        setRenderCoords(null, 0, 0, 0);
        transformations.clear();
        setNormal(0, 0, 0);
        resetTextureRotations();
        resetRenderedSides();
        resetTransformations();
        lightmap = new Map3D<Integer>(3, 3, 3, 0);
        renderFromInside = false;
        color = 0xFFFFFF;
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

    public void setRenderFromInside(boolean render) {

        renderFromInside = render;
    }

    private int getMixedBrightnessForBlock(int x, int y, int z) {

        if (world == null)
            return 0xFF00FF;

        return world.getBlock(location.getX() + x, location.getY() + y, location.getZ() + z).getMixedBrightnessForBlock(world,
                location.getX() + x, location.getY() + y, location.getZ() + z);
    }

    public void setRenderCoords(IBlockAccess world, int x, int y, int z) {

        this.world = world;
        location = new Vec3i(x, y, z);

        for (int x_ = -1; x_ <= 1; x_++) {
            for (int y_ = -1; y_ <= 1; y_++) {
                for (int z_ = -1; z_ <= 1; z_++) {
                    if (world != null) {
                        setLightValue(x_, y_, z_, getMixedBrightnessForBlock(x_, y_, z_));
                    } else {
                        setLightValue(x_, y_, z_, 0xFF00FF);
                    }
                }
            }
        }
    }

    public void addTransformation(Transformation transformation) {

        transformations.add(transformation);
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

    private void setLightValue(int x, int y, int z, int value) {

        lightmap.set(x + 1, y + 1, z + 1, value);
    }

    @SuppressWarnings("unused")
    private void addVertex(double x, double y, double z, double u, double v) {

        setTextureCoords(u, v);
        addVertex(x, y, z);
    }

    private void setTextureCoords(double u, double v) {

        Tessellator.instance.setTextureUV(u, v);
    }

    private void addVertex(double x, double y, double z) {

        Vec3d vertex = new Vec3d(x, y, z).transform(transformations);

        Vec3d normal = this.normal.transform(transformations);

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

        renderBox(cube, null);
    }

    public void renderBox(Vec3dCube cube, IIcon icon) {

        renderBox(cube, icon, icon, icon, icon, icon, icon);
    }

    public void renderBox(Vec3dCube cube, IIcon down, IIcon up, IIcon west, IIcon east, IIcon north, IIcon south) {

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

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(x, face.getMinX(), face.getMinY()).transform(transformations);
        Vec3d v2 = new Vec3d(x, face.getMinX(), face.getMaxY()).transform(transformations);
        Vec3d v3 = new Vec3d(x, face.getMaxX(), face.getMaxY()).transform(transformations);
        Vec3d v4 = new Vec3d(x, face.getMaxX(), face.getMinY()).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMinY() * 16), icon.getInterpolatedV((1 - face.getMinX()) * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMaxY() * 16), icon.getInterpolatedV((1 - face.getMinX()) * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMaxY() * 16), icon.getInterpolatedV((1 - face.getMaxX()) * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMinY() * 16), icon.getInterpolatedV((1 - face.getMaxX()) * 16));

        for (int i = 0; i < rotations[ForgeDirection.WEST.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }

    public void renderFaceXPos(Vec2dRect face, double x, IIcon icon) {

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(x, face.getMinX(), face.getMinY()).transform(transformations);
        Vec3d v2 = new Vec3d(x, face.getMaxX(), face.getMinY()).transform(transformations);
        Vec3d v3 = new Vec3d(x, face.getMaxX(), face.getMaxY()).transform(transformations);
        Vec3d v4 = new Vec3d(x, face.getMinX(), face.getMaxY()).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMaxY() * 16), icon.getInterpolatedV((1 - face.getMinX()) * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMaxY() * 16), icon.getInterpolatedV((1 - face.getMaxX()) * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMinY() * 16), icon.getInterpolatedV((1 - face.getMaxX()) * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMinY() * 16), icon.getInterpolatedV((1 - face.getMinX()) * 16));

        for (int i = 0; i < rotations[ForgeDirection.EAST.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }

    public void renderFaceYNeg(Vec2dRect face, double y, IIcon icon) {

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(face.getMinX(), y, face.getMinY()).transform(transformations);
        Vec3d v2 = new Vec3d(face.getMaxX(), y, face.getMinY()).transform(transformations);
        Vec3d v3 = new Vec3d(face.getMaxX(), y, face.getMaxY()).transform(transformations);
        Vec3d v4 = new Vec3d(face.getMinX(), y, face.getMaxY()).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV(face.getMinY() * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV(face.getMaxY() * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV(face.getMaxY() * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV(face.getMinY() * 16));

        for (int i = 0; i < rotations[ForgeDirection.DOWN.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }

    public void renderFaceYPos(Vec2dRect face, double y, IIcon icon) {

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(face.getMinX(), y, face.getMinY()).transform(transformations);
        Vec3d v2 = new Vec3d(face.getMinX(), y, face.getMaxY()).transform(transformations);
        Vec3d v3 = new Vec3d(face.getMaxX(), y, face.getMaxY()).transform(transformations);
        Vec3d v4 = new Vec3d(face.getMaxX(), y, face.getMinY()).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV(face.getMinY() * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV(face.getMaxY() * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV(face.getMaxY() * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV(face.getMinY() * 16));

        for (int i = 0; i < rotations[ForgeDirection.UP.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }

    public void renderFaceZNeg(Vec2dRect face, double z, IIcon icon) {

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(face.getMinX(), face.getMinY(), z).transform(transformations);
        Vec3d v2 = new Vec3d(face.getMinX(), face.getMaxY(), z).transform(transformations);
        Vec3d v3 = new Vec3d(face.getMaxX(), face.getMaxY(), z).transform(transformations);
        Vec3d v4 = new Vec3d(face.getMaxX(), face.getMinY(), z).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV((1 - face.getMinY()) * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV((1 - face.getMaxY()) * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV((1 - face.getMaxY()) * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV((1 - face.getMinY()) * 16));

        for (int i = 0; i < rotations[ForgeDirection.NORTH.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }

    public void renderFaceZPos(Vec2dRect face, double z, IIcon icon) {

        LightingHelper.loadLightmap(lightmap);

        Vec3d normal = this.normal.clone().transform(transformations);

        Vec3d v1 = new Vec3d(face.getMinX(), face.getMinY(), z).transform(transformations);
        Vec3d v2 = new Vec3d(face.getMaxX(), face.getMinY(), z).transform(transformations);
        Vec3d v3 = new Vec3d(face.getMaxX(), face.getMaxY(), z).transform(transformations);
        Vec3d v4 = new Vec3d(face.getMinX(), face.getMaxY(), z).transform(transformations);

        Vec2d t1 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV((1 - face.getMinY()) * 16));
        Vec2d t2 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV((1 - face.getMinY()) * 16));
        Vec2d t3 = new Vec2d(icon.getInterpolatedU(face.getMaxX() * 16), icon.getInterpolatedV((1 - face.getMaxY()) * 16));
        Vec2d t4 = new Vec2d(icon.getInterpolatedU(face.getMinX() * 16), icon.getInterpolatedV((1 - face.getMaxY()) * 16));

        for (int i = 0; i < rotations[ForgeDirection.SOUTH.ordinal()]; i++) {
            Vec2d v = t1;
            t1 = t2;
            t2 = t3;
            t3 = t4;
            t4 = v;
        }

        if (renderFromInside) {
            Vec3d v = v2;
            v2 = v4;
            v4 = v;
            Vec2d t = t2;
            t2 = t4;
            t4 = t;
            normal = new Vec3d(0, 0, 0).sub(normal);
        }

        Tessellator t = Tessellator.instance;

        t.setColorOpaque_I(color);
        t.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v1, normal));
        t.addVertexWithUV(v1.getX(), v1.getY(), v1.getZ(), t1.getX(), t1.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v2, normal));
        t.addVertexWithUV(v2.getX(), v2.getY(), v2.getZ(), t2.getX(), t2.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v3, normal));
        t.addVertexWithUV(v3.getX(), v3.getY(), v3.getZ(), t3.getX(), t3.getY());

        t.setBrightness(LightingHelper.getBrightnessForVertex(v4, normal));
        t.addVertexWithUV(v4.getX(), v4.getY(), v4.getZ(), t4.getX(), t4.getY());
    }
}
