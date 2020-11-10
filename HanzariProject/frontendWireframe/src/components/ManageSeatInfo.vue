<template>
  <div>
    <v-card flat color="transparent" v-if="!seatFloorMovementStatus">
      <v-btn style="float: right" @click="changeBackPage">X</v-btn>
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
      <v-card-title>Name</v-card-title>
      <v-row
        ><v-col cols="12" sm="9"
          ><v-text-field
            v-model="employeeName"
            label="employeeName"
            solo
            readonly
          ></v-text-field
        ></v-col>
        <v-col cols="12" sm="3"> <v-btn>edit</v-btn></v-col>
      </v-row>
      <v-card-title>Department</v-card-title>
      <v-text-field
        v-model="employeeDepartment"
        label="employeeDepartment"
        solo
        readonly
      ></v-text-field
      ><v-card-title>Floor</v-card-title>
      <v-row>
        <v-col cols="12" sm="9"
          ><v-text-field
            v-model="employeeFloor"
            label="employeeFloor"
            solo
            readonly
          ></v-text-field
        ></v-col>
        <v-col cols="12" sm="3">
          <v-btn @click="setSeatFloorMovement">edit</v-btn></v-col
        >
      </v-row>
      <v-btn @click="changeToVacant">Change To Vacant</v-btn>
    </v-card>
    <SeatFloorMovement v-if="seatFloorMovementStatus" />
  </div>
</template>

<script>
import SeatFloorMovement from "@/components/SeatFloorMovement.vue";
import { eventBus } from "../main";
export default {
  name: "EmployeeInfo",
  components: { SeatFloorMovement },
  data() {
    return {
      seatName: null,
      employeeName: null,
      employeeDepartment: null,
      employeeFloor: null,
      makingOtherEmployeeStatus: false,
      seatFloorMovementStatus: false,
    };
  },
  created() {
    eventBus.$on("seatFloorMovementStatus", (seatFloorMovementStatus) => {
      this.seatFloorMovementStatus = seatFloorMovementStatus;
    });
  },
  methods: {
    changeBackPage() {
      eventBus.$emit("manageSeatInfocomponentStatus", false);
    },
    inputSeatName() {
      if (this.seatName) {
        eventBus.$emit("inputSeatName", this.seatName);
      }
    },
    mappingOtherEmployee() {
      this.makingOtherEmployeeStatus = true;
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