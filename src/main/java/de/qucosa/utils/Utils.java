package de.qucosa.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    public static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    /**
     * get timestamp (lastmod) for url/urlset
     * @return date in w3c datetime format as string
     */
    public static String getCurrentW3cDatetime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String w3cDatetimeString = format.format(new Date())+"+00:00";
        return w3cDatetimeString;
    }

    public static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }
}

