<template>
  <v-card flat color="transparent">
    <v-card-title>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="이름/부서/내선번호 중 입력하세요. 예) 마효리, 부서1, 111-0001"
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
            <v-icon large id="showSeatButton" @click="showSeatButtonClicked(row.item)"
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
  props: ["eachEmployeeSeatMap"],
  data() {
    return {
      allEmployeeSeat: [],
      allEmployeeSeatMap: this.eachEmployeeSeatMap, //attach(db) -> employeeMap -> 이벤트버스로 불려온것
     
      search: "",
      headers: [
        { text: "이름",align: "start", sortable: true, value: "name",},
        { text: "부서", value: "department" },
        { text: "내선번호", value: "number" },
        { text: "", value: "showSeatButton" },
      ],
    };
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
      this.getAllEmployeeSeats();
    });
  },
  mounted() {
    this.getAllEmployeeSeats();
  },
  methods: {
    getAllEmployeeSeats() {
      if (this.allEmployeeSeatMap) {
        this.allEmployeeSeat = [];

        let keys = [];
        keys = Array.from(this.allEmployeeSeatMap.keys());

        for (let i = 0; i < keys.length; i++) {
          let seats = [];
          seats = this.allEmployeeSeatMap.get(keys[i]);
          for (let j = 0; j < seats.length; j++) {
            let newSeat = {};

              if (seats[j].employee_id != null) { //공석 제외
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
      eventBus.$emit("showSeat", item); //좌석 하이라이트
      eventBus.$emit("showSeatFloor", item.floorid); //층 이동
    },
  },
};
</script>