<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <div class="search" id="search">
        <AllFloorsDataTable v-bind:employee="employees"></AllFloorsDataTable>
        <EachEmployeeSeatDataTable></EachEmployeeSeatDataTable>
      </div>
    </div>

    <div class="d3" id="hr"></div>

    <div class="d2" id="d2">
      <FloorTabs></FloorTabs>
    </div>

    <div class="d3" id="hr"></div>
    <div class="d4" id="d4">
      <div class="listview" id="listview">
        <EachFloorDataTable></EachFloorDataTable>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { eventBus } from "../main.js";
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";
import FloorTabs from "@/components/FloorTabs.vue";
import EachFloorDataTable from "@/components/EachFloorDataTable.vue";
import EachEmployeeSeatDataTable from "@/components/EachEmployeeSeatDataTable.vue";
const portNum = 8080;
const host = "172.30.1.50";

export default {
  name: "HyoriTest",
  components: {
    AllFloorsDataTable,
    FloorTabs,
    EachFloorDataTable,
    EachEmployeeSeatDataTable,
  },
  data() {
    return {
      msg: "This is hyori page.",
      floorMsg: "Choose Floor",
      searchEmployeeMsg: "Search Employee",
      changeText: "Sample Text",
      selected: "",
      employees: []
    };
  },
  created() {
    this.employees = this.getEmployees();
  },
  methods: {
    updateText() {
      this.changeText = "Click Event Test";
    },
    getEmployees(){
      let initEmployeeList = new Array();
      
      axios.get("http://"+host+":"+portNum+"/employee").then(function(response) { 
        for (var i = 0; i < response.data.length; i++) {
          var newEmployee = {};
          newEmployee.name = response.data[i].employee_name;
          console.log(newEmployee.name + "???? employee ?? name");
          newEmployee.department = response.data[i].department_name;
          newEmployee.number = response.data[i].extension_number;
          newEmployee.employee_id = response.data[i].employee_id;
          newEmployee.seatIdList = response.data[i].seatList;
          console.log(newEmployee.seatIdList);
          initEmployeeList.push(newEmployee);
        }
        console.log(initEmployeeList.length+"employee ???? ?");
        
      });
      //eventBus.$emit("sendAxios", initEmployeeList);
      return initEmployeeList;
    }
  }
};
</script>

<style scoped>
.d1 {
  float: left;
  width: 25%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
  overflow-y: scroll;
}

.d2 {
  float: left;
  width: 50%;
  height: 100%;
  overflow-y: scroll;
  text-align: center;
}

.d3 {
  float: left;
  width: 0.3%;
  height: 100%;
  background-color: #888888;
}

.d4 {
  float: left;
  width: 25%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
  overflow-y: scroll;
}

#hr {
  cursor: pointer;
}

div {
  display: inline-block;
}
</style>