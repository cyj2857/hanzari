<template>
  <div id="app">
    <p>Vue with axios sample</p>
    <v-btn v-on:click="getData">getData(View Console)</v-btn>
  </div>
</template>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.2/dist/vue.js"></script>
<script src ="https://unpkg.com/axios/dist/axios.min.js"></script>

<script>
import axios from 'axios'
export default {
  name: "AxiosTest",
  methods: {
    getData: function() {
      axios.get(
        //"https://raw.githubusercontent.com/joshua1988/doit-vuejs/mas-ter/data/demo.json"
        "https://reqres.in/api/users?page=2"
      ).then(function(response) {
        console.log(response);
      });
    }
  }
};
</script>
