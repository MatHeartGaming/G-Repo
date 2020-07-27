package org.cis.DAO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProgrammingLanguageTest {

    // Init map<Key=Ext, Value=LanguageName>.
    private Map<String, String> mapExtLanguages = new ProgrammingLanguage().loadMapExtLanguages();

    @Test
    void loadMapExtLanguagesExtM() {
        String ext = ".m";

        List<String> expectedLanguages = new ArrayList<>();

        expectedLanguages.add("Limbo");
        expectedLanguages.add("M");
        expectedLanguages.add("MATLAB");
        expectedLanguages.add("MUF");
        expectedLanguages.add("Mathematica");
        expectedLanguages.add("Mercury");
        expectedLanguages.add("Objective-C");

        String actualLanguages = mapExtLanguages.get(ext);

        List<String> actualListLanguages = Arrays.asList(actualLanguages.split("\\|"));

        assertEquals(expectedLanguages, actualListLanguages);
        assertEquals(expectedLanguages.size(), actualListLanguages.size());

    }

    @Test
    void loadMapExtLanguagesExtJava() {
        String ext = ".java";

        List<String> expectedLanguages = new ArrayList<>();

        expectedLanguages.add("Java");

        String actualLanguages = mapExtLanguages.get(ext);

        List<String> actualListLanguages = Arrays.asList(actualLanguages.split("\\|"));

        assertEquals(expectedLanguages, actualListLanguages);
        assertEquals(expectedLanguages.size(), actualListLanguages.size());
    }

    @Test
    void loadMapExtLanguagesExtCs() {
        String ext = ".cs";

        List<String> expectedLanguages = new ArrayList<>();

        expectedLanguages.add("C#");
        expectedLanguages.add("Smalltalk");

        String actualLanguages = mapExtLanguages.get(ext);

        List<String> actualListLanguages = Arrays.asList(actualLanguages.split("\\|"));

        assertEquals(expectedLanguages, actualListLanguages);
        assertEquals(expectedLanguages.size(), actualListLanguages.size());
    }

    @Test
    void loadMapExtLanguagesExtH() {
        String ext = ".h";

        List<String> expectedLanguages = new ArrayList<>();

        expectedLanguages.add("C");
        expectedLanguages.add("C++");
        expectedLanguages.add("Objective-C");

        String actualLanguages = mapExtLanguages.get(ext);

        List<String> actualListLanguages = Arrays.asList(actualLanguages.split("\\|"));

        assertEquals(expectedLanguages, actualListLanguages);
        assertEquals(expectedLanguages.size(), actualListLanguages.size());
    }

    @Test
    void loadMapExtLanguagesExtNotExists() {
        String ext1 = ".json";
        String ext2 = ".properties";
        String ext3 = ".mp4";
        String ext4 = ".mp3";
        String ext5 = ".iso";
        String ext6 = ".md";
        String ext7 = ".png";
        String ext8 = ".jar";
        String ext9 = ".zip";


        assertNull(mapExtLanguages.get(ext1));
        assertNull(mapExtLanguages.get(ext2));
        assertNull(mapExtLanguages.get(ext3));
        assertNull(mapExtLanguages.get(ext4));
        assertNull(mapExtLanguages.get(ext5));
        assertNull(mapExtLanguages.get(ext6));
        assertNull(mapExtLanguages.get(ext7));
        assertNull(mapExtLanguages.get(ext8));
        assertNull(mapExtLanguages.get(ext9));

    }

}