import Vue from 'vue'
import Vuex from 'vuex'

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

    }
})