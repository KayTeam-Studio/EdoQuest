package org.kayteam.edoquest.inventories.prestigeeditor;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.PrestigiesInventory;
import org.kayteam.edoquest.prestige.Prestige;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class PrestigeEditorInventory extends InventoryBuilder {

    public PrestigeEditorInventory(EdoQuest plugin, Prestige prestige) {
        super(plugin.getInventories().getString("prestige.editor.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("prestige.editor.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("prestige.editor.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, 1)));
        // Close
        addItem(8, () -> inventories.getItemStack("prestige.editor.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // DisplayName
        addItem(10, () -> Yaml.replace(inventories.getItemStack("prestige.editor.displayName"), new String[][] {{"%displayName%", prestige.getDisplayName()}}));
        addLeftAction(10, (player, slot) -> {
            player.closeInventory();
            Yaml messages = plugin.getMessages();
            messages.sendMessage(player, "prestigeEditor.displayName.input");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    if (input.length() < 33) {
                        prestige.setDisplayName(input);
                        plugin.getPrestigeManager().savePrestige(prestige.getName());
                        plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige));
                        return true;
                    } else {
                        messages.sendMessage(player, "prestigeEditor.displayName.tooLong");
                    }
                    return false;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PrestigeEditorInventory(plugin, prestige));
                }
            });
        });
        // Commands
        addItem(12, () -> inventories.getItemStack("prestige.editor.commands"));
        addLeftAction(12, (player, slot) -> plugin.getInventoryManager().openInventory(player, new CommandsInventory(plugin, prestige, 1)));
        // RequirementKills
        addItem(14, () -> inventories.getItemStack("prestige.editor.requirementKills"));
        addLeftAction(14, (player, slot) -> plugin.getInventoryManager().openInventory(player, new KillsRequirementInventory(plugin, prestige, 1)));
        // Delete :D
        addItem(16, () -> inventories.getItemStack("prestige.editor.delete"));
        addLeftShiftAction(16, (player, slot) -> {
            plugin.getPrestigeManager().deletePrestige(prestige.getName());
            plugin.getInventoryManager().openInventory(player, new PrestigiesInventory(plugin, 1));
        });
    }

}