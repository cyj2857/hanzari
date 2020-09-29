<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field single-line hide-details></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" :search="search">
      <template v-slot:item="row">
        <!--<tr @click="createRect(row.item)">!-->
        <tr>
          <td>{{row.item.name}}</td>
          <td>{{row.item.department}}</td>
          <td>{{row.item.number}}</td>
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
      currentItem: null,
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
        //{ text: "Number", value: "number" },
        { text: "", value: "showSeatButton" }
      ],
      employees: []
    };
  },
  created() {
    eventBus.$on("showSeatDataTable", item => {
      //this.employees.push({ name: item.name, department: item.department, number: item.number, employee_id: item.employee_id });
      this.currentItem = item;
      console.log(this.currentItem.employee_id + "가 사원의 아이디입니다.");
    });

    eventBus.$on("eachEmployeeSeatMap", eachEmployeeSeatMap => {
      this.renderEachEmployeeSeatList(eachEmployeeSeatMap);
    });
  },
  methods: {
    showSeatButtonClicked(item) {
      console.log(item);
      eventBus.$emit("showSeat", item);
    },
    renderEachEmployeeSeatList(eachEmployeeSeatMap) {
      //리스트 초기화
      this.employees = [];
      console.log("사이즈는 바로바로" + eachEmployeeSeatMap.size);
      var eachEmployeeSeatList = eachEmployeeSeatMap.get("1771354");
      if (eachEmployeeSeatList) {
        for (var i = 0; i < eachEmployeeSeatList.length; i++) {
          this.employees.push({
            name: "Ma hyori",
            department: "Department Team",
            employee_id: "1771354",
            seat_id: eachEmployeeSeatList[i] + "번 자리"
          });

          //this.employees.push(employee);
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