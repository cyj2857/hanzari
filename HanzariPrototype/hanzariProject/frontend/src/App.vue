<template>
  <v-app id="app">
    <div id="routerLink">
      <v-card>
        <v-tabs background-color="primary" dark>
          <v-tab to="/yunjiTest">YunjiTestPage</v-tab>
          <v-tab to="/hyoriTest">HyoriTestPage</v-tab>
          <v-tab to="/YujinTest">YujinTestPage</v-tab>
          <v-tab to="/DongminTest">DongminTestPage</v-tab>
        </v-tabs>
      </v-card>
      <router-view />
    </div>
  </v-app>
</template>

<script>
import { eventBus } from "./main.js";
import axios from "axios";
const portNum = 8080;
const host = "172.30.1.50";

export default {
  name: "App",
  methods: {
    getSeats() {
      let loadSeatList = new Array()
      axios.get("http://"+host+":"+portNum+"/seats").then(function(response){
        for(let i = 0; i < response.data.length; i++) {
          let newSeat = {} // make new SeatObject
          newSeat.seat_id = response.data[i].seat_id
          console.log(newSeat.seat_id + "new object's seat_id")
          newSeat.floor = response.data[i].floor
          newSeat.x = response.data[i].x
          newSeat.y = response.data[i].y
          newSeat.building_id = response.data[i].building_id
          newSeat.employee_id = response.data[i].employee_id
          newSeat.width = response.data[i].width
          newSeat.height = response.data[i].height
          newSeat.degree = response.data[i].degree
          newSeat.shape_id = response.data[i].shape_id

          loadSeatList.push(newSeat)
        }
      })
      return loadSeatList
    }
  }
};
</script>

<style>
*:not(button) {
  width: 100%;
}
</style>
