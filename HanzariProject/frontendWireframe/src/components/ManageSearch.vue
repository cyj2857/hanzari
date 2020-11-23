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
      height="400px"
      class="elevation-1"
      no-data-text="데이터가 없습니다"
      :footer-props="{
        'items-per-page-text': '페이지 당 자리수',
        'items-per-page-options': [5, 10],
      }"
    >
      <template v-slot:item="row">
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.number }}</td>
          <td>
            <v-icon
              large
              id="showSeatButton"
              @click="showSeatButtonClicked(row.item)"
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
        { text: "이름", align: "start", sortable: true, value: "name" },
        { text: "부서", value: "department" },
        { text: "내선번호", value: "number" },
        { text: "", value: "showSeatButton" },
      ],
    };
  },
  created() {
    eventBus.$on("pushEachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
      this.getAllEmployeeSeats();
    });
  },
  mounted() {
    this.getAllEmployeeSeats();
  },
  beforeDestroy() {
    eventBus.$off("pushEachEmployeeSeatMap");
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
            let newSeatObject = {};

            if (seats[j].employeeId != null) {
              //공석 제외
              newSeatObject.seatId = seats[j].seatId;
              newSeatObject.employeeId = seats[j].employeeId;
              newSeatObject.name = seats[j].employeeName;
              newSeatObject.department = seats[j].employeeDepartment;
              newSeatObject.floorId = seats[j].floorId;
              newSeatObject.number = seats[j].employeeNumber;
              this.allEmployeeSeat.push(newSeatObject);
            }
          }
        }
      }
    },
    showSeatButtonClicked(clickedSeat) {
      eventBus.$emit("showSeatHighlight", clickedSeat); //좌석 하이라이트
      eventBus.$emit("pushFloorOfSeat", clickedSeat.floorId); //층 이동
    },
  },
};
</script>