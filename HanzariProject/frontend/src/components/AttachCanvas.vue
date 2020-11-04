<template>
  <div>
    <v-switch
      v-model="addVacantSwitch"
      inset
      :label="`공석만들기 모드 ${addVacantSwitch.toString()}`"
    ></v-switch>
    <canvas
      ref="canvas"
      class="canvas"
      width="950px"
      height="800px"
      style="text-align: center"
    ></canvas>
    <input v-show="false" ref="Upload" type="file" @change="changeImageFile" />
    <v-btn color="success" @click="$refs.Upload.click()"
      >Upload Background img file</v-btn
    >
    <v-btn @click="deleteBtn">Delete Selected Shape</v-btn>
    <v-btn @click="deleteAllBtn">Delete All Shapes</v-btn>
    <v-btn @click="clickChangeToVacant">Change to Vacant</v-btn>
    <v-btn @click="clickResetToRatio" color="pink">Reset Ratio</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
    <v-btn @click="test">test</v-btn>
    <EmployeeDialog
      :dialogStatus="this.employeeDialogStatus"
      @close="closeEmployeeDialog"
    />
    <ChangeSeatDialog
      :dialogStatus="this.changeSeatDialogStatus"
      @close="closeChangeSeatDialog"
    />
    <InputSeatNameDialog
      :dialogStatus="this.inputSeatNameDialogStatus"
      @close="closeInputSeatNameDialog"
    />
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import EmployeeDialog from "@/components/EmployeeDialog.vue";
import ChangeSeatDialog from "@/components/ChangeSeatDialog.vue";
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";
import InputSeatNameDialog from "@/components/InputSeatNameDialog.vue";

export default {
  props: [
    "copyEmployee",
    "copyFloors",
    "seat",
    "currentFloorImage",
    "otherFloorsImageList",
    "currentFloorSeatsList",
    "otherFloorsSeatsList",
  ],
  components: {
    EmployeeDialog,
    AllFloorsDataTable,
    ChangeSeatDialog,
    InputSeatNameDialog,
  },
  data: function () {
    return {
      addVacantSwitch: false, // 공석 만들기 위한 스위치 상태
      floorCanvas: null,

      currentSelectedFloorName: null,
      currentSelectedFloorId: null,

      currentFloorImageFromDb: this.currentFloorImage,
      otherFloorImageFromDb: this.otherFloorsImageList,
      //이미지 맵 하나로 관리(삭제 고려X)
      allImageMap: null, //모든 이미지 저장과 로드(floorid,file or url)

      currentFloorSeatListFromDb: this.currentFloorSeatsList, //current floor's seatList
      otherFloorSeatListFromDb: this.otherFloorsSeatsList,
      //seats: this.seat, //DB로부터 넘어온 현재 층의 자리들을 제외한 자리 Map <층이름, 자리리스트>
      allSeatMap: null, // -> all seat map (가시적 map)
      managerAllSeatMap: null, // -> DB 관리 자리 map

      employees: this.copyEmployee,
      eachEmployeeSeatMap: null, //each Employee's seats map

      allFloorList: this.copyFloors, // 가시적 층 리스트
      managerFloorList: this.copyFloors, // DB 관리 층 리스트

      employeeDialogStatus: false, // 사원 정보 다이얼로그
      changeSeatDialogStatus: false, // 자리 변경 다이얼로그
      inputSeatNameDialogStatus: false, // seatName 입력 다이얼로그 => 다이얼로그 이후에 모두 UI 변경 예정
    };
  },
  created() {
    eventBus.$on("changeFloor", (floor) => {
      console.log("changeFloor in AttachCanvas1");
      if (floor) {
        // null 이 아닐때
        this.currentSelectedFloorId = floor.floor_id;
        this.currentSelectedFloorName = floor.floor_name;
        this.changeFloor();
        console.log("changeFloor in AttachCanvas2");
        console.log(this.currentSelectedFloorName + " 여기가 현재층");
      } else {
        this.currentSelectedFloorId = null;
        this.currentSelectedFloorName = null;
      }
    });
    eventBus.$on("confirmChangeSeatDialog", (inputInfo) => {
      this.confirmChangeSeatDialog(inputInfo);
    });
    eventBus.$on("confirmInputSeatNameDialog", (inputSeatName) => {
      this.confirmInputSeatNameDialog(inputSeatName);
    });
    eventBus.$on("showSeat", (seat) => {
      console.log(seat);
      this.showSeat(seat);
    });

    eventBus.$on("MappingSeat", (item) => {
      this.setMappingSeat(item);
    });
    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      //console.log(allFloors);
    });
    eventBus.$on("managerFloorList", (managerFloors) => {
      this.managerFloorList = managerFloors;
      //console.log(managerFloors);
    });
    eventBus.$on("deleteSeatListKey", (floor_id) => {
      this.allSeatMap.delete(floor_id);

      /*managerAllSeatMap 에서 삭제되어도 되는 이유 : managerFloorList만큼 저장을 하기때문에 
      그에 해당되지 않는 key는 저장이 되지 않을 것. 그리고 DB에서도 삭제되는 층이 있으면 자동으로 
      그 층에 해당하는 자리들도 삭제함*/
      this.managerAllSeatMap.delete(floor_id);
    });
    if (this.allImageMap == null) {
      this.allImageMap = new Map();
    }
    if (this.allSeatMap == null) {
      this.allSeatMap = new Map();
    }
    if (this.managerAllSeatMap == null) {
      this.managerAllSeatMap = new Map();
    }
    if (this.eachEmployeeSeatMap == null) {
      this.eachEmployeeSeatMap = new Map();
    }
  },
  mounted() {
    this.initializing();
    this.clickLoadCurrentFloor();
  },
  methods: {
    test() {
      console.log(this.currentSelectedFloorId);
      console.log(this.getEachFloorSeatList(this.currentSelectedFloorId));
    },
    getEmployeeDialog() {
      this.employeeDialogStatus = true;
      console.log(this.employeeDialogStatus);
    },
    closeEmployeeDialog() {
      console.log("<<<close dialog>>>");
      this.employeeDialogStatus = false;
      console.log(this.employeeDialogStatus);
    },
    getInputSeatNameDialog() {
      eventBus.$emit("initInputSeatNameDialog", null);
      this.inputSeatNameDialogStatus = true;
      console.log(this.inputSeatNameDialogStatus);
    },
    closeInputSeatNameDialog() {
      console.log("<<<close dialog>>>");
      this.inputSeatNameDialogStatus = false;
      console.log(this.inputSeatNameDialogStatus);
    },
    confirmInputSeatNameDialog(inputSeatName) {
      console.log("<<<confirm dialog>>>");
      this.inputSeatNameDialogStatus = false;
      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      let activeObject = this.floorCanvas.getActiveObject();
      activeObject.set("modify", true);
      activeObject.seatName = inputSeatName;

      let seatNameObject = new fabric.IText(inputSeatName, {
        left: activeObject.item(0).left,
        top: activeObject.item(0).top - 15,
        fontSize: 15,
        fill: "black",
      });
      if (activeObject.item(2)) {
        // seatNmae 변경시
        activeObject.remove(activeObject.item(2));
      }

      activeObject.add(seatNameObject);
      activeObject.addWithUpdate();

      this.floorCanvas.renderAll();
    },
    getChangeSeatDialog() {
      eventBus.$emit("initChangeSeatDialog", null);
      this.changeSeatDialogStatus = true;
      console.log(this.changeSeatDialogStatus);
    },
    closeChangeSeatDialog() {
      console.log("<<<close dialog>>>");
      this.changeSeatDialogStatus = false;
      console.log(this.changeSeatDialogStatus);
    },
    // 층간이동
    confirmChangeSeatDialog(inputInfo) {
      console.log("<<<confirm dialog>>>");
      this.changeSeatDialogStatus = false;

      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let managerEachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      let activeObject = this.floorCanvas.getActiveObject();

      for (let i = 0; i < this.allFloorList.length; i++) {
        // 이후에 combobox로 만들던가 해서 불필요한 로직 줄일것.
        if (inputInfo[0] == this.allFloorList[i].floor_name) {
          activeObject.floor_id = this.allFloorList[i].floor_id;
          activeObject.floor_name = this.allFloorList[i].floor_name;
          activeObject.left = parseInt(inputInfo[1]);
          activeObject.top = parseInt(inputInfo[2]);
          activeObject.modify = true;

          let changeFloorSeatList = this.getEachFloorSeatList(
            this.allFloorList[i].floor_id //input floor's floor_id
          );
          let changeManagerFloorSeatList = this.getManagerEachFloorSeatList(
            this.allFloorList[i].floor_id
          );
          changeFloorSeatList.push(activeObject);
          changeManagerFloorSeatList.push(activeObject);

          //이동 후에 원래 list에서 삭제
          for (let j = 0; j < eachFloorSeatList.length; j++) {
            if (eachFloorSeatList[j].seatId == activeObject.seatId) {
              eachFloorSeatList.splice(j, 1);
            }
          }
          for (let j = 0; j < managerEachFloorSeatList.length; j++) {
            if (managerEachFloorSeatList[j].seatId == activeObject.seatId) {
              managerEachFloorSeatList[j].set("delete", true);
            }
          }

          eventBus.$emit("showSeatFloor", this.allFloorList[i].floor_id);
          eventBus.$emit("eachFloorSeatList", changeFloorSeatList);

          this.floorCanvas.renderAll();
        }
      }
    },
    //canvas, map 생성
    initializing() {
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        this.floorCanvas = new fabric.Canvas(ref, {
          fireRightClick: true, // <-- enable firing of right click events
          fireMiddleClick: true, // <-- enable firing of middle click events
          stopContextMenu: true, // <--  prevent context menu from showing
        });
        this.floorCanvas.on("mouse:wheel", (opt) => {
          if (!this.floorCanvas.viewportTransform) {
            return;
          }
          let evt = opt.e;
          if (evt.ctrlKey === true) {
            //zoom in and out
            let evt = opt.e;
            let deltaY = evt.deltaY;
            let zoom = this.floorCanvas.getZoom();
            zoom = zoom - deltaY / 300;
            if (zoom > 20) zoom = 20;
            if (zoom < 1) zoom = 0.95;
            this.floorCanvas.zoomToPoint(
              new fabric.Point(evt.offsetX, evt.offsetY),
              zoom
            );
          } else {
            //reset canvas ratio
            this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
          }
          opt.e.preventDefault();
          opt.e.stopPropagation();
        });

        //원하는 위치에 자동으로 공석 생성하기
        this.floorCanvas.on("mouse:down", (event) => {
          if (event.button === 3 && this.addVacantSwitch) {
            var pointer = this.floorCanvas.getPointer(event.e);
            var posX = pointer.x;
            var posY = pointer.y;
            console.log(posX + ", " + posY);
            this.addVacantSeat(posX, posY);
          }
        });

        this.floorCanvas.on("object:modified", function (e) {
          //크기, 이동, 회전
          let modifyObject = e.target;
          modifyObject.set("modify", true);
        });

        this.floorCanvas.on("object:selected", function (e) {
          let evt = e.e;
          if (evt.ctrlKey == true) {
            console.log("wow");
          }
          var activeObject = this.floorCanvas.getActiveObject();
          var activeGroup = this.floorCanvas.getActiveGroup();
        });

        this.manageKeyboard(this.floorCanvas);
      }
    },
    clickResetToRatio() {
      this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
    },
    manageKeyboard(floorCanvas) {
      document.onkeydown = function (event) {
        var key = window.event ? window.event.keyCode : event.keyCode;

        switch (key) {
          case 37: // left
            if (floorCanvas.getActiveObject()) {
              //floorCanvas.getActiveObject().left -= 5;
              floorCanvas
                .getActiveObject()
                .set("left", floorCanvas.getActiveObject().left - 5);
              floorCanvas.getActiveObject().set("modify", true);
              floorCanvas.renderAll();
            }
            break;
          case 39: // right
            if (floorCanvas.getActiveObject()) {
              //floorCanvas.getActiveObject().left += 5;
              floorCanvas
                .getActiveObject()
                .set("left", floorCanvas.getActiveObject().left + 5);
              floorCanvas.getActiveObject().set("modify", true);
              floorCanvas.renderAll();
            }
            break;
          case 38: // up
            if (floorCanvas.getActiveObject()) {
              //floorCanvas.getActiveObject().top -= 5;
              floorCanvas
                .getActiveObject()
                .set("top", floorCanvas.getActiveObject().top - 5);
              floorCanvas.getActiveObject().set("modify", true);
              floorCanvas.renderAll();
            }
            break;
          case 40: // down
            if (floorCanvas.getActiveObject()) {
              //floorCanvas.getActiveObject().top += 5;
              floorCanvas
                .getActiveObject()
                .set("top", floorCanvas.getActiveObject().top + 5);
              floorCanvas.getActiveObject().set("modify", true);
              floorCanvas.renderAll();
            }
            break;
          case 46: // delete
            if (floorCanvas.getActiveObject()) {
            }
            break;
        }
      };
    },
    createSeatUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    changeFloor() {
      //도형 랜더링 전에 화면의 도형들을  초기화
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach((obj) => {
          this.floorCanvas.remove(obj);
        });

      let myOnefloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
        this.currentSelectedFloorId
      );
      // managerEachFloorSeatList init 해주기 위함

      if (this.allImageMap.get(this.currentSelectedFloorId) != null) {
        let typeCheck = this.allImageMap.get(this.currentSelectedFloorId);
        if (typeof typeCheck === "string") {
          //url
          this.loadImageUrl(this.allImageMap.get(this.currentSelectedFloorId));
        } else {
          //file
          this.loadImageFile(this.allImageMap.get(this.currentSelectedFloorId));
        }
        //현재 층에 그린 도형들이 있다면
        if (myOnefloorSeatList) {
          for (let i = 0; i < myOnefloorSeatList.length; i++) {
            this.floorCanvas.add(myOnefloorSeatList[i]);
            console.log("myOnefloorSeatList : ");
            console.log(myOnefloorSeatList[i]);
          }

          eventBus.$emit("eachFloorSeatList", myOnefloorSeatList);
        }
      } else if (this.allImageMap.get(this.currentSelectedFloorId) == null) {
        //현재 층의 이미지가 저장되어있지 않다면
        //화면에 그려져있던 이미지와 도형 초기화
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            this.floorCanvas.remove(obj);
          });

        this.floorCanvas.backgroundImage = 0;
        this.floorCanvas.backgroundColor = "aliceblue";
        this.floorCanvas.renderAll();

        eventBus.$emit("eachFloorSeatList", myOnefloorSeatList);
      }
    },
    saveImageFile(file) {
      this.allImageMap.set(this.currentSelectedFloorId, file);
    },
    loadImageFile(file) {
      let reader = new FileReader();
      reader.onload = (e) => {
        fabric.Image.fromURL(e.target.result, (img) => {
          img.set({
            scaleX: this.floorCanvas.width / img.width,
            scaleY: this.floorCanvas.height / img.height,
          });
          this.floorCanvas.setBackgroundImage(
            img,
            this.floorCanvas.renderAll.bind(this.floorCanvas)
          );
        });
      };
      reader.readAsDataURL(file);
    },
    loadImageUrl(imgurl) {
      fabric.Image.fromURL(imgurl, (img) => {
        img.set({
          scaleX: this.floorCanvas.width / img.width,
          scaleY: this.floorCanvas.height / img.height,
        });
        this.floorCanvas.setBackgroundImage(
          img,
          this.floorCanvas.renderAll.bind(this.floorCanvas)
        );
      });
    },
    changeImageFile(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.saveImageFile(files[0]);
      this.loadImageFile(files[0]);
    },
    getColor(department) {
      const Colors = {
        Orange: "orange",
        Yellow: "yellow",
        Green: "green",
        Blue: "blue",
        Gray: "Gray",
      };
      if (department == "부서1") return Colors.Orange;
      else if (department == "부서2") return Colors.Yellow;
      else if (department == "부서3") return Colors.Green;
      else return Colors.Gray;
    },
    showSeat(seat) {
      console.log(seat.seat_id + "가 해당 자리의 아이디입니다.");
      let seatFloor = null;

      //seat의 층과 현재층이 같지 않다면
      if (this.currentSelectedFloorId != seat.floor_id) {
        //탭 전환 코드
        seatFloor = seat.floor_id;
      }
      //seat의 층과 현재층이 같다면
      else {
        seatFloor = this.currentSelectedFloorId;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(seatFloor);
      for (let i = 0; i < eachFloorSeatList.length; i++) {
        let myGroup = eachFloorSeatList[i];
        let asObject = myGroup.toObject([
          "employee_id",
          "floor_id",
          "seatId",
          "employee_department",
        ]);
        console.log(asObject.floor_id + "층에 자리가 있습니다.");

        let objectSeatId = asObject.seatId + "번";
        if (seat.seat_id == objectSeatId) {
          this.floorCanvas
            .getObjects()
            .slice()
            .forEach((obj) => {
              this.floorCanvas.remove(obj);
            });

          //각 층의 저장된 도형 리스트 화면에 뿌려주기
          //현재 층의 이미지가 저장되어있다면
          if (this.allImageMap.get(seatFloor) != null) {
            let typeCheck = this.allImageMap.get(this.currentSelectedFloorId);
            if (typeof typeCheck === "string") {
              //url
              this.loadImageUrl(
                this.allImageMap.get(this.currentSelectedFloorId)
              );
            } else {
              //file
              this.loadImageFile(
                this.allImageMap.get(this.currentSelectedFloorId)
              );
            }

            for (let i = 0; i < eachFloorSeatList.length; i++) {
              this.floorCanvas.add(eachFloorSeatList[i]);
            }
          }
          myGroup.item(0).set("opacity", 0);
          myGroup.item(0).set("stroke", "blue");
          myGroup.item(0).set("strokeWidth", 5);

          myGroup.item(0).animate("opacity", 1, {
            duration: 2000,
            onChange: this.floorCanvas.renderAll.bind(this.floorCanvas),
          });
          myGroup.item(0).animate("fill", "red", {
            onChange: this.floorCanvas.renderAll.bind(this.floorCanvas),
            duration: 2000,
            onComplete: getOrginItem,
          });

          let color = this.getColor(asObject.employee_department);
          function getOrginItem() {
            myGroup.item(0).set("opacity", 1);
            myGroup.item(0).set("fill", color);
            myGroup.item(0).set("stroke", null);
            myGroup.item(0).set("strokeWidth", null);
          }
        }
        //자리가 아직 없을때 예외처리 하기
      }
    },
    //각 층의 도형 리스트 반환하기
    getEachFloorSeatList: function (floor) {
      if (!floor) {
        // 초반에 층이 생성 안되었을때
        return;
      }
      //층에 해당하는 도형리스트가 만들어지지 않았을때 각 층의 도형 리스트 생성하기
      if (!this.allSeatMap.get(floor)) {
        let newSeatsList = new Array();
        this.allSeatMap.set(floor, newSeatsList);
        //console.log(this.allSeatMap.size + "allSeatMap 처음의 자리 맵 사이즈입니다");
        return this.allSeatMap.get(floor);
      } else {
        //console.log(this.allSeatMap.size + "allSeatMap 현재 자리 맵 사이즈입니다" );
        return this.allSeatMap.get(floor);
      }
    },
    getManagerEachFloorSeatList: function (floor) {
      if (!floor) {
        // 초반에 층이 생성 안되었을때
        return;
      }
      if (!this.managerAllSeatMap.get(floor)) {
        let newSeatsList = new Array();
        this.managerAllSeatMap.set(floor, newSeatsList);
        //console.log(this.managerAllSeatMap.size + "managerAllSeatMap 처음의 자리 맵 사이즈입니다");
        return this.managerAllSeatMap.get(floor);
      } else {
        //console.log(this.managerAllSeatMap.size + "managerAllSeatMap 현재 자리 맵 사이즈입니다" );
        return this.managerAllSeatMap.get(floor);
      }
    },
    //각 사원의 도형 리스트 반환하기
    getEachEmployeeSeatList: function (employee_id) {
      if (!this.eachEmployeeSeatMap.get(employee_id)) {
        let newEmployeeSeatList = new Array();
        this.eachEmployeeSeatMap.set(employee_id, newEmployeeSeatList);
        return this.eachEmployeeSeatMap.get(employee_id);
      } else {
        return this.eachEmployeeSeatMap.get(employee_id);
      }
    },
    //자리비우기(allFloorSeatMap에서 seatId 정보는 뺴지 않고 그대로 두기, 사원의 자리 리스트에서만 해당 seatId 제거하기)
    clickChangeToVacant() {
      let activeObject = null;
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      if (this.floorCanvas.getActiveObject().type == "group") {
        activeObject = this.floorCanvas.getActiveObject(); // 선택 객체 가져오기

        activeObject.set("modify", true);

        activeObject.employee_name = null;
        activeObject.employee_department = null;
        activeObject.employee_number = null;

        let groupToObject = activeObject.toObject(["seatId", "employee_id"]);
        console.log(groupToObject.seatId + "비우고자하는 자리의 SeatId입니다.");
        console.log(
          groupToObject.employee_id + "비우고자하는 자리의 employee_id입니다."
        );
        this.deleteEachEmployeeSeatList(groupToObject);

        activeObject.employee_id = null; // delete employee information in group

        activeObject
          .item(0)
          .set("fill", this.getColor(activeObject.employee_department));
        this.floorCanvas.remove(activeObject.item(1)); // delete textObject
        activeObject.item(1).set("text", "");
        //activeObject._objects[1].text = "";
        this.floorCanvas.renderAll();
      }
      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    //해당 층의 도형 리스트 전체 삭제하기
    deleteEachFloorSeatList: function (floor) {
      this.getEachFloorSeatList(floor).length = 0;
      return this.getEachFloorSeatList(floor);
    },
    // 해당 층의 도형 리스트의 Delete field 전체 true 만들기
    deleteManagerEachFloorSeatList: function (floor) {
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(floor);
      console.log(managerEachFloorSeatList);
      for (let i = 0; i < managerEachFloorSeatList.length; i++) {
        managerEachFloorSeatList[i].delete = true;
      }
    },
    //사원의 자리리스트에서 삭제된 자리를 삭제하기
    deleteEachEmployeeSeatList: function (groupToObject) {
      let oneEmployeeSeatList = this.getEachEmployeeSeatList(
        groupToObject.employee_id
      );
      for (let i = 0; i < oneEmployeeSeatList.length; i++) {
        if (oneEmployeeSeatList[i].seatId == groupToObject.seatId) {
          oneEmployeeSeatList.splice(i, 1);
        }
      }
      console.log(
        oneEmployeeSeatList.length + "전체 삭제한 자리 리스트 길이입니다."
      );
    },
    deleteAllBtn() {
      // 전체 삭제
      if (confirm("Are you sure?")) {
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            let groupToObject = obj.toObject(["seatId", "employee_id"]);
            this.deleteEachEmployeeSeatList(groupToObject);
            this.floorCanvas.remove(obj);
          });

        let eachFloorSeatList = this.deleteEachFloorSeatList(
          this.currentSelectedFloorId
        );
        this.deleteManagerEachFloorSeatList(this.currentSelectedFloorId);
        eventBus.$emit(
          "eachFloorSeatList",
          this.getEachFloorSeatList(this.currentSelectedFloorId)
        );
        eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
      }
    },
    deleteBtn() {
      //좌석 지우면 list에 있는거 없애기
      let activeObject = null;
      let eachFloorSeatList = this.deleteEachFloorSeatList(
        this.currentSelectedFloorId
      );

      let shapearray = new Array();

      if (confirm("Are you sure?")) {
        if (this.floorCanvas.getActiveObjects().length == 1) {
          // 단일객체
          activeObject = this.floorCanvas.getActiveObject();
          activeObject.set("delete", true);

          let groupToObject = activeObject.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
        } else {
          // 복수객체
          activeObject = this.floorCanvas.getActiveObjects();
          //activeObject.set("delete", true);

          for (let i = 0; i < activeObject.length; i++) {
            let groupToObject = activeObject[i].toObject([
              "seatId",
              "employee_id",
              "delete",
            ]);
            activeObject[i].set("delete", true);
            this.deleteEachEmployeeSeatList(groupToObject);
          }
          activeObject = this.floorCanvas.getActiveObject().toGroup();
        }

        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            shapearray.push(obj);
          });

        if (activeObject) {
          //층의 자리 리스트에서 제거하기
          shapearray.slice().forEach((obj) => {
            if (obj == activeObject) {
              let groupToObject = activeObject.toObject(["seatId"]);
              let index = shapearray.indexOf(activeObject);
              shapearray.splice(index, 1);
            }
          });
          this.floorCanvas.remove(activeObject);
          eachFloorSeatList.length = 0;
          this.allSeatMap.set(this.currentSelectedFloorId, shapearray);

          eventBus.$emit(
            "eachFloorSeatList",
            this.getEachFloorSeatList(this.currentSelectedFloorId)
          );
          eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
        }
      }
    },
    setMappingSeat(item) {
      // 공석 또는 사원이 매핑된 좌석에 사원 매핑
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);

      let activeObject = this.floorCanvas.getActiveObject(); //group 객체

      //해당 자리가 사원이 매핑되어있는 상태에서 다른 사원으로 변경하고자 하는 경우
      if (
        activeObject.employee_id != null &&
        activeObject.employee_id != item.employee_id
      ) {
        if (
          confirm(
            activeObject.employee_name +
              "사원의 자리를 " +
              item.name +
              "자리로 변경하시겠습니까?"
          )
        ) {
          let groupToObject = activeObject.toObject([
            "seatId",
            "employee_id",
            "floor_id",
          ]);
          activeObject.set("modify", true);
          this.deleteEachEmployeeSeatList(groupToObject);
        } else {
          return;
        }
      }
      //해당 자리가 사원이 매핑되어있는 상태에서 같은 사원으로 매핑을 한번더 하려고 하는 경우
      else if (
        activeObject.employee_id != null &&
        activeObject.employee_id == item.employee_id
      ) {
        alert("이 자리는 이미 " + item.name + "의 자리입니다.");
        return;
      }

      //해당 자리가 공석이라면 바로 매핑 가능
      activeObject.employee_name = item.name;
      activeObject.employee_department = item.department;
      activeObject.employee_number = item.number;
      activeObject.employee_id = item.employee_id;
      activeObject
        .item(0)
        .set("fill", this.getColor(activeObject.employee_department));
      activeObject.item(1).set("text", item.name);
      activeObject.set("modify", true);
      this.floorCanvas.renderAll();

      eachEmployeeSeatList.push(activeObject);

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    addVacantSeat(posX, posY) {
      let x = posX;
      let y = posY;

      if (!this.allImageMap.get(this.currentSelectedFloorId)) {
        alert("도면 이미지가 없습니다");
        console.log(this.getEachFloorSeatList(this.currentSelectedFloorId));
        return;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
        this.currentSelectedFloorId
      );

      let seatId = this.createSeatUUID();

      let rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: this.getColor(null),
        opacity: 1,
      });

      let textObject = new fabric.IText("", {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 13,
        fill: "black",
      });

      let group = new fabric.Group([rectangle, textObject], {
        seatId: seatId,
        floor_id: this.currentSelectedFloorId,
        employee_name: null,
        employee_department: null,
        employee_number: null,
        employee_id: null,
        left: x,
        top: y,
        angle: 0,
        create: true, //생성
        modify: false, //변경
        delete: false, //삭제
      });

      group.on("mousedown", (e) => {
        let group = e.target;
        if (e.button === 2) {
          let groupToObject = group.toObject([
            "employee_id",
            "employee_name",
            "employee_department",
          ]);
          eventBus.$emit("employee_id", groupToObject.employee_id);
          eventBus.$emit("employee_name", groupToObject.employee_name);
          eventBus.$emit("floor_name", this.currentSelectedFloorName);
          eventBus.$emit(
            "employee_department",
            groupToObject.employee_department
          );
          this.getEmployeeDialog();
        }
      });

      group.on("mousedblclick", (e) => {
        let evt = e.e;
        if (evt.ctrlKey === true) {
          console.log("ctrlKey");
          this.getInputSeatNameDialog();
        } else {
          this.getChangeSeatDialog(); // 자리 이동 dialog
        }
      });

      this.floorCanvas.on("object:scaling", (e) => {
        let scaledObject = e.target;
        let width = scaledObject.getScaledWidth() / scaledObject.scaleX;
        let height = scaledObject.getScaledHeight() / scaledObject.scaleY;

        scaledObject.width = width;
        scaledObject.height = height;

        let groupx = scaledObject.toObject([
          "width",
          "height",
          "scaleX",
          "scaleY",
        ]);
      });

      this.floorCanvas.add(group);
      eachFloorSeatList.push(group);
      managerEachFloorSeatList.push(group);
      this.floorCanvas.renderAll();

      console.log("전체층의 가시석 자리 맵 size = " + this.allSeatMap.size);
      console.log(
        "전체층의 관리 자리 맵 size = " + this.managerAllSeatMap.size
      );
      console.log(
        this.currentSelectedFloorId +
          "의 자리 리스트 length = " +
          eachFloorSeatList.length
      );
      console.log(
        this.currentSelectedFloorId +
          "의 자리 리스트 length = " +
          managerEachFloorSeatList.length
      );

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
    },

    clickSaveBtn() {
      if (this.managerFloorList) {
        //층 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          if (!this.managerFloorList[i].create) {
            // 원본
            if (this.managerFloorList[i].delete) {
              // 001 011 delete
              let deleteFloorKey = this.managerFloorList[i].floor_id;
              this.$emit("deleteFloorWithKey", "floors", deleteFloorKey);
            } else if (this.managerFloorList[i].modify) {
              //010 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveFloors", "floors", floorData);
            }
          } else {
            // front에서 생성
            if (this.managerFloorList[i].delete) {
              //101 111 nothing
              return;
            } else {
              //100 110 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveFloors", "floors", floorData);
            }
          }
        }

        //이미지 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          let imgData = new FormData();
          let floorid = this.managerFloorList[i].floor_id;
          let file = this.allImageMap.get(floorid);
          if (file != null) {
            imgData.append("imageFile", file);
            this.$emit("saveImages", "images", imgData, floorid);
          }
        }

        //자리 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
            this.managerFloorList[i].floor_id
          );

          if (managerEachFloorSeatList.length > 0) {
            console.log(
              managerEachFloorSeatList.length +
                this.managerFloorList[i].floor_id +
                "층의 자리 개수입니다."
            );

            for (let j = 0; j < managerEachFloorSeatList.length; j++) {
              let groupToObject = managerEachFloorSeatList[j].toObject([
                "seatId",
                "seatName",
                "floor_id",
                "left",
                "top",
                "employee_department",
                "employee_id",
                "width",
                "height",
                "scaleX",
                "scaleY",
                "create",
                "modify",
                "delete",
              ]);

              console.log(groupToObject);
              //axios api 호출
              if (!groupToObject.create) {
                // 원본
                if (groupToObject.delete) {
                  // 001 011 delete
                  let deleteSeatKey = groupToObject.seatId;
                  let deleteSeatFloor = groupToObject.floor_id;
                  this.$emit(
                    "deleteSeatWithKey",
                    "seats",
                    deleteSeatKey,
                    deleteSeatFloor
                  );
                } else if (groupToObject.modify) {
                  //010 그 id에 대하여 post
                  let seatData = {};
                  seatData.seat_id = groupToObject.seatId;
                  seatData.seat_name = groupToObject.seatName;
                  seatData.floor = groupToObject.floor_id;
                  seatData.x = groupToObject.left;
                  seatData.y = groupToObject.top;
                  seatData.is_group = false;
                  seatData.group_id = null;
                  seatData.building_id = "HANCOM01";
                  seatData.employee_id = groupToObject.employee_id;
                  seatData.width = groupToObject.width * groupToObject.scaleX;
                  seatData.height = groupToObject.height * groupToObject.scaleY;
                  seatData.degree = groupToObject.angle;
                  seatData.shape_id = "1";

                  console.log(seatData);
                  this.$emit("saveSeats", "seats", seatData, seatData.floor);
                }
              } else {
                // front에서 생성
                if (groupToObject.true) {
                  //101 111 nothing
                  return;
                } else {
                  //100 110 그 id에 대하여 post
                  let seatData = {};
                  seatData.seat_id = groupToObject.seatId;
                  seatData.seat_name = groupToObject.seatName;
                  seatData.floor = groupToObject.floor_id;
                  seatData.x = groupToObject.left;
                  seatData.y = groupToObject.top;
                  seatData.is_group = false;
                  seatData.group_id = null;
                  seatData.building_id = "HANCOM01";
                  seatData.employee_id = groupToObject.employee_id;
                  seatData.width = groupToObject.width * groupToObject.scaleX;
                  seatData.height = groupToObject.height * groupToObject.scaleY;
                  seatData.degree = groupToObject.angle;
                  seatData.shape_id = "1";

                  console.log(seatData);
                  this.$emit("saveSeats", "seats", seatData, seatData.floor);
                }
              }
            }
          }
        }
      }
    },
    getEmployeeObject(employee_id) {
      // seat table의 employee_id를 받으면 그에 맞는 정보 알아오기 위함
      let employeeInfoList = new Array();
      let employeeObject = {}; // return 될 Object
      for (let i = 0; i < this.employees.length; i++) {
        let employee = {};
        employee.name = this.employees[i].name;
        employee.department = this.employees[i].department;
        employee.number = this.employees[i].number;
        employee.employee_id = this.employees[i].employee_id;

        employeeInfoList.push(employee);
      }

      if (employee_id == null) {
        // 공석
        let employee = {};
        employee.name = null;
        employee.department = null;
        employee.number = null;

        employeeObject = employee;
      } else {
        // 매핑된 자리
        for (let i = 0; i < employeeInfoList.length; i++) {
          if (employee_id == employeeInfoList[i].employee_id) {
            employeeObject = employeeInfoList[i];
          }
        }
      }

      console.log(employeeObject);
      return employeeObject; // return 받아서 department, name, number 뽑아쓰기
    },
    makeGroupInfo(seat) {
      let employee = this.getEmployeeObject(seat.employee_id);

      let rectangle = new fabric.Rect({
        width: seat.width,
        height: seat.height,
        fill: this.getColor(employee.department),
        opacity: 1,
      });

      let textObject = null;
      if (seat.employee_id == null) {
        textObject = new fabric.IText("", {
          left: 0,
          top: rectangle.height / 3,
          fontSize: 15,
          fill: "black",
        });
      } else {
        textObject = new fabric.IText(employee.name, {
          left: 0,
          top: rectangle.height / 3,
          fontSize: 15 * rectangle.scaleX,
          fill: "black",
        });
      }

      let group = new fabric.Group([rectangle, textObject], {
        seatId: seat.seat_id,
        employee_name: employee.name,
        employee_department: employee.department,
        employee_number: employee.number,
        employee_id: seat.employee_id,
        floor_id: seat.floor,
        left: seat.x,
        top: seat.y,
        angle: seat.degree,
        create: seat.create,
        modify: seat.modify,
        delete: seat.delete,
      });

      if (seat.seat_name != null) {
        // seat_name is not null
        let seatNameObject = new fabric.IText(seat.seat_name, {
          left: rectangle.left,
          top: rectangle.top - 15,
          fontSize: 15,
          fill: "black",
        });

        group.add(seatNameObject);
      }

      group.on("mousedown", (e) => {
        let group = e.target;
        if (e.button === 2) {
          let groupToObject = group.toObject([
            "employee_id",
            "employee_name",
            "employee_department",
          ]);
          eventBus.$emit("employee_id", groupToObject.employee_id);
          eventBus.$emit("employee_name", groupToObject.employee_name);
          eventBus.$emit("floor_name", this.currentSelectedFloorName);
          eventBus.$emit(
            "employee_department",
            groupToObject.employee_department
          );
          this.getEmployeeDialog();
        }
      });

      group.on("mousedblclick", (e) => {
        let evt = e.e;
        if (evt.ctrlKey === true) {
          console.log("ctrlKey");
          this.getInputSeatNameDialog();
        } else {
          this.getChangeSeatDialog(); // 자리 이동 dialog
        }
      });

      return group;
    },

    clickLoadCurrentFloor() {
      //현재 층 이미지 로드
      for (let i = 0; i < this.currentFloorImageFromDb.length; i++) {
        let imgurl = this.currentFloorImageFromDb[i].url;
        let floorid = this.currentFloorImageFromDb[i].floorid;
        this.allImageMap.set(floorid, imgurl);

        this.loadImageUrl(imgurl);

        // 현재층 자리 로드
        if (this.currentFloorSeatListFromDb.length) {
          for (let i = 0; i < this.currentFloorSeatListFromDb.length; i++) {
            console.log(
              "현재층의 자리 개수는 ------> " +
                this.currentFloorSeatListFromDb.length
            );
            this.currentSelectedFloorId = this.currentFloorSeatListFromDb[
              i
            ].floor;

            let eachFloorSeatList = this.getEachFloorSeatList(
              this.currentFloorSeatListFromDb[i].floor
            );
            let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
              this.currentFloorSeatListFromDb[i].floor
            );
            let eachEmployeeSeatList = this.getEachEmployeeSeatList(
              this.currentFloorSeatListFromDb[i].employee_id
            );

            let group = this.makeGroupInfo(this.currentFloorSeatListFromDb[i]);

            this.floorCanvas.add(group);
            eachFloorSeatList.push(group);
            managerEachFloorSeatList.push(group);
            eachEmployeeSeatList.push(group);

            eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
            eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
            console.log(
              this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
            );
            console.log(
              this.currentFloorSeatListFromDb[i].employee_id +
                "의 자리 리스트 개수는 " +
                this.getEachEmployeeSeatList(
                  this.currentFloorSeatListFromDb[i].employee_id
                ).length +
                "입니다."
            );
          }
        } else {
          console.log(floorid);
          this.currentSelectedFloorId = floorid;
        }
      }

      this.clickLoadOtherFloors();
    },
    clickLoadOtherFloors() {
      //다른 층 이미지 로드
      for (let i = 0; i < this.otherFloorImageFromDb.length; i++) {
        let imgurl = this.otherFloorImageFromDb[i].url;
        let floorid = this.otherFloorImageFromDb[i].floorid;
        this.allImageMap.set(floorid, imgurl);
      }

      console.log(this.otherFloorSeatListFromDb);
      //다른 층 자리 로드
      if (this.otherFloorSeatListFromDb) {
        let keys = new Array();
        keys = Array.from(this.otherFloorSeatListFromDb.keys());

        for (let i = 0; i < keys.length; i++) {
          //2
          let seats = new Array();
          console.log(
            "나머지 층 개수는 ------> " + this.otherFloorSeatListFromDb.size
          );
          seats = this.otherFloorSeatListFromDb.get(keys[i]);
          console.log("!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~");
          console.log(keys[i] + seats);
          for (let j = 0; j < seats.length; j++) {
            let eachFloorSeatList = this.getEachFloorSeatList(seats[j].floor);
            let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
              seats[j].floor
            );
            let eachEmployeeSeatList = this.getEachEmployeeSeatList(
              seats[j].employee_id
            );

            let group = this.makeGroupInfo(seats[j]);
            console.log(group);
            eachFloorSeatList.push(group);
            managerEachFloorSeatList.push(group);
            eachEmployeeSeatList.push(group);
            // eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
            // eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
            console.log(
              this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
            );
            console.log(
              seats[j].employee_id +
                "의 자리 리스트 개수는 " +
                this.getEachEmployeeSeatList(seats[j].employee_id).length +
                "입니다."
            );
          }
        }
      }
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
