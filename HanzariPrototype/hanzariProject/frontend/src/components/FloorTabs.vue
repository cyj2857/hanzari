<template>
  <v-card>
    <v-card-text class="text-center">
      <AttachCanvas></AttachCanvas>
      <v-btn text @click="decreaseTab()">Remove Floor</v-btn>
      <v-divider class="mx-4" vertical></v-divider>
      <v-btn text @click="increaseTab()">Add Floor</v-btn>
    </v-card-text>
    <v-tabs v-model="floorNum" background-color="cyan" dark>
      <v-tab v-for="tab of items" :key="tab.id" @change="sendFloorInfo(tab.id)">{{tab.id}}</v-tab>
      <!-- <v-tab v-for="n in length" :key="n" @change="sendFloorInfo(n)">{{ n }} Floor</v-tab> -->
    </v-tabs>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
import AttachCanvas from "@/components/AttachCanvas.vue";
export default {
  components: {
    AttachCanvas
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [
      { id: "One"},
      { id: "Five"},
      { id: "Six"}
    ],
    floorNum: null
  }),
  mounted() {
    this.sendFloorInfo(this.items[0].id);
  },
  methods: {
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
    }
  },
  watch: {
    length(val) {
      this.tab = val - 1;
    }
  }
};
</script>