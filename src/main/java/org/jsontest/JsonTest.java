package org.jsontest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.list;


public class JsonTest {
    private static final String ROOT_PATH = "files\\";

    public static void main(String[] args) {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // Retrieve
            Stream<Path> str = list(Paths.get(ROOT_PATH));
            List<String> files =
                    str.filter(file -> !Files.isDirectory(file))
                            .map(Path::getFileName)
                            .map(Path::toString)
                            .filter(file-> file.endsWith("json"))
                            .collect(Collectors.toList());


            for (String fl : files){
                System.out.println("Files "+ fl);
                readFile(ROOT_PATH + fl, gson);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void readFile(String unescapedJson, Gson gson) throws IOException {
        StringBuilder sb = new StringBuilder();
        // create a reader
        BufferedReader reader = Files.newBufferedReader(Paths.get(String.valueOf(unescapedJson)), Charset.forName("CP1252"));

        // read line by line
        String line;
        while (( line = reader.readLine()) != null) {
            sb.append(line);
        }
        // unescape new lines
        String cleanFile = StringEscapeUtils.unescapeJava(sb.toString().replaceAll("^\"|\"$", ""));

        // convert JSON file to map
        JsonObject jo = gson.fromJson(cleanFile, JsonObject.class);
        for (Map.Entry<?, JsonElement> entry : jo.entrySet()) {
            if(entry.getValue().isJsonObject()){
                for (Map.Entry<?, JsonElement> hintEntry : entry.getValue().getAsJsonObject().entrySet()){
                    System.out.println(hintEntry.getKey() + "=" + hintEntry.getValue().toString());
                }
            }
            else {
                System.out.println(entry.getKey() + "=" + entry.getValue().toString());
            }
        }
        /*
        Map<?, ?> map = gson.fromJson(cleanFile, Map.class);

        // print map entries
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue().toString());
            if (entry.getValue().toString().contains("{")) {

                for (Map.Entry<?, ?> subentry : entry.getValue()){
                    System.out.println(subentry.getKey() + "=" + subentry.getValue().toString());
                }

            }
        }
        */
        // close reader
        reader.close();
    }
}