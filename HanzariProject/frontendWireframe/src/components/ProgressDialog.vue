<template>
  <v-dialog
    :value="loading"
    persistent
    fullscreen
    content-class="loading-dialog"
  >
    <v-container fill-height>
      <v-layout row justify-center align-center>
        <v-card flat color="transparent">
          <v-progress-circular
            indeterminate
            :size="150"
            :width="10"
            color="black"
          ></v-progress-circular>
          <v-card-title primary-title class="justify-center"
            >Loading</v-card-title
          >
        </v-card>
      </v-layout>
    </v-container>
  </v-dialog>
</template>

<script>
export default {
  name: "ProgressDialog",
  props: {
    dialogStatus: {
      type: Boolean,
    },
  },
  computed: {
    loading() {
      return this.dialogStatus;
    },
  },
};
</script>