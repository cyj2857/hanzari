import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify.js';

Vue.config.productionTip = false

//프로그램의 시작점 Vue 인스턴스를 생선한다.
new Vue({
  vuetify,
  render: h => h(App),
}).$mount('#app')
