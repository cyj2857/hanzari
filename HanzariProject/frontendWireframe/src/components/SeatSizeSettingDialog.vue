<template>
  <v-dialog max-width="800" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>
          <span class="headline">Settings</span>
        </v-card-title>
        <v-row
          ><v-col cols="10" sm="2">
            <v-card-text> Width : </v-card-text>
          </v-col>
          <v-col cols="10" sm="7"
            ><v-slider
              v-model="sliderWidth"
              thumb-label="always"
              :max="widthMax"
              :min="widthMin"
              hide-details
              @change="changeWidthSlider"
            >
            </v-slider
          ></v-col> </v-row
        ><v-row>
          <v-col cols="10" sm="2"> <v-card-text> Height : </v-card-text> </v-col
          ><v-col cols="10" sm="7"
            ><v-slider
              v-model="sliderHeight"
              class="align-center"
              thumb-label="always"
              :max="heightMax"
              :min="heightMin"
              hide-details
              @change="changeWidthSlider"
            >
            </v-slider
          ></v-col>
        </v-row>
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
      widthMin: 10,
      widthMax: 50,
      heightMin: 10,
      heightMax: 50,

      sliderWidth: null,
      sliderHeight: null,
    };
  },
  created() {
    eventBus.$on("initSeatSizeSettingDialog", (size) => {
      console.log(size);
      this.sliderWidth = size.width;
      this.sliderHeight = size.height;
    });
  },
  beforeDestroy() {
    eventBus.$off("initSeatSizeSettingDialog");
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    confirm() {
      let slider = {};

      slider.width = this.sliderWidth;
      slider.height = this.sliderHeight;

      eventBus.$emit("changeSlider", slider);
    },
    changeWidthSlider() {
      this.sliderWidth = this.sliderWidth;
    },
    changeHeightSlider() {
      this.sliderHeight = this.sliderHeight;
    },
  },
};
</script>
