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
        v-if="images && currentFloorSeats"
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-bind:copyEmployee="employees"
        v-bind:copyImages="images"
        v-on:loadOtherFloorSeats="getOtherFloorSeats"
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
      images: null,
      currentFloorSeats: null,

      floorIdList: [],
      currentFloorId: null,
    };
  },
  async created() {
    // 사원 load
    this.employees = await this.getEmployees();
    // 층 load
    this.floors = await this.getFloors();
    // 이미지 load
    this.images = await this.getImages();
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
    //추후 현재 층의 이미지만 가져오게 구현해야 함.
    async getImages() {
      let allImageList = new Array();
      try {
        let response = await axios.get(
          "http://172.30.1.56:9000/hanzari/hanzariFloor?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIOSFODNN7EXAMPLE%2F20201028%2F%2Fs3%2Faws4_request&X-Amz-Date=20201028T041651Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=11bae981b8684cfe3e8f18597fa59671980027b20fbed4197460a54806dde773"
        );
        let newImage = {};
        newImage.url = response.config.url;
        console.log(newImage.url);
        allImageList.push(newImage);
      } catch (e) {
        console.log(e);
      }

      console.log(allImageList);
      //return this.images;
      return allImageList;
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
    getOtherFloorSeats(tableName) {
      let currentFloorSeatList = new Array();
    },
    saveImages(tableName, data) {
      //추후에 api 구조 변경될 것을 생각하여 table, DTO를 넘겨받아 저장하는 것을 같은 함수로 묶지않음.
      let saveData = data;
      let saveTableName = tableName;

      // for (let value of saveData.values()) {
      //   console.log(value);
      // }

      /*axios
        .post("http://172.30.1.56:8081" + "/api/" + saveTableName, saveData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        });*/
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
    saveImages(tableName, data) {
      let saveData = data;
      let saveTableName = tableName;
      axios
        .post("http://172.30.1.56:8081" + "/api/" + saveTableName, saveData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
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
