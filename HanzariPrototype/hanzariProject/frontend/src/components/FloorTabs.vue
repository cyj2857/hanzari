<template>
  <v-card>
    <v-card-text class="text-center">
      <AttachView></AttachView>
      <v-btn text @click="decreaseTab()">Remove Tab</v-btn>
      <v-divider class="mx-4" vertical></v-divider>
      <v-btn text @click="increaseTab()">Add Tab</v-btn>
    </v-card-text>

    <v-tabs v-model="tab" background-color="cyan" dark>
      <v-tab v-for="n in length" :key="n" @change="sendFloorInfo(n)">{{ n }} Floor</v-tab>
      <v-tab v-for="n in length" :key="n" @click="sendFloorInfo(n)">{{ n }} Floor</v-tab>
    </v-tabs>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
import AttachView from "@/components/AttachView.vue";
export default {
  components: {
    AttachView
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [
      { tab: "One", content: "Tab 1 Content" },
      { tab: "Five", content: "Tab 2 Content" },
      { tab: "Six", content: "Tab 3 Content" }
    ],
    floorNum: null
  }),
  mounted() {
    this.sendFloorInfo(1);
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [
      { tab: "One", content: "Tab 1 Content" },
      { tab: "Five", content: "Tab 2 Content" },
      { tab: "Six", content: "Tab 3 Content" }
    ],
    floorNum: null
  }),
  methods: {
    sendFloorInfo(n) {
      this.floorNum = n;
      eventBus.$emit("changeFloor", this.floorNum);
    },
    decreaseTab() {
      this.length--;
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