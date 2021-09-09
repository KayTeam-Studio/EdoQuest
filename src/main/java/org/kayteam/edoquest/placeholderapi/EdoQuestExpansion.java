package org.kayteam.edoquest.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.edoquest.prestige.PrestigeManager;
import org.kayteam.kayteamapi.yaml.Yaml;

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
        /*
        %edoquest_next_prestige_display%
        %edoquest_next_prestige_name%
        %edoquest_prestige_display%
        %edoquest_prestige_name%
         */
        PrestigeManager prestigeManager = plugin.getPrestigeManager();
        Yaml settings = plugin.getSettings();
        switch (params) {
            case "next_prestige_display":
                try{
                    ArrayList<Prestige> prestigies = new ArrayList<>(plugin.getPrestigeManager().getPrestigiesMap().values());
                    int nextPrestigeIndex = 0;
                    Prestige prestige = prestigeManager.getPlayerPrestige(player.getPlayer());
                    if(prestige != null){
                        nextPrestigeIndex = prestigies.indexOf(prestige) + 1;
                    }
                    if(nextPrestigeIndex < prestigies.size()){
                        return prestigies.get(nextPrestigeIndex).getDisplayName();
                    }else{
                        return settings.getString("placeholders.maxPrestigeReached");
                    }
                }catch (Exception e){
                    return "&cError.";
                }
            case "next_prestige_name":
                try{
                    ArrayList<Prestige> prestigies = new ArrayList<>(plugin.getPrestigeManager().getPrestigiesMap().values());
                    int nextPrestigeIndex = 0;
                    Prestige prestige = prestigeManager.getPlayerPrestige(player.getPlayer());
                    if(prestige != null){
                        nextPrestigeIndex = prestigies.indexOf(prestige) + 1;
                    }
                    if(nextPrestigeIndex < prestigies.size()){
                        return prestigies.get(nextPrestigeIndex).getName();
                    }else{
                        return settings.getString("placeholders.maxPrestigeReached");
                    }
                }catch (Exception e){
                    return "&cError.";
                }
            case "prestige_display":
                try{
                    Prestige prestige = prestigeManager.getPlayerPrestige(player.getPlayer());
                    if(prestige != null){
                        return prestige.getDisplayName();
                    }else{
                        return settings.getString("placeholders.noPrestige");
                    }
                }catch (Exception e){
                    return "&cError.";
                }
            case "prestige_name":
                try{
                    Prestige prestige = prestigeManager.getPlayerPrestige(player.getPlayer());
                    if(prestige != null){
                        return prestige.getName();
                    }else{
                        return settings.getString("placeholders.noPrestige");
                    }
                }catch (Exception e){
                    return "&cError.";
                }
            default:
                return "&cInvalid placeholder.";
        }
    }
}
