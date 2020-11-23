<template>
  <v-app id="app">
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
            employeeList &&
            floorList &&
            latestFloorImage &&
            otherFloorsImageList
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
  </v-app>
</template>

<script>
import axios from "axios";
import { eventBus } from "../main";

import Tabs from "@/components/Tabs.vue";
import AssignSeats from "@/components/AssignSeats.vue";
import MappingEmployee from "@/components/MappingEmployee.vue";

const PORT_NUMBER = 8081;
const HOST = "172.30.1.53";
const BUILDING_ID = "HANCOM01";

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
    //가장 floor_order가 큰 층의 floorId를 가져오기 위함
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
          "http://" + HOST + ":" + PORT_NUMBER + "/api/employee"
        );
        for (var i = 0; i < response.data.length; i++) {
          var newEmployeeObject = {};
          newEmployeeObject.name = response.data[i].employee_name;
          newEmployeeObject.department = response.data[i].department_name;
          newEmployeeObject.number = response.data[i].extension_number;
          newEmployeeObject.employeeId = response.data[i].employee_id;
          newEmployeeObject.seatIdList = response.data[i].seatList;

          allEmployeeList.push(newEmployeeObject);
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
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors"
        );
        for (var i = 0; i < response.data.length; i++) {
          let newFloorObject = {};
          newFloorObject.floorId = response.data[i].floor_id;
          newFloorObject.floorName = response.data[i].floor_name;
          newFloorObject.buildingId = response.data[i].building_id;
          newFloorObject.floorOrder = response.data[i].floor_order;
          newFloorObject.create = false;
          newFloorObject.modify = false;
          newFloorObject.delete = false;

          allFloorList.push(newFloorObject);
        }

        allFloorList.sort(function (a, b) {
          return a.floorOrder < b.floorOrder
            ? -1
            : a.floorOrder > b.floorOrder
            ? 1
            : 0;
        });

        for (let i = 0; i < allFloorList.length; i++) {
          this.floorIdList.push(allFloorList[i].floorId);
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
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
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
                HOST +
                ":" +
                PORT_NUMBER +
                "/api/buildings/" +
                BUILDING_ID +
                "/floors/" +
                latestFloorId +
                "/images"
            );

            let newImageObject = {};
            newImageObject.url = response.config.url;
            newImageObject.floorId = latestFloorId;

            latestFloorImage.push(newImageObject);
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
                HOST +
                ":" +
                PORT_NUMBER +
                "/api/buildings/" +
                BUILDING_ID +
                "/floors/" +
                this.floorIdList[i] +
                "/images"
            );

            let newImageObject = {};
            newImageObject.url = response.config.url;
            newImageObject.floorId = this.floorIdList[i];
            responseList = newImageObject;
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
              HOST +
              ":" +
              PORT_NUMBER +
              "/api/buildings/" +
              BUILDING_ID +
              "/floors/" +
              latestFloorId +
              "/seats"
          );
          for (var i = 0; i < response.data.length; i++) {
            let newSeatObject = {};

            newSeatObject.seatId = response.data[i].seat_id;
            newSeatObject.seatName = response.data[i].seat_name;
            newSeatObject.floorId = response.data[i].floor;
            newSeatObject.x = response.data[i].x;
            newSeatObject.y = response.data[i].y;
            newSeatObject.isGroup = response.data[i].is_group;
            newSeatObject.buildingId = response.data[i].building_id;
            newSeatObject.employeeId = response.data[i].employee_id;
            newSeatObject.width = response.data[i].width;
            newSeatObject.height = response.data[i].height;
            newSeatObject.degree = response.data[i].degree;
            newSeatObject.shapeId = response.data[i].shape_id;
            newSeatObject.create = false;
            newSeatObject.delete = false;
            newSeatObject.modify = false;

            latestFloorSeatList.push(newSeatObject);
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
              HOST +
              ":" +
              PORT_NUMBER +
              "/api/buildings/" +
              BUILDING_ID +
              "/floors/" +
              this.floorIdList[i] +
              "/seats"
          );
          let responseList = [];
          // 그 층에 자리가 없다면
          //console.log(typeof response.data.length) //number
          //console.log(typeof 0) //number
          if (response.data.length === 0) { //number
            otherFloorsSeatMap.set(this.floorIdList[i], []);
          } else {
            for (let j = 0; j < response.data.length; j++) {
              // 자리 수 만큼 돈다
              let newSeatObject = {};
              newSeatObject.seatId = response.data[j].seat_id;
              newSeatObject.seatName = response.data[j].seat_name;
              newSeatObject.floorId = response.data[j].floor;
              newSeatObject.x = response.data[j].x;
              newSeatObject.y = response.data[j].y;
              newSeatObject.isGroup = response.data[j].is_group;
              newSeatObject.buildingId = response.data[j].building_id;
              newSeatObject.employeeId = response.data[j].employee_id;
              newSeatObject.width = response.data[j].width;
              newSeatObject.height = response.data[j].height;
              newSeatObject.degree = response.data[j].degree;
              newSeatObject.shapeId = response.data[j].shape_id;
              newSeatObject.create = false;
              newSeatObject.delete = false;
              newSeatObject.modify = false;

              responseList.push(newSeatObject);

              //console.log(typeof this.floorIdList[i]); //String
              //console.log(typeof response.data[j].floor) //String
              if (this.floorIdList[i] === response.data[j].floor) { //String
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

      try {
        axios.post(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
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
    saveImages(tableName, data, floorId) {
      let saveData = data;
      let saveTableName = tableName;

      try {
        axios.post(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors/" +
            floorId +
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
    saveSeats(tableName, data, floorId) {
      let saveData = data;
      let saveTableName = tableName;
      try {
        axios.post(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors/" +
            floorId +
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
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/" +
            deleteTableName +
            "/" +
            deleteKey
        );
      } catch (error) {
        console.error(error);
      }
    },
    deleteSeatWithKey(tableName, seatId, floorId) {
      let deleteTableName = tableName;
      let deleteKey = seatId;
      try {
        axios.delete(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors/" +
            floorId +
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
    async downloadCSVFile(floorId) {
      try {
        let response = await axios.get(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors/" +
            floorId +
            "/seats/get-csv-file-allfloor-seats",
          {
            headers: {
              "Content-Type": "text/csv",
            },
          }
        );

        let url = window.URL.createObjectURL(new Blob([response.data]));
        let link = document.createElement("a");
        let contentDisposition = response.headers["content-disposition"]; // 파일 이름 //cors

        let filename = null;
        if (contentDisposition) {
          let [fileNameMatch] = contentDisposition
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
    saveFromCSVFileToDB(saveData, floorId) {
      axios
        .post(
          "http://" +
            HOST +
            ":" +
            PORT_NUMBER +
            "/api/buildings/" +
            BUILDING_ID +
            "/floors/" +
            floorId +
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
