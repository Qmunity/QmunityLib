package uk.co.qmunity.lib.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import uk.co.qmunity.lib.helper.SystemInfoHelper;

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
                    dimension = NumberFormat.getInstance().parse(args[0]).intValue();
                } catch (ParseException e1) {
                    sender.addChatMessage(new ChatComponentText("Invalid dimension ID."));
                    return;
                }
                sendTextLines(sender, SystemInfoHelper.getTPSDetail(dimension));
            } else {
                sender.addChatMessage(new ChatComponentText("Usage: /qlib tps [dimension number]"));
            }
        } else if (args[0].equalsIgnoreCase("uptime")) {
            sender.addChatMessage(new ChatComponentText(SystemInfoHelper.getUptime()));
            sender.addChatMessage(new ChatComponentText(SystemInfoHelper.getAllocatedMem() + "/" + SystemInfoHelper.getMaxMem() + "[" + SystemInfoHelper.getPercentMemUse() + "%]"));
        }
    }

    private void sendTextLines(ICommandSender sender, List<String> text) {
        for (String line : text) {
            sender.addChatMessage(new ChatComponentText(line));
        }
    }
}
