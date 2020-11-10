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
      <v-btn small
        ><v-icon dark @click="removeFloor">remove_circle</v-icon></v-btn
      >
      <v-btn small><v-icon dark @click="addFloor">add_circle</v-icon></v-btn>
      <v-divider class="mx-4"></v-divider>
      <v-card-title>FloorName</v-card-title>
      <v-row>
        <v-text-field
          v-model="currentSelectedFloor.floor_name"
          label="Enter FloorName"
          solo
        ></v-text-field
      ></v-row>
      <v-divider class="mx-4"></v-divider>
      <v-card-title> Vacant Seats </v-card-title>
      <v-card-title> All Seats </v-card-title>
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
      length: null,
      firstLoadWatch: null,
      floorName: null,

      allFloorList: this.copyfloorList, // 여기에서 sort 안먹음
      managerFloorList: [], // DB에 save 할 리스트

      currentSelectedFloor: this.copyfloorList[0],
    };
  },
  created() {
    this.managerFloorList = this.allFloorList.slice();
    this.length = this.copyfloorList.length;
  },
  methods: {
    createFloorUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    clickFloor(floor) {
      this.currentSelectedFloor = floor;
      eventBus.$emit("changeFloor", floor);
      console.log(floor);
    },
    removeFloor() {
      if (this.length > 0) {
        //items에서 id가 현재 floor인 애 index 가져오기
        let currentFloorId = this.currentSelectedFloor.floor_id;
        const idx = this.allFloorList.findIndex(function (item) {
          return item.floor_id == currentFloorId;
        });
        if (idx > -1) {
          eventBus.$emit("deleteSeatListKey", this.allFloorList[idx].floor_id);

          // 삭제 가능
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;
          //items에서 그 index 삭제
        }
        this.length--;

        console.log(this.length + " length");
      } else {
        alert("there are no seats to delete!");
      }
    },
    addFloor() {
      let newFloor = {};
      newFloor.floor_id = this.createFloorUUID();
      newFloor.floor_name = "";
      newFloor.building_id = "HANCOM01";
      newFloor.floor_order = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

      this.length++;
    },
    editFloorName() {},
  },
};
</script>

