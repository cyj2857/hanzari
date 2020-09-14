<template>
  <div id="app">
    <div id="routerLink">
      <router-link to="/">Home</router-link>
      <router-link to="/hancomWorld">HancomWorld</router-link>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>