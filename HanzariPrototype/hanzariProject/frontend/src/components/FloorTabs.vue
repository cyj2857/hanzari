<template>
  <div>
    <v-card>
      <v-card-text class="text-center">
        <AttachCanvas></AttachCanvas>
        <v-btn text @click="decreaseTab()">Remove Floor</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn text @click="getDialog">Add Floor</v-btn>
        <!--<v-btn text @click="increaseTab()">Add Floor</v-btn>-->
      </v-card-text>
      <v-tabs v-model="floorNum" background-color="cyan" dark>
        <v-tab
          v-for="tab of items"
          :key="tab.id"
          @change="sendFloorInfo(tab.id)"
          >{{ tab.id }}</v-tab
        >
      </v-tabs>
    </v-card>
    <TabEnterDialog
      :dialogStatus="this.dialogStatus"
      @close="closeDialog"
    ></TabEnterDialog>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import AttachCanvas from "@/components/AttachCanvas.vue";
import TabEnterDialog from "@/components/TabEnterDialog.vue";
export default {
  components: {
    AttachCanvas,
    TabEnterDialog
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [{ id: "One" }, { id: "Five" }, { id: "Six" }],
    floorNum: null,
    dialogStatus: false
  }),
  created() {
    eventBus.$on("close", () => {
      this.closeDialog()
    })
  },
  mounted() {
    this.sendFloorInfo(this.items[0].id);
  },
  methods: {
    getDialog(){
      this.dialogStatus = true;
      console.log(this.dialogStatus);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    sendFloorInfo(n) {
      this.floorNum = n;
      eventBus.$emit("changeFloor", this.floorNum);
    },
    decreaseTab() {
      this.length--;
      console.log("length" + this.length);
      //pop
    },
    increaseTab() {
      this.length++;
      //push
    },
  },
  watch: {
    length(val) {
      this.tab = val - 1;
    },
  },
};
</script>