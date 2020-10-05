<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" :search="search">
      <template v-slot:item="row">
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.number }}</td>
          <td>
            <v-btn id="createSeatButton" @click="createSeatButtonClicked(row.item)">Create</v-btn>
          </td>
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
import axios from "axios";
const portNum = 8080;
export default {

  data() {
    return {
      allEmployeeSeatMap: null,
      search: "",
      headers: [
        {
          text: "Name",
          align: "start",
          sortable: true,
          value: "name"
        },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" },
        { text: "", value: "createSeatButton" },
        { text: "", value: "showSeatButton" }
      ],
      employees: []
    };
  },
  mounted() {
    this.employees = this.getEmployees();
  },
  created() {
    eventBus.$on("eachEmployeeSeatMap", eachEmployeeSeatMap => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  methods: {
    createSeatButtonClicked(item) {
      eventBus.$emit("createSeat", item);
    },
    showSeatButtonClicked(item) {
      //console.log("사이즈는 바로바로" + this.allEmployeeSeatMap.size);

      for (var k = 0; k < this.employees.length; k++) {
        if (this.employees[k].employee_id == item.employee_id) {
          //var eachEmployeeSeatList = this.allEmployeeSeatMap.get(
          //  item.employee_id
          //);
          var eachEmployeeSeatList = this.employees[k].seatIdList;
          console.log(eachEmployeeSeatList+"가 자리 개수이다.");

          console.log(
            this.employees[k].employee_id +
              "의 자리 개수는" +
              eachEmployeeSeatList.length
          ); //4

          if (eachEmployeeSeatList) {
            console.log(
              this.employees[k].employee_id +
                "의 자리는" +
                eachEmployeeSeatList[2]
            ); //2
            console.log(this.employees[k].name);

            this.employees[k].seatIdList = eachEmployeeSeatList;
            console.log(this.employees[k].seatIdList);

            eventBus.$emit("showSeatDataTable", this.employees[k]);
          }
        }
      }
    },
    getEmployees() {
      let initEmployeeList = new Array();
      
      axios.get("http://172.30.1.50:"+portNum+"/employee").then(function(response) {
        console.log(response.data.length + "는 사이즈입니다.");

        
        for (var i = 0; i < response.data.length; i++) {
          var newEmployee = {};

          newEmployee.name = response.data[i].employee_name;
          console.log(newEmployee.name + "내이름이야");
          newEmployee.department = response.data[i].department_name;
          newEmployee.number = response.data[i].extension_number;
          newEmployee.employee_id = response.data[i].employee_id;
          newEmployee.seatIdList = response.data[i].seatList;
          console.log(newEmployee.seatIdList);

          initEmployeeList.push(newEmployee);
        }
        console.log(initEmployeeList.length+"사람 개수야");
        
      });
      return initEmployeeList;
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