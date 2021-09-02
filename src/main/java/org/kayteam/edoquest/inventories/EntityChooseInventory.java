package org.kayteam.edoquest.inventories;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EntityChooseInventory extends InventoryBuilder {

    public EntityChooseInventory(EdoQuest plugin, Prestige prestige, Player player, int page) {
        super(plugin.getInventories().getString("entityChoose.title"), 6);
        final int page1 = page;
        Yaml inventories = plugin.getInventories();
        for (int i = 0; i < 9; i++) addItem(i, () -> inventories.getItemStack("entityChoose.items.panel"));
        for (int i = 46; i < 53; i++) addItem(i, () -> inventories.getItemStack("entityChoose.items.panel"));
        // Return :D
        addItem(0, () -> inventories.getItemStack("entityChoose.items.return"));
        addLeftAction(37, ((player1, i) -> plugin.getInventoryManager().openInventory(player1, new KillsRequirementInventory(plugin,prestige, player1, 1))));
        // Close :D
        addItem(8, () -> inventories.getItemStack("entityChoose.items.close"));
        addLeftAction(40, ((player1, i) -> player.closeInventory()));
        // Entities
        EntityType[] entityTypes = EntityType.values();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < entityTypes.length) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("entityChoose.items.entity"), new String[][] {
                        {"%entityType%", entityTypes[index].toString()}
                }));
                addLeftAction(i, ((player1, i1) -> {

                }));
            }
        }
        // Previous Page
        addItem(45, () -> inventories.getItemStack("entityChoose.items.previousPage"));
        addLeftAction(45, (player1, i) -> {
            if (page1 > 1) {
                plugin.getInventoryManager().openInventory(player, new EntityChooseInventory(plugin, prestige, player, page1 - 1));
            }
        });
        // Next Page
        addItem(53, () -> inventories.getItemStack("killsRequirement.items.nextPage"));
        addLeftAction(53, (player1, i) -> {
            int amount = page1 * (4 * 9);
            if (entityTypes.length > amount) {
                plugin.getInventoryManager().openInventory(player, new EntityChooseInventory(plugin, prestige, player, page1 + 1));
            }
        });
    }

}