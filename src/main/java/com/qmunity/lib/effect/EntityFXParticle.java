package com.qmunity.lib.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFXParticle extends Entity {

    public Particle particle;

    public EntityFXParticle(World world, double x, double y, double z, Particle particle) {

        super(world);
        posX = x;
        posY = y;
        posZ = z;

        ignoreFrustumCheck = true;

        this.particle = particle;
        particle.x = x;
        particle.y = y;
        particle.z = z;

        particle.parent = this;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {

        return pass == 1;
    }

    @Override
    public void onUpdate() {

        super.onUpdate();

        if (particle.getAge() >= particle.getMaxAge())
            setDead();

        particle.tick();

        posX += particle.velX;
        posY += particle.velY;
        posZ += particle.velZ;

        particle.velX = particle.velY = particle.velZ = 0;

        particle.makeOlder();
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {

    }

}
