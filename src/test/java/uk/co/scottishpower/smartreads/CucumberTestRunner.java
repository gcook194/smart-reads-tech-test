package uk.co.scottishpower.smartreads;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@SuppressWarnings("NewClassNamingConvention")
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"uk.co.scottishpower.smartreads.cucumberglue"})
public class CucumberTestRunner {

}
