<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <template>
          <v-card-title> input </v-card-title>
          <v-card-text> </v-card-text>
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
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
};
</script>