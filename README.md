# citydatahub_semantics

# 1. 시맨틱 모듈 개요

시맨틱 모듈은 데이터 허브 플랫폼의 시맨틱 데이터 구축 및 활용을 지원하는 모듈입니다. 데이터에 의미를 부여하는 시맨틱 개념을 기반으로 데이터 허브에 적재되어 있는 데이터를 시맨틱 데이터로 변환하고 사용자가 시맨틱 데이터 사용 및 활용이 가능하도록 API 지원 및 서비스를 제공합니다. 본 모듈에서는 스마트시티 공통 온톨로지 및 도메인 온톨로지를 개발하여 이를 기반으로 시맨틱 데이터(Triple Data)를 구축하고, 여러 시맨틱 기술을 활용하여 Linked Open Data Service를 제공하는데 목적이 있습니다.

<img width="995" alt="1 System_Architecture" src="https://user-images.githubusercontent.com/45223057/162766623-24c74ac5-497e-4a5a-b61d-57a2188d2f0c.png">

- Annotation Template Creation Interface (ATCI): 시맨틱 모듈이 기존의 hand-craft 방식의 시맨틱 어노테이션 기술이 아닌 템플릿 기반의 반자동 어노테이션 기술을 적용하기 위해 스마트시티 온톨로지를 기반으로 템플릿을 작성하는 인터페이스입니다.
- Annotator: 데이터 코어 모듈을 통해 수집하는 스마트시티 데이터를 시맨틱 데이터로 변환합니다.
- Validator: Annotator가 생성한 시맨틱 데이터가 스마트시티 온톨로지의 규칙에 따라 잘 작성되었는지 검증합니다.
- Triple Store: 시맨틱 데이터 전용 데이터베이스로, 본 모듈에서는 Virtuoso DB를 사용합니다.
- Semantic API: 높은 이해도를 필요로 하는 시맨틱 데이터 전용 쿼리인 SPARQL을 사용하지 않아도 시맨틱 데이터에 대한 REST 기능을 제공하는 API입니다.
- Property Graph Adaptor: 데이터 코어 모듈에 적재되어 있는 스마트시티 데이터를 Graph DB에 저장하여 그래프 순회 등의 기능을 할 수 있도록 변환하는 Adaptor입니다.
- Graph API: 모듈이 사용하는 Graph DB의 종류에 따라 그래프 쿼리를 익혀야 하는 번거로움을 해소하기 위해 사용하는 Graph DB와 그래프 쿼리의 종류와 관계없이 데이터에 대한 REST 기능 및 그래프 순회 기능을 제공하는 API입니다.
- Semantic Knowledge Extractor: 데이터 코어 모듈에 적재되어 있는 스마트시티 데이터를 기반으로 새로운 시맨틱 지식을 뽑아내는 기능을 합니다.

## 1.1 주요 특징

1. 데이터에 의미정보를 부여하여 컴퓨터가 데이터의 의미를 이해하고 조작할 수 있게 됩니다.
2. 의미 메타데이터 작성을 통해 문서에 포함된 의미들을 명확히 정의함으로써 데이터 통합으로 인한 데이터 중복 및 의미 충돌을 방지할 수 있습니다.
3. 반자동 어노테이터 기술을 연구하여 인간의 개입을 최소화하면서 시맨틱 데이터를 구축합니다.
4. 시맨틱 추론 규칙을 통하여 기계학습을 위한 데이터 라벨링 작업을 의미론적으로 수행합니다.
5. 시맨틱에 대한 이해가 없더라도 시맨틱 데이터를 활용할 수 있도록 REST API를 제공합니다.



## 1.2 기술 사양

1. Apache JENA API (v3.16.0), Virtuoso (v7.2.0)
2. W3C RDF, OWL 2
3. Arango DB (v3.7), ArangoJS (v6.14.0), AQL



## 1.3 기존 시스템과 차이점

![2 Key_Point](https://user-images.githubusercontent.com/45223057/162761539-170b5a8a-e8e1-4990-9b49-a04f8189be63.png)
