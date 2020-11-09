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
              <component
                v-bind:is="item.content" v-if="floors" v-bind:copyFloors="floors"
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
export default {
  components: {
    ManageSeats,
    ManageFloors,
  },
  data() {
    return {
      tab: null,
      items: [
        { title: "Seat", index: 0, content: "ManageSeats" },
        { title: "Floor", index: 1, content: "ManageFloors" },
        { title: "Search", index: 2 },
        { title: "MyPage", index: 3 },
      ],
      floors: null,
    };    
  },
  async created() {
    this.floors = await this.getFloors();
  },
  methods: {
    async getFloors() {
      let allFloorList = new Array();
      try {
        let response = await axios.get(
          "http://" +
            host +
            ":" +
            portNum +
            "/api/buildings/" +
            building_id +
            "/floors"
        );
        for (var i = 0; i < response.data.length; i++) {
          let newFloor = {};
          newFloor.floor_id = response.data[i].floor_id;
          newFloor.floor_name = response.data[i].floor_name;
          newFloor.building_id = response.data[i].building_id;
          newFloor.floor_order = response.data[i].floor_order;
          newFloor.create = false;
          newFloor.modify = false;
          newFloor.delete = false;

          allFloorList.push(newFloor);
        }

        allFloorList.sort(function (a, b) {
          return a.floor_order < b.floor_order
            ? -1
            : a.floor_order > b.floor_order
            ? 1
            : 0;
        });

        for (let i = 0; i < allFloorList.length; i++) {
          this.floorIdList.push(allFloorList[i].floor_id);
        }
        this.currentFloorId = this.floorIdList.slice(-1)[0];
      } catch (error) {
        console.log(error);
      }
      return allFloorList;
    },
  },
};
</script>