<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <AttachCanvas></AttachCanvas>
        <v-btn text @click="removeFloor">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
      </v-card-text>
      <v-tabs v-model="floorNum" background-color="cyan" dark>
        <v-tab v-for="tab of items" :key="tab.id" @change="setFloor(tab.id)">{{ tab.id }}</v-tab>
      </v-tabs>
    </v-card>
    <AddFloorDialog :dialogStatus="this.dialogStatus" @close="closeDialog"></AddFloorDialog>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import AttachCanvas from "@/components/AttachCanvas.vue";
import AddFloorDialog from "@/components/AddFloorDialog.vue";
export default {
  components: {
    AttachCanvas,
    AddFloorDialog
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [{ id: "One" }, { id: "Five" }, { id: "Six" }],
    floorNum: null,
    dialogStatus: false,
    inputFloor: null,
    seatFloor: null
  }),
  created() {
    eventBus.$on("confirm", () => {
      this.confirmDialog();
    }),
      eventBus.$on("floorInfo", floor => {
        this.inputFloor = floor;
      }),
      eventBus.$on("showSeatFloor", floor => {
        this.seatFloor = floor;
        console.log(this.seatFloor + "가 넘어온 자리 층입니다");

        for(let i=0;i<this.items.length;i++){
          if(this.seatFloor == this.items[i].id){
            this.floorNum = i;
            this.setFloor(this.items[this.floorNum].id);
          }
        }
      });
  },
  mounted() {
    this.floorNum = 0;
    this.setFloor(this.items[this.floorNum].id);
  },
  methods: {
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
      this.items.push({ id: this.inputFloor });

      this.increaseTab();
      console.log(this.length);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    removeFloor() {
      //items���� id�� ���� floor�� �� index ��������
      let currentFloorId = this.items[this.floorNum].id;
      const idx = this.items.findIndex(function(item) {
        return item.id == currentFloorId;
      });
      if (idx > -1) this.items.splice(idx, 1);

      console.log(this.items);
      //items���� �� index ����
      this.decreaseTab();
    },
    setFloor(n) {
      eventBus.$emit("changeFloor", n);
    },
    decreaseTab() {
      this.length--;
      console.log("length" + this.length);
      //pop
    },
    increaseTab() {
      this.length++;
      //push
    }
  },
  watch: {
    length(val) {
      this.floorNum = val - 1;
    }
  }
};
</script>