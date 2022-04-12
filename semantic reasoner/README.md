# Semantic Reasoner

This document describes the method and setting information for deploying Semantic Reasoner.

NOTE: It is not a standalone application, rather it is a project which can be used as a component in Semantic Labeling Tool. 



## Overview

Semantic Reasoner is a Java Maven project which can be imported and executed in compatible java IDE such as Eclipse and IntelliJ. Following are the dependencies required for deployment.



### **Dependencies**

- Minimum version of Java Required: 1.5.
- Maven Dependencies: These are listed under <dependencies> tab in “pom.xml”, in main project directory.
  - apache-jena-libs (version: 3.9.0)
  - owlapi-distribution (version: 3.4.9.2-ansell)
  - pellet-owlapiv3 (version: 2.3.3)
  - json-simple (version 1.1.1)
  - gson (version 2.8.6)
  - lombok (version 1.16.6)
  - commons-lang3 (version 3.0)
  - hibernate-core (version 5.5.8.Final)

- External Jar Files: These files are not available in maven repository and may need to add in the build path manually. These can be found in project/lib/
  - virt_jena3.jar
  - virtjdbc4.jar
- Connection with Virtuoso Triple Store: Three parameters are required to connect to the virtuoso: API base URI, prefix URI and SWRL prefix.
