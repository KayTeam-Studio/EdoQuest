package org.kayteam.edoquest.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.kayteam.edoquest.EdoQuest;

public class EntityDeathListener implements Listener {

    private final EdoQuest plugin;

    public EntityDeathListener(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player != null){
            plugin.getPrestigeManager().checkNextPrestige(player);
        }
    }
}
