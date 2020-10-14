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
      inputFloor: null,
      inputXLocation: null,
      inputYLocation: null,
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
        this.inputFloor == null ||
        this.inputXLocation == null ||
        this.inputYLocation == null
      )
        return;

      let changeSeatInfoMap = new Map();
      let previousInfoList = new Array();
      let currentInfoList = new Array();

      previousInfoList.push(this.previousFloor);
      previousInfoList.push(this.previousXLocation);
      previousInfoList.push(this.previousYLocation);

      currentInfoList.push(this.seatId);
      currentInfoList.push(this.inputFloor);
      currentInfoList.push(this.inputXLocation);
      currentInfoList.push(this.inputYLocation);

      changeSeatInfoMap.set("previous", previousInfoList);
      changeSeatInfoMap.set("current", currentInfoList);

      eventBus.$emit("confirmChangeSeatDialog", changeSeatInfoMap);
      /* 받는 곳(AttachCanvas)에서 할 일
      1. 기존 floor를 넘겨받아 eachFloorSeatMap에서 기존 floor 해당하는 list를 가져오고 
      A) 같은 층일때 (기존 floor == 입력 floor)
      2-A. group 중 seatId가 같은 group의 정보를 복사해온 후 (employee 정보를 알기위함) x, y를 수정하여 eachFloorSeatMap에 수정된 group push한 후 원래 group delete.
      3-A. renderAll
      B) 다른 층일때 (기존 floor != 입력 floor)
      2-B. group중 seatId가 같은 group 정보를 복사해온 후 (employee 정보를 알기위함) x, y를 수정함. 
           eachFloorSeatMap에서 입력 floor를 key로 하여 수정한 group을 push 한 후 기존 group delete
      3-B. 탭 변환 후 renderAll
      
      (상상코딩)
      */
    },
  },
};
</script>