package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

import java.util.List;

public class PrestigiesInventory extends InventoryBuilder {

    private final EdoQuest plugin;
    private int page = 1;

    public PrestigiesInventory(EdoQuest plugin, Player player) {
        super(plugin.getInventories().getString("prestigies.title", "EdoQuest Management"), 6);
        this.plugin = plugin;
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("prestigies.items.panel"));
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
        addItem(44, () -> inventories.getItemStack("prestigies.items.previousPage"));
        // createPrestige
        addItem(48, () -> inventories.getItemStack("prestigies.items.createPrestige"));
        // Next Page
        addItem(53, () -> inventories.getItemStack("prestigies.items.nextPage"));
    }

    @Override
    public void onReload() {
        Yaml inventories = plugin.getInventories();
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
    }

}