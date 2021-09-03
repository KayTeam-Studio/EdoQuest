package org.kayteam.edoquest.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;

import java.util.ArrayList;

public class EdoQuestExpansion extends PlaceholderExpansion {

    private final EdoQuest plugin;

    public EdoQuestExpansion(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "KayTeam";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // %edoquest_next_prestige%
        if("next_prestige".equals(params)){
            try{
                ArrayList<Prestige> prestiges = new ArrayList<>(plugin.getPrestigeManager().getPrestigiesMap().values());
                int nextPrestigeIndex = 0;
                if(plugin.getPrestigeManager().getPlayerPrestige(player.getPlayer()) != null){
                    nextPrestigeIndex = prestiges.indexOf(plugin.getPrestigeManager().getPlayerPrestige(player.getPlayer()))+1;
                }
                if(nextPrestigeIndex < prestiges.size()){
                    return ((Prestige)prestiges.get(nextPrestigeIndex)).getDisplayName();
                }else{
                    return plugin.getSettings().getString("placeholders.maxPrestigeReached");
                }
            }catch (Exception e){
                return "&cError.";
            }
        // %edoquest_display_name%
        }else if("display_name".equals(params)){
            try{
                if(plugin.getPrestigeManager().getPlayerPrestige(player.getPlayer()) != null){
                    return plugin.getPrestigeManager().getPlayerPrestige(player.getPlayer()).getDisplayName();
                }else{
                    return plugin.getSettings().getString("placeholders.defaultPrestigeDisplayName");
                }
            }catch (Exception e){
                return "&cError.";
            }
        }else{
            return "&cInvalid placeholder.";
        }
    }
}
