<template>
  <div>
    <v-card flat color="transparent">
      <v-card-title>Floor</v-card-title>
      <v-row>
        <v-col
          v-for="floor of this.allFloorList"
          :key="floor.floor_id"
          class="d-flex child-flex"
          cols="4"
        >
          <v-btn large @click="clickFloor(floor)">{{ floor.floor_name }}</v-btn>
        </v-col>
      </v-row>
      <v-btn small><v-icon dark>remove_circle</v-icon></v-btn>
      <v-btn small><v-icon dark>add_circle</v-icon></v-btn>
      <v-divider class="mx-4"></v-divider>
      <v-card-title>FloorName</v-card-title>
      <v-text-field
        v-model="floorName"
        label="Enter FloorName"
        solo
      ></v-text-field>
    </v-card>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import ManageAddFloors from "@/components/ManageAddFloors.vue";
export default {
  props: ["copyfloorList"],
  components: {},
  data() {
    return {
      floorNum: null, //v-tabs v-model
      length: null,
      firstLoadWatch: null,
      floorName: null,

      allFloorList: this.copyfloorList, // ���⿡�� sort �ȸ���
      managerFloorList: [], // DB�� save �� ����Ʈ
    };
  },
  created() {
    this.managerFloorList = this.allFloorList.slice();
    this.length = this.copyfloorList.length;
  },
  methods: {
    clickFloor(floor) {
      eventBus.$emit("currentSelectedFloor", floor); //attachCanvas
      eventBus.$emit("currentSelectedFloorToManageSeats", floor); //ManageSeats

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);
      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    setFloor(floor) {
      eventBus.$emit("changeFloor", floor);
      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);
      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
  },
};
</script>