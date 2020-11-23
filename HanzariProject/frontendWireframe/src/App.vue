<template>
  <!-- <router-view /> -->
  <Hanzari />
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
