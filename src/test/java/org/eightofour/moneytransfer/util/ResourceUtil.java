package org.eightofour.moneytransfer.util;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    private ResourceUtil() {
        throw new IllegalStateException("This class isn't instantiable");
    }

    public static String getResourceAsString(Class<?> clazz, String fileName)
            throws IOException {
        return IOUtils.toString(
            clazz.getResourceAsStream(fileName),
            Charsets.UTF_8
        );
    }

    public static InputStream getResourceAsStream(Class<?> clazz, String fileName) {
        return clazz.getResourceAsStream(fileName);
    }
}