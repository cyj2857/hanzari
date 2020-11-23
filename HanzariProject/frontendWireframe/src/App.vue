<template>
    <Login />
</template>

<script>
import { eventBus } from "./main.js";
import axios from "axios";
import Hanzari from "@/views/Hanzari.vue";
import Login from "@/views/Login.vue";

export default {
  name: "App",
  components: {
    Hanzari,
    Login,
  },
};
</script>
