import java.io.*;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static javafx.application.Platform.exit;

public class JSON_Reader {

    public static void main(String[] args) {
        JSON_Reader reader = new JSON_Reader();
        reader.view_all_file_keys();



        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/yelp_review.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObj = (JSONObject) parser.parse(line);
                reader.insert_to_review(jsonObj);
                System.exit(0);
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

    private void view_all_file_keys(){
        System.out.println("---------------------- Yelp Review ----------------------");
        view_json_object_keys("YelpDataset/yelp_review.json");

        System.out.println("---------------------- Yelp Business ----------------------");
        view_json_object_keys("YelpDataset/yelp_business.json");

        System.out.println("---------------------- Yelp Checkin ----------------------");
        view_json_object_keys("YelpDataset/yelp_checkin.json");

        System.out.println("---------------------- Yelp User ----------------------");
        view_json_object_keys("YelpDataset/yelp_user.json");
    }

    private void view_json_object_keys(String path) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            JSONObject jsonObj = (JSONObject) parser.parse(line);
            Set<String> test = jsonObj.keySet();
            System.out.println(test);
            for (String item : test) {
                System.out.println(item + "---" + jsonObj.get(item));
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String add_quotes(String s){
        return "\"" + s + "\"";
    }

    private String insert_to_review(JSONObject entry) {

        List<String> fields = Arrays.asList("review_id", "date", "stars", "text", "type", "user_id", "business_id");
        List<String> vote_fields = Arrays.asList("cool", "useful", "funny");

        JSONObject votes = (JSONObject) entry.get("votes");

        String[] fields_result = new String[fields.size()];
        String[] votes_fields_result = new String[vote_fields.size()];

        for(int i=0; i<fields.size(); i++){
//            System.out.println(fields.get(i) + entry.get(fields.get(i)));
            fields_result[i] = entry.get(fields.get(i)).toString();
        }

        for(int i=0; i<vote_fields.size(); i++){
//            System.out.println(vote_fields.get(i) + votes.get(vote_fields.get(i)));
            votes_fields_result[i] = votes.get(vote_fields.get(i)).toString();
        }


        String insert_string = "INSERT INTO Review VALUES(";
        //review_id
        insert_string += add_quotes(fields_result[fields.indexOf("review_id")]);
        insert_string += ",";
        //date_string
        insert_string += add_quotes(fields_result[fields.indexOf("date")]);
        insert_string += ",";
        //v_cool
        insert_string += votes_fields_result[vote_fields.indexOf("cool")];
        insert_string += ",";
        //v_useful
        insert_string += votes_fields_result[vote_fields.indexOf("useful")];
        insert_string += ",";
        //v_funny
        insert_string += votes_fields_result[vote_fields.indexOf("funny")];
        insert_string += ",";
        //stars
        insert_string += fields_result[fields.indexOf("stars")];
        insert_string += ",";
        //text
        insert_string += add_quotes(fields_result[fields.indexOf("text")]);
        insert_string += ",";
        //type
        insert_string += add_quotes(fields_result[fields.indexOf("type")]);
        insert_string += ",";
        //user_id
        insert_string += add_quotes(fields_result[fields.indexOf("user_id")]);
        insert_string += ",";
        //business_id
        insert_string += add_quotes(fields_result[fields.indexOf("business_id")]);
        insert_string += ");";

        System.out.println(insert_string);
        return insert_string;
    }

    private String insert_to_user(JSONObject entry) {

        List<String> fields = Arrays.asList("user_id", "name", "yelping_since", "average_stars", "review_count", "type", "fans");
        List<String> vote_fields = Arrays.asList("cool", "useful", "funny");
        List<String> compliment_fields = Arrays.asList("note", "plain", "cool", "hot", "funny");//TODO CHANGE THIS


        JSONObject votes = (JSONObject) entry.get("votes");

        JSONObject compliments = (JSONObject) entry.get("compliments");

        String[] fields_result = new String[fields.size()];
        String[] votes_fields_result = new String[vote_fields.size()];

        for(int i=0; i<fields.size(); i++){
//            System.out.println(fields.get(i) + entry.get(fields.get(i)));
            fields_result[i] = entry.get(fields.get(i)).toString();
        }

        for(int i=0; i<vote_fields.size(); i++){
//            System.out.println(vote_fields.get(i) + votes.get(vote_fields.get(i)));
            votes_fields_result[i] = votes.get(vote_fields.get(i)).toString();
        }

//        for(int i=0; i<compliment_fields.size(); i++){
////            System.out.println(vote_fields.get(i) + votes.get(vote_fields.get(i)));
//            votes_fields_result[i] = votes.get(vote_fields.get(i)).toString();
//        } TODO IMPLEMENT THIS FOR COMPLIMENT.. IF IT IS NULL THEN SHOULD DEFAULT TO 0


        String insert_string = "INSERT INTO User VALUES(";
        //user_id
        insert_string += add_quotes(fields_result[fields.indexOf("user_id")]);
        insert_string += ",";
        //name
        insert_string += add_quotes(fields_result[fields.indexOf("name")]);
        insert_string += ",";
        //yelping_since
        insert_string += add_quotes(fields_result[fields.indexOf("yelping_since")]);
        insert_string += ",";
        //average_stars
        insert_string += add_quotes(fields_result[fields.indexOf("average_stars")]); //TODO SHOULD THIS BE QUOTED? IS THIS VARCHAR OR INT?
        insert_string += ",";
        //review_count
        insert_string += fields_result[fields.indexOf("review_count")];
        insert_string += ",";
        //type
        insert_string += add_quotes(fields_result[fields.indexOf("type")]);
        insert_string += ",";
        //fans
        insert_string += fields_result[fields.indexOf("fans")];
        insert_string += ",";


        //TODO ??? ADD IN COMPLIMENTS


        //v_cool
        insert_string += votes_fields_result[vote_fields.indexOf("cool")];
        insert_string += ",";
        //v_useful
        insert_string += votes_fields_result[vote_fields.indexOf("useful")];
        insert_string += ",";
        //v_funny
        insert_string += votes_fields_result[vote_fields.indexOf("funny")];
        insert_string += ");";

        System.out.println(insert_string);
        return insert_string;
    }


    private String insert_to_business(JSONObject entry) {

        List<String> fields = Arrays.asList("user_id", "name", "yelping_since", "average_stars", "review_count", "type", "fans");
        List<String> hours_fields = Arrays.asList("mon_open", "mon_close", "tue_open", "tue_close", "wed_open", "wed_close", "thu_open",
                "thu_close","fri_open","fri_close","sat_open","sat_close", "sun_open", "sun_close");

        JSONObject hours = (JSONObject) entry.get("hours");
        JSONObject[] day_of_week = new JSONObject[7];
        day_of_week[0] = (JSONObject) hours.get("Monday");
        day_of_week[1] = (JSONObject) hours.get("Tuesday");
        day_of_week[2] = (JSONObject) hours.get("Wednesday");
        day_of_week[3] = (JSONObject) hours.get("Thursday");
        day_of_week[4] = (JSONObject) hours.get("Friday");
        day_of_week[5] = (JSONObject) hours.get("Saturday"); //TODO CHECK IF THERES ARE THERE?
        day_of_week[6] = (JSONObject) hours.get("Sunday");


        List<String> attributes;    //TODO IMPLEMENT THESE
        List<String> categories;    //TODO IMPLEMENT THESE

        String[] fields_result = new String[fields.size()];
        ArrayList<String> hour_fields_result = new ArrayList<>();

        for(int i=0; i<fields.size(); i++){
            fields_result[i] = entry.get(fields.get(i)).toString();
        }

        for(int i=0; i<day_of_week.length; i++){
            //TODO CHECK FOR EXCEPTIONS
            hour_fields_result.add((day_of_week[i].get("open").toString()));
            hour_fields_result.add((day_of_week[i].get("close").toString()));
        }


        String insert_string = "INSERT INTO User VALUES(";
        //business_id
        insert_string += add_quotes(fields_result[fields.indexOf("user_id")]);
        insert_string += ",";
        //name
        insert_string += add_quotes(fields_result[fields.indexOf("name")]);
        insert_string += ",";
        //full_address
        insert_string += add_quotes(fields_result[fields.indexOf("full_address")]);
        insert_string += ",";
        //longitude
        insert_string += add_quotes(fields_result[fields.indexOf("longitude")]); //TODO SHOULD THIS BE QUOTED? IS THIS VARCHAR OR INT?
        insert_string += ",";
        //latitude
        insert_string += fields_result[fields.indexOf("latitude")];
        insert_string += ",";
        //review_count
        insert_string += add_quotes(fields_result[fields.indexOf("review_count")]);
        insert_string += ",";
        //stars
        insert_string += fields_result[fields.indexOf("stars")];
        insert_string += ",";
        //type
        insert_string += add_quotes(fields_result[fields.indexOf("type")]);
        insert_string += ",";
        //open
        insert_string += fields_result[fields.indexOf("open")];
        insert_string += ",";


        insert_string += ",";
        insert_string += ");";
        System.out.println(insert_string);
        return insert_string;
    }
}
