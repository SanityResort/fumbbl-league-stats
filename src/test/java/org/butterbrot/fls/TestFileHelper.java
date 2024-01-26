package org.butterbrot.fls;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class TestFileHelper {

    static String loadFile(String file) throws IOException {

        try (InputStream in = TestFileHelper.class.getResourceAsStream(file)) {
            return new String(IOUtils.toByteArray(in), StandardCharsets.UTF_8);
        }
    }

}
