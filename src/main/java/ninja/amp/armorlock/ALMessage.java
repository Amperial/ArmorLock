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

import ninja.amp.amplib.messenger.Message;

/**
 * Messages in Armor Lock.
 */
public enum ALMessage implements Message {
    CANT_MODIFY("Armor.CantModify", "You don't have permission to modify your armor!");

    private String message;
    private final String path;
    private final String defaultMessage;

    ALMessage(String path, String defaultMessage) {
        this.message = defaultMessage;
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets the message string.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message string.
     *
     * @param message The message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the path to the message.
     *
     * @return The path to the message.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the default message string of the message.
     *
     * @return The default message.
     */
    public String getDefault() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return message;
    }

}
