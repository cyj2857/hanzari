<template>
  <div>
    <v-card flat color="transparent">
      <v-row>
        <v-col cols="12" sm="8">
          <v-card-title
            ><v-icon large>stairs</v-icon>
            <h3>층 설정</h3></v-card-title
          ></v-col
        ><v-col cols="12" sm="4">
          <v-btn small
            ><v-icon medium dark @click="removeFloor"
              >remove_circle</v-icon
            ></v-btn
          >
          <v-btn small
            ><v-icon medium dark @click="addFloor">add_circle</v-icon></v-btn
          ></v-col
        ></v-row
      >
      <v-row style="overflow-y: scroll; height: 180px">
        <v-col
          v-for="floor of this.allFloorList"
          :key="floor.floor_id"
          class="d-flex child-flex"
          cols="4"
          ><v-tooltip bottom
            ><template v-slot:activator="{ on }">
              <v-btn
                v-on="on"
                large
                @click="clickFloor(floor)"
                @mouseover="showToolTip(floor)"
                :style="{
                  border: clickFloorIndexes.includes(floor.floor_id)
                    ? 'thick solid black'
                    : '',
                }"
                ><h3>{{ floor.floor_name }}</h3></v-btn
              >
            </template>
            <span v-html="toolTipText"> </span>
          </v-tooltip>
        </v-col>
      </v-row>
      <v-divider class="mx-4"></v-divider>
      <v-card-title
        ><v-icon large>stairs</v-icon>
        <h3>층 이름 편집</h3></v-card-title
      >
      <v-row v-if="currentSelectedFloor">
        <v-col cols="12" sm="9">
          <v-text-field
            v-model="currentSelectedFloor.floor_name"
            @keyup="editFloorName"
            label="층 이름을 입력하세요"
            solo
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="3">
          <v-card-text><h2>층</h2></v-card-text>
        </v-col>
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-card-title
        ><v-icon large>image</v-icon>
        <h3>배경화면 설정</h3></v-card-title
      >
      <v-row>
        <v-col cols="12" sm="9">
          <v-card>
            <v-card-text>{{ currentFloorImageName }}</v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" sm="3">
          <v-card-text>
            <input
              v-show="false"
              ref="Upload"
              type="file"
              @change="changeImageFile"
            />
            <v-btn color="blue-grey lighten-2" @click="$refs.Upload.click()"
              ><h4>업로드</h4></v-btn
            >
          </v-card-text>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import "material-design-icons-iconfont/dist/material-design-icons.css";

export default {
  props: ["copyFromTabsFloorList", "copyFromTabsLatestFloorImage", "copyFromTabsOtherFloorsImageList"],
  data() {
    return {
      length: null,

      allFloorList: [],
      managerFloorList: [],

      currentSelectedFloor: null,

      clickFloorIndexes: null,

      allImageMap: null,
      currentFloorImageName: "이미지를 업로드하세요",

      allSeatMap: null,
      toolTipText: null,
    };
  },
  created() {
    if (this.copyFromTabsFloorList && this.copyFromTabsFloorList.length) {
      this.currentSelectedFloor = this.copyFromTabsFloorList[
        this.copyFromTabsFloorList.length - 1
      ];
      this.allFloorList = this.copyFromTabsFloorList;
      this.managerFloorList = this.allFloorList.slice();
      this.length = this.copyFromTabsFloorList.length;
      this.clickFloorIndexes = this.currentSelectedFloor.floor_id;
    }

    if (this.allImageMap == null) {
      this.allImageMap = new Map();
      if (this.copyFromTabsLatestFloorImage) {
        for (let i = 0; i < this.copyFromTabsLatestFloorImage.length; i++) {
          let imgurl = this.copyFromTabsLatestFloorImage[i].url;
          let floorid = this.copyFromTabsLatestFloorImage[i].floorid;
          this.allImageMap.set(floorid, imgurl);
        }
      }

      if (this.copyFromTabsOtherFloorsImageList) {
        for (let i = 0; i < this.copyFromTabsOtherFloorsImageList.length; i++) {
          let imgurl = this.copyFromTabsOtherFloorsImageList[i].url;
          let floorid = this.copyFromTabsOtherFloorsImageList[i].floorid;
          this.allImageMap.set(floorid, imgurl);
        }
      }
    }

    eventBus.$on("allSeatMap", (allSeatMap) => {
      this.allSeatMap = allSeatMap;
    });

    eventBus.$on("showSeatFloor", (floorid) => {
      for (let i = 0; i < this.allFloorList.length; i++) {
        if (floorid == this.allFloorList[i].floor_id) {
          this.clickFloor(this.allFloorList[i]);
        }
      }
    });
  },
  methods: {
    showToolTip(floor) {
      if (this.allSeatMap) {
        if (this.allSeatMap.get(floor.floor_id)) {
          if (this.allSeatMap.get(floor.floor_id).length) {
            let eachFloorSeatList = this.allSeatMap.get(floor.floor_id);
            let department = new Array();

            let currentFloorSeatsLength = eachFloorSeatList.length;
            let currentFloorVacantSeatsLength = null;
            let employeeDepartmentMap = new Map();

            if (currentFloorSeatsLength) {
              for (let i = 0; i < eachFloorSeatList.length; i++) {
                if (eachFloorSeatList[i].employee_id == null) {
                  currentFloorVacantSeatsLength++;
                } else {
                  if (
                    !employeeDepartmentMap.get(
                      eachFloorSeatList[i].employee_department
                    )
                  ) {
                    let employees = new Array();
                    employeeDepartmentMap.set(
                      eachFloorSeatList[i].employee_department,
                      employees
                    );
                  }

                  employeeDepartmentMap
                    .get(eachFloorSeatList[i].employee_department)
                    .push(eachFloorSeatList[i].employee_id);
                }
              }

              let keys = new Array();
              keys = Array.from(employeeDepartmentMap.keys());
              let text = "<br>";
              for (let i = 0; i < keys.length; i++) {
                console.log(keys[i] + employeeDepartmentMap.get(keys[i]));
                text +=
                  keys[i] +
                  " : " +
                  employeeDepartmentMap.get(keys[i]).length +
                  "<br>";
              }

              this.toolTipText =
                floor.floor_name +
                "층 <br>" +
                "전체 좌석 : " +
                currentFloorSeatsLength +
                "<br> 공석 : " +
                currentFloorVacantSeatsLength +
                text;
            }
          } else {
            this.toolTipText =
              floor.floor_name + "층 <br>" + "좌석이 없습니다.";
          }
        } else {
          this.toolTipText = floor.floor_name + "층 <br>" + "좌석이 없습니다.";
        }
      } else {
        this.toolTipText = floor.floor_name + "층 <br>" + "좌석이 없습니다.";
      }
    },
    clickFloor(floor) {
      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(floor.floor_id);

      if (this.allImageMap.get(floor.floor_id)) {
        this.currentFloorImageName = this.allImageMap.get(floor.floor_id).name;
      } else {
        this.currentFloorImageName = "이미지를 업로드하세요";
      }

      this.currentSelectedFloor = floor;
      eventBus.$emit("changeFloor", floor);
    },
    createFloorUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    addFloor() {
      let newFloor = {};
      newFloor.floor_id = this.createFloorUUID();
      newFloor.floor_name = "";
      newFloor.building_id = "HANCOM01";
      newFloor.floor_order = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

      this.currentSelectedFloor = newFloor;

      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(this.currentSelectedFloor.floor_id);

      this.length++;

      eventBus.$emit("changeFloor", this.currentSelectedFloor);

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    editFloorName() {
      const idx = this.allFloorList.findIndex((item) => {
        return item.floor_id == this.currentSelectedFloor.floor_id;
      });

      this.allFloorList[idx].modify = true;
      this.managerFloorList[idx].modify = true;

      eventBus.$emit("changeFloorName", this.currentSelectedFloor.floor_name);

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    removeFloor() {
      if (this.length > 0) {
        let currentFloorId = this.currentSelectedFloor.floor_id;
        const idx = this.allFloorList.findIndex(function (item) {
          return item.floor_id == currentFloorId;
        });
        if (idx > -1) {
          eventBus.$emit("deleteSeatListKey", this.allFloorList[idx].floor_id);
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;

          let nextIdx = null;
          if (idx == 0) {
            nextIdx = idx;
          } else {
            nextIdx = idx - 1;
          }

          eventBus.$emit("changeFloor", this.allFloorList[nextIdx]);

          let allFloors = this.allFloorList.slice();
          eventBus.$emit("allFloorList", allFloors);

          let managerFloors = this.managerFloorList.slice();
          eventBus.$emit("managerFloorList", managerFloors);

          this.currentSelectedFloor = this.allFloorList[nextIdx];
        }

        this.length--;

        if (this.length > 0) {
          this.clickFloorIndexes = [];
          this.clickFloorIndexes.push(this.currentSelectedFloor.floor_id);
        }
      } else {
        alert("there are no seats to delete!");
      }
    },
    changeImageFile(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.saveImageFile(files[0]);
    },
    saveImageFile(file) {
      this.allImageMap.set(this.currentSelectedFloor.floor_id, file);
      this.currentFloorImageName = this.allImageMap.get(
        this.currentSelectedFloor.floor_id
      ).name;
      eventBus.$emit("allImageMap", this.allImageMap);
    },
  },
};
</script>

