<template>
  <div class="hanzari" id="hanzari">
    <div class="d1">
      <Tabs />
    </div>
    <div class="d2">
      <AttachCanvas />
    </div>
  </div>
</template>

<script>
import axios from "axios";

import Tabs from "@/components/Tabs.vue";
import AttachCanvas from "@/components/AttachCanvas.vue";
export default {
  name: "Hanzari",
  components: {
    Tabs,
    AttachCanvas,
  },
  data() {
    return {};
  },
};
</script>

<style scoped>
.d1 {
  float: left;
  height: 100%;
}
.d2 {
  width: 70%;
  height: 100%;
}
</style>
