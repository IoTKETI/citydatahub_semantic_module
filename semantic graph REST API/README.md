# 1. City Data Hub - semantic graph REST API 설치가이드

## 1.1 개요

스마트시티 데이터 허브의 데이터가 Graph DB에 저장되거나 저장된 후, 사용하는 Graph DB의 종류에 따른 그래프 쿼리를 익히지 않고도 REST 기능을 사용가능하도록 지원하는 API이다.
Eclipse나 IntelliJ와 같은 Java IDE에서 실행할 수 있는 Java Maven 프로젝트다.

## 2 사전 설치

### 2.1 종속성

1. Java 버전: 1.8

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
cd ~/semantic graph REST API
# build maven 
mvn clean install
```

### 2.3 메이블 빌드 후에 필요한 외부 jar 종속성 추가
1. project/lib 내부의 jar 파일 혹은 수동 다운로드를 통해 jar 파일 추가
2. Eclipse에서 프로젝트에 마우스 오른쪽 버튼으로 클릭하고 Build Path>Configure Build Path>Java Build Path로 이동
3. 라이브러리 탭에서 빌드 경로에 현재 추가되어 있는 jar 표시
4. 오른쪽에서 “Add External Jars” 클릭 후 lib 디렉토리를 찾아 안에 있는 jar 파일 선택
5. “Apply and Close” 클릭 

   
