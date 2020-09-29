<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <template v-if="employee_id">
          <v-card-title>
            {{ this.employee_name }} Employee Information
          </v-card-title>
          <v-card-text>
            floor : {{ this.floor_id }} id : {{ this.employee_id }} name :
            {{ this.employee_name }}
          </v-card-text>
        </template>
        <template v-else>
          <v-card-title>
            Vacant Seat
          </v-card-title>
        </template>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="$emit('close')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import { eventBus } from "../main";
export default {
  props: {
    dialogStatus: {
      type: Boolean,
    },
  },
  data() {
    return {
      employee_id: "",
      floor_id: "",
      employee_name: "",
    };
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  created() {
    eventBus.$on("employee_id", (employee_id) => {
      this.employee_id = employee_id;
    });
    eventBus.$on("floor_id", (floor_id) => {
      this.floor_id = floor_id;
    });
    eventBus.$on("employee_name", (employee_name) => {
      this.employee_name = employee_name;
    });
  },
};
</script>
