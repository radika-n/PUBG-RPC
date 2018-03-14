/*
NOTE: I've based this off of an example provided by MinnDevelopment | https://github.com/MinnDevelopment
 */

package me.joezwet.pubg.rpc;

import club.minnced.discord.rpc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WindowTest {

    public static void main(String args[]) {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordRichPresence presence = new DiscordRichPresence();
        String applicationId = "421514688787120149";
        String steamId = "578080"; // PUBG Steam ID so when you click the game name in Discord it shows the game dialog.

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = () -> System.out.println("Ready!");

        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "In the Menus";
        presence.state = "Waiting...";
        presence.largeImageKey = "loc_menu";
        presence.largeImageText = "Main Menu";
        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        t.start();

        JFrame frame = new JFrame("PUBG Discord RPC Concept by JoeZwet#6252");
        GridLayout frameLayout = new GridLayout(2, 1);
        frame.setLayout(frameLayout);

        JPanel top = new JPanel();
        GridLayout topLayout = new GridLayout(9, 2);
        top.setLayout(topLayout);


        JPanel bottom = new JPanel();
        GridLayout botLayout = new GridLayout(3, 3);
        bottom.setLayout(botLayout);

        //Server Selector
        String servers[] = {"North America", "Europe", "Korea/Japan", "Oceania", "Asia", "South and Central America", "South East Asia"};
        JLabel serverLable = new JLabel("Server ");
        serverLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JComboBox server = new JComboBox(servers);

        //Match Type
        String matchTypes[] = {"Menu", "Public", "Public Custom", "Private Custom"};
        JLabel matchLable = new JLabel("Match Type ");
        matchLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JComboBox match = new JComboBox(matchTypes);

        //Team Type
        String teamTypes[] = {"Solo", "Duo", "Squad", "One-Man Squad", "Two-Man Squad", "Three-Man Squad"};
        JLabel teamLable = new JLabel("Team Type ");
        teamLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JComboBox teamType = new JComboBox(teamTypes);

        //Perspective
        String perspectiveTypes[] = {"First Person", "Third Person"};
        JLabel perspectiveLable = new JLabel("Perspective ");
        perspectiveLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JComboBox perspective = new JComboBox(perspectiveTypes);

        //party/group
        JLabel partySizeLable = new JLabel("Party Members");
        partySizeLable.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel partyMaxLable = new JLabel("Party Max");
        partyMaxLable.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField partySizeText = new JTextField("0");
        JLabel partyLabel = new JLabel("of"); partyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField partyMaxText = new JTextField("0");

        //images
        String locationList[] = {"Menu", "Erangel", "Miramar"};
        JComboBox location = new JComboBox(locationList);
        JLabel locLable = new JLabel("Location/Map ");
        locLable.setHorizontalAlignment(SwingConstants.RIGHT);


        JLabel smallImageLable = new JLabel("Small Image Key ");
        smallImageLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField smallImage = new JTextField("players");
        JLabel smallTextLable = new JLabel("Small Image Text ");
        smallTextLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField smallText = new JTextField("Team: P1, P2, P3, P4");

        //timestamp settings
        JLabel resetLable = new JLabel("Reset start timestamp ");
        resetLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JCheckBox resetTimestampBox = new JCheckBox();
        JLabel hideLable = new JLabel("Hide time counter ");
        hideLable.setHorizontalAlignment(SwingConstants.RIGHT);
        JCheckBox hideTimestampBox = new JCheckBox();



        JButton submit = new JButton("Update Presence");
        submit.addActionListener(e -> {
            String details = match.getSelectedItem().toString() + " | " + server.getSelectedItem().toString();
            String state = "In Game | " + teamType.getSelectedItem().toString() + " " + perspective.getSelectedItem().toString();

            switch (location.getSelectedIndex()) {
                case 0:
                    presence.largeImageKey = "loc_menu";
                    presence.largeImageText = "In the Main Menu";
                    presence.details = "In the Menus";
                    presence.state = "Waiting...";
                    break;
                case 1:
                    presence.largeImageKey = "loc_erangel";
                    presence.largeImageText = "Map: Erangel";
                    presence.details = details;
                    presence.state = state;
                    break;
                case 2:
                    presence.largeImageKey = "loc_miramar";
                    presence.largeImageText = "Map: Miramar";
                    presence.details = details;
                    presence.state = state;
                    break;
                default:
                    presence.largeImageKey = "loc_erangel";
                    presence.largeImageText = "Map: Erangel";
                    presence.details = details;
                    presence.state = state;
                    break;

            }

            if(resetTimestampBox.isSelected()) {
                resetTimestampBox.setSelected(false);
                presence.startTimestamp = System.currentTimeMillis() / 1000;
            }
            if(hideTimestampBox.isSelected()) {
                presence.startTimestamp = 0;
            }
            try { presence.partySize = Integer.parseInt(partySizeText.getText()); } catch (Exception ignored) { presence.partySize = 0; }
            try { presence.partyMax  = Integer.parseInt(partyMaxText.getText());  } catch (Exception ignored) { presence.partyMax = 0; }

            lib.Discord_UpdatePresence(presence);
        });

        top.add(locLable);
        top.add(location);
        top.add(serverLable);
        top.add(server);
        top.add(matchLable);
        top.add(match);
        top.add(teamLable);
        top.add(teamType);
        top.add(perspectiveLable);
        top.add(perspective);
        top.add(smallImageLable);
        top.add(smallImage);
        top.add(smallTextLable);
        top.add(smallText);
        top.add(resetLable);
        top.add(resetTimestampBox);
        top.add(hideLable);
        top.add(hideTimestampBox);

        bottom.add(partySizeLable);
        bottom.add(new JPanel());
        bottom.add(partyMaxLable);
        bottom.add(partySizeText);
        bottom.add(partyLabel);
        bottom.add(partyMaxText);
        bottom.add(new JPanel());
        bottom.add(submit);
        bottom.add(new JPanel());

        frame.add(top);
        frame.add(bottom);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().setVisible(false);
                t.interrupt();
            }
        });

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}