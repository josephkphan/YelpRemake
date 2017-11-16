import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.PreparedStatement;
import java.util.*;

public class Populate {

    JDBCHandler jdbc_handler;

    private String[] mbc = {
            "Active Life", "Arts and Entertainment", "Automotive", "Car Rental", "Cafe",
            "Beauty and Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores",
            "Department Stores", "Education", "Event Planning and Services", "Flowers and Gifts",
            "Food", "Health and Medical", "Home Services", "Home and Garden", "Hospitals", "Hotels and Travel",
            "Hardware Stores", "Grocery", "medical Centers", "Nurseries and Gardening", "Nightlife",
            "Restaurants", "Shopping", "Transportation"
    };

    private static final String insert_user_string = "INSERT INTO YelpUser VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String insert_business_string = "INSERT INTO Business VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String insert_attributes_string = "INSERT INTO Attributes VALUES (?,?,?)";
    private static final String insert_main_categories_string = "INSERT INTO MainCategories VALUES (?,?)";
    private static final String insert_sub_categories_string = "INSERT INTO SubCategories VALUES (?,?)";
    private static final String insert_review_string = "INSERT INTO Review VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String insert_check_in_string = "INSERT INTO CheckIn VALUES (?,?,?,?)";

    PreparedStatement business_statement, user_statement, attribute_statement, main_category_statement,
            sub_category_statement, review_statement, check_in_statement;

    private static final int BATCH_SIZE = 1000;

    private Map<String, Integer> batch_counter = new HashMap<>();

    private ArrayList<String> main_business_categories = new ArrayList<>(Arrays.asList(mbc));

    public static void main(String[] args) { //TODO CHECK THE INPUTS TO HAVE THE 4 json files
        JDBCHandler jdbc_handler = new JDBCHandler();

        Populate reader = new Populate(jdbc_handler);
//        reader.read_file();
//        reader.view_all_file_keys();

        //The Order matters here! Look at createdb so see the dependencies (foreign keys)

        // execute example java Populate yelp_business.json yelp_user.json yelp_review.json yelp_checkin.json
//        args = new String[]{"yelp_checkin.json"};
        args = new String[]{"yelp_business.json", "yelp_user.json", "yelp_review.json", "yelp_checkin.json"};

        // Populates Business Table
        if (Arrays.asList(args).contains("yelp_business.json")) {
            System.out.println("Populating Tables: Business, MainCategories, SubCategories, Attributes");
            reader.insert_to_business("yelp_business.json");
        }

        if (Arrays.asList(args).contains("yelp_user.json")) {
            System.out.println("Populating Table: YelpUser");
            reader.insert_to_user("yelp_user.json");
        }

        if (Arrays.asList(args).contains("yelp_review.json")) {
            System.out.println("Populating Table: Review");
            reader.insert_to_review("yelp_review.json");
        }

        if (Arrays.asList(args).contains("yelp_checkin.json")) {
            System.out.println("Populating Table: CheckIn");
            reader.insert_to_checkin("yelp_checkin.json");
        }

        jdbc_handler.closeConnection();
    }

    public Populate(JDBCHandler jdbc_handler) {
        this.jdbc_handler = jdbc_handler;
        business_statement = jdbc_handler.getPreparedStatement(insert_business_string);
        check_in_statement = jdbc_handler.getPreparedStatement(insert_check_in_string);
        user_statement = jdbc_handler.getPreparedStatement(insert_user_string);
        attribute_statement = jdbc_handler.getPreparedStatement(insert_attributes_string);
        main_category_statement = jdbc_handler.getPreparedStatement(insert_main_categories_string);
        sub_category_statement = jdbc_handler.getPreparedStatement(insert_sub_categories_string);
        review_statement = jdbc_handler.getPreparedStatement(insert_review_string);

        batch_counter.put("business", 0);
        batch_counter.put("check_in", 0);
        batch_counter.put("user", 0);
        batch_counter.put("attribute", 0);
        batch_counter.put("main_category", 0);
        batch_counter.put("sub_category", 0);
        batch_counter.put("review", 0);
    }

    private String sanitize_string(String s) {
        s = s.replaceAll("[\n\r]", " ");
        s = s.replaceAll("'", "");
        s = s.replaceAll("&", "and");// & Symbols don't work during inserts into DB
        if (s.length() > 3000) {
            s = s.substring(0, 2500); //For Text Cap
        }
        return s.trim();
    }

    private void writeBatch(PreparedStatement statement, String batch_key, boolean forceWrite) throws Exception {
        if (forceWrite) {
            statement.executeBatch();
            statement.clearBatch();
            batch_counter.replace(batch_key, 0);
        } else {
            batch_counter.replace(batch_key, batch_counter.get(batch_key) + 1);
            if (batch_counter.get(batch_key) % BATCH_SIZE == 0) {
                statement.executeBatch();
                statement.clearBatch();
                batch_counter.replace(batch_key, 0);
            }
        }
    }

    private Boolean isMainBusinessCategory(String s) {
        return main_business_categories.contains(s);
    }


    /**
     * This will covert the json object into an equivalent Database Query Insert String
     *
     * @param file_name : name of user json file
     * @return Database Query INSERT String
     */
    private void insert_to_user(String file_name) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/" + file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject entry = (JSONObject) parser.parse(line);

                List<String> fields = Arrays.asList("user_id", "name", "yelping_since", "average_stars", "review_count", "type", "fans");
                List<String> vote_fields = Arrays.asList("cool", "useful", "funny");
                List<String> compliment_fields = Arrays.asList("note", "plain", "cool", "hot", "funny");//TODO  MAKE THIS ITS OWN TABLE

                JSONObject votes = (JSONObject) entry.get("votes");
                JSONObject compliments = (JSONObject) entry.get("compliments");

                String[] fields_result = new String[fields.size()];
                String[] votes_fields_result = new String[vote_fields.size()];
                String[] compliments_fields_result = new String[compliment_fields.size()];

                for (int i = 0; i < fields.size(); i++) {
                    fields_result[i] = sanitize_string(entry.get(fields.get(i)).toString());
                }

                for (int i = 0; i < vote_fields.size(); i++) {
                    votes_fields_result[i] = sanitize_string(votes.get(vote_fields.get(i)).toString());
                }

                for (int i = 0; i < compliment_fields.size(); i++) {
                    try {
                        compliments_fields_result[i] = compliments.get(compliment_fields.get(i)).toString();
                    } catch (Exception e) {
                        compliments_fields_result[i] = "0";
                    }
                }

                try {
                    user_statement.setString(1, fields_result[fields.indexOf("user_id")]);
                    user_statement.setString(2, fields_result[fields.indexOf("name")]);
                    user_statement.setString(3, fields_result[fields.indexOf("yelping_since")]);
                    user_statement.setString(4, fields_result[fields.indexOf("average_stars")]);
                    user_statement.setString(5, fields_result[fields.indexOf("review_count")]);
                    user_statement.setString(6, fields_result[fields.indexOf("type")]);
                    user_statement.setString(7, fields_result[fields.indexOf("fans")]);
                    user_statement.setInt(8, Integer.parseInt(votes_fields_result[vote_fields.indexOf("cool")]));
                    user_statement.setInt(9, Integer.parseInt(votes_fields_result[vote_fields.indexOf("useful")]));
                    user_statement.setInt(10, Integer.parseInt(votes_fields_result[vote_fields.indexOf("funny")]));
                    user_statement.addBatch();

                    writeBatch(user_statement, "user", false);

                } catch (Exception e) {
                    System.out.println("EXCEPTION insert_to_user: " + e.getMessage());
                }
            }
            fileReader.close();
            writeBatch(user_statement, "user", true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This will covert the json object into an equivalent Database Query Insert String
     *
     * @param file_name : "line" of the json input file
     * @return Database Query INSERT String
     */
    private void insert_to_business(String file_name) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/" + file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject entry = (JSONObject) parser.parse(line);

                List<String> fields = Arrays.asList("business_id", "name", "full_address", "city", "state", "longitude", "latitude", "review_count",
                        "stars", "type", "open");

                List<String> hours_fields = Arrays.asList("mon_open", "mon_close", "tue_open", "tue_close", "wed_open", "wed_close", "thu_open",
                        "thu_close", "fri_open", "fri_close", "sat_open", "sat_close", "sun_open", "sun_close");

                JSONObject hours = (JSONObject) entry.get("hours");
                JSONObject[] day_of_week = new JSONObject[7];
                day_of_week[0] = (JSONObject) hours.get("Monday");
                day_of_week[1] = (JSONObject) hours.get("Tuesday");
                day_of_week[2] = (JSONObject) hours.get("Wednesday");
                day_of_week[3] = (JSONObject) hours.get("Thursday");
                day_of_week[4] = (JSONObject) hours.get("Friday");
                day_of_week[5] = (JSONObject) hours.get("Saturday");
                day_of_week[6] = (JSONObject) hours.get("Sunday");


                JSONObject attributes = (JSONObject) entry.get("attributes");
                JSONArray categories = (JSONArray) entry.get("categories");


                String[] fields_result = new String[fields.size()];
                ArrayList<String> hour_fields_result = new ArrayList<>();

                for (int i = 0; i < fields.size(); i++) {
                    fields_result[i] = sanitize_string(entry.get(fields.get(i)).toString());
                }

                for (int i = 0; i < day_of_week.length; i++) {
                    try {
                        hour_fields_result.add((day_of_week[i].get("open").toString()));
                    } catch (NullPointerException e) {
                        hour_fields_result.add("null");
                    }
                    try {
                        hour_fields_result.add((day_of_week[i].get("close").toString()));
                    } catch (NullPointerException e) {
                        hour_fields_result.add("null");
                    }
                }

                try {
                    business_statement.setString(1, fields_result[fields.indexOf("business_id")]);
                    business_statement.setString(2, fields_result[fields.indexOf("name")]);
                    business_statement.setString(3, fields_result[fields.indexOf("full_address")]);
                    business_statement.setString(4, fields_result[fields.indexOf("city")]);
                    business_statement.setString(5, fields_result[fields.indexOf("state")]);
                    business_statement.setString(6, fields_result[fields.indexOf("longitude")]);
                    business_statement.setString(7, fields_result[fields.indexOf("latitude")]);
                    business_statement.setLong(8, Long.parseLong(fields_result[fields.indexOf("review_count")]));
                    business_statement.setDouble(9, Double.parseDouble(fields_result[fields.indexOf("stars")]));
                    business_statement.setString(10, fields_result[fields.indexOf("type")]);
                    business_statement.setString(11, fields_result[fields.indexOf("open")]);
                    for (int i = 12; i <= 25; i++) {
                        business_statement.setString(i, hour_fields_result.get(i - 12));
                    }
                    business_statement.addBatch();
                    writeBatch(business_statement, "business", true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("EXCEPTION insert_to_business: " + e.getMessage());
                }

                insert_to_business_attributes(fields_result[fields.indexOf("business_id")], attributes, "");
                insert_to_business_categories(fields_result[fields.indexOf("business_id")], categories);
            }
            fileReader.close();
            writeBatch(business_statement, "business", true);
            writeBatch(attribute_statement, "business", true);
            writeBatch(main_category_statement, "main_category", true);
            writeBatch(sub_category_statement, "sub_category", true);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void insert_to_business_attributes(String business_id, JSONObject entry, String prefix) {
        try {
            for (Object key : entry.keySet()) {
//                System.out.println(prefix + sanitize_string(key.toString()));
//                System.out.println(entry.get(key).toString());
                if (entry.get(key).toString().contains("{")) {
                    insert_to_business_attributes(business_id, (JSONObject) entry.get(key), key.toString());
                } else {
                    attribute_statement.setString(1, sanitize_string(business_id));
                    attribute_statement.setString(2, sanitize_string(prefix + " " + sanitize_string(key.toString()) + " " + entry.get(key).toString()));
                    attribute_statement.setString(3, sanitize_string(entry.get(key).toString()));
                    attribute_statement.addBatch();
                    writeBatch(attribute_statement, "attribute", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EXCEPTION insert_to_business_attributes: " + e.getMessage());
        }
    }

    private void insert_to_business_categories(String business_id, JSONArray entry) {
        try {
            for (Object s : entry) {
                if (isMainBusinessCategory(sanitize_string(s.toString()))) {
                    main_category_statement.setString(1, sanitize_string(business_id));
                    main_category_statement.setString(2, sanitize_string(s.toString()));
                    main_category_statement.addBatch();
                    writeBatch(main_category_statement, "main_category", false);

                } else {
                    sub_category_statement.setString(1, sanitize_string(business_id));
                    sub_category_statement.setString(2, sanitize_string(s.toString()));
                    sub_category_statement.addBatch();
                    writeBatch(sub_category_statement, "sub_category", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EXCEPTION insert_to_business_categories: " + e.getMessage());
        }
    }

    /**
     * This will covert the json object into an equivalent Database Query Insert String
     *
     * @param file_name : "line" of the json input file
     * @return Database Query INSERT String
     */
    private void insert_to_review(String file_name) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/" + file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject entry = (JSONObject) parser.parse(line);

                List<String> fields = Arrays.asList("review_id", "date", "stars", "text", "type", "user_id", "business_id");
                List<String> vote_fields = Arrays.asList("cool", "useful", "funny");

                JSONObject votes = (JSONObject) entry.get("votes");

                String[] fields_result = new String[fields.size()];
                String[] votes_fields_result = new String[vote_fields.size()];

                for (int i = 0; i < fields.size(); i++) {
                    fields_result[i] = sanitize_string(entry.get(fields.get(i)).toString());
                }

                for (int i = 0; i < vote_fields.size(); i++) {
                    votes_fields_result[i] = votes.get(vote_fields.get(i)).toString();
                }

                try {
                    review_statement.setString(1, fields_result[fields.indexOf("review_id")]);
                    review_statement.setString(2, fields_result[fields.indexOf("date")]);
                    review_statement.setInt(3, Integer.parseInt(votes_fields_result[vote_fields.indexOf("cool")]));
                    review_statement.setInt(4, Integer.parseInt(votes_fields_result[vote_fields.indexOf("useful")]));
                    review_statement.setInt(5, Integer.parseInt(votes_fields_result[vote_fields.indexOf("funny")]));
                    review_statement.setDouble(6, Double.parseDouble(fields_result[fields.indexOf("stars")]));
                    review_statement.setString(7, fields_result[fields.indexOf("text")]);
                    review_statement.setString(8, fields_result[fields.indexOf("type")]);
                    review_statement.setString(9, fields_result[fields.indexOf("user_id")]);
                    review_statement.setString(10, fields_result[fields.indexOf("business_id")]);

                    review_statement.addBatch();
                    writeBatch(review_statement, "review", false);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("EXCEPTION insert_to_review: " + e.getMessage());
                }
            }
            fileReader.close();
            writeBatch(review_statement, "review", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This will covert the json object into an equivalent Database Query Insert String
     *
     * @param file_name : "line" of the json input file
     * @return Database Query INSERT String
     */
    private void insert_to_checkin(String file_name) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("YelpDataset/" + file_name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject entry = (JSONObject) parser.parse(line);
                List<String> fields = Arrays.asList("business_id", "type", "checkin_info");
                String[] fields_result = new String[fields.size()];

                for (int i = 0; i < fields.size(); i++) {
                    fields_result[i] = sanitize_string(entry.get(fields.get(i)).toString());
                }
                int total_checkins = 0;
                JSONObject checking_info = (JSONObject) entry.get("checkin_info");
                for (Object key : checking_info.keySet()) {
                    total_checkins +=  Integer.parseInt(checking_info.get(key).toString());
                }

                try {
                    check_in_statement.setString(1, fields_result[fields.indexOf("business_id")]);
                    check_in_statement.setString(2, fields_result[fields.indexOf("type")]);
                    check_in_statement.setString(3, fields_result[fields.indexOf("checkin_info")]);
                    check_in_statement.setInt(4, total_checkins);
                    check_in_statement.addBatch();
                    writeBatch(check_in_statement, "check_in", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fileReader.close();
            writeBatch(check_in_statement, "check_in", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
