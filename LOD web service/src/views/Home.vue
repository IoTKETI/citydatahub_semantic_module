<template>
    <div >
        <section class="welcome" style="margin-top: 30px; margin-left: 30px; margin-right: 30px" >
            <h2>Welcome to LOD Service</h2>
            <h3>This LOD Service provide by Data Hub Project</h3>
                
            <p>
                데이터 허브 프로젝트는 다양한 도시 주체들이 참여하여 지속적인 서비스 창출과 도시 운영의 스마트화가 가능하도록,
                도시 구성원, 환경 및 ICT 인프라에서 발생하는 도시 데이터의 통합적 관리, 운영 및 스마트 시티 서비스 인프라를
                제공하는 지속가능한 스마트시티 데이터 허브 개발을 위해 시작되었다.
            </p>
            <p>
                LOD는 데이터들을 상호관계에 따라 연결하는 Linked Data 와 누구나 사용할 수 있도록 공공 데이터로 개방되는 Open Data가 합쳐진 개념이다.
                LOD는 데이터들끼리 다양한 관계에 의해 연결되어 있는 형태를 가지며, 웹 상에서 URI를 통해 누구나 링크된 데이터를 재사용할 수 있도록 Data Set을 발행해준다.
                RDF, OWL과 같은 관계성 기반 언어를 사용하여 기계의 가독성을 높여주며, 유일 식별체계인 URI를 이용하기 때문에 데이터 중복을 감소시킨다.
            </p>
            <p>
                본 LOD Service는 스마트 시티의 다양한 산업에서 발생하는 데이터들을 서로 연결하여 의미있는 데이터를 발행한다.
                도시자원의 관리와 보존, 서비스를 위해 Data 중심의 의미적 연결성을 확보하고,
                사용자가 데이터 허브의 산업용 공공 데이터를 활용할 수 있도록 Data Set 배포 기능을 제공한다.
                또한 시맨틱에 지식이 없는 사용자도 시맨틱 데이터를 검색하고 연관된 데이터에 접근할 수 있도록 서비스하며,
                데이터들의 관계를 쉽게 파악할 수 있도록 가시화하여 제공한다.
            </p>
        </section>
    </div>
</template>

<script>

export default {
  name: 'Home',
  components: {
    
  }

}
</script>

<style scoped>

.welcome {
    background-color: #fff;
    padding: 40px 30px 45px 30px;
    box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
    margin: 0 0 25px 0;
    display: block;
    /* margin-bottom: 0 !important; */
    border: 0;
    font-size: 100%;
    font: inherit;
    /* vertical-align: baseline; */
}

h2 {
    font-size: 1.4em;
    color: #373f42;
    margin: 0 0 0.25em 0;
}

h3 {
    font-size: 1.1em;
    color: #96a9b5;
    margin: 0 0 1.25em 0;
}

p {
    margin: 0 0 2em 0;

}

</style>