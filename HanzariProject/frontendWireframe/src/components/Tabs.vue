<template>
  <div>
    <v-card>
      <v-tabs v-model="tab" background-color="black" dark>
        <v-tab v-for="item in items" :key="item.index"> {{ item.title }}</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" :key="item.index">
          <v-card flat>
            <v-card-text>
              <component v-bind:is="item.content" 
              :copyEmployeeList="employees"
              :copyfloorList="floors" 
              ></component>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";

const portNum = 8081;
const host = "172.30.1.53"; //yj
const building_id = "HANCOM01";

import ManageSeats from "@/components/ManageSeats.vue";
import ManageFloors from "@/components/ManageFloors.vue";
import { eventBus } from '../main';
export default {
  props: ["copyEmployee"],
  components: {
    ManageSeats,
    ManageFloors,
  },
  data() {
    return {
      employees: this.copyEmployee,
      floors : this.copyFloors,
     
      tab: null,
      items: [
        { title: "Seat", index: 0, content: "ManageSeats" },
        { title: "Floor", index: 1, content: "ManageFloors" },
        { title: "Search", index: 2 },
        { title: "MyPage", index: 3 },
      ],
      
    };    
  },
};
</script>