package org.kayteam.edoquest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        String group = event.getPrestige().getPrestigeRank();
        try{
            EdoQuest.getPermissions().playerAddGroup(player, group);
            player.playSound(player.getLocation(),
                    Sound.valueOf(plugin.getSettings().getString("questComplete.sound")), 1, 1);
            if(plugin.getSettings().getBoolean("questComplete.title.enabled")){
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                plugin.getSettings().getString("questComplete.title.title",
                                new String[][]{{"%display_name%", event.getPrestige().getDisplayName()}})),
                        ChatColor.translateAlternateColorCodes('&',
                                plugin.getSettings().getString("questComplete.title.subtitle",
                                new String[][]{{"%display_name%", event.getPrestige().getDisplayName()}})));
            }
            plugin.getSettings().sendMessage(player, "questComplete.messages",
                    new String[][]{{"%display_name%", event.getPrestige().getDisplayName()}});
        }catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c[EdoQuest] An error has ocurred trying to give &f"+group+" &cto &f"+player.getName()));
        }
    }
}
