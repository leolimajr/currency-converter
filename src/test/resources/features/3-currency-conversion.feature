Feature: Currency Conversion
  As a customer
  In order to convert a currency
  I need to submit a quote to the website
  
Background:
  Given I am logged in with username "user@test.com" and password "userpassword"
  
Scenario: valid currency conversion
  Given I am on the conversion page
  When I provide the currency I have "USD"
  And I provide the currency I want "BRL"
  And I provide the amount I have "1000"
  Then I should get a currency conversion