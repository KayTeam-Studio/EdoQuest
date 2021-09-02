package org.kayteam.edoquest.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kayteam.edoquest.EdoQuest;

public class PlayerJoinListener implements Listener {

    private final EdoQuest plugin;

    public PlayerJoinListener(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.getPrestigeManager().loadPlayerPrestige(event.getPlayer());
    }
}
