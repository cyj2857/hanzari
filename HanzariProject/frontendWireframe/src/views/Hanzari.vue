<template>
  <div class="hanzari" id="hanzari">
    <v-toolbar color="black" dark>
      <v-toolbar-title>Hanzari</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items class="hidden-sm-and-down">
        <v-btn text> Delete All </v-btn>
        <v-divider vertical></v-divider>
        <v-btn text> Save </v-btn>
        <v-divider vertical></v-divider>
        <v-btn text> Export </v-btn>
      </v-toolbar-items>
    </v-toolbar>
    
    <div class="d1">
      <Tabs />
    </div>
    <div class="d2">
      <AttachCanvas />
    </div>
  </div>
</template>

<script>
import axios from "axios";

import Tabs from "@/components/Tabs.vue";
import AttachCanvas from "@/components/AttachCanvas.vue";
export default {
  name: "Hanzari",
  components: {
    Tabs,
    AttachCanvas,
  },
  data() {
    return {};
  },
};
</script>

<style scoped>
.d1 {
  float: left;
  height: 100%;
}
.d2 {
  width: 70%;
  height: 100%;
}
</style>
