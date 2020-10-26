<template>
  <v-card>
    <v-card-title>
      <v-spacer
        >{{ this.currentFloor }} {{ this.currentFloorVacantSeatsLength }}좌석 /
        {{ this.currentFloorSeatsLength }}좌석
      </v-spacer>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="employees"
      class="elevation-1"
      height="775px"
    >
      <template v-slot:[`item.department`]="{ item }">
        <v-chip :color="getColor(item.department)" dark>{{
          item.department
        }}</v-chip>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
//@change="renderEachFloorSeatList(eachFloorSeatList)"
import { eventBus } from "../main.js";
import EachEmployeeSeatDataTableVue from "./EachEmployeeSeatDataTable.vue";
export default {
  data() {
    return {
      headers: [
        { text: "Name", align: "start", sortable: true, value: "name" },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" },
      ],
      employees: [],
      currentFloorSeatsLength: 0,
      currentFloorVacantSeatsLength: 0,
      currentFloor: null,
    };
  },
  created() {
    eventBus.$on("eachFloorSeatList", (eachFloorSeatList) => {
      if (eachFloorSeatList == undefined) {
        return;
      } else {
        this.renderEachFloorSeatList(eachFloorSeatList);
      }
    });
    eventBus.$on("changeFloor", (floor) => {
      if (floor == null) {
        this.currentFloor = null;
      } else {
        this.currentFloor = floor.floor_name;
      }
    });
  },
  methods: {
    getColor(department) {
      console.log("부서는" + department);
      if (department == "부서1") return "orange";
      else if (department == "부서2") return "yellow";
      else if (department == "부서3") return "green";
      else return "gray";
    },
    //emit된 eachFloorSeatList를 받아와서 하나씩 employees에 넣어준다.
    renderEachFloorSeatList(eachFloorSeatList) {
      //리스트 초기화
      this.employees = [];
      this.currentFloorVacantSeatsLength = 0;

      if (eachFloorSeatList.length != 0) {
        for (let i = 0; i < eachFloorSeatList.length; i++) {
          if (eachFloorSeatList[i].employee_id == null) {
            console.log(eachFloorSeatList[i].seatId + "빈공석의 seatId입니다");
            this.currentFloorVacantSeatsLength++;
          }

          let employee = {};
          employee.name = eachFloorSeatList[i].employee_name;
          employee.department = eachFloorSeatList[i].employee_department;
          employee.number = eachFloorSeatList[i].employee_number;

          this.employees.push({
            name: employee.name,
            department: employee.department,
            number: employee.number,
          });

          this.currentFloorSeatsLength = this.employees.length;

          console.log(employee.number);
        }
      } else {
        //this.employees = [];
        this.currentFloorSeatsLength = 0;
      }
    },
  },
};
</script>
