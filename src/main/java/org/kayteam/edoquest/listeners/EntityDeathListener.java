package org.kayteam.edoquest.listeners;

import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.kayteam.edoquest.EdoQuest;
import org.kayteam.edoquest.events.QuestCompleteEvent;
import org.kayteam.edoquest.prestige.Prestige;

import java.util.ArrayList;

public class EntityDeathListener implements Listener {

    private final EdoQuest plugin;

    public EntityDeathListener(EdoQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player != null){
            Prestige actualPrestige = plugin.getPrestigeManager().getPlayerPrestige(player);
            Prestige nextPrestige;
            ArrayList<Prestige> prestigiesArrayList = new ArrayList<>(plugin.getPrestigeManager().getPrestigiesMap().values());
            if(prestigiesArrayList.size()<prestigiesArrayList.indexOf(actualPrestige)+1){
                if(actualPrestige != null){
                    try{
                        nextPrestige = prestigiesArrayList.get(prestigiesArrayList.indexOf(actualPrestige)+1);
                    }catch (Exception e){
                        return;
                    }
                }else{
                    nextPrestige = prestigiesArrayList.get(0);
                }
                boolean completed = true;
                for(EntityType entityType : nextPrestige.getKillsRequirement().getEntities()){
                    int entityKillsNeeded = nextPrestige.getKillsRequirement().getAmount(entityType);
                    if((player.getStatistic(Statistic.KILL_ENTITY, entityType)+1) < entityKillsNeeded){
                        completed = false;
                        break;
                    }
                }
                if(completed){
                    actualPrestige = nextPrestige;
                    plugin.getServer().getPluginManager().callEvent(new QuestCompleteEvent(player, nextPrestige));
                }
                plugin.getPrestigeManager().getPlayerData().put(player, actualPrestige);
            }
        }
    }
}
