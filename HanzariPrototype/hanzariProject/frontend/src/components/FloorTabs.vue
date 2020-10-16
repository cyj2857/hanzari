<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <v-btn text @click="removeFloor">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
      </v-card-text>
      <v-tabs v-model="floorNum" background-color="cyan" dark>
        <v-tab
          v-for="tab of this.floors"
          :key="tab.floor_name"
          @change="setFloor(tab.floor_name)"
        >
          {{ tab.floor_name }}</v-tab
        >
      </v-tabs>
    </v-card>
    <AddFloorDialog
      :dialogStatus="this.dialogStatus"
      @close="closeDialog"
    ></AddFloorDialog>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import AddFloorDialog from "@/components/AddFloorDialog.vue";
export default {
  props: ["floor"],
  components: {
    AddFloorDialog,
  },
  data() {
    return {
      length: 3,
      tab: null,
      floorNum: null,
      dialogStatus: false,
      inputFloor: null,
      seatFloor: null,
      floors: this.floor,
      initData: null,
    };
  },
  created() {
    eventBus.$on("confirm", () => {
      this.confirmDialog();
    }),
      eventBus.$on("floorInfo", (floor) => {
        this.inputFloor = floor;
      }),
      eventBus.$on("showSeatFloor", (floor) => {
        this.seatFloor = floor;
        console.log(this.seatFloor + "가 넘어온 자리 층입니다");

        for (let i = 0; i < this.floors.length; i++) {
          if (this.seatFloor == this.floors[i].floor_name) {
            this.floorNum = i;
            this.setFloor(this.floors[this.floorNum].floor_name);
          }
        }
      });

    let allItems = this.floors;
    eventBus.$emit("allFloorItems", allItems);
  },
  beforeUpdate() {
    if (this.initData) {
      return;
    } else {
      this.floorNum = 0;
      this.setFloor(this.floors[this.floorNum].floor_name);
      this.initData = "yes";
    }
  },
  watch: {
    length(val) {
      let allItems = this.floors;
      
      this.floorNum = val - 1;

      eventBus.$emit("allFloorItems", allItems);
    },
  },
  methods: {
    decreaseTab() {
      this.length--;
      this.floorNum = this.length - 1;
      this.setFloor(this.floors[this.floorNum].floor_name);
      //pop
    },
    increaseTab() {
      this.length++;
      this.floorNum = this.length + 1;
      this.setFloor(this.floors[this.floorNum].floor_name);
      //push
    },
    getDialog() {
      eventBus.$emit("initFloor", null);
      this.dialogStatus = true;
      console.log(this.dialogStatus);
    },
    confirmDialog() {
      console.log("<<<confirm dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
      console.log(this.inputFloor + "from add floor dialog");
      this.floors.push({floor_name: this.inputFloor, building_id: "HANCOM01", floor_index: this.floors.length});

      this.increaseTab();
      console.log(this.length);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    removeFloor() {
      //items에서 id가 현재 floor인 애 index 가져오기
      let currentFloorId = this.floors[this.floorNum].floor_name;
      const idx = this.floors.findIndex(function (item) {
        return item.floor_name == currentFloorId;
      });
      if (idx > -1) this.floors.splice(idx, 1);

      //items에서 그 index 삭제
      this.decreaseTab();
    },
    setFloor(n) {
      eventBus.$emit("changeFloor", n);
    },
    getFloorName(floorNum) {
      return this.floors[floorNum].floor_name;
    },
  },
};
</script>