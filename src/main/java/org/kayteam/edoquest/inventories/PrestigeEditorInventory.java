package org.kayteam.edoquest.inventories;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class PrestigeEditorInventory extends InventoryBuilder {

    public PrestigeEditorInventory(EdoQuest plugin, Prestige prestige, Player player) {
        super(plugin.getInventories().getString("prestigeEditor.title"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("prestigeEditor.items.panel"));
        // Return :D
        addItem(37, () -> inventories.getItemStack("prestigeEditor.items.return"));
        addLeftAction(37, ((player1, i) -> plugin.getInventoryManager().openInventory(player1, new PrestigiesInventory(plugin, player1, 1))));
        // Close :D
        addItem(40, () -> inventories.getItemStack("prestigeEditor.items.close"));
        addLeftAction(40, ((player1, i) -> player.closeInventory()));
        // Delete :D
        addItem(43, () -> inventories.getItemStack("prestigeEditor.items.delete"));
        addLeftShiftAction(43, ((player1, i) -> {
            plugin.getPrestigeManager().deletePrestige(prestige.getName());
            plugin.getInventoryManager().openInventory(player1, new PrestigiesInventory(plugin, player1, 1));
        }));
        // DisplayName :D
        addItem(10, () -> Yaml.replace(inventories.getItemStack("prestigeEditor.items.displayName"), new String[][] {{"%displayName%", prestige.getDisplayName()}}));
        addLeftAction(10, ((player1, i) -> {
            player1.closeInventory();
            Yaml messages = plugin.getMessages();
            messages.sendMessage(player, "prestigeEditor.displayName.input");
            plugin.getInputManager().addInput(player1, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    if (input.length() < 33) {
                        prestige.setDisplayName(input);
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige, player));
                        return true;
                    } else {
                        messages.sendMessage(player, "prestigeEditor.displayName.tooLong");
                    }
                    return false;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige, player));
                }
            });
        }));
        // RankToPrestige :D
        addItem(11, () -> Yaml.replace(inventories.getItemStack("prestigeEditor.items.prestigeRank"), new String[][] {{"%prestigeRank%", prestige.getPrestigeRank()}}));
        addLeftAction(11, (player1, i) -> plugin.getInventoryManager().openInventory(player1, new PrestigeRankSelectorInventory(plugin, prestige, player, 1)));
        // RequirementKills
        addItem(15, () -> inventories.getItemStack("prestigeEditor.items.requirementKills"));
        addLeftAction(15, (player1, i) -> plugin.getInventoryManager().openInventory(player1, new KillsRequirementInventory(plugin, prestige, player, 1)));
    }

}