import Vue from 'vue'
import Router from 'vue-router'
import fabric from 'fabric'

import Hanzari from '@/views/Hanzari'
import Login from '@/views/Login'

Vue.use(Router)
Vue.use(fabric)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/Hanzari',
      name: 'Hanzari',
      component: Hanzari
    }
  ]
})
