package uk.co.qmunity.lib.helper;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quetzi on 26/02/15.
 */
public class TeleportHelper {

    public static TeleportQueue teleportQueue;

    public TeleportHelper() {

        teleportQueue = new TeleportQueue();
    }

    public class TeleportQueue {

        private List<TeleportEntry> queue = new ArrayList<TeleportEntry>();

        public TeleportQueue() {

        }

        public class TeleportEntry {

            private String player;
            private String type;
            private int    dim;
            private int    x;
            private int    y;
            private int    z;

            public TeleportEntry(String player, int dim, int x, int y, int z) {

                this.player = player;
                this.type = "location";
                this.dim = dim;
                this.x = x;
                this.y = y;
                this.z = z;
            }

            public TeleportEntry(String player) {

                this.player = player;
                this.type = "default";
            }

            public String getPlayer() {
                return player;
            }

            public int getDim() {
                return dim;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int getZ() {
                return z;
            }
        }

        public boolean addToQueue(String player) {
            return this.queue.add(new TeleportEntry(player));
        }

        public boolean addToQueue(String player, int dim, int x, int y, int z) {

            return this.queue.add(new TeleportEntry(player.toLowerCase(), dim, x, y, z));
        }

        public void clearQueue() {

            teleportQueue = new TeleportQueue();
        }

        public boolean process(String player) {
            for (TeleportEntry te : this.queue) {
                if (te.getPlayer().equals(player.toLowerCase())) {
                    if (te.type.equals("default")) {
                        sendToDefaultSpawn(te.getPlayer());
                    } else {
                        sendToLocation(player, te.getDim(), te.getX(), te.getY(), te.getZ());
                    }
                    remove(player);
                    return true;
                }
            }
            return false;
        }

        public void remove(String player) {

            for (TeleportEntry te : this.queue) {
                if (te.getPlayer().equals(player.toLowerCase())) {
                    this.queue.remove(te);
                }
            }
        }

        public boolean isQueued(String player) {

            for (TeleportEntry te : this.queue) {
                if (te.getPlayer().equals(player.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }

        public List<String> getQueue() {

            List<String> queuedPlayers = new ArrayList<String>();
            for (TeleportEntry te : this.queue) {
                queuedPlayers.add(te.getPlayer());
            }
            return queuedPlayers;
        }
    }

    public static boolean movePlayer(String playername, int dim, ChunkCoordinates dest) {

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            if (player.dimension != dim) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dim);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
            return true;
        } else {
            queuePlayer(playername, dim, dest);
            return false;
        }
    }

    public static boolean sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.getBedLocation(0) != null) {
                return sendToBed(playername);
            } else {
                return sendToDimension(playername, 0);
            }
        }
        teleportQueue.addToQueue(playername);
        return false;
    }

    public static boolean sendToBed(String playername) {

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
        ChunkCoordinates dest = player.getBedLocation(0);
        return movePlayer(playername, 0, dest);
    }

    public static boolean sendToDimension(String playername, int dim) {

        ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(dim).getSpawnPoint();
        return movePlayer(playername, dim, dest);
    }

    public static boolean sendToLocation(String playername, int dim, int x, int y, int z) {

        return movePlayer(playername, dim, new ChunkCoordinates(x, y, z));
    }

    private static boolean queuePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (!teleportQueue.isQueued(playername)) {
            return teleportQueue.addToQueue(playername, dim, dest.posX, dest.posY, dest.posZ);
        }
        return false;
    }
}
