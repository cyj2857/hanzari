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
                    v-model="inputFloor"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="X location*"
                    v-model="inputXLocation"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="Y location*"
                    v-model="inputYLocation"
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
      inputFloor: null,
      inputXLocation: null,
      inputYLocation: null,
    };
  },
  created() {
    eventBus.$on("initChangeSeatDialog", (value) => {
      this.inputFloor = value
      this.inputXLocation = value
      this.inputYLocation = value
    });
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    confirm() {
      if (
        this.inputFloor == null ||
        this.inputXLocation == null ||
        this.inputYLocation == null
      )
        return;

      let inputInfo = new Array()
      inputInfo.push(this.inputFloor)
      inputInfo.push(this.inputXLocation)
      inputInfo.push(this.inputYLocation)

      eventBus.$emit("confirmChangeSeatDialog", inputInfo);

    /*입력값 넘겨주고 getActiveObject한 이후에 거기에 _obejcts. 으로 변경하고 renderall 할 것. */
    },
  },
};
</script>