package xyz.duncanruns.jingle.easycoop;

import com.google.common.io.Resources;
import me.duncanruns.e4mcbiat.E4mcClient;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.jingle.Jingle;
import xyz.duncanruns.jingle.JingleAppLaunch;
import xyz.duncanruns.jingle.easycoop.gui.EasyCoopPanel;
import xyz.duncanruns.jingle.gui.JingleGUI;
import xyz.duncanruns.jingle.plugin.PluginEvents;
import xyz.duncanruns.jingle.plugin.PluginManager;
import xyz.duncanruns.jingle.util.GrabUtil;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class EasyCoop {
    public static final Path FOLDER = Jingle.FOLDER.resolve("easy-coop");
    public static EasyCoopOptions options = new EasyCoopOptions();
    private static EasyCoopPanel panel;

    private static E4mcClient e4mcClient = null;


    public static void main(String[] args) throws IOException {
        JingleAppLaunch.launchWithDevPlugin(args, PluginManager.JinglePluginData.fromString(
                Resources.toString(Resources.getResource(EasyCoop.class, "/jingle.plugin.json"), Charset.defaultCharset())
        ), EasyCoop::initialize);
    }

    public static void initialize() {
        FOLDER.toFile().mkdirs();
        options = EasyCoopOptions.tryLoad();
        panel = new EasyCoopPanel();
        JingleGUI.addPluginTab("Easy Co-op", panel.mainPanel);

        if (!options.nlVer.isEmpty() && !Files.exists(FOLDER.resolve(options.nlJar))) {
            options.nlJar = "";
            options.nlVer = "";
        }

        PluginEvents.STOP.register(EasyCoop::onJingleStop);
        Thread thread = new Thread(() -> {
            NinjaLinkMeta ninjaLinkMeta = NinjaLinkMeta.get();
            if (ninjaLinkMeta == null || Objects.equals(options.nlVer, ninjaLinkMeta.latest)) return;
            swingvokeAndWait(() -> panel.onUpdateAvailable());
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static synchronized void startE4mc() {
        if (e4mcClient != null) throw new IllegalStateException("Previous e4mc has not ended!");
        Thread thread = new Thread(() -> {
            e4mcClient = new E4mcClient(domain -> swingvokeAndWait(() -> panel.onE4mcStarted(domain)), msg -> Jingle.log(Level.INFO, "E4mc Broadcast: " + msg));
            try {
                e4mcClient.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stopE4mc();
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static synchronized void stopE4mc() {
        if (e4mcClient != null) e4mcClient.close();
        e4mcClient = null;
        swingvokeAndWait(panel::onE4mcStopped);
    }

    public static void swingvokeAndWait(Runnable r) throws RuntimeException {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized void onJingleStop() {
        if (e4mcClient != null) e4mcClient.close();
        NinjaLinkRunner.close();
        options.trySave();
    }

    public static synchronized void setMCPort(int port) {
        options.e4mcbiatPort = port;
        if (e4mcClient != null) e4mcClient.setMCPort(port);
    }

    public static void downloadNinjaLink() {
        NinjaLinkRunner.close();
        if (!options.nlJar.isEmpty()) {
            try {
                Files.delete(FOLDER.resolve(options.nlJar));
            } catch (IOException e) {
                Jingle.logError("Failed to delete old NinjaLink!", e);
                Jingle.log(Level.WARN, "There will be an extra NinjaLink jar in " + FOLDER + ", it should be deleted.");
            }
        }
        NinjaLinkMeta meta = NinjaLinkMeta.get();
        String jarName = "NinjaLink-" + meta.latest + ".jar";
        try {
            GrabUtil.download(meta.latest_download, FOLDER.resolve(jarName));
        } catch (IOException e) {
            Jingle.logError("Failed to download NinjaLink!", e);
            return;
        }
        options.nlJar = jarName;
        options.nlVer = meta.latest;

        swingvokeAndWait(() -> panel.onFinishNLUpdate());
    }

    public static boolean launchNinjaLink() {
        try {
            NinjaLinkRunner.launch(FOLDER.resolve(options.nlJar), options.nlIp, options.nlNickname, options.nlRoomName, options.nlRoomPass, () -> panel.onNinjaLinkClosed());
            return true;
        } catch (IOException e) {
            panel.onNinjaLinkClosed();
            return false;
        }
    }
}
