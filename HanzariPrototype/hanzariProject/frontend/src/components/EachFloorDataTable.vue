<template>
  <v-card>
    <v-card-title>
      <v-spacer>{{this.currentFloor}}</v-spacer>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="employees"
      class="elevation-1"
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
export default {
  data() {
    return {
      headers: [
        { text: "Name", align: "start", sortable: true, value: "name" },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" },
      ],
      employees: [],
      currentFloor: null
    };
  },
  created() {
    eventBus.$on("eachFloorSeatList", (eachFloorSeatList) => {
      this.renderEachFloorSeatList(eachFloorSeatList);
    });
    eventBus.$on("changeFloor", (floor)=>{
      this.currentFloor = floor
    })
  },
  methods: {
    getColor(department) {
      console.log("부서는" + department);
      if (department == "Development Team") return "orange";
      else if (department == "Secure Team") return "yellow";
      else if (department == "Marketing Team") return "green";
      else if (department == "Design Team") return "blue";
    },
    //emit된 eachFloorSeatList를 받아와서 하나씩 employees에 넣어준다.
    renderEachFloorSeatList(eachFloorSeatList) {
      //리스트 초기화
      this.employees = [];
      if (eachFloorSeatList) {
        for (let i = 0; i < eachFloorSeatList.length; i++) {
          let employee = {};
          employee.name = eachFloorSeatList[i].employee_name;
          employee.department = eachFloorSeatList[i].employee_department;
          employee.number = eachFloorSeatList[i].employee_number;

          this.employees.push({
            name: employee.name,
            department: employee.department,
            number: employee.number,
          });

          console.log(employee.number);
          //this.employees.push(employee);
        }
      }
    },
  }
};
</script>
