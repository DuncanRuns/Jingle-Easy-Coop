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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

public class EasyCoop {
    public static void main(String[] args) throws IOException {
        JingleAppLaunch.launchWithDevPlugin(args, PluginManager.JinglePluginData.fromString(
                Resources.toString(Resources.getResource(EasyCoop.class, "/jingle.plugin.json"), Charset.defaultCharset())
        ), EasyCoop::initialize);
    }

    public static void initialize() {
        JingleGUI.addPluginTab("Easy Co-op", new EasyCoopPanel().mainPanel);
    }
}
