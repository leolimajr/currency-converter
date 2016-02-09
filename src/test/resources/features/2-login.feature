Feature: Login
  As a customer
  In order to access the website content
  I need to log in to the website

Scenario: valid credentials
  Given I am on the login page
  When I provide the username "user@test.com"
  And I provide the password "userpassword"
  Then I should be successfully logged in