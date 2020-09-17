<template>
  <v-app id="app">
    <div id="routerLink">
      <v-card>
        <v-tabs v-model="tab" background-color="primary" dark>
          <v-tab to="/">Home</v-tab>
          <v-tab to="/hancomWorld">HancomWorld</v-tab>
          <v-tab to="/yunjiTest">YunjiTestPage</v-tab>
          <v-tab to="/hyoriTest">HyoriTestPage</v-tab>
        </v-tabs>
      </v-card>
      <router-view />
    </div>
  </v-app>
</template>


<script>
export default {
  name: "App"
};
</script>

<style>
* {
  width: 100%;
}
</style>
