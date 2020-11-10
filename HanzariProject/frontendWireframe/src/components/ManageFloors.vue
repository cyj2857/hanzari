<template>
  <div>
    Floors
    <v-divider class="mx-4" vertical></v-divider>
    <v-divider class="mx-4" vertical></v-divider>
    <v-divider class="mx-4" vertical></v-divider>
    <v-divider class="mx-4" vertical></v-divider>
    <v-divider class="mx-4" vertical></v-divider>
    <v-divider class="mx-4" vertical></v-divider>
    <v-btn small @click="removeFloor"
      ><v-icon dark>remove_circle</v-icon></v-btn
    >
    <hr />
    <br />

    <v-tabs v-model="floorNum" background-color="black" dark>
      <v-tab
        v-for="floor of this.allFloorList"
        :key="floor.floor_id"
        @change="setFloor(floor)"
      >
        {{ floor.floor_name }}
      </v-tab>
    </v-tabs>
    <component v-for="(btns, index) in floorBtns" :key="index" :is="btns" />

    <v-btn small @click="getAddFloorDialog"
      ><v-icon dark>add_circle</v-icon></v-btn
    >
    <hr />
    <br />

    Floor Name
    <hr />
    <v-text-field
      v-model="inputChangeFloorName"
      required
      @click="getChangeFloorNameDialog"
      v-on:keyup.enter="confirm"
    ></v-text-field>

    <v-btn @click="test">Test</v-btn>

    <ManageAddFloors 
      :dialogStatus="this.addFloorDialogStatus"
      @close="closeAddFloorDialog"
    ></ManageAddFloors>
  </div>
</template>

<script>
const BtnComponent = {
  template: "<button>" + "button" + "</button>",
  methods: {},
};
import { eventBus } from "../main.js";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import ManageAddFloors from "@/components/ManageAddFloors.vue";
export default {
  props: ["copyfloorList"],
  components: {
    ManageAddFloors,
  },
  data() {
    return {
      floorBtns: [BtnComponent],

      floorNum: null, //v-tabs v-model
      length: null,
      firstLoadWatch: null,

      allFloorList: this.copyfloorList, // 여기에서 sort 안먹음
      managerFloorList: [], // DB에 save 할 리스트

      seatFloor: null,

      addFloorDialogStatus: false,
      changeFloorNameDialogStatus: false,

      inputFloorName: null,
      inputChangeFloorName: null,
    };
  },
  created() {
    this.managerFloorList = this.allFloorList.slice();
    this.length = this.copyfloorList.length;

    if (this.length == 0) {
      /* 층 없는 상태에서 자리 생성 막기위해 넘겨줌
      길이가 늘어나지 않으므로 watch에서 불리지 않아서 created에서 불러주기*/
      let allFloors = this.allFloorList;
      eventBus.$emit("allFloorList", allFloors);
    }
    eventBus.$on("AddFloor", (floor_name) => {
      this.inputFloorName = floor_name;
      this.confirmAddFloorDialog();
    });
    eventBus.$on("ChangeFloorName", (floor_name) => {
      this.inputChangeFloorName = floor_name;
      this.confirmChangeFloorNameDialog();
    });
    eventBus.$on("initChangeFloorName", (initFloor) => {
      this.inputChangeFloorName = initFloor;
    });
    eventBus.$on("showSeatFloor", (floor_id) => {
      this.seatFloorId = floor_id;
      //console.log(this.seatFloorId + "가 넘어온 자리 층입니다");

      for (let i = 0; i < this.allFloorList.length; i++) {
        if (this.seatFloorId == this.allFloorList[i].floor_id) {
          this.floorNum = i;
          this.setFloor(this.allFloorList[this.floorNum]);
        }
      }
    });
  },
  watch: {
    length(length) {
      /* DB에서 로드해올때도 length가 늘어나기 때문에 watch가 불림
      층을 삭제하는 경우에도 managerFloors 에는 남아있으므로 삭제된 층의 자리들도 저장하는 경우가 있을수 있기 때문에
      둘다 eventBus로 보내줘야함*/
      if (!this.firstLoadWatch) {
        //처음 load
        this.floorNum = length - 1; // floor의 index가 되는 floorNum
        this.firstLoadWatch = true;
      } else {
        //DB 로드 끝낸 후에 불리는 부분
        this.floorNum = length - 1; // floor의 index가 되는 floorNum
      }

      if (this.length == 0) {
        this.setFloor(null);
      } else {
        this.setFloor(this.allFloorList[this.floorNum]);
      }
    },
  },
  methods: {
    test() {
      console.log(this.allFloorList);
    },
    confirm() {
      if (this.inputChangeFloorName == null) return;
      eventBus.$emit("ChangeFloorName", this.inputChangeFloorName);
    },
    setFloor(floor) {
      eventBus.$emit("changeFloor", floor);

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    getAddFloorDialog() {
      eventBus.$emit("initFloor", null);
      this.addFloorDialogStatus = true;
    },
    closeAddFloorDialog() {
      this.addFloorDialogStatus = false;
    },
    confirmAddFloorDialog() {
      this.addFloorDialogStatus = false;

      let newFloor = {};
      newFloor.floor_id = this.createFloorUUID();
      newFloor.floor_name = this.inputFloorName;
      newFloor.building_id = "HANCOM01";
      newFloor.floor_order = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

      this.floorBtns.push(BtnComponent);

      this.length++;
      this.floorNum = this.length + 1;

      console.log(this.length + " length");
    },
    getChangeFloorNameDialog() {
      eventBus.$emit(
        "initChangeFloorName",
        this.allFloorList[this.floorNum].floor_name
      );
      this.changeFloorNameDialogStatus = true;
    },
    closeChangeFloorNameDialog() {
      this.changeFloorNameDialogStatus = false;
    },
    confirmChangeFloorNameDialog() {
      this.changeFloorNameDialogStatus = false;
      this.changeFloorName(this.inputChangeFloorName);
    },
    removeFloor() {
      if (this.length > 0) {
        //items에서 id가 현재 floor인 애 index 가져오기
        let currentFloorId = this.allFloorList[this.floorNum].floor_id;
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
        this.floorNum = this.length - 1;

        //console.log(this.length + " length");
      } else {
        alert("there are no seats to delete!");
      }
    },
    changeFloorName(inputChangeFloorName) {
      // 여기에서 floor의 modify true 해줄 예정
      let currentFloorId = this.allFloorList[this.floorNum].floor_id;
      const idx = this.allFloorList.findIndex(function (item) {
        return item.floor_id == currentFloorId;
      });
      this.allFloorList[idx].floor_name = inputChangeFloorName;
      this.allFloorList[idx].modify = true;

      this.managerFloorList[idx].floor_name = inputChangeFloorName;
      this.managerFloorList[idx].modify = true;
    },
    createFloorUUID() {
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