package org.kayteam.edoquest.prestige.requirement;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KillsRequirement extends Requirement{

    public KillsRequirement() {
        super("kills");
    }

    private HashMap<EntityType, Integer> entities = new HashMap<>();
    public List<EntityType> getEntities() {
        return new ArrayList<>(entities.keySet());
    }
    public void addEntity(EntityType entityType, int amount) {
        entities.put(entityType, amount);
    }
    public void removeEntity(EntityType entityType) {
        entities.remove(entityType);
    }
    public int getAmount(EntityType entityType) {
        return entities.get(entityType);
    }
    public boolean containEntity(EntityType entityType) {
        return entities.containsKey(entityType);
    }

}