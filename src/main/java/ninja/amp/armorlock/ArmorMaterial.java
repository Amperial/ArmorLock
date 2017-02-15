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
import org.bukkit.inventory.ItemStack;

/**
 * Materials of armor.
 */
public enum ArmorMaterial {
    LEATHER,
    CHAINMAIL,
    IRON,
    GOLD,
    DIAMOND;

    /**
     * @param type The ArmorType of the armor.
     *
     * @return The armor itemstack.
     */
    public ItemStack getArmor(ArmorType type) {
        return new ItemStack(Material.valueOf(name() + "_" + type.name()));
    }

    /**
     * Gets the ArmorMaterial of a piece of armor.
     *
     * @param material The piece of armor.
     * @return The ArmorMaterial.
     */
    public static ArmorMaterial getArmorMaterial(Material material) {
        for (ArmorMaterial armorMaterial : ArmorMaterial.values()) {
            if (material.name().contains(armorMaterial.name())) {
                return armorMaterial;
            }
        }
        return null;
    }

}
