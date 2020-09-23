<template>
  <v-card>
    <v-card-text class="text-center">
      <AttachView></AttachView>

      <v-btn text @click="length--">Remove Tab</v-btn>
      <v-divider class="mx-4" vertical></v-divider>
      <v-btn text @click="length++">Add Tab</v-btn>
    </v-card-text>

    <v-tabs v-model="tab" background-color="cyan" dark>
<<<<<<< HEAD
      <v-tab v-for="n in length" :key="n" @change="sendFloorInfo(n)">{{ n }} Floor</v-tab>
=======
      <v-tab v-for="n in length" :key="n" @click="sendFloorInfo(n)">{{ n }} Floor</v-tab>
>>>>>>> 726afe70f9660802f1c90ca461ed2dced811796b
    </v-tabs>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
import AttachView from "@/components/AttachView.vue";
export default {
  components: {
    AttachView
<<<<<<< HEAD
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [
          { tab: 'One', content: 'Tab 1 Content' },
          { tab: 'Five', content: 'Tab 2 Content' },
          { tab: 'Six', content: 'Tab 3 Content' }
        ],
    floorNum: null,
  }),
  mounted() {
    this.sendFloorInfo(1)
=======
>>>>>>> 726afe70f9660802f1c90ca461ed2dced811796b
  },
  data: () => ({
    length: 3,
    tab: null,
    items: [
          { tab: 'One', content: 'Tab 1 Content' },
          { tab: 'Five', content: 'Tab 2 Content' },
          { tab: 'Six', content: 'Tab 3 Content' }
        ],
    floorNum: null,
  }),
  methods: {
    sendFloorInfo (n) {
      this.floorNum = n
      eventBus.$emit("changeFloor", this.floorNum)
    }
  },
  watch: {
    length(val) {
      this.tab = val - 1;
    }
  }
};
</script>