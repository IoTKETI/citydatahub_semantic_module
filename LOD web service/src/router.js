import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home'
import Search from './views/Search'
import Select from './views/Select'
import Detail from './views/Detail'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/home',
            name: 'Home',
            component: Home
        },
        {
            path: '/search',
            name: 'Search',
            component: Search
        },
        {
            path: '/select',
            name: 'Select',
            component: Select
        },
        {
            path: '/detail',
            name: 'Detail',
            component: Detail,
            props: true
        },
    ]
})
