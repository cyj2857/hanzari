<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" :search="search">
      <template v-slot:item="row">
        <tr @click="showAlert(row.item)">
          <td>{{row.item.name}}</td>
          <td>{{row.item.department}}</td>
          <td>{{row.item.number}}</td>
          <td>
            <v-btn id="button" @click="onButtonClick(row.item)">Show</v-btn>
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
        { text: "", value: "button" }
      ],
      employees: [
        {
          name: "Ma Hyori",
          department: "Development Team",
          number: "010-5617-4977",
          employee_id:"1771354"
        },
        {
          name: "No Yunji",
          department: "Development Team",
          number: "010-4673-2827",
          employee_id:"1745674"
        },
        {
          name: "Choi Yujin",
          department: "Secure Team",
          number: "010-7906-3827",
          employee_id:"1791234"
        },
        {
          name: "Kim Dongmin",
          department: "has no Team",
          number: "010-3352-0898",
          employee_id:"1804321"
        }
      ]
    };
  },
  methods: {
    onButtonClick(item) {
      alert("click on " + item.name);
    },
    showAlert(item) {
      eventBus.$emit("createdRect", item);
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