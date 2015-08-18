package uk.co.qmunity.lib.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.co.qmunity.lib.client.helper.ShaderHelper;

public class FileHelper {

    public static String readFileAsString(String filename) throws Exception {

        StringBuilder source = new StringBuilder();
        InputStream in = ShaderHelper.class.getResourceAsStream(filename);
        Exception exception = null;
        BufferedReader reader = null;

        if (in == null) {
            return "";
        }

        try {
            try {
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        source.append(line).append('\n');
                    }
                } catch (Exception exc) {
                    exception = exc;
                }
            } catch (Exception exc) {
                exception = exc;
            } finally {
                in.close();
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } finally {
                if (exception != null) {
                    throw exception;
                }
            }
        }

        return source.toString();
    }

}
