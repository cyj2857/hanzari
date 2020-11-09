<template>
  <div>
    <v-card>
      <v-tabs v-model="tab" background-color="black" dark>
        <v-tab v-for="item in items" :key="item.index"> {{ item.title }}</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" :key="item.index">
          <v-card flat>
            <v-card-text>
              <component v-bind:is="item.content" :copyEmployeeList="employees"></component>
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
import ManageSeats from "@/components/ManageSeats.vue";
import ManageFloors from "@/components/ManageFloors.vue";
import { eventBus } from '../main';
export default {
  props: ["copyEmployee"],
  components: {
    ManageSeats,
    ManageFloors,
  },
  data() {
    return {
      employees: this.copyEmployee,
      tab: null,
      items: [
        { title: "Seat", index: 0, content: "ManageSeats" },
        { title: "Floor", index: 1, content: "ManageFloors" },
        { title: "Search", index: 2 },
        { title: "MyPage", index: 3 },
      ],
    };
  },
  created() {
    console.log(this.employees)
  },
};
</script>