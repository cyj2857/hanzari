<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="seats"
      :search="search"
      height="265px"
    >
      <template v-slot:item="row">
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.floor_id }}</td>
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
  data() {
    return {
      search: "",
      headers: [
        {
          text: "Name",
          align: "start",
          sortable: true,
          value: "name",
        },
        { text: "Department", value: "department" },
        //{ text: "SeatID", value: "seat_id" },
        { text: "Floor", value: "floor_id" },
        { text: "", value: "showSeatButton" },
      ],
      seats: [],
    };
  },
  created() {
    eventBus.$on("showSeatDataTable", (employee) => {
      this.renderEachEmployeeSeatList(employee);
    });
  },
  methods: {
    showSeatButtonClicked(seat) {
      console.log(seat);
      eventBus.$emit("showSeat", seat);
      eventBus.$emit("showSeatFloor", seat.floor_id);
    },
    renderEachEmployeeSeatList(employee) {
      //리스트 초기화
      this.seats = [];
      let eachEmployeeSeatList = employee.seatIdList;

      if (eachEmployeeSeatList) {
        for (let i = 0; i < eachEmployeeSeatList.length; i++) {
          let newSeat = {};

          let groupToObject = eachEmployeeSeatList[i].toObject([
            "seatId",
            "employee_id",
            "floor_id",
          ]);

          newSeat.seat_id = groupToObject.seatId + "번";
          newSeat.employee_id = groupToObject.employee_id;
          newSeat.name = employee.name;
          newSeat.department = employee.department;
          newSeat.floor_id = groupToObject.floor_id;

          console.log(newSeat.seat_id + "입니다.");

          this.seats.push(newSeat);
        }
      }
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