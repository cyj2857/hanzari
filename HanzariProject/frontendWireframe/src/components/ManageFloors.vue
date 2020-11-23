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
          :key="floor.floorId"
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
                  border: clickFloorIndexes.includes(floor.floorId)
                    ? 'thick solid black'
                    : '',
                }"
                ><h3>{{ floor.floorName }}</h3></v-btn
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
            v-model="currentSelectedFloor.floorName"
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
  props: [
    "copyFromTabsFloorList",
    "copyFromTabsLatestFloorImage",
    "copyFromTabsOtherFloorsImageList",
  ],
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
      this.clickFloorIndexes = this.currentSelectedFloor.floorId;
    }

    if (this.allImageMap == null) {
      this.allImageMap = new Map();
      if (this.copyFromTabsLatestFloorImage) {
        for (let i = 0; i < this.copyFromTabsLatestFloorImage.length; i++) {
          let imgUrl = this.copyFromTabsLatestFloorImage[i].url;
          let floorId = this.copyFromTabsLatestFloorImage[i].floorId;
          this.allImageMap.set(floorId, imgUrl);
        }
      }

      if (this.copyFromTabsOtherFloorsImageList) {
        for (let i = 0; i < this.copyFromTabsOtherFloorsImageList.length; i++) {
          let imgUrl = this.copyFromTabsOtherFloorsImageList[i].url;
          let floorId = this.copyFromTabsOtherFloorsImageList[i].floorId;
          this.allImageMap.set(floorId, imgUrl);
        }
      }
    }

    eventBus.$on("allSeatMap", (allSeatMap) => {
      this.allSeatMap = allSeatMap;
    });

    eventBus.$on("showSeatFloor", (floorId) => {
      for (let i = 0; i < this.allFloorList.length; i++) {
        //console.log(typeof floorId);//String
        //console.log(typeof this.allFloorList[i].floorId); //String

        if (floorId === this.allFloorList[i].floorId) {
          this.clickFloor(this.allFloorList[i]);
        }
      }
    });
  },
  beforeDestroy() {
    eventBus.$off("allSeatMap");
    eventBus.$off("showSeatFloor");
  },
  methods: {
    showToolTip(floor) {
      if (this.allSeatMap) {
        if (this.allSeatMap.get(floor.floorId)) {
          if (this.allSeatMap.get(floor.floorId).length) {
            let eachFloorSeatList = this.allSeatMap.get(floor.floorId);
            let department = [];

            let currentFloorSeatsLength = eachFloorSeatList.length;
            let currentFloorVacantSeatsLength = null;
            let employeeDepartmentMap = new Map();

            if (currentFloorSeatsLength) {
              for (let i = 0; i < eachFloorSeatList.length; i++) {
                if (eachFloorSeatList[i].employeeId == null) {
                  currentFloorVacantSeatsLength++;
                } else {
                  if (
                    !employeeDepartmentMap.get(
                      eachFloorSeatList[i].employeeDepartment
                    )
                  ) {
                    let employees = [];
                    employeeDepartmentMap.set(
                      eachFloorSeatList[i].employeeDepartment,
                      employees
                    );
                  }

                  employeeDepartmentMap
                    .get(eachFloorSeatList[i].employeeDepartment)
                    .push(eachFloorSeatList[i].employeeId);
                }
              }

              let keys = [];
              keys = Array.from(employeeDepartmentMap.keys());
              let text = "<br>";
              for (let i = 0; i < keys.length; i++) {
                text +=
                  keys[i] +
                  " : " +
                  employeeDepartmentMap.get(keys[i]).length +
                  "<br>";
              }

              this.toolTipText =
                floor.floorName +
                "층 <br>" +
                "전체 좌석 : " +
                currentFloorSeatsLength +
                "<br> 공석 : " +
                currentFloorVacantSeatsLength +
                text;
            }
          } else {
            this.toolTipText =
              floor.floorName + "층 <br>" + "좌석이 없습니다.";
          }
        } else {
          this.toolTipText = floor.floorName + "층 <br>" + "좌석이 없습니다.";
        }
      } else {
        this.toolTipText = floor.floorName + "층 <br>" + "좌석이 없습니다.";
      }
    },
    clickFloor(floor) {
      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(floor.floorId);

      if (this.allImageMap.get(floor.floorId)) {
        this.currentFloorImageName = this.allImageMap.get(floor.floorId).name;
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
      newFloor.floorId = this.createFloorUUID();
      newFloor.floorName = "";
      newFloor.buildingId = "HANCOM01";
      newFloor.floorOrder = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

      this.currentSelectedFloor = newFloor;

      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(this.currentSelectedFloor.floorId);

      this.length++;

      eventBus.$emit("changeFloor", this.currentSelectedFloor);

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    editFloorName() {
      const idx = this.allFloorList.findIndex((item) => {
        //console.log(typeof item.floorId);//String
        //console.log(typeof this.currentSelectedFloor.floorId); //String
        return item.floorId === this.currentSelectedFloor.floorId;
      });

      this.allFloorList[idx].modify = true;
      this.managerFloorList[idx].modify = true;

      eventBus.$emit("changeFloorName", this.currentSelectedFloor.floorName);

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    removeFloor() {
      if (this.length > 0) {
        let currentFloorId = this.currentSelectedFloor.floorId;
        const idx = this.allFloorList.findIndex(function (item) {
          //console.log(typeof item.floorId) //String
          //console.log(typeof currentFloorId) //String
          return item.floorId === currentFloorId; //String
        });
        if (idx > -1) {
          eventBus.$emit("deleteSeatListKey", this.allFloorList[idx].floorId);
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;

          let nextIdx = null;
          console.log(typeof idx) //number
          console.log(typeof 0) //number
          if (idx === 0) { //number
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
          this.clickFloorIndexes.push(this.currentSelectedFloor.floorId);
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
      this.allImageMap.set(this.currentSelectedFloor.floorId, file);
      this.currentFloorImageName = this.allImageMap.get(
        this.currentSelectedFloor.floorId
      ).name;
      eventBus.$emit("allImageMap", this.allImageMap);
    },
  },
};
</script>

