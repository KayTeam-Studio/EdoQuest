package org.kayteam.edoquest.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
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
        -
        %edoquest_requirement_NUMERO_PRESTIGE%
        %edoquest_prestige_status_PRESTIGE%
         */
        PrestigeManager prestigeManager = plugin.getPrestigeManager();
        Yaml settings = plugin.getSettings();
        if(params.startsWith("prestige_status")){
            try{
                ArrayList<Prestige> prestiges = new ArrayList<>(plugin.getPrestigeManager().getPrestigiesMap().values());
                Prestige prestige = plugin.getPrestigeManager().getPrestige(params.split("_")[2]);
                Prestige playerPrestige = plugin.getPrestigeManager().getPlayerPrestige(player.getPlayer());
                if(prestige != null){
                    if(prestiges.indexOf(prestige) <= prestiges.indexOf(playerPrestige)){
                        return settings.getString("placeholders.prestigeStatus.unlocked");
                    }else{
                        return settings.getString("placeholders.prestigeStatus.locked");
                    }
                }else{
                    return "&cError.";
                }
            }catch (Exception e){
                return "&cError.";
            }
        }else if(params.startsWith("requirement")){
            try{
                Prestige prestige = plugin.getPrestigeManager().getPrestige(params.split("_")[2]);
                int entityIndex = Integer.parseInt(params.split("_")[1]);
                EntityType entityType = prestige.getKillsRequirement().getEntities().get(entityIndex);
                return (entityType.toString()+": "+player.getPlayer().getStatistic(Statistic.KILL_ENTITY, entityType)+"/"+prestige.getKillsRequirement().getAmount(entityType));
            }catch (Exception e){
                return "&cError.";
            }
        }else if(params.equals("next_prestige_display")){
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
        }else if(params.equals("next_prestige_name")){
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
        }else if(params.equals("prestige_display")){
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
        }else if(params.equals("prestige_name")){
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
        }else{
            return "&cInvalid placeholder.";
        }
    }
}
