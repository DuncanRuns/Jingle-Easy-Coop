package xyz.duncanruns.jingle.easycoop;

import com.google.gson.Gson;
import xyz.duncanruns.jingle.util.GrabUtil;

public class NinjaLinkMeta {
    private static boolean tried = false;
    private static NinjaLinkMeta meta = null;

    public final String latest;
    public final String latest_download;

    @SuppressWarnings("unused")
    public NinjaLinkMeta(String latest, String latestDownload) {
        this.latest = latest;
        this.latest_download = latestDownload;
    }

    public static synchronized NinjaLinkMeta get() {
        if (!tried) {
            tried = true;
            try {
                meta = new Gson().fromJson(GrabUtil.grab("https://raw.githubusercontent.com/DuncanRuns/NinjaLink/refs/heads/main/meta.json"), NinjaLinkMeta.class);
            } catch (Exception ignored) {
            }
        }
        return meta;
    }
}
