package xyz.duncanruns.jingle.easycoop;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NinjaLinkRunner {
    private static Process process = null;

    private static Path getJavaPath() {
        return Paths.get(System.getProperty("java.home")).resolve("bin").resolve("javaw.exe");
    }

    public static synchronized void launch(Path jarPath, String ip, String nickname, String roomName, String roomPass, Runnable onClose) throws IOException {
        if(process != null) close();
        List<String> cmd = new ArrayList<>(7);
        cmd.add(getJavaPath().toString());
        cmd.add("-jar");
        cmd.add(jarPath.toString());
        cmd.add(ip);
        cmd.add(nickname);
        if (!roomName.isEmpty()) cmd.add(roomName);
        if (!roomPass.isEmpty()) cmd.add(roomPass);

        process = new ProcessBuilder(cmd).directory(jarPath.getParent().toFile()).start();
        process.onExit().thenAccept(p -> {
            process = null;
            onClose.run();
        });
    }

    public static synchronized void close() {
        if (process == null) return;
        process.destroy();
        process = null;
    }
}
