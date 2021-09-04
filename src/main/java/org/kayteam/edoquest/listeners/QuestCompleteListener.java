package org.kayteam.edoquest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.StringUtil;

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
            // ADD PERMISSION PARENT GROUP BY COMMAND
            Prestige prestige = event.getPrestige();
            for (String command:prestige.getCommands()) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), StringUtil.replace(command, player, new String[][] {{"%prestige%", group}}));
            }
            // PLAY QUEST COMPLETE SOUND
            try {
                Sound sound = Sound.valueOf(plugin.getSettings().getString("questComplete.sound.type"));
                player.playSound(player.getLocation(), sound, 1, 1);
            }  catch (IllegalArgumentException ignored) {
                plugin.getLogger().info("&c[EdoQuest] An error has occurred trying to play the sound &f" + plugin.getSettings().getString("questComplete.sound.type"));
            } catch (NullPointerException ignored) {
                plugin.getLogger().info("&c[EdoQuest] The settings path 'questComplete.sound.type' no exist!");
            }
            // SEND QUEST COMPLETE TITLE
            if(plugin.getSettings().getBoolean("questComplete.title.enabled")){
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                plugin.getSettings().getString("questComplete.title.text",
                                new String[][]{{"%display_name%", event.getPrestige().getDisplayName()}})),
                        ChatColor.translateAlternateColorCodes('&',
                                plugin.getSettings().getString("questComplete.title.subText",
                                new String[][]{{"%display_name%", event.getPrestige().getDisplayName()}})));
            }
            // SEND QUEST COMPLETE MESSAGES
            plugin.getSettings().sendMessage(player, "questComplete.messages.texts", new String[][]{{"%display_name%", prestige.getDisplayName()}});
        }catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "[EdoQuest] An error has occurred trying to give &f"+group+" &cto &f"+player.getName());
        }
    }
}
