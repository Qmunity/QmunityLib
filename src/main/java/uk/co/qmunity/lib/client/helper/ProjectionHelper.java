package uk.co.qmunity.lib.client.helper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Most of this class was made by the OpenMods team for OpenModsLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/OpenMods/OpenModsLib
 */
public class ProjectionHelper {

    private IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private FloatBuffer objectCoords = GLAllocation.createDirectFloatBuffer(3);
    private FloatBuffer winCoords = GLAllocation.createDirectFloatBuffer(3);

    public void refreshMatrices() {

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
    }

    public void updateMatrices() {

        GL11.glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrix(projection);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrix(modelview);
    }

    public Vec3 unproject(float winX, float winY, float winZ) {

        GLU.gluUnProject(winX, winY, winZ, modelview, projection, viewport, objectCoords);

        float objectX = objectCoords.get(0);
        float objectY = objectCoords.get(1);
        float objectZ = objectCoords.get(2);

        return Vec3.createVectorHelper(objectX, objectY, objectZ);
    }

    public Vec3 project(float objX, float objY, float objZ) {

        GLU.gluProject(objX, objY, objZ, modelview, projection, viewport, winCoords);

        float winX = winCoords.get(0);
        float winY = winCoords.get(1);
        float winZ = winCoords.get(2);

        return Vec3.createVectorHelper(winX, winY, winZ);
    }
}