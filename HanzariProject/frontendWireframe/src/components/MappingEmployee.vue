<template>
  <v-card flat color="transparent">
    <v-icon large style="float: right" @click="changeBackPage">clear</v-icon>
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
            <v-icon
              id="MappingSeatButton"
              draggable="true"
              @click="MappingSeatButtonClicked(row.item)"
              >add_box</v-icon
            >
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
      allEmployeeSeatMap: null,
    };
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  methods: {
    MappingSeatButtonClicked(item) {
      eventBus.$emit("MappingSeat", item);
    },
    changeBackPage() {
      eventBus.$emit("mappingEmployeeComponentStatus", false);
    },
  },
};
</script>