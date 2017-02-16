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

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Iterator;

public class ArmorManager implements Listener {

    private static final Permission MODIFY = new Permission("armorlock.modify", PermissionDefault.OP);
    private static final Permission NO_DROP = new Permission("armorlock.dropnothing", PermissionDefault.FALSE);
    private static final Permission TRAINEE = new Permission("armorlock.trainee", PermissionDefault.FALSE);
    private static final Permission GUARD = new Permission("armorlock.guard", PermissionDefault.FALSE);
    private static final Permission SUPER_GUARD = new Permission("armorlock.superguard", PermissionDefault.FALSE);

    private final ArmorLock plugin;
    private final ArmorSet trainee;
    private final ArmorSet guard;
    private final ArmorSet superGuard;

    public ArmorManager(ArmorLock plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        FileConfiguration config = plugin.getConfig();
        trainee = ArmorSet.loadSet(config.getConfigurationSection("trainee"));
        guard = ArmorSet.loadSet(config.getConfigurationSection("guard"));
        superGuard = ArmorSet.loadSet(config.getConfigurationSection("superguard"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Only handle clicks by players
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        // If the player has permission to modify there is no need for checks
        if (player.hasPermission(MODIFY)) {
            return;
        }

        if (event.isShiftClick()) {
            // If shift clicking outside of player inventory the item would not move to an armor slot
            if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
                return;
            }

            // Check if the item would move to or out of an armor slot
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getType() != Material.AIR) {
                ArmorType type = ArmorType.getArmorType(item.getType());
                if (type != null) {
                    if (event.getSlotType() == InventoryType.SlotType.ARMOR || type.canEquip(player)) {
                        event.setCancelled(true);
                        plugin.getMessenger().sendMessage(player, ALMessage.CANT_MODIFY);
                    }
                }
            }
        } else if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            // Player directly clicked on an armor slot
            event.setCancelled(true);
            plugin.getMessenger().sendMessage(player, ALMessage.CANT_MODIFY);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Only handle drags by players
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        // If the player has permission to modify there is no need for checks
        if (player.hasPermission(MODIFY)) {
            return;
        }

        ItemStack item = event.getOldCursor();
        if (item != null && item.getType() != Material.AIR) {
            ArmorType type = ArmorType.getArmorType(item.getType());
            if (type != null) {
                event.setCancelled(true);
                plugin.getMessenger().sendMessage(player, ALMessage.CANT_MODIFY);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            ArmorType type = ArmorType.getArmorType(item.getType());
            Player player = event.getPlayer();
            if (type != null && type.canEquip(player) && !player.hasPermission(MODIFY)) {
                event.setCancelled(true);
                plugin.getMessenger().sendMessage(player, ALMessage.CANT_MODIFY);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        giveArmor(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        giveArmor(event.getPlayer());
    }

    private void giveArmor(Player player) {
        if (player.hasPermission(SUPER_GUARD)) {
            superGuard.apply(player);
        } else if (player.hasPermission(GUARD)) {
            guard.apply(player);
        } else if (player.hasPermission(TRAINEE)) {
            trainee.apply(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasPermission(NO_DROP)) {
            Iterator<ItemStack> drops = event.getDrops().iterator();
            while (drops.hasNext()) {
                ItemStack drop = drops.next();
                if (drop != null && drop.getType() != Material.AIR && ArmorType.isArmor(drop.getType())) {
                    drops.remove();
                }
            }
        }
    }

}
