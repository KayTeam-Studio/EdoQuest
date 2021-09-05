package org.kayteam.edoquest.inventories.questcomplete;

import org.bukkit.Sound;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;

public class SoundSelectorInventory extends InventoryBuilder {

    public SoundSelectorInventory(EdoQuest plugin, int page) {
        super(plugin.getInventories().getString("questComplete.soundSelector.inventoryTitle"), 6);
        Yaml inventories = plugin.getInventories();
        fillItem(() -> inventories.getItemStack("questComplete.soundSelector.panel"), new int[] {1, 6});
        // Return
        addItem(0, () -> inventories.getItemStack("questComplete.soundSelector.return"));
        addLeftAction(0, (player, i) -> plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin)));
        // Close
        addItem(8, () -> inventories.getItemStack("questComplete.soundSelector.close"));
        addLeftAction(8, (player, i) -> player.closeInventory());
        // Sounds
        Sound[] sounds = Sound.values();
        for (int i = 9; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < sounds.length) {
                addItem(i, () -> Yaml.replace(inventories.getItemStack("questComplete.soundSelector.sound"), new String[][] {{"%sound%", sounds[index].toString()}}));
                addLeftAction(i, (player, slot) -> {
                    plugin.getSettings().set("questComplete.sound", sounds[index].toString());
                    plugin.getSettings().saveFileConfiguration();
                    plugin.getInventoryManager().openInventory(player, new QuestCompleteSettingsInventory(plugin));
                });
                addRightAction(i, (player, slot) -> player.playSound(player.getLocation(), sounds[index], 1, 1));
            }
        }
        // PreviousPage
        if (page > 1) {
            addItem(45, () -> inventories.getItemStack("questComplete.soundSelector.previousPage"));
            addLeftAction(45, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin,page - 1)));
        }
        // NextPage
        if (sounds.length > (page * (4 * 9))) {
            addItem(53, () -> inventories.getItemStack("questComplete.soundSelector.nextPage"));
            addLeftAction(53, (player, i) -> plugin.getInventoryManager().openInventory(player, new SoundSelectorInventory(plugin, page + 1)));
        }
    }

}