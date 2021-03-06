/*
MIT License

Copyright (c) 2018 Whippy Tools

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package pl.bmstefanski.spawn.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bmstefanski.commands.CommandArguments;
import pl.bmstefanski.commands.CommandExecutor;
import pl.bmstefanski.commands.Messageable;
import pl.bmstefanski.commands.annotation.Command;
import pl.bmstefanski.commands.annotation.GameOnly;
import pl.bmstefanski.commands.annotation.Permission;
import pl.bmstefanski.spawn.SpawnPlugin;
import pl.bmstefanski.spawn.configuration.SpawnConfig;
import pl.bmstefanski.tools.manager.TeleportManager;
import pl.bmstefanski.tools.storage.configuration.Messages;

public class SpawnCommand implements CommandExecutor, Messageable {

    private final SpawnPlugin plugin;
    private final Messages messages;
    private final SpawnConfig config;

    public SpawnCommand(SpawnPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.config = plugin.getConfiguration();
    }

    @Command(name = "spawn", usage = "[player]", max = 1)
    @Permission("tools.command.spawn")
    @GameOnly(false)
    @Override
    public void execute(CommandSender commandSender, CommandArguments commandArguments) {
        TeleportManager taskManager = new TeleportManager(plugin.getParentPlugin());

        if (config.getExists()) {

            Location location = plugin.getSpawnLocation();

            if (commandArguments.getSize() == 0) {
                if (!(commandSender instanceof Player)) {
                    sendMessage(commandSender, messages.getOnlyPlayer());
                    return;
                }

                Player player = (Player) commandSender;

                taskManager.teleport(player, location, plugin.getParentPlugin().getConfiguration().getSpawnDelay());
                return;
            }

            if (Bukkit.getPlayer(commandArguments.getParam(0)) == null) {
                sendMessage(commandSender, StringUtils.replace(messages.getPlayerNotFound(), "%player%", commandArguments.getParam(0)));
                return;
            }

            Player target = Bukkit.getPlayer(commandArguments.getParam(0));
            target.teleport(location);

            return;
        }

        sendMessage(commandSender, messages.getSpawnFailed());

    }

}
