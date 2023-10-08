package se331.lab.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class DBHelper {
    static Map<String, Object> db;
    static {
        JsonParser jsonParser = new BasicJsonParser();
        File dbFile;
        try {
            dbFile = ResourceUtils.getFile("classpath:db-min.json");
        } catch (FileNotFoundException e) {
            dbFile = null;
        }
        if (dbFile != null) {
            byte[] bytes = new byte[(int) dbFile.length()];
            try (FileInputStream fis = new FileInputStream(dbFile)) {
                fis.read(bytes);
            } catch (IOException e) {
                System.err.println(e);
            }
            db = jsonParser
                    .parseMap(new String(bytes, StandardCharsets.UTF_8));
        }

    }

    public List<LinkedHashMap<String, Object>> getTable(String name) {
        if (db.containsKey(name)) {
            @SuppressWarnings("unchecked")
            ArrayList<LinkedHashMap<String, Object>> out = (ArrayList<LinkedHashMap<String, Object>>) db.get(name);
            return out;
        } else {
            return null;
        }
    }
}
