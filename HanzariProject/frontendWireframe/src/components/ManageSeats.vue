<template>
  <div>
    <v-card flat color="transparent" v-if="!mappingEmployeeComponentStatus">
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title
            ><v-icon large>event_seat</v-icon>
            <h3>공석 만들기</h3></v-card-title
          ></v-col
        >
        <v-col cols="12" sm="3">
          <v-switch
            v-model="addVacantSwitch"
            inset
            @change="changeSwitchStatus"
          ></v-switch
        ></v-col>

        <v-col
          v-for="size of this.sizeItems"
          :key="size.index"
          class="d-flex child-flex"
          cols="4"
        >
          <v-btn
            large
            :disabled="!addVacantSwitch"
            tile
            @click="clickSizeBtn(size.size)"
            >{{ size.size }}</v-btn
          ></v-col
        >
        <v-col>
          <v-card-text
            >현재 가로 길이 : {{ this.clickedSize.width }}</v-card-text
          ><v-card-text
            >현재 세로 길이 : {{ this.clickedSize.height }}</v-card-text
          ></v-col
        ><v-col
          ><v-btn
            text
            style="float: right"
            @click="getSeatSizeSetting"
            :disabled="!addVacantSwitch || !clickedSize"
          >
            <p class="font-italic">세부 설정</p>
          </v-btn></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="10" sm="6">
          <v-card-text>
            <v-btn color="blue lighten-3" @click="getMappingEmployeeComponent"
              ><h4><v-icon large>perm_identity</v-icon>사원 매핑하기</h4></v-btn
            ></v-card-text
          >
        </v-col>
        <v-col cols="10" sm="4">
          <v-card-text>
            <v-btn color="pink lighten-3" @click="clickChangeToVacant"
              ><h4><v-icon large>person_add_disabled</v-icon>자리 비우기</h4></v-btn
            ></v-card-text
          >
        </v-col>
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-card-title
        ><v-icon large>stairs</v-icon>
        <h3>층간 이동하기</h3></v-card-title
      >
      <v-row>
        <v-col cols="9">
          <v-combobox
            v-model="selectedFloorItems"
            :items="floorItems"
            label="층을 선택하세요"
            single-line
            outlined
            dense
          ></v-combobox>
        </v-col>
        <v-col cols="12" sm="3">
          <v-icon large @click="clickChangeFloorSeat">edit</v-icon></v-col
        >
      </v-row>
    </v-card>
    
    <MappingEmployee
      :copyFromManageSeatsEmployeeList="employee"
      v-if="mappingEmployeeComponentStatus && employee"
    />
    <SeatSizeSettingDialog
      :dialogStatus="this.seatSizeSettingDialogStatus"
      @close="closeSeatSizeSettingDialog"
    />
  </div>
</template>

<script>
import MappingEmployee from "@/components/MappingEmployee.vue";
import SeatSizeSettingDialog from "@/components/SeatSizeSettingDialog.vue";
import { eventBus } from "../main";
export default {
  name: "ManageSeats",
  props: ["copyFromTabsEmployeeList", "copyFromTabsFloorList"],
  components: {
    MappingEmployee,
    SeatSizeSettingDialog,
  },
  data() {
    return {
      employee: this.copyFromTabsEmployeeList,
      mappingEmployeeComponentStatus: false,

      sizeItems: [
        { index: 0, src: "../assets/rect1.png", size: 20 },
        { index: 1, src: "../assets/rect2.png", size: 30 },
        { index: 2, src: "../assets/rect3.png", size: 40 },
      ],
      floorItems: [],
      selectedFloorItems: null,

      
      addVacantSwitch: false,
      

      allFloorList: this.copyFromTabsFloorList,
      currentSelectedFloor: null,

      seatSizeSettingDialogStatus: false,

      clickedSize: { width: 0, height: 0 },
    };
  },
  created() {
    if (this.copyFromTabsFloorList && this.copyFromTabsFloorList.length) {
      this.currentSelectedFloor = this.allFloorList[
        this.allFloorList.length - 1
      ];

      for (let i = 0; i < this.copyFromTabsFloorList.length; i++) {
        if (
          this.currentSelectedFloor.floor_id == this.copyFromTabsFloorList[i].floor_id
        ) {
          continue;
        }

        this.floorItems.push(this.copyFromTabsFloorList[i].floor_name);
      }
    }

    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      this.floorItems = [];
      for (let i = 0; i < this.allFloorList.length; i++) {
        if (
          this.currentSelectedFloor.floor_id == this.allFloorList[i].floor_id
        ) {
          continue;
        }
        this.floorItems.push(this.allFloorList[i].floor_name);
      }
    });

    eventBus.$on("changeFloor", (floor) => {
      this.currentSelectedFloor = floor;
    });

    eventBus.$on(
      "mappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );

    eventBus.$on("changeSlider", (seatSize) => {
      this.clickedSize.width = seatSize.width;
      this.clickedSize.height = seatSize.height;

      this.confirmSeatSizeSettingDialog(seatSize);
    });
  },
  methods: {
    clickChangeFloorSeat() {
      if (this.selectedFloorItems) {
        eventBus.$emit("clickChangeFloorSeat", this.selectedFloorItems);
      }
    },
    getMappingEmployeeComponent() {
      this.mappingEmployeeComponentStatus = true;
    },
    changeSwitchStatus() {
      eventBus.$emit("changeAddVacantSwitch", this.addVacantSwitch);
    },
    getSeatSizeSetting() {
      eventBus.$emit("initSeatSizeSettingDialog", this.clickedSize);
      this.seatSizeSettingDialogStatus = true;
    },
    closeSeatSizeSettingDialog() {
      this.seatSizeSettingDialogStatus = false;
    },
    confirmSeatSizeSettingDialog(seatSize) {
      this.seatSizeSettingDialogStatus = false;

      eventBus.$emit("setSeatSizeDialog", seatSize);
    },
    clickSizeBtn(size) {
      let seatSize = {};

      seatSize.width = size;
      seatSize.height = size;
      this.clickedSize = seatSize;

      eventBus.$emit("setSeatSizeDialog", seatSize);
    },
    clickChangeToVacant() {
      eventBus.$emit("changeToVacant", true);
    },
  },
};
</script>