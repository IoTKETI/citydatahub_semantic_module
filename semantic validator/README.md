## Semantic Validator

Semantic Validator is a Java Maven project which can be imported and executed in compatible java IDE such as Eclipse and IntelliJ. Following are the dependencies required for deployment.



### 1 **Dependencies**

-  Minimum version of Java Required: 1.5.
-  Maven Dependencies: These are listed under <dependencies> tab in “pom.xml”, in main project directory.
   - apache-jena-libs (version: 3.9.0)
   - org.semanticweb.hermit (version: 1.3.8.4) 
-  External Jar Files: These files are not available in maven repository and may need to add in the build path manually. These can be found in project/lib/
   - virt_jena3.jar
   - virtjdbc4.jar
-  Connection with Virtuoso Triple Store: Three parameters are required to connect to the virtuoso: host URL, user name and password. In addition, it is required that the triple store is already populated with ontologies (vocabularies), so that Semantic Validator can retrieve them for the validation.



### 2 **Configuration** 

- The file is located at project/src/java/com/semantics/InterfaceConfiguration.java, where virtuoso triple store parameters can be configured.
  - The parameters are described as follows:


| **Property**  | **Sample**                     | **Description**                                              |
| ------------- | ------------------------------ | ------------------------------------------------------------ |
| base_tdb__url | jdbc:virtuoso://localhost:1111 | Parameter to  set host URL. This default value is set for localhost. However it can be  changed to the specific ip address |
| user_id       | dba                            | Default user  name for virtuoso TDB. However, different user can be provided if created in  virtuoso with appropriate permissions. |
| user_pass     | dba                            | Default user  password for virtuoso TDB. However, different user information can be  provided if created in virtuoso with appropriate permissions. |



### 3 **Important File Directories**

- Following are the important directories for Semantic Validator.
  - Project java code files : project/src/java/com/semantics/validation
  - External Jar files : project/lib
  - Maven Dependencies : project/pom.xml




## 4 Deployment Steps

NOTE: the installation instructions are based on eclipse IDE in windows, so it may vary for different IDEs and platforms.



### 4.1 **Download Project**

Download the project from github and choose "semantic validator"



### 4.2 **Import Project in IDE**

- In Eclipse, Go to File>Import>Maven>Existing Maven Projects
- Locate the downloaded project and import.
- 

### 4.3 **Add external Jar dependencies**

- The external jars to be added are located in the project at project/lib. If they are not already added, then must be added manually.
- In Eclipse, right click on project folder, go to Build Path > Configure Build Path > Java Build Path. Then go under the Libraries tab, which shows the current jars added to the build path.
- On the right side click “Add External Jars”.
- Locate the lib directory and select the files inside.
- Click “Apply and Close”



### 4.4 **Application Deployment**

The class Validator.java can be used for validation. Which has three main functions: createOWLOntologyFromInstance(), isEntailed(), isConsistent().

- Constructor: Validator(): retrieves the ontologies from the TDB and generates OWLOntology object which is required for validation. Therefore it’s object should be created only when the ontology is changed or added.
- createOWLOntologyFromInstance(): this converts the individual assertions in the Jena Model object to OWLOntology object, which is required to be validated. Therefore execute this function before validation, to populate the instance object, which contains the individual assertions.
- isConsistent(): Use this function to check whether the individual assertions are consistent with the ontologies in the TDB.
- isEntailed(): Similarly use this function to validate entailment.
