package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class PrestigiesInventory extends InventoryBuilder {

    public PrestigiesInventory(EdoQuest plugin, Player player, int page) {
        super(plugin.getInventories().getString("prestigies.title"), 6);
        final int page1 = page;
        Yaml inventories = plugin.getInventories();
        for (int i = 1; i < 8; i++) addItem(i, () -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        for (int i = 46; i < 49; i++) addItem(i, () -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        for (int i = 50; i < 53; i++) addItem(i, () -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("prestigies.items.return"));
        addLeftAction(0, (player1, slot) -> plugin.getInventoryManager().openInventory(player1, new EdoQuestInventory(plugin, player)));
        // Close
        addItem(8, () -> inventories.getItemStack("prestigies.items.close"));
        addLeftAction(8, (player1, slot) -> player.closeInventory());
        // Prestigies
        List<String> prestigies = plugin.getPrestigeManager().getPrestigies();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < prestigies.size()) {
                Prestige prestige = plugin.getPrestigeManager().getPrestige(prestigies.get(index));
                addItem(i, () -> Yaml.replace(inventories.getItemStack("prestigies.items.nextPage"), new String[][] {
                        {"%name%", prestige.getName()},
                        {"%displayName%", prestige.getDisplayName()}
                }));
                addLeftAction(i, (player1, slot) -> plugin.getInventoryManager().openInventory(player1, new PrestigeEditorInventory(plugin, prestige, player1)));
            }
        }
        // Previous Page
        addItem(45, () -> inventories.getItemStack("prestigies.items.previousPage"));
        addLeftAction(45, ((player1, i) -> {
            if (page1 > 1) {
                plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, player, page1 - 1));
            }
        }));
        // createPrestige
        addItem(48, () -> inventories.getItemStack("prestigies.items.createPrestige"));
        // Next Page
        addItem(53, () -> inventories.getItemStack("prestigies.items.nextPage"));
        addLeftAction(53, ((player1, i) -> {
            int amount = page1 * (4 * 9);
            if (prestigies.size() > amount) {
                plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, player, page1 + 1));
            }
        }));
    }

}