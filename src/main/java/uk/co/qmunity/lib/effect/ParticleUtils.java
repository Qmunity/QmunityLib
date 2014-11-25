package uk.co.qmunity.lib.effect;

import net.minecraft.world.World;

public class ParticleUtils {

    public static void spawnParticle(World w, double x, double y, double z, Particle particle) {

        if (particle.parent != null)
            return;

        w.spawnEntityInWorld(new EntityFXParticle(w, x, y, z, particle));
    }

}
