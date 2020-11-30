<template>
  <router-view />
</template>

<script>
//한글 테스트
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
