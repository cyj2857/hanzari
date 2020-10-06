<template>
  <div id="app">
    <p>Vue with axios2</p>
    <v-btn v-on:click="getData">getData(View Console)</v-btn>
  </div>
</template>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.2/dist/vue.js"></script>
<script src ="https://unpkg.com/axios/dist/axios.min.js"></script>

<script>
import axios from "axios";
export default {
  name: "AxiosTest",
  methods: {
    getData: function() {
      axios.get("http://172.30.1.50:8080/employee").then(function(response) {
        console.log(response)
      });
    }
  }
};
</script>
