package com.ford.featuretoggle;


import static java.lang.Boolean.valueOf;
import static java.lang.System.getenv;
import static java.lang.String.format;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


public class FeatureRule implements TestRule {
    private static String FLAG_NAME = "FEATURES_%s_ENABLED";

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Feature feature = description.getAnnotation(Feature.class);
                if (feature != null) {
                    String flag = getenv(format(FLAG_NAME, feature.value().toString()));
                    boolean enabled = valueOf(flag != null ? flag : "false");
                    if (enabled) {
                        base.evaluate();
                    } else {
                        System.out.println("Skipping 'feature test' " + description.getMethodName() + " since "
                                + feature.value().toString() + " is not enabled.");
                    }
                } else {
                    base.evaluate();
                }
            }
        };
    }



}