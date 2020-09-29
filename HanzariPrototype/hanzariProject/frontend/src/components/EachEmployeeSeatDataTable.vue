<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="seats" :search="search">
      <template v-slot:item="row">
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{row.item.name}}</td>
          <td>{{row.item.department}}</td>
          <td>{{row.item.seat_id}}</td>
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
        { text: "SeatID", value: "seat_id" },
        { text: "", value: "showSeatButton" }
      ],
      seats: []
    };
  },
  created() {
    eventBus.$on("showSeatDataTable", employee => {
      this.renderEachEmployeeSeatList(employee);
    });
  },
  methods: {
    showSeatButtonClicked(item) {
      console.log(item);
      eventBus.$emit("showSeat", item);
    },
    renderEachEmployeeSeatList(employee) {
      //리스트 초기화
      this.seats = [];
      var eachEmployeeSeatList = employee.seatIdList;

      if (eachEmployeeSeatList) {
        for (var i = 0; i < eachEmployeeSeatList.length; i++) {

          var newSeat = {};
          newSeat.seat_id = eachEmployeeSeatList[i]+"번";
          newSeat.employee_id = employee.employee_id;
          newSeat.name = employee.name;
          newSeat.department = employee.department;
          
          console.log(newSeat.seat_id+"입니다.");

          this.seats.push(newSeat);
        }
      }
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