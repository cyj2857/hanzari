<template>
  <v-card>
    <v-btn @click="getAllEmployeeSeats()">Show</v-btn>
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
      :items="allEmployeeSeat"
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
        </tr>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";

export default {
  name: "ManageSearch",
  props: ["eachEmployeeSeatMap", "copyEmployeeList"],
  data() {
    return {
      employees: this.copyEmployeeList,
      allEmployeeSeat: [],
      allEmployeeSeatMap: this.eachEmployeeSeatMap,
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
      ],
    };
  },
  mounted() {
    // eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
    //   this.allEmployeeSeatMap = eachEmployeeSeatMap;
    // });
  },
  methods: {
    getAllEmployeeSeats() {
      let keys = new Array();
      keys = Array.from(this.allEmployeeSeatMap.keys());
      for (let i = 0; i < keys.length; i++) {
        let seats = new Array();
        seats = this.allEmployeeSeatMap.get(keys[i]);
        for (let j = 0; j < seats.length; j++) {
          let newSeat = {};
          //console.log(seats[j])

          if (seats[j].employee_id != null) {
            //공석 제외
            newSeat.seatid = seats[j].seatId + "번";
            newSeat.employeeid = seats[j].employee_id;
            newSeat.name = seats[j].employee_name;
            newSeat.department = seats[j].employee_department;
            newSeat.floorid = seats[j].floor_id;
            newSeat.number = seats[j].employee_number;
            this.allEmployeeSeat.push(newSeat);
          }
        }
      }
      console.log(this.allEmployeeSeat);
    },
    showSeatButtonClicked(item) {
      eventBus.$emit("showSeat", item);
      eventBus.$emit("showSeatFloor", item.floor_id);
    },
  },
};
</script>