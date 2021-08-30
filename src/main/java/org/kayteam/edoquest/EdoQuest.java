package org.kayteam.edoquest;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.edoquest.prestige.PrestigeManager;
import org.kayteam.edoquest.util.kayteam.KayTeam;
import org.kayteam.edoquest.util.yaml.Yaml;

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
    private final Yaml prestigies = new Yaml(this, "prestigies");
    public Yaml getPrestigies() {
        return prestigies;
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
        KayTeam.sendBrandMessage(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        KayTeam.sendBrandMessage(this, "&cDisabled");
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

    }

    private void registerCommands() {

    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> registeredServiceProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (registeredServiceProvider != null) {
            permission = registeredServiceProvider.getProvider();
        }
        return permission != null;
    }

}