// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import vSelectMenu from 'v-selectmenu'

import axios from 'axios' // we can use this.$axios without using 'import' in vue instance
import { store } from './store'

export const eventBus = new Vue()

Vue.prototype.$axios = axios

Vue.use(Vuetify, {
  iconfont: 'mdi' // 'md' || 'mdi' || 'fa' || 'fa4'
})
Vue.use(vSelectMenu)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
  vuetify: new Vuetify(),
  //beforeCreate() { this.$store.commit('loginCheck') }
})
