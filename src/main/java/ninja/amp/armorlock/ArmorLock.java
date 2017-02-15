/*
 * This file is part of ArmorLock.
 *
 * Copyright (c) 2013-2017 <http://github.com/ampayne2/ArmorLock//>
 *
 * ArmorLock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArmorLock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ArmorLock.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.armorlock;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.command.CommandGroup;
import ninja.amp.amplib.command.commands.AboutCommand;
import ninja.amp.amplib.command.commands.HelpCommand;
import ninja.amp.amplib.command.commands.ReloadCommand;
import ninja.amp.amplib.messenger.DefaultMessage;
import ninja.amp.amplib.messenger.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.EnumSet;

/**
 * The main class of the Armor Lock plugin.
 */
public class ArmorLock extends AmpJavaPlugin {

    private ArmorManager armorManager;

    @Override
    public void onEnable() {
        DefaultMessage.PREFIX.setMessage("&6[&5Armor Lock&6] &7");
        DefaultMessage.RELOAD.setMessage("Reloaded Armor Lock.");
        enableAmpLib();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        Messenger.PRIMARY_COLOR = ChatColor.valueOf(config.getString("colors.primary", "DARK_PURPLE"));
        Messenger.SECONDARY_COLOR = ChatColor.valueOf(config.getString("colors.secondary", "GRAY"));
        Messenger.HIGHLIGHT_COLOR = ChatColor.valueOf(config.getString("colors.highlights", "GOLD"));
        config.set("configversion", getDescription().getVersion());
        saveConfig();
        getMessenger().registerMessages(EnumSet.allOf(ALMessage.class));

        Command about = new AboutCommand(this);
        about.setCommandUsage("/al");
        Command help = new HelpCommand(this);
        help.setCommandUsage("/al help");
        Command reload = new ReloadCommand(this);
        reload.setCommandUsage("/al reload");
        CommandGroup armorLock = new CommandGroup(this, "armorlock")
                .addChildCommand(about)
                .addChildCommand(help)
                .addChildCommand(reload);
        armorLock.setPermission(new Permission("armorlock.admin", PermissionDefault.OP));
        getCommandController().addCommand(armorLock);

        armorManager = new ArmorManager(this);
    }

    @Override
    public void onDisable() {
        armorManager = null;

        HandlerList.unregisterAll(this);
        disableAmpLib();
    }

}
