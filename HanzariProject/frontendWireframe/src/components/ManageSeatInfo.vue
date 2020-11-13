<template>
  <div>
    <v-card flat>
      <v-card-title>SeatName</v-card-title>
      <v-row
        ><v-col cols="12" sm="9"
          ><v-text-field
            v-model="seatName"
            label="seatName을 입력하세요."
            solo
          ></v-text-field
        ></v-col>
        <v-col cols="12" sm="3">
          <v-btn @click="inputSeatName">Enter</v-btn></v-col
        >
      </v-row>
      <v-btn @click="changeToVacant">Change To Vacant</v-btn>
    </v-card>
  </div>
</template>

<script>
import { eventBus } from "../main";
export default {
  name: "EmployeeInfo",
  data() {
    return {
      seatName: null,

      list: [
        { id: 1, name: "One" },
        { id: 2, name: "Two" },
        { id: 3, name: "Three" },
        { id: 4, name: "Four" },
        { id: 5, name: "Five" },
      ],
    };
  },

  methods: {
    inputSeatName() {
      if (this.seatName) {
        eventBus.$emit("inputSeatName", this.seatName);
      }
    },
    setSeatFloorMovement() {
      this.seatFloorMovementStatus = true;
    },
    changeToVacant() {
      eventBus.$emit("changeToVacant", true);
    },
  },
};
</script>