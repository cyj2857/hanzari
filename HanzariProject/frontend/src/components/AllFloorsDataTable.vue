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
            <v-btn id="showSeatButton" @click="showSeatButtonClicked(row.item)"
              >Show</v-btn
            >
          </td>
          <td>
            <v-btn
              id="MappingSeatButton"  draggable="true"
              @click="MappingSeatButtonClicked(row.item)"
              >Mapping</v-btn
            >
          </td>
        </tr>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  props: ["copyEmployee"],
  data() {
    return {
      allEmployeeSeatMap: null,
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
        { text: "", value: "showSeatButton" },
        { text: "", value: "mapSeatButton" },
      ],
      employees: this.copyEmployee,
      userAuthority: "Viewer",
      disabledValue: null,
    };
  },

  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  methods: {
    showSeatButtonClicked(item) {
      if(this.allEmployeeSeatMap){
        for (let k = 0; k < this.employees.length; k++) {
          if (this.employees[k].employee_id == item.employee_id) {
            let eachEmployeeSeatList = this.allEmployeeSeatMap.get(
              item.employee_id
            );
            console.log(eachEmployeeSeatList + "eachEmployeeSeatList");

            if (eachEmployeeSeatList) {
              console.log(
                this.employees[k].employee_id +
                 "employee_id? length" +
                  eachEmployeeSeatList.length
              ); //2
              console.log(this.employees[k].name);

              this.employees[k].seatIdList = eachEmployeeSeatList;
              console.log(this.employees[k].seatIdList);
            
              eventBus.$emit("showSeatDataTable", this.employees[k]);
            }
          }
        }
      }
    },

    MappingSeatButtonClicked(item) {
      eventBus.$emit("MappingSeat", item);
    },
  },
};
</script>

<style scoped>
#button {
  font-family: Arial, Helvetica, sans-serif;
  font-size: 10px;
}
</style>