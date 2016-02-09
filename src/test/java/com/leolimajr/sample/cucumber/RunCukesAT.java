package com.leolimajr.sample.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", monochrome = true, plugin = {
		"html:target/cucumber-html-report", "junit:target/cucumber-junit/TEST-Cukes.xml"

})
public class RunCukesAT {
}