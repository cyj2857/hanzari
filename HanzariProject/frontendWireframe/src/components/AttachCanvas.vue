<template>
  <div>
    <canvas
      ref="canvas"
      class="canvas"
      id="canvas"
      style="text-align: center"
    ></canvas>
    <v-btn @click="clickTestSeat">Test Seat</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  name: "AttachCanvas",
  data() {
    return {};
  },
  methods: {
    clickTestSeat() {
      eventBus.$emit("manageSeatInfocomponentStatus", true);
    },
  },
};
</script>