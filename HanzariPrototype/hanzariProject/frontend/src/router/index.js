import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import HancomWorld from '@/components/HancomWorld'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/hancomWorld',
      name: 'hancomWorld',
      component: HancomWorld
    }
  ]
})
