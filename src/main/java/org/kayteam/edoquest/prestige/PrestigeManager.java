package org.kayteam.edoquest.prestige;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.*;

public class PrestigeManager {

    public PrestigeManager(EdoQuest plugin) {
        this.plugin = plugin;
    }

    private final EdoQuest plugin;
    private final HashMap<String, Prestige> prestigies = new HashMap<>();
    private final HashMap<Player, Prestige> playerData = new HashMap<>();
    private final List<Prestige> prestigeList = new ArrayList<>();

    public List<Prestige> getPrestigeList() {
        return prestigeList;
    }

    public void sortPrestigies() {
        Collections.sort(prestigeList);
    }

    public void loadPrestigies() {
        Yaml prestigies = plugin.getPrestigies();
        Set<String> names = prestigies.getFileConfiguration().getKeys(false);
        if (names.size() > 0) {
            for (String name : names) {
                Prestige prestige = new Prestige(name);
                if (prestigies.contains(name + ".display.name")) {
                    if (prestigies.isString(name + ".display.name")) {
                        prestige.setDisplayName(prestigies.getString(name + ".display.name", name));
                    }
                }
                if (prestigies.contains(name + ".requirements.kills")) {
                    if (prestigies.isList(name + ".requirements.kills")) {
                        List<String> kills = prestigies.getFileConfiguration().getStringList(name + ".requirements.kills");
                        if (kills.size() > 0) {
                            for (String kill : kills) {
                                String entityTypeString = kill.split(":")[0];
                                String amountString = kill.split(":")[1];
                                try {
                                    EntityType entityType = EntityType.fromName(entityTypeString);
                                    int amount = Integer.parseInt(amountString);
                                    prestige.getKillsRequirement().addEntity(entityType, amount);
                                } catch (NumberFormatException ignored) {
                                    plugin.getLogger().info("The amount '" + amountString + "' no is valid, this kill requirement is skipped from the " + name + " prestige.");
                                } catch (IllegalArgumentException ignored) {
                                    plugin.getLogger().info("The entity '" + entityTypeString + "' no is valid, this kill requirement is skipped from the " + name + " prestige.");
                                }
                            }
                        }
                    }
                }
                this.prestigeList.add(prestige);
                Bukkit.getLogger().info("LOADED "+prestige.toString());
            }
        }
        Collections.sort(prestigeList);
        for(Prestige p : prestigeList){
            this.prestigies.put(p.getName(), p);
        }
    }

    public void loadPlayerPrestige(Player player){
        Thread thread = new Thread(() -> {
            Prestige playerPrestige = null;
            for(Prestige prestige : prestigies.values()){
                boolean completed = true;
                for(EntityType entityType : prestige.getKillsRequirement().getEntities()){
                    int entityKillsNeeded = prestige.getKillsRequirement().getAmount(entityType);
                    if(player.getStatistic(Statistic.KILL_ENTITY, entityType) < entityKillsNeeded){
                        completed = false;
                        break;
                    }
                }
                if(completed){
                    playerPrestige = prestige;
                    playerData.put(player, playerPrestige);
                }else{
                    break;
                }
            }
        });
        thread.start();
    }

    public void checkNextPrestige(Player player){
        Prestige actualPrestige = getPlayerPrestige(player);
        Prestige nextPrestige;
        ArrayList<Prestige> prestigiesArrayList = new ArrayList<>(this.prestigies.values());
        if(prestigiesArrayList.size()<prestigiesArrayList.indexOf(actualPrestige)+1){
            if(actualPrestige != null){
                try{
                    nextPrestige = prestigiesArrayList.get(prestigiesArrayList.indexOf(actualPrestige)+1);
                }catch (Exception e){
                    return;
                }
            }else{
                nextPrestige = prestigiesArrayList.get(0);
            }
            boolean completed = true;
            for(EntityType entityType : nextPrestige.getKillsRequirement().getEntities()){
                int entityKillsNeeded = nextPrestige.getKillsRequirement().getAmount(entityType);
                if((player.getStatistic(Statistic.KILL_ENTITY, entityType)+1) < entityKillsNeeded){
                    completed = false;
                    break;
                }
            }
            if(completed){
                actualPrestige = nextPrestige;
                plugin.getServer().getPluginManager().callEvent(new QuestCompleteEvent(player, nextPrestige));
            }
            playerData.put(player, actualPrestige);
        }
    }

    public void loadPlayersData(){
        for(Player player : plugin.getServer().getOnlinePlayers()){
            Thread thread = new Thread(){
                public void run(){
                    loadPlayerPrestige(player);
                }
            };
            thread.start();
        }
    }

    public Prestige getPlayerPrestige(Player player){
        return playerData.get(player);
    }

    public void deletePrestige(String name) {
        Yaml prestigies = plugin.getPrestigies();
        prestigies.set(name, null);
        prestigies.saveFileConfiguration();
        this.prestigies.remove(name);
    }

    public void saveAll() {
        if (prestigies.size() > 0) {
            for (String name:prestigies.keySet()) {
                savePrestige(name);
            }
        }
    }

    public void savePrestige(String name) {
        Yaml prestigies = plugin.getPrestigies();
        Prestige prestige = this.prestigies.get(name);
        prestigies.set(name + ".display.name", prestige.getDisplayName());
        prestigies.set(name + ".prestigeRank", prestige.getPrestigeRank());
        List<String> kills = new ArrayList<>();
        for (EntityType entityType:prestige.getKillsRequirement().getEntities()) {
            kills.add(entityType.name() + ":" + prestige.getKillsRequirement().getAmount(entityType));
        }
        prestigies.set(name + ".requirement.kills", kills);
        prestigies.saveFileConfiguration();
    }

    public List<String> getPrestigies() {
        return new ArrayList<>(this.prestigies.keySet());
    }

    public HashMap<String, Prestige> getPrestigiesMap(){
        return prestigies;
    }

    public Prestige getPrestige(String name) {
        return prestigies.get(name);
    }

    public void addPrestige(Prestige prestige) { prestigies.put(prestige.getName(), prestige); }

}