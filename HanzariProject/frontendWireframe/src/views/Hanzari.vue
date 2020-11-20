<template>
  <div class="hanzari" id="hanzari">
    <v-app-bar app dark>
      <v-toolbar color="black" dark>
        <v-spacer>
          <v-toolbar-items>
            <v-icon large dark @click="drawer = !drawer" v-if="drawer"
              >keyboard_arrow_left</v-icon
            >
            <v-icon large dark @click="drawer = !drawer" v-if="!drawer"
              >keyboard_arrow_right</v-icon
            >
          </v-toolbar-items>
        </v-spacer>
        <v-toolbar-title>한자리</v-toolbar-title></v-toolbar
      >
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app :width="500">
      <Tabs
        v-if="
          employeeList && floorList && latestFloorImage && otherFloorsImageList
        "
        v-bind:copyEmployeeList="employeeList"
        v-bind:copyFloorList="floorList"
        v-bind:copyLatestFloorImage="latestFloorImage"
        v-bind:copyOtherFloorsImageList="otherFloorsImageList"
      />
    </v-navigation-drawer>
    <v-main>
      <AssignSeats
        v-if="
          employeeList &&
          floorList &&
          latestFloorImage &&
          latestFloorSeatList &&
          otherFloorsImageList &&
          otherFloorsSeatMap
        "
        v-bind:copyEmployeeList="employeeList"
        v-bind:copyFloorList="floorList"
        v-bind:copyLatestFloorImage="latestFloorImage"
        v-bind:copyOtherFloorsImageList="otherFloorsImageList"
        v-bind:copyLatestFloorSeatList="latestFloorSeatList"
        v-bind:copyOtherFloorsSeatMap="otherFloorsSeatMap"
        v-on:saveImages="saveImages"
        v-on:saveFloors="saveFloors"
        v-on:saveSeats="saveSeats"
        v-on:deleteFloorWithKey="deleteFloorWtihKey"
        v-on:deleteSeatWithKey="deleteSeatWithKey"
        v-on:downloadCSVFile="downloadCSVFile"
        v-on:saveFromCSVFileToDB="saveFromCSVFileToDB"
      />
    </v-main>
  </div>
</template>

<script>
import axios from "axios";
import { eventBus } from "../main";

import Tabs from "@/components/Tabs.vue";
import AssignSeats from "@/components/AssignSeats.vue";
import MappingEmployee from "@/components/MappingEmployee.vue";

const portNum = 8081;
const host = "172.30.1.53";
const building_id = "HANCOM01";

export default {
  name: "Hanzari",
  components: {
    Tabs,
    AssignSeats,
    MappingEmployee,
  },
  data() {
    return {
      drawer: null,
      employeeList: null,

      floorList: null,
      floorIdList: [],
      latestFloor: null,

      latestFloorImage: null,
      otherFloorsImageList: null,

      latestFloorSeatList: null,
      otherFloorsSeatMap: null,
    };
  },
  async created() {
    //사원 load
    this.employeeList = await this.getEmployeeList();
    //층 load
    this.floorList = await this.getFloorList();
    //가장 floor_order가 큰 층의 floor_id를 가져오기 위함
    this.latestFloor = await this.getLatestFloor();

    // 최신 층 이미지 load
    this.latestFloorImage = await this.getLatestFloorImage();
    // 최신 층 자리 load
    this.latestFloorSeatList = await this.getLatestFloorSeatList();

    // 나머지 층 이미지 load
    this.otherFloorsImageList = await this.getOtherFloorImageList();
    // 나머지 층 자리 load
    this.otherFloorsSeatMap = await this.getOtherFloorsSeatMap();
  },
  methods: {
    async getEmployeeList() {
      let allEmployeeList = [];
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

          allEmployeeList.push(newEmployee);
        }
      } catch (error) {
        console.log(error);
      }
      return allEmployeeList;
    },
    async getFloorList() {
      let allFloorList = [];
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
    async getLatestFloorImage() {
      let latestFloorImage = [];
      if (this.latestFloor) {
        let latestFloorId = this.latestFloor.floor_id;
        if (latestFloorId != null) {
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
                "/images"
            );

            let newImage = {};
            newImage.url = response.config.url;
            newImage.floorid = latestFloorId;

            latestFloorImage.push(newImage);
          } catch (error) {
            console.log(error);
          }
        }
      }
      return latestFloorImage;
    },
    //나머지 층 이미지 가져오기
    async getOtherFloorImageList() {
      let otherFloorsImageList = [];
      let responseList = null;
      if (this.floorIdList.length > 0) {
        try {
          for (let i = 0; i < this.floorIdList.length - 1; i++) {
            let response = await axios.get(
              "http://" +
                host +
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
            otherFloorsImageList.push(responseList);
          }
        } catch (error) {
          console.error(error);
        }
      }
      return otherFloorsImageList;
    },
    //우선 최신 층의 자리만 가져옴
    async getLatestFloorSeatList() {
      let latestFloorSeatList = [];
      if (this.latestFloor) {
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

            latestFloorSeatList.push(newSeat);
          }
        } catch (error) {
          console.log(error);
        }
      }
      return latestFloorSeatList;
    },
    //최신 층을 제외한 다른 층의 자리들을 가져와서 백그라운드 리스트에 가지고 있기
    async getOtherFloorsSeatMap() {
      let otherFloorsSeatMap = new Map();
      try {
        for (let i = 0; i < this.floorIdList.length - 1; i++) {
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
          let responseList = [];
          // 그 층에 자리가 없다면
          if (response.data.length == 0) {
            otherFloorsSeatMap.set(this.floorIdList[i], []);
          } else {
            for (let j = 0; j < response.data.length; j++) {
              // 자리 수 만큼 돈다
              let newSeat = {};
              newSeat.seat_id = response.data[j].seat_id;
              newSeat.seat_name = response.data[j].seat_name;
              newSeat.floor = response.data[j].floor;
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
                otherFloorsSeatMap.set(this.floorIdList[i], responseList);
              }
            }
          }
        }
      } catch (error) {
        console.error(error);
      }
      return otherFloorsSeatMap;
    },
    saveFloors(tableName, data) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("saveTableName is");
      console.log(saveTableName);
      try {
        axios.post(
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
        );
      } catch (error) {
        console.error(error);
      }
    },
    saveImages(tableName, data, floor_id) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);
      try {
        axios.post(
          "http://" +
            host +
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
        );
      } catch (error) {
        console.error(error);
      }
    },
    saveSeats(tableName, data, floor_id) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);
      try {
        axios.post(
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
        );
      } catch (error) {
        console.error(error);
      }
    },
    deleteFloorWtihKey(tableName, key) {
      let deleteTableName = tableName;
      let deleteKey = key;
      try {
        axios.delete(
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
        );
      } catch (error) {
        console.error(error);
      }
    },
    deleteSeatWithKey(tableName, seatId, floor_id) {
      let deleteTableName = tableName;
      let deleteKey = seatId;
      try {
        axios.delete(
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
        );
      } catch (error) {
        console.error(error);
      }
    },

    //get CSV File from DB and download CSV file
    async downloadCSVFile(floor_id) {
      try {
        const response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors/" +
            floor_id +
            "/seats/get-csv-file",
          {
            headers: {
              "Content-Type": "text/csv",
            },
          }
        );

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        const contentDisposition = response.headers["content-disposition"]; // 파일 이름 //cors

        let filename = null;
        if (contentDisposition) {
          const [fileNameMatch] = contentDisposition
            .split(";")
            .filter((str) => str.includes("filename"));
          if (fileNameMatch) [, filename] = fileNameMatch.split("=");
        }
        link.href = url;
        link.setAttribute("download", `${filename}`);
        link.style.cssText = "display:none";
        document.body.appendChild(link);
        link.click();
        link.remove();
      } catch (error) {
        console.log(error);
      }
    },

    //save information from CSV to DB
    saveFromCSVFileToDB(saveData, floor_id) {
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
            "/seats/update-by-file",
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
  },
};
</script>
