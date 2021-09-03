package org.kayteam.edoquest.inventories.questcomplete;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.EdoQuestInventory;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class SettingsInventory extends InventoryBuilder {

    public SettingsInventory(EdoQuest plugin) {
        super(plugin.getInventories().getString("questComplete.settings.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("questComplete.settings.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("questComplete.settings.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EdoQuestInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("questComplete.settings.return"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // SoundEnabled
        addItem(13, () -> inventories.getItemStack("questComplete.settings.soundEnabled"));
        // Sound
        addItem(22, () -> inventories.getItemStack("questComplete.settings.sound"));
        addLeftAction(22, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin, 1)));
        addRightAction(22, (player, i) -> {
            try {
                Sound sound = Sound.valueOf(plugin.getSettings().getString("questComplete.sound"));
                player.playSound(player.getLocation(), sound, 1, 1);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().info("The 'questComplete.sound' in 'settings.yml' contain a invalid sound!");
            } catch (NullPointerException e) {
                plugin.getLogger().info("The 'questComplete.sound' in 'settings.yml' no exist!");
            }
        });
        // TitleEnabled
        String titleStatus;
        if (plugin.getSettings().getBoolean("questComplete.title.enabled")) {
            titleStatus = inventories.getString("questComplete.texts.enabled");
        } else {
            titleStatus = inventories.getString("questComplete.texts.disabled");
        }
        addItem(12, () -> Yaml.replace(inventories.getItemStack("questComplete.settings.titleEnabled"), new String[][] {{"%status%", titleStatus}}));
        addLeftAction(12, (player, slot) -> {
            plugin.getSettings().set("questComplete.title.enabled", !plugin.getSettings().getBoolean("questComplete.title.enabled"));
            plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin));
        });
        // TitleText
        addItem(21, () -> inventories.getItemStack("questComplete.settings.titleText"));
        addLeftAction(21, (player, slot) -> {
            player.closeInventory();
            plugin.getMessages().sendMessage(player, "questComplete.titleTextInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    plugin.getSettings().set("questComplete.title.text", input);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) { plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin)); }
            });
        });
        // TitleSubText
        addItem(30, () -> inventories.getItemStack("questComplete.settings.subText"));
        addLeftAction(30, (player, slot) -> {
            player.closeInventory();
            plugin.getMessages().sendMessage(player, "questComplete.titleSubTextInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    plugin.getSettings().set("questComplete.title.subText", input);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) { plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin)); }
            });
        });
    }

}