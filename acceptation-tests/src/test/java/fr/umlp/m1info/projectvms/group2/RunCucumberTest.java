package fr.umlp.m1info.projectvms.group2;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("fr.umlp.m1info.projectvms.group2")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.umlp.m1info.projectvms.group2")
public class RunCucumberTest {
}
