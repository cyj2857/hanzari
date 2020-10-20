<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <v-btn text @click="removeFloor">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
      </v-card-text>
      <v-tabs v-model="floorNum" background-color="cyan" dark>
        <v-tab
          v-for="tab of this.allFloorItems"
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
      allFloorItems: this.copyFloors.sort(function (a, b) {
        // viewmodel(사본)
        return a.floor_index < b.floor_index
          ? -1
          : a.floor_index > b.floor_index
          ? 1
          : 0;
      }),
      length: this.copyFloors.length,
      initData: null,
      createFloorList: [], // DB에 새로 생긴 애들을 추가해주기 위함 post 저장 2순서 객체 가지고 있음
      deleteFloorList: [], // 삭제된 것 관리하기 위함. delete 삭제 3순서  객체의 floor_id 가지고 있음
    };
  },
  created() {
    //!! 처음 정의!!
    let allItems = this.allFloorItems;
    eventBus.$emit("allFloorItems", allItems);
    // 만약 처음에 null이라면
    // 층 없는 상태에서 자리 생성 exception 처리 위해 created에서 넘겨줌

    eventBus.$on("confirm", () => {
      this.confirmDialog();
    }),
      eventBus.$on("floorInfo", (floor) => {
        this.inputFloor = floor;
      }),
      eventBus.$on("showSeatFloor", (floor) => {
        this.seatFloor = floor;
        console.log(this.seatFloor + "가 넘어온 자리 층입니다");

        for (let i = 0; i < this.allFloorItems.length; i++) {
          if (this.seatFloor == this.allFloorItems[i].floor_name) {
            this.floorNum = i;
            this.setFloor(this.allFloorItems[this.floorNum].floor_name);
          }
        }
      });
  },
  beforeUpdate() {
    if (this.initData && this.length != 0) {
      //일단 한 층이 무조건 DB에 있다는 전제하에 돌아감
      this.setFloor(this.allFloorItems[this.floorNum].floor_name);
      return;
    } else {
      // 초기
      this.allFloorItems = this.copyFloors.sort(function (a, b) {
        return a.floor_index < b.floor_index
          ? -1
          : a.floor_index > b.floor_index
          ? 1
          : 0;
      });
      this.length = this.copyFloors.length;
      this.initData = "init";
    }
  },
  watch: {
    length(length) {
      this.floorNum = length - 1; // floor의 index가 되는

      let allItems = this.allFloorItems;
      eventBus.$emit("allFloorItems", allItems);

      let createFloorList = this.createFloorList
      let deleteFloorList = this.deleteFloorList

      eventBus.$emit("createFloorList", createFloorList)
      eventBus.$emit("deleteFloorList", deleteFloorList)

      console.log(this.createFloorList)
      console.log(this.deleteFloorList)
    },
  },
  methods: {
    setFloor(n) {
      eventBus.$emit("changeFloor", n);
    },
    getDialog() {
      eventBus.$emit("initFloor", null);
      this.dialogStatus = true;
      console.log(this.dialogStatus);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    confirmDialog() {
      console.log("<<<confirm dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
      console.log(this.inputFloor + "from add floor dialog");

      let newFloor = {};
      newFloor.floor_id = this.getFloorUUID();
      newFloor.floor_name = this.inputFloor;
      newFloor.building_id = "HANCOM01";
      newFloor.floor_index = this.allFloorItems.length;

      this.allFloorItems.push(newFloor);
      this.createFloorList.push(newFloor);
      this.increaseTab();
      console.log(this.length);
    },
    increaseTab() {
      this.length++;
      this.floorNum = this.length + 1;
      if (!this.dialogStatus && this.inputFloor) {
        this.setFloor(this.inputFloor);
      }
    },
    removeFloor() {
      if (this.length > 0) {
        //items에서 id가 현재 floor인 애 index 가져오기
        let currentFloorId = this.allFloorItems[this.floorNum].floor_name;
        const idx = this.allFloorItems.findIndex(function (item) {
          return item.floor_name == currentFloorId;
        });
        if (idx > -1) {
          this.deleteFloorList.push(this.allFloorItems[this.floorNum].floor_id);
          this.allFloorItems.splice(idx, 1);
        }
        //items에서 그 index 삭제
        this.decreaseTab();
      } else {
        alert("there are no seats to delete!");
      }
    },
    decreaseTab() {
      console.log(this.length);

      this.length--;
      this.floorNum = this.length - 1;
      if (this.length == 0) {
        this.setFloor(null);
      } else {
        this.setFloor(this.allFloorItems[this.floorNum].floor_name);
      }

      console.log(this.length);
      //pop
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