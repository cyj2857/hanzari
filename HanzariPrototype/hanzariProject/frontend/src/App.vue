<template>
  <v-app id="app">
    <div id="routerLink">
      <router-link to="/">Home</router-link>
      <router-link to="/hancomWorld">HancomWorld</router-link>
      <router-link to="/yunjiTest">YunjiTestPage</router-link>
      <router-link to="/hyoriTest">HyoriTestPage</router-link>
    </div>
    <router-view/>
    <v-btn color="success">Success</v-btn>
    <v-btn color="error">Error</v-btn>
    <v-btn color="warning">Warning</v-btn>
    <v-btn color="info">Info</v-btn>
  </v-app>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
width:100%;
}
</style>