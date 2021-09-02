package org.kayteam.edoquest;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.edoquest.commands.EdoQuestCommand;
import org.kayteam.edoquest.listeners.EntityDeathListener;
import org.kayteam.edoquest.listeners.PlayerJoinListener;
import org.kayteam.edoquest.listeners.QuestCompleteListener;
import org.kayteam.edoquest.placeholderapi.EdoQuestExpansion;
import org.kayteam.edoquest.prestige.PrestigeManager;
import org.kayteam.kayteamapi.BrandSender;
import org.kayteam.kayteamapi.input.InputManager;
import org.kayteam.kayteamapi.inventory.InventoryManager;
import org.kayteam.kayteamapi.yaml.Yaml;

public final class EdoQuest extends JavaPlugin {

    // Files
    private final Yaml settings = new Yaml(this, "settings");
    public Yaml getSettings() {
        return settings;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }
    private final Yaml inventories = new Yaml(this, "inventories");
    public Yaml getInventories() {
        return inventories;
    }
    private final Yaml prestigies = new Yaml(this, "prestiges");
    public Yaml getPrestigies() {
        return prestigies;
    }

    // Inventory Manager
    private final InventoryManager inventoryManager = new InventoryManager(this);
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    // Input Manager
    private final InputManager inputManager = new InputManager();
    public InputManager getInputManager() {
        return inputManager;
    }

    // Vault Permission
    private static Permission permission;
    public static Permission getPermissions() {
        return permission;
    }

    // Prestige Manager
    private final PrestigeManager prestigeManager = new PrestigeManager(this);
    public PrestigeManager getPrestigeManager() {
        return prestigeManager;
    }

    @Override
    public void onEnable() {
        registerFiles();
        registerListeners();
        registerCommands();
        if (!setupPermissions()) {
            getLogger().info("This plugin required a permission plugin to work.");
            getPluginLoader().disablePlugin(this);
            return;
        }
        prestigeManager.loadPrestigies();
        prestigeManager.loadPlayersData();
        new EdoQuestExpansion(this).register();
        BrandSender.sendBrandMessage(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        BrandSender.sendBrandMessage(this, "&cDisabled");
    }

    public void onReload() {

    }

    private void registerFiles() {
        settings.registerFileConfiguration();
        messages.registerFileConfiguration();
        inventories.registerFileConfiguration();
        prestigies.registerFileConfiguration();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(inventoryManager, this);
        pluginManager.registerEvents(inputManager, this);
        pluginManager.registerEvents(new EntityDeathListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new QuestCompleteListener(this), this);
    }

    private void registerCommands() {
        new EdoQuestCommand(this);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> registeredServiceProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (registeredServiceProvider != null) {
            permission = registeredServiceProvider.getProvider();
        }
        return permission != null;
    }

}