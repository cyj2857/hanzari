import Vue from 'vue'
import Router from 'vue-router'
import fabric from 'fabric'

import Admin from '@/views/HyoriTest'
import Manager from '@/views/YujinTest'
import Viewer from '@/views/YunjiTest'

Vue.use(Router)
Vue.use(fabric)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/hyoriTest',
      name: 'Admin',
      component: Admin
    },
    {
      path: '/YujinTest',
      name: 'Manager',
      component: Manager
    },
    {
      path: '/yunjiTest',
      name: 'Viewer',
      component: Viewer
    }
  ]
})
