<template>
  <div>
    <v-card flat color="transparent">
      <v-card-title>Floor</v-card-title>
      <v-row>
        <v-col
          v-for="floor of this.allFloorList"
          :key="floor.floor_id"
          class="d-flex child-flex"
          cols="4"
        >
          <v-btn large @click="clickFloor(floor)">{{ floor.floor_name }}</v-btn>
        </v-col>
      </v-row>
      <v-btn small
        ><v-icon dark @click="removeFloor">remove_circle</v-icon></v-btn
      >
      <v-btn small><v-icon dark @click="addFloor">add_circle</v-icon></v-btn>
      <v-divider class="mx-4"></v-divider>
      <v-card-title>FloorName</v-card-title>
      <v-row>
        <v-text-field
          v-if="currentSelectedFloor"
          v-model="currentSelectedFloor.floor_name"
          @keyup="editFloorName"
          label="Enter FloorName"
          solo
        ></v-text-field
      ></v-row>
      <v-divider class="mx-4"></v-divider>
      <v-card-title>
        Vacant Seats {{ currentFloorVacantSeatsLength }}
      </v-card-title>
      <v-card-title> All Seats {{ currentFloorSeatsLength }} </v-card-title>
    </v-card>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import "material-design-icons-iconfont/dist/material-design-icons.css";

export default {
  props: ["copyfloorList"],
  components: {},
  data() {
    return {
      length: null,
      firstLoadWatch: null,
      floorName: null,

      allFloorList: this.copyfloorList,
      managerFloorList: [],

      currentSelectedFloor: null,

      currentFloorSeatsLength: 0,
      currentFloorVacantSeatsLength: 0,

      employees: [],
    };
  },
  created() {
    if (this.copyfloorList) {
      this.currentSelectedFloor = this.copyfloorList[
        this.copyfloorList.length - 1
      ];
      this.managerFloorList = this.allFloorList.slice();
      this.length = this.copyfloorList.length;
    }

    eventBus.$on("eachFloorSeatList", (eachFloorSeatList) => {
      if (eachFloorSeatList == undefined) {
        return;
      } else {
        this.renderEachFloorSeatList(eachFloorSeatList);
      }
    });
  },
  methods: {
    editFloorName() {
      const idx = this.allFloorList.findIndex((item) => {
        return item.floor_id == this.currentSelectedFloor.floor_id;
      });

      this.allFloorList[idx].modify = true;
      this.managerFloorList[idx].modify = true;

      eventBus.$emit("changeFloor", this.currentSelectedFloor);
      eventBus.$emit(
        "currentSelectedFloorToManageSeats",
        this.currentSelectedFloor
      );
      
      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    createFloorUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    clickFloor(floor) {
      this.currentSelectedFloor = floor;
      eventBus.$emit("changeFloor", floor);
      eventBus.$emit("currentSelectedFloorToManageSeats", floor); //ManageSeats to manage image

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
    removeFloor() {
      if (this.length > 0) {
        //items���� id�� ���� floor�� �� index ��������
        let currentFloorId = this.currentSelectedFloor.floor_id;
        const idx = this.allFloorList.findIndex(function (item) {
          return item.floor_id == currentFloorId;
        });
        if (idx > -1) {
          eventBus.$emit("deleteSeatListKey", this.allFloorList[idx].floor_id);
          this.allFloorList.splice(idx, 1);
          this.managerFloorList[idx].delete = true;

          eventBus.$emit("changeFloor", this.allFloorList[idx - 1]);
          eventBus.$emit(
            "currentSelectedFloorToManageSeats",
            this.allFloorList[idx - 1]
          );
          let allFloors = this.allFloorList.slice();
          eventBus.$emit("allFloorList", allFloors);

          let managerFloors = this.managerFloorList.slice();
          eventBus.$emit("managerFloorList", managerFloors);

          this.currentSelectedFloor = this.allFloorList[idx - 1];

          this.length--;
        }
        console.log(this.length + " length");
      } else {
        alert("there are no seats to delete!");
      }
    },
    addFloor() {
      let newFloor = {};
      newFloor.floor_id = this.createFloorUUID();
      newFloor.floor_name = "";
      newFloor.building_id = "HANCOM01";
      newFloor.floor_order = this.allFloorList.length;
      newFloor.create = true;
      newFloor.modify = false;
      newFloor.delete = false;

      this.allFloorList.push(newFloor);
      this.managerFloorList.push(newFloor);

      this.currentSelectedFloor = newFloor;

      this.length++;

      eventBus.$emit("changeFloor", this.currentSelectedFloor);
      eventBus.$emit(
        "currentSelectedFloorToManageSeats",
        this.currentSelectedFloor
      );

      let allFloors = this.allFloorList.slice();
      eventBus.$emit("allFloorList", allFloors);

      let managerFloors = this.managerFloorList.slice();
      eventBus.$emit("managerFloorList", managerFloors);
    },
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
        this.currentFloorSeatsLength = 0;
      }
    },
  },
};
</script>

