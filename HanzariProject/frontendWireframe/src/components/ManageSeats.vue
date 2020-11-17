<template>
  <div>
    <v-card flat color="transparent" v-if="!mappingEmployeeComponentStatus">
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title
            ><v-icon large>event_seat</v-icon>공석 만들기</v-card-title
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
            세부 설정
          </v-btn></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="12">
          <v-card-title
            ><v-icon large>perm_identity</v-icon>사원 매핑하기</v-card-title
          >
          <v-card-text>
            <v-btn @click="getMappingEmployeeComponent"
              >사원 매핑하기</v-btn
            ></v-card-text
          ></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>
      
      <v-card-title><v-icon large>stairs</v-icon>층간 이동하기</v-card-title>
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
          <v-icon large @click="clickChangeFloor">edit</v-icon></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="12">
          <v-card-title
            ><v-icon large>person_add_disabled</v-icon>자리 비우기
            <v-card-text>
              <v-btn @click="clickChangeToVacant"
                >자리 비우기</v-btn
              ></v-card-text
            ></v-card-title
          ></v-col
        ></v-row
      >
    </v-card>
    <MappingEmployee
      :copyEmployeeListTwo="employee"
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
  props: ["copyEmployeeList", "copyfloorList"],
  components: {
    MappingEmployee,
    SeatSizeSettingDialog,
  },
  data() {
    return {
      sizeItems: [
        { index: 0, src: "../assets/rect1.png", size: 20 },
        { index: 1, src: "../assets/rect2.png", size: 30 },
        { index: 2, src: "../assets/rect3.png", size: 40 },
      ],
      floorItems: [],
      selectedFloorItems : null,

      employee: this.copyEmployeeList,
      addVacantSwitch: false,
      mappingEmployeeComponentStatus: false,

      currentSelectedFloorId: null,
      allFloorList: this.copyfloorList,
      //seatName: null,
      //changeFloor: null,

      seatSizeSettingDialogStatus: false,

      clickedSize: { width: 0, height: 0 },
      clickIndexes: null,
    };
  },
  created() {
    for (let i = 0; i < this.copyfloorList.length; i++) {
      let floor_name = this.copyfloorList[i].floor_name;
      this.floorItems.push(floor_name)
    }

    if (this.copyfloorList.length) {
      this.currentSelectedFloorId = this.allFloorList[
        this.allFloorList.length - 1
      ].floor_id;
    } else {
      this.currentSelectedFloorId = null;
    }

    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      this.floorItems.push(this.allFloorList);
    });

    /*eventBus.$on("changeFloor", (floor) => {
      if (floor) {
        // null 이 아닐때
        this.currentSelectedFloorId = floor.floor_id;
      } else {
        this.currentSelectedFloorId = null;
      }
    });*/
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
    clickChangeFloor() {
      if (this.selectedFloorItems) {
        eventBus.$emit("clickChangeFloor", this.selectedFloorItems);
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