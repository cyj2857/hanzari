<template>
  <div>
    <canvas
      ref="canvas"
      class="canvas"
      id="canvas"
      width="1850px"
      height="1100px"
      style="text-align: center"
    ></canvas>
    <v-menu
      v-model="contextMenuStatus"
      :position-x="contextMenuXLocation"
      :position-y="contextMenuYLocation"
      absolute
      offset-y
      max-width="180"
    >
      <v-list>
        <v-list-item
          v-for="(item, index) in contextMenuItems"
          :key="index"
          @click="clickContextMenu(item.index)"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-tooltip
      top
      v-model="toolTipStatus"
      :position-x="toolTipXLocation"
      :position-y="toolTipYLocation"
      :color="toolTipColor"
    >
      <span v-html="toolTipText"> </span>
    </v-tooltip>
    <v-btn @click="clickTestSeat">Test Seat</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  name: "AttachCanvas",
  data() {
    return {
      floorCanvas: null,
      zoom: 1,
      fontSize: 25,
      clipboard: null,

      currentSelectedFloorName: null,
      currentSelectedFloorId: null,

      contextMenuStatus: false,
      contextMenuXLocation: 0,
      contextMenuYLocation: 0,
      contextMenuItems: [
        { title: "자리 비우기", index: 0 },
        { title: "삭제하기", index: 1 },
        { title: "층간 이동하기", index: 2 },
        { title: "복제하기", index: 3 },
        { title: "seatName 입력", index: 4 },
      ],

      toolTipStatus: false,
      toolTipXLocation: 100,
      toolTipYLocation: 100,
      toolTipColor: null,
      toolTipText: null,

      ableAddVacant: false,
      seatLength: null,
    };
  },
  created() {
    eventBus.$on("changeAddVacantSwitch", (switchValue) => {
      console.log(switchValue)
      this.ableAddVacant = switchValue;
    });
    eventBus.$on("changeslider", (sliderValue) => {
      console.log(sliderValue)
      this.seatLength = sliderValue;
    });
  },
  mounted() {
    this.initializing();
    //this.clickLoadCurrentFloor();
  },
  methods: {
    //canvas, map 생성
    initializing() {
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        this.floorCanvas = new fabric.Canvas(ref, {
          fireRightClick: true, //enable firing of right click events
          fireMiddleClick: true, //enable firing of middle click events
          stopContextMenu: true, //prevent context menu from showing
        });

        this.floorCanvas.on("mouse:wheel", (opt) => {
          if (!this.floorCanvas.viewportTransform) {
            return;
          }

          let evt = opt.e;
          let deltaY = evt.deltaY;
          this.zoom = this.floorCanvas.getZoom();

          this.zoom = this.zoom - deltaY / 300;

          if (evt.ctrlKey === true) {
            //zoom in and out
            if (this.zoom > 10) this.zoom = 10;
            else if (this.zoom < 1) this.zoom = 1;

            this.floorCanvas.zoomToPoint(
              new fabric.Point(evt.offsetX, evt.offsetY),
              this.zoom
            );
          } else {
            //reset canvas ratio
            this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
            this.zoom = 1;
          }
          this.checkZoom();
          opt.e.preventDefault();
          opt.e.stopPropagation();
        });

        //원하는 위치에 자동으로 공석 생성하기
        this.floorCanvas.on("mouse:down", (event) => {
          if (event.button === 3) {
            if (this.addVacantSwitch) {
              var pointer = this.floorCanvas.getPointer(event.e);
              var posX = pointer.x;
              var posY = pointer.y;
              //console.log(posX + ", " + posY);
              this.addVacantSeat(posX, posY);
            } else if (this.floorCanvas.getActiveObject()) {
              //contextMenu
              var pointer = this.floorCanvas.getPointer(event.e);
              var posX = this.floorCanvas.getActiveObject().left;
              var posY = this.floorCanvas.getActiveObject().top;
              this.showContextMenu(posX, posY);
              //console.log(posX + "/" + posY);
            }
          }
        });
        this.floorCanvas.on("object:modified", (e) => {
          let modifyObject = e.target;
          modifyObject.set("modify", true);
        });

        this.floorCanvas.on("mouse:over", (event) => {
          let group = event.target;
          if (group != null) {
            var posX = event.e.clientX;
            var posY = event.e.clientY;

            let groupToObject = group.toObject([
              "employee_id",
              "employee_name",
              "employee_department",
            ]);

            this.showToolTip(
              posX,
              posY,
              groupToObject.employee_id,
              groupToObject.employee_name,
              groupToObject.employee_department
            );

            //console.log("Employee_id" + groupToObject.employee_id);
            //console.log("Employee_name" + groupToObject.employee_name);
            //console.log("Floor_name" + this.currentSelectedFloorName);

            //eventBus.$emit("employee_id", groupToObject.employee_id);
            //eventBus.$emit("employee_name", groupToObject.employee_name);
            //eventBus.$emit("floor_name", this.currentSelectedFloorName);
            //eventBus.$emit(
            //  "employee_department",
            //  groupToObject.employee_department
            //);
            //this.getEmployeeDialog();
          } else {
            this.closeToolTip();
          }
        });

        //키보드 조작(상하좌우 이동/복붙/삭제)
        //this.manageKeyboard();
      }
    },
    initSeatSize(){

    },
    showToolTip(
      clientX,
      clientY,
      employee_id,
      employee_name,
      employee_department
    ) {
      this.toolTipXLocation = clientX;
      this.toolTipYLocation = clientY;

      if (employee_id == null) {
        this.toolTipText = "공석";
        this.toolTipColor = null;
      } else {
        this.toolTipText =
          "이름 : " +
          employee_name +
          "<br>아이디 : " +
          employee_id +
          "<br>부서 : " +
          employee_department;

        this.toolTipColor = "pink lighten-2";
      }

      this.toolTipStatus = true;
    },
    closeToolTip() {
      this.toolTipStatus = false;
    },

    clickTestSeat() {
      eventBus.$emit("manageSeatInfocomponentStatus", true);
      console.log(this.switchValue);
    },
  },
};
</script>

<style scoped>
.canvas {
  margin-left: 45px;
  border: 1px solid #000;
  background: aliceblue;
  height: 800px;
  width: 900px;
}
ul {
  padding: 0px;
  margin: 0px;
  display: block;
}
</style>
