package de.levdev.tsbot.utils;

import java.util.ArrayList;

/**
 * Created by Levin on 16.08.2016.
 */
public class Team {

    private static ArrayList<String> administrators = new ArrayList<>();
    private static ArrayList<String> srmoderators = new ArrayList<>();
    private static ArrayList<String> supporters = new ArrayList<>();
    private static ArrayList<String> builder = new ArrayList<>();
    private static ArrayList<String> team = new ArrayList<>();

    public static ArrayList<String> getAdministrators(){
        return administrators;
    }

    public static ArrayList<String> getSrmoderators() {
        return srmoderators;
    }

    public static ArrayList<String> getSupporters() {
        return supporters;
    }

    public static ArrayList<String> getBuilder() {
        return builder;
    }

    public static ArrayList<String> getTeam() {

        team.addAll(administrators);
        team.addAll(srmoderators);
        team.addAll(supporters);
        team.addAll(builder);

        return team;
    }

    public static void clearTeamList(){
        team.clear();
    }
}
