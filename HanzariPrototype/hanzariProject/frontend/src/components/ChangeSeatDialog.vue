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
          <v-btn @click="test">test</v-btn>
          <v-btn @click="confirm">Confirm</v-btn>
          <v-btn @click="$emit('close')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<!--여기에서 group의 seatId,x,y 변경한 이후에 group에 다시 매핑해주고 attachCanvas로 넘겨줄 것!-->
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
      previousFloor: null,
      previousXLocation: null,
      previousYLocation: null,
      seatId: null,
      floor: null,
      XLocation: null,
      YLocation: null,
    };
  },
  created() {
    eventBus.$on("initChangeSeatDialog", (group) => {
      let groupToObject = group.toObject(["seatId", "top", "floor_id", "left"]);
      this.seatId = groupToObject.seatId; // 이후에 random만 올 예정
      this.previousFloor = groupToObject.floor_id;
      this.previousXLocation = groupToObject.top;
      this.previousYLocation = groupToObject.left;
    });
  },
  computed: {
    dialog() {
      return this.dialogStatus;
    },
  },
  methods: {
    test() {
      console.log(this.seatId);
      console.log(this.previousFloor);
      console.log(this.previousXLocation);
      console.log(this.previousYLocation);
    },
    confirm() {
      if (
        this.floor == null ||
        this.XLocation == null ||
        this.YLocation == null
      )
        return;

      eventBus.$emit("confirmChangeSeatDialog");
      /* 받는 곳에서 할 일
      원래 floor를 넘겨받아 eachFloorSeatMap의 floor에 해당하는 list 가져오고
      거기에서 group중 seatId가 같은 애를 캔버스에서만 삭제하고 그거 group 정보 복사해와서
      eachFloorSeatMap에서는 key를 입력 floor로 하여 이동 (되는지는 모르겠음)
      */
    },
  },
};
</script>