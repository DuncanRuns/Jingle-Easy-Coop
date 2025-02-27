package xyz.duncanruns.jingle.easycoop;

import me.duncanruns.e4mcbiat.util.GrabUtil;

public class NinjaLinkMeta {
    private static boolean tried = false;
    private static NinjaLinkMeta meta = null;

    public final String latest;
    public final String latest_download;

    @SuppressWarnings("unused")
    public NinjaLinkMeta(String latest, String latestDownload) {
        this.latest = latest;
        latest_download = latestDownload;
    }

    public static synchronized NinjaLinkMeta retrieve() {
        if (!tried) {
            tried = true;
            try {
                meta = GrabUtil.grabJson("https://raw.githubusercontent.com/DuncanRuns/NinjaLink/refs/heads/main/meta.json", NinjaLinkMeta.class);
            } catch (Exception ignored) {
            }
        }
        return meta;
    }
}
