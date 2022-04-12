# 1. City Data Hub - Semantic Module 설치가이드

## 1.1 개요

시맨틱 모듈은 데이터 허브 플랫폼의 시맨틱 데이터 구축 및 활용을 지원하는 모듈이다. Eclipse나 IntelliJ와 같은 Java IDE에서 실행할 수 있는 Java Maven 프로젝트다.

## 2 사전 설치

### 2.1 종속성

1. Java 버전: 1.8
2. Maven Dependencies: “pom.xml”의 <dependencies>에 반드시 포함되어야 하는 종속성 목록
  -	apache-jena-libs (version: 3.15.0)
  -	org.semanticweb.hermit (version: 1.3.8.4) * NOTE: Semantic Validator와 함께 구동 시 필요
3. 외부 Jar Files: 본 파일은 maven repository에서 사용될 수 없으며 빌드 경로에 수동으로 추가해주어야 한다. 본 파일은 project/lib/에서 찾거나 다음 링크에서 다운로드 가능하다. (http://vos.openlinksw.com/owiki/wiki/VOS/VOSDownload#Jena%20Provider)
  -	virt_jena3.jar
  -	virtjdbc4.jar
4. NGSI-LD Broker API와 연결: 데이터 허브의 데이터 코어에 저장되어 있는 데이터에 접근하려면 NGSI-LD Broker API를 통해 조회, 업데이트된 데이터 통지가 가능하다. NGSI-LD Broker API와는 HTTP 통신을 통해 연결이 가능하며, Annotator 동작을 위해서는 반드시 NGSI-LD Broker API가 가동되고 있어야 한다. HTTP 통신을 위해 NGSI-LD Broker API가 가동되는 Server IP와 개방포트가 필요하다.
5. Virtuoso Triple Store와 연결: 본 프로젝트에서는 Annotator가 생성한 triple data를 저장하기 위해 Virtuoso Triple Store를 사용한다. Virtuoso와 연결을 위해서는 설치 및 가동중인 Virtuoso Triple Store의 호스트 URL, 사용자 이름 및 패스워드가 필요하다. 

### 2.2 설정
  
1. NGSI-LD Broker API와 연결을 위한 NGSI-LD Broker API 가동 Server IP 및 개방포트 설정은 project/src/main/java/com/semantic/annotator/controller/HttpController.java에서 한다.
Property 명	설정값 sample	설명
apiUrl	http://localhost:8080	NGSI-LD Broker API IP, Port
2. Virtuoso Triple Store와 연결을 위한 호스트 URL, 사용자 이름 및 패스워드 설정은 project/src/main/java/com/semantic/annotator/annotation/Annotator.java에서 한다.
Property 명	설정값 sample	설명
dbUrl	http://localhost:8080	호스트 URL
dbUser	dba	기본 사용자 이름
dbPass	dba	기본 사용자 패스워드

## 3 설치
*	NOTE: 본 배포 가이드는 Window에서 Eclipse IDE를 대상으로 작성되었으므로, 다른 환경 및 플랫폼과 다를 수 있음
  
### 3.1 git clone

```bash
# git clone
git clone https://github.com/IoTKETI/citydatahub_semantics.git semantics
```

### 3.2 maven build

```bash
cd ~/semantics
cd ~/semantic annotator
# build maven 
mvn clean install
```

### 3.3 메이블 빌드 후에 필요한 외부 jar 종속성 추가
1. project/lib 내부의 jar 파일 혹은 수동 다운로드를 통해 jar 파일 추가 (2.1 종속성 참조)
2. Eclipse에서 프로젝트에 마우스 오른쪽 버튼으로 클릭하고 Build Path>Configure Build Path>Java Build Path로 이동
3. 라이브러리 탭에서 빌드 경로에 현재 추가되어 있는 jar 표시
4. 오른쪽에서 “Add External Jars” 클릭 후 lib 디렉토리를 찾아 안에 있는 jar 파일 선택
5. “Apply and Close” 클릭 

## 4. 예제 실행

1. 프로젝트의 AnnotatorApplication.java파일을 오른쪽 클릭을 통해 Run As>Java Application으로 Semantic Annotator 사용 및 실행이 가능하다.
2. HttpController를 통해 두가지 기능을 수행한다.
  - createSubscription(): 새로 생성 혹은 업데이트되는 entity가 생기면 notification을 받을 수 있도록 모든 entity 타입에 대해 subscription을 생성한다.
  -	getEntities(): 데이터 코어에 저장되어 있는 모든 entity와 새로 생성 혹은 업데이트되는 entity를 모두 조회하여 Semantic Annotation을 수행한다.
   
