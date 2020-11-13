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
      allEmployeeSeat: [], //가시적
      allEmployeeSeatMap: this.eachEmployeeSeatMap, //attach(db) -> employeeMap -> 이벤트버스로 불려온것
      search: "",
      headers: [
        {
          text: "이름",
          align: "start",
          sortable: true,
          value: "name",
        },
        { text: "부서", value: "department" },
        { text: "내선번호", value: "number" },
        { text: "", value: "showSeatButton" },
      ],
    };
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
      console.log(this.allEmployeeSeatMap);

      this.getAllEmployeeSeats();
    });
  },
  mounted() {
    this.getAllEmployeeSeats();
  },
  methods: {
    getAllEmployeeSeats() {
      if (this.allEmployeeSeatMap) {
        this.allEmployeeSeat=[];
        let keys = new Array();
        keys = Array.from(this.allEmployeeSeatMap.keys());
        for (let i = 0; i < keys.length; i++) {
          let seats = new Array();
          seats = this.allEmployeeSeatMap.get(keys[i]);
          for (let j = 0; j < seats.length; j++) {
            let newSeat = {};

            //if (this.findSeatFromAllEmployeeSeatBySeatId(seats[j]) == false) {
              if (seats[j].employee_id != null) {
                //���� ����
                newSeat.seatid = seats[j].seatId;
                newSeat.employeeid = seats[j].employee_id;
                newSeat.name = seats[j].employee_name;
                newSeat.department = seats[j].employee_department;
                newSeat.floorid = seats[j].floor_id;
                newSeat.number = seats[j].employee_number;
                this.allEmployeeSeat.push(newSeat);
              }
            //}
          }
        }
      }
    },
    findSeatFromAllEmployeeSeatBySeatId(seatId) {
      if (this.allEmployeeSeat.length > 0) {
        for (let i = 0; i < this.allEmployeeSeat.length; i++) {
          if (seatId == this.allEmployeeSeat[i].seatid) {
            return true;
          }
          return false;
        }
      } else {
        return false;
      }
    },
    showSeatButtonClicked(item) {
      eventBus.$emit("showSeat", item);
      eventBus.$emit("showSeatFloor", item.floorid);
    },
  },
};
</script>