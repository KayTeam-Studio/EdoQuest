package org.kayteam.edoquest.prestige;

import org.kayteam.edoquest.prestige.requirement.KillsRequirement;
import org.kayteam.edoquest.prestige.requirement.RankRequirement;

public class Prestige {

    public Prestige(String name) {
        this.name = name;
    }

    private final String name;
    public String getName() {
        return name;
    }

    private String displayName = "";
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    private String prestigeRank = "";
    public void setPrestigeRank(String prestigeRank) {
        this.prestigeRank = prestigeRank;
    }
    public String getPrestigeRank() {
        return prestigeRank;
    }

    private final RankRequirement rankRequirement = new RankRequirement();
    public RankRequirement getRankRequirement() {
        return rankRequirement;
    }

    private final KillsRequirement killsRequirement = new KillsRequirement();
    public KillsRequirement getKillsRequirement() {
        return killsRequirement;
    }

}