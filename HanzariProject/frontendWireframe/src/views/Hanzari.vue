<template>
  <div class="hanzari" id="hanzari">
    <v-app-bar app dark>
      <v-toolbar color="black" dark>
        <v-spacer>
          <v-toolbar-items class="hidden-sm-and-down">
            <v-icon large dark @click="drawer = !drawer">menu</v-icon>
           
          </v-toolbar-items></v-spacer
        >
        <v-toolbar-title>Hanzari</v-toolbar-title></v-toolbar
      ></v-app-bar
    >

    <v-navigation-drawer v-model="drawer" app :width="500">
      <Tabs
        v-if="employees && floors && currentFloorImage && otherFloorsImage"
        v-bind:copyEmployee="employees"
        v-bind:copyFloors="floors"
        v-bind:currentFloorImage="currentFloorImage"
        v-bind:otherFloorsImageList="otherFloorsImage"
      />
    </v-navigation-drawer>
    <v-main>
      <AttachCanvas
        v-if="
          employees &&
          floors &&
          currentFloorImage &&
          otherFloorsImage &&
          currentFloorSeats &&
          otherFloorsSeat
        "
        v-bind:copyEmployee="employees"
        v-bind:copyFloors="floors"
        v-bind:currentFloorImage="currentFloorImage"
        v-bind:otherFloorsImageList="otherFloorsImage"
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-bind:otherFloorsSeatsList="otherFloorsSeat"
        v-on:saveImages="saveImages"
        v-on:saveFloors="saveFloors"
        v-on:saveSeats="saveSeats"
        v-on:deleteFloorWithKey="deleteFloorWtihKey"
        v-on:deleteSeatWithKey="deleteSeatWithKey"
      />
    </v-main>
  </div>
</template>

<script>
import axios from "axios";

import Tabs from "@/components/Tabs.vue";
import AttachCanvas from "@/components/AttachCanvas.vue";
import MappingEmployee from "@/components/MappingEmployee.vue";
import ManageSeatInfo from "@/components/ManageSeatInfo.vue";
import { eventBus } from "../main";

const portNum = 8081;
const host = "172.30.1.53";
const building_id = "HANCOM01";

export default {
  name: "Hanzari",
  components: {
    Tabs,
    AttachCanvas,
    MappingEmployee,
    ManageSeatInfo,
  },
  data() {
    return {
      drawer: null,
      employees: null,
      floors: null,

      currentFloorImage: null,
      otherFloorsImage: null,

      currentFloorSeats: null,
      otherFloorsSeat: null,

      floorIdList: [],
      latestFloor: null,
    };
  },
  async created() {
    // 사원 load
    this.employees = await this.getEmployees();
    // 층 load
    this.floors = await this.getFloors();
    //가장 floor_order가 큰 층의 floor_id를 가져오기 위함
    this.latestFloor = await this.getLatestFloor();
    // 현재 층 이미지 load
    this.currentFloorImage = await this.getCurrentFloorImage();
    // 현재 층 자리 load
    this.currentFloorSeats = await this.getCurrentFloorSeats();
    // 나머지 층 이미지 load
    this.otherFloorsImage = await this.loadOtherFloorsImage();
    // 나머지 층 자리 load
    this.otherFloorsSeat = await this.loadOtherFloorSeats();
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
      } catch (error) {
        console.log(error);
      }
      return allFloorList;
    },
    async getLatestFloor() {
      let latestFloor = null;
      try {
        let response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/get-latest-floor"
        );
        latestFloor = response.data;
      } catch (error) {
        console.log(error);
      }
      return latestFloor;
    },
    //현재 층 이미지 가져오기
    async getCurrentFloorImage() {
      let currentFloorImage = new Array();
      let latestFloorId = this.latestFloor.floor_id;

      if (latestFloorId != null) {
        try {
          let response = await axios.get(
            "http://" +
              "172.30.1.56" +
              ":" +
              portNum +
              "/api/buildings/" +
              building_id +
              "/floors/" +
              latestFloorId +
              "/images"
          );

          let newImage = {};
          newImage.url = response.config.url;
          newImage.floorid = latestFloorId;
          currentFloorImage.push(newImage);
        } catch (error) {
          console.log(error);
        }
      }

      return currentFloorImage;
    },
    //나머지 층 이미지 가져오기
    async loadOtherFloorsImage() {
      let otherFloorImageList = new Array();
      let responseList = null;

      if (this.floorIdList.length > 0) {
        try {
          for (let i = 0; i < this.floorIdList.length - 1; i++) {
            let response = await axios.get(
              "http://" +
                "172.30.1.56" +
                ":" +
                portNum +
                "/api/buildings/" +
                building_id +
                "/floors/" +
                this.floorIdList[i] +
                "/images"
            );

            let newImage = {};
            newImage.url = response.config.url;
            newImage.floorid = this.floorIdList[i];
            responseList = newImage;
            otherFloorImageList.push(responseList);
          }
        } catch (error) {
          console.error(error);
        }
      }

      this.otherFloorsImage = otherFloorImageList;
      return this.otherFloorsImage;
    },
    //우선 현재 층의 자리만 가져옴
    async getCurrentFloorSeats() {
      let currentFloorSeatList = new Array();
      let latestFloorId = this.latestFloor.floor_id;
      try {
        let response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            latestFloorId +
            "/seats"
        );
        for (var i = 0; i < response.data.length; i++) {
          let newSeat = {};

          newSeat.seat_id = response.data[i].seat_id;
          newSeat.seat_name = response.data[i].seat_name;
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
      } catch (error) {
        console.log(error);
      }
      return currentFloorSeatList;
    },
    //현재 층을 제외한 다른 층의 자리들을 가져와서 백그라운드 리스트에 가지고 있기
    async loadOtherFloorSeats() {
      let otherFloorSeatMap = new Map();
      try {
        for (let i = 0; i < this.floorIdList.length - 1; i++) {
          // 층만큼 돈다
          let response = await axios.get(
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

          let responseList = new Array();
          // 그 층에 자리가 없다면
          if (response.data.length == 0) {
            otherFloorSeatMap.set(this.floorIdList[i], new Array());
          } else {
            for (let j = 0; j < response.data.length; j++) {
              // 자리 수 만큼 돈다
              let newSeat = {};
              newSeat.seat_id = response.data[j].seat_id;
              newSeat.seat_name = response.data[j].seat_name;
              newSeat.floor = response.data[j].floor; // floor_id
              newSeat.x = response.data[j].x;
              newSeat.y = response.data[j].y;
              newSeat.is_group = response.data[j].is_group;
              newSeat.building_id = response.data[j].building_id;
              newSeat.employee_id = response.data[j].employee_id;
              newSeat.width = response.data[j].width;
              newSeat.height = response.data[j].height;
              newSeat.degree = response.data[j].degree;
              newSeat.shape_id = response.data[j].shape_id;
              newSeat.create = false;
              newSeat.delete = false;
              newSeat.modify = false;

              responseList.push(newSeat);

              if (this.floorIdList[i] == response.data[j].floor) {
                otherFloorSeatMap.set(this.floorIdList[i], responseList);
              }
            } // end of for
          }
        } // end of for
      } catch (error) {
        console.error(error);
      }
      this.otherFloorsSeat = otherFloorSeatMap;
      return this.otherFloorsSeat;
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
          console.log(res);
        });
    },
    saveImages(tableName, data, floor_id) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);

      //for (var key of saveData.keys()) {
      //  console.log(key);
      //}

      //for (var value of saveData.values()) {
      //  console.log(value);
      //}

      axios
        .post(
          "http://" +
            "172.30.1.56" +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            floor_id +
            "/" +
            tableName,
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
          console.log(res);
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
    deleteSeatWithKey(tableName, seatId, floor_id) {
      let deleteTableName = tableName;
      let deleteKey = seatId;
      axios
        .delete(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            floor_id +
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
  },
};
</script>
