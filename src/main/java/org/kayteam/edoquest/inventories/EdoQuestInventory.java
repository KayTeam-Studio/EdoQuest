package org.kayteam.edoquest.inventories;

import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.questcomplete.SettingsInventory;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EdoQuestInventory extends InventoryBuilder {

    private final EdoQuest plugin;

    public EdoQuestInventory(EdoQuest plugin) {
        super(plugin.getInventories().getString("edoQuest.title", "EdoQuest Management"), 6);
        this.plugin = plugin;
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("edoQuest.items.panel"));
        // Close
        addItem(10, () -> inventories.getItemStack("edoQuest.items.close"));
        addLeftAction(10, (player, slot) -> player.closeInventory());
        // Reload
        addItem(16, () -> inventories.getItemStack("edoQuest.items.reload"));
        addLeftAction(16, (player, slot) -> {
            player.closeInventory();
            plugin.onReload();
        });
        // Prestigies
        addItem(12, () -> Yaml.replace(inventories.getItemStack("edoQuest.items.prestigies")));
        addLeftAction(12, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, player, 1)));
        // Quest Complete Settings
        addItem(13, () -> inventories.getItemStack("edoQuest.items.questCompleteSettings"));
        addLeftAction(13, (player, i) -> plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin)));
    }

}