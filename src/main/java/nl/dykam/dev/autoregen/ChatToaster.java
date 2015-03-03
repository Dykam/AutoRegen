package nl.dykam.dev.autoregen;

import org.bukkit.entity.Player;

public class ChatToaster implements Toaster {
    @Override
    public void sendMessage(Player sender, String message) {
        sender.sendMessage(message);
    }
}
