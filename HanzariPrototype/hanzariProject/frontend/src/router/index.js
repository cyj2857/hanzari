import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/views/Home'
import HancomWorld from '@/views/HancomWorld'
import YunjiTest from '@/views/YunjiTest'
import HyoriTest from '@/views/HyoriTest'

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
    },
    {
      path: '/yunjiTest',
      name: 'yunjiTest',
      component: YunjiTest
    },
    {
      path: '/hyoriTest',
      name: 'hyoriTest',
      component: HyoriTest
    }
  ]
})
