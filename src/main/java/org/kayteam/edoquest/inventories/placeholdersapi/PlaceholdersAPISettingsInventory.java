package org.kayteam.edoquest.inventories.placeholdersapi;

import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.EdoQuestInventory;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class PlaceholdersAPISettingsInventory extends InventoryBuilder {

    public PlaceholdersAPISettingsInventory(EdoQuest plugin) {
        super(plugin.getInventories().getString("placeholderapi.inventoryTitle"), 6);
        Yaml settings = plugin.getSettings();
        Yaml messages = plugin.getMessages();
        Yaml inventories = plugin.getInventories();
        // Panel
        fillItem(() -> inventories.getItemStack("placeholderapi.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("placeholderapi.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EdoQuestInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("placeholderapi.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // Max Prestige Reached
        addItem(12, () -> Yaml.replace(inventories.getItemStack("placeholderapi.maxPrestigeReached"), new String[][] {
                {"%text%", settings.getString("placeholders.maxPrestigeReached")}
        }));
        addLeftAction(12, (player, slot) -> {
            player.closeInventory();
            messages.sendMessage(player, "placeholderapi.inputMaxPrestigeReachedText");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    settings.set("placeholders.maxPrestigeReached", input);
                    settings.saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                }
            });
        });
        // No Prestige
        addItem(14, () -> Yaml.replace(inventories.getItemStack("placeholderapi.noPrestige"), new String[][] {
                {"%text%", settings.getString("placeholders.noPrestige")}
        }));
        addLeftAction(14, (player, slot) -> {
            player.closeInventory();
            messages.sendMessage(player, "placeholderapi.inputNoPrestigeText");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    settings.set("placeholders.noPrestige", input);
                    settings.saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                }
            });
        });
        // Locked Status
        addItem(30, () -> Yaml.replace(inventories.getItemStack("placeholderapi.lockedStatus"), new String[][] {
                {"%text%", settings.getString("placeholders.prestigeStatus.locked")}
        }));
        addLeftAction(30, (player, slot) -> {
            player.closeInventory();
            messages.sendMessage(player, "placeholderapi.inputLockedPrestigeText");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    settings.set("placeholders.prestigeStatus.locked", input);
                    settings.saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                }
            });
        });

        // Unlocked Status
        addItem(32, () -> Yaml.replace(inventories.getItemStack("placeholderapi.unlockedStatus"), new String[][] {
                {"%text%", settings.getString("placeholders.prestigeStatus.unlocked")}
        }));
        addLeftAction(32, (player, slot) -> {
            player.closeInventory();
            messages.sendMessage(player, "placeholderapi.inputUnlockedPrestigeText");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    settings.set("placeholders.prestigeStatus.unlocked", input);
                    settings.saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) {
                    plugin.getInventoryManager().openInventory(player, new PlaceholdersAPISettingsInventory(plugin));
                }
            });
        });
    }

}