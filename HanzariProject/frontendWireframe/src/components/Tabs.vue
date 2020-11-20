<template>
  <div>
    <v-toolbar color="black" dark> </v-toolbar>
      <v-tabs v-model="tab" background-color="black" dark height="65" horizontal grow>
          <v-tabs-slider color="blue"></v-tabs-slider>
        <v-tab  v-for="item in items" :key="item.index">
          <v-icon large dark>{{ item.icon }}</v-icon></v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" :key="item.index">
          <v-card flat>
            <v-card-text>
              <component
                v-bind:is="item.content"
                :copyEmployeeList="employees"
                :copyfloorList="floors"
                :eachEmployeeSeatMap="eachEmployeeSeatMap"
                :copyLatestFloorImage="latestFloorImage"
                :copyOtherFloorsImageList="otherFloorsImageList"
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
    "copyEmployee",
    "copyFloors",
    "copyLatestFloorImage",
    "copyOtherFloorsImageList",
  ],
  components: {
    ManageSeats,
    ManageFloors,
    ManageSearch,
  },
  data() {
    return {
      tab: null,

      employees: this.copyEmployee,
      floors: this.copyFloors,

      eachEmployeeSeatMap: null,

      latestFloorImage: this.copyLatestFloorImage,
      otherFloorsImageList: this.copyOtherFloorsImageList,

      items: [
        { icon: "stairs", title :"층 설정",index: 0, content: "ManageFloors" },
        { icon: "event_seat",title :"좌석 설정", index: 1, content: "ManageSeats" },
        { icon: "search", index: 2,title :"검색", content: "ManageSearch" },
      ],
    };
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.eachEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
};
</script>