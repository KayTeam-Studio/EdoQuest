package org.kayteam.edoquest.inventories.prestigeeditor;

import org.bukkit.entity.EntityType;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EntityChooserInventory extends InventoryBuilder {

    public EntityChooserInventory(EdoQuest plugin, Prestige prestige, int page) {
        super(plugin.getInventories().getString("entityChoose.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("entityChoose.panel"), new int[]{1, 6});
        // Return
        addItem(0, () -> inventories.getItemStack("entityChoose.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, 1)));
        // Close
        addItem(8, () -> inventories.getItemStack("entityChoose.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // Entities
        EntityType[] entityTypes = EntityType.values();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < entityTypes.length) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("entityChoose.entity"), new String[][] {{"%entityType%", entityTypes[index].toString()}}));
                addLeftAction(i, (player, slot) -> {
                    prestige.getKillsRequirement().addEntity(entityTypes[index], 0);
                    plugin.getPrestigeManager().savePrestige(prestige.getName());
                    plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, 1));
                });
            }
        }
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("killsRequirement.previousPage"));
            addLeftAction(45, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EntityChooserInventory(plugin, prestige,page - 1)));
        }
        // NextPage
        if (entityTypes.length > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("killsRequirement.nextPage"));
            addLeftAction(53, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EntityChooserInventory(plugin, prestige, page + 1)));
        }
    }

}