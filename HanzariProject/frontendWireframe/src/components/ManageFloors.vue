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
          <v-btn large @click="clickFloor(floor.floor_id)">{{
            floor.floor_name
          }}</v-btn>
        </v-col>
      </v-row>
      <v-btn small
        ><v-icon dark>remove_circle</v-icon></v-btn
      >
      <v-btn small
        ><v-icon dark>add_circle</v-icon></v-btn
      >
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

      allFloorList: this.copyfloorList, // 여기에서 sort 안먹음
      managerFloorList: [], // DB에 save 할 리스트
    };
  },
  created() {
    this.managerFloorList = this.allFloorList.slice();
    this.length = this.copyfloorList.length;
  },
  methods: {
    clickFloor(floor_id) {},
  },
};
</script>