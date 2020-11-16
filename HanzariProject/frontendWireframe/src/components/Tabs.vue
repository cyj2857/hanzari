<template>
  <div>
    <v-toolbar color="black" dark> </v-toolbar>
    <v-card height="800px">
      <v-tabs v-model="tab" background-color="black" dark height="65">
          <v-tabs-slider color="blue"></v-tabs-slider>
        <v-tab  v-for="item in items" :key="item.index"><v-icon large dark>{{ item.icon }}</v-icon>{{item.title}} </v-tab>
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
                :copyLatestFloorImage="myLatestFloorImage"
                :copyOtherFloorsImageList="myOtherFloorsImageList"
              ></component>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
import ManageSeats from "@/components/ManageSeats.vue";
import ManageFloors from "@/components/ManageFloors.vue";
import ManageSearch from "@/components/ManageSearch.vue";
import { eventBus } from "../main";
export default {
  props: [
    "copyEmployee",
    "copyFloors",
    "latestFloorImage",
    "otherFloorsImageList",
  ],
  components: {
    ManageSeats,
    ManageFloors,
    ManageSearch,
  },
  data() {
    return {
      employees: this.copyEmployee,
      floors: this.copyFloors,
      eachEmployeeSeatMap: null,

      myLatestFloorImage: this.latestFloorImage,
      myOtherFloorsImageList: this.otherFloorsImageList,

      tab: null,
      items: [
        { icon: "stairs", title :"층설정",index: 0, content: "ManageFloors" },
        { icon: "event_seat",title :"좌석설정", index: 1, content: "ManageSeats" },
        { icon: "search", index: 2,title :"검색", content: "ManageSearch" },
      ],
    };
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.eachEmployeeSeatMap = eachEmployeeSeatMap;
      console.log(this.eachEmployeeSeatMap);
    });
  },
};
</script>