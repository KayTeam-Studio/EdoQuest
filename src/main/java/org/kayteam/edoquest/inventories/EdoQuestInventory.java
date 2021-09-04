package org.kayteam.edoquest.inventories;

import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.placeholdersapi.PlaceholdersAPISettingsInventory;
import org.kayteam.edoquest.inventories.questcomplete.QuestCompleteSettingsInventory;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EdoQuestInventory extends InventoryBuilder {

    public EdoQuestInventory(EdoQuest plugin) {
        super(plugin.getInventories().getString("edoQuest.inventoryTitle", "EdoQuest Management"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("edoQuest.panel"));
        // Close
        addItem(10, () -> inventories.getItemStack("edoQuest.close"));
        addLeftAction(10, (player, slot) -> player.closeInventory());
        // Prestigies
        addItem(12, () -> Yaml.replace(inventories.getItemStack("edoQuest.prestigies")));
        addLeftAction(12, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, 1)));
        // Quest Complete Settings
        addItem(14, () -> inventories.getItemStack("edoQuest.questCompleteSettings"));
        addLeftAction(14, (player, slot) -> plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin)));
        // Reload
        addItem(16, () -> inventories.getItemStack("edoQuest.reload"));
        addLeftAction(16, (player, slot) -> {
            player.closeInventory();
            plugin.onReload();
        });
        // PlaceholdersAPI
        addItem(31, () -> inventories.getItemStack("edoQuest.placeholdersSettings"));
        addLeftAction(31, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin)));
    }

}