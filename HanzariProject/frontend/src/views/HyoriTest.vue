<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <div class="search" id="search">
        <AllFloorsDataTable
          v-bind:copyEmployee="employees"
        ></AllFloorsDataTable>
        <EachEmployeeSeatDataTable></EachEmployeeSeatDataTable>
      </div>
    </div>

    <div class="d3" id="hr"></div>

    <div class="d2" id="d2">
      <AttachCanvas
        v-bind:seat="seats"
        v-bind:copyEmployee="employees"
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-on:saveByAxios="saveData"
        v-on:deleteFloorByAxiosWithKey="deleteFloorByKey"
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
const building = "HANCOM01";
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
      employees: [],
      floors: [],
      seats: [],
      currentFloorSeats: [],
      currentFloor: null,
    };
  },
  created() {
    this.employees = this.getEmployees();
    this.floors = this.getFloors();
    //this.seats = this.getAllSeats(); //Map
    eventBus.$on("changeFloor", (floor) => {
      this.currentFloor = floor;
      console.log(this.currentFloor + "여기가 HyoriTest로 넘어온 현재층");
    });

    this.currentFloorSeats = this.getCurrentFloorSeats(this.currentFloor); //currentFloor's seatList

    console.log(this.getFloorLength() + "층의 개수입니다."); //0
    console.log(this.getEmployeeLength() + "사원의 개수입니다."); //0
  },
  mounted() {
    //changeFloor될때 넘어오는 floor_id를 넣어야함.
    // console.log(
    //   this.floors[this.floors.length - 1].floor_id +
    //     "디비로부터 가지고온 층들의 맨 마지막 층의 아이디입니다."
    // );
    //console.log(this.floors.length+"층의 개수입니다."); //0
    // this.currentFloorSeats = this.getCurrentFloorSeats(
    //   this.floors[this.floors.length - 1].floor_id
    // )
    //console.log(this.currentFloorSeats.length+"디비로부터 가지고온 현재층의 자리리스트 길이입니다.");
  },
  beforeUpdate() {
    console.log(this.getFloorLength() + "층의 개수입니다."); //2
    //this.currentFloor = this.floors[this.getFloorLength() - 1];
    //console.log(this.currentFloor.floor_name+"맨 마지막 층의 아이디입니다.");
  },
  methods: {
    getFloorLength() {
      return this.floors.length;
    },
    getEmployeeLength() {
      return this.employees.length;
    },
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
    //floor_id 현재는 name으로 인자가 넘어옴
    getCurrentFloorSeats(floor) {
      let currentFloorSeatList = new Array();
      axios
        .get(
          // "http://" +
          //   host +
          //   ":" +
          //   portNum +
          //   "/api/floors/" +
          //   floor_id +
          //   "/seats"
          "http://" + host + ":" + portNum + "/api/seats"
        )
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            //console.log(reseponse.data[i].floor);
            //if (response.data[i].floor == floor) {
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

              currentFloorSeatList.push(newSeat);
            //}
          }
        });
      //console.log("넘어온 현재층에 대한 자리리스트 개수입니다. -> "+currentFloorSeatList.length);
      return currentFloorSeatList;
    },
    getOneFloorSeats(floor_id) {
      let oneFloorSeatList = new Array();
      axios
        .get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/floors/" +
            floor_id +
            "/seats"
        )
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
            newSeat.create = response.data[i].create;
            newSeat.delete = response.data[i].delete;
            newSeat.modify = response.data[i].modify;

            loadSeatList.push(newSeat);
          }
        });
      return oneFloorSeatList;
    },
    getAllSeats() {
      //all seats // 현재 층 제외한 all seats로 다시 구현해야함.
      let allDBSeatMap = new Map();
      console.log(this.floors.length + "층의 개수입니다. 함수안에서요"); //0
      for (let i = 0; i < this.floors.length; i++) {
        allDBSeatMap.set(
          this.floors[i].floor_name,
          this.getOneFloorSeats(this.floors[i].floor_id)
        );
      }
      //console.log(this.floors.length+"층의 개수입니다. 함수안에서요")
      return allDBSeatMap;
    },
    getFloors() {
      let allFloorList = new Array();
      axios
        .get("http://" + host + ":" + portNum + "/api/floors")
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            let newFloor = {};
            newFloor.floor_id = response.data[i].floor_id;
            newFloor.floor_name = response.data[i].floor_name;
            newFloor.building_id = response.data[i].building_id;
            newFloor.floor_order = response.data[i].floor_order;
            newFloor.create = false;
            newFloor.modify = false;
            newFloor.delete = false;

            allFloorList.push(newFloor);
          }
        });
      return allFloorList;
    },
    saveData(tableName, data) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);

      axios
        .post(
          "http://" + host + ":" + portNum + "/api/" + saveTableName,
          JSON.stringify(saveData),
          {
            headers: { "Content-Type": `application/json` },
          }
        )
        .then((res) => {
          console.log(res.saveData);
        });
    },
    deleteFloorByKey(tableName, key) {
      let deleteTableName = tableName;
      let deleteKey = key;
      axios
        .delete(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/" +
            deleteTableName +
            "/" +
            deleteKey
        )
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