<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="employees"
      @change="renderEachFloorSeatList(eachFloorSeatList)"
    >
      <template v-slot: item.department="{ item }">
        <v-chip :color="getColor(item.department)" dark>{{
          item.department
        }}</v-chip>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  data() {
    return {
      headers: [
        { text: "Name", align: "start", sortable: true, value: "name" },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" }
      ],
      employees: []
        /*{
          name: "Ma Hyori",
          department: "Development Team",
          number: "010-5617-4977"
        },
        {
          name: "No Yunji",
          department: "Development Team",
          number: "010-4673-2827"
        },
        {
          name: "Choi Yujin",
          department: "Secure Team",
          number: "010-7906-3827"
      },
      {
          name: "Kim Dongmin",
          department: "Engineering Team",
          number: "010-7906-3827"
      },
      {
          name: "Ahn Hyunho",
          department: "Engineering Team",
          number: "010-7906-3827"
        }*/
    };
  },
  created() {
    eventBus.$on('eachFloorSeatList' , eachFloorSeatList => {
      this.renderEachFloorSeatList(eachFloorSeatList);
    });
  },
  mounted() {
    this.renderEachFloorSeatList(eachFloorSeatList);
  },
  methods: {
    getColor(department) {
      if (department=="Secure Team") return "red";
      else if (department=="Development Team") return "orange";
      else if (department=="Engineering Team") return "green";
    },
    //emit된 eachFloorSeatList를 받아와서 하나씩 employees에 넣어준다.
    renderEachFloorSeatList(eachFloorSeatList){
      //리스트 초기화
      this.employees=[];
      for (var i = 0; i < eachFloorSeatList.length; i++) {
        console.log(eachFloorSeatList[i].employee_name);
        console.log(eachFloorSeatList[i].employee_department);
        console.log(eachFloorSeatList[i].employee_number);
        var employee = {};
        employee.name=eachFloorSeatList[i].employee_name;
        employee.department=eachFloorSeatList[i].employee_department;
        employee.number=eachFloorSeatList[i].employee_number;
        console.log(employee.number);
        this.employees.push(employee);
      }
    }
  }
};
</script>