package nl.dykam.dev.autoregen.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Title {

    public static void sendTitle(CommandSender player, TitleSettings titleSettings, String title, String subtitle) {
        sendTitle(player.getName(), titleSettings, title, subtitle);
    }

    public static void sendTitle(String player, TitleSettings titleSettings, String title, String subtitle) {
        ProtocolManager protocolManager = getProtocolManager();
        Player playerObject = Bukkit.getPlayer(player);
        if (protocolManager != null && playerObject != null) {
            PacketContainer timesPacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
            timesPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
            timesPacket.getIntegers().write(0, 5);
            timesPacket.getIntegers().write(1, 20);
            timesPacket.getIntegers().write(2, 5);
            PacketContainer titlePacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
            titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
            titlePacket.getChatComponents().write(0, WrappedChatComponent.fromJson("{text: \"" + title + "\"}"));
            PacketContainer subTitlePacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
            subTitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
            subTitlePacket.getChatComponents().write(0, WrappedChatComponent.fromJson("{text:\"" + subtitle + "\"}"));
            try {
                protocolManager.sendServerPacket(playerObject, timesPacket);
                protocolManager.sendServerPacket(playerObject, subTitlePacket);
                protocolManager.sendServerPacket(playerObject, titlePacket);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            String prefix = "title " + player + " ";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), prefix + "times " + titleSettings.getTicksFadeIn() + " " + titleSettings.getDuration() + " " + titleSettings.getTicksFadeOut() + " ");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), prefix + "subtitle {text:\"" + subtitle + "\"}");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), prefix + "title {text:\"" + title + "\"}");
        }
    }

    private static ProtocolManager getProtocolManager() {
        try {
            return ProtocolLibrary.getProtocolManager();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void sendTitle(CommandSender player, TitleSettings titleSettings, String title) {
        sendTitle(player, titleSettings, title, "");
    }

    public static void sendTitle(String player, TitleSettings titleSettings, String title) {
        sendTitle(player, titleSettings, title, "");
    }

    public static void sendSubTitle(CommandSender player, TitleSettings titleSettings, String subtitle) {
        sendTitle(player, titleSettings, "", subtitle);
    }

    public static void sendSubTitle(String player, TitleSettings titleSettings, String subtitle) {
        sendTitle(player, titleSettings, "", subtitle);
    }
}
