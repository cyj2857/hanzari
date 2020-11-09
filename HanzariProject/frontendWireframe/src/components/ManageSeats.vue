<template>
  <div>
    <v-card
      flat
      color="transparent"
      v-if="!mappingEmployeeComponentStatus && !manageSeatInfocomponentStatus"
    >
      <v-row>
        <v-col cols="12" sm="9">
          <v-card-title>Create Vacant Seats</v-card-title></v-col
        >
        <v-col cols="12" sm="3">
          <v-switch v-model="addVacantSwitch" inset></v-switch
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
              >Background Image Setting</v-btn>
              <v-card>
              <v-card-text>{{currentFloorImage}}</v-card-text>
              </v-card>
            </v-card-text>
            </v-col>
      </v-row>
      <v-divider class="mx-4"></v-divider>
    </v-card>
    <MappingEmployee
      :copyEmployeeListTwo="employee"
      v-if="mappingEmployeeComponentStatus && !manageSeatInfocomponentStatus"
    />
    <ManageSeatInfo v-if="manageSeatInfocomponentStatus" />
  </div>
</template>

<script>
import MappingEmployee from "@/components/MappingEmployee.vue";
import ManageSeatInfo from "@/components/ManageSeatInfo.vue";
import { eventBus } from "../main";
export default {
  name: "ManageSeats",
  props: ["copyEmployeeList"],
  components: {
    MappingEmployee,
    ManageSeatInfo,
  },
  data() {
    return {
      employee: this.copyEmployeeList,
      addVacantSwitch: false, // ���� ����� ���� ����ġ ����
      min: 1,
      max: 50,
      slider: 25,
      mappingEmployeeComponentStatus: false,
      manageSeatInfocomponentStatus: false,

      allImageMap: null,
      currentSelectedFloorId: "One",
      currentFloorImage: null,
    };
  },
  created() {
    eventBus.$on(
      "mappingEmployeeComponentStatus",
      (mappingEmployeeComponentStatus) => {
        this.mappingEmployeeComponentStatus = mappingEmployeeComponentStatus;
      }
    );
    eventBus.$on(
      "manageSeatInfocomponentStatus",
      (manageSeatInfocomponentStatus) => {
        console.log(manageSeatInfocomponentStatus);
        this.manageSeatInfocomponentStatus = manageSeatInfocomponentStatus;
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
    changeImageFile(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.saveImageFile(files[0]);
      //this.loadImageFile(files[0]);
    },
    saveImageFile(file) {
      console.log(file);
      this.currentFloorImage = file.name;
      this.allImageMap.set(this.currentSelectedFloorId, file);
      console.log(this.allImageMap.size);
      eventBus.$emit("allImageMap", this.allImageMap);
    },
  },
};
</script>