package nl.dykam.dev.autoregen.util;

import org.bukkit.entity.Player;

public class Title {
    public static void sendTitle(Player player, TitleSettings titleSettings, String title, String subtitle) {
        player.sendTitle(title, subtitle, titleSettings.getTicksFadeIn(), titleSettings.getDuration(), titleSettings.getTicksFadeOut());
    }

    public static void sendSubTitle(Player player, TitleSettings titleSettings, String subtitle) {
        sendTitle(player, titleSettings, "", subtitle);
    }
}
