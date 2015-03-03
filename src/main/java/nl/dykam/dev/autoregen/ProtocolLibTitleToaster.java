package nl.dykam.dev.autoregen;

import nl.dykam.dev.autoregen.util.Title;
import org.bukkit.entity.Player;

public class ProtocolLibTitleToaster implements Toaster {
    @Override
    public void sendMessage(Player sender, String message) { // http://dev.bukkit.org/bukkit-plugins/protocollib/pages/tutorial/
        Title.sendSubTitle(sender, AutoRegenPlugin.TITLE_SETTINGS, message);
    }
}
