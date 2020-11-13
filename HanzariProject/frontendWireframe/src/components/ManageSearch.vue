<template>
  <v-card flat color="transparent">
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
            <v-icon id="showSeatButton" @click="showSeatButtonClicked(row.item)"
              >event_seat</v-icon
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
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.eachEmployeeSeatMap = eachEmployeeSeatMap;
      console.log(this.eachEmployeeSeatMap);
    });
  },
  mounted() {
    this.getAllEmployeeSeats();
  },
  methods: {
    getAllEmployeeSeats() {
      if (this.allEmployeeSeatMap) {
        let keys = new Array();
        keys = Array.from(this.allEmployeeSeatMap.keys());
        for (let i = 0; i < keys.length; i++) {
          let seats = new Array();
          seats = this.allEmployeeSeatMap.get(keys[i]);
          for (let j = 0; j < seats.length; j++) {
            let newSeat = {};

            if (seats[j].employee_id != null) {
              //공석 제외
              newSeat.seatid = seats[j].seatId;
              newSeat.employeeid = seats[j].employee_id;
              newSeat.name = seats[j].employee_name;
              newSeat.department = seats[j].employee_department;
              newSeat.floorid = seats[j].floor_id;
              newSeat.number = seats[j].employee_number;
              this.allEmployeeSeat.push(newSeat);
            }
          }
        }
      }
    },
    showSeatButtonClicked(item) {
      eventBus.$emit("showSeat", item);
      eventBus.$emit("showSeatFloor", item.floorid);
    },
  },
};
</script>