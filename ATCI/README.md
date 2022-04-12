
This document describes the method and setting information for deploying Annotation Template Creation Interface (ATCI) Application.

## 1 Overview

This document provides the instruction to utilize the Annotation Template Creation Interface (ATCI) for generating templates for semantic annotation of RDF triple data. It is an application with Graphical User Interface through which the template designer or the semantic expert can select the minimal required classes properties and relationships, for annotating the considered non-semantic data. The output format of the generated template is JSON. The template is then delivered to the Semantic Annotation tool, where it is utilized to perform annotation.



## 2 Pre-Installation

ATCI is a Java Maven project which can be imported and executed in compatible java IDE such as Eclipse and ItelliJ. Following are the dependencies required for deployment of ATCI.



### 2.1 **Dependencies**

-  Minimum version of Java Required: 1.5.
-  Maven Dependencies: These are listed under <dependencies> tab in “pom.xml”, in main project directory.
   - apache-jena-libs (version: 3.9.0)
   - json-simple (version: 1.1.1)
   - gson (version: 2.8.6)
-  External Jar Files: These files are not available in maven repository and may need to add in the build path manually. These can be found in project/lib/
   - virt_jena3.jar
   - virtjdbc4.jar
-  Connection with Virtuoso Triple Store: Three parameters are required to connect to the virtuoso: host URL, user name and password. In addition, it is required that the triple store is already populated with ontologies (vocabularies), so that ATCI can retrieve them for the template creation.



### 2.2 **Configuration** 

- There is only one file located at project/src/java/com/semantics/InterfaceConfiguration.java, where virtuoso triple store parameters and application font size can be configured.
  - The parameters are described as follows:


| **Property**  | **Sample**                     | **Description**                                              |
| ------------- | ------------------------------ | ------------------------------------------------------------ |
| TDB_URL       | jdbc:virtuoso://localhost:1111 | Parameter to  set host URL. This default value is set for localhost. However it can be  changed to the specific ip address |
| TDB_USER_NAME | dba                            | Default user  name for virtuoso TDB. However, different user can be provided if created in  virtuoso with appropriate permissions. |
| TDB_USER_PASS | dba                            | Default user  password for virtuoso TDB. However, different user information can be  provided if created in virtuoso with appropriate permissions. |
| fontSize      | 62                             | Application font size                                        |



### 2.3 **Important File Directories**

- Following are the important directories for ATCI.
  - Project java code files : project/src/java/com/semantics/
  - External Jar files : project/lib
  - Maven Dependencies : project/pom.xml
  - Directory to store Saved Entries : project/EntryStore



## 3 Deployment Steps

NOTE: the installation instructions are based on eclipse IDE in windows, so it may vary for different IDEs and platforms.


### 3.1 **Download Project**

Download the project from github and choose "ATCI"

### 3.2 **Import Project in IDE**

- In Eclipse, Go to File>Import>Maven>Existing Maven Projects
- Locate the downloaded project and import.

### 3.3 **Add external Jar dependencies**

- The external jars to be added are located in the project at project/lib. If they are not already added, then must be added manually.
- In Eclipse, right click on project folder, go to Build Path > Configure Build Path > Java Build Path. Then go under the Libraries tab, which shows the current jars added to the build path.
- On the right side click “Add External Jars”.
- Locate the lib directory and select the files inside.
- Click “Apply and Close”

### 3.4 **Run Application**

- In Eclipse, locate the StartApplication.java in the project, right click and go to Run As > Java Application.



## 4 Considerations

Following point should be considered prior to utilizing ATCI:

- ATCI requires to connect with Virtuoso Triple Database (TDB), whose configuration is described in the document “ATCI Deployment Guide”.
- It is required that the ontologies should be available in the TDB, prior to starting the ATCI application.
- It is required that the ontology prefixes should be provided prior to executing the ATCI application. These prefixes can be setup in {project directory}\src\main\java\com\semantic\PrefixSet.java
- The ATCI can extract basic information such as class definition, subclass definition, object and data property definition, basic domain and range definition of a property. It cannot process complex information such as those which are formed through UNION and INTERSECTION, which involve blank node, SWRL rules etc.



## 5 User Guide

The ATCI execution steps are defined in document: “ATCI Deployment Guide”. Following are the steps to generate a template using ATCI interface:



1. Upon execution, the first panel is to select the required graphs representing the ontologies in the TDB. This panel is visible at the top of the UI. The graphs to be selected from the list shown at the right side, and the graph which are selected are shown on the left side.
2. After selecting the ontology graphs, press the “Get Classes” button shown right after the drop down menu “Select Entry for Template Creation”.
3. All the class and their sub-class URIs will be be shown in the class selection panel, which are extracted from the ontologies selected earlier. Required classes can be selected from the list similar to the ontology graphs.
   1. If you select a class multiple times then ATCI will consider multiple instances to be generated for that class.
   2. Therefore, the selected classes on the right side, actually shows the procedurally generated Individual URI for the considered class.
   3. Since the class selection process can be tedious, based on the number of class definitions per ontology, the selected classes can be save as a local file using “Save Class Entry” button.
   4. After saving, the class entry will be available in the drop down menu “Select Entry for Template Creation”, to avoid manual reselection of all the same classes for next template creation.

4. After selecting all the require classes, press the “Get Object Properties” button to extract all the relevant object properties for the classes selected above. Then below the button, the middle drop down list will be populated with the extracted object properties.
5. Upon selecting an object property from the list, the left and right drop down lists will be populated, showing the all possible individuals from the above class selection panel. The object property assertion can be composed and then added or removed using buttons “Add Relation (+)” or “Remove Relation (-)” respectively.
   1. Only those individuals will be available in the list, which are consistent with the selected ontologies and the object property domains and ranges.

6. Similarly Data properties can be selected for the individuals generated in the class selection panel. The data property assertion can be composed and then added or removed using buttons “Add Data Relation” or “Remove Data Relation” respectively.
   1. In this case, the domain of the data property will be the XSD data type, in accordance with the respective data property range definitions.

7. After selecting all the properties, press the “Save Template” button at the bottom of the UI to generate and save the template in JSON format.



