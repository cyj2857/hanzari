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
const host = "172.30.1.50";

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
      //console.log("?Ç¨?ù¥Ï¶àÎäî Î∞îÎ°úÎ∞îÎ°ú" + this.allEmployeeSeatMap.size);

      for (let k = 0; k < this.employees.length; k++) {
        if (this.employees[k].employee_id == item.employee_id) {
          //let eachEmployeeSeatList = this.allEmployeeSeatMap.get(
          //  item.employee_id
          //);
          let eachEmployeeSeatList = this.employees[k].seatIdList;
          console.log(eachEmployeeSeatList+"Í∞? ?ûêÎ¶? Í∞úÏàò?ù¥?ã§.");

          console.log(
            this.employees[k].employee_id +
              "?ùò ?ûêÎ¶? Í∞úÏàò?äî" +
              eachEmployeeSeatList.length
          ); //4

          if (eachEmployeeSeatList) {
            console.log(
              this.employees[k].employee_id +
                "?ùò ?ûêÎ¶¨Îäî" +
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
      
      axios.get("http://"+host+":"+portNum+"/employee").then(function(response) {
        console.log(response.data.length + "?äî ?Ç¨?ù¥Ï¶àÏûÖ?ãà?ã§.");

        
        for (let i = 0; i < response.data.length; i++) {
          let newEmployee = {};

          newEmployee.name = response.data[i].employee_name;
          console.log(newEmployee.name + "?Ç¥?ù¥Î¶ÑÏù¥?ïº");
          newEmployee.department = response.data[i].department_name;
          newEmployee.number = response.data[i].extension_number;
          newEmployee.employee_id = response.data[i].employee_id;
          newEmployee.seatIdList = response.data[i].seatList;
          console.log(newEmployee.seatIdList);

          initEmployeeList.push(newEmployee);
        }
        console.log(initEmployeeList.length+"?Ç¨?ûå Í∞úÏàò?ïº");
        
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