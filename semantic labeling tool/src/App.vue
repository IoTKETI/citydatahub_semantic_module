<template>
  <div id="app">
      <Home />
      <div id ="content" class="content">
      <!-- <AE /> -->
      <router-view></router-view>
      </div>
  </div>
</template>
<script>
import Home from './components/Home.vue'
export default {
  name: "App",
  components:{
    Home,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
