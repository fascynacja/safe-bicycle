# safe bicycle
I have created this project as a proof of concept and a starting point which can be extended as needed. Depending on current requirements it can be optimised or simplified.


1) Requirements and input data
 
- rule engine is based on scripts provided on given path- currently on classpath.  
- new risk type can be introduced without making changes in the code, just by adding new groovy file to a directory configured by property
- 1 endpoint which takes as input a set of bicycles and returns calculated values
 
2) Conclusions
-  Since the rules engine need to be flexible to introduce new risk types I have decided to dynamically load the scripts from given path. 

3) Final solution

- The idea is that every risk type will have its own groovy script defined, which will calculate premium and sum insured data according to the specification
  - all those scripts will have a BaseScript. I added additional layer: "DecoratedBaseScript", which collects common calculations shared by all scripts for all risk types. 
- When the application is starting it is reading all script files from path provided by property: rules.path. The script names have to be the same as risk types. They will be stored in a mapping:
  - key=riskType , value=Script
- Mapping will be used by rule engine to call specific furmulas for given riskTypes. 
- I have created 1 endpoint, which takes set of bicycles and return premium and sum insured calculations along with some bicycle properties.
- Endpoint will call CalculationService which will do basic validation, dto-entity mappings and later on use the RuleEngine in order to run formulas defined in the groovy scripts.
- In order to see the current API please run the application and check the page:
  http://localhost:8080/swagger-ui/index.html 
  
 
4) Possible optimisations and trade offs

- store scripts in distributed storage like Amazon S3  
- validation of groovy scripts, which will check if they contain needed methods, called by rules-engine.
- better validation of bicycle data, which is necessary to run the groovy formulas (eg risks not empty)
- adding a script to the directory without need to restart the app. A special endpoint could be introduced which would recreate the rule engine or read again all scripts and supplement the missing one.
- better quality of groovy code and package organisation
- if needed this service can be easily scaled, just common source of scripts needs to be provided.

5) Tests

I have created a couple of tests for every layer (REST, Integration, JUnit Service) with basic scnenarios. They are not comprehensive for the real production scenario. I would add more detailed paths and scenarios for a real live project.
 
6) Running the app

- You can run the app from commandline:
  ```
  ./mvnw spring-boot:run
  ```
  
7) Notes
   
I have changed the BaseScript in the following line:
  ```
 ['VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 1, 'FACTOR_MIN': 3]]
  ```
I believe there is mistake having two times FACTOR_MIN instead of FACTOR_MAX. I changed it into 
  ```
 ['VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 1, 'FACTOR_MAX': 3]]
  ```
   


