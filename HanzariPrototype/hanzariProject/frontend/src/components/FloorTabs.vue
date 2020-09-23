<template>
  <v-card>
    <v-card-text class="text-center">
      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" :key="item.tab">
          <v-card flat>
            <v-card-text>
              <AttachView :floorNum="item.tab"></AttachView>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>

      <v-btn text @click="decreaseTab()">Remove Tab</v-btn>
      <v-divider class="mx-4" vertical></v-divider>
      <v-btn text @click="increaseTab()">Add Tab</v-btn>
    </v-card-text>

    <v-tabs v-model="tab" background-color="cyan" dark>
      <v-tab v-for="n in length" :key="n">{{ n }} Floor</v-tab>
    </v-tabs>
  </v-card>
</template>

<script>
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
  methods: {
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