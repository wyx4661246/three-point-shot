package com.malzahar.tps.common.help;

public class FAQUrl {

    public static final String APPLY_TOPIC_URL =
        "";

    public static final String NAME_SERVER_ADDR_NOT_EXIST_URL =
        "";

    public static final String GROUP_NAME_DUPLICATE_URL =
        "";

    public static final String CLIENT_PARAMETER_CHECK_URL =
        "";

    public static final String SUBSCRIPTION_GROUP_NOT_EXIST =
        "";

    public static final String CLIENT_SERVICE_NOT_OK =
        "";

    // FAQ: No route info of this topic, TopicABC
    public static final String NO_TOPIC_ROUTE_INFO =
        "";

    public static final String LOAD_JSON_EXCEPTION =
        "";

    public static final String SAME_GROUP_DIFFERENT_TOPIC =
        "";

    public static final String MQLIST_NOT_EXIST =
        "";

    public static final String UNEXPECTED_EXCEPTION_URL =
        "";

    public static final String SEND_MSG_FAILED =
        "";

    public static final String UNKNOWN_HOST_EXCEPTION =
        "";

    private static final String TIP_STRING_BEGIN = "\nSee ";
    private static final String TIP_STRING_END = " for further details.";

    public static String suggestTodo(final String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(TIP_STRING_BEGIN);
        sb.append(url);
        sb.append(TIP_STRING_END);
        return sb.toString();
    }

    public static String attachDefaultURL(final String errorMessage) {
        if (errorMessage != null) {
            int index = errorMessage.indexOf(TIP_STRING_BEGIN);
            if (-1 == index) {
                StringBuilder sb = new StringBuilder();
                sb.append(errorMessage);
                sb.append("\n");
                sb.append("For more information, please visit the url, ");
                sb.append(UNEXPECTED_EXCEPTION_URL);
                return sb.toString();
            }
        }

        return errorMessage;
    }
}
