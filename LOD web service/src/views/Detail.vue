<template>
  <div>
    <div class="data" style="align: center; margin-right: 100px">
      <el-row>
        <el-col :span="4">
          <div class="grid-content bg-purple">
            <br />
            <div class="imgbox-updated">
              <img
                class="img"
                v-bind:src="graphDetail.imageLink"
                v-bind:alt="'Image'"
              />
            </div>
            <br />
          </div>
        </el-col>
        <el-col :span="20">
          <br />
          <div class="name">{{ graphDetail.parkingLot }}</div>
          <el-row>
            <div class="name" style="color: blue; text-decoration: underline">
              {{ graphDetail }}
            </div>
          </el-row>
        </el-col>
      </el-row>
    </div>
    <div class="detail" style="align: center; margin-right: 100px">
      <el-tabs type="border-card">
        <el-tab-pane label="Table">
          <div style="margin: 30px 30px 30px 30px">
            <b-table-simple hover small caption-top responsive bordered>
              <b-tbody>
                <b-tr>
                  <b-th class="text-center" colspan="1" variant="secondary"
                    >subject</b-th
                  >
                  <b-th class="text-center" colspan="1" variant="secondary"
                    >predicate</b-th
                  >
                  <b-th class="text-center" colspan="1" variant="secondary"
                    >object</b-th
                  >
                </b-tr>
                <b-tr v-for="triple in triples" :key="triple">
                  <b-th class="text-center" colspan="1">{{
                    triple.subject
                  }}</b-th>
                  <b-th class="text-center" colspan="1">{{
                    triple.predicate
                  }}</b-th>
                  <b-th class="text-center" colspan="1">{{
                    triple.object
                  }}</b-th>
                </b-tr>
              </b-tbody>
            </b-table-simple>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Graph" style="text-align: center; height: 786px">
          <d3-network
            :net-nodes="nodes"
            :net-links="links"
            :options="options"
          />
        </el-tab-pane>
        <el-tab-pane label="LOD publish">
          <div style="text-align: center">
            <img
              style="margin: 80px 80px 100px 80px"
              src="./../assets/json.jpg"
              v-on:click="click"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import D3Network from "vue-d3-network";

const baseURI = "http://192.168.0.16:8080/semantic/api/v1";

export default {
  name: "Detail",
  data() {
    return {
      data: [99, 71, 78, 25, 36, 92],
      line: "",
      triples: [],
      tripleList: [],
      prefixList: [],
      nodes: [],
      links: [],
      options: {
        force: 3000,
        nodeSize: 20,
        nodeLabels: true,
        linkWidth: 5,
      },
      graphDetail: this.$route.params.graph_obj,
      graphID: "",
      prefixFormat: "simple",
      user: "user2",
      accessToken:
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWRtaW5TeXN0ZW0iLCJ1c2VySWQiOiJzZW1hbnRpYyIsIm5pY2tuYW1lIjoia2V0aV9zZW1hbnRpYyIsImVtYWlsIjoic2VtYW50aWNAYWRtaW4uY29tIiwicm9sZSI6IlNlbWFudGljX0FkbWluIiwiaWF0IjoxNjI4NTkwNjEyLCJleHAiOjE2Mjg1OTQyMTIsImF1ZCI6IjNlUm9maHJsNndQV2F1WDJ1MEdUIiwiaXNzIjoidXJuOmRhdGFodWI6Y2l0eWh1YjpzZWN1cml0eSJ9.hyuhB6k5gs_ZAW-2p_j4JXRjLPMhsNM3ZqVCFmjRO3CLqj9zGuq99pBI3Qvc6X3kVlemk9xy0MaIRUzPhoK4Q-QANmABT0hfKVlVWaeTpiP6SwWvuctCrI2yaPWSYTIptpBv6ebF_caomI4Z3_OXyUSVAM--nt0eypZUUohSImJKz3sQ7caDiPSut8lrVyPrk8OutZ_Vlbat8jaJ6bh6U8GHghe4efzzb5p7nP6Ew6qsq5FweGzA_5HQNTEkyYvIE6wu5EunMFBaDToNIySY1aKe786lwD_W00EEXOlmOXLMB7ekkz_srnvhia3q517-tQwv1szrbfyy5rBPamAM7Q",
    };
  },
  components: {
    D3Network,
  },
  mounted() {
    this.makeDetail();
  },
  methods: {
    click() {
      console.log("!!?!");
      axios
        .get(baseURI + "/graphs/" + this.graphID, {
          params: {
            user: this.user,
            accessToken: this.accessToken,
            asFile: "true",
          },
          responseType: "blob",
        })
        .then((res) => {
          console.log(res.data);
          const blob = new Blob([res.data], { type: "application/rdf+xml" });
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          link.download = this.graphDetail;
          link.click();
          URL.revokeObjectURL(link.href);
        });
    },
    makeDetail() {
      axios
        .get(baseURI + "/graphs/" + this.graphDetail, {
          params: {
            prefixFormat: this.prefixFormat,
            user: this.user,
            accessToken: this.accessToken,
            asFile: "false",
          },
        })
        .then((res) => {
          this.tripleList = res.data.graphTriples;
          this.prefixList = res.data.prefixList;
          for (const triple of this.tripleList) {
            this.triples.push({
              subject: triple.subject,
              predicate: triple.predicate,
              object: triple.object,
            });
          }

          this.graphID = this.graphDetail;
          this.graphDetail = this.graphDetail.split(":")[1];
          for (const prefix of this.prefixList) {
            for (let prefixKey of Object.keys(prefix)) {
              for (var triple of this.tripleList) {
                for (let tripleKey of Object.keys(triple)) {
                  if (!triple[tripleKey].indexOf(prefixKey + ":")) {
                    triple[tripleKey] = triple[tripleKey].split(":")[1];
                  }
                }
              }
            }
          }
          for (var triple1 of this.tripleList) {
            var sub = triple1.subject.replace("_" + this.graphDetail, "");
            var obj = triple1.object.replace("_" + this.graphDetail, "");
            obj = obj.split("^^")[0];
            triple1.subject = sub;
            triple1.object = obj;
            console.log("sub: " + triple1.subject);
            console.log("pre: " + triple1.predicate);
            console.log("obj: " + triple1.object);
            var subCount = 0;
            var objCount = 0;
            for (var node of this.nodes) {
              if (node.id == triple1.subject) {
                subCount++;
              } else if (node.id == triple1.object) {
                objCount++;
              }
            }
            if (subCount == 0) {
              this.nodes.push({
                id: triple1.subject,
                name: triple1.subject,
              });
            }
            if (objCount == 0) {
              this.nodes.push({
                id: triple1.object,
                name: triple1.object,
              });
            }
          }
          for (var triple2 of this.tripleList) {
            this.links.push({
              sid: triple2.subject,
              tid: triple2.object,
            });
          }
        });
    },
  },
};
</script>


<style scoped>
.node {
  stroke: #fff;
  fill: #ddd;
  stroke-width: 1.5px;
}
.link {
  stroke: #999;
  stroke-opacity: 0.6;
  stroke-width: 1px;
}
marker {
  stroke: #999;
  fill: rgba(124, 240, 10, 0);
}
.node-text {
  font: 11px sans-serif;
  fill: black;
}
.link-text {
  font: 9px sans-serif;
  fill: grey;
}

.svg {
  margin: 25px;
}

.path {
  fill: none;
  stroke: #76bf8a;
  stroke-width: 3px;
}

.data {
  background-color: #d6d6d6;
  /* width: 75%; */
  margin-left: 100px;
  margin-top: 30px;
  box-shadow: 2px 2px 2px 2px rgba(128, 128, 128, 0.1);
}

.detail {
  background-color: white;
  margin-left: 100px;
  margin-top: 30px;
}

.bg-purple {
  margin-right: 0px;
  border-right: solid 1px #b5bec3;
  text-align: center;
}

.imgbox {
  border: solid 1px;
  margin: 0px 35px 0px 35px;
  background-color: #efefef;
}

.name {
  margin: 20px 0px 0px 20px;
  font-size: 1.1em;
}

.park {
  background-color: orange;
  margin: 20px 0px 0px 20px;
  text-align: center;
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
</style>

<style src="vue-d3-network/dist/vue-d3-network.css"></style>
