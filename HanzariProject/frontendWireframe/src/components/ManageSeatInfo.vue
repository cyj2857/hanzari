<template>
  <div>
    <v-toolbar color="black" dark> </v-toolbar>
    <v-card flat v-if="!seatFloorMovementStatus">
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
        <v-col cols="12" sm="3">
          <v-selectmenu
            :data="list"
            v-model="value"
            type="regular"
            align="center"
          >
            <v-btn>edit</v-btn>
            </v-selectmenu>
        </v-col>
      </v-row>
      <v-card-title>Department</v-card-title>
      <v-text-field
        v-model="employeeDepartment"
        label="employeeDepartment"
        solo
        readonly
      >
      </v-text-field
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

      value: "hllo",
      list: [
        { id: 1, name: "김인사", desc: "hii" },
        { id: 2, name: "마효리", desc: "joo" },
        { id: 3, name: "노윤지", desc: "joo" },
        { id: 4, name: "안현호", desc: "joo" },
        { id: 5, name: "최유진", desc: "joo" },
        { id: 6, name: "김동민", desc: "joo" },
      ],
    };
  },
  created() {
    eventBus.$on("seatFloorMovementStatus", (seatFloorMovementStatus) => {
      this.seatFloorMovementStatus = seatFloorMovementStatus;
    });
    eventBus.$on("dblClickedGroup", (dblClickedGroup) => {
      console.log(dblClickedGroup);
      let groupToObject = dblClickedGroup.toObject([
        "employee_id",
        "employee_name",
        "employee_department",
      ]);
      this.employeeName = groupToObject.employee_name;
      this.employeeDepartment = groupToObject.employee_department;
    });
  },
  methods: {
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
      this.employeeName = null;
      this.employeeDepartment = null;
    },
  },
};
</script>