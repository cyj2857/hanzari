<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>
          <span class="headline">Add Floor</span>
        </v-card-title>
        <v-text-field
          label="Floor"
          v-model="inputFloorName"
          required
        ></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="confirm">Confirm</v-btn>
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
      inputFloorName: null,
    };
  },
  created() {
    eventBus.$on("initFloor", (initFloor) => {
      this.inputFloorName = initFloor;
    });
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    confirm() {
      if (this.inputFloorName == null) return;
      eventBus.$emit("AddFloor", this.inputFloorName);
    },
  },
};
</script>
