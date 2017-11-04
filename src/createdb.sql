-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- Create Tables --

---------------------------------- GATHERED FROM YELP_USER.JSON --------------------------------------------

-- Yelp User --
CREATE TABLE User (
    user_id VARCHAR(255) PRIMARY KEY, -- Varchar because data given is U1, U2...
    name VARCHAR(255),
    yelping_since VARCHAR(255),
    average_stars VARCHAR(255),
    review_count INTEGER,
    type VARCHAR(255),
    fans INTEGER,
    c_note INTEGER,   -- c for compliments
    c_plain INTEGER,
    c_cool INTEGER,
    c_hot INTEGER,
    c_funny INTEGER,
    v_cool INTEGER,    -- v for votes
    v_useful INTEGER,
    v_funny INTEGER
);

CREATE TABLE Friends (
    user_id VARCHAR(255)
);

CREATE TABLE Elite (
    user_id VARCHAR(255)
);

---------------------------------- GATHERED FROM YELP_BUSINESS.JSON --------------------------------------------
-- Business --
CREATE TABLE Business (
    business_id VARCHAR(255) PRIMARY KEY, -- Varchar because of input given in excel
    name VARCHAR(255),
    full_address VARCHAR(255),
    longitude VARCHAR (255),
    latitude VARCHAR(255),
    review_count INTEGER,
    stars VARCHAR(255),
    type VARCHAR(255),
    open VARCHAR(255)
);

-- Attributes --
CREATE TABLE Attributes(
    business_id VARCHAR(255),
    attribute VARCHAR(255),
    value VARCHAR (255), --true or false
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE Categories(
    business_id VARCHAR(255),
    category VARCHAR(255),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE Neighborhoods(
    business_id VARCHAR (255),
    neighborhood VARCHAR(255),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE Hours(
    business_id VARCHAR (255),
    mon_open VARCHAR(255),
    mon_close VARCHAR(255),
    tue_open VARCHAR(255),
    tue_close VARCHAR(255),
    wed_open VARCHAR(255),
    wed_close VARCHAR(255),
    thu_open VARCHAR(255),
    thu_close VARCHAR(255),
    fri_open VARCHAR(255),
    fri_close VARCHAR(255),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

---------------------------------- GATHERED FROM YELP_CHECKIN.JSON --------------------------------------------
CREATE TABLE CheckIn (
    business_id VARCHAR(255),
    type VARCHAR(255),
    checkin_indo VARCHAR(255),  --TODO HOW AM I GOING TO DISPLAY THIS SHIT?
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);


---------------------------------- GATHERED FROM YELP_REVIEW.JSON --------------------------------------------
CREATE TABLE Review(
    review_id VARCHAR(255),
    date_string VARCHAR(255),
    v_cool INTEGER,
    v_useful INTEGER,
    v_funny INTEGER,
    stars INTEGER,
    text VARCHAR(255),
    type VARCHAR(255),
    user_id VARCHAR(255),
    business_id VARCHAR(255),
    FOREIGN KEY (business_id) REFERENCES Business(business_id),
    FOREIGN KEY (user_id) REFERENCES YelpUser(user_id)
);