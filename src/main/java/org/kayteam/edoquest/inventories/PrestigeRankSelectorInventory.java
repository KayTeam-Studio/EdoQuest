package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class PrestigeRankSelectorInventory extends InventoryBuilder {

    public PrestigeRankSelectorInventory(EdoQuest plugin, Prestige prestige, Player player, int page) {
        super(plugin.getInventories().getString("prestigeRankSelector.title"), 6);
        final int page1 = page;
        Yaml inventories = plugin.getInventories();
        for (int i = 1; i < 8; i++) addItem(i, () -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        for (int i = 46; i < 53; i++) addItem(i, () -> inventories.getItemStack("prestigeRankSelector.items.panel"));
        // Return :D
        addItem(0, () -> inventories.getItemStack("prestigeRankSelector.items.return"));
        addLeftAction(37, ((player1, i) -> plugin.getInventoryManager().openInventory(player1, new PrestigeEditorInventory(plugin,prestige, player1))));
        // Close :D
        addItem(8, () -> inventories.getItemStack("prestigeRankSelector.items.close"));
        addLeftAction(40, ((player1, i) -> player.closeInventory()));
        // Ranks
        String[] groups = EdoQuest.getPermissions().getGroups();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < groups.length) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("prestigeRankSelector.items.rank"), new String[][] {
                        {"%rank%", groups[index]}
                }));
                addLeftAction(i, ((player1, slot) -> {
                    prestige.setPrestigeRank(groups[index]);
                    plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige, player1));
                }));
            }
        }
        // Previous Page
        addItem(45, () -> inventories.getItemStack("prestigeRankSelector.items.previousPage"));
        addLeftAction(45, ((player1, i) -> {
            if (page1 > 1) {
                plugin.getInventoryManager().openInventory(player, new PrestigeRankSelectorInventory(plugin, prestige, player, page1 - 1));
            }
        }));
        // Next Page
        addItem(53, () -> inventories.getItemStack("prestigeRankSelector.items.nextPage"));
        addLeftAction(53, ((player1, i) -> {
            int amount = page1 * (4 * 9);
            if (groups.length > amount) {
                plugin.getInventoryManager().openInventory(player, new PrestigeRankSelectorInventory(plugin, prestige, player, page1 + 1));
            }
        }));
    }
}
