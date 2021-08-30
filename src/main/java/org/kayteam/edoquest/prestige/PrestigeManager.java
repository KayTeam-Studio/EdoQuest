package org.kayteam.edoquest.prestige;

import org.bukkit.entity.EntityType;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.util.yaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PrestigeManager {

    public PrestigeManager(EdoQuest plugin) {
        this.plugin = plugin;
    }

    private final EdoQuest plugin;
    private final HashMap<String, Prestige> prestigies = new HashMap<>();

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
                if (prestigies.contains(name + ".requirements.rank")) {
                    if (prestigies.isString(name + ".requirements.rank")) {
                        prestige.getRankRequirement().setRank(prestigies.getString(name + ".requirements.rank"));
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
                                    EntityType entityType = EntityType.valueOf(entityTypeString);
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
                this.prestigies.put(name, prestige);
            }
        }
    }

    public void deletePrestige(String name) {
        Yaml prestigies = plugin.getPrestigies();
        prestigies.set(name, null);
        prestigies.saveFileConfiguration();
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
        prestigies.set(name + ".requirement.rank", prestige.getRankRequirement().getRank());
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

    public Prestige getPrestige(String name) {
        return prestigies.get(name);
    }

}