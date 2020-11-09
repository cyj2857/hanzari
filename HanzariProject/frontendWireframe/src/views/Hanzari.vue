<template>
  <div class="hanzari" id="hanzari">
    <Tabs />
    <AttachCanvas />
  </div>
</template>

<script>
import axios from "axios";

import Taps from "@/components/Taps.vue";
import AttachCanvas from "@/components/AttachCanvas.vue";
export default {
  name: "Hanzari",
  components: {
    Taps,
    AttachCanvas,
  },
  data() {
    return {};
  },
};
</script>
