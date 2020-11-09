<template>
  <div>
    <v-card flat color="transparent">
      <v-btn style="float: right" @click="changeBackPage">X</v-btn></v-card
    >
  </div>
</template>

<script>
import { eventBus } from "../main";
export default {
  name: "SeatFloorMovement",
  data() {
    return {};
  },
  methods: {
    changeBackPage() {
      eventBus.$emit("seatFloorMovementStatus", false);
    },
  },
};
</script>