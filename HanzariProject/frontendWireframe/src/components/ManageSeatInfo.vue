<template>
  <v-card flat color="transparent">
    <v-btn style="float: right" @click="changeBackPage">X</v-btn>
    <v-card-title>Name</v-card-title>
    <v-text-field
      v-model="employeeName"
      label="employeeName"
      solo
      readonly
    ></v-text-field
    ><v-card-title>Department</v-card-title>
    <v-text-field
      v-model="employeeDepartment"
      label="employeeDepartment"
      solo
      readonly
    ></v-text-field
    ><v-card-title>Floor</v-card-title>
    <v-text-field
      v-model="employeeFloor"
      label="employeeFloor"
      solo
      readonly
    ></v-text-field>
    <v-btn>Change To Vacant</v-btn>
  </v-card>
</template>

<script>
import { eventBus } from "../main";
export default {
  name: "EmployeeInfo",
  data() {
    return {
      employeeName: null,
      employeeDepartment: null,
      employeeFloor: null,
    };
  },
  methods: {
    changeBackPage() {
      eventBus.$emit("manageSeatInfocomponentStatus", false);
    },
  },
};
</script>