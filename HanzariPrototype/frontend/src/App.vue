<template>
  <v-app id="app">
    <div id="routerLink">
      <v-card>
        <v-tabs background-color="primary" dark>
          <v-tab to="/hyoriTest">Admin</v-tab>
          <v-tab to="/yunjiTest">Manager</v-tab>
          <v-tab to="/YujinTest">Viewer</v-tab>
        </v-tabs>
      </v-card>
      <router-view />
    </div>
  </v-app>
</template>

<script>
import { eventBus } from "./main.js";
import axios from "axios";
const portNum = 8080;
const host = "172.30.1.50";

export default {
  name: "App"
};
</script>

<style>
*:not(button) {
  width: 100%;
}
</style>
