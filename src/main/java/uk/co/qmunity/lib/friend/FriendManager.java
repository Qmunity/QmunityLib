package uk.co.qmunity.lib.friend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class FriendManager {

    public static List<IPlayer> getFriends(EntityPlayer player) {

        return getFriends(player.getGameProfile().getId());
    }

    public static List<IPlayer> getFriends(IPlayer player) {

        return getFriends(player.getUUID());
    }

    public static List<IPlayer> getFriends(UUID player) {

        return new ArrayList<IPlayer>();
    }

}
