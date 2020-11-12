<template>
  <div>
    <v-card>
      <v-tabs v-model="tab" background-color="black" dark height="65">
        <v-tab v-for="item in items" :key="item.index"> {{ item.title }}</v-tab>
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
  props: ["copyEmployee", "copyFloors"],
  components: {
    ManageSeats,
    ManageFloors,
    ManageSearch
  },
  data() {
    return {
      employees: this.copyEmployee,
      floors: this.copyFloors,
      eachEmployeeSeatMap: null,

      tab: null,
      items: [
        { title: "Floor", index: 0, content: "ManageFloors" },
        { title: "Seat", index: 1, content: "ManageSeats" },
        { title: "Search", index: 2, content: "ManageSearch" }
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