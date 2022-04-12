# SWRLRuleGeneratorAPI2021

SWRL Rule Generator API is a Java Maven project, developed using Java Spring Boot. The project can be imported and executed in compatible java IDE such as Eclipse and IntelliJ. Following are the dependencies required for deployment.


### 1 **Dependencies**

- Minimum version of Java Required: 1.5.
- Maven Dependencies: These are listed under <dependencies> tab in “pom.xml”, in main project directory.
  - apache-jena-libs (version: 3.9.0)
  - owlapi-distribution (version: 3.4.9.2-ansell)
  - pellet-owlapiv3 (version: 2.3.3)
  - mysql-connector-java (version 8.0.18)
  - json-simple (version 1.1.1)
  - gson (version 2.8.6)
  - jackson-dataformat-csv (version)
  - hibernate-core (version 5.5.8.Final)
  - hibernate-jpa-2.0-api (1.0.1.Final)
  - lombok (version 1.16.6)
  - commons-lang3 (version 3.0)

- External Jar Files: These files are not available in maven repository and may need to add in the build path manually. These can be found in project/lib/
  - virt_jena3.jar
  - virtjdbc4.jar
- Connection with Virtuoso Triple Store: Three parameters are required to connect to the virtuoso: API base URI, prefix URI and SWRL prefix. 



### 2 **APIConfiguration** 

- The file is located at project/src/main/java/semantics/cityhub/keti/APIConfiguration.java.
  - The parameters are described as follows:


| **Property** | **Sample**                                        | **Description**                                              |
| ------------ | ------------------------------------------------- | ------------------------------------------------------------ |
| API_BASE_URI | api/axiom-generator/v1                            | Parameter to set API base URL. This is required for the SWRL Labeling Tool. It can be  changed based on specific preferences |
| PREFIX_URI   | http://www.city-hub.kr/swrl/2021/1                | Default prefix URI to be used in defining SWRL concepts. It can be  changed based on specific preferences. |
| SWRL_PREFIX  | http://swrl.stanford.edu/ontologies/3.3/swrla.owl | Default SWRL prefix to be used for SWRL concept definitions. |



### 3 **Important File Directories**

- Following are the important directories for Semantic Validator.
  - Project java code files : project/src/main/java/semantics/cityhub/keti
    - API Data Access Object (DAO): project/src/main/java/semantics/cityhub/keti/SWRLRuleDAO.java
    - Java code to define SWRL Entity Models: project/src/main/java/semantics/cityhub/keti/model
    - Reasoner java code files: project/src/main/java/semantics/cityhub/keti/reasoner
    - Additional Utility code files: project/src/main/java/semantics/cityhub/keti/utils
  - External Jar files : project/lib
  - Maven Dependencies : project/pom.xml




## *4* Deployment Steps

NOTE: the installation instructions are based on Eclipse IDE in Windows, so it may vary for different IDEs and platforms.



### 4.1 **Download Project**

Download the project from github and choose "SWRLRuleGeneratorAPI2021"



### 4.2 **Import Project in IDE**

- In Eclipse, Go to File>Import>Maven>Existing Maven Projects
- Locate the downloaded project and import.

### 4.3 **Add external Jar dependencies**

- The external jars to be added are located in the project at project/lib. If they are not already added, then must be added manually.
- In Eclipse, right click on project folder, go to Build Path > Configure Build Path > Java Build Path. Then go under the Libraries tab, which shows the current jars added to the build path.
- On the right side click “Add External Jars”.
- Locate the lib directory and select the files inside.
- Click “Apply and Close”



### 4.4 **Application Deployment**

NOTE: The TDB and ACP Server must be running prior to the execution of this Application.

After all the required dependencies have been installed, the application can be executed using ServerApplication.java class located at project/src/main/java/semantics/cityhub/keti/SwrlRuleGeneratorApiApplication.java:

- Right click on SwrlRuleGeneratorApiApplication.java, select "Run As", select either "Java Application" or "Spring Boot App".
- Upon execution, standard Spring Boot execution can be seen on the Eclipse Console.
- After the information is displayed: "...Started ServerApplication in ..... seconds ...", the console will stop displaying further information and the API is online to provide RESTful services.
