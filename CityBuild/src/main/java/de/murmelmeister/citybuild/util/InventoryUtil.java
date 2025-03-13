package de.murmelmeister.citybuild.util;

import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Base64;

public final class InventoryUtil {

    public static String saveItems(ItemStack[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeInt(items.length);
            for (ItemStack item : items) {
                if (item != null) {
                    dataOutputStream.writeBoolean(true);
                    byte[] itemBytes = item.serializeAsBytes();
                    dataOutputStream.writeInt(itemBytes.length);
                    dataOutputStream.write(itemBytes);
                } else dataOutputStream.writeBoolean(false);
            }
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to save items", e);
        }
    }

    public static ItemStack[] loadItems(String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            ItemStack[] items = new ItemStack[dataInputStream.readInt()];
            for (int i = 0; i < items.length; i++) {
                boolean exist = dataInputStream.readBoolean();
                if (exist) {
                    int itemLength = dataInputStream.readInt();
                    byte[] itemBytes = new byte[itemLength];
                    dataInputStream.readFully(itemBytes);
                    items[i] = ItemStack.deserializeBytes(itemBytes);
                } else items[i] = null;
            }
            return items;
        } catch (IOException e) {
            throw new RuntimeException("Unable to load items", e);
        }
    }
}
