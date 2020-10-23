<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <v-btn text @click="removeFloor">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <!-- <v-btn text @click="changeFloorName">Change Floor Name</v-btn> 추후에 구현 예정-->
      </v-card-text>
      <v-tabs v-model="floorNum" background-color="cyan" dark>
        <v-tab
          v-for="tab of this.allFloorList"
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
      allFloorList: this.copyFloors, // 여기에서 sort 안먹음
      managerFloorList: [], // DB에 save 할 리스트
      length: null,
      initData: null,
      firstLoadWatch: null,
    };
  },
  created() {
    if (this.copyFloors.length == 0) {
      /* 층 없는 상태에서 자리 생성 막기위해 넘겨줌
       이 경우 길이가 늘어나지 않으므로 watch에서 불리지 않음
       그래서 created에서 불러주기*/
      let allFloors = this.allFloorList;
      eventBus.$emit("allFloorList", allFloors);
    }

    eventBus.$on("confirm", () => {
      this.confirmDialog();
    }),
      eventBus.$on("floorInfo", (floor) => {
        this.inputFloor = floor;
      }),
      eventBus.$on("showSeatFloor", (floor) => {
        this.seatFloor = floor;
        console.log(this.seatFloor + "가 넘어온 자리 층입니다");
        for (let i = 0; i < this.allFloorList.length; i++) {
          if (this.seatFloor == this.allFloorList[i].floor_name) {
            this.floorNum = i;
            this.setFloor(this.allFloorList[this.floorNum].floor_name);
          }
        }
      });
  },
  beforeUpdate() {
    // 실제로 렌더링되기 전에 컴포넌트에서 반응 데이터의 
    // 새로운 상태를 가져와야하는 경우 사용
    if (this.initData && this.length != 0) {
      this.setFloor(this.allFloorList[this.floorNum].floor_name);
      return;
    } else {
      // 초기
      this.allFloorList = this.copyFloors.sort(function (a, b) {
        return a.floor_order < b.floor_order
          ? -1
          : a.floor_order > b.floor_order
          ? 1
          : 0;
      });
      this.length = this.copyFloors.length;
      this.initData = true;
    }
  },
  watch: {
    length(length) {
      /* DB에서 로드해올때도 length가 늘어나기 때문에 watch가 불림
      층을 삭제하는 경우에도 managerFloors 에는 남아있으므로 삭제된 층의 자리들도 저장하는 경우가 있을수 있기 때문에
      둘다 eventBus로 보내줘야함*/
      if (!this.firstLoadWatch) {
        this.floorNum = length - 1; // floor의 index가 되는 floorNum

        let allFloors = this.allFloorList;
        eventBus.$emit("allFloorList", allFloors);

        this.managerFloorList = allFloors.slice();
        let managerFloors = this.managerFloorList;
        eventBus.$emit("managerFloorList", managerFloors);

        this.firstLoadWatch = true;
      } else {
        //DB 로드 끝낸 후에 불리는 부분
        this.floorNum = length - 1; // floor의 index가 되는 floorNum

        let allFloors = this.allFloorList;
        eventBus.$emit("allFloorList", allFloors);

        let managerFloors = this.managerFloorList;
        eventBus.$emit("managerFloorList", managerFloors);
      }
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
      newFloor.floor_order = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

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
        let currentFloorId = this.allFloorList[this.floorNum].floor_name;
        const idx = this.allFloorList.findIndex(function (item) {
          return item.floor_name == currentFloorId;
        });
        if (idx > -1) {
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;
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
        this.setFloor(this.allFloorList[this.floorNum].floor_name);
      }

      console.log(this.length);
      //pop
    },
    changeFloorName() {
      // 여기에서 floor의 modify true 해줄 예정
      let currentFloorId = this.allFloorList[this.floorNum].floor_name;
      const idx = this.allFloorList.findIndex(function (item) {
        return item.floor_name == currentFloorId;
      });
      this.allFloorList[idx].floor_name = "변경된 floor name";
      this.allFloorList[idx].modify = true;

      this.managerFloorList[idx].floor_name = "변경된 floor name";
      this.managerFloorList[idx].modify = true;
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