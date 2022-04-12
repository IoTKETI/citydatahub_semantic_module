<template>
  <div>
    <h2 style="margin-left: 30px; margin-top: 20px">
      Keyword: {{ $store.state.keyword }}
    </h2>
    <div class="data" v-for="gd in getGraphList" :key="gd.graphURI">
      <el-row>
        <el-col :span="4">
          <div class="grid-content bg-purple">
            <br />
            <div class="imgbox-updated">
              <img
                class="img"
                v-bind:src="gd.imageLink"
                v-bind:alt="'Park-' + gd.id"
              />
            </div>
            <br />
          </div>
        </el-col>
        <el-col :span="20">
          <br />
          <div class="name">{{ gd.graphURI }}</div>
          <el-row>
            <router-link :to="{ name: 'Detail', params: { graph_obj: gd.graphName } }"
              ><el-col :span="2" class="btn">LOD</el-col>
            </router-link>
            <el-col :span="2" class="btn">View Map</el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Select",
  components: {},
  data() {
    return {
      keyword: this.$store.state.keyword,
      prefixFormat: "simple",
      user: "user2",
      accessToken:
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWRtaW5TeXN0ZW0iLCJ1c2VySWQiOiJzZW1hbnRpYyIsIm5pY2tuYW1lIjoia2V0aV9zZW1hbnRpYyIsImVtYWlsIjoic2VtYW50aWNAYWRtaW4uY29tIiwicm9sZSI6IlNlbWFudGljX0FkbWluIiwiaWF0IjoxNjI4NTkwNjEyLCJleHAiOjE2Mjg1OTQyMTIsImF1ZCI6IjNlUm9maHJsNndQV2F1WDJ1MEdUIiwiaXNzIjoidXJuOmRhdGFodWI6Y2l0eWh1YjpzZWN1cml0eSJ9.hyuhB6k5gs_ZAW-2p_j4JXRjLPMhsNM3ZqVCFmjRO3CLqj9zGuq99pBI3Qvc6X3kVlemk9xy0MaIRUzPhoK4Q-QANmABT0hfKVlVWaeTpiP6SwWvuctCrI2yaPWSYTIptpBv6ebF_caomI4Z3_OXyUSVAM--nt0eypZUUohSImJKz3sQ7caDiPSut8lrVyPrk8OutZ_Vlbat8jaJ6bh6U8GHghe4efzzb5p7nP6Ew6qsq5FweGzA_5HQNTEkyYvIE6wu5EunMFBaDToNIySY1aKe786lwD_W00EEXOlmOXLMB7ekkz_srnvhia3q517-tQwv1szrbfyy5rBPamAM7Q",
      graphList: [],
      prefixList: [],
      getGraphList: [],
    };
  },
  mounted() {
    this.getKeyword();
  },
  methods: {
    getKeyword() {
      const baseURI = "http://192.168.0.16:8080/semantic/api/v1";
      axios
        .get(baseURI + "/graphs", {
          params: {
            graphType: "ontology,instance",
            keyword: this.keyword,
            prefixFormat: this.prefixFormat,
            user: this.user,
            accessToken: this.accessToken,
          },
        })
        .then((res) => {
          this.prefixList = res.data.prefixList;
          this.graphList = res.data.graphList;
          for (const prefix of this.prefixList) {
            for (let key of Object.keys(prefix)) {

              for (var graph of this.graphList) {

                if (!graph.indexOf(key + ":")) {
                  this.getGraphList.push({
                    graphURI: graph.replace(key + ":", prefix[key]),
                    graphName: graph
                  })
                } else {
                  this.getGraphList.push({
                    graphURI: graph,
                    graphName: key
                  })
                }

              }
            }
          }
        });
    },
  },
  created() {},
};
</script>

<style scoped>
h2 {
  font-size: 1.4em;
  color: #373f42;
  margin: 0 0 0.25em 0;
}

.park {
  background-color: orange;
  margin: 30px 0px 0px 30px;
  text-align: center;
  box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
}

.name {
  margin: 20px 0px 0px 20px;
  font-size: 1.1em;
}

.btn {
  border: solid 1px cornflowerblue;
  margin: 20px 0px 10px 20px;
  text-align: center;
  color: cornflowerblue;
  box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
}

.data {
  background-color: white;
  /* width: 75%; */
  margin-left: 100px;
  margin-top: 40px;
  box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
}

.imgbox-updated {
  height: 115px;
  width: 210px;
  border: solid 1px;
  margin: 0px 35px 0px 35px;
  background-color: #b5bec3;
  box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
}

.img {
  border: 1px solid #ddd;
  border-radius: 4px;
  height: 120px;
  width: 215px;
}

.imgbox {
  border: solid 1px;
  margin: 0px 35px 0px 35px;
  background-color: #b5bec3;
  box-shadow: 2px 2px 2px 1px rgba(128, 128, 128, 0.1);
}

.bg-purple {
  margin-right: 0px;
  border-right: solid 1px #b5bec3;
  text-align: center;
}

.grid-content {
  min-height: 36px;
  padding: 3px;
}
</style>
