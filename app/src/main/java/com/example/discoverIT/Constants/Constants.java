package com.example.discoverIT.Constants;

import java.util.HashMap;

public class Constants {

    public static final String KEY_IS_SIGNED_IN = "false";
    public static String TOKEN = "token";
    public static String KEY_USER = "user";
    public static String KEY_NAME = "name";
    public static String KEY_PREFERENCE_NAME = "preferenceName";
    public static String KEY_USER_ID = "userId";
    public static String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static String KEY_RECEIVER_ID = "receiverId";
    public static String KEY_MESSAGE = "message";
    public static String KEY_TIMESTAMP = "timestamp";
    public static String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static String KEY_SENDER_NAME = "senderName";
    public static String KEY_RECEIVER_NAME = "receiverName";
    public static String KEY_SENDER_IMAGE = "senderImage";
    public static String KEY_RECEIVER_IMAGE = "receiverImage";
    public static String KEY_LAST_MESSAGE = "lastMessage";
    public static String KEY_AVAILABILITY = "availability";
    public static String KEY_COLLECTION_USERS = "users";
    public static String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static String REMOTE_MSG_DATA = "data";
    public static String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static String KEY_FCM_TOKEN = "fcmToken";
    public static String KEY_USER_INFO = "userInfo";
    public static Boolean SEND_NOTIFICATION = true;

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if(remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAtryPxKk:APA91bFecNAaiFOxl933EfaoaC6SmyHr3oFc_WDIW2EcMlrehfqw0WQpny-ILmfVtzzo1jieEbuNPaFraZ0ogqeMwdfw8uRVukkAoD7TpEES-8qEC2fWiL1dbGWm_t3yT-YFfxl6d6rH"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}
