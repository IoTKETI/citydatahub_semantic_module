<template>
  <div class="cont">
    <h2 style=" margin-left: 30px; margin-top: 20px ">Data Hub LOD Searching Service</h2>

    <div style="margin-top: 180px;" class="cent">
      <h3>Search by Keywork</h3>
      <div style="margin-top: 30px; align:center; margin-bottom: 200px">
        <el-input
          style="width:40%"
          placeholder="Please input"
          v-model=$store.state.keyword
          class="input-with-select"
        >
          <el-button slot="append" id="getKeyword" style="position:relative"  icon="el-icon-search">
            <router-link to="./select" style="position:absolute; left:5px; opacity:0">gogogo</router-link>
          </el-button>
        </el-input>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      keyword: ""
    };
  },
  methods: {
    // getKeyword() {
    //   this.$store.state.keyword = this.keyword;
    // }
  }

};
</script>

<style scoped>
.cont {
  width: 100%;
}

h2 {
  font-size: 1.4em;
  color: #373f42;
  margin: 0 0 0.25em 0;
}

h3 {
  color: #373f42;
  font-size: 1.1em;
  /* color: #96a9b5; */
}

.cent {
  width: 100%;
  align-self: center;
  text-align: center;
}
</style>