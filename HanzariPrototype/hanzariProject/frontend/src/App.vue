<template>
  <div id="app">
    <div id="routerLink">
      <router-link to="/">Home</router-link>
      <router-link to="/hancomWorld">HancomWorld</router-link>
      <router-link to="/yunjiTest">YunjiTestPage</router-link>
      <router-link to="/hyoriTest">HyoriTestPage</router-link>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
width:100%;
padding: 0.5%;
}
</style>