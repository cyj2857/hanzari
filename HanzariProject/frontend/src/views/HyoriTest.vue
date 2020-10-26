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
        v-bind:images="images"
        v-bind:currentFloorSeatsList="currentFloorSeats"
        v-on:saveByImages="saveImage"
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
      images: [],
      currentFloorSeats: [],
      currentFloor: null,
    };
  },
  created() {
    this.employees = this.getEmployees();
    this.floors = this.getFloors();
    this.images = this.getImages();
    //this.seats = this.getAllSeats(); //Map
    eventBus.$on("changeFloor", (floor) => {
      this.currentFloor = floor;
      console.log(
        this.currentFloor + "?ó¨Í∏∞Í?? HyoriTestÎ°? ?Ñò?ñ¥?ò® ?òÑ?û¨Ï∏?"
      );
    });

    this.currentFloorSeats = this.getCurrentFloorSeats(this.currentFloor); //currentFloor's seatList

    console.log(this.getFloorLength() + "Ï∏µÏùò Í∞úÏàò?ûÖ?ãà?ã§."); //0
    console.log(this.getEmployeeLength() + "?Ç¨?õê?ùò Í∞úÏàò?ûÖ?ãà?ã§."); //0
  },
  mounted() {
    //changeFloor?ê†?ïå ?Ñò?ñ¥?ò§?äî floor_idÎ•? ?Ñ£?ñ¥?ïº?ï®.
    // console.log(
    //   this.floors[this.floors.length - 1].floor_id +
    //     "?îîÎπÑÎ°úÎ∂??Ñ∞ Í∞?Ïß?Í≥†Ïò® Ï∏µÎì§?ùò Îß? ÎßàÏ??Îß? Ï∏µÏùò ?ïÑ?ù¥?îî?ûÖ?ãà?ã§."
    // );
    //console.log(this.floors.length+"Ï∏µÏùò Í∞úÏàò?ûÖ?ãà?ã§."); //0
    // this.currentFloorSeats = this.getCurrentFloorSeats(
    //   this.floors[this.floors.length - 1].floor_id
    // )
    //console.log(this.currentFloorSeats.length+"?îîÎπÑÎ°úÎ∂??Ñ∞ Í∞?Ïß?Í≥†Ïò® ?òÑ?û¨Ï∏µÏùò ?ûêÎ¶¨Î¶¨?ä§?ä∏ Í∏∏Ïù¥?ûÖ?ãà?ã§.");
  },
  beforeUpdate() {
    console.log(this.getFloorLength() + "Ï∏µÏùò Í∞úÏàò?ûÖ?ãà?ã§."); //2
    //this.currentFloor = this.floors[this.getFloorLength() - 1];
    //console.log(this.currentFloor.floor_name+"Îß? ÎßàÏ??Îß? Ï∏µÏùò ?ïÑ?ù¥?îî?ûÖ?ãà?ã§.");
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
    //floor_id ?òÑ?û¨?äî name?úºÎ°? ?ù∏?ûêÍ∞? ?Ñò?ñ¥?ò¥
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
            newSeat.create = false;
            newSeat.delete = false;
            newSeat.modify = false;

            currentFloorSeatList.push(newSeat);
            //}
          }
        });
      //console.log("?Ñò?ñ¥?ò® ?òÑ?û¨Ï∏µÏóê ????ïú ?ûêÎ¶¨Î¶¨?ä§?ä∏ Í∞úÏàò?ûÖ?ãà?ã§. -> "+currentFloorSeatList.length);
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
      //all seats // ?òÑ?û¨ Ï∏? ?†ú?ô∏?ïú all seatsÎ°? ?ã§?ãú Íµ¨ÌòÑ?ï¥?ïº?ï®.
      let allDBSeatMap = new Map();
      console.log(
        this.floors.length + "Ï∏µÏùò Í∞úÏàò?ûÖ?ãà?ã§. ?ï®?àò?ïà?óê?Ñú?öî"
      ); //0
      for (let i = 0; i < this.floors.length; i++) {
        allDBSeatMap.set(
          this.floors[i].floor_name,
          this.getOneFloorSeats(this.floors[i].floor_id)
        );
      }
      //console.log(this.floors.length+"Ï∏µÏùò Í∞úÏàò?ûÖ?ãà?ã§. ?ï®?àò?ïà?óê?Ñú?öî")
      return allDBSeatMap;
    },
    getFloors() {
      let allFloorList = new Array();
      axios
        .get("http://" + host + ":" + portNum + "/api/buildings/HANCOM01/floors")
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
      axios
        //.get("http://" + host + ":" + portNum + "api/building/{~}/floor/{~}/imageurl")
        .get(
          "http://172.30.1.56:9000/hanzari/%ED%95%9C%EA%B8%80%EA%B3%BC%EC%BB%B4%ED%93%A8%ED%84%B0-1%EC%B8%B5.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIOSFODNN7EXAMPLE%2F20201023%2F%2Fs3%2Faws4_request&X-Amz-Date=20201023T021304Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=3762c647cfd02789e889243ef2d333aa0d18abd1894aca75b7edaf3d2848e306"
        )
        .then((response) => {
          const initImageList = new Array();
          const imgurl = response.config.url;

          initImageList.push(imgurl);
          this.images = initImageList[0];

          console.log(this.images);
          //console.log(initImageList);
          //console.log(initImageList.length); //1
          //console.log(initImageList[0]);

          return this.images;
        });
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
    saveImage(tableName, data) {
      let saveData = data;
      let saveTableName = tableName;
      console.log("saveData is");
      console.log(saveData);
      console.log("------------");
      console.log("saveTableName is");
      console.log(saveTableName);

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