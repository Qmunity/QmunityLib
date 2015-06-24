package uk.co.qmunity.lib.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import uk.co.qmunity.lib.helper.BlockPos;
import uk.co.qmunity.lib.helper.PlayerHelper;
import uk.co.qmunity.lib.helper.SystemInfoHelper;
import uk.co.qmunity.lib.helper.TeleportHelper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Quetzi on 26/02/15.
 */
public class CommandQLib extends CommandBase {

    @Override
    public String getCommandName() {

        return "qlib";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return "/qlib getuuid|tps|uptime";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        } else if (args[0].equalsIgnoreCase("getuuid")) {
            if (args.length != 2) {
                sender.addChatMessage(new ChatComponentText("Usage: /qlib getuuid <player>"));
            } else {
                String UUID = MinecraftServer.getServer().func_152358_ax().func_152655_a(args[1].toLowerCase()) != null ? MinecraftServer.getServer().func_152358_ax().func_152655_a(args[1].toLowerCase()).getId().toString() : "Player not found";
                sender.addChatMessage(new ChatComponentText("UUID for " + args[1] + ": " + UUID));
            }
        } else if (args[0].equalsIgnoreCase("tps")) {
            if (args.length == 1) {
                sendTextLines(sender, SystemInfoHelper.getTPSSummary());
            } else if (args.length == 2) {
                int dimension;
                try {
                    dimension = NumberFormat.getInstance().parse(args[1]).intValue();
                    sendTextLines(sender, SystemInfoHelper.getTPSDetail(dimension));
                } catch (ParseException e1) {
                    sender.addChatMessage(new ChatComponentText("Invalid dimension ID."));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("Usage: /qlib tps [dimension number]"));
            }
        } else if (args[0].equalsIgnoreCase("uptime")) {
            sender.addChatMessage(new ChatComponentText("Uptime: " + SystemInfoHelper.getUptime()));
            sender.addChatMessage(new ChatComponentText("Memory Usage: " + SystemInfoHelper.getAllocatedMem() + "/" + SystemInfoHelper.getMaxMem() + "[" + SystemInfoHelper.getPercentMemUse() + "%]"));
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (sender.getCommandSenderName().equals("Server") || PlayerHelper.isOpped(sender.getCommandSenderName())) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("showqueue")) {
                        if (TeleportHelper.teleportQueue.getQueue().size() > 0) {
                            String list = "";
                            for (String line : TeleportHelper.teleportQueue.getQueue()) {
                                if (!list.equals("")) {
                                    list = list + ", " + line;
                                } else {
                                    list = line;
                                }
                            }
                            sender.addChatMessage(new ChatComponentText("Queued players: " + list));
                        }
                    } else if (args[1].equalsIgnoreCase("clearqueue")) {
                        TeleportHelper.teleportQueue.clearQueue();
                        sender.addChatMessage(new ChatComponentText("Teleport queue has been cleared."));
                    } else if (TeleportHelper.sendToDefaultSpawn(args[1].toLowerCase())) {
                        sender.addChatMessage(new ChatComponentText(args[1] + " moved to their spawn."));
                    } else {
                        sender.addChatMessage(new ChatComponentText(args[1] + " not online, added to the queue for processing when next online."));
                    }
                } else if (args.length == 3) {
                    int dim = parseInt(sender, args[2]);
                    if (TeleportHelper.sendToDimension(args[1], dim)) {
                        sender.addChatMessage(new ChatComponentText("Moved " + args[1] + " to dimension " + dim));
                    } else {
                        sender.addChatMessage(new ChatComponentText(args[1] + " is not online, added to queue."));
                    }
                } else if (args.length == 6) {
                    int dim = parseInt(sender, args[2]);
                    int x = parseInt(sender, args[3]);
                    int y = parseInt(sender, args[4]);
                    int z = parseInt(sender, args[5]);
                    if (TeleportHelper.movePlayer(args[1], dim, new BlockPos(x, y, z))) {
                        sender.addChatMessage(new ChatComponentText(args[1] + " moved to dimension " + dim + ": " + x + ", " + ", " + y + ", " + z));
                    } else {
                        sender.addChatMessage(new ChatComponentText(args[1] + " is offline, added to queue."));
                    }
                } else {
                    sender.addChatMessage(new ChatComponentText("Usage: /qlib tp <player> [dim] [x] [y] [z]"));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("You do not have permission to use this command."));
            }
        }
    }

    private void sendTextLines(ICommandSender sender, List<String> text) {

        for (String line : text) {
            sender.addChatMessage(new ChatComponentText(line));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        return true;
    }
}
