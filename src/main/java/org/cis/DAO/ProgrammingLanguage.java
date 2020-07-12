package org.cis.DAO;

import com.google.gson.stream.JsonReader;
import org.cis.controllo.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ProgrammingLanguage {

    public static final String RELATIVE_PATH_LANGUAGE_JSON = "\\src\\main\\resources\\languages\\languages.json";
	
    public Map<String, String> loadMapExtLanguages() {
        String separator = "|";
        Map<String, String> extensions = new HashMap<>();
        JsonReader reader = null;

        try {
            String absolutePathLanguagesJSON = FileUtils.createAbsolutePath(RELATIVE_PATH_LANGUAGE_JSON).toString();
            reader = new JsonReader(new FileReader(absolutePathLanguagesJSON));

            // Reading the root object.
            reader.beginObject();
            while (reader.hasNext()) {
                String languageName = reader.nextName();

                // Reading extensions.
                reader.beginArray();
                while (reader.hasNext()) {
                    String extensionValue = reader.nextString();

                    String languages = extensions.get(extensionValue);
                    if (languages != null) {
                        languages = languages + separator + languageName;
                    } else {
                        languages = languageName;
                    }
                    extensions.put(extensionValue, languages);
                }
                reader.endArray();

            }
            reader.endObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Collections.unmodifiableMap(extensions);
    }

}
