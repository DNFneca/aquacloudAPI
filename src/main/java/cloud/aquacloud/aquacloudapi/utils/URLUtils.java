package cloud.aquacloud.aquacloudapi.utils;

import java.io.UnsupportedEncodingException;

public final class URLUtils {
    public static String uuidToURL(String uuid) throws UnsupportedEncodingException {
        return uuid.replace("-", "");
    }

    public static String urlToUUID(String url) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.insert(8, "-");
        builder.insert(13, "-");
        builder.insert(18, "-");
        builder.insert(23, "-");
        return builder.toString();
    }
}
