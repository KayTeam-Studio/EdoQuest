package org.kayteam.edoquest.inventories;

import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class KillsRequirementInventory extends InventoryBuilder {

    public KillsRequirementInventory(EdoQuest plugin, Prestige prestige, Player player, int page) {
        super(plugin.getInventories().getString("killsRequirement.title"), 6);
        final int page1 = page;
        Yaml inventories = plugin.getInventories();
        for (int i = 0; i < 9; i++) addItem(i, () -> inventories.getItemStack("killsRequirement.items.panel"));
        for (int i = 46; i < 53; i++) addItem(i, () -> inventories.getItemStack("killsRequirement.items.panel"));
        // Return :D
        addItem(0, () -> inventories.getItemStack("killsRequirement.items.return"));
        addLeftAction(37, ((player1, i) -> plugin.getInventoryManager().openInventory(player1, new PrestigeEditorInventory(plugin,prestige, player1))));
        // Close :D
        addItem(8, () -> inventories.getItemStack("killsRequirement.items.close"));
        addLeftAction(40, ((player1, i) -> player.closeInventory()));
        // Entities
        List<EntityType> entityTypes = prestige.getKillsRequirement().getEntities();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < entityTypes.size()) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("killsRequirement.items.entity"), new String[][] {
                        {"%entityType%", entityTypes.get(index).toString()},
                        {"%killsAmount%", prestige.getKillsRequirement().getAmount(entityTypes.get(index)) + ""}
                }));
                addLeftAction(i, ((player1, i1) -> {

                }));
                addRightAction(i, ((player1, i1) -> {
                    prestige.getKillsRequirement().removeEntity(entityTypes.get(index));
                    plugin.getInventoryManager().openInventory(player1, new KillsRequirementInventory(plugin, prestige, player, page));
                }));
            }
        }
        // Previous Page
        addItem(45, () -> inventories.getItemStack("killsRequirement.items.previousPage"));
        addLeftAction(45, ((player1, i) -> {
            if (page1 > 1) {
                plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, player, page1 - 1));
            }
        }));
        // Next Page
        addItem(53, () -> inventories.getItemStack("killsRequirement.items.nextPage"));
        addLeftAction(53, ((player1, i) -> {
            int amount = page1 * (4 * 9);
            if (entityTypes.size() > amount) {
                plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, player, page1 + 1));
            }
        }));
    }

}