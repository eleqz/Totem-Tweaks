package net.pathdos.totemtweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Settings {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public float totemSize = 1.0F;
    public float popSize = 0.3F;
    public boolean disableEquipAnimation = false;

    @SerializedName("TotemPopAnimation")
    public boolean popAnimation = true;
    public int animationSpeed = 40;
    public boolean lockRotationPosition = false;
    public boolean disableRotations = false;
    public boolean staticSize = false;

    public boolean enableTotemSizeChange = false;
    public float totemSizeChangeSpeed = 1.0F;
    public float minTotemSize = 0.5F;
    public float maxTotemSize = 1.0F;

    public static Settings load(Path path) {
        if (Files.notExists(path)) {
            return new Settings();
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Settings settings = GSON.fromJson(reader, Settings.class);
            return settings != null ? settings : new Settings();
        } catch (IOException | JsonParseException e) {
            LOGGER.warn("Could not read {}, using the defaults", path, e);
            return new Settings();
        }
    }

    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Could not save {}", path, e);
        }
    }

    public float heldTotemSize() {
        if (!enableTotemSizeChange) {
            return totemSize;
        }
        double wave = Math.sin(System.currentTimeMillis() / 1000.0 * totemSizeChangeSpeed) / 2.0 + 0.5;
        return (float) (minTotemSize + wave * (maxTotemSize - minTotemSize));
    }
}
