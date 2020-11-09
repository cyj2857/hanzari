<template>
  <v-card flat color="transparent">
    <v-btn style="float: right" @click="changeBackPage">X</v-btn>
    <v-card-title>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="this.employees"
      :search="search"
      height="265px"
    >
      <template v-slot:item="row">
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.number }}</td>
          <td>
            <v-btn id="MappingSeatButton" draggable="true">Mapping</v-btn>
          </td>
        </tr>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main";
export default {
  name: "MappingEmployee",
  props: ["copyEmployeeListTwo"],
  data() {
    return {
      employees: this.copyEmployeeListTwo,
      search: "",
      headers: [
        {
          text: "Name",
          align: "start",
          sortable: true,
          value: "name",
        },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" },
        { text: "", value: "mapSeatButton" },
      ],
      testEmployees: [
        {
          name: "NoYunji",
          department: "human resource",
          number: "123-5677",
        },
      ],
    };
  },
  created() {
    console.log(this.employees);

    
  },
  methods: {
    /*MappingSeatButtonClicked(item) {
      eventBus.$emit("MappingSeat", item);
    },*/
    changeBackPage() {
      eventBus.$emit("mappingEmployeeComponentStatus", false);
    },
  },
};
</script>