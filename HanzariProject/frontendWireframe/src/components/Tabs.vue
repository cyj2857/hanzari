<template>
  <div>
    <v-toolbar color="black" dark> </v-toolbar>
    <v-tabs
      v-model="tab"
      background-color="black"
      dark
      height="65"
      horizontal
      grow
    >
      <v-tabs-slider color="blue"></v-tabs-slider>
      <v-tab v-for="item in items" :key="item.index">
        <v-icon large dark>{{ item.icon }}</v-icon></v-tab
      >
    </v-tabs>

    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item.index">
        <v-card flat>
          <v-card-text>
            <component
              v-bind:is="item.content"
              :copyFromTabsEmployeeList="employeeList"
              :copyFromTabsDepartmentList="departmentList"
              :copyFromTabsFloorList="floors"
              :eachEmployeeSeatMap="eachEmployeeSeatMap"
              :copyFromTabsLatestFloorImage="latestFloorImage"
              :copyFromTabsOtherFloorsImageList="otherFloorsImageList"
              :copyFromTabsLatestFloorSeatList="latestFloorSeatList"
              :copyFromTabsOtherFloorSeatMap="otherFloorSeatMap"
            ></component>
          </v-card-text>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script>
import { eventBus } from "../main";
import ManageSeats from "@/components/ManageSeats.vue";
import ManageFloors from "@/components/ManageFloors.vue";
import ManageSearch from "@/components/ManageSearch.vue";

export default {
  props: [
    "copyEmployeeList",
    "copyDepartmentList",
    "copyFloorList",
    "copyLatestFloorImage",
    "copyOtherFloorsImageList",
    "copyLatestFloorSeatList",
    "copyOtherFloorsSeatMap",
  ],
  components: {
    ManageSeats,
    ManageFloors,
    ManageSearch,
  },
  data() {
    return {
      tab: null,

      employeeList: this.copyEmployeeList,
      departmentList: this.copyDepartmentList,
      floors: this.copyFloorList,

      eachEmployeeSeatMap: null,

      latestFloorImage: this.copyLatestFloorImage,
      otherFloorsImageList: this.copyOtherFloorsImageList,

      latestFloorSeatList: this.copyLatestFloorSeatList,

      otherFloorSeatMap: this.copyOtherFloorsSeatMap,

      items: [
        { icon: "stairs", title: "층 설정", index: 0, content: "ManageFloors" },
        {
          icon: "event_seat",
          title: "좌석 설정",
          index: 1,
          content: "ManageSeats",
        },
        { icon: "search", index: 2, title: "검색", content: "ManageSearch" },
      ],
    };
  },
  created() {
    eventBus.$on("pushEachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.eachEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  beforeDestroy() {
    eventBus.$off("pushEachEmployeeSeatMap");
  },
};
</script>