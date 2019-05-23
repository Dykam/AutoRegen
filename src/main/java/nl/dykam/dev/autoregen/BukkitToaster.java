package nl.dykam.dev.autoregen;

import nl.dykam.dev.autoregen.util.Title;
import org.bukkit.entity.Player;

public class BukkitToaster implements Toaster {
    @Override
    public void sendMessage(Player sender, String message) {
        Title.sendSubTitle(sender, AutoRegenPlugin.TITLE_SETTINGS, message);
    }
}
