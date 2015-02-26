package uk.co.qmunity.lib.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Quetzi on 26/02/15.
 */
public class SystemInfoHelper {

    private static       Runtime       runtime       = Runtime.getRuntime();
    private static       NumberFormat  format        = NumberFormat.getInstance();
    public static        long          startTime     = 0;
    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");

    public static String getOSname() {

        return System.getProperty("os.name");
    }

    public static String getOSversion() {

        return System.getProperty("os.version");
    }

    public static String getOsArch() {

        return System.getProperty("os.arch");
    }

    public static String getTotalMem() {
        return StringHelper.bytesToString(runtime.totalMemory());
    }

    public static String getUsedMem() {
        return StringHelper.bytesToString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public static String getFreeMem() {
        return StringHelper.bytesToString(runtime.freeMemory());
    }

    public static String getAllocatedMem() {
        return StringHelper.bytesToString(runtime.totalMemory());
    }

    public static String getMaxMem() {
        return StringHelper.bytesToString(runtime.maxMemory());
    }

    public static String getPercentMemUse() {
        return format.format(((float)runtime.totalMemory() / (float)runtime.maxMemory()) * 100);
    }

    public static String getUptime() {

        long uptime = System.currentTimeMillis() - startTime;
        return StringHelper.millisToString(uptime);
    }

    public static double getDimensionTPS(WorldServer worldServer) {
        double worldTickLength = MathHelper.mean(MinecraftServer.getServer().worldTickTimes.get(worldServer.provider.dimensionId)) * 1.0E-6D;
        return Math.min(1000.0 / worldTickLength, 20);
    }

    public static double getWorldTickTime(WorldServer worldServer) {
        return MathHelper.mean(MinecraftServer.getServer().worldTickTimes.get(worldServer.provider.dimensionId)) * 1.0E-6D;
    }

    public static List<String> getTPSSummary() {

        List<String> textOutput = new ArrayList<String>();
        int chunksLoaded = 0;
        textOutput.add(getUptime());
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            chunksLoaded += world.getChunkProvider().getLoadedChunkCount();
            textOutput.add("[" + world.provider.dimensionId + "]" + world.provider.getDimensionName() + ": " + timeFormatter.format(getWorldTickTime(world)) + "ms [" + timeFormatter.format(getDimensionTPS(world))
                           + "]");
        }
        textOutput.add("Total Chunks loaded: " + chunksLoaded);
        textOutput.add("Overall: " + timeFormatter.format(MathHelper.mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D) + "ms ["
                       + Math.min(1000.0 / (MathHelper.mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D), 20) + "]");
        return textOutput;
    }
    @SuppressWarnings("unchecked")
    public static List<String> getTPSDetail(int dimension) {

        List<String> textOutput = new ArrayList<String>();
        WorldServer world = MinecraftServer.getServer().worldServerForDimension(dimension);
        textOutput.add("Uptime: " + getUptime());
        textOutput.add("Information for [" + dimension + "]" + world.provider.getDimensionName());
        textOutput.add("Players (" + world.playerEntities.size() + "): " + getPlayersForDimension(dimension));
        textOutput.add("Item Entities: " + getItemEntityCount((ArrayList<Entity>)world.loadedEntityList));
        textOutput.add("Hostile Mobs: " + getHostileEntityCount((ArrayList<Entity>)world.loadedEntityList));
        textOutput.add("Passive Mobs: " + getPassiveEntityCount((ArrayList<Entity>)world.loadedEntityList));
        textOutput.add("Total Living Entities: " + getLivingEntityCount((ArrayList<Entity>)world.loadedEntityList));
        textOutput.add("Total Entities: " + world.loadedEntityList.size());
        textOutput.add("Tile Entities: " + world.loadedTileEntityList.size());
        textOutput.add("Loaded Chunks: " + world.getChunkProvider().getLoadedChunkCount());
        textOutput.add("TPS: " + timeFormatter.format(getWorldTickTime(world)) + "ms[" + getDimensionTPS(world) + "]");
        return textOutput;
    }

    private static int getItemEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityItem) {
                count++;
            }
        }
        return count;
    }

    private static int getPassiveEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityAnimal) {
                count++;
            }
        }
        return count;
    }

    private static int getHostileEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityMob) {
                count++;
            }
        }
        return count;
    }

    private static int getLivingEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityLiving) {
                count++;
            }
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    private static String getPlayersForDimension(int dimension) {

        ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().worldServerForDimension(dimension).playerEntities;
        if (players.size() == 0) {
            return "None";
        } else {
            String playersString = "";
            Iterator<EntityPlayer> ite = players.iterator();
            while (ite.hasNext()) {
                playersString = playersString + ite.next().getCommandSenderName();
                if (ite.hasNext()) {
                    playersString = playersString + ",";
                } else {
                    playersString = playersString + ".";
                }
            }
            return playersString;
        }
    }
}
