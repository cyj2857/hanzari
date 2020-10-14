<template>
  <v-dialog max-width="500" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <template>
          <v-card-title> ChangeSeat </v-card-title>
          <v-card-text
            ><v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    label="Floor*"
                    v-model="floor"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="X location*"
                    v-model="XLocation"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="Y location*"
                    v-model="YLocation"
                    required
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </template>
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
      floor: null,
      XLocation: null,
      YLocation: null,
    };
  },
  created() {
    eventBus.$on("initChangeSeatDialogFloor", (floor) => {
      this.floor = floor;
    }),
      eventBus.$on("initChangeSeatDialogX", (x) => {
        this.XLocation = x;
      }),
      eventBus.$on("initChangeSeatDialogY", (y) => {
        this.YLocation = y;
      });
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    confirm() {
      eventBus.$emit("confirmChangeSeatDialog");
    },
  },
};
</script>