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
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-bind:seat="seats"
        v-bind:copyEmployee="employees"
        v-bind:copyImages="images"
        v-on:saveImages="saveImages"
        v-on:saveFloors="saveFloors"
        v-on:saveSeats="saveSeats"
        v-on:deleteFloorWithKey="deleteFloorWtihKey"
        v-on:deleteSeatWithKey="deleteSeatWithKey"
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
const portNum = 8081;
const host = "172.30.1.53"; //yj
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
      images: [],
      currentFloorSeats: [],
      currentFloorName: null,
      currentFloorId: null,
    };
  },
  created() {
    this.employees = this.getEmployees();
    this.floors = this.getFloors();
    this.images = this.getImages();
    eventBus.$on("changeFloor", (floor) => {
      if (floor == null) {
        this.currentFloorName = null;
        this.currentFloorId = null;
      } else {
        this.currentFloorName = floor.floor_name;
        this.currentFloorId = floor.floor_id;
      }
    });

    this.currentFloorSeats = this.getCurrentFloorSeats(this.currentFloorId); //currentFloor's seatList

    console.log(this.getFloorLength() + "층의 개수"); //0
    console.log(this.getEmployeeLength() + "사원 개수"); //0
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
    //floor_id ?��?��?�� name?���? ?��?���? ?��?��?��
    getCurrentFloorSeats(floor_id) {
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
            //if (response.data[i].floor == floor_id) {
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
            newSeat.create = false;
            newSeat.delete = false;
            newSeat.modify = false;

            currentFloorSeatList.push(newSeat);
            //}
          }
        });
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
            newSeat.create = false;
            newSeat.delete = false;
            newSeat.modify = false;

            loadSeatList.push(newSeat);
          }
        });
      return oneFloorSeatList;
    },
    getAllSeats() {
      //all seats // ?��?�� �? ?��?��?�� all seats�? ?��?�� 구현?��?��?��.
      let allDBSeatMap = new Map();
      console.log(
        this.floors.length + "층의 개수?��?��?��. ?��?��?��?��?��?��"
      ); //0
      for (let i = 0; i < this.floors.length; i++) {
        allDBSeatMap.set(
          this.floors[i].floor_name,
          this.getOneFloorSeats(this.floors[i].floor_id)
        );
      }
      //console.log(this.floors.length+"층의 개수?��?��?��. ?��?��?��?��?��?��")
      return allDBSeatMap;
    },
    getFloors() {
      let allFloorList = new Array();
      axios
        .get(
          "http://" + host + ":" + portNum + "/api/buildings/HANCOM01/floors"
        )
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
    getImages() {
      //let allImageList = new Array();
      axios
        //.get("http://" + host + ":" + portNum + "/api/" + "building/HANCOM01/floor/{floorid}/imgurl")
        .get(
          "http://172.30.1.56:9000/hanzari/%ED%95%9C%EA%B8%80%EA%B3%BC%EC%BB%B4%ED%93%A8%ED%84%B0-1%EC%B8%B5.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIOSFODNN7EXAMPLE%2F20201023%2F%2Fs3%2Faws4_request&X-Amz-Date=20201023T021304Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=3762c647cfd02789e889243ef2d333aa0d18abd1894aca75b7edaf3d2848e306"
        )
        .then((response) => {
          const imgurl = response.config.url;
          this.images = imgurl;

          /* for (var i = 0; i < response.data.length; i++) {
            let newImage = {};
            newImage.url = response[i].config.url;
            
            allImageList.push(newImage);
          }*/

          //console.log(this.images);
          //console.log(initImageList);
          //console.log(initImageList.length); //1
        });

      //return allImageList;
      return this.images;
    },
    saveImages(tableName, data) {
      //추후에 api 구조 변경될 것을 생각하여 table, DTO를 넘겨받아 저장하는 것을 같은 함수로 묶지않음.
      let saveData = data;
      let saveTableName = tableName;
      //console.log(saveData)
      //console.log(saveData.key)

      // for (let value of saveData.keys()) {
      //   console.log(value);
      // }

      axios
        .post("http://172.30.1.56:8081" + "/api/" + saveTableName, saveData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(function () {
          console.log("axios SUCCESS!!");
        })
        .catch(function () {
          console.log("axios FAILURE!!");
        });
    },
    saveFloors(tableName, data) {
      //추후에 api 구조 변경될 것을 생각하여 table, DTO를 넘겨받아 저장하는 것을 같은 함수로 묶지않음.
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
    saveSeats(tableName, data) {
      //추후에 api 구조 변경될 것을 생각하여 table, DTO를 넘겨받아 저장하는 것을 같은 함수로 묶지않음.
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
    deleteFloorWtihKey(tableName, key) {
      //추후에 api 구조 변경될 것을 생각하여 key를 받아서 삭제하는 것을 같은 함수로 묶지않음.
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
    deleteSeatWithKey(tableName, key) {
      //추후에 api 구조 변경될 것을 생각하여 key를 받아서 삭제하는 것을 같은 함수로 묶지않음.
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