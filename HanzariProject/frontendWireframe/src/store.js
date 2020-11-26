import Vue from 'vue'
import Vuex from 'vuex'
import router from './router'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        hhhost: 'http://172.30.1.50:8081',
        token: '',
        authority: ''
    },
    mutations: {
        loginToken: function (state, payload) {
            state.token = payload
        },
        logout: function (state) {
            if (state.token) {
                state.token = ''
                alert('logout')
            }
        },
        loginCheck: function (state) {
            if (state.tokenn) {
                //로그인이 되어있는 상태에서 필요한 로직 구현
            } else {
                router.push({
                    name: 'login'
                }).catch(error => {
                    console.log(error)
                })
            }
        }

    },
    // modules: {
    //     dataStore
    // },
    // plugins: [createPersistedState({
    //     paths: [dataStore],
    // })],
})