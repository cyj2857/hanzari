import Vue from 'vue'
import Router from 'vue-router'
import fabric from 'fabric'

import Home from '@/views/Home'
import YunjiTest from '@/views/YunjiTest'
import HyoriTest from '@/views/HyoriTest'
import YujinTest from '@/views/YujinTest'
import DongminTest from '@/views/DongminTest'

Vue.use(Router)
Vue.use(fabric)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
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
    },
    {
      path: '/YujinTest',
      name: 'YujinTest',
      component: YujinTest
    },
    {
      path: '/DongminTest',
      name: 'dongminTest',
      component: DongminTest
    }
  ]
})
