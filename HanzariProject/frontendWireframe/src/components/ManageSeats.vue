<template>
  <div>
    <v-card flat color="transparent" v-if="!mappingEmployeeComponentStatus">
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title
            ><v-icon large >event_seat</v-icon>공석 만들기</v-card-title
          ></v-col
        >
        <v-col cols="12" sm="3">
          <v-switch
            v-model="addVacantSwitch"
            inset
            @change="changeSwitchStatus"
          ></v-switch
        ></v-col>
        <v-col cols="12"
          ><v-slider
            v-if="addVacantSwitch"
            v-model="slider"
            class="align-center"
            thumb-label="always"
            :max="max"
            :min="min"
            hide-details
            @change="changeSliderValue"
          >
          </v-slider
        ></v-col>
      </v-row>

      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="12">
          <v-card-title
            ><v-icon large >perm_identity</v-icon>사원 매핑하기</v-card-title
          >
          <v-card-text>
            <v-btn @click="getMappingEmployeeComponent"
              >사원 매핑하기</v-btn
            ></v-card-text
          ></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-card-title><v-icon large >event_seat</v-icon>좌석 번호 부여하기</v-card-title>
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
  </div>
</template>

<script>
import MappingEmployee from "@/components/MappingEmployee.vue";
import { eventBus } from "../main";
export default {
  name: "ManageSeats",
  props: ["copyEmployeeList", "copyfloorList"],
  components: {
    MappingEmployee,
  },
  data() {
    return {
      employee: this.copyEmployeeList,
      addVacantSwitch: false, // ���� ����� ���� ����ġ ����
      min: 1,
      max: 50,
      slider: 25,
      mappingEmployeeComponentStatus: false,

      currentSelectedFloorId: null,
      allFloorList: this.copyfloorList,
      seatName: null,
      changeFloor: null,
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
      //console.log(this.allFloorList);
    });

    eventBus.$on("changeFloor", (floor) => {
      //console.log(floor);
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
    //editSeatName() {
    //  console.log(this.seatName);
    //  eventBus.$emit("inputSeatName", this.seatName);
    //},
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
      eventBus.$emit("changeslider", this.slider);
    },
    changeSliderValue() {
      eventBus.$emit("changeslider", this.slider);
    },
  },
};
</script>