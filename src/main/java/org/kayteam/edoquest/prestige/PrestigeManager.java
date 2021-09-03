package org.kayteam.edoquest.prestige;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.*;
import java.util.logging.Level;

public class PrestigeManager {

    public PrestigeManager(EdoQuest plugin) {
        this.plugin = plugin;
    }

    private final EdoQuest plugin;
    private final LinkedHashMap<String, Prestige> prestigies = new LinkedHashMap<>();
    private final LinkedHashMap<Player, Prestige> playerData = new LinkedHashMap<>();
    private final List<Prestige> prestigeList = new ArrayList<>();

    public List<Prestige> getPrestigeList() {
        return prestigeList;
    }

    public HashMap<Player, Prestige> getPlayerData() {
        return playerData;
    }

    public int getListIndex(Prestige prestige) {
        return prestigeList.indexOf(prestige);
    }

    public void movePrestige(Prestige prestige, int index) {
        prestigeList.remove(prestige);
        prestigeList.add(index, prestige);
        savePrestige(prestige.getName());
    }

    public void sortPrestigies() {
        Collections.sort(prestigeList);
        /*
        for(int i = 0; i < prestigeList.size(); i++) {
            Prestige prestige = prestigeList.get(i);
            if (prestige.getPosition() != i) {
                prestige.setPosition(i);
                savePrestige(prestige.getName());
            }
        }
        */
    }

    public void loadPrestigies() {
        Yaml prestigies = plugin.getPrestigies();
        Set<String> names = prestigies.getFileConfiguration().getKeys(false);
        if (names.size() > 0) {
            for (String name : names) {
                Prestige prestige = new Prestige(name);
                boolean loadComplete = true;
                if (prestigies.contains(name + ".display.name")) {
                    if (prestigies.isString(name + ".display.name")) {
                        prestige.setDisplayName(prestigies.getString(name + ".display.name"));
                    }
                }else{
                    loadComplete = false;
                    plugin.getLogger().log(Level.SEVERE, "Prestige "+prestige.getName()+" missing display.name path, please add this to correct enable of that prestige.");
                    return;
                }
                if (prestigies.contains(name + ".position")) {
                    if (prestigies.isInt(name + ".position")) {
                        prestige.setPosition(prestigies.getInt(name + ".position"));
                    }
                }else{
                    loadComplete = false;
                    plugin.getLogger().log(Level.SEVERE, "Prestige "+prestige.getName()+" missing position path, please add this to correct enable of that prestige.");
                    return;
                }
                if (prestigies.contains(name + ".prestigeRank")) {
                    if (prestigies.isString(name + ".prestigeRank")) {
                        prestige.setPrestigeRank(prestigies.getString(name + ".prestigeRank"));
                    }
                }else{
                    loadComplete = false;
                    plugin.getLogger().log(Level.SEVERE, "Prestige "+prestige.getName()+" missing prestigeRank path, please add this to correct enable of that prestige.");
                    return;
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
                }else{
                    loadComplete = false;
                    plugin.getLogger().log(Level.SEVERE, "Prestige "+prestige.getName()+" missing requirements.kills path, please add this to correct enable of that prestige.");
                    return;
                }
                if(loadComplete){
                    prestigeList.add(prestige);
                    plugin.getLogger().info("Prestige "+prestige.getName()+" has been loaded correctly.");
                }else{
                    plugin.getLogger().log(Level.SEVERE, "An error has occurred ");
                }
            }
        }
        sortPrestigies();
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
        prestigies.set(name + ".position", prestige.getPosition());
        prestigies.set(name + ".prestigeRank", prestige.getPrestigeRank());
        List<String> kills = new ArrayList<>();
        for (EntityType entityType:prestige.getKillsRequirement().getEntities()) {
            kills.add(entityType.name() + ":" + prestige.getKillsRequirement().getAmount(entityType));
        }
        prestigies.set(name + ".requirements.kills", kills);
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

    public void addPrestige(Prestige prestige) {
        prestigies.put(prestige.getName(), prestige);
        prestigeList.add(prestige);
        sortPrestigies();
    }

}