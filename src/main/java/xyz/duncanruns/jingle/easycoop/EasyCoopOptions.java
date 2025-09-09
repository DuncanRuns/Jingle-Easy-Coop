package xyz.duncanruns.jingle.easycoop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.duncanruns.jingle.Jingle;
import xyz.duncanruns.jingle.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EasyCoopOptions {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = EasyCoop.FOLDER.resolve("options.json");

    public int e4mcbiatPort = 25565;

    public String nlJar = "";
    public String nlVer = "";
    public String nlAddress = "";
    public String nlNickname = "";
    public String nlRoomName = "";
    public String nlRoomPass = "";

    public boolean nlQAB = false;
    public boolean e4mcQAB = false;

    public static EasyCoopOptions tryLoad() {
        if (!Files.exists(PATH)) return new EasyCoopOptions();
        try {
            return FileUtil.readJson(PATH, EasyCoopOptions.class);
        } catch (Exception e) {
            Jingle.logError("Failed to load Easy Co-op options!", e);
            return new EasyCoopOptions();
        }
    }

    public void trySave() {
        try {
            FileUtil.writeString(PATH, GSON.toJson(this));
        } catch (IOException e) {
            Jingle.logError("Failed to save Easy Co-op options!", e);
        }
    }

    public String getNLAddress() {
        return nlAddress.trim().isEmpty() ? EasyCoop.DEFAULT_NL_ADDRESS : nlAddress;
    }
}
