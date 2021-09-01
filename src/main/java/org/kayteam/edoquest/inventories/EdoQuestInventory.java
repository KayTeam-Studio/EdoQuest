package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class EdoQuestInventory extends InventoryBuilder {

    private final EdoQuest plugin;

    public EdoQuestInventory(EdoQuest plugin, Player player) {
        super(plugin.getInventories().getString("edoQuest.title", "EdoQuest Management"), 6);
        this.plugin = plugin;
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("edoQuest.items.panel"));
        // Close
        addItem(10, () -> inventories.getItemStack("edoQuest.items.close"));
        addLeftAction(10, (player1, slot) -> player.closeInventory());
        // Reload
        addItem(16, () -> inventories.getItemStack("edoQuest.items.reload"));
        addLeftAction(16, (player1, slot) -> {
            player.closeInventory();
            plugin.onReload();
        });
        // Prestigies
        addItem(12, () -> Yaml.replace(inventories.getItemStack("edoQuest.items.prestigies"), player));
        addLeftAction(12, (player1, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, player1, 1)));
    }

}