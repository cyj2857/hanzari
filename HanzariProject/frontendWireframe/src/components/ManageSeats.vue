<template>
  <div>
    <v-card
      flat
      color="transparent"
      v-if="!mappingEmployeeComponentStatus"
    >
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title>Create Vacant Seats</v-card-title></v-col
        >
        <v-col cols="12" sm="3">
          <v-switch
            v-model="addVacantSwitch"
            inset
            @change="changeSwitchStatus"
          ></v-switch
        ></v-col>
        <v-col cols="12"
          ><v-slider
            v-if="addVacantSwitch"
            v-model="slider"
            class="align-center"
            thumb-label="always"
            :max="max"
            :min="min"
            hide-details
            @change="changeSliderValue"
          >
          </v-slider
        ></v-col>
      </v-row>

      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="12">
          <v-card-title>Mapping With Structure</v-card-title>
          <v-card-text>
            <v-btn @click="getMappingEmployeeComponent"
              >Mapping Employee</v-btn
            ></v-card-text
          ></v-col
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>

      <v-row>
        <v-col cols="12">
          <v-card-title>Get Image From Your PC</v-card-title>
          <v-card-text>
            <input
              v-show="false"
              ref="Upload"
              type="file"
              @change="changeImageFile"
            />
            <v-btn @click="$refs.Upload.click()"
              >Background Image Setting</v-btn
            >
            <v-card>
              <v-card-text>{{ currentFloorImage }}</v-card-text>
            </v-card>
          </v-card-text>
        </v-col>
      </v-row>
      <v-divider class="mx-4"></v-divider>
    </v-card>
    <MappingEmployee
      :copyEmployeeListTwo="employee"
      v-if="
        mappingEmployeeComponentStatus && 
        employee
      "
    />
    
  </div>
</template>

<script>
import MappingEmployee from "@/components/MappingEmployee.vue";
import { eventBus } from "../main";
export default {
  name: "ManageSeats",
  props: ["copyEmployeeList","copyfloorList"],
  components: {
    MappingEmployee,
  },
  data() {
    return {
      employee: this.copyEmployeeList,
      addVacantSwitch: false, // ���� ����� ���� ����ġ ����
      min: 1,
      max: 50,
      slider: 25,
      mappingEmployeeComponentStatus: false,

      allImageMap: null,
      currentSelectedFloorId: null,
      currentFloorImage: null,

      allFloorList: this.copyfloorList,
    };
  },
  created() {
    this.currentSelectedFloorId = this.allFloorList[
      this.allFloorList.length - 1
    ].floor_id;

     eventBus.$on("currentSelectedFloorToManageSeats", (floor) => {
       console.log(floor);
      if (floor) {// null 이 아닐때
        this.currentSelectedFloorId = floor.floor_id;
      } else {
        this.currentSelectedFloorId = null;
      }
    });
    eventBus.$on(
      "mappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );

    if (this.allImageMap == null) {
      this.allImageMap = new Map();
    }
  },
  methods: {
    getMappingEmployeeComponent() {
      this.mappingEmployeeComponentStatus = true;
    },
    changeSwitchStatus() {
      eventBus.$emit("changeAddVacantSwitch", this.addVacantSwitch);
      eventBus.$emit("changeslider", this.slider);
    },
    changeSliderValue() {
      eventBus.$emit("changeslider", this.slider);
    },
    changeImageFile(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.saveImageFile(files[0]);
    },
    saveImageFile(file) {
      this.currentFloorImage = file.name;
      console.log(this.currentSelectedFloorId);
      this.allImageMap.set(this.currentSelectedFloorId, file);
      eventBus.$emit("allImageMap",this.allImageMap);
    },
  },
};
</script>