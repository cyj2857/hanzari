<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>
          <span class="headline">Add Floor</span>
        </v-card-title>
        <v-text-field label="Floor" v-model="floor" required></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="test">test</v-btn>
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
      floor: null,
    };
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    test() {
      console.log(this.floor)
    },
    confirm() {
      eventBus.$emit("floorInfo", this.floor);
      eventBus.$emit("confirm");
    },
  },
};
</script>
