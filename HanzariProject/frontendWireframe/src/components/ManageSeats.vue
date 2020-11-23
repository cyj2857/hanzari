<template>
  <div>
    <v-card flat color="transparent" v-if="!mappingEmployeeComponentStatus">
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title
            ><v-icon large>event_seat</v-icon>
            <h3>좌석 만들기</h3></v-card-title
          ></v-col
        >
        <v-col cols="12" sm="3">
          <v-switch
            v-model="addVacantSwitch"
            inset
            @change="changeSwitchStatus"
          ></v-switch
        ></v-col>
      </v-row>

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
              ><h4>
                <v-icon large>person_add_disabled</v-icon>자리 비우기
              </h4></v-btn
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
          <v-select
            :items="floorItems"
            item-value="floorId"
            item-text="floorName"
            v-model="selectedFloorItemsId"
            chips
            label="층을 선택하세요"
            single-line
            outlined
            dense
            no-data-text="이동할 층이 없습니다."
          ></v-select>
        </v-col>
        <v-col cols="12" sm="3">
          <v-icon large @click="changeFloorSeat">edit</v-icon></v-col
        >
      </v-row>
    </v-card>

    <MappingEmployee
      :copyFromManageSeatsEmployeeList="employee"
      v-if="mappingEmployeeComponentStatus && employee"
    />
  </div>
</template>

<script>
import MappingEmployee from "@/components/MappingEmployee.vue";
import { eventBus } from "../main";
export default {
  name: "ManageSeats",
  props: ["copyFromTabsEmployeeList", "copyFromTabsFloorList"],
  components: {
    MappingEmployee,
  },
  data() {
    return {
      employee: this.copyFromTabsEmployeeList,
      mappingEmployeeComponentStatus: false,

      floorItems: [],
      selectedFloorItemsId: null,

      addVacantSwitch: false,

      allFloorList: this.copyFromTabsFloorList,
      currentSelectedFloor: null,
    };
  },
  created() {
    if (this.copyFromTabsFloorList && this.copyFromTabsFloorList.length) {
      this.currentSelectedFloor = this.allFloorList[
        this.allFloorList.length - 1
      ];

      for (let i = 0; i < this.copyFromTabsFloorList.length; i++) {
        //console.log(typeof this.currentSelectedFloor.floorId);//String
        //console.log(typeof this.copyFromTabsFloorList[i].floorId); //String
        if (
          this.currentSelectedFloor.floorId ===
          this.copyFromTabsFloorList[i].floorId

        ) {
          continue;
        }

        this.floorItems.push(this.copyFromTabsFloorList[i]);
      }
    }

    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      this.initFloorItems();
    });

    eventBus.$on("changeFloor", (floor) => {
      this.currentSelectedFloor = floor;
      this.initFloorItems();
    });

    eventBus.$on(
      "mappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );
  },
  beforeDestroy() {
    eventBus.$off("allFloorList");
    eventBus.$off("changeFloor");
    eventBus.$off("mappingEmployeeComponentStatus");
  },
  methods: {
    initFloorItems() {
      this.floorItems = [];
      for (let i = 0; i < this.allFloorList.length; i++) {

        //console.log(typeof this.currentSelectedFloor.floorId); //String
        //console.log(typeof this.allFloorList[i].floorId);//String
 
        if (this.currentSelectedFloor.floorId === this.allFloorList[i].floorId) {

          continue;
        }

        this.floorItems.push(this.allFloorList[i]);
      }
    },
    changeFloorSeat() {
      if (this.selectedFloorItemsId) {
        eventBus.$emit("changeFloorSeat", this.selectedFloorItemsId);
      } else {
        alert("이동할 층을 선택하지 않았습니다.");
      }
    },
    getMappingEmployeeComponent() {
      this.mappingEmployeeComponentStatus = true;
    },
    changeSwitchStatus() {
      eventBus.$emit("changeAddVacantSwitch", this.addVacantSwitch);
    },
    clickChangeToVacant() {
      eventBus.$emit("changeToVacant");
    },
  },
};
</script>