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
      <AttachCanvas
        v-bind:seat="seats"
        v-bind:employee="employees"
        v-on:saveByAxios="SaveData"
        v-on:deleteByAxios="DeleteData"
      ></AttachCanvas>
      <FloorTabs v-bind:copyFloors="floors"></FloorTabs>
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
import AttachCanvas from "@/components/AttachCanvas.vue";
import FloorTabs from "@/components/FloorTabs.vue";
import EachFloorDataTable from "@/components/EachFloorDataTable.vue";
import EachEmployeeSeatDataTable from "@/components/EachEmployeeSeatDataTable.vue";
const portNum = 6080;
const host = "172.30.1.50";

export default {
  name: "Admin",
  components: {
    AllFloorsDataTable,
    AttachCanvas,
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
      employees: [],
      seats: [],
      floors: [],
    };
  },
  created() {
    this.employees = this.getEmployees();
    this.seats = this.getSeats();
    this.floors = this.getFloors();
  },
  methods: {
    getEmployees() {
      let initEmployeeList = new Array();
      axios
        .get("http://" + host + ":" + portNum + "/api/employee")
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            var newEmployee = {};
            newEmployee.name = response.data[i].employee_name;
            //console.log(newEmployee.name + "???? employee ?? name");
            newEmployee.department = response.data[i].department_name;
            newEmployee.number = response.data[i].extension_number;
            newEmployee.employee_id = response.data[i].employee_id;
            newEmployee.seatIdList = response.data[i].seatList;
            //console.log(newEmployee.seatIdList);
            initEmployeeList.push(newEmployee);
          }
        });
      return initEmployeeList;
    },
    getSeats() {
      let loadSeatList = new Array();
      axios
        .get("http://" + host + ":" + portNum + "/api/seats")
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            let newSeat = {};
            newSeat.seat_id = response.data[i].seat_id;
            newSeat.floor = response.data[i].floor;
            newSeat.x = response.data[i].x;
            newSeat.y = response.data[i].y;
            newSeat.is_group = response.data[i].is_group;
            newSeat.building_id = response.data[i].building_id;
            newSeat.employee_id = response.data[i].employee_id;
            newSeat.width = response.data[i].width;
            newSeat.height = response.data[i].height;
            newSeat.degree = response.data[i].degree;
            newSeat.shape_id = response.data[i].shape_id;

            loadSeatList.push(newSeat);
          }
        });
      return loadSeatList;
    },
    getFloors() {
      let loadFloorList = new Array();
      axios
        .get("http://" + host + ":" + portNum + "/api/floors")
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            let newFloor = {};
            newFloor.floor_id = response.data[i].floor_id;
            newFloor.floor_name = response.data[i].floor_name;
            newFloor.building_id = response.data[i].building_id;
            newFloor.floor_index = response.data[i].floor_index;

            loadFloorList.push(newFloor);
          }
        });
      return loadFloorList;
    },
    SaveData(data, tableName) {
      let Savedata = data;
      let SavetableName = tableName;
      console.log("Savedata is");
      console.log(Savedata);
      console.log("------------");
      console.log("SavetableName is");
      console.log(SavetableName);

      axios
        .post(
          "http://" + host + ":" + portNum + "/api/" + SavetableName,
          JSON.stringify(Savedata),
          {
            headers: { "Content-Type": `application/json` },
          }
        )
        .then((res) => {
          console.log(res.Savedata);
        });
    },
    DeleteData(data, tableName) {
      let deleteData = data;
      let deleteTableName = tableName
      axios
        .delete("http://" + host + ":" + portNum + "/api/" + deleteTableName + "/" + deleteData)
        .then(function (response) {
          // handle success
          console.log(response);
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        });
    },
  },
};
</script>

<style scoped>
.d1 {
  float: left;
  width: 25%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
}

.d2 {
  float: left;
  width: 50%;
  height: 100%;
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
}

#hr {
  cursor: pointer;
}

div {
  display: inline-block;
}
</style>