<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <v-btn text @click="removeFloor">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text>Edit Floor name</v-btn>
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
  props: ["copyFloors"],
  components: {
    AddFloorDialog,
  },
  data() {
    return {
      floorNum: null, //v-tabs v-model
      dialogStatus: false,
      inputFloor: null,
      seatFloor: null,
      floors: this.copyFloors.sort(function (a, b) {
        return a.floor_index < b.floor_index
          ? -1
          : a.floor_index > b.floor_index
          ? 1
          : 0;
      }),
      length: this.copyFloors.length,
      initData: null,
    };
  },
  created() {
    //!! 처음 정의!!
    let allItems = this.floors;
    eventBus.$emit("allFloorItems", allItems);
    // 만약 처음에 null이라면 null 인걸 canvas도 알아야 exception 처리가능해서 created에서 넘겨줌

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
  },
  beforeUpdate() {
    if (this.initData && this.length != 0) {
      //일단 한 층이 무조건 DB에 있다는 전제하에 돌아감
      this.setFloor(this.floors[this.floorNum].floor_name);
      return;
    } else {
      // 초기
      this.floors = this.copyFloors.sort(function (a, b) {
        return a.floor_index < b.floor_index
          ? -1
          : a.floor_index > b.floor_index
          ? 1
          : 0;
      });

      this.length = this.copyFloors.length;
      this.initData = "yes";
    }
  },
  watch: {
    length(length) {
      let allItems = this.floors;

      this.floorNum = length - 1; // floor의 index가 되는

      eventBus.$emit("allFloorItems", allItems);
      console.log(this.length);
    },
  },
  methods: {
    decreaseTab() {
      console.log(this.length);

      this.length--;
      this.floorNum = this.length - 1;
      if (this.length == 0) {
        this.setFloor(null);
      } else {
        this.setFloor(this.floors[this.floorNum].floor_name);
      }

      console.log(this.length);
      //pop
    },
    increaseTab() {
      this.length++;
      this.floorNum = this.length + 1;
      if (!this.dialogStatus && this.inputFloor) {
        this.setFloor(this.inputFloor);
      }
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
      this.floors.push({
        floor_id: this.getFloorUUID(),
        floor_name: this.inputFloor,
        building_id: "HANCOM01",
        floor_index: this.floors.length,
      });

      this.increaseTab();
      console.log(this.length);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    removeFloor() {
      if (this.length > 0) {
        //items에서 id가 현재 floor인 애 index 가져오기
        let currentFloorId = this.floors[this.floorNum].floor_name;
        const idx = this.floors.findIndex(function (item) {
          return item.floor_name == currentFloorId;
        });
        if (idx > -1) this.floors.splice(idx, 1);

        //items에서 그 index 삭제
        this.decreaseTab();
      } else {
        alert("there are no seats to delete!");
      }
    },
    setFloor(n) {
      eventBus.$emit("changeFloor", n);
    },
    getFloorUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
  },
};
</script>