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
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.number }}</td>
          <!--<td><v-btn id="createSeatButton" @click="createSeatButtonClicked(row.item)">Create</v-btn></td> !-->
          <td>
            <v-btn id="showSeatButton" @click="showSeatButtonClicked(row.item)"
              >Show</v-btn
            >
          </td>
          <td>
            <v-btn
              id="MappingSeatButton"
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
  props: ["employee"],
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
        { text: "", value: "createSeatButton" },
        { text: "", value: "showSeatButton" },
      ],
      employees: this.employee,
    };
  },

  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  methods: {
    // createSeatButtonClicked(item) {
    //   eventBus.$emit("createSeat", item);
    // },
    showSeatButtonClicked(item) {
      //console.log("allEmployeeSeatMap? size" + this.allEmployeeSeatMap.size);

      for (let k = 0; k < this.employees.length; k++) {
        if (this.employees[k].employee_id == item.employee_id) {
          let eachEmployeeSeatList = this.allEmployeeSeatMap.get(
            item.employee_id
          );
          //var eachEmployeeSeatList = this.employees[k].seatIdList;
          console.log(eachEmployeeSeatList + "eachEmployeeSeatList");

          console.log(
            this.employees[k].employee_id +
              "employee_id? length" +
              eachEmployeeSeatList.length
          ); //4

          if (eachEmployeeSeatList) {
            console.log(
              this.employees[k].employee_id +
                "employee_id? length" +
                eachEmployeeSeatList[2]
            ); //2
            console.log(this.employees[k].name);

            this.employees[k].seatIdList = eachEmployeeSeatList;
            console.log(this.employees[k].seatIdList);

            eventBus.$emit("showSeatDataTable", this.employees[k]);
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