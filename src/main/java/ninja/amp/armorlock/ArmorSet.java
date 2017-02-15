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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ArmorSet {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    public ArmorSet(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public void apply(Player player) {
        // Apply armor set to player inventory
        EntityEquipment equipment = player.getEquipment();
        equipment.setHelmet(helmet);
        equipment.setChestplate(chestplate);
        equipment.setLeggings(leggings);
        equipment.setBoots(boots);
    }

    public static ArmorSet loadSet(ConfigurationSection section) {
        // Load helmet, chestplate, leggings, and boots items
        ItemStack helmet = loadItem(section.getConfigurationSection("helmet"));
        ItemStack chestplate = loadItem(section.getConfigurationSection("chestplate"));
        ItemStack leggings = loadItem(section.getConfigurationSection("leggings"));
        ItemStack boots = loadItem(section.getConfigurationSection("boots"));

        // Create armor set
        return new ArmorSet(helmet, chestplate, leggings, boots);
    }

    public static ItemStack loadItem(ConfigurationSection section) {
        // Item not found in config
        if (section == null) {
            return null;
        }

        // Load item stack
        ItemStack item = new ItemStack(Material.valueOf(section.getString("material")));

        // Load item enchantments
        if (section.isConfigurationSection("enchantments")) {
            ConfigurationSection enchants = section.getConfigurationSection("enchantments");
            for (String name : enchants.getKeys(false)) {
                Enchantment enchant = Enchantment.getByName(name);
                if (enchant != null && enchants.isInt(name)) {
                    item.addEnchantment(enchant, enchants.getInt(name));
                }
            }
        }

        return item;
    }

}
