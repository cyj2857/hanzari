<template>
  <div>
    <v-card>
      <v-tabs background-color="cyan" dark>
        <v-tab
          v-for="item of this.allTabItems"
          :key="item.index"
          @change="setComponent(item.index)"
        >
          {{ item.title }}</v-tab
        >
      </v-tabs>
    </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      allTabItems: [
        { title: "Seat", index: 0 },
        { title: "Floor", index: 1 },
        { title: "Search", index: 2 },
        { title: "MyPage", index: 3 },
      ],
    };
  },
  created() {},
};
</script>