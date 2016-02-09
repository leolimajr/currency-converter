Feature: Sign up
  As a customer
  In order to have access the website content
  I need to sign up to the website
Scenario: valid input
  Given I am on the sign up page
  When I provide the email address "newuser@test.com"
  And I provide the birth date "01/01/1990"
  And I provide the street address "Mainstrabe 10"
  And I provide the zip code "85579"
  And I provide the city name "Munich"
  And I provide the country "DE"
  And I provide the password  "userpassword"
  And I provide the password confirmation "userpassword"
  Then I should register as a customer