package com.jphan.db_porter;

import java.io.*;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSON_Reader {

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/yelp_business.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObj = (JSONObject) parser.parse(line);
                System.out.println(jsonObj.get("business_id"));
                System.out.println(jsonObj);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
