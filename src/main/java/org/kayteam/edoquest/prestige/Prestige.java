package org.kayteam.edoquest.prestige;

import org.jetbrains.annotations.NotNull;
import org.kayteam.edoquest.prestige.requirement.KillsRequirement;

public class Prestige implements Comparable<Prestige> {

    public Prestige(String name) {
        this.name = name;
    }

    private final String name;
    private int position = 0;
    private String displayName = "";
    private String prestigeRank = "";

    private final KillsRequirement killsRequirement = new KillsRequirement();


    public String getName() {
        return name;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setPrestigeRank(String prestigeRank) {
        this.prestigeRank = prestigeRank;
    }
    public String getPrestigeRank() {
        return prestigeRank;
    }

    public KillsRequirement getKillsRequirement() {
        return killsRequirement;
    }

    @Override
    public int compareTo(@NotNull Prestige p) {
        return Integer.compare(position, p.getPosition());
    }

}