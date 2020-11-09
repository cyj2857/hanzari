<template>
  <v-app id="app">
    <Hanzari />
  </v-app>
</template>

<script>
import { eventBus } from "./main.js";
import axios from "axios";
import Hanzari from "@/views/Hanzari.vue";

export default {
  name: "App",
  components: {
    Hanzari,
  },
};
</script>
