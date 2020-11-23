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
            v-model="addVacantSwitchStatus"
            inset
            @change="changeAddVacantSwitchStatus"
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
            <v-btn color="pink lighten-3" @click="clickChangeSeatToVacant"
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

      addVacantSwitchStatus: false,

      allFloorList: this.copyFromTabsFloorList,
      currentSelectedFloorObject: null,
    };
  },
  created() {
    if (this.copyFromTabsFloorList && this.copyFromTabsFloorList.length) {
      this.currentSelectedFloorObject = this.allFloorList[
        this.allFloorList.length - 1
      ];

      for (let i = 0; i < this.copyFromTabsFloorList.length; i++) {
        //console.log(typeof this.currentSelectedFloor.floorId);//String
        //console.log(typeof this.copyFromTabsFloorList[i].floorId); //String
        if (
          this.currentSelectedFloorObject.floorId ===
          this.copyFromTabsFloorList[i].floorId

        ) {
          continue;
        }

        this.floorItems.push(this.copyFromTabsFloorList[i]);
      }
    }

    eventBus.$on("allFloorList", (allFloorList) => {
      this.allFloorList = allFloorList;
      this.initFloorItems();
    });

    eventBus.$on("pushSelectedFloorObject", (floorObject) => {
      this.currentSelectedFloorObject = floorObject;
      this.initFloorItems();
    });

    eventBus.$on(
      "pushMappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );
  },
  beforeDestroy() {
    eventBus.$off("allFloorList");
    eventBus.$off("pushSelectedFloorObject");
    eventBus.$off("pushMappingEmployeeComponentStatus");
  },
  methods: {
    initFloorItems() {
      this.floorItems = [];
      for (let i = 0; i < this.allFloorList.length; i++) {

        //console.log(typeof this.currentSelectedFloorObject.floorId); //String
        //console.log(typeof this.allFloorList[i].floorId);//String
 
        if (this.currentSelectedFloorObject.floorId === this.allFloorList[i].floorId) {

          continue;
        }

        this.floorItems.push(this.allFloorList[i]);
      }
    },
    changeFloorSeat() {
      if (this.selectedFloorItemsId) {
        eventBus.$emit("moveSeatToAnotherFloor", this.selectedFloorItemsId);
      } else {
        alert("이동할 층을 선택하지 않았습니다.");
      }
    },
    getMappingEmployeeComponent() {
      this.mappingEmployeeComponentStatus = true;
    },
    changeAddVacantSwitchStatus() {
      eventBus.$emit("pushAddVacantSwitchStatus", this.addVacantSwitchStatus);
    },
    clickChangeSeatToVacant() {
      eventBus.$emit("changeSeatToVacant");
    },
  },
};
</script>