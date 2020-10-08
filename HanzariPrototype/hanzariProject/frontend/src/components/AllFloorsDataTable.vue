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
      employees: [
        {
          name: "Ma Hyori",
          department: "Development Team",
          number: "010-5617-4977",
          employee_id: "1771354",
          seatIdList: null
        },
        {
          name: "No Yunji",
          department: "Development Team",
          number: "010-4673-2827",
          employee_id: "1745674",
          seatIdList: null
        },
        {
          name: "Choi Yujin",
          department: "Secure Team",
          number: "010-7906-3827",
          employee_id: "1791234",
          seatIdList: null
        },
        {
          name: "Kim Dongmin",
          department: "Marketing Team",
          number: "010-3352-0898",
          employee_id: "1804321",
          seatIdList: null
        },
        {
          name: "Ahn Hyeon ho",
          department: "Design Team",
          number: "010-34852-3421",
          employee_id: "1932584",
          seatIdList: null
        }
      ]
    };
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
      console.log("사이즈는 바로바로" + this.allEmployeeSeatMap.size);
      for (var k = 0; k < this.employees.length; k++) {
        if (this.employees[k].employee_id == item.employee_id) {
          var eachEmployeeSeatList = this.allEmployeeSeatMap.get(
            item.employee_id
          );
          console.log(this.employees[k].employee_id+"의 자리 개수는"+eachEmployeeSeatList.length);//4
          if (eachEmployeeSeatList) {
            console.log(this.employees[k].employee_id+"의 자리는"+eachEmployeeSeatList[2]);//2
            console.log(this.employees[k].name);
            this.employees[k].seatIdList = eachEmployeeSeatList;
            console.log(this.employees[k].seatIdList);
            eventBus.$emit("showSeatDataTable", this.employees[k]);
          }
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