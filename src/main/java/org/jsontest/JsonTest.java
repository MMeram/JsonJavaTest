package org.jsontest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.list;


public class JsonTest {
    private static final String ROOT_PATH = "files\\";

    public static void main(String[] args) {
        try {
            // create map to hint vector
            Map<String, List<String>> land2hintsMap = new HashMap<>();
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
                readFile(ROOT_PATH + fl, gson, land2hintsMap);
            }

            // Sort


            // iterate through map
            // create a treemap to sort the lands alphabetically
            TreeMap<String, List<String>> sortedMap = new TreeMap<>(land2hintsMap);
            for (Map.Entry<String, List<String>> entry : sortedMap.entrySet()){
                System.out.println("Land = " + entry.getKey());
                for (String hint : entry.getValue()){
                    System.out.println("\tHint = " + hint);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void readFile(String unescapedJson, Gson gson, Map<String, List<String>> land2hintsMap) throws IOException {
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
                //System.out.println("Land = " + entry.getKey());
                List<String> hintList = new ArrayList<>();
                for (Map.Entry<?, JsonElement> hintEntry : entry.getValue().getAsJsonObject().entrySet()){
                    //System.out.println("\t"+hintEntry.getKey() + "=" + hintEntry.getValue().toString());
                    hintList.add(hintEntry.getValue().toString());
                }
                // add the land to the map
                land2hintsMap.put(entry.getKey().toString(), hintList);
            }
            else {
                System.out.println(entry.getKey() + "=" + entry.getValue().toString());
            }
        }
        // close reader
        reader.close();
    }
}