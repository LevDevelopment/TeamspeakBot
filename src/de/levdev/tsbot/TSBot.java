package de.levdev.tsbot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import de.levdev.tsbot.utils.Team;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Levin on 31.05.2016.
 */

public class TSBot {

    private static final String PREFIX = "[[color=black]MidnightGames[color=black]Bot] ";
    private static final String KICK_MESSAGE_NAME = "Dieser Name ist nicht erlaubt!";

    private static ArrayList<String> forbiddenWords = new ArrayList<>();

    private static ArrayList<String> support_channels = new ArrayList<>();

    private static void initRanks(){
        Team.getAdministrators().add("nekxiz");
        Team.getAdministrators().add("luke67893");

        /* Add more team-members */
    }

    private static void initChannels(){
        /* Add more support channels */
        //TODO
    }

    private static void initForbiddenWords(){
        forbiddenWords.add("noob");
    }

    public static void main(String[] args) {

            System.out.println("The teamspeak-bot is starting...");
            final TS3Config config = new TS3Config();
            config.setHost("178.238.237.170"); // Your ip (no domain! IPv4)
            config.setQueryPort(10011); // 10011 is the standard port
            System.out.println("TS3-Config successfully loaded!");

            final TS3Query query = new TS3Query(config);
            query.connect();
            System.out.println("Query successfully loaded!");

            final TS3Api api = query.getApi();
            api.login("bot", ""); // Query login-name (most of the time 'serveradmin'), password
            api.selectVirtualServerByPort(9987);
            api.setNickname("RealisticBot");
            api.sendServerMessage(PREFIX + "Der RealisticBot wurde gestartet!");
            api.registerAllEvents();

        initRanks();
        initChannels();
        initForbiddenWords();

        api.getClients().forEach(client -> api.sendPrivateMessage(client.getId(), PREFIX + "Der Bot wurde gestartet!"));

        api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent textMessageEvent){

                if (textMessageEvent.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    switch (textMessageEvent.getMessage().toLowerCase()){
                        case "test":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Test erfolgreich");
                            break;
                        case "hallo":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Hallo " + textMessageEvent.getInvokerName() + "!");
                            break;
                        case "hey":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Hallo " + textMessageEvent.getInvokerName() + "!");
                            break;
                        case "ich habe eine frage":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Falls du Fragen hast, schreibe den MidnightGamesBot mit 'support' oder 'hilfe' an!");
                            break;
                        case "support":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst sobald ein Mitarbeiter vom Support anwesend ist, mit ihm verbunden!");
                            for(Client client : api.getClients()){
                                if(Team.getTeam().contains(client.getNickname().toLowerCase())){
                                    api.pokeClient(client.getId(), "Im Support-Channel benötigt ein Spieler [" + textMessageEvent.getInvokerName() + "] Hilfe!");
                                }
                            }

                            for(Channel channel : api.getChannels()){
                                if(channel.getName().contains("» Support I")){ // Place the name of the first support-channel here
                                    if(channel.getTotalClients() == 0){
                                        api.moveClient(textMessageEvent.getInvokerId(), channel.getId());
                                        api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst in einen freien Warteraum verschoben...");
                                        return;
                                    }
                                } else if(channel.getName().contains("» Support II")){ // Place the name of the second support-channel here
                                    api.moveClient(textMessageEvent.getInvokerId(), channel.getId());
                                    api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst in einen freien Warteraum verschoben...");
                                    return;
                                }
                            }

                            break;
                        case "hilfe":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst sobald ein Mitarbeiter vom Support anwesend ist, mit ihm verbunden!");
                            for(Client client : api.getClients()){
                                if(Team.getTeam().contains(client.getNickname().toLowerCase())) {
                                    api.pokeClient(client.getId(), "Im Support-Channel benötigt ein Spieler [" + textMessageEvent.getInvokerName() + "] Hilfe!");
                                }
                            }

                            for(Channel channel : api.getChannels()){
                                if(channel.getName().contains("» Support I")){ // Place the name of the first support-channel here
                                    if(channel.getTotalClients() == 0){
                                        api.moveClient(textMessageEvent.getInvokerId(), channel.getId());
                                        api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst in einen freien Warteraum verschoben...");
                                        return;
                                    }
                                } else if(channel.getName().contains("» Support II")){ // Place the name of the second support-channel here
                                    api.moveClient(textMessageEvent.getInvokerId(), channel.getId());
                                    api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du wirst in einen freien Warteraum verschoben...");
                                    return;
                                }
                            }

                            break;

                        case "!info":
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Der TeamspeakBot wurde von LevDev entwickelt.");
                            break;

                    }

                    if(Team.getAdministrators().contains(textMessageEvent.getInvokerName().toLowerCase())){
                        if(textMessageEvent.getMessage().toLowerCase().startsWith("!bc")){
                            api.sendServerMessage(PREFIX + textMessageEvent.getMessage().replace("!bc ", ""));
                        }
                        if(textMessageEvent.getMessage().toLowerCase().startsWith("!broadcast")){
                            api.sendServerMessage(PREFIX + textMessageEvent.getMessage().replace("!broadcast ", ""));
                        }
                        if(textMessageEvent.getMessage().toLowerCase().startsWith("!online")){
                            api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Momentan sind " + api.getClients().size() + " Spieler online!");
                        }
                    }

                    /*if(administrators.contains(textMessageEvent.getInvokerName().toLowerCase())){
                        if(textMessageEvent.getMessage().toLowerCase().equals("!end")){
                            for(Client client : api.getClients()){
                                for(Channel channel : api.getChannels()){
                                    if(channel.getName().contains("")){
                                        api.moveClient(client.getId(), channel.getId());
                                    } else {
                                        api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Du bist momentan nicht im Support-Channel!");
                                        return;
                                    }
                                }
                            }
                        }
                        if(textMessageEvent.getMessage().toLowerCase().equals("!opensupport")){
                            for(Channel channel : api.getChannels()){

                            }
                        }
                    }*/ //TODO in the next update :D
                }

                if(textMessageEvent.getTargetMode() != TextMessageTargetMode.SERVER) {
                    if (forbiddenWords.contains(textMessageEvent.getMessage().toLowerCase())) {
                        api.banClient(textMessageEvent.getInvokerId(), 3600, "Wortwahl ('" + textMessageEvent.getMessage() + "')");
                    } else if (textMessageEvent.getMessage().equalsIgnoreCase("bot")) {
                        api.sendPrivateMessage(textMessageEvent.getInvokerId(), "Hier bin ich!");
                    }
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                if(forbiddenWords.contains(clientJoinEvent.getInvokerName().toLowerCase())){
                    api.kickClientFromServer(KICK_MESSAGE_NAME, clientJoinEvent.getClientId());
                    return;
                }
                api.sendPrivateMessage(clientJoinEvent.getClientId(), "Herzlich Willkommen " + clientJoinEvent.getClientNickname()
                        + "!");
                api.sendPrivateMessage(clientJoinEvent.getClientId(), "Wir wünschen Dir einen unterhaltsamen Aufenthalt auf unserem Teamspeak!");
                api.sendPrivateMessage(clientJoinEvent.getClientId(), "Falls du Fragen hast, schreibe den Bot mit 'support' oder 'hilfe' an!");


                if(Team.getAdministrators().contains(clientJoinEvent.getClientNickname().toLowerCase())) {
                    int id = clientJoinEvent.getClientId();
                    if (api.getClientInfo(id).getDescription().toLowerCase().contains("admin") || api.getClientInfo(id).getDescription().toLowerCase().contains("dev")) {
                        api.sendPrivateMessage(id, "==========> INFOS <==========");
                        api.sendPrivateMessage(id, "Momentan sind " + (api.getClients().size() - 1) + " Spieler online!");
                        api.sendPrivateMessage(id, "Momentan sind " + api.getChannels().size() + " Channel aktiv!");
                    } else {
                        api.kickClientFromServer(KICK_MESSAGE_NAME, clientJoinEvent.getClientId());
                    }
                }

            }

            @Override
            public void onClientLeave(ClientLeaveEvent clientLeaveEvent){

            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {

            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

            }

            @Override
            public void onClientMoved(ClientMovedEvent clientMovedEvent) {

            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
                for(Client client : api.getClients()){
                    if(Team.getAdministrators().contains(client.getNickname().toLowerCase())){
                        api.sendPrivateMessage(client.getId(), "[color=blue]Es wurde ein Channel erstellt!");
                        api.sendPrivateMessage(client.getId(), "[color=blue]Ersteller: [color=red]" + channelCreateEvent.getInvokerName());
                        api.sendPrivateMessage(client.getId(), "[color=blue]IP-Adresse: [color=red]" + api.getClientInfo(channelCreateEvent.getInvokerId()).getIp());
                        api.sendPrivateMessage(client.getId(), "_____________________________________________________________________");
                    }
                }
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
                for(Client client : api.getClients()){
                    if(Team.getAdministrators().contains(client.getNickname().toLowerCase())){
                        api.sendPrivateMessage(client.getId(), "[color=blue]Es wurde ein Channel gelöscht!");
                        api.sendPrivateMessage(client.getId(), "[color=blue]Ersteller: [color=red]" + channelDeletedEvent.getInvokerName());
                        api.sendPrivateMessage(client.getId(), "[color=blue]IP-Adresse: [color=red]" + api.getClientInfo(channelDeletedEvent.getInvokerId()).getIp());
                        api.sendPrivateMessage(client.getId(), "_____________________________________________________________________");
                    }
                }
            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent event){

            }
        });

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            for(Client client : api.getClients()){
                if(client.isOutputMuted()){
                    for(Channel channel : api.getChannels()) {
                        if(Team.getTeam().contains(client.getNickname().toLowerCase()) == false) {
                            if (channel.getName().equals("")) { // Place here the name of the AFK-Channel
                                if (client.getChannelId() != channel.getId()) {
                                    api.moveClient(client.getId(), channel.getId());
                                    api.sendPrivateMessage(client.getId(), "Du wurdest in den AFK-Channel gemoved, da deine Lautsprecher deaktiviert sind!");
                                }
                            }
                        } else {
                            //TODO
                        }
                    }
                }
            }
        },10L, 10L, TimeUnit.SECONDS);

        ScheduledExecutorService executorService3 = Executors.newSingleThreadScheduledExecutor();
        executorService3.scheduleAtFixedRate(() -> {
            api.sendServerMessage(PREFIX + "MidnightGames hat auch einen Minecraft-Server!");
            api.sendServerMessage(PREFIX + "Wir würden uns sehr freuen, wenn du uns besuchen würdest. Die IP wird lauten: [color=purple]play.midnightgames.net");
        }, 0L, 1L, TimeUnit.HOURS);

        ScheduledExecutorService executorService4 = Executors.newSingleThreadScheduledExecutor();
        executorService4.scheduleAtFixedRate(() -> {
            for(Client client : api.getClients()){
                if(client.getNickname().contains("[Supporter]") ||
                        client.getNickname().contains("[Moderator]") ||
                        client.getNickname().contains("[Mod]") ||
                        client.getNickname().contains("[Admin]") ||
                        client.getNickname().contains("[Developer") ||
                        client.getNickname().contains("[Dev]") ||
                        client.getNickname().contains("[TS-Supporter]") ||
                        client.getNickname().contains("[TS-Moderator]") ||
                        client.getNickname().contains("[TS-Mod]") ||
                        client.getNickname().contains("[Sr-Moderator]") ||
                        client.getNickname().contains("[Sr-Mod]") ||
                        client.getNickname().contains("[Builder]") ||
                        client.getNickname().contains("[Head-Builder]")) {
                            api.banClient(client.getId(), 300, KICK_MESSAGE_NAME);
                }
            }
        }, 2L, 2L, TimeUnit.SECONDS);

    }
}
