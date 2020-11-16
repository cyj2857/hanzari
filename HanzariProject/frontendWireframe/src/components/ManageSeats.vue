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

        <v-col class="d-flex child-flex" cols="4"
          ><v-btn tile :disabled="!addVacantSwitch" @click="clickBtn1"
            >20</v-btn
          ></v-col
        >
        <v-col class="d-flex child-flex" cols="4"
          ><v-btn tile :disabled="!addVacantSwitch" @click="clickBtn2"
            >30</v-btn
          ></v-col
        >
        <v-col class="d-flex child-flex" cols="4"
          ><v-btn tile :disabled="!addVacantSwitch" @click="clickBtn3"
            >40</v-btn
          ></v-col
        ><v-col
          ><v-btn
            text
            style="float: right"
            @click="getSeatSizeSetting"
            :disabled="!addVacantSwitch"
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

      <v-card-title
        ><v-icon large>event_seat</v-icon>좌석 번호 부여하기</v-card-title
      >
      <v-row
        ><v-col cols="12" sm="9"
          ><v-text-field
            v-model="seatName"
            label="좌석 번호를 입력하세요."
            solo
          ></v-text-field
        ></v-col>
        <v-col cols="12" sm="3">
          <v-icon large @click="inputSeatName">edit</v-icon></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-card-title><v-icon large>stairs</v-icon>층간 이동하기</v-card-title
      ><v-row
        ><v-col cols="12" sm="9"
          ><v-text-field
            v-model="changeFloor"
            label="이동할 층을 입력하세요."
            solo
          ></v-text-field
        ></v-col>
        <v-col cols="12" sm="3">
          <v-icon large @click="clickChangeFloor">edit</v-icon></v-col
        >
      </v-row>
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
      employee: this.copyEmployeeList,
      addVacantSwitch: false, // ���� ����� ���� ����ġ ����
      mappingEmployeeComponentStatus: false,

      currentSelectedFloorId: null,
      allFloorList: this.copyfloorList,
      seatName: null,
      changeFloor: null,

      seatSizeSettingDialogStatus: false,

      clickedSize: null,
      clickIndexes: null,
    };
  },
  created() {
    if (this.copyfloorList.length) {
      this.currentSelectedFloorId = this.allFloorList[
        this.allFloorList.length - 1
      ].floor_id;
    } else {
      this.currentSelectedFloorId = null;
    }

    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
    });

    eventBus.$on("changeFloor", (floor) => {
      if (floor) {
        // null 이 아닐때
        this.currentSelectedFloorId = floor.floor_id;
      } else {
        this.currentSelectedFloorId = null;
      }
    });
    eventBus.$on(
      "mappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );
  },
  methods: {
    inputSeatName() {
      if (this.seatName) {
        eventBus.$emit("inputSeatName", this.seatName);
      }
    },
    clickChangeFloor() {
      if (this.changeFloor) {
        eventBus.$emit("clickChangeFloor", this.changeFloor);
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
    clickBtn1() {
      this.clickedSize = 20;

      this.clickIndexes = [];
      this.clickIndexes.push();
      eventBus.$emit("setSeatSize", this.clickedSize);
    },
    clickBtn2() {
      this.clickedSize = 30;
      this.clickIndexes = [];
      this.clickIndexes.push();
      eventBus.$emit("setSeatSize", this.clickedSize);
    },
    clickBtn3() {
      this.clickedSize = 40;
      this.clickIndexes = [];
      this.clickIndexes.push();
      eventBus.$emit("setSeatSize", this.clickedSize);
    },
  },
};
</script>