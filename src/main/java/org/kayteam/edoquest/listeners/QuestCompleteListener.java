package org.kayteam.edoquest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;

import java.util.logging.Level;

public class QuestCompleteListener implements Listener {

    private final EdoQuest plugin;

    public QuestCompleteListener(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuestComplete(QuestCompleteEvent event){
        String group = event.getPrestige().getPrestigeRank();
        try{
            EdoQuest.getPermissions().playerAddGroup(event.getPlayer(), group);
            Bukkit.broadcastMessage("El jugador "+event.getPlayer().getName()+" subió al prestigio "+event.getPrestige().getDisplayName());
        }catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c[EdoQuest] An error has ocurred trying to give &f"+group+" &cto &f"+event.getPlayer().getName()));
        }
    }
}
