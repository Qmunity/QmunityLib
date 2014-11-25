package uk.co.qmunity.lib.effect;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Particle {

    protected int age = 0;
    protected double x = 0, y = 0, z = 0;
    protected double velX = 0, velY = 0, velZ = 0;

    protected EntityFXParticle parent;

    public abstract void tick();

    /**
     * Remember to add @SideOnly(Side.CLIENT)
     */
    @SideOnly(Side.CLIENT)
    public abstract void render(double x, double y, double z, float frame);

    public void update() {

    }

    public int getAge() {

        return age;
    }

    public void makeOlder() {

        age++;
    }

    public abstract int getMaxAge();

    public void spawn(World w, int x, int y, int z) {

        ParticleUtils.spawnParticle(w, x, y, z, this);
    }

}
