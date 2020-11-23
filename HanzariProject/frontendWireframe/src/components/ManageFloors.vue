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
          v-for="floorObject of this.allFloorList"
          :key="floorObject.floorId"
          class="d-flex child-flex"
          cols="4"
          ><v-tooltip bottom
            ><template v-slot:activator="{ on }">
              <v-btn
                v-on="on"
                large
                @click="clickFloor(floorObject)"
                @mouseover="showToolTip(floorObject)"
                :style="{
                  border: clickFloorIndexes.includes(floorObject.floorId)
                    ? 'thick solid black'
                    : '',
                }"
                ><h3>{{ floorObject.floorName }}</h3></v-btn
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
      <v-row v-if="currentSelectedFloorObject">
        <v-col cols="12" sm="9">
          <v-text-field
            v-model="currentSelectedFloorObject.floorName"
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

      currentSelectedFloorObject: null,

      clickFloorIndexes: null,

      allImageMap: null,
      currentFloorImageName: "이미지를 업로드하세요",

      allSeatMap: null,
      toolTipText: null,
    };
  },
  created() {
    if (this.copyFromTabsFloorList && this.copyFromTabsFloorList.length) {
      this.currentSelectedFloorObject = this.copyFromTabsFloorList[
        this.copyFromTabsFloorList.length - 1
      ];
      this.allFloorList = this.copyFromTabsFloorList;
      this.managerFloorList = this.allFloorList.slice();
      this.length = this.copyFromTabsFloorList.length;
      this.clickFloorIndexes = this.currentSelectedFloorObject.floorId;
    }

    if (this.allImageMap == null) {
      this.allImageMap = new Map();
      if (this.copyFromTabsLatestFloorImage) {
        for (let i = 0; i < this.copyFromTabsLatestFloorImage.length; i++) {
          let newImageObject = {};
          newImageObject.imgPath = this.copyFromTabsLatestFloorImage[i].imgPath;
          newImageObject.floorId = this.copyFromTabsLatestFloorImage[i].floorId;
          newImageObject.imgFileName = this.copyFromTabsLatestFloorImage[
            i
          ].imgFileName;

          this.allImageMap.set(newImageObject.floorId, newImageObject);
          this.currentFloorImageName = this.allImageMap.get(
            newImageObject.floorId
          ).imgFileName;
        }
      }

      if (this.copyFromTabsOtherFloorsImageList) {
        for (let i = 0; i < this.copyFromTabsOtherFloorsImageList.length; i++) {
          let newImageObject = {};
          newImageObject.imgPath = this.copyFromTabsOtherFloorsImageList[i].imgPath;
          newImageObject.floorId = this.copyFromTabsOtherFloorsImageList[
            i
          ].floorId;
          newImageObject.imgFileName = this.copyFromTabsOtherFloorsImageList[
            i
          ].imgFileName;
          this.allImageMap.set(newImageObject.floorId, newImageObject);
        }
      }
    }

    eventBus.$on("pushAllSeatMap", (allSeatMap) => {
      this.allSeatMap = allSeatMap;
    });

    eventBus.$on("pushFloorOfSeat", (floorId) => {
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
    eventBus.$off("pushAllSeatMap");
    eventBus.$off("pushFloorOfSeat");
  },
  methods: {
    showToolTip(floorObject) {
      if (this.allSeatMap) {
        if (this.allSeatMap.get(floorObject.floorId)) {
          if (this.allSeatMap.get(floorObject.floorId).length) {
            let eachFloorSeatList = this.allSeatMap.get(floorObject.floorId);
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
                floorObject.floorName +
                "층 <br>" +
                "전체 좌석 : " +
                currentFloorSeatsLength +
                "<br> 공석 : " +
                currentFloorVacantSeatsLength +
                text;
            }
          } else {
            this.toolTipText =
              floorObject.floorName + "층 <br>" + "좌석이 없습니다.";
          }
        } else {
          this.toolTipText =
            floorObject.floorName + "층 <br>" + "좌석이 없습니다.";
        }
      } else {
        this.toolTipText =
          floorObject.floorName + "층 <br>" + "좌석이 없습니다.";
      }
    },
    clickFloor(floorObject) {
      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(floorObject.floorId);

      if (this.allImageMap.get(floorObject.floorId)) {
        this.currentFloorImageName = this.allImageMap.get(
          floorObject.floorId
        ).imgFileName;
      } else {
        this.currentFloorImageName = "이미지를 업로드하세요";
      }

      this.currentSelectedFloorObject = floorObject;
      eventBus.$emit("pushSelectedFloorObject", floorObject);
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
      let newFloorObject = {};
      newFloorObject.floorId = this.createFloorUUID();
      newFloorObject.floorName = "";
      newFloorObject.buildingId = "HANCOM01";
      newFloorObject.floorOrder = this.allFloorList.length;
      newFloorObject.create = true;
      newFloorObject.modify = false;
      newFloorObject.delete = false;

      this.allFloorList.push(newFloorObject);
      this.managerFloorList.push(newFloorObject);

      this.currentSelectedFloorObject = newFloorObject;

      this.clickFloorIndexes = [];
      this.clickFloorIndexes.push(this.currentSelectedFloorObject.floorId);

      this.length++;

      eventBus.$emit(
        "pushSelectedFloorObject",
        this.currentSelectedFloorObject
      );

      let allFloorList = this.allFloorList.slice();
      eventBus.$emit("pushAllFloorList", allFloorList);

      let managerFloorList = this.managerFloorList.slice();
      eventBus.$emit("pushManagerFloorList", managerFloorList);
    },
    editFloorName() {
      const idx = this.allFloorList.findIndex((item) => {
        //console.log(typeof item.floorId);//String
        //console.log(typeof this.currentSelectedFloor.floorId); //String
        return item.floorId === this.currentSelectedFloorObject.floorId;
      });

      this.allFloorList[idx].modify = true;
      this.managerFloorList[idx].modify = true;

      eventBus.$emit(
        "pushChangedFloorName",
        this.currentSelectedFloorObject.floorName
      );

      let allFloorList = this.allFloorList.slice();
      eventBus.$emit("pushAllFloorList", allFloorList);

      let managerFloorList = this.managerFloorList.slice();
      eventBus.$emit("pushManagerFloorList", managerFloorList);
    },
    removeFloor() {
      if (this.length > 0) {
        let currentFloorId = this.currentSelectedFloorObject.floorId;
        const idx = this.allFloorList.findIndex(function (item) {
          //console.log(typeof item.floorId) //String
          //console.log(typeof currentFloorId) //String
          return item.floorId === currentFloorId; //String
        });
        if (idx > -1) {
          eventBus.$emit("pushDeletedFloorId", this.allFloorList[idx].floorId);
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;

          let nextIdx = null;
          console.log(typeof idx); //number
          console.log(typeof 0); //number
          if (idx === 0) {
            //number
            nextIdx = idx;
          } else {
            nextIdx = idx - 1;
          }

          eventBus.$emit("pushSelectedFloorObject", this.allFloorList[nextIdx]);

          let allFloorList = this.allFloorList.slice();
          eventBus.$emit("pushAllFloorList", allFloorList);

          let managerFloorList = this.managerFloorList.slice();
          eventBus.$emit("pushManagerFloorList", managerFloorList);

          this.currentSelectedFloorObject = this.allFloorList[nextIdx];
        }

        this.length--;

        if (this.length > 0) {
          this.clickFloorIndexes = [];
          this.clickFloorIndexes.push(this.currentSelectedFloorObject.floorId);
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
      let newImageObject = {};
      newImageObject.imgPath = file;
      newImageObject.floorId = this.currentSelectedFloorObject.floorId;
      newImageObject.imgFileName = file.name;

      this.allImageMap.set(
        this.currentSelectedFloorObject.floorId,
        newImageObject
      );
      this.currentFloorImageName = this.allImageMap.get(
        this.currentSelectedFloorObject.floorId
      ).imgFileName;

      eventBus.$emit("pushAllImageMap", this.allImageMap);
    },
  },
};
</script>

