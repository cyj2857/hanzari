<template>
  <div>
    <v-card>
      <v-tabs v-model="tab" background-color="black" dark>
        <v-tab
          v-for="item in items"
          :key="item.index"
        >
          {{ item.title }}</v-tab
        >
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" :key="item.index">
          <v-card flat>
            <v-card-text>
              <component v-bind:is="item.content"></component>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
import ManageSeats from "@/components/ManageSeats.vue";
export default {
  components: {
    ManageSeats,
  },
  data() {
    return {
      tab: null,
      items: [
        { title: "Seat", index: 0, content: "ManageSeats" },
        { title: "Floor", index: 1 },
        { title: "Search", index: 2 },
        { title: "MyPage", index: 3 },
      ],
    };
  },
};
</script>