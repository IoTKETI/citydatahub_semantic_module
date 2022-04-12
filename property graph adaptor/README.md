# 1. City Data Hub - property graph adaptor 설치가이드

## 1.1 개요

스마트시티 데이터 허브의 데이터가 Graph DB에 저장될 수 있도록 변환해주는 adaptor이다. Eclipse나 IntelliJ와 같은 Java IDE에서 실행할 수 있는 Java Maven 프로젝트다.

## 2 사전 설치

### 2.1 종속성

1. Java 버전: 1.8
2. NGSI-LD Broker API와 연결: 데이터 허브의 데이터 코어에 저장되어 있는 데이터에 접근하려면 NGSI-LD Broker API를 통해 조회, 업데이트된 데이터 통지가 가능하다. NGSI-LD Broker API와는 HTTP 통신을 통해 연결이 가능하며, Annotator 동작을 위해서는 반드시 NGSI-LD Broker API가 가동되고 있어야 한다. HTTP 통신을 위해 NGSI-LD Broker API가 가동되는 Server IP와 개방포트가 필요하다.
3. Arango DB와 연결: 본 프로젝트에서는 Adaptor가 생성한 데이터를 저장하기 위해 Arango DB를 사용한다. 

### 2.2  설치
*	NOTE: 본 배포 가이드는 Window에서 Eclipse IDE를 대상으로 작성되었으므로, 다른 환경 및 플랫폼과 다를 수 있음
  
### 2.1 git clone

```bash
# git clone
git clone https://github.com/IoTKETI/citydatahub_semantics.git semantics
```

### 2.2 maven build

```bash
cd ~/semantics
cd ~/property graph adaptor
# build maven 
mvn clean install
```

### 2.3 메이블 빌드 후에 필요한 외부 jar 종속성 추가
1. project/lib 내부의 jar 파일 혹은 수동 다운로드를 통해 jar 파일 추가 (2.1 종속성 참조)
2. Eclipse에서 프로젝트에 마우스 오른쪽 버튼으로 클릭하고 Build Path>Configure Build Path>Java Build Path로 이동
3. 라이브러리 탭에서 빌드 경로에 현재 추가되어 있는 jar 표시
4. 오른쪽에서 “Add External Jars” 클릭 후 lib 디렉토리를 찾아 안에 있는 jar 파일 선택
5. “Apply and Close” 클릭 

   
