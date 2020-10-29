<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <div class="search" id="search">
        <AllFloorsDataTable
          v-if="employees"
          :copyEmployee="employees"
        ></AllFloorsDataTable>
        <EachEmployeeSeatDataTable></EachEmployeeSeatDataTable>
      </div>
    </div>

    <div class="d3" id="hr"></div>

    <div class="d2" id="d2">
      <AttachCanvas
        v-bind:copyEmployee="employees"
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-bind:currentFloorImageList="currentFloorImage"
        v-on:loadOtherFloorSeats="getOtherFloorSeats"
        v-on:loadOtherFloorImage="getOtherFloorImage"
        v-on:saveImages="saveImages"
        v-on:saveFloors="saveFloors"
        v-on:saveSeats="saveSeats"
        v-on:deleteFloorWithKey="deleteFloorWtihKey"
        v-on:deleteSeatWithKey="deleteSeatWithKey"
      ></AttachCanvas>
      <FloorTabs v-if="floors" v-bind:copyFloors="floors"></FloorTabs>
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
const building_id = "HANCOM01";
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
      employees: null,
      floors: null,

      currentFloorImage: null,
      currentFloorSeats: null,

      floorIdList: [],
      currentFloor: null,
      currentFloorId: null,
    };
  },
  async created() {
    // 사원 load
    this.employees = await this.getEmployees();
    // 층 load
    this.floors = await this.getFloors();
    // 이미지 load
    this.currentFloorImage = await this.getCurrentFloorImage();
    // 자리 load
    this.currentFloorSeats = await this.getCurrentFloorSeats();
  },
  methods: {
    async getEmployees() {
      let initEmployeeList = new Array();
      try {
        let response = await axios.get(
          "http://" + host + ":" + portNum + "/api/employee"
        );
        for (var i = 0; i < response.data.length; i++) {
          var newEmployee = {};
          newEmployee.name = response.data[i].employee_name;
          newEmployee.department = response.data[i].department_name;
          newEmployee.number = response.data[i].extension_number;
          newEmployee.employee_id = response.data[i].employee_id;
          newEmployee.seatIdList = response.data[i].seatList;
          initEmployeeList.push(newEmployee);
        }
      } catch (error) {
        console.log(error);
      }

      return initEmployeeList;
    },
    async getFloors() {
      let allFloorList = new Array();
      try {
        let response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors"
        );
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

        allFloorList.sort(function (a, b) {
          return a.floor_order < b.floor_order
            ? -1
            : a.floor_order > b.floor_order
            ? 1
            : 0;
        });

        for (let i = 0; i < allFloorList.length; i++) {
          this.floorIdList.push(allFloorList[i].floor_id);
        }
        this.currentFloorId = this.floorIdList.slice(-1)[0];
      } catch (error) {
        console.log(error);
      }

      return allFloorList;
    },
    //현재 층 이미지 가져오기
    async getCurrentFloorImage() {
      let currentFloorImageList = new Array();
      try {
        let response = await axios
        .get(
          "http://172.30.1.56:8081/api/buildings/" +
            building_id +
            "/floors/" +
            this.currentFloorId +
            "/images"
        );

        let newImage = {};
        newImage.url = response.config.url;
        //newImage.floor = response.data.floor; // floor_id
        //newImage.building_id = response.data.building_id;
        newImage.create = false;
        newImage.modify = false;

        currentFloorImageList.push(newImage);

      } catch (e) {
        console.log(e);
      }
      return currentFloorImageList;
    },
    //나머지 층 이미지 가져오기
    async getOtherFloorImage(tableName) {
      let otherFloorImageList = new Array();
      try {
        let response = null;
        for (let i = 0; i < this.floorIdList-1; i++) {
          response = await axios
          .get(
            "http://172.30.1.56:8081/api/buildings/" +
              building_id +
              "/floors/" +
              this.currentFloorId +
              "/images"
          );
          for (var i = 0; i < response.data.length; i++) {
            let newImage = {};
            newImage.url = response[i].config.url;
            //newImage.floor = response[i].data.floor; // floor_id
            //newImage.building_id = response[i].data.building_id;
            newImage.create = false;
            newImage.delete = false;

            otherFloorImageList.push(newImage);
          }
        }
      } catch (e) {
        console.error(e);
      }
      return otherFloorImageList;
    },

    //우선 현재 층의 자리만 가져옴
    async getCurrentFloorSeats() {
      //console.log(this.currentFloorId); // ok
      let currentFloorSeatList = new Array();
      try {
        let response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            this.currentFloorId +
            "/seats"
        );
        for (var i = 0; i < response.data.length; i++) {
          let newSeat = {};
          newSeat.seat_id = response.data[i].seat_id;
          newSeat.floor = response.data[i].floor; // floor_id
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
        }
      } catch (e) {
        console.log(e);
      }
      return currentFloorSeatList;
    },
    //현재 층을 제외한 다른 층의 자리들을 가져와서 백그라운드 리스트에 가지고 있기
    async getOtherFloorSeats(tableName) {
      let otherFloorSeatList = new Array();
      try {
        let response = null;
        for (let i = 0; i < this.floorIdList - 1; i++) {
          response = await axios.get(
            "http://" +
              host +
              ":" +
              portNum +
              "/api/buildings/" +
              building_id +
              "/floors/" +
              this.floorIdList[i] +
              "/seats"
          );
          for (var i = 0; i < response.data.length; i++) {
            let newSeat = {};
            newSeat.seat_id = response.data[i].seat_id;
            newSeat.floor = response.data[i].floor; // floor_id
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

            otherFloorSeatList.push(newSeat);
          }
        }
      } catch (e) {
        console.error(e);
      }
      return otherFloorSeatList;
    },
    saveFloors(tableName, data) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("saveTableName is");
      console.log(saveTableName);

      axios
        .post(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/" +
            saveTableName,
          JSON.stringify(saveData),
          {
            headers: { "Content-Type": `application/json` },
          }
        )
        .then((res) => {
          console.log(res.saveData);
        });
    },
    saveImages(tableName, data, floor_id) {
      //추후에 api 구조 변경될 것을 생각하여 table, DTO를 넘겨받아 저장하는 것을 같은 함수로 묶지않음.
      let saveData = data;
      let saveTableName = tableName;
      axios.post(
          "http://172.30.1.56:8081/api/" +
            saveTableName +
            "/buildings/" +
            building_id +
            "/floors/" +
            floor_id +
            "/",
          saveData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        )
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    saveSeats(tableName, data, floor_id) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);
      axios
        .post(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            floor_id +
            "/" +
            saveTableName,
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
      let deleteTableName = tableName;
      let deleteKey = key;
      axios
        .delete(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/" +
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
    deleteSeatWithKey(tableName, key, floor_id) {
      let deleteTableName = tableName;
      let deleteKey = key;
      axios
        .delete(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/floors/" +
            floor_id +
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
