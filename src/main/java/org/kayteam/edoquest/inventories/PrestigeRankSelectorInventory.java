package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class PrestigeRankSelectorInventory extends InventoryBuilder {

    private int page = 1;

    public PrestigeRankSelectorInventory(EdoQuest plugin, Prestige prestige, Player player) {
        super(plugin.getInventories().getString("prestigeRankSelector.title"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        // Return :D
        addItem(0, () -> inventories.getItemStack("prestigeRankSelector.items.return"));
        addLeftAction(37, ((player1, i) -> plugin.getInventoryManager().openInventory(player1, new PrestigeEditorInventory(plugin,prestige, player1))));
        // Close :D
        addItem(8, () -> inventories.getItemStack("prestigeRankSelector.items.close"));
        addLeftAction(40, ((player1, i) -> player.closeInventory()));
        // Ranks

        // Previous Page
        addItem(45, () -> inventories.getItemStack("prestigeRankSelector.items.previousPage"));
        addLeftAction(45, ((player1, i) -> {

        }));
        // Next Page
        addItem(53, () -> inventories.getItemStack("prestigeRankSelector.items.nextPage"));
    }
}
