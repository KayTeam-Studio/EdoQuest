package org.kayteam.edoquest.inventories.questcomplete;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.inventories.EdoQuestInventory;
import org.kayteam.kayteamapi.input.inputs.ChatInput;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class QuestCompleteSettingsInventory extends InventoryBuilder {

    public QuestCompleteSettingsInventory(EdoQuest plugin) {
        super(plugin.getInventories().getString("questComplete.settings.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("questComplete.settings.panel"));
        // Return
        addItem(0, () -> inventories.getItemStack("questComplete.settings.return"));
        addLeftAction(0, (player, slot) -> plugin.getInventoryManager().openInventory(player, new EdoQuestInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("questComplete.settings.close"));
        addLeftAction(8, (player, slot) -> player.closeInventory());
        // SoundEnabled
        if(plugin.getSettings().getBoolean("questComplete.sound.enabled")){
            addItem(13, () -> inventories.getItemStack("questComplete.settings.soundEnabled"));
        }else{
            addItem(13, () -> inventories.getItemStack("questComplete.settings.soundDisabled"));
        }
        addLeftAction(13, (player, i) -> {
            if(plugin.getSettings().getBoolean("questComplete.sound.enabled")){
                plugin.getSettings().set("questComplete.sound.enabled", false);
                plugin.getSettings().saveFileConfiguration();
            }else{
                plugin.getSettings().set("questComplete.sound.enabled", true);
                plugin.getSettings().saveFileConfiguration();
            }
            plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
        });
        // Sound Selector
        addItem(22, () -> Yaml.replace(inventories.getItemStack("questComplete.settings.sound"),
                new String[][]{{"%sound%", plugin.getSettings().getString("questComplete.sound.type")}}));
        addLeftAction(22, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin, 1)));
        addRightAction(22, (player, i) -> {
            try {
                Sound sound = Sound.valueOf(plugin.getSettings().getString("questComplete.sound.type"));
                player.playSound(player.getLocation(), sound, 1, 1);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().info("The 'questComplete.sound.type' in 'settings.yml' contain a invalid sound!");
            } catch (NullPointerException e) {
                plugin.getLogger().info("The 'questComplete.sound.type' in 'settings.yml' no exist!");
            }
        });
        // TitleEnabled
        if(plugin.getSettings().getBoolean("questComplete.title.enabled")){
            addItem(12, () -> inventories.getItemStack("questComplete.settings.titleEnabled"));
        }else{
            addItem(12, () -> inventories.getItemStack("questComplete.settings.titleDisabled"));
        }
        addLeftAction(12, (player, i) -> {
            if(plugin.getSettings().getBoolean("questComplete.title.enabled")){
                plugin.getSettings().set("questComplete.title.enabled", false);
                plugin.getSettings().saveFileConfiguration();
            }else{
                plugin.getSettings().set("questComplete.title.enabled", true);
                plugin.getSettings().saveFileConfiguration();
            }
            plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
        });
        // TitleText
        addItem(21, () -> Yaml.replace(inventories.getItemStack("questComplete.settings.titleText"),
                new String[][]{{"%title%", plugin.getSettings().getString("questComplete.title.text")}}));
        addLeftAction(21, (player, slot) -> {
            player.closeInventory();
            plugin.getMessages().sendMessage(player, "questComplete.titleTextInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    plugin.getSettings().set("questComplete.title.text", input);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) { plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin)); }
            });
        });
        // TitleSubText
        addItem(30, () -> Yaml.replace(inventories.getItemStack("questComplete.settings.titleSubText"),
                new String[][]{{"%subTitle%", plugin.getSettings().getString("questComplete.title.subText")}}));
        addLeftAction(30, (player, slot) -> {
            player.closeInventory();
            plugin.getMessages().sendMessage(player, "questComplete.titleSubTextInput");
            plugin.getInputManager().addInput(player, new ChatInput() {
                @Override
                public boolean onChatInput(Player player, String input) {
                    plugin.getSettings().set("questComplete.title.subText", input);
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
                    return true;
                }

                @Override
                public void onPlayerSneak(Player player) { plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin)); }
            });
        });
        // MessageEnabled
        if(plugin.getSettings().getBoolean("questComplete.messages.enabled")){
            addItem(14, () -> inventories.getItemStack("questComplete.settings.messagesEnabled"));
        }else{
            addItem(14, () -> inventories.getItemStack("questComplete.settings.messagesDisabled"));
        }
        addLeftAction(14, (player, i) -> {
            if(plugin.getSettings().getBoolean("questComplete.messages.enabled")){
                plugin.getSettings().set("questComplete.messages.enabled", false);
                plugin.getSettings().saveFileConfiguration();
            }else{
                plugin.getSettings().set("questComplete.messages.enabled", true);
                plugin.getSettings().saveFileConfiguration();
            }
            plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
        });
        // Messages Edit Menu
        addItem(23, () -> Yaml.replace(inventories.getItemStack("questComplete.settings.messages"),
                new String[][]{{"%amount%", String.valueOf(plugin.getSettings().getList("questComplete.messages.texts").size())}}));
        addLeftAction(23, ((player, i) -> plugin.getInventoryManager().openInventory(player, new MessagesEditorInventory(plugin, 1))));
    }

}