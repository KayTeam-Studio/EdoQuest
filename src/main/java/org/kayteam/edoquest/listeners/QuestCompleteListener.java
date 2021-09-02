package org.kayteam.edoquest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;

public class QuestCompleteListener implements Listener {

    private final EdoQuest plugin;

    public QuestCompleteListener(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuestComplete(QuestCompleteEvent event){
        Bukkit.getLogger().info("PRESTIGE COMPLETE");
        Bukkit.getLogger().info(event.getPlayer().toString());
        Bukkit.getLogger().info(event.getPrestige().toString());
    }
}
