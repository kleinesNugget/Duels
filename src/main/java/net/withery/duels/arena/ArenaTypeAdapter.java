package net.withery.duels.arena;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ArenaTypeAdapter extends TypeAdapter<Arena[]> {

    @Override
    public void write(JsonWriter jsonWriter, Arena[] arenas) throws IOException {
        jsonWriter.beginArray();
        for (Arena arena : arenas) {
            jsonWriter.beginObject();
            jsonWriter.name(arena.getName());
            jsonWriter.beginObject();
            jsonWriter.name("pos-1").value(serializeRegionLocation(arena.getRegion().getPosition1()));
            jsonWriter.name("pos-2").value(serializeRegionLocation(arena.getRegion().getPosition2()));
            jsonWriter.name("spawn-1").value(serializeSpawnLocation(arena.getSpawn1()));
            jsonWriter.name("spawn-2").value(serializeSpawnLocation(arena.getSpawn2()));
            jsonWriter.name("disabled").value(arena.isDisabled());
            jsonWriter.endObject();
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public Arena[] read(JsonReader jsonReader) throws IOException {
        Set<Arena> arenas = new HashSet<>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String name;
            Location pos1 = null;
            Location pos2 = null;
            Location spawn1 = null;
            Location spawn2 = null;
            boolean disabled = false;

            name = jsonReader.nextName();
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                switch (jsonReader.nextName()) {
                    case "pos-1" -> pos1 = deserializeRegionLocation(jsonReader.nextString());
                    case "pos-2" -> pos2 = deserializeRegionLocation(jsonReader.nextString());
                    case "spawn-1" -> spawn1 = deserializeSpawnLocation(jsonReader.nextString());
                    case "spawn-2" -> spawn2 = deserializeSpawnLocation(jsonReader.nextString());
                    case "disabled" -> disabled = jsonReader.nextBoolean();
                }
            }
            jsonReader.endObject();
            jsonReader.endObject();

            arenas.add(new Arena(name, pos1, pos2, spawn1, spawn2, disabled));
        }
        jsonReader.endArray();

        return arenas.toArray(Arena[]::new);
    }

    private @Nullable String serializeRegionLocation(@Nullable Location location) {
        if (location == null) return null;

        World world = location.getWorld();
        if (world == null) return null;

        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    private @Nullable Location deserializeRegionLocation(@NotNull String string) {
        String[] parts = string.split(";");
        if (parts.length != 4) {
            // Error
            return null;
        }

        String worldName = parts[0];
        double x;
        double y;
        double z;

        try {
            x = Double.parseDouble(parts[1]);
            y = Double.parseDouble(parts[2]);
            z = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            // Error
            return null;
        }

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            // Error
            return null;
        }

        return new Location(world, x, y, z);
    }

    private @Nullable String serializeSpawnLocation(@Nullable Location location) {
        if (location == null) return null;

        World world = location.getWorld();
        if (world == null) return null;

        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    private @Nullable Location deserializeSpawnLocation(@NotNull String string) {
        String[] parts = string.split(";");
        if (parts.length != 6) {
            // Error
            return null;
        }

        String worldName = parts[0];
        double x;
        double y;
        double z;
        float yaw;
        float pitch;

        try {
            x = Double.parseDouble(parts[1]);
            y = Double.parseDouble(parts[2]);
            z = Double.parseDouble(parts[3]);
            yaw = Float.parseFloat(parts[4]);
            pitch = Float.parseFloat(parts[5]);
        } catch (NumberFormatException e) {
            // Error
            return null;
        }

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            // Error
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

}