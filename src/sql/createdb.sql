-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- Create Tables --

---------------------------------- GATHERED FROM YELP_USER.JSON --------------------------------------------

-- Yelp User --
CREATE TABLE YelpUser (
    user_id VARCHAR(100) Primary Key, -- Varchar because data given is U1, U2...
    name VARCHAR(100),
    yelping_since VARCHAR(100),
    average_stars FLOAT,
    review_count NUMBER,
    type VARCHAR(100),
    fans NUMBER,
    c_note NUMBER,   -- c for compliments
    c_plain NUMBER,
    c_cool NUMBER,
    c_hot NUMBER,
    c_funny NUMBER,
    v_cool NUMBER,    -- v for votes
    v_useful NUMBER,
    v_funny NUMBER
);

CREATE TABLE Friends (
    user_id VARCHAR(100)
);

CREATE TABLE Elite (
    user_id VARCHAR(100)
);

---------------------------------- GATHERED FROM YELP_BUSINESS.JSON --------------------------------------------
-- Business --
CREATE TABLE Business (
    business_id VARCHAR(100) PRIMARY KEY, -- Varchar because of input given in excel
    name VARCHAR(100),
    full_address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    longitude VARCHAR (100),
    latitude VARCHAR(100),
    review_count NUMBER,
    stars FLOAT,
    type VARCHAR(100),
    open VARCHAR(100),
    mon_open VARCHAR(100),
    mon_close VARCHAR(100),
    tue_open VARCHAR(100),
    tue_close VARCHAR(100),
    wed_open VARCHAR(100),
    wed_close VARCHAR(100),
    thu_open VARCHAR(100),
    thu_close VARCHAR(100),
    fri_open VARCHAR(100),
    fri_close VARCHAR(100),
    sat_open VARCHAR(100),
    sat_close VARCHAR(100),
    sun_open VARCHAR(100),
    sun_close VARCHAR (100)
);

-- Attributes --
CREATE TABLE Attributes(
    business_id VARCHAR(100),
    attribute VARCHAR(100),
    value VARCHAR (2500), --true or false
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE MainCategories(
    business_id VARCHAR(100),
    category VARCHAR(100),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE SubCategories(
    business_id VARCHAR(100),
    category VARCHAR(100),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

CREATE TABLE Neighborhoods(
    business_id VARCHAR (100),
    neighborhood VARCHAR(100),
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);

---------------------------------- GATHERED FROM YELP_REVIEW.JSON --------------------------------------------
CREATE TABLE Review(
    review_id VARCHAR(100) Primary Key,
    date_string VARCHAR(100),
    v_cool NUMBER,
    v_useful NUMBER,
    v_funny NUMBER,
    stars FLOAT,
    text VARCHAR(2000),
    type VARCHAR(100),
    user_id VARCHAR(100),
    business_id VARCHAR(100),
    FOREIGN KEY (business_id) REFERENCES Business(business_id),
    FOREIGN KEY (user_id) REFERENCES YelpUser(user_id)
);

---------------------------------- GATHERED FROM YELP_CHECKIN.JSON --------------------------------------------
CREATE TABLE CheckIn (
    business_id VARCHAR(100),
    type VARCHAR(100),
    checkin_info VARCHAR(2000),  --TODO HOW AM I GOING TO DISPLAY THIS SHIT?
    FOREIGN KEY (business_id) REFERENCES Business(business_id)
);


CREATE INDEX idx_bname
ON Business (name);

CREATE INDEX idx_aattribute
ON Attributes (attribute);

CREATE INDEX idx_maincategory
ON MainCategories (category);

CREATE INDEX idx_subcategory
ON SubCategories (category);
