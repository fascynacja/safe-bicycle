package org.pysz.safebicycle.rules;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
@Slf4j
public class RulesConfiguration {

    @Value("${rules.path}")
    public String rulesPath = "src/main/resources/rules/risk/";

    @Bean
    RuleEngine ruleEngine() throws IOException {

        File risksDirectory = ResourceUtils.getFile(rulesPath);
        if (!risksDirectory.exists()) {
            log.error("Provided path to risks directory is invalid: {}", rulesPath); //fail fast , context will not load
            throw new RuleEngineException("Provided path to risks directory is invalid: " + rulesPath);
        }
        File[] files = ResourceUtils.getFile(rulesPath).listFiles();
        if (files == null){
            log.error("Provided path to risks directory is invalid: {}", rulesPath); //fail fast , context will not load
            throw new RuleEngineException("Provided path to risks directory is invalid: " + rulesPath);
        }
        Map<String, Script> ruleStorage = new HashMap<>();
        for (File file : files) {
            Script ruleScript = new GroovyShell().parse(file); // validation of script, does it have needed methods etc
            ruleStorage.put(ruleScript.getMetaClass().getTheClass().getName(), ruleScript);
        }
        if (ruleStorage.isEmpty()) {
            log.warn("The rule engine was not initialized correctly, because no rule files were found in given path: {}", rulesPath); //fail fast , context will not load
            throw new RuleEngineException("The rule engine was not initialized correctly, because no rule files were found in path: " + rulesPath);
        }
        return new RuleEngine(ruleStorage);
    }
}
