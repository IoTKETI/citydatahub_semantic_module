import Vue from 'vue'
import App from './App.vue'
import 'element-ui/lib/theme-chalk/index.css';
import ElementUI from 'element-ui';
import router from './router'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import * as d3 from 'd3'
import { store } from './components/store.js';
import axios from 'axios'

Vue.prototype.$http = axios

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(BootstrapVue)
Vue.use(d3)

new Vue({
  router,
  render: h => h(App),
  store: store,
}).$mount('#app')
