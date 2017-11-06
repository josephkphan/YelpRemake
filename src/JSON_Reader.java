import java.io.*;

import org.json.simple.JSONArray;
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
//        reader.read_file();
        reader.view_all_file_keys();

        //The Order matters here! Look at createdb so see the dependencies (foreign keys)
        reader.read_json_file("yelp_business.json");
        reader.read_json_file("yelp_user.json");
        reader.read_json_file("yelp_review.json");
        reader.read_json_file("yelp_checkin.json");

    }

    private void view_all_file_keys(){
        System.out.println("---------------------- Yelp Business Keys----------------------");
        view_json_object_keys("yelp_business.json");

        System.out.println("---------------------- Yelp User Keys----------------------");
        view_json_object_keys("yelp_user.json");

        System.out.println("---------------------- Yelp Review Keys----------------------");
        view_json_object_keys("yelp_review.json");

        System.out.println("---------------------- Yelp Checkin Keys----------------------");
        view_json_object_keys("yelp_checkin.json");
    }

    /**
     * IMPORTANT: File Path & directory matters!
     * Takes in one of four files:
     *    yelp_business.json
     *    yelp_review.json
     *    yelp_checkin.json
     *    yelp_user.json
     * These files should be in folder "YelpDataset" at the root of the project (equivalent with src folder)
     *
     * This will read the file line by line and write it into the database
     */
    private void read_file(){

        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/yelp_business.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            Set<String> overall = new HashSet<>();
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObj = (JSONObject) parser.parse(line);
                System.out.println(jsonObj.get("attributes"));

//                Set<String> test = ((JSONObject)jsonObj.get("attributes")).keySet();
//                overall.addAll(test);
//                System.out.println("-------------");
//                System.out.println(overall);
            }
            fileReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }



    //[Has TV, Coat Check, Open 24 Hours, Accepts Insurance, Alcohol, Dogs Allowed, Caters, Price Range, Happy Hour, Good for Kids, Good For Dancing, Outdoor Seating, Good For Kids, Takes Reservations, Waiter Service, Wi-Fi, Good For, Parking, Hair Types Specialized In, Drive-Thru, Order at Counter, Accepts Credit Cards, BYOB/Corkage, Good For Groups, Noise Level, By Appointment Only, Take-out, Wheelchair Accessible, BYOB, Music, Attire, Payment Types, Delivery, Ambience, Dietary Restrictions, Corkage, Ages Allowed, Smoking]
    /**
     * IMPORTANT: File Path & directory matters!
     * Takes in one of four files:
     *    yelp_business.json
     *    yelp_review.json
     *    yelp_checkin.json
     *    yelp_user.json
     * These files should be in folder "YelpDataset" at the root of the project (equivalent with src folder)
     *
     * This will read the file line by line and write it into the database
     * @param file_name
     */
    private void read_json_file(String file_name){

        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/"+ file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObj = (JSONObject) parser.parse(line);
                if(file_name.equals("yelp_business.json")){
                    insert_to_business(jsonObj);
                }else if (file_name.equals("yelp_checkin.json")){
                    insert_to_checkin(jsonObj);
                }else if (file_name.equals("yelp_review.json")){
                    insert_to_review(jsonObj);
                }else if (file_name.equals("yelp_user.json")){
                    insert_to_user(jsonObj);
                }else {
                    throw new Exception("json file not found");
                }
//                break;  //TODO REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME REMOVE ME
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * IMPORTANT: File Path & directory matters!
     * Takes in one of four files:
     *    yelp_business.json
     *    yelp_review.json
     *    yelp_checkin.json
     *    yelp_user.json
     * These files should be in folder "YelpDataset" at the root of the project (equivalent with src folder)
     *
     * This will read the first entry (line) of the file and output the keys
     * @param file_name
     */
    private void view_json_object_keys(String file_name) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/"+ file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            JSONObject jsonObj = (JSONObject) parser.parse(line);
            Set<String> test = jsonObj.keySet();
            System.out.println(test);
            for (String item : test) {
                System.out.println(item + " --- " + jsonObj.get(item));
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This will return the input with as a string surrounded by quotes
     * i.e.
     *      input: hello world
     *      output: "hello world"
     * this will be primarily used with inserting values into the database to explicitly note a value as a string
     */
    private String add_quotes(String s){
        return "\"" + s + "\"";
    }

    private String remove_last_character(String s){
        return s.substring(0, s.length() - 1);
    }

    private String sanitize_string(String s){
        return s.replaceAll("[\n\r]", " ");
    }

    /**
     * This will covert the json object into an equivalent Database Query Insert String
     * @param entry : "line" of the json input file
     * @return Database Query INSERT String
     */
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

        insert_string = sanitize_string(insert_string);

        System.out.println(insert_string);
        return insert_string;
    }

    /**
     * This will covert the json object into an equivalent Database Query Insert String
     * @param entry : "line" of the json input file
     * @return Database Query INSERT String
     */
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

        insert_string = sanitize_string(insert_string);

        System.out.println(insert_string);
        return insert_string;
    }

    /**
     * This will covert the json object into an equivalent Database Query Insert String
     * @param entry : "line" of the json input file
     * @return Database Query INSERT String
     */
    private String insert_to_business(JSONObject entry) {

        List<String> fields = Arrays.asList("business_id", "name", "full_address", "city", "state", "longitude", "latitude", "review_count",
                "stars", "type", "open");

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


        JSONObject attributes = (JSONObject) entry.get("attributes");
        JSONArray categories = (JSONArray) entry.get("categories");


        String[] fields_result = new String[fields.size()];
        ArrayList<String> hour_fields_result = new ArrayList<>();

        for(int i=0; i<fields.size(); i++){
            fields_result[i] = entry.get(fields.get(i)).toString();
        }

        for(int i=0; i<day_of_week.length; i++){
            try {
                hour_fields_result.add((day_of_week[i].get("open").toString()));
            }catch (NullPointerException e){
                hour_fields_result.add("null");
            }
            try {
                hour_fields_result.add((day_of_week[i].get("close").toString()));
            }catch (NullPointerException e){
                hour_fields_result.add("null");
            }
        }


        String insert_string = "INSERT INTO Business VALUES(";
        //business_id
        insert_string += add_quotes(fields_result[fields.indexOf("business_id")]);
        insert_string += ",";
        //name
        insert_string += add_quotes(fields_result[fields.indexOf("name")]);
        insert_string += ",";
        //full_address
        insert_string += add_quotes(fields_result[fields.indexOf("full_address")]);
        insert_string += ",";
        //city
        insert_string += add_quotes(fields_result[fields.indexOf("city")]); //TODO SHOULD THIS BE QUOTED? IS THIS VARCHAR OR INT?
        insert_string += ",";
        //state
        insert_string += fields_result[fields.indexOf("state")];
        insert_string += ",";
        //longitude
        insert_string += add_quotes(fields_result[fields.indexOf("longitude")]);
        insert_string += ",";
        //latitude
        insert_string += fields_result[fields.indexOf("latitude")];
        insert_string += ",";
        //review_count
        insert_string += add_quotes(fields_result[fields.indexOf("review_count")]);
        insert_string += ",";
        //open
        insert_string += fields_result[fields.indexOf("stars")];
        insert_string += ",";
        //type
        insert_string += fields_result[fields.indexOf("type")];
        insert_string += ",";
        //open
        insert_string += fields_result[fields.indexOf("open")];
        insert_string += ",";

        for (String s : hour_fields_result){
            insert_string += add_quotes(s);
            insert_string += ",";
        }

        insert_string = remove_last_character(insert_string);

        insert_string += ");";

        insert_string = sanitize_string(insert_string);

        System.out.println(insert_string);


        insert_to_business_attributes(fields_result[fields.indexOf("business_id")], attributes);

        insert_to_business_categories(fields_result[fields.indexOf("business_id")], categories);



        return insert_string;
    }


    private void insert_to_business_attributes(String business_id, JSONObject entry){
        for (Object key : entry.keySet()) {
            String insert_string = "INSERT INTO Attributes VALUES(";
            insert_string += add_quotes(business_id);
            insert_string += ",";
            insert_string += add_quotes(key.toString());
            insert_string += ",";
            insert_string += add_quotes(entry.get(key).toString());
            insert_string += ");";
            insert_string = sanitize_string(insert_string);
            System.out.println(insert_string);
        }

    }

    private void insert_to_business_categories(String business_id, JSONArray entry){
        for (Object s : entry) {
            String insert_string = "INSERT INTO Categories VALUES(";
            insert_string += add_quotes(business_id);
            insert_string += ",";
            insert_string += add_quotes(s.toString());
            insert_string += ");";
            insert_string = sanitize_string(insert_string);
            System.out.println(insert_string);
        }

    }


    /**
     * This will covert the json object into an equivalent Database Query Insert String
     * @param entry : "line" of the json input file
     * @return Database Query INSERT String
     */
    private String insert_to_checkin(JSONObject entry) {

        List<String> fields = Arrays.asList("business_id", "type", "checkin_info");
        String[] fields_result = new String[fields.size()];

        //TODO CHECKIN INFO?????

        for(int i=0; i<fields.size(); i++){
            fields_result[i] = entry.get(fields.get(i)).toString();
        }

        String insert_string = "INSERT INTO CheckIn VALUES(";
        //user_id
        insert_string += add_quotes(fields_result[fields.indexOf("business_id")]);
        insert_string += ",";
        //name
        insert_string += add_quotes(fields_result[fields.indexOf("type")]);
        insert_string += ",";
        //yelping_since
        insert_string += add_quotes(fields_result[fields.indexOf("checkin_info")]);
        insert_string += ");";
        insert_string = sanitize_string(insert_string);
        System.out.println(insert_string);
        return insert_string;
    }
}
