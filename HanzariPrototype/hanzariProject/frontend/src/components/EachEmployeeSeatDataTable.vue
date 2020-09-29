<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" :search="search">
      <template v-slot:item="row">
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{row.item.name}}</td>
          <td>{{row.item.department}}</td>
          <td>{{row.item.number}}</td>
          <td>
            <v-btn id="showSeatButton" @click="showSeatButtonClicked(row.item)">Show</v-btn>
          </td>
        </tr>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  data() {
    return {
      search: "",
      headers: [
        {
          text: "Name",
          align: "start",
          sortable: true,
          value: "name"
        },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" },
        { text: "", value: "showSeatButton" }
      ],
      employees: []
    };
  },
  created() {
    eventBus.$on("showSeatDataTable", item => {
	  this.employees.push({ name: item.name, department: item.department, number: item.number })
	});
  },
  methods: {
    showSeatButtonClicked(item) {
      eventBus.$emit("showSeat", item);
    }
  }
};
</script>

<style scoped>
#button {
  font-family: Arial, Helvetica, sans-serif;
  font-size: 10px;
}
</style> 