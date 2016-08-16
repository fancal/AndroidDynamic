package com.elianshang.bridge.tool;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class UMengEventTool {

    private static void onEvent(Context context, String eventId) {
//        MobclickAgent.onEvent(context, eventId);
    }

    private static void onEvent(Context context, String eventId, Map<String, String> map) {
//        MobclickAgent.onEvent(context, eventId, map);
    }

    private static void onEventValue(Context context, String eventId, Map<String, String> map, int du) {
//        MobclickAgent.onEventValue(context, eventId, map, du);
    }

    public static void onRequest(Context context, String action, String state, String function, int du) {
        Map<String, String> map = new HashMap();
        map.put("action", action);
        map.put("state", state);
        map.put("function", function);
        onEventValue(context, "event_requset", map, du);
    }

    /**
     * 显示通知
     *
     * @param context
     */
    public static void onShowNotification(Context context, String title, String message, String messageId, String t, String i, boolean n) {
        Map<String, String> map = new HashMap();
        map.put("action", "显示通知");
        map.put("title", title);
        map.put("message", message);
        map.put("messageId", messageId);
        map.put("extra_t", t);
        map.put("extra_i", i);
        map.put("extra_n", n ? "1" : "0");
        onEvent(context, "event_notification", map);
    }

    /**
     * 打开通知
     *
     * @param context
     */
    public static void onOpenNotification(Context context, String title, String message, String messageId,String t, String i, boolean n) {
        Map<String, String> map = new HashMap();
        map.put("action", "打开通知");
        map.put("title", title);
        map.put("message", message);
        map.put("messageId", messageId);
        map.put("extra_t", t);
        map.put("extra_i", i);
        map.put("extra_n", n ? "1" : "0");
        onEvent(context, "event_notification", map);
    }
}
