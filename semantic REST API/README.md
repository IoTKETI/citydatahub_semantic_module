## 1 Deployment Steps

NOTE: the installation instructions are based on Eclipse IDE in Windows, so it may vary for different IDEs and platforms.



### 1.1 **Download Project**

Download the project from github and choose "semantic REST API"



### 1.2 **Import Project in IDE**

- In Eclipse, Go to File>Import>Maven>Existing Maven Projects

- Locate the downloaded project and import.

  

### 1.3 **Add external Jar dependencies**

- The external jars to be added are located in the project at project/lib. If they are not already added, then must be added manually.
- In Eclipse, right click on project folder, go to Build Path > Configure Build Path > Java Build Path. Then go under the Libraries tab, which shows the current jars added to the build path.
- On the right side click “Add External Jars”.
- Locate the lib directory and select the files inside.
- Click “Apply and Close”



### 1.4 **Application Deployment**

NOTE: The TDB and ACP Server must be running prior to the execution of this Application.

After all the required dependencies have been installed, the application can be executed using ServerApplication.java class located at project/src/java/com/cityhub/semantic/ServerApplication.java:

- Right click on ServerApplication.java, select "Run As", select either "Java Application" or "Spring Boot App".
- Upon execution, standard Spring Boot execution can be seen on the Eclipse Console.
- After the information is displayed: "...Started ServerApplication in 2.886 seconds ...", the console will stop displaying further information and the API is online to provide RESTful services.
