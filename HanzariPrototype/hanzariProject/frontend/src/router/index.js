import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/views/Home'
import HancomWorld from '@/views/HancomWorld'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/hancomWorld',
      name: 'hancomWorld',
      component: HancomWorld
    }
  ]
})
