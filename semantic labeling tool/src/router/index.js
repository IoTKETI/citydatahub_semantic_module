import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'
import label from '../views/labeling.vue'
import about from '../views/About.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: label
  },
  {
    path: '/label',
    name: 'label',
    component: label
  },
  {
    path: '/about',
    name: 'about',
    component: about
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: about
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
