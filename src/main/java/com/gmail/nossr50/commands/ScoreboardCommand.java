package com.gmail.nossr50.commands;

import com.gmail.nossr50.mcMMO;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardCommand implements TabExecutor {

    private final mcMMO pluginRef;

    public ScoreboardCommand(mcMMO pluginRef) {
        this.pluginRef = pluginRef;
    }

    private static final List<String> FIRST_ARGS = ImmutableList.of("keep", "time", "clear");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (pluginRef.getCommandTools().noConsoleUsage(sender)) {
            return true;
        }

        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("reset")) {
                    pluginRef.getScoreboardManager().clearBoard(sender.getName());
                    sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Clear"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("keep")) {
                    if (!pluginRef.getScoreboardSettings().getScoreboardsEnabled()) {
                        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Disabled"));
                        return true;
                    }

                    if (!pluginRef.getScoreboardManager().isBoardShown(sender.getName())) {
                        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.NoBoard"));
                        return true;
                    }

                    pluginRef.getScoreboardManager().keepBoard(sender.getName());
                    sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Keep"));
                    return true;
                }

                return help(sender);

            case 2:
                if (args[0].equalsIgnoreCase("time") || args[0].equalsIgnoreCase("timer")) {
                    if (pluginRef.getCommandTools().isInvalidInteger(sender, args[1])) {
                        return true;
                    }

                    int time = Math.abs(Integer.parseInt(args[1]));

                    pluginRef.getScoreboardManager().setRevertTimer(sender.getName(), time);
                    sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Timer", time));
                    return true;
                }

                return help(sender);

            default:
                return help(sender);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], FIRST_ARGS, new ArrayList<>(FIRST_ARGS.size()));
            default:
                return ImmutableList.of();
        }
    }

    private boolean help(CommandSender sender) {
        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Help.0"));
        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Help.1"));
        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Help.2"));
        sender.sendMessage(pluginRef.getLocaleManager().getString("Commands.Scoreboard.Help.3"));
        return true;
    }
}
